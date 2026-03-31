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
  const wishlist = JSON.parse(localStorage.getItem('bazario_wishlist') || '[]');
  el.textContent = wishlist.length;
}

// ─── Update cart count on every page ───
function initCartCount() {
  const el = document.getElementById('cartCount');
  if (!el) return;
  const cart  = JSON.parse(localStorage.getItem('bazario_cart') || '[]');
  const total = cart.reduce((s, i) => s + i.qty, 0);
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