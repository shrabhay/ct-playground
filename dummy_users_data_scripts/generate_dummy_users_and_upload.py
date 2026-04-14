#!/usr/bin/env python3
"""
=============================================================================
generate_and_upload.py — CT Playground Dummy Data Script
=============================================================================

Generates 15,000 realistic Indian user profiles with 5 years of event
history and uploads them to CleverTap via the Upload API.

USAGE:
  Set environment variables first:
    Mac/Linux:  export CT_ACCOUNT_ID=your-id && export CT_PASSCODE=your-passcode
    Windows:    set CT_ACCOUNT_ID=your-id && set CT_PASSCODE=your-passcode

  Then run:
    python3 generate_and_upload.py

  To resume after interruption:
    python3 generate_and_upload.py --resume

CONFIGURATION:
  Edit the CONFIG section below to adjust user counts, date range, etc.
=============================================================================
"""

import os
import sys
import json
import time
import random
import argparse
import requests
from datetime import datetime, timedelta
from pathlib import Path

# =============================================================================
# CONFIGURATION
# =============================================================================

CONFIG = {
    # User counts
    "total_users":      15000,
    "high_value_pct":   0.15,   # 2,250 users
    "regular_pct":      0.40,   # 6,000 users
    "occasional_pct":   0.30,   # 4,500 users
    "dormant_pct":      0.15,   # 2,250 users

    # Date range
    "history_start":    datetime(2020, 1, 1),
    "history_end":      datetime(2025, 12, 31),

    # CT API
    "batch_size":       200,     # CT Upload API max per request
    "rate_limit":       0.05,     # seconds between requests (2 req/sec)
    "max_retries":      3,       # retry failed batches this many times
    "retry_delay":      5,       # seconds between retries

    # Checkpoint file for resume capability
    "checkpoint_file":  "ct_upload_checkpoint.json",
    "output_dir":       "ct_dummy_data",
}

# CT API endpoints
CT_BASE_URL   = "https://api.clevertap.com"
CT_UPLOAD_URL = f"{CT_BASE_URL}/1/upload"

# =============================================================================
# REALISTIC INDIAN DATA
# =============================================================================

FIRST_NAMES_M = [
    "Aarav", "Arjun", "Vikram", "Rahul", "Rohit", "Karan", "Amit", "Sanjay",
    "Deepak", "Rajesh", "Suresh", "Mahesh", "Ramesh", "Ganesh", "Dinesh",
    "Nikhil", "Aakash", "Vivek", "Manish", "Pradeep", "Ajay", "Vijay",
    "Sanjay", "Ravi", "Sunil", "Anil", "Kapil", "Sachin", "Mohit", "Sumit",
    "Ankit", "Ravi", "Kunal", "Vishal", "Shubham", "Akash", "Harsh", "Varun",
    "Gaurav", "Pranav", "Abhishek", "Tushar", "Tarun", "Yash", "Siddharth",
    "Kartik", "Alok", "Nitin", "Pavan", "Rohan", "Ishan", "Dev", "Veer",
    "Sameer", "Kabir", "Arnav", "Aditya", "Shivam", "Parth", "Mihir"
]

FIRST_NAMES_F = [
    "Priya", "Pooja", "Neha", "Isha", "Ananya", "Riya", "Shreya", "Kavya",
    "Divya", "Meera", "Sunita", "Rekha", "Geeta", "Seema", "Reema",
    "Anjali", "Swati", "Shweta", "Nisha", "Usha", "Suman", "Kiran",
    "Pallavi", "Aarti", "Bharti", "Shalini", "Preeti", "Sapna", "Vandana",
    "Monika", "Ritu", "Sneha", "Nidhi", "Shipra", "Komal", "Simran",
    "Sakshi", "Mansi", "Tanvi", "Aditi", "Deepa", "Lata", "Mala",
    "Radha", "Sita", "Gita", "Yamini", "Bhavna", "Charu", "Disha",
    "Esha", "Falguni", "Garima", "Hema", "Jyoti", "Kriti", "Lavanya"
]

LAST_NAMES = [
    "Sharma", "Verma", "Gupta", "Singh", "Kumar", "Patel", "Shah", "Joshi",
    "Mehta", "Agarwal", "Mishra", "Tiwari", "Pandey", "Yadav", "Chauhan",
    "Rajput", "Nair", "Menon", "Pillai", "Reddy", "Rao", "Naidu", "Iyer",
    "Krishnan", "Subramanian", "Chatterjee", "Banerjee", "Das", "Bose",
    "Mukherjee", "Ghosh", "Sen", "Roy", "Dey", "Biswas", "Saha", "Ganguly",
    "Kulkarni", "Desai", "Patil", "Jain", "Agarwal", "Rastogi", "Saxena",
    "Srivastava", "Tripathi", "Dubey", "Shukla", "Bajpai", "Awasthi"
]

CITIES = [
    "Mumbai", "Delhi", "Bengaluru", "Hyderabad", "Chennai", "Kolkata",
    "Pune", "Ahmedabad", "Jaipur", "Surat", "Lucknow", "Kanpur",
    "Nagpur", "Indore", "Thane", "Bhopal", "Visakhapatnam", "Patna",
    "Vadodara", "Ghaziabad", "Ludhiana", "Agra", "Nashik", "Faridabad",
    "Meerut", "Rajkot", "Varanasi", "Srinagar", "Aurangabad", "Amritsar",
    "Gurugram", "Noida", "Coimbatore", "Kochi", "Chandigarh", "Mysuru"
]

