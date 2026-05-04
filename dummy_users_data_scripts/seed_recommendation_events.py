#!/usr/bin/env python3
"""
seed_recommendation_events.py

Generates recent Product Viewed + Added to Cart events for all dummy users
so that CT's recommendation engine has data within the 180-day window.

Covers all 85 products from bazario_product_catalog.csv — the exact set
uploaded to the CT catalog — so recommendations fire for every product page.

Usage:
    export CT_ACCOUNT_ID=your-account-id
    export CT_PASSCODE=your-passcode
    python3 dummy_users_data_scripts/seed_recommendation_events.py

Run from: ct-playground/ root directory
CSV expected at: ecommerce/user_profiles.csv
"""

import csv
import json
import os
import random
import time
from datetime import datetime, timedelta
import ssl
ssl._create_default_https_context = ssl._create_unverified_context

# =============================================================================
# CONFIG
# =============================================================================

CONFIG = {
    "csv_path":        "ecommerce/user_profiles.csv",
    "checkpoint_file": "rec_seed_checkpoint.json",
    "batch_size":      200,
    "rate_limit":      0.3,
    "views_per_user":  5,
    "carts_per_user":  2,
    "days_window":     170,
}

CT_ACCOUNT_ID = os.environ.get("CT_ACCOUNT_ID", "")
CT_PASSCODE   = os.environ.get("CT_PASSCODE", "")

if not CT_ACCOUNT_ID or not CT_PASSCODE:
    print("ERROR: CT_ACCOUNT_ID and CT_PASSCODE environment variables must be set.")
    exit(1)

HEADERS = {
    "X-CleverTap-Account-Id": CT_ACCOUNT_ID,
    "X-CleverTap-Passcode":   CT_PASSCODE,
    "Content-Type":           "application/json",
}

CT_UPLOAD_URL = "https://api.clevertap.com/1/upload"

# =============================================================================
# PRODUCTS — sourced from bazario_product_catalog.csv (85 products)
# These are the exact products in the CT catalog. IDs must match so CT
# can compute co-occurrence for recommendations across ALL product pages.
# =============================================================================

