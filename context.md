# CT Playground — Project Context Document
# Last updated: April 2026
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
| Data storage | localStorage (Firebase migration planned — Phase 7) |
| Data scripts | Python 3 + requests library |
| Version control | GitHub → Netlify auto-deploy |

---

## PROJECT STRUCTURE

```
ct-playground/
├── README.md                        # Project overview for GitHub
├── index.html                       # Vertical selector homepage
├── demo.html                        # Shared demo console (PIN protected)
├── netlify.toml                     # Netlify build config + proxy redirects
│
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
│       └── auth.js                  # Shared navbar, counts, popup handler
│
├── netlify/
│   └── functions/
│       └── ct-proxy.js              # CT API proxy (passcode server-side)
│
└── data-scripts/                    # Python dummy data scripts
    ├── README.md
    ├── generate_and_upload.py       # Main dummy data script
    ├── update_vertical_from_csv.py  # Push Vertical property from CSV
    ├── create_cart_abandoners.py    # Cart abandonment events
    └── fix_joined_at.py             # Fix date properties to $D_EPOCH
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

### 🔄 Phase 6 — CT Channel Integration (IN PROGRESS)
**Campaign 1 — Welcome Offer ✅ COMPLETE**
- Trigger: User Signed Up
- Custom HTML popup with Bazario branding
- Events: Web Popup Viewed, Web Popup Clicked, Web Popup Dismissed
- Profile properties: Last Popup Seen, Last Popup Clicked At, Last Popup Dismissed At
- Working correctly on Netlify

**Campaign 2 — Cart Abandonment ⬅ NEXT**
- Trigger: TBD
- Target: Cart Abandoners segment

**Campaign 3 — Re-engagement**
- Trigger: TBD
- Target: Dormant segment

**Campaign 4 — High Value Upsell**
- Trigger: Product Viewed where price > 10000
- Target: High Value segment

**Remaining channels after pop-ups:**
- Exit Intent
- Web Push (Fire Live via CT API)
- Native Display
- Web Inbox (Fire Live via CT API)
- Bulletins (Fire Live via CT API)
- Product Experiences
- Recommendations
- Catalogs

### 🔲 Phase 7 — Firebase Backend Migration
- Move cart, wishlist, orders, addresses from localStorage → Firestore
- Firebase Auth replaces simulated login (email/password — no OTP simulation)
- Multi-device sync enabled

### 🔲 Phase 8 — Bazario Android App (Kotlin)
### 🔲 Phase 9 — Bazario iOS App (Swift)
### 🔲 Phase 10 — Fintech Vertical (Web + Android + iOS)
### 🔲 Phase 11 — Subscription Vertical
### 🔲 Phase 12 — Foodtech Vertical
### 🔲 Phase 13 — Vertical Selector Homepage Upgrade
### 🔲 Phase 14 — Custom Domain

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

**account.html fix:**
- Auth page div is hidden by default (style="display:none")
- showAuthPage() sets display to flex explicitly
- Prevents flash of login page on redirect

### Redirect destinations per campaign:
| Campaign | CTA redirect | Dismiss redirect |
|---|---|---|
| Welcome Offer | index.html | account.html |
| Cart Abandonment | cart.html | same page (window.location.href) |
| Re-engagement | index.html | same page |
| High Value Upsell | cart.html | same page |

### demo.html Channel Demos Approach
**SDK-driven channels** (Web Pop-ups, Exit Intent, Native Display, Product Experiences, Recommendations):
- [Preview] button → inject popup HTML directly into demo.html as overlay
- [How to Trigger] → explains what real user action fires the campaign

**API-driven channels** (Web Push, Web Inbox, Bulletins):
- [Preview] button → same as above
- [Fire Live] button → 10 second countdown → Netlify proxy → CT API

### Storage Keys (localStorage)
- `bazario_user` — current logged-in user session
- `bazario_profile_${email}` — persists user profile across logout
- `bazario_cart_${email}` / `bazario_cart_guest` — per-user cart
- `bazario_wishlist_${email}` / `bazario_wishlist_guest` — per-user wishlist
- `bazario_orders_${email}` — per-user orders
- `bazario_addresses_${email}` — per-user addresses
- `ct_active_vertical` — active vertical for demo.html context

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

### Profile Properties
```
Common: Name, Email, Phone, Gender, DOB, City, MSG-email, MSG-sms, MSG-push,
        Vertical, Vertical Type, Joined At ($D_EPOCH format)

Bazario-specific: Is Prime, Preferred Category, Total Orders, Lifetime Value,
                  Last Order Date ($D_EPOCH), Last Order Value,
                  Last Popup Seen, Last Popup Seen At ($D_EPOCH),
                  Last Popup Clicked, Last Popup Clicked At ($D_EPOCH),
                  Last Popup Dismissed, Last Popup Dismissed At ($D_EPOCH)
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

## NETLIFY PROXY

**Endpoints:**
- `POST /ct-proxy/upload` → CT Upload API (write events + profiles)
- `GET /ct-proxy/profile?email=` → CT Profile API (read any user)