CITY_WEIGHTS = [
    15, 14, 10, 8, 7, 6, 5, 4, 3, 3, 2, 2,
    2, 2, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1,
    1, 1, 1, 1, 1, 1, 2, 2, 1, 1, 1, 1
]

CATEGORIES = [
    "Electronics", "Fashion", "Grocery", "Home & Furniture",
    "Appliances", "Beauty", "Sports", "Books"
]

CATEGORY_WEIGHTS = [25, 20, 15, 12, 10, 8, 5, 5]

PRODUCTS = {
    "Electronics": [
        {"name": "Samsung Galaxy M14 5G", "id": "E001", "price": 10999, "brand": "Samsung"},
        {"name": "Apple iPhone 13", "id": "E002", "price": 49999, "brand": "Apple"},
        {"name": "OnePlus Nord CE3 Lite", "id": "E003", "price": 17999, "brand": "OnePlus"},
        {"name": "boAt Airdopes 141 TWS", "id": "E004", "price": 1299, "brand": "boAt"},
        {"name": "Noise ColorFit Pro 4", "id": "E005", "price": 2499, "brand": "Noise"},
        {"name": "Sony WH-1000XM5", "id": "E010", "price": 24990, "brand": "Sony"},
        {"name": "HP 15s Core i5 Laptop", "id": "E008", "price": 49990, "brand": "HP"},
        {"name": "Samsung 32 Smart Monitor", "id": "E014", "price": 17990, "brand": "Samsung"},
        {"name": "JBL Flip 6 Speaker", "id": "E020", "price": 8999, "brand": "JBL"},
    ],
    "Fashion": [
        {"name": "Levi's 511 Slim Fit Jeans", "id": "F001", "price": 2399, "brand": "Levi's"},
        {"name": "Nike Air Max 270", "id": "F003", "price": 6995, "brand": "Nike"},
        {"name": "Allen Solly Formal Shirt", "id": "F004", "price": 949, "brand": "Allen Solly"},
        {"name": "Puma Men Hoodie", "id": "F006", "price": 1599, "brand": "Puma"},
        {"name": "Woodland Leather Shoes", "id": "F010", "price": 3299, "brand": "Woodland"},
        {"name": "Adidas Track Pants", "id": "F007", "price": 2099, "brand": "Adidas"},
        {"name": "Crocs Classic Clog", "id": "F019", "price": 2799, "brand": "Crocs"},
    ],
    "Grocery": [
        {"name": "Tata Salt 1kg Pack of 6", "id": "G001", "price": 162, "brand": "Tata"},
        {"name": "Aashirvaad Atta 10kg", "id": "G002", "price": 379, "brand": "Aashirvaad"},
        {"name": "Nescafe Classic 200g", "id": "G004", "price": 499, "brand": "Nescafe"},
        {"name": "Maggi Noodles Pack of 12", "id": "G007", "price": 168, "brand": "Maggi"},
        {"name": "Tata Tea Premium 500g", "id": "G006", "price": 249, "brand": "Tata Tea"},
        {"name": "Dabur Honey 1kg", "id": "G008", "price": 349, "brand": "Dabur"},
        {"name": "Britannia Good Day 600g", "id": "G011", "price": 149, "brand": "Britannia"},
    ],
    "Home & Furniture": [
        {"name": "Wakefit Memory Foam Mattress", "id": "H001", "price": 12999, "brand": "Wakefit"},
        {"name": "Pigeon Non-Stick Tawa 28cm", "id": "H005", "price": 499, "brand": "Pigeon"},
        {"name": "Milton Thermosteel Flask", "id": "H010", "price": 699, "brand": "Milton"},
        {"name": "Cello Airtight Container Set", "id": "H007", "price": 999, "brand": "Cello"},
        {"name": "Philips LED Bulb Pack of 6", "id": "H008", "price": 449, "brand": "Philips"},
        {"name": "Borosil Vision Glass Set", "id": "H012", "price": 549, "brand": "Borosil"},
    ],
    "Appliances": [
        {"name": "Prestige Iris Mixer Grinder", "id": "A002", "price": 1795, "brand": "Prestige"},
        {"name": "Atomberg BLDC Ceiling Fan", "id": "A004", "price": 2849, "brand": "Atomberg"},
        {"name": "Bajaj Majesty Rice Cooker", "id": "A005", "price": 1599, "brand": "Bajaj"},
        {"name": "Havells Instant Water Geyser", "id": "A009", "price": 3499, "brand": "Havells"},
        {"name": "LG Fully Automatic Washing Machine", "id": "A001", "price": 34990, "brand": "LG"},
    ],
    "Beauty": [
        {"name": "Himalaya Neem Face Wash", "id": "B001", "price": 349, "brand": "Himalaya"},
        {"name": "Maybelline Fit Me Foundation", "id": "B003", "price": 449, "brand": "Maybelline"},
        {"name": "Dove Body Lotion 400ml", "id": "B006", "price": 299, "brand": "Dove"},
        {"name": "Mamaearth Vitamin C Serum", "id": "B007", "price": 549, "brand": "Mamaearth"},
        {"name": "WOW Apple Cider Shampoo", "id": "B009", "price": 349, "brand": "WOW"},
    ],
    "Sports": [
        {"name": "Burnlab Resistance Bands Set", "id": "SP005", "price": 799, "brand": "Burnlab"},
        {"name": "Nivia Iron Dumbbell 5kg Pair", "id": "SP008", "price": 1499, "brand": "Nivia"},
        {"name": "Yonex Voltric Badminton Racquet", "id": "SP002", "price": 2899, "brand": "Yonex"},
        {"name": "Decathlon Running T-Shirt", "id": "SP007", "price": 499, "brand": "Decathlon"},
        {"name": "Reebok Lite 3.0 Running Shoes", "id": "SP010", "price": 2999, "brand": "Reebok"},
    ],
    "Books": [
        {"name": "Atomic Habits", "id": "BK001", "price": 399, "brand": "Penguin"},
        {"name": "The Psychology of Money", "id": "BK002", "price": 349, "brand": "Jaico"},
        {"name": "Sapiens", "id": "BK006", "price": 499, "brand": "Vintage"},
        {"name": "Deep Work", "id": "BK007", "price": 399, "brand": "Piatkus"},
        {"name": "Zero to One", "id": "BK005", "price": 349, "brand": "Currency"},
    ],
}