PRODUCTS = {
    "Electronics": [
        {"id": "E001", "name": "Samsung Galaxy M14 5G",                "brand": "Samsung",  "price": 10999},
        {"id": "E002", "name": "Apple iPhone 13 128GB Midnight",        "brand": "Apple",    "price": 49999},
        {"id": "E003", "name": "OnePlus Nord CE3 Lite 5G",              "brand": "OnePlus",  "price": 17999},
        {"id": "E004", "name": "boAt Rockerz 450 Bluetooth Headphone",  "brand": "boAt",     "price": 999},
        {"id": "E005", "name": "MI 10000mAh Power Bank",                "brand": "Xiaomi",   "price": 849},
        {"id": "E006", "name": "Samsung 55 Crystal 4K UHD Smart TV",    "brand": "Samsung",  "price": 42990},
        {"id": "E007", "name": "Canon EOS 1500D 24.1MP DSLR Camera",    "brand": "Canon",    "price": 34990},
        {"id": "E008", "name": "HP 15s Core i5 12th Gen Laptop",        "brand": "HP",       "price": 49990},
        {"id": "E009", "name": "Logitech MK215 Wireless Keyboard+Mouse","brand": "Logitech", "price": 999},
        {"id": "E010", "name": "Sony WH-1000XM5 Noise Cancelling",      "brand": "Sony",     "price": 24990},
        {"id": "E011", "name": "Apple AirPods Pro 2nd Gen",             "brand": "Apple",    "price": 19900},
        {"id": "E012", "name": "Samsung 970 EVO Plus 500GB SSD",        "brand": "Samsung",  "price": 4999},
        {"id": "E013", "name": "GoPro HERO11 Black Action Camera",      "brand": "GoPro",    "price": 34990},
        {"id": "E014", "name": "Anker 65W GaN Fast Charger USB-C",      "brand": "Anker",    "price": 1999},
        {"id": "E015", "name": "JBL Flip 6 Portable Bluetooth Speaker", "brand": "JBL",      "price": 9999},
    ],
    "Fashion": [
        {"id": "F001", "name": "Levi's 511 Slim Fit Men Jeans",         "brand": "Levi's",        "price": 2399},
        {"id": "F002", "name": "Allen Solly Men Slim Fit Formal Shirt",  "brand": "Allen Solly",   "price": 899},
        {"id": "F003", "name": "Nike Air Max 270 Running Shoes",         "brand": "Nike",          "price": 6995},
        {"id": "F004", "name": "W Women Floral Printed Kurta",           "brand": "W",             "price": 899},
        {"id": "F005", "name": "Peter England Men Regular Fit Chinos",   "brand": "Peter England", "price": 1199},
        {"id": "F006", "name": "Wildcraft 33L Rucksack Backpack",        "brand": "Wildcraft",     "price": 1299},
        {"id": "F007", "name": "Fossil Gen 6 Smartwatch Brown Leather",  "brand": "Fossil",        "price": 14995},
        {"id": "F008", "name": "Fastrack Analog Watch Black Dial",       "brand": "Fastrack",      "price": 1295},
        {"id": "F009", "name": "Baggit Women PU Tote Bag Tan Brown",     "brand": "Baggit",        "price": 1499},
        {"id": "F010", "name": "Woodland Men Leather Casual Shoes",      "brand": "Woodland",      "price": 3299},
    ],
    "Grocery": [
        {"id": "G001", "name": "Tata Salt Iodised 1kg Pack of 6",        "brand": "Tata",       "price": 162},
        {"id": "G002", "name": "Aashirvaad Atta Whole Wheat 10kg",       "brand": "Aashirvaad", "price": 379},
        {"id": "G003", "name": "Amul Butter Pasteurised 500g",           "brand": "Amul",       "price": 265},
        {"id": "G004", "name": "Nescafe Classic Instant Coffee 200g",    "brand": "Nescafe",    "price": 499},
        {"id": "G005", "name": "Fortune Sunflower Oil 5L",               "brand": "Fortune",    "price": 689},
        {"id": "G006", "name": "Tata Tea Premium 500g",                  "brand": "Tata Tea",   "price": 249},
        {"id": "G007", "name": "Maggi 2-Minute Masala Noodles Pack 12",  "brand": "Maggi",      "price": 156},
        {"id": "G008", "name": "Dabur Honey 500g",                       "brand": "Dabur",      "price": 199},
        {"id": "G009", "name": "Himalaya Chyavanprash 1kg",              "brand": "Himalaya",   "price": 299},
        {"id": "G010", "name": "Haldirams Bhujia Sev 1kg",               "brand": "Haldirams",  "price": 299},
    ],
    "Home & Furniture": [
        {"id": "H001", "name": "Wakefit Orthopedic Memory Foam Mattress", "brand": "Wakefit",       "price": 12999},
        {"id": "H002", "name": "Amazon Basics Microfiber Pillow Pack 2",  "brand": "Amazon Basics", "price": 599},
        {"id": "H003", "name": "Cello Checkers Dining Chair Set of 4",    "brand": "Cello",         "price": 3999},
        {"id": "H004", "name": "Philips 9W LED Bulb Pack of 10",          "brand": "Philips",       "price": 399},
        {"id": "H005", "name": "Solimo 700 Thread Count Bedsheet Double",  "brand": "Solimo",        "price": 899},
        {"id": "H006", "name": "Pigeon Plastic Foldable Dining Table",     "brand": "Pigeon",        "price": 2999},
        {"id": "H007", "name": "Godrej Interio Steel Almirah 2-Door",      "brand": "Godrej",        "price": 8999},
        {"id": "H008", "name": "Story@Home Blackout Curtain 5ft Pack 2",   "brand": "Story@Home",    "price": 799},
        {"id": "H009", "name": "Borosil Vision Glass Set 350ml 6pcs",      "brand": "Borosil",       "price": 399},
        {"id": "H010", "name": "Milton Thermosteel Flip Lid Flask 1L",     "brand": "Milton",        "price": 699},
    ],
    "Appliances": [
        {"id": "A001", "name": "LG 8kg 5 Star Inverter Washing Machine",  "brand": "LG",           "price": 34990},
        {"id": "A002", "name": "Prestige Iris 750W Mixer Grinder 3 Jars", "brand": "Prestige",     "price": 1795},
        {"id": "A003", "name": "Voltas 1.5 Ton 5 Star Inverter Split AC", "brand": "Voltas",       "price": 38990},
        {"id": "A004", "name": "Philips HL7777 600W Mixer Grinder",        "brand": "Philips",      "price": 2499},
        {"id": "A005", "name": "Bajaj Majesty 1000W Dry Iron",             "brand": "Bajaj",        "price": 499},
        {"id": "A006", "name": "Havells 1200mm Ceiling Fan Bianco",        "brand": "Havells",      "price": 2799},
        {"id": "A007", "name": "Orient Areva 1200W Hair Dryer",            "brand": "Orient",       "price": 799},
        {"id": "A008", "name": "Crompton Arno Neo 15L Water Heater",       "brand": "Crompton",     "price": 6499},
        {"id": "A009", "name": "Eureka Forbes Aquaguard Marvel RO+UV",     "brand": "Eureka Forbes","price": 14999},
        {"id": "A010", "name": "Godrej 184L 3 Star Direct-Cool Fridge",    "brand": "Godrej",       "price": 13990},
    ],
    "Beauty": [
        {"id": "B001", "name": "Lakme Eyeconic Kajal Black",              "brand": "Lakme",             "price": 149},
        {"id": "B002", "name": "L'Oreal Paris Elvive Shampoo 650ml",      "brand": "L'Oreal",           "price": 399},
        {"id": "B003", "name": "Mamaearth Onion Hair Mask 200ml",         "brand": "Mamaearth",         "price": 349},
        {"id": "B004", "name": "Nykaa SkinShield Foundation",             "brand": "Nykaa",             "price": 549},
        {"id": "B005", "name": "Forest Essentials Rose Water 100ml",      "brand": "Forest Essentials", "price": 695},
        {"id": "B006", "name": "Dove Body Lotion Deeply Nourishing 400ml","brand": "Dove",              "price": 249},
        {"id": "B007", "name": "MCaffeine Coffee Face Scrub 100g",        "brand": "MCaffeine",         "price": 349},
        {"id": "B008", "name": "Biotique Bio Kelp Protein Shampoo 340ml", "brand": "Biotique",          "price": 175},
        {"id": "B009", "name": "Plum Grape Seed Sunscreen SPF 50",        "brand": "Plum",              "price": 395},
        {"id": "B010", "name": "Lakme Absolute Matte Revolution Lip Color","brand": "Lakme",            "price": 599},
    ],
    "Sports": [
        {"id": "S001", "name": "Nivia Football Storm Size 5",             "brand": "Nivia",     "price": 599},
        {"id": "S002", "name": "Yonex Voltric 1DG Badminton Racket",      "brand": "Yonex",     "price": 2499},
        {"id": "S003", "name": "Cosco Cricket Bat Kashmir Willow",        "brand": "Cosco",     "price": 899},
        {"id": "S004", "name": "Domyos Fitness Mat 8mm",                  "brand": "Domyos",    "price": 799},
        {"id": "S005", "name": "Skullcandy Jib True Wireless Earbuds",    "brand": "Skullcandy","price": 1999},
        {"id": "S006", "name": "Boldfit Gym Gloves with Wrist Support",   "brand": "Boldfit",   "price": 349},
        {"id": "S007", "name": "Nivia Marathon Running Shoes Men",        "brand": "Nivia",     "price": 1499},
        {"id": "S008", "name": "SG Cricket Helmet",                       "brand": "SG",        "price": 1299},
        {"id": "S009", "name": "Strauss Adjustable Dumbbell Set 20kg",    "brand": "Strauss",   "price": 2999},
        {"id": "S010", "name": "Reebok Men Cardio Motion Training Shoes", "brand": "Reebok",    "price": 2499},
    ],
    "Books": [
        {"id": "BK001", "name": "Atomic Habits — James Clear",                 "brand": "James Clear",    "price": 399},
        {"id": "BK002", "name": "The Psychology of Money — Morgan Housel",     "brand": "Morgan Housel",  "price": 349},
        {"id": "BK003", "name": "Rich Dad Poor Dad — Robert Kiyosaki",         "brand": "Robert Kiyosaki","price": 299},
        {"id": "BK004", "name": "The Alchemist — Paulo Coelho",                "brand": "Paulo Coelho",   "price": 199},
        {"id": "BK005", "name": "Ikigai — Hector Garcia & Francesc Miralles",  "brand": "Hector Garcia",  "price": 199},
        {"id": "BK006", "name": "Wings of Fire — APJ Abdul Kalam",             "brand": "APJ Abdul Kalam","price": 149},
        {"id": "BK007", "name": "The 5 AM Club — Robin Sharma",                "brand": "Robin Sharma",   "price": 349},
        {"id": "BK008", "name": "Zero to One — Peter Thiel",                   "brand": "Peter Thiel",    "price": 349},
        {"id": "BK009", "name": "Deep Work — Cal Newport",                     "brand": "Cal Newport",    "price": 399},
        {"id": "BK010", "name": "Class 10 Mathematics NCERT Textbook",         "brand": "NCERT",          "price": 75},
    ],
}

