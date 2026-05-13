# CT Playground — Project Context Document
# Last updated: May 2026
# Purpose: Full context for Claude to assist with this project across conversations

---

## WHO I AM

- **Name:** Abhay Shrawankar
- **Role:** Senior Solutions Engineer at CleverTap
- **Location:** Delhi, India
- **Use case:** Building CT Playground as a personal learning and demo tool
- **Experience:** 10+ years, prior roles at Datability Technologies, HCL, Concentrix
- **Preferences:**
  - Step-by-step granular guidance for technical tasks
  - Batch commits — one deploy per session, not per change
  - Concise answers with bullets/headers preferred
  - Always refer back to decisions made in previous conversations
  - Never lose track of architectural decisions

---

## WHAT IS CT PLAYGROUND

A multi-vertical CleverTap integration sandbox for:
1. **Learning** — understanding CT's full feature set hands-on
2. **Demo** — showing prospects CT's capabilities live during sales calls
3. **Pre-sales enablement** — a self-contained demo environment

**Live URL:** ctplayground.netlify.app
**GitHub repo:** ct-playground
**Netlify plan:** Personal ($9/month, 1000 build minutes)
**CT Project name:** CT Playground
**CT Region:** India (SK)

---

## TECH STACK

| Layer | Technology |
|---|---|
| Frontend | HTML + Vanilla JS + Tailwind CSS (CDN) |
| Hosting | Netlify (auto-deploy from GitHub) |
| Serverless | Netlify Functions (Node.js) |
| Analytics | CleverTap Web SDK |
| Data storage | Firestore (Firebase) + localStorage cache for session |
| Data scripts | Python 3 + requests library |
| Version control | GitHub → Netlify auto-deploy |

---

## PROJECT STRUCTURE

```
ct-playground/
├── README.md                        # Project overview for GitHub
├── index.html                       # Vertical selector homepage
├── demo.html                        # Shared demo console (PIN protected)
├── clevertap_sw.js                  # CT service worker for Web Push
├── netlify.toml                     # Netlify build config + proxy redirects
│
├── js/                              # Shared across all verticals
│   └── firebase.js                  # Firebase init + Auth + shared Firestore helpers
├── ecommerce/                       # Bazario ecommerce vertical
│   ├── index.html                   # Homepage with carousel
│   ├── listing.html                 # Category listing with filters + pagination
│   ├── product.html                 # Product detail page
│   ├── cart.html                    # Cart with coupons
│   ├── checkout.html                # Multi-step checkout
│   ├── confirmation.html            # Order confirmation
│   ├── account.html                 # Login, signup, profile management
│   ├── wishlist.html                # Wishlist management
│   └── js/
│       ├── catalogue.js             # 104 products across 8 categories
│       ├── clevertap.js             # CT SDK init + helper functions
│       ├── auth.js                  # Shared navbar, counts, popup handler
│       └── firebase-bazario.js      # Bazario-specific Firestore helpers
│
├── netlify/
│   └── functions/
│       └── ct-proxy.js              # CT API proxy (passcode server-side)
│
└── dummy_users_data_scripts/        # Python dummy data scripts
    ├── README.md
    ├── generate_ecommerce_users_and_upload.py  # Main dummy data script
    ├── update_vertical_from_csv.py             # Push Vertical property from CSV
    ├── create_cart_abandoners.py               # Cart abandonment events
    ├── fix_joined_at.py                        # Fix date properties to $D_EPOCH
    └── seed_recommendation_events.py           # Seed Product Viewed + Added to Cart for recommendations
```

---

## VERTICALS PLANNED

| # | Vertical | Brand | Status | Email Domain |
|---|---|---|---|---|
| 1 | Ecommerce | Bazario | ✅ Live | Realistic Indian emails |
| 2 | Fintech | TBD | 🔲 Phase 10 | @fintech-user.com |
| 3 | Subscription | TBD | 🔲 Phase 11 | @subscription-user.com |
| 4 | Foodtech | TBD | 🔲 Phase 12 | @foodtech-user.com |

**Note:** Brand names for Fintech, Subscription, Foodtech are TBD — to be decided when we get to those phases.

---

## COMPLETE ROADMAP

