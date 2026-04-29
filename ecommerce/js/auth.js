// =============================================================================
// auth.js — Shared auth state + navbar counts for all Bazario pages
// =============================================================================

function getUser() {
  return JSON.parse(localStorage.getItem('bazario_user') || 'null');
}

function isLoggedIn() {
  return !!getUser();
}

// ─── Update navbar login state ───
function initAuthNav() {
  const user     = getUser();
  const loginBtn = document.querySelector('.nav-btn-login');
  if (!loginBtn) return;

  if (user) {
    const firstName = user.name ? user.name.split(' ')[0] : 'Account';
    loginBtn.outerHTML = `
      <div style="display:flex;align-items:center;gap:4px">
        <button
          onclick="window.location.href='account.html'"
          style="display:flex;flex-direction:column;align-items:center;padding:6px 14px;
                 background:rgba(255,255,255,0.15);border:none;border-radius:4px;
                 cursor:pointer;gap:2px;transition:background 0.15s;"
          onmouseover="this.style.background='rgba(255,255,255,0.25)'"
          onmouseout="this.style.background='rgba(255,255,255,0.15)'">
          <span style="font-size:11px;color:rgba(255,255,255,0.8)">Hello,</span>
          <span style="font-size:13px;font-weight:600;color:#fff;">👤 ${firstName}</span>
        </button>
      </div>
    `;
  }
}

// ─── Update wishlist count on every page ───
function initWishlistCount() {
  const el = document.getElementById('wishlistCount');
  if (!el) return;
  const user     = JSON.parse(localStorage.getItem('bazario_user') || 'null');
  const wishKey  = user ? `bazario_wishlist_${user.email}` : 'bazario_wishlist_guest';
  const wishlist = JSON.parse(localStorage.getItem(wishKey) || '[]');
  el.textContent = wishlist.length;
}

// ─── Update cart count on every page ───
function initCartCount() {
  const el = document.getElementById('cartCount');
  if (!el) return;
  const user    = JSON.parse(localStorage.getItem('bazario_user') || 'null');
  const cartKey = user ? `bazario_cart_${user.email}` : 'bazario_cart_guest';
  const cart    = JSON.parse(localStorage.getItem(cartKey) || '[]');
  const total   = cart.reduce((s, i) => s + i.qty, 0);
  el.textContent = total;
}

// ── RC: Refresh values for current user on every page load ──
function refreshRCForUser() {
  var user = getUser();
  if (!user || !user.email) return;
  if (typeof clevertap.fetchVariables !== 'function') {
    setTimeout(refreshRCForUser, 500);
    return;
  }
  clevertap.fetchVariables(function() {
    var wzrkPE = localStorage.getItem('WZRK_PE');
    if (wzrkPE) {
      localStorage.setItem('WZRK_PE_' + user.email, wzrkPE);
    }
  });
}

function initSharedNav() {
  initAuthNav();
  initWishlistCount();
  initCartCount();
  refreshRCForUser();
  applyGlobalRC();
}

if (document.readyState === 'loading') {
  document.addEventListener('DOMContentLoaded', initSharedNav);
} else {
  initSharedNav();
}

// ── CT Web Pop-up message handler ──────────────────────────────
// Listens for postMessage events from CT pop-up iframes
// and fires CT events + profile updates on the parent page
// Redirect URLs and text are campaign-driven via payload — no hardcoding here

var _bzLastProcessed = {};

