# CT Playground — Data Scripts

Python scripts for generating and managing dummy data in CleverTap.

## Setup
```bash
export CT_ACCOUNT_ID=your-account-id
export CT_PASSCODE=your-passcode
pip3 install requests
```

## Scripts

| Script | Purpose | Run Once? |
|---|---|---|
| `generate_and_upload.py` | Generate 15,000 Bazario users + 3.2M events | ✓ Done |
| `update_vertical_from_csv.py` | Push Vertical property to all profiles | ✓ Done |
| `create_cart_abandoners.py` | Create cart abandonment events for ~25% of users | ✓ Done |
| `fix_joined_at.py` | Fix Joined At + Last Order Date as CT date properties | ✓ Done |

## Future Scripts (when verticals are built)

- `generate_fintech.py` — Fintech dummy data
- `generate_subscription.py` — Subscription dummy data  
- `generate_foodtech.py` — Foodtech dummy data

## Notes

- Always run from the `ct-playground` root directory
- CSV exports from CT go in `ct-playground/` root — not inside this folder
- Checkpoint file `ct_upload_checkpoint.json` saves in root during upload
- Each vertical uses a different email domain to avoid profile overlap