**Environment variables in Netlify:**
- `CT_ACCOUNT_ID` — Account ID
- `CT_PASSCODE` — Passcode (NEVER in code or GitHub)

**Note:** Account ID and Token are in clevertap.js (committed to GitHub) — they are public by design (visible in browser DevTools). Only the Passcode is truly secret.

---

## DUMMY DATA

**Script:** data-scripts/generate_and_upload.py
**Result:** 15,000 profiles, 3,246,470 events, 0 errors

**Archetypes:**
- High Value (15%) — 8-20 purchases, high AOV, mostly Prime
- Regular (40%) — 3-8 purchases, moderate AOV
- Occasional (30%) — 1-3 purchases, low AOV
- Dormant (15%) — historical purchases, inactive 180-540 days

**Event history:** 2020-2025 (5 years)
**Seasonal spikes:** Diwali (Oct/Nov 3x), New Year (Jan 2x), Summer Sale (Jun/Jul 2x)

**Date property format:** Always use $D_EPOCH format for CT date properties
- Joined At: $D_EPOCH ✓ (fixed via fix_joined_at.py)
- Last Order Date: $D_EPOCH ✓ (fixed via fix_joined_at.py)
- DOB: $D_EPOCH ✓

**Vertical property:** Vertical: Bazario pushed to all 15,000 profiles

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

## FIREBASE MIGRATION PLAN (Phase 7)

**What moves to Firestore:**
- User profiles → users collection
- Cart → carts collection (keyed by user ID)
- Wishlist → wishlists collection
- Orders → orders collection
- Addresses → addresses collection

**What stays the same:**
- All CT event calls — completely unchanged
- All CT channel integrations — completely unchanged

**Authentication:**
- Email/password via Firebase Auth (no OTP simulation)
- Google Sign-In optional
- Same CT onUserLogin call after Firebase auth succeeds

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

## CURRENT STATUS (as of April 2026)

**Last completed:** Campaign 1 — Welcome Offer popup (Phase 6)
**Currently working on:** Campaign 2 — Cart Abandonment popup
**Pending after campaigns:** Wire up demo.html Channel Demos section

**Pending commits:** None — all committed
**Netlify:** Live at ctplayground.netlify.app

---

## PHASE 6 STATUS — CT Channel Integration

### ✅ Completed Channels
1. Web Pop-ups (4 campaigns: Welcome Offer, Cart Abandonment, Re-engagement, High Value Upsell)
2. Exit Intent (3 campaigns: Cart, Checkout with auto-discount, Product with auto add-to-cart)
3. Native Display (31 campaigns across Homepage/Listing/Product/Confirmation pages, KV pair approach)
4. Product Experiences — RC (43 variables, Phase A + Phase B fully complete across all pages)

### 🔄 In Progress — Channel 5: Recommendations
- Bazario product catalog uploaded to CT (85 products, 8 categories)
- Catalog mapped to Product Viewed + Added to Cart events
- Two recommendations created and published:
  - Bazario - Similar Products (based on Product Viewed, 30 days)
  - Bazario - Frequently Added Together (based on Added to Cart, 30 days)
- Added recommendation divs to product.html and cart.html:
  - product.html: #ct-native-display-recommendations-product
  - cart.html: #ct-native-display-recommendations-cart
- NEXT STEP: Create CT Native Display campaigns using recommendation content
  - Campaign 1: [Bazario] ND Rec — Similar Products → product.html
  - Campaign 2: [Bazario] ND Rec — Frequently Added Together → cart.html
  - Need to create 3 recommendation blocks per campaign in CT
  - Share liquid tags screenshot → get HTML from Claude → publish

### 🔲 Remaining Channels
6. Catalogs
7. Web Push
8. Web Inbox
9. Bulletins
10. demo.html Channel Demos section (Phase B)

### Key Technical Decisions Made
- Native Display uses KV pair approach (not Custom HTML) for category banners
- RC uses WZRK_PE_${email} per-user localStorage pattern
- isPrime sync fix: syncPrimeFromCT awaited BEFORE ctIdentifyUser in completeLogin
- postMessage handler in auth.js is fully payload-driven
- clevertap.js uses new SDK from jsdelivr CDN with enablePersonalization: true

---

## IMPORTANT NOTES FOR CLAUDE

1. Always refer back to architectural decisions above — don't suggest approaches that conflict with them
2. Batch commits — suggest one commit per session, not per change
3. Provide step-by-step granular guidance for all technical tasks
4. When building new verticals, follow the multi-vertical architecture standard exactly
5. The CT web popup pattern is confirmed working — always use postMessage approach
6. Never suggest putting CT Passcode in frontend code or GitHub
7. Firebase migration happens in Phase 7 — don't suggest localStorage alternatives before then
8. demo.html channel demos: SDK channels get Preview + instructions, API channels get Preview + Fire Live
9. All date properties in CT must use $D_EPOCH format
10. Always check if a change affects multiple pages/files and flag them all upfront
11. Conversation management — Use the SAME conversation throughout a phase. Start a NEW conversation only when moving to a new phase.