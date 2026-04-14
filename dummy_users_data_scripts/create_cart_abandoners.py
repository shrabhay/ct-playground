#!/usr/bin/env python3
"""
=============================================================================
create_cart_abandoners.py — Create Cart Abandonment Events from CT CSV
=============================================================================

Reads email addresses from a CT exported profiles CSV and pushes
Checkout Initiated events (without Charged) for a subset of users,
creating realistic cart abandonment data for segment demos.

USAGE:
  export CT_ACCOUNT_ID=your-id
  export CT_PASSCODE=your-passcode
  python3 create_cart_abandoners.py --csv your_export.csv

CONFIGURATION:
  Edit CONFIG below to adjust abandonment rate and time window.
=============================================================================
"""

import os
import sys
import csv
import time
import random
import argparse
import requests
from datetime import datetime, timedelta

# =============================================================================
# CONFIGURATION
# =============================================================================

CONFIG = {
    "abandonment_rate":  0.25,    # 25% of users get abandonment events
    "events_per_user":   (1, 3),  # each abandoner gets 1-3 abandonment events
    "days_back":         60,      # spread events across last 60 days
    "batch_size":        200,
    "rate_limit":        0.1,
}

CT_UPLOAD_URL = "https://api.clevertap.com/1/upload"

CATEGORIES = [
    "Electronics", "Fashion", "Grocery", "Home & Furniture",
    "Appliances", "Beauty", "Sports", "Books"
]

CATEGORY_WEIGHTS = [25, 20, 15, 12, 10, 8, 5, 5]

PRODUCTS = {
    "Electronics": [
        {"name": "Samsung Galaxy M14 5G", "id": "E001", "price": 10999},
        {"name": "Apple iPhone 13",        "id": "E002", "price": 49999},
        {"name": "OnePlus Nord CE3 Lite",  "id": "E003", "price": 17999},
        {"name": "Sony WH-1000XM5",        "id": "E010", "price": 24990},
        {"name": "HP 15s Core i5 Laptop",  "id": "E008", "price": 49990},
    ],
    "Fashion": [
        {"name": "Levi's 511 Slim Fit Jeans", "id": "F001", "price": 2399},
        {"name": "Nike Air Max 270",           "id": "F003", "price": 6995},
        {"name": "Woodland Leather Shoes",     "id": "F010", "price": 3299},
    ],
    "Grocery": [
        {"name": "Aashirvaad Atta 10kg",   "id": "G002", "price": 379},
        {"name": "Nescafe Classic 200g",   "id": "G004", "price": 499},
    ],
    "Home & Furniture": [
        {"name": "Wakefit Memory Foam Mattress", "id": "H001", "price": 12999},
        {"name": "Milton Thermosteel Flask",      "id": "H010", "price": 699},
    ],
    "Appliances": [
        {"name": "Prestige Iris Mixer Grinder",    "id": "A002", "price": 1795},
        {"name": "LG Washing Machine",             "id": "A001", "price": 34990},
    ],
    "Beauty": [
        {"name": "Himalaya Neem Face Wash",  "id": "B001", "price": 349},
        {"name": "Mamaearth Vitamin C Serum","id": "B007", "price": 549},
    ],
    "Sports": [
        {"name": "Burnlab Resistance Bands", "id": "SP005", "price": 799},
        {"name": "Nivia Iron Dumbbell 5kg",  "id": "SP008", "price": 1499},
    ],
    "Books": [
        {"name": "Atomic Habits",            "id": "BK001", "price": 399},
        {"name": "The Psychology of Money",  "id": "BK002", "price": 349},
    ],
}

PAYMENT_METHODS = ["upi", "card", "netbanking", "cod", "emi"]
PAYMENT_WEIGHTS = [45, 25, 15, 10, 5]


# =============================================================================
# HELPERS
# =============================================================================

def get_credentials():
    account_id = os.environ.get("CT_ACCOUNT_ID")
    passcode   = os.environ.get("CT_PASSCODE")
    if not account_id or not passcode:
        print("\n❌  CT credentials not found.")
        print("    export CT_ACCOUNT_ID=your-id")
        print("    export CT_PASSCODE=your-passcode")
        sys.exit(1)
    return account_id, passcode