PAYMENT_METHODS = ["upi", "card", "netbanking", "cod", "emi"]
PAYMENT_WEIGHTS = [45, 25, 15, 10, 5]

SEARCH_QUERIES = [
    "samsung", "iphone", "laptop", "earbuds", "smartwatch",
    "jeans", "shoes", "kurta", "jacket", "t-shirt",
    "atta", "coffee", "tea", "noodles", "biscuits",
    "mattress", "mixer", "fan", "bulb", "pressure cooker",
    "face wash", "shampoo", "moisturiser", "sunscreen",
    "cricket bat", "dumbbells", "yoga mat", "protein",
    "novel", "self help", "coding book"
]

# Seasonal multipliers — (month, day_range, multiplier, name)
SEASONAL_SPIKES = [
    (10, (15, 31), 3.0, "Diwali"),
    (11, (1,  15), 2.5, "Post-Diwali"),
    (1,  (1,  7),  2.0, "New Year"),
    (6,  (15, 30), 2.0, "Summer Sale"),
    (7,  (1,  15), 1.8, "Mid-Year Sale"),
    (8,  (10, 20), 1.5, "Independence Day"),
    (1,  (24, 27), 1.5, "Republic Day"),
    (2,  (10, 16), 1.3, "Valentine's"),
    (12, (20, 31), 1.8, "Year End"),
    (9,  (1,  10), 1.4, "Onam"),
]

# =============================================================================
# HELPER FUNCTIONS
# =============================================================================

def get_credentials():
    """Read CT credentials from environment variables."""
    account_id = os.environ.get("CT_ACCOUNT_ID")
    passcode   = os.environ.get("CT_PASSCODE")
    if not account_id or not passcode:
        print("\n❌  ERROR: CT credentials not found.")
        print("    Please set environment variables before running:")
        print("    Mac/Linux:")
        print("      export CT_ACCOUNT_ID=your-account-id")
        print("      export CT_PASSCODE=your-passcode")
        print("    Windows:")
        print("      set CT_ACCOUNT_ID=your-account-id")
        print("      set CT_PASSCODE=your-passcode")
        sys.exit(1)
    return account_id, passcode


def progress_bar(current, total, prefix="", width=40):
    """Simple terminal progress bar."""
    pct      = current / total
    filled   = int(width * pct)
    bar      = "█" * filled + "░" * (width - filled)
    print(f"\r{prefix} [{bar}] {current:,}/{total:,} ({pct*100:.1f}%)", end="", flush=True)


def random_date(start, end):
    """Random datetime between start and end."""
    delta = end - start
    return start + timedelta(seconds=random.randint(0, int(delta.total_seconds())))


def to_epoch(dt):
    """Convert datetime to Unix timestamp."""
    return int(dt.timestamp())


def get_seasonal_multiplier(dt):
    """Get purchase probability multiplier for a given date."""
    for month, day_range, multiplier, _ in SEASONAL_SPIKES:
        if dt.month == month and day_range[0] <= dt.day <= day_range[1]:
            return multiplier
    return 1.0


def generate_phone():
    """Generate a realistic Indian mobile number."""
    # Indian mobile numbers start with 6, 7, 8, or 9
    prefix = random.choice([6, 7, 8, 9])
    return f"+91{prefix}{random.randint(100000000, 999999999)}"


def generate_email(name, index):
    """Generate a unique email from name and index."""
    clean = name.lower().replace(" ", ".")
    domains = ["gmail.com", "yahoo.com", "outlook.com", "hotmail.com",
               "rediffmail.com", "ymail.com", "protonmail.com"]
    domain  = random.choice(domains)
    suffix  = random.randint(10, 9999)
    return f"{clean}{suffix}@{domain}"


def weighted_choice(options, weights):
    """Choose from options using weights."""
    return random.choices(options, weights=weights, k=1)[0]


def load_checkpoint():
    """Load upload progress from checkpoint file."""
    path = Path(CONFIG["checkpoint_file"])
    if path.exists():
        with open(path) as f:
            return json.load(f)
    return {"profiles_uploaded": 0, "events_uploaded": 0, "errors": []}