window.addEventListener('message', function(e) {
  if (!e.data || e.data.type !== 'bz_popup') return;

  // ── Deduplicate — ignore same action+campaign within 1 second ──
  var dedupeKey = e.data.action + '_' + (e.data.payload?.campaign_id || '');
  var now       = Date.now();
  if (_bzLastProcessed[dedupeKey] && now - _bzLastProcessed[dedupeKey] < 1000) return;
  _bzLastProcessed[dedupeKey] = now;

  console.log('BZ_POPUP message received:', e.data);

  var action  = e.data.action;
  var payload = e.data.payload || {};
  var epoch   = "$D_" + Math.floor(Date.now() / 1000);

  // Strip handler-only routing fields — these are for auth.js only, not CT events
  var ctPayload = Object.assign({}, payload);
  delete ctPayload.cta_url;
  delete ctPayload.cta_text;
  delete ctPayload.cta_action;
  delete ctPayload.dismiss_url;
  delete ctPayload.dismiss_text;

  // ── Helper: redirect only if a url is provided ──
  function maybeRedirect(url) {
    if (url) {
      setTimeout(function() {
        window.location.href = url;
      }, 300);
    }
  }

  if (action === 'viewed') {
    if (typeof clevertap !== 'undefined') {
      clevertap.event.push("Web Popup Viewed", ctPayload);
      clevertap.profile.push({
        "Site": {
          "Last Popup Seen":    payload.campaign_name,
          "Last Popup Seen At": epoch
        }
      });
    }
  }

  if (action === 'clicked') {
    console.log('clicked — cta_url is:', payload.cta_url);
    if (typeof clevertap !== 'undefined') {
      clevertap.event.push("Web Popup Clicked", Object.assign({}, ctPayload, {
        cta_text:   payload.cta_text   || "CTA Clicked",
        cta_action: payload.cta_action || "cta_click"
      }));
      clevertap.profile.push({
        "Site": {
          "Last Popup Clicked":    payload.campaign_name,
          "Last Popup Clicked At": epoch
        }
      });
    }
    maybeRedirect(payload.cta_url);
  }

  if (action === 'nothanks') {
    if (typeof clevertap !== 'undefined') {
      clevertap.event.push("Web Popup Dismissed", Object.assign({}, ctPayload, {
        dismiss_action: "no_thanks_link",
        dismiss_text:   payload.dismiss_text || "No thanks"
      }));
      clevertap.profile.push({
        "Site": {
          "Last Popup Dismissed":    payload.campaign_name,
          "Last Popup Dismissed At": epoch
        }
      });
    }
    maybeRedirect(payload.dismiss_url);
  }

  if (action === 'dismissed') {
    if (typeof clevertap !== 'undefined') {
      clevertap.event.push("Web Popup Dismissed", Object.assign({}, ctPayload, {
        dismiss_action: "close_button"
      }));
      clevertap.profile.push({
        "Site": {
          "Last Popup Dismissed":    payload.campaign_name,
          "Last Popup Dismissed At": epoch
        }
      });
    }
    maybeRedirect(payload.dismiss_url);
  }

  if (action === 'close') {
    function removeCtOverlay(attempts) {
      var overlay = document.getElementById('intentPreview');
      if (overlay) {
        overlay.parentElement.removeChild(overlay);
      } else if (attempts > 0) {
        setTimeout(function() { removeCtOverlay(attempts - 1); }, 100);
      }
    }
    removeCtOverlay(5);
  }
});