CATEGORIES       = list(PRODUCTS.keys())
CATEGORY_WEIGHTS = [25, 20, 15, 12, 10, 8, 5, 5]

# =============================================================================
# HELPERS
# =============================================================================

def to_epoch(dt):
    return int(dt.timestamp())


def random_recent_ts():
    days_ago = random.uniform(1, CONFIG["days_window"])
    return datetime.now() - timedelta(days=days_ago)


def weighted_choice(choices, weights):
    total = sum(weights)
    r = random.uniform(0, total)
    cumulative = 0
    for choice, weight in zip(choices, weights):
        cumulative += weight
        if r <= cumulative:
            return choice
    return choices[-1]


def load_checkpoint():
    if os.path.exists(CONFIG["checkpoint_file"]):
        with open(CONFIG["checkpoint_file"]) as f:
            return json.load(f)
    return {"users_done": 0, "events_uploaded": 0}


def save_checkpoint(data):
    with open(CONFIG["checkpoint_file"], "w") as f:
        json.dump(data, f)


def progress_bar(done, total, label=""):
    pct = done / total if total else 0
    bar = int(pct * 40)
    print(f"\r  {label} [{'█'*bar}{'░'*(40-bar)}] {done:,}/{total:,} ({pct:.1%})", end="", flush=True)