def save_checkpoint(data):
    """Save upload progress to checkpoint file."""
    with open(CONFIG["checkpoint_file"], "w") as f:
        json.dump(data, f)


# =============================================================================
# USER GENERATION
# =============================================================================

def generate_user(index, archetype):
    """Generate a single user profile based on archetype."""
    gender = random.choice(["M", "F"])
    first  = random.choice(FIRST_NAMES_M if gender == "M" else FIRST_NAMES_F)
    last   = random.choice(LAST_NAMES)
    name   = f"{first} {last}"
    email  = generate_email(name, index)
    phone  = generate_phone()
    city   = weighted_choice(CITIES, CITY_WEIGHTS)

    # DOB — age range based on archetype
    age_ranges = {
        "high_value":  (28, 45),
        "regular":     (22, 40),
        "occasional":  (18, 55),
        "dormant":     (25, 50),
    }
    age_min, age_max = age_ranges[archetype]
    age    = random.randint(age_min, age_max)
    dob    = datetime.now() - timedelta(days=age * 365 + random.randint(0, 364))

    # Signup date — spread across 2020-2025 based on archetype
    signup_ranges = {
        "high_value":  (datetime(2020, 1, 1), datetime(2023, 12, 31)),
        "regular":     (datetime(2020, 6, 1), datetime(2024, 6, 30)),
        "occasional":  (datetime(2021, 1, 1), datetime(2025, 6, 30)),
        "dormant":     (datetime(2020, 1, 1), datetime(2023, 6, 30)),
    }
    signup_start, signup_end = signup_ranges[archetype]
    joined_at = random_date(signup_start, signup_end)

    # Category preference
    pref_category = weighted_choice(CATEGORIES, CATEGORY_WEIGHTS)

    # Is Prime — more likely for high value users
    prime_prob = {"high_value": 0.75, "regular": 0.35, "occasional": 0.10, "dormant": 0.05}
    is_prime   = random.random() < prime_prob[archetype]

    # Order stats — based on archetype and time since signup
    months_active = (datetime.now() - joined_at).days / 30
    order_rates   = {
        "high_value":  (3, 6),    # orders per month
        "regular":     (1, 2),
        "occasional":  (0.25, 0.5),
        "dormant":     (0.5, 1),   # historical, not recent
    }
    rate_min, rate_max = order_rates[archetype]
    avg_rate   = random.uniform(rate_min, rate_max)
    total_orders = max(0, int(months_active * avg_rate * random.uniform(0.5, 1.5)))

    # Average order value by archetype
    aov_ranges = {
        "high_value":  (3000, 25000),
        "regular":     (800, 5000),
        "occasional":  (300, 3000),
        "dormant":     (500, 4000),
    }
    aov = random.randint(*aov_ranges[archetype])
    lifetime_value = total_orders * aov

    # Last order date
    if total_orders > 0:
        if archetype == "dormant":
            last_order_date = random_date(
                datetime.now() - timedelta(days=540),
                datetime.now() - timedelta(days=180)
            )
        else:
            last_order_date = random_date(
                datetime.now() - timedelta(days=60),
                datetime.now() - timedelta(days=1)
            )
        last_order_value = random.randint(*aov_ranges[archetype])
    else:
        last_order_date  = None
        last_order_value = 0

    return {
        "email":           email,
        "name":            name,
        "phone":           phone,
        "gender":          gender,
        "dob":             dob,
        "city":            city,
        "joined_at":       joined_at,
        "is_prime":        is_prime,
        "pref_category":   pref_category,
        "total_orders":    total_orders,
        "lifetime_value":  lifetime_value,
        "last_order_date": last_order_date,
        "last_order_value":last_order_value,
        "archetype":       archetype,
        "aov":             aov,
    }


def user_to_ct_profile(user):
    """Convert user dict to CT Upload API profile format."""
    profile_data = {
        "Name":             user["name"],
        "Email":            user["email"],
        "Phone":            user["phone"],
        "Gender":           user["gender"],
        "DOB":              to_epoch(user["dob"]),
        "City":             user["city"],
        "Is Prime":         user["is_prime"],
        "Preferred Category": user["pref_category"],
        "Total Orders":     user["total_orders"],
        "Lifetime Value":   user["lifetime_value"],
        "Archetype":        user["archetype"].replace("_", " ").title(),
        "MSG-email":        True,
        "MSG-sms":          True,
        "MSG-push":         True,
        "Joined At": f"$D_{to_epoch(user['joined_at'])}",
    }

    if user["last_order_date"]:
        profile_data["Last Order Date"]  = f"$D_{to_epoch(user['last_order_date'])}"
        profile_data["Last Order Value"] = user["last_order_value"]

    return {
        "type":        "profile",
        "identity":    user["email"],
        "profileData": profile_data,
        "ts":          to_epoch(user["joined_at"]),
    }


# =============================================================================
# EVENT GENERATION
# =============================================================================