// ─── GLOBAL RC — Apply across all pages ──────────────────────────────────────
function applyGlobalRC() {
  try {
    var user = JSON.parse(localStorage.getItem('bazario_user') || 'null');
    if (!user || !user.email) return;
    var wzrkPE = localStorage.getItem('WZRK_PE_' + user.email);
    if (!wzrkPE) return;
    var vars = JSON.parse(decodeURIComponent(wzrkPE));

    // 1. Maintenance mode — show full page overlay
    if (vars['Bazario.Global.global_maintenance_mode'] === true) {
      var overlay = document.createElement('div');
      overlay.id  = 'maintenance-overlay';
      overlay.style.cssText = `
        position: fixed;
        inset: 0;
        background: #fff;
        z-index: 999999;
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: center;
        font-family: 'Inter', sans-serif;
        text-align: center;
        padding: 24px;
      `;
      overlay.innerHTML = `
        <div style="font-size:64px;margin-bottom:16px;">🔧</div>
        <div style="font-size:28px;font-weight:700;color:#1a1a2e;margin-bottom:8px;">
          We'll be back soon!
        </div>
        <div style="font-size:15px;color:#666;line-height:1.7;max-width:480px;margin-bottom:24px;">
          Bazario is currently undergoing scheduled maintenance.<br/>
          We apologize for the inconvenience. Please check back shortly.
        </div>
        <div style="background:#f0fdf4;border:1px solid #22c55e;border-radius:8px;
          padding:12px 24px;font-size:13px;color:#16a34a;font-weight:600;">
          🕐 Expected back in 30 minutes
        </div>
        <div style="margin-top:24px;font-size:13px;color:#999;">
          Need help? Contact us at 
          <a href="tel:${vars['Bazario.Global.global_support_phone'] || '1800-123-4567'}" 
             style="color:#2874f0;font-weight:600;">
            ${vars['Bazario.Global.global_support_phone'] || '1800-123-4567'}
          </a>
        </div>
      `;
      document.body.appendChild(overlay);

      // CT Event
      ctEvent("Maintenance Mode Viewed", {
        page:     window.location.pathname,
        vertical: "bazario"
      });
      return; // Stop further RC processing
    }

    // 2. App download banner
    if (vars['Bazario.Global.global_show_app_download_banner'] === true) {
      // Don't show if already dismissed this session
      if (sessionStorage.getItem('bz_app_banner_dismissed')) return;

      var appBanner = document.createElement('div');
      appBanner.id  = 'app-download-banner';
      appBanner.style.cssText = `
        position: fixed;
        bottom: 0;
        left: 0;
        right: 0;
        background: linear-gradient(135deg, #1a237e, #1565c0);
        color: #fff;
        padding: 12px 20px;
        display: flex;
        align-items: center;
        justify-content: space-between;
        z-index: 9998;
        font-family: 'Inter', sans-serif;
        box-shadow: 0 -4px 16px rgba(0,0,0,0.15);
      `;
      appBanner.innerHTML = `
        <div style="display:flex;align-items:center;gap:12px;">
          <span style="font-size:28px;">📱</span>
          <div>
            <div style="font-size:13px;font-weight:700;">Get the Bazario App</div>
            <div style="font-size:11px;color:rgba(255,255,255,0.8);">
              Exclusive app-only deals & faster checkout
            </div>
          </div>
        </div>
        <div style="display:flex;align-items:center;gap:10px;">
          <button onclick="handleAppDownload('android')" style="
            background:#ff6f00;color:#fff;border:none;border-radius:6px;
            padding:8px 14px;font-size:12px;font-weight:700;cursor:pointer;">
            Android
          </button>
          <button onclick="handleAppDownload('ios')" style="
            background:#fff;color:#1565c0;border:none;border-radius:6px;
            padding:8px 14px;font-size:12px;font-weight:700;cursor:pointer;">
            iOS
          </button>
          <button onclick="dismissAppBanner()" style="
            background:none;border:none;color:rgba(255,255,255,0.7);
            font-size:18px;cursor:pointer;padding:4px 8px;">
            ✕
          </button>
        </div>
      `;
      document.body.appendChild(appBanner);

      // CT Event — App Banner Viewed
      ctEvent("App Download Banner Viewed", {
        page:     window.location.pathname,
        vertical: "bazario"
      });
    }

    // 3. Support phone — update all tel links in footer
    if (vars['Bazario.Global.global_support_phone']) {
      var phone = vars['Bazario.Global.global_support_phone'];
      document.querySelectorAll('a[href^="tel:"]').forEach(function(el) {
        el.href        = 'tel:' + phone;
        el.textContent = phone;
      });
      // Also update any element with class support-phone
      document.querySelectorAll('.support-phone').forEach(function(el) {
        el.textContent = phone;
      });
    }

  } catch(e) {
    console.warn('Global RC apply failed:', e);
  }
}

// ─── APP BANNER HELPERS ───────────────────────────────────────────────────────
function handleAppDownload(platform) {
  ctEvent("App Download Clicked", {
    platform: platform,
    page:     window.location.pathname,
    vertical: "bazario"
  });
  showToast('📱 Redirecting to ' + (platform === 'android' ? 'Play Store' : 'App Store') + '...');
}

function dismissAppBanner() {
  var banner = document.getElementById('app-download-banner');
  if (banner) banner.remove();
  sessionStorage.setItem('bz_app_banner_dismissed', '1');
  ctEvent("App Download Banner Dismissed", {
    page:     window.location.pathname,
    vertical: "bazario"
  });
}
