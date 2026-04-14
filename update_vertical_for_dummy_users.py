#!/usr/bin/env python3
"""
=============================================================================
update_vertical_from_csv.py — Add Vertical property using CT exported CSV
=============================================================================

Reads email addresses from a CT exported profiles CSV and pushes
Vertical: Bazario and Vertical Type: Ecommerce to each profile.

USAGE:
  1. Export profiles from CT Dashboard → Profiles → Export
  2. Place the CSV in the same folder as this script
  3. Run:
     export CT_ACCOUNT_ID=your-id
     export CT_PASSCODE=your-passcode
     python3 update_vertical_from_csv.py --csv your_export.csv

  Or if your CSV is named profiles.csv:
     python3 update_vertical_from_csv.py
=============================================================================
"""

import os
import sys
import csv
import time
import argparse
import requests

# ─── Configuration ────────────────────────────────────────────────────────────
CT_UPLOAD_URL = "https://api.clevertap.com/1/upload"
BATCH_SIZE    = 200
RATE_LIMIT    = 0.1   # seconds between requests


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
    """
    Read email addresses from CT exported CSV.
    CT exports typically have an 'Email' or 'email' column.
    Tries multiple common column names automatically.
    """
    emails    = []
    skipped   = 0
    possible_cols = ['Email', 'email', 'EMAIL', 'email_address', 'Email Address']

    with open(filepath, newline='', encoding='utf-8-sig') as f:
        reader  = csv.DictReader(f)
        headers = reader.fieldnames or []

        print(f"  CSV columns found: {', '.join(headers)}")

        # Find which column has emails
        email_col = None
        for col in possible_cols:
            if col in headers:
                email_col = col
                break

        if not email_col:
            print(f"\n❌  Could not find email column in CSV.")
            print(f"    Available columns: {', '.join(headers)}")
            print(f"    Please check the CSV and update 'possible_cols' in the script.")
            sys.exit(1)

        print(f"  Using column: '{email_col}'")

        for row in reader:
            email = row.get(email_col, '').strip()
            if email and '@' in email:
                emails.append(email)
            else:
                skipped += 1

    print(f"  ✓ {len(emails):,} valid emails found")
    if skipped:
        print(f"  ⚠️  {skipped} rows skipped (missing or invalid email)")

    return emails


def upload_batch(records, headers):
    payload = {"d": records}
    try:
        response = requests.post(
            CT_UPLOAD_URL,
            headers=headers,
            json=payload,
            timeout=30
        )
        data = response.json()
        if data.get("status") == "success":
            return True, data.get("processed", len(records))
        else:
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
    parser = argparse.ArgumentParser(description="Update Vertical property from CT CSV export")
    parser.add_argument(
        "--csv",
        default="profiles.csv",
        help="Path to CT exported profiles CSV (default: profiles.csv)"
    )
    args = parser.parse_args()

    print("\n" + "="*60)
    print("  UPDATE VERTICAL FROM CSV — CT PLAYGROUND")
    print("="*60)

    # ── Check CSV exists ──
    if not os.path.exists(args.csv):
        print(f"\n❌  CSV file not found: {args.csv}")
        print(f"    Export profiles from CT Dashboard → Profiles → Export")
        print(f"    Then run: python3 update_vertical_from_csv.py --csv your_file.csv")
        sys.exit(1)

    print(f"\n  CSV file    : {args.csv}")
    print(f"  Vertical    : Bazario")
    print(f"  Vertical Type: Ecommerce")

    # ── Get credentials ──
    account_id, passcode = get_credentials()
    headers = {
        "X-CleverTap-Account-Id": account_id,
        "X-CleverTap-Passcode":   passcode,
        "Content-Type":           "application/json",
    }
    print(f"  Account     : {account_id}\n")

    # ── Read emails ──
    print("  Step 1: Reading emails from CSV...")
    emails = read_emails_from_csv(args.csv)

    if not emails:
        print("\n❌  No valid emails found in CSV. Exiting.")
        sys.exit(1)

    total = len(emails)

    # ── Upload in batches ──
    print(f"\n  Step 2: Pushing Vertical property to {total:,} profiles...")
    print(f"  Estimated time: ~{max(1, total // 200 // 10)} minutes\n")

    processed  = 0
    errors     = 0
    batch      = []
    start_time = __import__('time').time()

    for email in emails:
        batch.append({
            "type":     "profile",
            "identity": email,
            "profileData": {
                "Vertical":      "Bazario",
                "Vertical Type": "Ecommerce"
            }
        })

        if len(batch) >= BATCH_SIZE:
            success, count = upload_batch(batch, headers)
            if success:
                processed += count
            else:
                errors    += len(batch)
            batch = []
            time.sleep(RATE_LIMIT)
            progress_bar(processed + errors, total, "  Updating")

    # Upload remaining
    if batch:
        success, count = upload_batch(batch, headers)
        if success:
            processed += count
        else:
            errors    += len(batch)

    progress_bar(total, total, "  Updating")

    elapsed = int(time.time() - start_time)
    mins    = elapsed // 60
    secs    = elapsed %  60

    print(f"\n\n{'='*60}")
    print("  UPDATE COMPLETE")
    print(f"{'='*60}")
    print(f"  Profiles updated : {processed:,}")
    print(f"  Errors           : {errors}")
    print(f"  Time taken       : {mins}m {secs}s")
    print(f"{'='*60}")

    if errors == 0:
        print("\n  ✓ All profiles now have:")
        print("    Vertical      = Bazario")
        print("    Vertical Type = Ecommerce")
    else:
        print(f"\n  ⚠️  {errors} profiles failed.")
        print(f"  Run again to retry — already-updated profiles will just get re-confirmed.")

    print("\n  Verify in CT:")
    print("  Profiles → Filter: Vertical = Bazario → should return all profiles\n")


if __name__ == "__main__":
    main()