# =============================================================================
# READ CSV
# =============================================================================

def load_emails():
    """Read all valid emails from user_profiles.csv (columns: Gender, Email)."""
    emails   = []
    csv_path = CONFIG["csv_path"]

    if not os.path.exists(csv_path):
        print(f"ERROR: CSV not found at {csv_path}")
        print("  Make sure you're running from the ct-playground/ root directory.")
        exit(1)

    with open(csv_path, newline="", encoding="utf-8") as f:
        reader = csv.reader(f)
        for row in reader:
            if len(row) >= 2:
                email = row[1].strip()
                if email and "@" in email:
                    emails.append(email)

    print(f"  Loaded {len(emails):,} valid emails from CSV")
    return emails


# =============================================================================
# EVENT GENERATION
# =============================================================================

def generate_events_for_user(email):
    """
    Generate Product Viewed + Added to Cart events for one user.
    All timestamps fall within the last CONFIG[days_window] days.
    """
    events = []

    view_categories = random.choices(
        CATEGORIES,
        weights=CATEGORY_WEIGHTS,
        k=CONFIG["views_per_user"]
    )

    viewed_products = []

    for category in view_categories:
        product = random.choice(PRODUCTS[category])
        ts      = random_recent_ts()

        events.append({
            "type":     "event",
            "identity": email,
            "evtName":  "Product Viewed",
            "evtData":  {
                "product_id":   product["id"],
                "product_name": product["name"],
                "category":     category,
                "brand":        product["brand"],
                "price":        product["price"],
                "source":       "listing_page",
                "vertical":     "bazario",
            },
            "ts": to_epoch(ts),
        })

        viewed_products.append((product, category, ts))

    # Added to Cart — subset of viewed products
    cart_items = random.sample(
        viewed_products,
        min(CONFIG["carts_per_user"], len(viewed_products))
    )

    for product, category, viewed_ts in cart_items:
        cart_ts = viewed_ts + timedelta(minutes=random.randint(2, 8))
        events.append({
            "type":     "event",
            "identity": email,
            "evtName":  "Added to Cart",
            "evtData":  {
                "product_id":   product["id"],
                "product_name": product["name"],
                "category":     category,
                "brand":        product["brand"],
                "price":        product["price"],
                "quantity":     random.randint(1, 2),
                "cart_value":   product["price"] * random.randint(1, 2),
                "source":       "product_page",
                "vertical":     "bazario",
            },
            "ts": to_epoch(cart_ts),
        })

    return events


# =============================================================================
# UPLOAD
# =============================================================================

