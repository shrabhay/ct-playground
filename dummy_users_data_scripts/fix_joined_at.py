#!/usr/bin/env python3
"""
=============================================================================
fix_joined_at.py — Fix Joined At date property for CT Playground profiles
=============================================================================

Reads email addresses from a CT exported CSV and pushes a realistic
Joined At date in CT's $D_EPOCH format so date-based segments work.

Since the CSV only contains emails (no Joined At values), this script
generates realistic signup dates spread across 2020-2025 — matching
the distribution used in the original dummy data script.

USAGE:
  export CT_ACCOUNT_ID=your-id
  export CT_PASSCODE=your-passcode
  python3 fix_joined_at.py --csv your_export.csv
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

CT_UPLOAD_URL = "https://api.clevertap.com/1/upload"
BATCH_SIZE    = 200
RATE_LIMIT    = 0.1

# Date ranges spread across 2020-2025, weighted toward 2022-2023
DATE_RANGES = [
    (datetime(2020, 1, 1),  datetime(2020, 12, 31), 10),
    (datetime(2021, 1, 1),  datetime(2021, 12, 31), 15),
    (datetime(2022, 1, 1),  datetime(2022, 12, 31), 20),
    (datetime(2023, 1, 1),  datetime(2023, 12, 31), 25),
    (datetime(2024, 1, 1),  datetime(2024, 12, 31), 20),
    (datetime(2025, 1, 1),  datetime(2025, 12, 31), 10),
]


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
    skipped       = 0
    possible_cols = ['Email', 'email', 'EMAIL', 'email_address', 'Email Address']

    with open(filepath, newline='', encoding='utf-8-sig') as f:
        reader  = csv.DictReader(f)
        headers = reader.fieldnames or []

        print(f"  CSV columns: {', '.join(headers)}")

        email_col = next((c for c in possible_cols if c in headers), None)
        if not email_col:
            print(f"\n❌  Could not find email column.")
            print(f"    Available: {', '.join(headers)}")
            sys.exit(1)

        print(f"  Using column: '{email_col}'")

        for row in reader:
            email = row.get(email_col, '').strip()
            if email and '@' in email:
                emails.append(email)
            else:
                skipped += 1

    print(f"  ✓ {len(emails):,} emails found")
    if skipped:
        print(f"  ⚠️  {skipped} rows skipped")

    return emails


def generate_joined_at():
    """Generate a realistic signup date across 2020-2025."""
    weights = [r[2] for r in DATE_RANGES]
    chosen  = random.choices(DATE_RANGES, weights=weights, k=1)[0]
    start, end, _ = chosen
    delta   = end - start
    offset  = timedelta(seconds=random.randint(0, int(delta.total_seconds())))
    return int((start + offset).timestamp())


def generate_last_order_date():
    """
    Generate a realistic Last Order Date.
    Weighted toward recent dates — most active users ordered recently.
    """
    order_ranges = [
        (datetime(2020, 1, 1),  datetime(2021, 12, 31), 10),  # Historical
        (datetime(2022, 1, 1),  datetime(2022, 12, 31), 15),  # Older
        (datetime(2023, 1, 1),  datetime(2023, 12, 31), 20),  # Mid
        (datetime(2024, 1, 1),  datetime(2024, 12, 31), 30),  # Recent
        (datetime(2025, 1, 1),  datetime(2025, 12, 31), 25),  # Latest
    ]
    weights = [r[2] for r in order_ranges]
    chosen  = random.choices(order_ranges, weights=weights, k=1)[0]
    start, end, _ = chosen
    delta   = end - start
    offset  = timedelta(seconds=random.randint(0, int(delta.total_seconds())))
    return int((start + offset).timestamp())


def to_ct_date(epoch):
    return f"$D_{epoch}"


def upload_batch(records, headers):
    try:
        response = requests.post(
            CT_UPLOAD_URL,
            headers=headers,
            json={"d": records},
            timeout=30
        )
        data = response.json()
        if data.get("status") == "success":
            return True, data.get("processed", len(records))
        return False, 0
    except Exception as e:
        print(f"\n  ⚠️  Request error: {e}")
        return False, 0


def progress_bar(current, total, prefix="", width=40):
    pct    = current / total if total > 0 else 0
    filled = int(width * pct)
    bar    = "█" * filled + "░" * (width - filled)
    print(f"\r{prefix} [{bar}] {current:,}/{total:,} ({pct*100:.1f}%)",
          end="", flush=True)


def main():
    parser = argparse.ArgumentParser(description="Fix Joined At date property")
    parser.add_argument("--csv", default="profiles.csv",
                        help="Path to CT exported profiles CSV")
    args = parser.parse_args()

    print("\n" + "="*60)
    print("  FIX JOINED AT — CT PLAYGROUND")
    print("="*60)
    print("\n  Generates realistic Joined At dates (2020-2025)")
    print("  and pushes them in CT $D_EPOCH format.")
    print("  Enables segments like 'Joined At in last 90 days'\n")

    if not os.path.exists(args.csv):
        print(f"\n❌  CSV file not found: {args.csv}")
        sys.exit(1)

    account_id, passcode = get_credentials()
    ct_headers = {
        "X-CleverTap-Account-Id": account_id,
        "X-CleverTap-Passcode":   passcode,
        "Content-Type":           "application/json",
    }
    print(f"  Account: {account_id}\n")

    # ── Read emails ──
    print(f"  Step 1: Reading emails from {args.csv}...")
    emails = read_emails_from_csv(args.csv)
    total  = len(emails)

    # ── Generate dates ──
    print(f"\n  Step 2: Generating signup dates...")
    random.seed(777)

    dated_profiles = []
    year_counts    = {}

    for email in emails:
        epoch = generate_joined_at()
        year  = datetime.fromtimestamp(epoch).year
        year_counts[year] = year_counts.get(year, 0) + 1

        # Generate Last Order Date
        # 85% of users have at least one order (15% dormant with no recent order)
        if random.random() < 0.85:
            last_order_epoch = generate_last_order_date()
        else:
            last_order_epoch = None

        dated_profiles.append((email, epoch, last_order_epoch))

    print(f"\n  Signup distribution across years:")
    for year in sorted(year_counts.keys()):
        count = year_counts[year]
        bar   = "█" * (count // 150)
        print(f"    {year}: {count:,} users  {bar}")

    print(f"\n  Sample (first 3 profiles):")
    for email, epoch, last_order_epoch in dated_profiles[:3]:
        dt  = datetime.fromtimestamp(epoch)
        lod = datetime.fromtimestamp(last_order_epoch).strftime('%d %b %Y') if last_order_epoch else 'No orders'
        print(f"    {email[:35]:<35} Joined: {dt.strftime('%d %b %Y')}  Last Order: {lod}")

    lod_count = sum(1 for _, _, lod in dated_profiles if lod)
    print(f"\n  Will update:")
    print(f"    Joined At       → all {total:,} profiles")
    print(f"    Last Order Date → {lod_count:,} profiles (85% of users)")

    confirm = input(f"\n  Proceed? (y/n): ").strip().lower()
    if confirm != 'y':
        print("  Cancelled.")
        sys.exit(0)

    # ── Upload ──
    print(f"\n  Step 3: Pushing Joined At to CT...")

    batch     = []
    processed = 0
    errors    = 0

    for email, epoch, last_order_epoch in dated_profiles:
        profile_data = {
            "Joined At": to_ct_date(epoch)
        }
        if last_order_epoch:
            profile_data["Last Order Date"] = to_ct_date(last_order_epoch)

        batch.append({
            "type":     "profile",
            "identity": email,
            "profileData": profile_data
        })

        if len(batch) >= BATCH_SIZE:
            success, count = upload_batch(batch, ct_headers)
            if success:
                processed += count
            else:
                errors    += len(batch)
            batch = []
            time.sleep(RATE_LIMIT)
            progress_bar(processed + errors, total, "  Updating")

    if batch:
        success, count = upload_batch(batch, ct_headers)
        if success:
            processed += count
        else:
            errors    += len(batch)

    progress_bar(total, total, "  Updating")

    print(f"\n\n{'='*60}")
    print("  COMPLETE")
    print(f"{'='*60}")
    print(f"  Profiles updated : {processed:,}")
    print(f"  Errors           : {errors}")
    print(f"{'='*60}")

    if errors == 0:
        print("\n  ✓ Joined At is now a proper CT date property.")
        print("\n  Segments you can now build:")
        print("    Joined At in last 90 days          → New Users")
        print("    Joined At before 2022-01-01        → Long-term Users")
        print("    Last Order Date in last 30 days    → Recently Active")
        print("    Last Order Date before 180 days    → Lapsed Buyers")
    else:
        print(f"\n  ⚠️  {errors} profiles failed — run again to retry.")

    print()


if __name__ == "__main__":
    main()