def read_emails_from_csv(filepath):
    emails        = []
    possible_cols = ['Email', 'email', 'EMAIL', 'email_address', 'Email Address']

    with open(filepath, newline='', encoding='utf-8-sig') as f:
        reader    = csv.DictReader(f)
        headers   = reader.fieldnames or []
        email_col = next((c for c in possible_cols if c in headers), None)

        if not email_col:
            print(f"\n❌  Could not find email column.")
            print(f"    Available columns: {', '.join(headers)}")
            sys.exit(1)

        print(f"  Using column: '{email_col}'")

        for row in reader:
            email = row.get(email_col, '').strip()
            if email and '@' in email:
                emails.append(email)

    return emails


def weighted_choice(options, weights):
    return random.choices(options, weights=weights, k=1)[0]


def random_recent_date(days_back):
    """Random datetime within the last N days."""
    offset = random.randint(1, days_back)
    base   = datetime.now() - timedelta(days=offset)
    return base.replace(
        hour=random.randint(8, 23),
        minute=random.randint(0, 59),
        second=random.randint(0, 59)
    )


def to_epoch(dt):
    return int(dt.timestamp())


def generate_abandonment_events(email):
    """
    Generate 1-3 cart abandonment sessions for a user.
    Each session: Category Viewed → Product Viewed → Added to Cart
                  → Cart Viewed → Checkout Initiated
    Deliberately NO Charged event — that's what makes them abandoners.
    """
    events     = []
    num_events = random.randint(*CONFIG["events_per_user"])

    for _ in range(num_events):
        t        = random_recent_date(CONFIG["days_back"])
        category = weighted_choice(CATEGORIES, CATEGORY_WEIGHTS)
        products = PRODUCTS.get(category, PRODUCTS["Electronics"])
        product  = random.choice(products)
        quantity = random.randint(1, 3)
        cart_val = product["price"] * quantity
        coupon   = random.random() < 0.2

        # Category Viewed
        events.append({
            "type":     "event",
            "identity": email,
            "evtName":  "Category Viewed",
            "evtData":  {
                "category_name":  category,
                "source":         "listing_page",
                "total_products": random.randint(8, 104),
                "vertical":       "bazario"
            },
            "ts": to_epoch(t),
        })

        # Product Viewed
        t2 = t + timedelta(minutes=random.randint(2, 5))
        events.append({
            "type":     "event",
            "identity": email,
            "evtName":  "Product Viewed",
            "evtData":  {
                "product_id":   product["id"],
                "product_name": product["name"],
                "category":     category,
                "price":        product["price"],
                "source":       "listing_page",
                "vertical":     "bazario"
            },
            "ts": to_epoch(t2),
        })

        # Added to Cart
        t3 = t2 + timedelta(minutes=random.randint(2, 4))
        events.append({
            "type":     "event",
            "identity": email,
            "evtName":  "Added to Cart",
            "evtData":  {
                "product_id":   product["id"],
                "product_name": product["name"],
                "category":     category,
                "price":        product["price"],
                "quantity":     quantity,
                "cart_value":   cart_val,
                "source":       "listing_page",
                "vertical":     "bazario"
            },
            "ts": to_epoch(t3),
        })

        # Cart Viewed
        t4 = t3 + timedelta(minutes=random.randint(1, 3))
        events.append({
            "type":     "event",
            "identity": email,
            "evtName":  "Cart Viewed",
            "evtData":  {
                "item_count":        quantity,
                "cart_value":        cart_val,
                "top_category":      category,
                "unique_categories": 1,
                "vertical":          "bazario"
            },
            "ts": to_epoch(t4),
        })

        # Checkout Initiated — the last event, no Charged follows
        t5 = t4 + timedelta(minutes=random.randint(2, 5))
        events.append({
            "type":     "event",
            "identity": email,
            "evtName":  "Checkout Initiated",
            "evtData":  {
                "item_count":     quantity,
                "cart_value":     cart_val,
                "top_category":   category,
                "category_count": 1,
                "coupon_applied": coupon,
                "coupon_code":    "BAZARIO10" if coupon else "",
                "vertical":       "bazario"
            },
            "ts": to_epoch(t5),
        })

    return events