def generate_compact_events(user):
    """
    Generate a compact but meaningful event set per user.
    Focuses on key events for segments, funnels and revenue.
    Targets ~15-25 events per user — enough for demo, fast to upload.
    """
    events    = []
    archetype = user["archetype"]
    joined_at = user["joined_at"]
    now       = datetime.now()

    if archetype == "dormant":
        activity_end = now - timedelta(days=random.randint(180, 540))
    else:
        activity_end = now

    # Number of purchase events based on archetype
    purchase_counts = {
        "high_value":  random.randint(8, 20),
        "regular":     random.randint(3, 8),
        "occasional":  random.randint(1, 3),
        "dormant":     random.randint(1, 4),
    }
    num_purchases = purchase_counts[archetype]

    # Spread purchases across the user's active period
    if (activity_end - joined_at).days < 1:
        return events

    for _ in range(num_purchases):
        # Random purchase date
        t = random_date(joined_at, activity_end)
        category = weighted_choice(CATEGORIES, CATEGORY_WEIGHTS)
        products  = PRODUCTS.get(category, PRODUCTS["Electronics"])
        product   = random.choice(products)
        quantity  = random.randint(1, 3)
        order_val = product["price"] * quantity
        payment   = weighted_choice(PAYMENT_METHODS, PAYMENT_WEIGHTS)
        order_id  = f"BZ-{to_epoch(t)}-{random.randint(1000,9999)}"

        # Login
        events.append({
            "type": "event", "identity": user["email"],
            "evtName": "User Logged In",
            "evtData": {"method": "password", "is_prime": user["is_prime"]},
            "ts": to_epoch(t),
        })

        # Category Viewed
        t2 = t + timedelta(minutes=2)
        events.append({
            "type": "event", "identity": user["email"],
            "evtName": "Category Viewed",
            "evtData": {
                "category_name":  category,
                "source":         "listing_page",
                "total_products": random.randint(8, 104),
            },
            "ts": to_epoch(t2),
        })

        # Product Viewed
        t3 = t2 + timedelta(minutes=3)
        events.append({
            "type": "event", "identity": user["email"],
            "evtName": "Product Viewed",
            "evtData": {
                "product_id":   product["id"],
                "product_name": product["name"],
                "category":     category,
                "brand":        product["brand"],
                "price":        product["price"],
                "source":       "listing_page",
            },
            "ts": to_epoch(t3),
        })

        # Added to Cart
        t4 = t3 + timedelta(minutes=2)
        events.append({
            "type": "event", "identity": user["email"],
            "evtName": "Added to Cart",
            "evtData": {
                "product_id":   product["id"],
                "product_name": product["name"],
                "category":     category,
                "brand":        product["brand"],
                "price":        product["price"],
                "quantity":     quantity,
                "cart_value":   order_val,
                "source":       "listing_page",
            },
            "ts": to_epoch(t4),
        })

        # Checkout Initiated
        t5 = t4 + timedelta(minutes=3)
        events.append({
            "type": "event", "identity": user["email"],
            "evtName": "Checkout Initiated",
            "evtData": {
                "item_count":     quantity,
                "cart_value":     order_val,
                "top_category":   category,
                "category_count": 1,
                "coupon_applied": random.random() < 0.2,
                "coupon_code":    "BAZARIO10" if random.random() < 0.2 else "",
            },
            "ts": to_epoch(t5),
        })

        # Charged
        t6 = t5 + timedelta(minutes=4)
        events.append({
            "type": "event", "identity": user["email"],
            "evtName": "Charged",
            "evtData": {
                "Amount":       order_val,
                "Payment mode": payment,
                "Order ID":     order_id,
                "Items": [{
                    "Product name": product["name"],
                    "Category":     category,
                    "Brand":        product["brand"],
                    "Price":        product["price"],
                    "Quantity":     quantity,
                }],
            },
            "ts": to_epoch(t6),
        })

        # Order Confirmed
        t7 = t6 + timedelta(minutes=1)
        events.append({
            "type": "event", "identity": user["email"],
            "evtName": "Order Confirmed",
            "evtData": {
                "order_id":       order_id,
                "order_value":    order_val,
                "item_count":     quantity,
                "payment_method": payment,
                "top_category":   category,
            },
            "ts": to_epoch(t7),
        })

    # Add a few browsing-only sessions (no purchase) for realism
    browse_counts = {
        "high_value":  random.randint(3, 6),
        "regular":     random.randint(2, 4),
        "occasional":  random.randint(1, 2),
        "dormant":     random.randint(0, 1),
    }
    for _ in range(browse_counts[archetype]):
        t = random_date(joined_at, activity_end)
        category = weighted_choice(CATEGORIES, CATEGORY_WEIGHTS)
        products  = PRODUCTS.get(category, PRODUCTS["Electronics"])
        product   = random.choice(products)

        events.append({
            "type": "event", "identity": user["email"],
            "evtName": "Category Viewed",
            "evtData": {"category_name": category, "source": "listing_page", "total_products": random.randint(8, 104)},
            "ts": to_epoch(t),
        })
        events.append({
            "type": "event", "identity": user["email"],
            "evtName": "Product Viewed",
            "evtData": {
                "product_id": product["id"], "product_name": product["name"],
                "category": category, "brand": product["brand"],
                "price": product["price"], "source": "listing_page",
            },
            "ts": to_epoch(t + timedelta(minutes=3)),
        })
        # Wishlist for some
        if random.random() < 0.3:
            events.append({
                "type": "event", "identity": user["email"],
                "evtName": "Added to Wishlist",
                "evtData": {
                    "product_id": product["id"], "product_name": product["name"],
                    "category": category, "brand": product["brand"],
                    "price": product["price"], "source": "listing_page",
                },
                "ts": to_epoch(t + timedelta(minutes=5)),
            })

    return events

