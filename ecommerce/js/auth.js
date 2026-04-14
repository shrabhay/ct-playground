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

// ─── Run everything on DOM ready ───
function initSharedNav() {
  initAuthNav();
  initWishlistCount();
  initCartCount();
}

if (document.readyState === 'loading') {
  document.addEventListener('DOMContentLoaded', initSharedNav);
} else {
  initSharedNav();
}

// ── CT Web Pop-up message handler ──────────────────────────────
// Listens for postMessage events from CT pop-up iframes
// and fires CT events + profile updates on the parent page
window.addEventListener('message', function(e) {
  if (!e.data || e.data.type !== 'bz_popup') return;

  var action  = e.data.action;
  var payload = e.data.payload || {};
  var epoch   = "$D_" + Math.floor(Date.now() / 1000);

  if (action === 'viewed') {
    if (typeof clevertap !== 'undefined') {
      clevertap.event.push("Web Popup Viewed", payload);
      clevertap.profile.push({
        "Site": {
          "Last Popup Seen":    payload.campaign_name,
          "Last Popup Seen At": epoch
        }
      });
    }
  }

  if (action === 'clicked') {
    if (typeof clevertap !== 'undefined') {
      clevertap.event.push("Web Popup Clicked", Object.assign({}, payload, {
        cta_text:   "Shop Now & Save 20%",
        cta_action: "shop_now"
      }));
      clevertap.profile.push({
        "Site": {
          "Last Popup Clicked":    payload.campaign_name,
          "Last Popup Clicked At": epoch
        }
      });
    }
    setTimeout(function() {
      window.location.href = 'index.html';
    }, 300);
  }

  if (action === 'dismissed') {
    if (typeof clevertap !== 'undefined') {
      clevertap.event.push("Web Popup Dismissed", Object.assign({}, payload, {
        dismiss_action: "close_button"
      }));
      clevertap.profile.push({
        "Site": {
          "Last Popup Dismissed":    payload.campaign_name,
          "Last Popup Dismissed At": epoch
        }
      });
    }
  }

  if (action === 'close') {
    var overlay = document.getElementById('intentPreview');
    if (overlay) overlay.style.display = 'none';
  }
});