def upload_batch(records, headers):
    try:
        response = requests.post(
            CT_UPLOAD_URL,
            headers=headers,
            json={"d": records},
            timeout=30
        )
        data = response.json()
        return data.get("status") == "success", data.get("processed", 0)
    except Exception as e:
        print(f"\n  ⚠️  Request error: {e}")
        return False, 0


def progress_bar(current, total, prefix="", width=40):
    pct    = current / total if total > 0 else 0
    filled = int(width * pct)
    bar    = "█" * filled + "░" * (width - filled)
    print(f"\r{prefix} [{bar}] {current:,}/{total:,} ({pct*100:.1f}%)",
          end="", flush=True)


# =============================================================================
# MAIN
# =============================================================================

def main():
    parser = argparse.ArgumentParser(description="Create cart abandonment events")
    parser.add_argument(
        "--csv",
        default="profiles.csv",
        help="Path to CT exported profiles CSV (default: profiles.csv)"
    )
    args = parser.parse_args()

    print("\n" + "="*60)
    print("  CREATE CART ABANDONERS — CT PLAYGROUND")
    print("="*60)

    if not os.path.exists(args.csv):
        print(f"\n❌  CSV file not found: {args.csv}")
        sys.exit(1)

    account_id, passcode = get_credentials()
    headers = {
        "X-CleverTap-Account-Id": account_id,
        "X-CleverTap-Passcode":   passcode,
        "Content-Type":           "application/json",
    }

    # ── Read emails ──
    print(f"\n  Step 1: Reading emails from {args.csv}...")
    all_emails = read_emails_from_csv(args.csv)
    print(f"  ✓ {len(all_emails):,} total profiles found")

    # ── Select abandoners ──
    random.seed(999)   # fixed seed for reproducibility
    num_abandoners = int(len(all_emails) * CONFIG["abandonment_rate"])
    abandoners     = random.sample(all_emails, num_abandoners)

    print(f"\n  Abandonment rate : {CONFIG['abandonment_rate']*100:.0f}%")
    print(f"  Users selected   : {num_abandoners:,}")
    print(f"  Events per user  : {CONFIG['events_per_user'][0]}-{CONFIG['events_per_user'][1]}")
    print(f"  Time window      : Last {CONFIG['days_back']} days")
    print(f"  Est. total events: ~{num_abandoners * 2:,}")

    # ── Generate and upload events ──
    print(f"\n  Step 2: Generating and uploading abandonment events...")

    batch      = []
    processed  = 0
    errors     = 0

    for count, email in enumerate(abandoners):
        events = generate_abandonment_events(email)
        batch.extend(events)

        # Upload when batch is full
        while len(batch) >= CONFIG["batch_size"]:
            to_upload = batch[:CONFIG["batch_size"]]
            batch     = batch[CONFIG["batch_size"]:]
            success, count_done = upload_batch(to_upload, headers)
            if success:
                processed += count_done
            else:
                errors    += len(to_upload)
            time.sleep(CONFIG["rate_limit"])

        if (count + 1) % 100 == 0:
            progress_bar(count + 1, num_abandoners, "  Processing")

    # Upload remaining
    if batch:
        success, count_done = upload_batch(batch, headers)
        if success:
            processed += count_done
        else:
            errors    += len(batch)

    progress_bar(num_abandoners, num_abandoners, "  Processing")

    print(f"\n\n{'='*60}")
    print("  COMPLETE")
    print(f"{'='*60}")
    print(f"  Users targeted   : {num_abandoners:,}")
    print(f"  Events uploaded  : {processed:,}")
    print(f"  Errors           : {errors}")
    print(f"{'='*60}")

    print(f"\n  ✓ Done! Now build this segment in CT:")
    print(f"    Checkout Initiated exists in last {CONFIG['days_back']} days")
    print(f"    AND Charged does not exist in last {CONFIG['days_back']} days")
    print(f"\n  Expected segment size: ~{num_abandoners:,} users\n")


if __name__ == "__main__":
    main()