def generate_events_for_user(user):
    """Generate realistic event history for a user across 5 years."""
    events     = []
    archetype  = user["archetype"]
    joined_at  = user["joined_at"]
    now        = datetime.now()

    # Session frequency per month by archetype
    session_rates = {
        "high_value":  (4, 8),    # sessions per month
        "regular":     (2, 4),
        "occasional":  (1, 2),
        "dormant":     (1, 2),
    }

    # For dormant users, limit activity to before their dormancy started
    if archetype == "dormant":
        activity_end = now - timedelta(days=random.randint(180, 540))
    else:
        activity_end = now

    # Generate session dates from joined_at to activity_end
    current = joined_at
    order_count = 0

    while current < activity_end:
        # Sessions this month
        rate_min, rate_max = session_rates[archetype]
        sessions_this_month = random.randint(rate_min, rate_max)

        for _ in range(sessions_this_month):
            if current >= activity_end:
                break

            # Session start time — random time during the month
            session_time = current + timedelta(
                days=random.uniform(0, 30),
                hours=random.uniform(8, 23),
                minutes=random.uniform(0, 59)
            )

            if session_time >= activity_end:
                break

            # ── Login event ──
            events.append({
                "type":     "event",
                "identity": user["email"],
                "evtName":  "User Logged In",
                "evtData":  {"method": "password", "is_prime": user["is_prime"]},
                "ts":       to_epoch(session_time),
            })

            # ── Browsing events ──
            category = weighted_choice(CATEGORIES, CATEGORY_WEIGHTS)
            t = session_time + timedelta(minutes=random.randint(1, 3))

            events.append({
                "type":     "event",
                "identity": user["email"],
                "evtName":  "Category Viewed",
                "evtData":  {
                    "category_name":  category,
                    "source":         "listing_page",
                    "total_products": random.randint(8, 104),
                },
                "ts": to_epoch(t),
            })

            # ── Product views ──
            products_in_cat = PRODUCTS.get(category, PRODUCTS["Electronics"])
            num_views = random.randint(1, min(4, len(products_in_cat)))
            viewed_products = random.sample(products_in_cat, num_views)

            for product in viewed_products:
                t += timedelta(minutes=random.randint(2, 8))
                events.append({
                    "type":     "event",
                    "identity": user["email"],
                    "evtName":  "Product Viewed",
                    "evtData":  {
                        "product_id":    product["id"],
                        "product_name":  product["name"],
                        "category":      category,
                        "brand":         product["brand"],
                        "price":         product["price"],
                        "source":        "listing_page",
                    },
                    "ts": to_epoch(t),
                })

            # ── Search ──
            if random.random() < 0.3:
                t += timedelta(minutes=random.randint(1, 3))
                events.append({
                    "type":     "event",
                    "identity": user["email"],
                    "evtName":  "Product Searched",
                    "evtData":  {
                        "query":         random.choice(SEARCH_QUERIES),
                        "results_count": random.randint(1, 20),
                        "source":        "listing_search_bar",
                    },
                    "ts": to_epoch(t),
                })

            # ── Add to wishlist ──
            if random.random() < 0.25:
                product = random.choice(viewed_products)
                t += timedelta(minutes=random.randint(1, 3))
                events.append({
                    "type":     "event",
                    "identity": user["email"],
                    "evtName":  "Added to Wishlist",
                    "evtData":  {
                        "product_id":   product["id"],
                        "product_name": product["name"],
                        "category":     category,
                        "brand":        product["brand"],
                        "price":        product["price"],
                        "source":       "listing_page",
                    },
                    "ts": to_epoch(t),
                })

            # ── Purchase flow — probability based on archetype + seasonality ──
            seasonal_mult = get_seasonal_multiplier(session_time)
            purchase_probs = {
                "high_value":  0.35 * seasonal_mult,
                "regular":     0.15 * seasonal_mult,
                "occasional":  0.05 * seasonal_mult,
                "dormant":     0.08 * seasonal_mult,
            }
            purchase_prob = min(purchase_probs[archetype], 0.95)

            if random.random() < purchase_prob and viewed_products:
                product      = random.choice(viewed_products)
                cart_value   = product["price"] * random.randint(1, 3)
                payment_mode = weighted_choice(PAYMENT_METHODS, PAYMENT_WEIGHTS)
                quantity     = random.randint(1, 3)
                order_value  = product["price"] * quantity

                # Add to cart
                t += timedelta(minutes=random.randint(2, 5))
                events.append({
                    "type":     "event",
                    "identity": user["email"],
                    "evtName":  "Added to Cart",
                    "evtData":  {
                        "product_id":   product["id"],
                        "product_name": product["name"],
                        "category":     category,
                        "brand":        product["brand"],
                        "price":        product["price"],
                        "quantity":     quantity,
                        "cart_value":   cart_value,
                        "source":       "listing_page",
                    },
                    "ts": to_epoch(t),
                })

                # Cart viewed
                t += timedelta(minutes=random.randint(1, 3))
                events.append({
                    "type":     "event",
                    "identity": user["email"],
                    "evtName":  "Cart Viewed",
                    "evtData":  {
                        "item_count":        quantity,
                        "cart_value":        cart_value,
                        "top_category":      category,
                        "unique_categories": 1,
                    },
                    "ts": to_epoch(t),
                })

                # Checkout initiated
                t += timedelta(minutes=random.randint(2, 5))
                events.append({
                    "type":     "event",
                    "identity": user["email"],
                    "evtName":  "Checkout Initiated",
                    "evtData":  {
                        "item_count":     quantity,
                        "cart_value":     cart_value,
                        "top_category":   category,
                        "category_count": 1,
                        "coupon_applied": random.random() < 0.2,
                        "coupon_code":    random.choice(["BAZARIO10", ""]) if random.random() < 0.2 else "",
                    },
                    "ts": to_epoch(t),
                })

                # Charged — CT reserved event
                t += timedelta(minutes=random.randint(3, 8))
                order_id = f"BZ-{to_epoch(t)}-{random.randint(1000, 9999)}"
                events.append({
                    "type":     "event",
                    "identity": user["email"],
                    "evtName":  "Charged",
                    "evtData":  {
                        "Amount":       order_value,
                        "Payment mode": payment_mode,
                        "Order ID":     order_id,
                        "Items": [{
                            "Product name": product["name"],
                            "Category":     category,
                            "Brand":        product["brand"],
                            "Price":        product["price"],
                            "Quantity":     quantity,
                        }],
                    },
                    "ts": to_epoch(t),
                })

                # Order confirmed
                t += timedelta(minutes=random.randint(1, 2))
                events.append({
                    "type":     "event",
                    "identity": user["email"],
                    "evtName":  "Order Confirmed",
                    "evtData":  {
                        "order_id":       order_id,
                        "order_value":    order_value,
                        "item_count":     quantity,
                        "payment_method": payment_mode,
                        "top_category":   category,
                    },
                    "ts": to_epoch(t),
                })

                order_count += 1

            # ── Logout ──
            t += timedelta(minutes=random.randint(5, 30))
            events.append({
                "type":     "event",
                "identity": user["email"],
                "evtName":  "User Logged Out",
                "evtData":  {},
                "ts":       to_epoch(t),
            })

        current += timedelta(days=30)

    return events