### ✅ Phase 1 — Foundation
- GitHub repo → Netlify pipeline
- VS Code + Git + Live Server
- .gitignore for CT credentials (later reversed — Account ID/Token are public)

### ✅ Phase 2 — Bazario Web Site
- 8 pages built end to end
- 104 products across 8 categories in catalogue.js
- Working search, filters, pagination, carousel, wishlist
- Per-user cart, wishlist, orders, addresses in localStorage
- Guest cart/wishlist merge on login
- Login gate before checkout with post-auth redirect

### ✅ Phase 3 — CT SDK Integration (all 8 pages)
- 38 unique events across all pages
- onUserLogin with Vertical: Bazario on signup/login
- Charged event with Items array on checkout
- Total Orders and Lifetime Value pushed after purchase
- All events auto-tagged with vertical: bazario via ctEvent()
- Beacon/replay duplicate event issue resolved
- Fire events on arrival not departure (URL params for source)

### ✅ Phase 4 — Demo Infrastructure
- Root index.html — vertical selector with 4 cards
- demo.html — full demo console with PIN gate
- Netlify Function proxy — /ct-proxy/upload and /ct-proxy/profile
- Vertical theme adapts colours per active vertical
- Live Demo button on all 8 Bazario pages
- Profile Manager — load and push any CT profile via proxy
- Data Flow Visualiser — 6 animated flow diagrams
- External Integration payloads — 4 live cards

### ✅ Phase 5 — Dummy Data
- 15,000 Indian user profiles uploaded
- 3,246,470 events spanning 5 years (2020-2025)
- 4 archetypes: High Value (2,250), Regular (6,000), Occasional (4,500), Dormant (2,250)
- Seasonal spikes: Diwali, New Year, Summer Sale
- Vertical: Bazario pushed to all profiles
- 15 segments built in CT dashboard
- 4 funnels built in CT dashboard

### ✅ Phase 6 — CT Channel Integration (COMPLETE)
1. Web Pop-ups (4 campaigns: Welcome Offer, Cart Abandonment, Re-engagement, High Value Upsell)
2. Exit Intent (3 campaigns: Cart, Checkout with auto-discount, Product with auto add-to-cart)
3. Native Display (31 campaigns across Homepage/Listing/Product/Confirmation + 2 recommendation campaigns)
4. Product Experiences / RC (43 variables, Phase A + B fully complete across all pages)
5. Recommendations (Similar Products → product.html, Frequently Added Together → cart.html)
6. Catalogs (covered via Web Inbox + Email in Phase 14)
7. Web Push (3 campaigns + API fire live from demo.html)
8. Web Inbox (4 campaigns: Featured Deal, Category Deal Alert, Demo Offer, Demo Order Update)
9. Bulletins (3 business events: Flash Sale Started, New Collection Added, Product Back in Stock)
10. demo.html Channel Demos section (fully built — SDK channels + API channels + fire live)

**Known parked items (not blockers):**
- Web Inbox Order Confirmed (Charged trigger) — CT issue, parked
- Bulletins delivery to demo user — daily segment recomputation limitation
- Segment Preview — REMOVED from demo.html. CT has no "get segments for user" API. CT dashboard shows segment membership directly.
- Email channel — Phase 14 (custom domain)

### ✅ Phase 7 — Firebase Backend Migration (COMPLETE)
- Firebase project: ct-playground-0323 (shared across all verticals)
- Firebase Auth (email/password) replaces simulated login
- Firestore replaces localStorage for cart, wishlist, orders, addresses
- js/firebase.js — shared init + Auth + profile helpers (all verticals)
- ecommerce/js/firebase-bazario.js — Bazario-specific collection helpers
- Guest cart/wishlist preserved in localStorage, merged to Firestore on login
- localStorage retained as session cache for UI display (bazario_user key)
- sanitizeCartItem() strips nested arrays before Firestore writes
- clevertap.logout() called on Firebase signOut to prevent CT identity bleed
- Checkout page loads addresses from Firestore dynamically (no hardcoded addresses)
- Segment Preview removed from demo.html (CT has no "get segments for user" API)
- CT web popup postMessage handler restored in auth.js (was lost during rewrite)

