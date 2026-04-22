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
