# CT Playground

A multi-vertical CleverTap integration sandbox for learning, demo, and pre-sales use.

Live at → **[ctplayground.netlify.app](https://ctplayground.netlify.app)**

---

## What This Is

CT Playground is a fully integrated demo environment built on top of CleverTap. It simulates four real-world business verticals — each with a working website, live CT event tracking, dummy user data, and a shared demo console for prospect calls.

Built by a Solutions Engineer to demonstrate CleverTap's capabilities across industries — from ecommerce to fintech — with real data flowing into a live CT dashboard.

---

## Verticals

| Vertical | Brand | Status | URL |
|---|---|---|---|
| Ecommerce | Bazario | ✅ Live | `/ecommerce` |
| Fintech | TBD | 🔲 Coming Soon | `/fintech` |
| Subscription | TBD | 🔲 Coming Soon | `/subscription` |
| Foodtech | TBD | 🔲 Coming Soon | `/foodtech` |

---

## Project Structure

```
ct-playground/
│
├── index.html                               # Vertical selector homepage
├── demo.html                                # Shared demo console (PIN protected)
├── netlify.toml                             # Netlify build config + proxy redirects
│
├── ecommerce/                               # Bazario ecommerce vertical
│   ├── index.html                           # Homepage with carousel
│   ├── listing.html                         # Category listing with filters + pagination
│   ├── product.html                         # Product detail page
│   ├── cart.html                            # Cart with coupons
│   ├── checkout.html                        # Multi-step checkout
│   ├── confirmation.html                    # Order confirmation
│   ├── account.html                         # Login, signup, profile
│   ├── wishlist.html                        # Wishlist management
│   └── js/
│       ├── catalogue.js                     # 104 products across 8 categories
│       ├── clevertap.js                     # CT SDK init + helper functions
│       └── auth.js                          # Shared navbar state (login, counts)
│
├── netlify/
│   └── functions/
│       └── ct-proxy.js                      # CT API proxy (keeps passcode server-side)
│
└── dummy_users_data_scripts/                # Dummy data generation scripts
    ├── README.md                            # Script usage instructions
    ├── generate_dummy_users_and_upload.py   # Generate + upload 15,000 users + events
    ├── update_vertical_from_csv.py          # Push Vertical property from CSV
    ├── create_cart_abandoners.py            # Create cart abandonment events
    └── fix_joined_at.py                     # Fix date properties to $D_EPOCH format
```

---

## What's Integrated

### Bazario — Ecommerce (Complete)

**Site features:**
- 8 fully working pages end to end
- 104 products across 8 categories
- Working search, filters, pagination, carousel
- Per-user cart, wishlist, orders, addresses
- Guest cart/wishlist merge on login
- Login gate before checkout with post-auth redirect

**CleverTap SDK integration:**
- 38 unique events across all 8 pages
- `onUserLogin` with full profile properties on signup/login
- `Charged` event with Items array on purchase
- `Total Orders` and `Lifetime Value` pushed after each purchase
- All events auto-tagged with `vertical: bazario`
- Beacon/replay duplicate event issue resolved

**Events tracked:**
```
User Signed Up          User Logged In          User Logged Out
Profile Updated         Preference Changed       Order History Viewed
Product Viewed          Category Viewed          Category Clicked
Banner Clicked          Product Searched         Filter Applied
Sort Applied            Listing Page Changed      Variant Selected
Added to Cart           Removed from Cart         Cart Viewed
Cart Quantity Updated   Saved for Later           Coupon Applied
Coupon Failed           Checkout Initiated        Checkout Page Viewed
Address Selected        Payment Method Selected   Payment Step Completed
Charged                 Order Confirmed           Order Rated
Added to Wishlist       Removed from Wishlist     Wishlist Viewed
Moved to Cart from Wishlist                       Add All Wishlist to Cart
Buy Now Clicked         Delivery Check
```

### Demo Console (`demo.html`)

- PIN-protected access
- Live event firing via CT SDK
- Event firing against any user via Netlify proxy
- User Profile Manager — load and push any CT profile
- Data Flow Visualiser — 6 animated flow diagrams
- External Integration payloads — Warehouse, CRM, Server-side, Historical
- Session event log
- Vertical-adaptive theme

### Netlify Proxy

Two serverless endpoints:
- `POST /ct-proxy/upload` → CT Upload API (events + profiles)
- `GET /ct-proxy/profile` → CT Profile API (read any user)

Passcode stored in Netlify environment variables — never in code.

### Dummy Data

- **15,000 Indian user profiles** across 4 archetypes
- **3,246,470 events** spanning 5 years (2020–2025)
- Seasonal purchase spikes — Diwali, New Year, Summer Sale
- Realistic Indian names, cities, phone numbers
- Pre-built segments — High Value, Dormant, Cart Abandoners, Prime Members and more
- Pre-built funnels — Core Purchase, Checkout Conversion, New User Activation

---

## Tech Stack

| Layer | Technology |
|---|---|
| Frontend | HTML + Vanilla JS + Tailwind CDN |
| Hosting | Netlify (auto-deploy from GitHub) |
| Serverless | Netlify Functions (Node.js) |
| Analytics | CleverTap Web SDK |
| Data storage | localStorage (Firebase migration planned) |
| Data scripts | Python 3 + requests |

---

## Setup

### Prerequisites
- VS Code
- Git
- Python 3 (for data scripts)
- Netlify account
- CleverTap account

### Local Development

```bash
# Clone the repo
git clone https://github.com/your-username/ct-playground.git
cd ct-playground

# clevertap.js is already in the repo with CT credentials
# Edit clevertap.js and add your Account ID and Token
# No additional setup needed for the SDK
```

Open `ecommerce/index.html` with Live Server in VS Code.

### Netlify Deployment

1. Connect repo to Netlify
2. Set environment variables in Netlify dashboard:
   - `CT_ACCOUNT_ID` — your CleverTap Account ID
   - `CT_PASSCODE` — your CleverTap Passcode
3. Push to GitHub — Netlify auto-deploys

### Dummy Data

```bash
cd data-scripts
pip3 install requests

export CT_ACCOUNT_ID=your-account-id
export CT_PASSCODE=your-passcode

python3 generate_and_upload.py
```

See `data-scripts/README.md` for full instructions.

---

## Roadmap

### Complete ✅
- Phase 1 — Foundation (GitHub + Netlify pipeline)
- Phase 2 — Bazario Web Site (8 pages, full ecommerce journey)
- Phase 3 — CT SDK Integration (38 events across all pages)
- Phase 4 — Demo Infrastructure (demo.html + Netlify proxy)
- Phase 5 — Dummy Data (15,000 users, 3.2M events, 15 segments)

### In Progress / Planned 🔲
- Phase 6 — CT Channel Integration (web push, pop-ups, inbox, recommendations)
- Phase 7 — Firebase Backend (replace localStorage with Firestore)
- Phase 8 — Bazario Android App (Kotlin + CT Android SDK)
- Phase 9 — Bazario iOS App (Swift + CT iOS SDK)
- Phase 10 — Fintech Vertical (web + Android + iOS)
- Phase 11 — Subscription Vertical
- Phase 12 — Foodtech Vertical
- Phase 13 — Vertical Selector Homepage (upgrade)
- Phase 14 — Custom Domain

---

## Multi-Vertical Architecture

All four verticals share one CT project. Users and data are kept separate by:

- **Unique email domains per vertical** — `@fintech-user.com`, `@subscription-user.com` etc.
- **Vertical-specific event names** — no event names overlap between verticals
- **Vertical profile property** — `Vertical: Bazario` on all profiles
- **Vertical auto-tagging** — every CT event carries `vertical: bazario` automatically

See the architecture standard documented in the codebase for full details.

---

## Note

`ecommerce/js/clevertap.js` is committed to this repository. The CT Account ID and Token it contains are **public by design** — they are visible to anyone who opens browser DevTools on the live site. They are write-only from the SDK perspective and cannot be used to read data from your CT dashboard.

The **CT Passcode** (used for server-side API calls) is never stored in any file in this repo. It lives exclusively in Netlify environment variables.

---

*Built for CleverTap demo and learning purposes.*