### 🔲 Phase 8 — Bazario Android App (Kotlin)
### 🔲 Phase 9 — Bazario iOS App (Swift)
### 🔲 Phase 10 — Fintech Vertical (Web + Android + iOS)
### 🔲 Phase 11 — Subscription Vertical
### 🔲 Phase 12 — Foodtech Vertical
### 🔲 Phase 13 — Vertical Selector Homepage Upgrade
### 🔲 Phase 14 — Custom Domain
- Set up custom domain → also enables Email channel integration (ESP + verified sending domain + DNS records)

---

## KEY ARCHITECTURAL DECISIONS

### CT Integration
- **Single CT project** for all 4 verticals (CT Playground)
- **Fire events on arrival** not departure — prevents beacon/replay duplicates
- **Pass source via URL params** between pages — listing.html reads source, banner, position
- **No arrays** anywhere except Charged event
- **Charged event** uses Title Case reserved properties (Amount, Payment mode, Order ID, Items)
- **ctEvent() helper** auto-appends vertical property to every event
- **onUserLogin** only at signup/login — never on page load

### Multi-Vertical Data Architecture
- **Unique email domains** per vertical for dummy users
  - Bazario: realistic Indian emails (already done — don't change)
  - Others: @fintech-user.com, @subscription-user.com, @foodtech-user.com
- **Vertical property** pushed as flat string on all profiles
- **Unique event names** per vertical — no overlap between verticals
- **Vertical auto-tagging** — ctEvent() appends vertical automatically
- **Segmentation** — filter by Vertical = [name] as primary filter

### Demo Architecture
- **Vertical selector** on root index.html sets ct_active_vertical in localStorage
- **demo.html** reads ct_active_vertical and adapts theme + content
- **PIN gate** on demo.html — 6 digit, session persists
- **Proxy** keeps CT passcode server-side in Netlify env vars

### CT Web Pop-up Pattern (CONFIRMED WORKING)
This is the pattern that works — follow for ALL future campaigns:

**Pop-up HTML (in CT dashboard):**
```javascript
var BZ_POPUP = { campaign_name, campaign_id, popup_type, trigger_event, vertical, ... };

function sendMessage(action) {
  window.parent.postMessage({ type: "bz_popup", action, payload: BZ_POPUP }, "*");
}

sendMessage("viewed"); // fires on render

window.bzCtaClick = function() {
  sendMessage("clicked");
  sendMessage("close");
  document.getElementById('bz-popup-overlay').remove();
};

window.bzNoThanks = function() {
  sendMessage("nothanks");
  sendMessage("close");
  document.getElementById('bz-popup-overlay').remove();
};

window.bzDismiss = function() {
  sendMessage("dismissed");
  sendMessage("close");
  document.getElementById('bz-popup-overlay').remove();
};
```

**auth.js message listener handles:**
- `viewed` → CT event Web Popup Viewed + profile push
- `clicked` → CT event Web Popup Clicked + profile push + redirect to CTA destination
- `nothanks` → CT event Web Popup Dismissed (dismiss_action: no_thanks_link) + redirect
- `dismissed` → CT event Web Popup Dismissed (dismiss_action: close_button) + redirect
- `close` → removeCtOverlay() targeting intentPreview div

**CRITICAL:** All dismiss actions redirect away from current page — this is what kills CT's overlay naturally. Do NOT try to hide the overlay via style changes — CT's MutationObserver overrides them.

**CT overlay structure:**
```html
<div id="intentPreview" style="display:block;...">
  <iframe id="wiz-iframe-intent" ...></iframe>
</div>
```

**demo.html popup handling:**
- auth.js is NOT loaded on demo.html — popup postMessage handler is added directly in demo.html
- On any dismiss action (clicked/nothanks/dismissed/close) → page reloads after 300ms
- This kills CT's overlay reliably without needing auth.js redirect mechanism
- PIN session persists via sessionStorage so user is not asked to re-enter PIN

### Redirect destinations per campaign:
| Campaign | CTA redirect | Dismiss redirect |
|---|---|---|
| Welcome Offer | index.html | account.html |
| Cart Abandonment | cart.html | same page (window.location.href) |
| Re-engagement | index.html | same page |
| High Value Upsell | cart.html | same page |

### Native Display Architecture
- **KV pair approach** (not Custom HTML) for all category banners
- CT fires `CT_web_native_display` event with `data.kv` object
- Page JS reads KV pairs and renders HTML itself — full styling control
- Topic routing: each campaign sets `topic` KV → JS router dispatches to correct renderer
- **Recommendations** also use KV pair approach — CT resolves liquid tags server-side
  and passes resolved product data (name, image, price, etc.) as plain strings in KV pairs

### Recommendations Architecture
- Two CT recommendation models: "Bazario - Similar Products", "Bazario - Frequently Added Together"
- Both use 180-day window (CT maximum)
- Seeded with 105,000 events via seed_recommendation_events.py (5 Product Viewed + 2 Added to Cart per user, last 170 days)
- Campaign 1: [Bazario] ND Rec — Similar Products → product.html (#ct-native-display-recommendations-product)
- Campaign 2: [Bazario] ND Rec — Frequently Added Together → cart.html (#ct-native-display-recommendations-cart)
- 25 KV pairs per campaign: topic + 8 fields × 3 slots (id, name, image, price, original_price, discount, rating, tags)

### Web Push Architecture
- VAPID keys generated and stored in CT Dashboard (Chrome + Firefox)
- Service worker: clevertap_sw.js at repo root (required)
- Soft prompt (Card Popup) enabled in CT → Settings → Channels → Web Push
- Permission prompt fires on page load (HTTPS only) with 3s delay via clevertap.js
- Account-level Passcode required for API calls (not User Passcode)

### Bulletins Architecture
- 3 business events: Flash Sale Started, New Collection Added, Product Back in Stock
- Interest segments pre-computed daily at midnight — not real-time
- API endpoint: POST /ct-proxy/bulletin → CT /1/targets/trigger.json
- c-by field MUST be actual CT admin user email (not placeholder)
- Account-level Passcode required (Admin role)
- Flash Sale matches on category; Back in Stock matches on product_id; New Collection matches on category (Wishlist Added)

### Web Inbox Architecture
- Element ID: bz-inbox-trigger (registered in CT Dashboard → Settings → Channels → Web Inbox)
- Bell icon injected into navbar via auth.js initInboxTrigger() on all Bazario pages
- Bell icon also added directly to demo.html topbar for demo console use
- 4 campaigns: Featured Deal of the Day, Category Deal Alert, Demo Offer, Demo Order Update
- Demo-specific events (Demo Offer Triggered, Demo Order Update Triggered) must exist in CT before campaign creation — seed by firing once from demo.html
- Categories configured: Offers, Orders, Recommended

### Storage Keys (localStorage)
- `bazario_user` — current logged-in user session
- `bazario_profile_${email}` — persists user profile across logout
- `bazario_cart_${email}` / `bazario_cart_guest` — per-user cart
- `bazario_wishlist_${email}` / `bazario_wishlist_guest` — per-user wishlist
- `bazario_orders_${email}` — per-user orders
- `bazario_addresses_${email}` — per-user addresses
- `ct_active_vertical` — active vertical for demo.html context
- `WZRK_PE_${email}` — per-user RC variable cache

---

## CT EVENTS SCHEMA — BAZARIO

### Account Events
```
onUserLogin — Name, Email, Phone, Gender, DOB, City, Is Prime,
              MSG-email, MSG-sms, MSG-push, Vertical, Vertical Type, Joined At
Signup Initiated — method, has_referral, from_cart, vertical
User Signed Up — method, gender, has_dob, referral_used, vertical
User Logged In — method, is_prime, vertical
Profile Updated — fields_updated, vertical
Preference Changed — preference, value, vertical
User Logged Out — vertical
Order History Viewed — order_count, vertical
```

### Browse Events
```
Category Viewed — category_name, source, total_products, vertical
Category Clicked — category_name, source, vertical
Banner Clicked — banner_name, banner_position, destination, vertical
Product Viewed — product_id, product_name, category, brand, price,
                 original_price, discount_pct, rating, source, vertical
Product Searched — query, results_count, source, vertical
Filter Applied — filter_type, filter_value, category, vertical
Sort Applied — sort_type, category, vertical
Listing Page Changed — category, page_number, vertical
Variant Selected — product_id, product_name, variant_type, variant_value, vertical
Delivery Check — pincode, product_id, product_name, category, vertical
```

### Cart Events
```
Added to Cart — product_id, product_name, category, brand, price,
                quantity, cart_value, source, vertical
Removed from Cart — product_id, product_name, category, brand, price,
                    quantity, cart_value, vertical
Cart Viewed — item_count, cart_value, top_category, unique_categories, vertical
Cart Quantity Updated — product_id, product_name, category, new_quantity, cart_value, vertical
Saved for Later — product_id, product_name, category, price, vertical
Coupon Applied — coupon_code, discount_pct, cart_value, vertical
Coupon Failed — coupon_code, cart_value, vertical
Buy Now Clicked — product_id, product_name, category, brand, price, quantity, variant, vertical
```

### Checkout Events
```
Checkout Initiated — item_count, cart_value, top_category, category_count,
                     coupon_applied, coupon_code, vertical
Checkout Page Viewed — item_count, cart_value, step, vertical
Address Selected — address_type, city, state, is_new, cart_value, vertical
Payment Method Selected — payment_method, cart_value, vertical
Payment Step Completed — payment_method, cart_value, vertical
Charged — Amount, Payment mode, Order ID, Items[] (CT reserved event — Title Case)
Order Confirmed — order_id, order_value, item_count, payment_method, top_category, vertical
Order Rated — order_id, rating, context, vertical
```

### Wishlist Events
```
Added to Wishlist — product_id, product_name, category, brand, price, source, vertical
Removed from Wishlist — product_id, product_name, category, price, vertical
Wishlist Viewed — item_count, vertical
Moved to Cart from Wishlist — product_id, product_name, category, brand, price, cart_value, vertical
Add All Wishlist to Cart — item_count, cart_value, vertical
```

### Web Popup Events (Phase 6)
```
Web Popup Viewed — campaign_name, campaign_id, popup_type, trigger_event,
                   coupon_shown (if applicable), vertical
Web Popup Clicked — (all above) + cta_text, cta_action
Web Popup Dismissed — (all above) + dismiss_action (close_button / no_thanks_link)
```

### Channel Demo Events (Phase 6)
```
Demo Offer Triggered — vertical (fires Demo Offer Web Inbox campaign)
Demo Order Update Triggered — vertical (fires Demo Order Update Web Inbox campaign)
Recommendation Clicked — recommendation_type, product_id, page, vertical
Native Display Clicked — campaign_name, category_shown, cta_url, vertical
Promo Banner Viewed — page, promo_text, vertical
Prime Upsell Viewed — page, vertical
Prime Upsell Clicked — page, vertical
Urgency Message Viewed — product_id, product_name, category, urgency_text, vertical
```

### Profile Properties
```
Common: Name, Email, Phone, Gender, DOB, City, MSG-email, MSG-sms, MSG-push,
        Vertical, Vertical Type, Joined At ($D_EPOCH format)

Bazario-specific: Is Prime, Preferred Category, Total Orders, Lifetime Value,
                  Last Order Date ($D_EPOCH), Last Order Value,
                  Last Popup Seen, Last Popup Seen At ($D_EPOCH),
                  Last Popup Clicked, Last Popup Clicked At ($D_EPOCH),
                  Last Popup Dismissed, Last Popup Dismissed At ($D_EPOCH),
                  Last Prime Upsell Seen, Last Prime Upsell Clicked
```

---

## CT SEGMENTS BUILT (15 total)

1. High Value Users — Archetype = High Value (~2,250)
2. Dormant Users — Archetype = Dormant (~2,250)
3. Cart Abandoners — Checkout Initiated in last 60 days AND Charged not in last 60 days (~3,750)
4. Prime Members — Is Prime = true (~2,500)
5. Lapsed Buyers — Charged exists AND Last Order Date before 180 days ago
6. High Value Electronics Buyers — Archetype = High Value AND Charged where Category = Electronics
7. Mumbai Users — City = Mumbai (~1,800)
8. New Users — Joined At in last 90 days
9. Power Buyers — Total Orders > 10
10. Wishlist but Never Purchased — Added to Wishlist exists AND Charged does not exist
11. Recently Active Buyers — Last Order Date in last 30 days
12. First Cohort — Joined At between Jan 2020 and Dec 2020
13. Peak Cohort — Joined At between Jan 2023 and Dec 2023
14. High Spenders — Lifetime Value > 50000
15. Bazario Vertical — Vertical = Bazario (all 15,000 dummy users)

---

## CT FUNNELS BUILT

1. Core Purchase — Category Viewed → Product Viewed → Added to Cart → Checkout Initiated → Charged
2. Checkout Conversion — Checkout Initiated → Checkout Page Viewed → Payment Method Selected → Charged
3. New User Activation — User Signed Up → Product Viewed → Added to Cart → Charged
4. Wishlist to Purchase — Added to Wishlist → Moved to Cart from Wishlist → Charged

---

## CT CHANNEL INTEGRATION — BAZARIO (Phase 6 Complete)

### Web Pop-ups (4 campaigns)
| Campaign | Trigger | Segment |
|---|---|---|
| Welcome Offer — New User Discount | User Signed Up | All Users |
| Cart Abandonment — Discount Nudge | Cart Viewed | All Users |
| Re-engagement — Welcome Back | Home Page Viewed | Dormant Users · index.html only |
| High Value Upsell — Prime Push | Product Viewed (price > ₹10,000) | Is Prime = false |

### Exit Intent (3 campaigns)
| Campaign | Page |
|---|---|
| Cart Exit Recovery | cart.html |
| Checkout Exit with Auto-Discount | checkout.html |
| Product Exit with Auto Add-to-Cart | product.html |

### Native Display (31 campaigns)
- Homepage banners: 8 category variants — trigger: Category Viewed (last viewed category)
- Listing banners: 8 category variants — trigger: Category Viewed
- Product banners: 7 category variants — trigger: Product Viewed
- Confirmation banners: 8 category variants — trigger: Order Confirmed
- All use KV pair approach with topic routing

### Recommendation Campaigns (2)
- [Bazario] ND Rec — Similar Products → product.html (trigger: Product Viewed)
- [Bazario] ND Rec — Frequently Added Together → cart.html (trigger: Cart Viewed)

### Product Experiences / RC (43 variables)
- Phase A: 25 UI + content variables (hero text, gradients, CTAs, badges)
- Phase B: 18 business logic variables (free delivery threshold, coupons, sort order)
- Per-user localStorage pattern: WZRK_PE_${email}

### Web Push (3 campaigns)
| Campaign | Trigger |
|---|---|
| Abandoned Cart Recovery | Added to Cart → no Charged within 1 min |
| Re-engagement Deal Alert | One-time · Dormant Users |
| Order Confirmation | Charged · for-loop personalisation on Items array |

### Web Inbox (4 campaigns)
| Campaign | Trigger | Category |
|---|---|---|
| Featured Deal of the Day | Home Page Viewed | Offers |
| Category Deal Alert | Category Viewed | Offers |
| Demo Offer | Demo Offer Triggered | Offers |
| Demo Order Update | Demo Order Update Triggered | Orders |

### Bulletins (3 business events)
| Business Event | Interest Segment | Properties |
|---|---|---|
| Flash Sale Started | Product Viewed · category match | category, discount_pct |
| New Collection Added | Wishlist Added · category match | category, collection_name |
| Product Back in Stock | Product Viewed · product_id match | product_id, product_name, category |

### demo.html Channel Demos
- SDK Channels: Web Pop-ups, Native Display, Product Experiences, Recommendations, Web Inbox
  - Each has 🔴 Fire Live buttons that fire qualifying CT SDK events directly
  - CT campaigns respond and render on demo.html itself
- API Channels: Web Push (event-triggered + API fire live), Bulletins (Fire Live)
  - Web Push API: POST /ct-proxy/webpush → CT /1/send/webpush.json
  - Bulletins API: POST /ct-proxy/bulletin → CT /1/targets/trigger.json
- External Trigger: Phase 14 (requires Email channel + custom domain)

---

## NETLIFY PROXY

**Endpoints:**
- `POST /ct-proxy/upload` → CT Upload API (write events + profiles)
- `GET /ct-proxy/profile?email=` → CT Profile API (read any user)
- `POST /ct-proxy/webpush` → CT Send Web Push API (/1/send/webpush.json)
- `POST /ct-proxy/bulletin` → CT Bulletins Trigger API (/1/targets/trigger.json)

**Environment variables in Netlify:**
- `CT_ACCOUNT_ID` — Account ID
- `CT_PASSCODE` — Account-level Passcode (NEVER in code or GitHub)

**Note:** Account ID and Token are in clevertap.js (committed to GitHub) — they are public by design (visible in browser DevTools). Only the Passcode is truly secret.

---

## DUMMY DATA

**Scripts:** dummy_users_data_scripts/
**Result:** 15,000 profiles, 3,246,470 events, 0 errors

**Archetypes:**
- High Value (15%) — 8-20 purchases, high AOV, mostly Prime
- Regular (40%) — 3-8 purchases, moderate AOV
- Occasional (30%) — 1-3 purchases, low AOV
- Dormant (15%) — historical purchases, inactive 180-540 days

**Event history:** 2020-2025 (5 years)
**Seasonal spikes:** Diwali (Oct/Nov 3x), New Year (Jan 2x), Summer Sale (Jun/Jul 2x)

**Date property format:** Always use $D_EPOCH format for CT date properties
- Joined At: $D_EPOCH ✓
- Last Order Date: $D_EPOCH ✓
- DOB: $D_EPOCH ✓

**Recommendation seed events:** seed_recommendation_events.py
- 105,000 events (15,000 users × 7 events each)
- 5 Product Viewed + 2 Added to Cart per user
- All within last 170 days (inside CT's 180-day recommendation window)
- Covers all 85 products from bazario_product_catalog.csv

---

## MULTI-VERTICAL ARCHITECTURE STANDARD

Follow this for ALL verticals — web, Android, iOS:

**Rule 1 — User Identity:**
Different email domains per vertical for dummy users. Bazario uses realistic emails (already done).

**Rule 2 — Profile Properties:**
Common properties on all verticals + vertical-specific properties that never overlap.

**Rule 3 — Events:**
Unique event names per vertical. No event name should appear on two different verticals.

**Rule 4 — Vertical Tagging:**
Every event carries vertical property via ctEvent() helper.
Every profile has Vertical and Vertical Type as flat string properties.

**Rule 5 — Segments:**
Always filter by Vertical = [name] as primary filter.

**Rule 6 — Mobile Apps:**
Same event schema as web + app-specific events (App Installed, App Opened, Push Notification Clicked etc.)
App events carry additional properties: platform, app_version, os_version, device_model.

**Rule 7 — Dummy Data Scripts:**
Each vertical uses random.seed() for reproducibility.
Seeds: Fintech=100, Subscription=200, Foodtech=300.

---

## FIREBASE — COMPLETED (Phase 7)

**Firebase project:** ct-playground-0323
**Region:** asia-south1 (Mumbai)
**Console:** console.firebase.google.com

**Firestore collections:**
- users/{uid} — profile data (all verticals share this)
- carts/{uid} — cart items (bazario)
- wishlists/{uid} — wishlist items (bazario)
- orders/{uid}/items/{orderId} — order history (bazario)
- addresses/{uid} — saved addresses (bazario)

**File structure:**
- js/firebase.js — shared: config, init, fbAuth, fbDb, fbGetProfile, fbSetProfile
- ecommerce/js/firebase-bazario.js — bazario: cart, wishlist, orders, addresses, merge
- Future verticals: fintech/js/firebase-fintech.js etc.

**Auth pattern:**
- Firebase Auth (email/password) — passwords never stored in Firestore
- onAuthStateChanged is the source of truth for auth state on all pages
- localStorage bazario_user = session cache (uid + display fields only)
- clevertap.onUserLogin fires immediately after Firebase auth succeeds
- clevertap.logout() called on signOut to reset CT session

**Guest users:**
- Guest cart → localStorage key: bazario_cart_guest
- Guest wishlist → localStorage key: bazario_wishlist_guest
- On login → fbMergeGuestData() merges into Firestore and clears localStorage

**Cart item sanitization:**
- sanitizeCartItem() strips specs/highlights/tags/variants before Firestore write
- Firestore does not support nested arrays — specs field was the culprit
- Applied inside fbSetCart() in firebase-bazario.js

**Security rules:**
- Users can only read/write their own documents (uid match enforced)
- API key is safe to commit — security enforced via Firestore Rules
- API key restricted to ctplayground.netlify.app and localhost domains

**Android/iOS apps (Phases 8-9):**
- Register as separate apps in same Firebase project (CT Playground Android/iOS)
- Same Firestore database and Auth pool shared
- Each gets own google-services.json / GoogleService-Info.plist

---

## ANDROID/iOS PLAN (Phases 8-9)

**Tools:**
- Android: Android Studio + Kotlin + Firebase Android SDK + CT Android SDK
- iOS: Xcode + Swift/SwiftUI + Firebase iOS SDK + CT iOS SDK

**Abhay has a Mac** — both Android and iOS development possible.
**Android first**, then iOS.

**App-specific CT events:**
App Installed, App Opened, App Backgrounded, App Crashed,
Push Notification Received/Clicked/Dismissed,
In-App Shown/Clicked/Dismissed, Deep Link Opened

**Multi-device demo:**
Same user logs into web + Android + iOS → single CT profile with events from all platforms.

---

## CURRENT STATUS (as of May 2026)

**Last completed:** Phase 7 — Firebase Backend Migration
**Currently working on:** Phase 8 — Bazario Android App (Kotlin)
**Pending commits:** None — all committed
**Netlify:** Live at ctplayground.netlify.app

---

## IMPORTANT NOTES FOR CLAUDE

1. Always refer back to architectural decisions above — don't suggest approaches that conflict with them
2. Batch commits — suggest one commit per session, not per change
3. Provide step-by-step granular guidance for all technical tasks
4. When building new verticals, follow the multi-vertical architecture standard exactly
5. The CT web popup pattern is confirmed working — always use postMessage approach
6. Never suggest putting CT Passcode in frontend code or GitHub
7. Firebase migration is COMPLETE (Phase 7). localStorage is only used as session cache (bazario_user). All data reads/writes go to Firestore. Guest users use localStorage with _guest suffix keys.
8. All date properties in CT must use $D_EPOCH format
9. Always check if a change affects multiple pages/files and flag them all upfront
10. Use the SAME conversation throughout a phase. Start a NEW conversation only when moving to a new phase.
11. Email channel — Skip for now. Revisit in Phase 14 when custom domain is set up. Requires verified sending domain, DNS records (SPF, DKIM, DMARC), and ESP integration (recommend SendGrid free tier or Amazon SES).
12. auth.js is NOT loaded on demo.html — popup postMessage handler added directly in demo.html. Dismissing reloads the page to kill CT overlay (reliable because sessionStorage preserves PIN).
13. Bulletins API requires c-by = actual CT admin email (not placeholder). Account-level Passcode required (not User Passcode). Admin role required on CT account.
14. Web Inbox demo campaigns use demo-specific events (Demo Offer Triggered, Demo Order Update Triggered). These events must exist in CT before campaign creation — seed by firing once from demo.html.
15. Recommendations use KV pair approach (not Custom HTML). CT resolves liquid tags server-side and sends plain string values. 25 KV pairs per campaign (topic + 8 fields × 3 slots).
16. Native Display campaigns fire on destination page, not source page — prevents duplicate events.
17. RC variables use per-user localStorage key WZRK_PE_${email} — synced after CT SDK loads via fetchVariables().
18. clevertap.js should be committed to GitHub (Account ID and Token are public by design).
19. Web Push requires HTTPS — only works on Netlify, not Live Server. VAPID keys stored in CT Dashboard. Account-level Passcode required for API push.
20. demo.html Channel Demos section: SDK channels have 🔴 Fire Live buttons that fire CT SDK events directly on demo.html. CT campaigns respond and render inline. API channels (Web Push, Bulletins) call Netlify proxy → CT API.
21. Firebase API key is safe to commit to GitHub — security enforced via Firestore Rules and domain restrictions set in Google Cloud Console. Same principle as CT Account ID.
22. Always call clevertap.logout() alongside fbAuth.signOut() — without it, CT session persists and can corrupt the next user's profile via identity stitching.
23. Cart items must be sanitized before Firestore writes via sanitizeCartItem() — product objects contain nested arrays (specs field) which Firestore forbids.
24. For future verticals, create vertical-specific firebase-{vertical}.js files. Never add vertical-specific helpers to js/firebase.js — it must stay lean and shared.