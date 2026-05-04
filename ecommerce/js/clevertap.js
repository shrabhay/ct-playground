// =============================================================================
// clevertap.js — CleverTap SDK Initialisation for Bazario
// Loaded on every page before any CT event calls.
// =============================================================================

// ─── STEP 1: Initialise CleverTap ───────────────────────────────────────────
// This block must run before the SDK script loads.
// It sets up the queue that holds events fired before the SDK is ready.

var clevertap = {
  event:          [],
  profile:        [],
  account:        [],
  onUserLogin:    [],
  notifications:  [],
  privacy:        [],
  nativeDisplays: []
};

// ─── STEP 2: Set Your Credentials ───────────────────────────────────────────
// Replace the placeholders below with your actual CT Playground credentials.
// These are safe to commit to GitHub — they are public by design (SDK only).
// NEVER put your Passcode here — that is for server-side scripts only.

clevertap.account.push({ "id": "RKW-659-477Z" });
clevertap.token  = "c56-032";

// =============================================================================
// PRODUCT EXPERIENCES — REMOTE CONFIG VARIABLES
// Defines all RC variables with default values.
// Dot notation creates folder hierarchy in CT dashboard:
// Bazario > Homepage > show_prime_banner etc.
// syncVariables() must be called once (as test profile) to push to CT dashboard.
// fetchVariables() is called on each page load to get latest values.
// =============================================================================
// ── Wait for CT SDK to load before defining variables ──
(function waitForCT(attempts) {
  attempts = attempts || 0;
  if (attempts > 20) {
    console.warn('[RC] CT SDK did not load in time');
    return;
  }

  if (typeof clevertap.defineVariable === 'function') {

    // ── Homepage ──
    window.rcHomepageShowPrimeBanner    = clevertap.defineVariable("Bazario.Homepage.show_prime_banner",     true);
    window.rcHomepagePrimeBannerText    = clevertap.defineVariable("Bazario.Homepage.prime_banner_text",     "⚡ Unlock free delivery on every order — Join Prime at ₹999/year");
    window.rcHomepageShowDeals          = clevertap.defineVariable("Bazario.Homepage.show_deals_section",    true);
    window.rcHomepageShowTrending       = clevertap.defineVariable("Bazario.Homepage.show_trending_section", true);
    window.rcHomepageSaleLabel          = clevertap.defineVariable("Bazario.Homepage.homepage_sale_label",   "MEGA SALE");

    // ── Listing ──
    window.rcListingPromoBannerEnabled  = clevertap.defineVariable("Bazario.Listing.listing_promo_banner_enabled",    true);
    window.rcListingPromoText           = clevertap.defineVariable("Bazario.Listing.listing_promo_text",              "🎉 Extra 10% off on orders above ₹999 — Use code BAZARIO10");
    window.rcListingPromoBgColor        = clevertap.defineVariable("Bazario.Listing.listing_promo_bg_color",          "#1565c0");
    window.rcListingFreeDeliveryThresh  = clevertap.defineVariable("Bazario.Listing.listing_free_delivery_threshold", 499);
    window.rcListingShowPrimeBadge      = clevertap.defineVariable("Bazario.Listing.listing_show_prime_badge",        true);
    window.rcListingSortDefault         = clevertap.defineVariable("Bazario.Listing.listing_sort_default",            "relevance");
    window.rcListingProductsPerPage     = clevertap.defineVariable("Bazario.Listing.listing_products_per_page",       12);

    // ── Product ──
    window.rcPdpShowDeliveryCheck       = clevertap.defineVariable("Bazario.Product.pdp_show_delivery_check",     true);
    window.rcPdpShowOffersSection       = clevertap.defineVariable("Bazario.Product.pdp_show_offers_section",     true);
    window.rcPdpShowSimilarProducts     = clevertap.defineVariable("Bazario.Product.pdp_show_similar_products",   true);
    window.rcPdpFreeDeliveryThreshold   = clevertap.defineVariable("Bazario.Product.pdp_free_delivery_threshold", 499);
    window.rcPdpEmiEnabled              = clevertap.defineVariable("Bazario.Product.pdp_emi_enabled",             true);
    window.rcPdpCtaPrimaryText          = clevertap.defineVariable("Bazario.Product.pdp_cta_primary_text",        "Add to Cart");
    window.rcPdpCtaSecondaryText        = clevertap.defineVariable("Bazario.Product.pdp_cta_secondary_text",      "Buy Now");
    window.rcPdpUrgencyText             = clevertap.defineVariable("Bazario.Product.pdp_urgency_text",            "Only a few left in stock!");
    window.rcPdpShowUrgency             = clevertap.defineVariable("Bazario.Product.pdp_show_urgency",            false);
    window.rcPdpRatingEnabled           = clevertap.defineVariable("Bazario.Product.pdp_rating_enabled",          true);

    // ── Cart ──
    window.rcCartCouponEnabled          = clevertap.defineVariable("Bazario.Cart.cart_coupon_enabled",            true);
    window.rcCartFreeDeliveryThreshold  = clevertap.defineVariable("Bazario.Cart.cart_free_delivery_threshold",   499);
    window.rcCartShowSavingsSummary     = clevertap.defineVariable("Bazario.Cart.cart_show_savings_summary",      true);
    window.rcCartShowRecommended        = clevertap.defineVariable("Bazario.Cart.cart_show_recommended",          true);
    window.rcCartPromoText              = clevertap.defineVariable("Bazario.Cart.cart_promo_text",                "🚚 Free delivery on orders above ₹499");
    window.rcCartPrimeUpsellEnabled     = clevertap.defineVariable("Bazario.Cart.cart_prime_upsell_enabled",      true);

    // ── Checkout ──
    window.rcCheckoutCodEnabled         = clevertap.defineVariable("Bazario.Checkout.checkout_cod_enabled",           true);
    window.rcCheckoutCodFee             = clevertap.defineVariable("Bazario.Checkout.checkout_cod_fee",               40);
    window.rcCheckoutShowTrustBadges    = clevertap.defineVariable("Bazario.Checkout.checkout_show_trust_badges",     true);
    window.rcCheckoutPromoText          = clevertap.defineVariable("Bazario.Checkout.checkout_promo_text",            "🔒 100% Secure Payments — Your data is protected");
    window.rcCheckoutFreeDeliveryThresh = clevertap.defineVariable("Bazario.Checkout.checkout_free_delivery_threshold", 499);
    window.rcCheckoutExpressEnabled     = clevertap.defineVariable("Bazario.Checkout.checkout_express_enabled",       false);

    // ── Account ──
    window.rcAccountPrimePrice          = clevertap.defineVariable("Bazario.Account.account_prime_annual_price",    999);
    window.rcAccountPrimeDiscountText   = clevertap.defineVariable("Bazario.Account.account_prime_discount_text",   "That's just ₹83/month — less than a cup of coffee ☕");
    window.rcAccountShowReferral        = clevertap.defineVariable("Bazario.Account.account_show_referral_section", false);

    // ── Global ──
    window.rcGlobalFreeDeliveryThresh   = clevertap.defineVariable("Bazario.Global.global_free_delivery_threshold",   499);
    window.rcGlobalSupportPhone         = clevertap.defineVariable("Bazario.Global.global_support_phone",             "1800-123-4567");
    window.rcGlobalShowAppBanner        = clevertap.defineVariable("Bazario.Global.global_show_app_download_banner",  false);
    window.rcGlobalMaintenanceMode      = clevertap.defineVariable("Bazario.Global.global_maintenance_mode",          false);
    window.rcGlobalSaleEventName        = clevertap.defineVariable("Bazario.Global.global_sale_event_name",           "Mega Sale");

    window._rcVariablesDefined = true;

  } else {
    console.log('[RC] Waiting for CT SDK... attempt', attempts + 1);
    setTimeout(function() { waitForCT(attempts + 1); }, 2000);
  }
})(0);