def upload_batch(records):
    import urllib.request
    import urllib.error

    payload = json.dumps({"d": records}).encode("utf-8")
    req     = urllib.request.Request(CT_UPLOAD_URL, data=payload, headers=HEADERS, method="POST")

    for attempt in range(3):
        try:
            ctx = ssl.create_default_context()
            ctx.check_hostname = False
            ctx.verify_mode = ssl.CERT_NONE
            with urllib.request.urlopen(req, timeout=30, context=ctx) as resp:
                result      = json.loads(resp.read().decode())
                processed   = result.get("processed",   0)
                unprocessed = result.get("unprocessed", [])
                return processed, unprocessed
        except urllib.error.HTTPError as e:
            if e.code == 429:
                print(f"\n  Rate limited — waiting 5s (attempt {attempt+1}/3)")
                time.sleep(5)
            else:
                print(f"\n  HTTP {e.code} — {e.reason}")
                return 0, records
        except Exception as ex:
            print(f"\n  Upload error: {ex}")
            if attempt < 2:
                time.sleep(2)

    return 0, records


# =============================================================================
# MAIN
# =============================================================================

def main():
    random.seed(42)

    print("\n" + "="*60)
    print("  CT Playground — Recommendation Event Seeder")
    print("="*60)

    total_catalog = sum(len(v) for v in PRODUCTS.values())
    print(f"\n  Catalog coverage:  {total_catalog} products across {len(CATEGORIES)} categories")
    print(f"  Views per user:    {CONFIG['views_per_user']}")
    print(f"  Carts per user:    {CONFIG['carts_per_user']}")
    print(f"  Days window:       last {CONFIG['days_window']} days")
    print(f"  Batch size:        {CONFIG['batch_size']}")

    print(f"\n{'─'*60}")
    print("  STEP 1: Loading emails from CSV")
    print(f"{'─'*60}\n")
    emails = load_emails()

    total_users  = len(emails)
    events_per_u = CONFIG["views_per_user"] + CONFIG["carts_per_user"]
    print(f"  Total users:          {total_users:,}")
    print(f"  Events per user:      {events_per_u}")
    print(f"  Total events to push: ~{total_users * events_per_u:,}")

    checkpoint      = load_checkpoint()
    users_done      = checkpoint.get("users_done", 0)
    events_uploaded = checkpoint.get("events_uploaded", 0)

    if users_done > 0:
        print(f"\n  Resuming from checkpoint — {users_done:,} users already done")

    print(f"\n{'─'*60}")
    print("  STEP 2: Generating and uploading events")
    print(f"{'─'*60}\n")

    all_errors       = []
    pending          = []
    remaining_emails = emails[users_done:]

    for i, email in enumerate(remaining_emails):
        pending.extend(generate_events_for_user(email))

        while len(pending) >= CONFIG["batch_size"]:
            batch   = pending[:CONFIG["batch_size"]]
            pending = pending[CONFIG["batch_size"]:]

            processed, unprocessed = upload_batch(batch)
            events_uploaded += processed
            if unprocessed:
                all_errors.append({"count": len(unprocessed)})

            checkpoint["users_done"]      = users_done + i + 1
            checkpoint["events_uploaded"] = events_uploaded
            save_checkpoint(checkpoint)
            time.sleep(CONFIG["rate_limit"])

        progress_bar(users_done + i + 1, total_users, "  Uploading")

    # Flush remaining
    if pending:
        processed, unprocessed = upload_batch(pending)
        events_uploaded += processed
        if unprocessed:
            all_errors.append({"count": len(unprocessed)})

    checkpoint["users_done"]      = total_users
    checkpoint["events_uploaded"] = events_uploaded
    save_checkpoint(checkpoint)

    print(f"\n\n{'='*60}")
    print("  DONE")
    print(f"{'='*60}")
    print(f"  Events uploaded:  {events_uploaded:,}")
    print(f"  Error batches:    {len(all_errors)}")
    if all_errors:
        print("  ⚠ Some records failed — check CT dashboard")
    print(f"\n  Next steps:")
    print(f"  1. Wait 1-2 hours for CT to recompute recommendations")
    print(f"  2. CT → Recommendations → 'Bazario - Similar Products'")
    print(f"     'Total items recommended' should now be > 0")
    print(f"  3. Test product.html — the Similar Products strip should render\n")


if __name__ == "__main__":
    main()
