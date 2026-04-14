// =============================================================================
// clevertap.js — CleverTap SDK Initialisation for Bazario
// Loaded on every page before any CT event calls.
// =============================================================================

// ─── STEP 1: Initialise CleverTap ───────────────────────────────────────────
// This block must run before the SDK script loads.
// It sets up the queue that holds events fired before the SDK is ready.

var clevertap = {
  event:        [],
  profile:      [],
  account:      [],
  onUserLogin:  [],
  notifications:[],
  privacy:      []
};

// ─── STEP 2: Set Your Credentials ───────────────────────────────────────────
// Replace the placeholders below with your actual CT Playground credentials.
// These are safe to commit to GitHub — they are public by design (SDK only).
// NEVER put your Passcode here — that is for server-side scripts only.

clevertap.account.push({ "id": "RKW-659-477Z" });
clevertap.token  = "c56-032";

// ─── STEP 3: Privacy Settings ───────────────────────────────────────────────
// optOut: false  — user has not opted out of tracking
// useIP:  false  — do not use IP address for geolocation (GDPR best practice)

clevertap.privacy.push({ optOut: false });
clevertap.privacy.push({ useIP:  false });

// ─── STEP 4: Load the SDK Script ────────────────────────────────────────────
// Loads CleverTap's SDK asynchronously from their CDN.
// The 'async' attribute ensures it doesn't block page rendering.

(function() {
  var wzrk = document.createElement('script');
  wzrk.type = 'text/javascript';
  wzrk.async = true;
  wzrk.src = (
    document.location.protocol === 'https:'
      ? 'https://d2r1yp2w7bby2u.cloudfront.net'
      : 'http://static.clevertap.com'
  ) + '/js/a.js';

  var s = document.getElementsByTagName('script')[0];
  s.parentNode.insertBefore(wzrk, s);
})();

// =============================================================================
// HELPER FUNCTIONS
// Used across all pages for common CT operations.
// =============================================================================

// ─── Get the currently logged-in user ───────────────────────────────────────
function getCTUser() {
  return JSON.parse(localStorage.getItem('bazario_user') || 'null');
}

// ─── Push user identity to CleverTap ────────────────────────────────────────
// Call this on login AND signup — CT merges anonymous events automatically.
// Gender must be "M" or "F" — CT reserved values.
function ctIdentifyUser(user) {
  if (!user) return;
  clevertap.onUserLogin.push({
    "Site": {
      "Name":         user.name   || '',
      "Email":        user.email  || '',
      "Phone":        (user.phone || '').replace(/\s+/g, ''),
      "Gender":       user.gender === 'Male' ? 'M' : user.gender === 'Female' ? 'F' : '',
      "DOB":          user.dob ? new Date(user.dob) : undefined,
      "City":         user.city   || '',
      "Is Prime":     user.isPrime || false,
      "Joined At":    user.joinedAt || new Date().toISOString(),
      "Vertical":     "Bazario",
      "Vertical Type":"Ecommerce"
    }
  });
}

// ─── Push profile properties (for updates after login) ──────────────────────
// Use this for incremental updates — NOT for login/signup (use ctIdentifyUser).
function ctUpdateProfile(properties) {
  clevertap.profile.push({ "Site": properties });
}

// ─── Push a custom event ────────────────────────────────────────────────────
// Convenience wrapper — same as clevertap.event.push() but with error handling.
function ctEvent(eventName, properties) {
  try {
    const vertical = localStorage.getItem('ct_active_vertical') || 'bazario';
    const enriched = {
      ...(properties || {}),
      vertical: vertical
    };
    clevertap.event.push(eventName, enriched);
  } catch(e) {
    console.warn('CT event failed:', eventName, e);
  }
}

// ─── Auto-identify on page load ─────────────────────────────────────────────
// If a user is already logged in (persisted in localStorage), push their
// identity to CT on every page load. This ensures CT always knows who the
// user is across sessions, even if onUserLogin wasn't called this session.
// (function autoIdentify() {
//   const user = getCTUser();
//   if (!user) return;

//   // Use profile.push (not onUserLogin) for returning sessions —
//   // onUserLogin is only for the actual login/signup moment.
//   clevertap.profile.push({
//     "Site": {
//       "Name":     user.name  || '',
//       "Email":    user.email || '',
//       "Phone":    user.phone || '',
//       "City":     user.city  || '',
//       "Is Prime": user.isPrime || false
//     }
//   });
// })();