// ─── STEP 3: Privacy Settings ───────────────────────────────────────────────
// optOut: false  — user has not opted out of tracking
// useIP:  false  — do not use IP address for geolocation (GDPR best practice)

clevertap.privacy.push({ optOut: false });
clevertap.privacy.push({ useIP:  false });
clevertap.enablePersonalization = true;

// ─── STEP 4: Load the SDK Script ────────────────────────────────────────────
// Loads CleverTap's SDK asynchronously from their CDN.
// The 'async' attribute ensures it doesn't block page rendering.

(function() {
  var wzrk = document.createElement('script');
  wzrk.type = 'text/javascript';
  wzrk.async = true;
  wzrk.src = 'https://cdn.jsdelivr.net/npm/clevertap-web-sdk/clevertap.min.js';
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

// =============================================================================
// WEB PUSH — Permission Prompt
// Triggered on page load. CT auto-throttles — won't re-ask if rejected
// within the last 7 days. Soft Prompt (Card Popup) must be enabled in
// CT Dashboard → Settings → Channels → Web Push → Soft Prompt.
// Only runs on HTTPS (Netlify) — silently skips on localhost.
// =============================================================================

(function() {
  if (location.protocol !== 'https:') return;

  window.addEventListener('load', function() {
    setTimeout(function() {
      clevertap.notifications.push({
        "titleText":            "Stay in the loop! 🛍️",
        "bodyText":             "Get notified about deals, order updates and offers personalised for you",
        "okButtonText":         "Yes, notify me!",
        "rejectButtonText":     "Maybe later",
        "okButtonColor":        "#2874f0",
        "askAgainTimeInSeconds": 5
      });
    }, 3000);
  });
})();

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
