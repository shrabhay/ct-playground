// =============================================================================
// auth.js — Shared auth state + navbar counts for all Bazario pages
// Phase 7: Migrated to Firebase Auth + Firestore
// =============================================================================

// ─── SYNCHRONOUS HELPER (uses localStorage cache) ────────────────────────────
// Safe to call synchronously — returns cached profile for quick access
function getUser() {
  return JSON.parse(localStorage.getItem('bazario_user') || 'null');
}

// ─── FIREBASE AUTH STATE LISTENER ────────────────────────────────────────────
// Runs automatically on every page load — initialises navbar, counts, CT
fbAuth.onAuthStateChanged(async (firebaseUser) => {
  if (firebaseUser) {
    // Always re-read from Firestore — ensures cross-platform updates
    // (Android, iOS) are immediately reflected on web.
    let profile = await fbGetProfile(firebaseUser.uid);
    if (profile) {
      localStorage.setItem('bazario_user', JSON.stringify(profile));
    } else {
      // Firestore failed — fall back to localStorage cache
      profile = JSON.parse(localStorage.getItem('bazario_user') || 'null');
    }
    if (profile) {
      initAuthNav(profile);
      await initCartCount();
      await initWishlistCount();
    }
  } else {
    initAuthNav(null);
    initCartCount();
    initWishlistCount();
  }
});

// ─── NAVBAR LOGIN STATE ───────────────────────────────────────────────────────
function initAuthNav(user) {
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

// ─── CART COUNT ───────────────────────────────────────────────────────────────
async function initCartCount() {
  const el = document.getElementById('cartCount');
  if (!el) return;

  const uid = fbAuth.currentUser?.uid;
  if (uid) {
    const cart = await fbGetCart(uid);
    el.textContent = cart.reduce((s, i) => s + (i.qty || 1), 0);
  } else {
    const guestCart = JSON.parse(localStorage.getItem('bazario_cart_guest') || '[]');
    el.textContent = guestCart.reduce((s, i) => s + (i.qty || 1), 0);
  }
}

// ─── WISHLIST COUNT ───────────────────────────────────────────────────────────
async function initWishlistCount() {
  const el = document.getElementById('wishlistCount');
  if (!el) return;

  const uid = fbAuth.currentUser?.uid;
  if (uid) {
    const wishlist = await fbGetWishlist(uid);
    el.textContent = wishlist.length;
  } else {
    const guestWishlist = JSON.parse(localStorage.getItem('bazario_wishlist_guest') || '[]');
    el.textContent = guestWishlist.length;
  }
}

// ─── CT IDENTITY HELPER ───────────────────────────────────────────────────────
// Called from account.html after login/signup
function ctIdentifyUser(user) {
  clevertap.onUserLogin.push({
    "Site": {
      "Name":     user.name    || '',
      "Email":    user.email   || '',
      "Phone":    user.phone   || '',
      "Gender":   user.gender === 'Male' ? 'M' : user.gender === 'Female' ? 'F' : '',
      "City":     user.city    || '',
      "Is Prime": user.isPrime || false,
      "Vertical": "Bazario"
    }
  });
}

// ─── CART KEY HELPERS (used by pages that still manage cart locally) ──────────
// Returns the Firestore uid if logged in, else 'guest'
function getCartKey() {
  const user = getUser();
  return user ? `bazario_cart_${user.email}` : 'bazario_cart_guest';
}

function getWishlistKey() {
  const user = getUser();
  return user ? `bazario_wishlist_${user.email}` : 'bazario_wishlist_guest';
}

// ─── CT WEB POP-UP HANDLER ────────────────────────────────────────────────────
// Listens for postMessage from CT popup iframes on all Bazario pages
// Fires CT events and kills overlay via navigation (CT MutationObserver
// overrides any style-based attempts — redirect is the only reliable approach)

window.addEventListener('message', function(e) {
  if (!e.data || e.data.type !== 'bz_popup') return;

  var action   = e.data.action;
  var payload  = e.data.payload || {};
  var campaign = payload.campaign_name || '';

  // ── Fire CT events ──
  if (action === 'viewed') {
    clevertap.event.push('Web Popup Viewed', {
      campaign_name: campaign,
      vertical:      'bazario'
    });
  }

  if (action === 'clicked') {
    clevertap.event.push('Web Popup Clicked', {
      campaign_name: campaign,
      vertical:      'bazario'
    });
    clevertap.profile.push({ 'Site': { 'Last Popup Clicked': campaign } });
  }

  if (action === 'nothanks') {
    clevertap.event.push('Web Popup Dismissed', {
      campaign_name:  campaign,
      dismiss_action: 'no_thanks_link',
      vertical:       'bazario'
    });
    clevertap.profile.push({ 'Site': { 'Last Popup Dismissed': campaign } });
  }

  if (action === 'dismissed') {
    clevertap.event.push('Web Popup Dismissed', {
      campaign_name:  campaign,
      dismiss_action: 'close_button',
      vertical:       'bazario'
    });
    clevertap.profile.push({ 'Site': { 'Last Popup Dismissed': campaign } });
  }

  // ── Kill overlay on close ──
  // 'close' always fires after clicked/nothanks/dismissed.
  // Redirecting away naturally destroys CT's intentPreview div —
  // DO NOT attempt DOM manipulation, CT's MutationObserver overrides it.
  if (action === 'close') {
    setTimeout(function() {
      // Welcome Offer dismiss → account.html (encourage signup)
      // Everything else → reload same page
      if (campaign.toLowerCase().includes('welcome offer')) {
        window.location.href = 'account.html';
      } else {
        window.location.reload();
      }
    }, 300);
  }
});
