// ─────────────────────────────────────────────────────────────────────────────
// auth.js — Shared authentication state for all Bazario ecommerce pages
// Include on every page: <script src="js/auth.js"></script>
// Place the script tag just before </body> on each page.
// ─────────────────────────────────────────────────────────────────────────────

// ─── Get current logged-in user from localStorage ───
function getUser() {
  return JSON.parse(localStorage.getItem('bazario_user') || 'null');
}

// ─── Check if user is logged in ───
function isLoggedIn() {
  return !!getUser();
}

// ─── Update the navbar to reflect login state ───
// Finds .nav-btn-login on the page and replaces it with the user's
// first name and a dropdown-style button when logged in.
function initAuthNav() {
  const user = getUser();
  const loginBtn = document.querySelector('.nav-btn-login');
  if (!loginBtn) return; // some pages don't have a login button

  if (user) {
    const firstName = user.name ? user.name.split(' ')[0] : 'Account';
    // Replace the plain login button with a logged-in state button
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
  // If not logged in, the default Login/Sign Up button stays as-is
}

// ─── Run as soon as DOM is ready ───
if (document.readyState === 'loading') {
  document.addEventListener('DOMContentLoaded', initAuthNav);
} else {
  initAuthNav();
}