# =============================================================================
# CT UPLOAD
# =============================================================================

def upload_batch(records, headers, batch_type=""):
    """Upload a single batch to CT Upload API with retry logic."""
    payload = {"d": records}

    for attempt in range(1, CONFIG["max_retries"] + 1):
        try:
            response = requests.post(
                CT_UPLOAD_URL,
                headers=headers,
                json=payload,
                timeout=30
            )
            data = response.json()

            if data.get("status") == "success":
                processed   = data.get("processed", len(records))
                unprocessed = data.get("unprocessed", [])
                return processed, unprocessed
            else:
                # CT returned an error
                if attempt < CONFIG["max_retries"]:
                    time.sleep(CONFIG["retry_delay"])
                else:
                    return 0, records

        except requests.exceptions.RequestException as e:
            if attempt < CONFIG["max_retries"]:
                time.sleep(CONFIG["retry_delay"])
            else:
                return 0, records

    return 0, records


# =============================================================================
# MAIN
# =============================================================================

def main(resume=False):
    print("\n" + "="*60)
    print("  CT PLAYGROUND — DUMMY DATA GENERATOR")
    print("="*60)

    # Get credentials
    account_id, passcode = get_credentials()
    headers = {
        "X-CleverTap-Account-Id": account_id,
        "X-CleverTap-Passcode":   passcode,
        "Content-Type":           "application/json",
    }

    print(f"\n✓ Credentials loaded (Account: {account_id})")
    print(f"✓ Generating {CONFIG['total_users']:,} users")
    print(f"✓ Event history: 2020 - 2025 (5 years)")
    print(f"✓ Batch size: {CONFIG['batch_size']} records")

    # Load checkpoint if resuming
    checkpoint = load_checkpoint() if resume else {
        "profiles_uploaded": 0,
        "events_uploaded":   0,
        "errors":            [],
        "start_time":        datetime.now().isoformat(),
    }

    if resume and checkpoint.get("profiles_uploaded", 0) > 0:
        print(f"\n↩  Resuming from checkpoint:")
        print(f"   Profiles uploaded: {checkpoint['profiles_uploaded']:,}")
        print(f"   Events uploaded:   {checkpoint['events_uploaded']:,}")

    # ── STEP 1: GENERATE USERS ──
    print(f"\n{'─'*60}")
    print("  STEP 1: Generating user profiles")
    print(f"{'─'*60}")

    total = CONFIG["total_users"]
    high_value_count  = int(total * CONFIG["high_value_pct"])
    regular_count     = int(total * CONFIG["regular_pct"])
    occasional_count  = int(total * CONFIG["occasional_pct"])
    dormant_count     = total - high_value_count - regular_count - occasional_count

    archetype_list = (
        ["high_value"]  * high_value_count  +
        ["regular"]     * regular_count     +
        ["occasional"]  * occasional_count  +
        ["dormant"]     * dormant_count
    )
    random.shuffle(archetype_list)

    users = []
    for i, archetype in enumerate(archetype_list):
        user = generate_user(i, archetype)
        users.append(user)
        if (i + 1) % 500 == 0:
            progress_bar(i + 1, total, "  Generating users")

    progress_bar(total, total, "  Generating users")
    print(f"\n  ✓ {total:,} users generated")
    print(f"    High Value:  {high_value_count:,}")
    print(f"    Regular:     {regular_count:,}")
    print(f"    Occasional:  {occasional_count:,}")
    print(f"    Dormant:     {dormant_count:,}")

    # ── STEP 2: UPLOAD PROFILES ──
    print(f"\n{'─'*60}")
    print("  STEP 2: Uploading profiles to CleverTap")
    print(f"{'─'*60}")

    profiles_done  = checkpoint.get("profiles_uploaded", 0)
    profiles_total = total
    all_errors     = checkpoint.get("errors", [])

    if profiles_done < profiles_total:
        start_idx    = profiles_done
        profile_recs = [user_to_ct_profile(u) for u in users[start_idx:]]
        batches      = [
            profile_recs[i:i + CONFIG["batch_size"]]
            for i in range(0, len(profile_recs), CONFIG["batch_size"])
        ]

        for batch_idx, batch in enumerate(batches):
            processed, unprocessed = upload_batch(batch, headers, "profile")
            profiles_done += processed

            if unprocessed:
                all_errors.append({
                    "type":  "profile",
                    "batch": batch_idx,
                    "count": len(unprocessed),
                })

            checkpoint["profiles_uploaded"] = profiles_done
            checkpoint["errors"]            = all_errors
            save_checkpoint(checkpoint)

            progress_bar(profiles_done, profiles_total, "  Uploading profiles")
            time.sleep(CONFIG["rate_limit"])

        print(f"\n  ✓ {profiles_done:,} profiles uploaded")
    else:
        print(f"  ✓ Profiles already uploaded ({profiles_done:,}) — skipping")

    # ── STEP 3: GENERATE AND UPLOAD EVENTS ──
    print(f"\n{'─'*60}")
    print("  STEP 3: Generating and uploading events")
    print(f"{'─'*60}")

    events_done = checkpoint.get("events_uploaded", 0)
    users_done  = checkpoint.get("users_events_done", 0)
    remaining   = [(i, u) for i, u in enumerate(users) if i >= users_done]

    print(f"\n  Users done so far:          {users_done:,}")
    print(f"  Remaining users to process: {len(remaining):,}")

    all_event_records = []

    for count, (user_idx, user) in enumerate(remaining):

        # Generate a compact but meaningful event set for this user
        user_events = generate_compact_events(user)
        all_event_records.extend(user_events)

        # Upload when we have a full batch of 200 or on last user
        while len(all_event_records) >= CONFIG["batch_size"]:
            batch = all_event_records[:CONFIG["batch_size"]]
            all_event_records = all_event_records[CONFIG["batch_size"]:]
            processed, _ = upload_batch(batch, headers, "event")
            events_done += processed
            time.sleep(CONFIG["rate_limit"])

        # Checkpoint every 200 users
        if (count + 1) % 200 == 0:
            checkpoint["events_uploaded"]   = events_done
            checkpoint["users_events_done"] = users_done + count + 1
            checkpoint["errors"]            = all_errors
            save_checkpoint(checkpoint)
            progress_bar(count + 1, len(remaining), "  Processing users")

    # Upload any remaining records
    if all_event_records:
        batches = [
            all_event_records[i:i + CONFIG["batch_size"]]
            for i in range(0, len(all_event_records), CONFIG["batch_size"])
        ]
        for batch in batches:
            processed, _ = upload_batch(batch, headers, "event")
            events_done += processed
            time.sleep(CONFIG["rate_limit"])

    progress_bar(len(remaining), len(remaining), "  Processing users")
    print(f"\n  ✓ {events_done:,} events uploaded")

    # ── SUMMARY ──
    end_time  = datetime.now()
    start_str = checkpoint.get("start_time", end_time.isoformat())
    start_dt  = datetime.fromisoformat(start_str)
    elapsed   = end_time - start_dt
    minutes   = int(elapsed.total_seconds() / 60)
    seconds   = int(elapsed.total_seconds() % 60)

    print(f"\n{'='*60}")
    print("  UPLOAD COMPLETE")
    print(f"{'='*60}")
    print(f"  Profiles uploaded : {profiles_done:,}")
    print(f"  Events uploaded   : {events_done:,}")
    print(f"  Errors            : {len(all_errors)}")
    print(f"  Time taken        : {minutes}m {seconds}s")
    print(f"{'='*60}\n")

    if all_errors:
        print(f"  ⚠️  {len(all_errors)} batches had errors.")
        print(f"  Check {CONFIG['checkpoint_file']} for details.")
    else:
        print("  ✓ No errors — all data uploaded successfully!")
        # Clean up checkpoint on success
        Path(CONFIG["checkpoint_file"]).unlink(missing_ok=True)

    print("\n  Next steps:")
    print("  1. Check CleverTap dashboard → Profiles (allow 5-10 mins)")
    print("  2. Check CleverTap dashboard → Events")
    print("  3. Build segments in CT using the Archetype property")
    print("  4. Build funnels: Category Viewed → Product Viewed → Charged")
    print()


if __name__ == "__main__":
    parser = argparse.ArgumentParser(description="CT Playground Dummy Data Generator")
    parser.add_argument("--resume", action="store_true",
                        help="Resume from checkpoint after interruption")
    args = parser.parse_args()
    main(resume=args.resume)
