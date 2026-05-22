package com.ctplayground;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.clevertap.android.sdk.CleverTapAPI;
import com.ctplayground.adapters.CartAdapter;
import com.ctplayground.utils.FirebaseHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CartActivity extends AppCompatActivity {

    private List<Map<String, Object>> cartItems = new ArrayList<>();
    private CartAdapter adapter;
    private String currentUid;

    // Coupon state
    private boolean couponApplied  = false;
    private String  couponCode     = "";
    private int     couponDiscount = 0; // percentage

    private static final Map<String, Integer> VALID_COUPONS = new HashMap<>();
    static {
        VALID_COUPONS.put("BAZARIO10", 10);
        VALID_COUPONS.put("NEWUSER",   15);
        VALID_COUPONS.put("SAVE20",    20);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) currentUid = user.getUid();

        setupNavbar();
        setupRecyclerView();
        setupCoupon();
        // loadCart() removed from here
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadCart(); // now runs every time the screen becomes visible
    }

    private void setupNavbar() {
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
        findViewById(R.id.btnAccount).setOnClickListener(v ->
                startActivity(new Intent(this, AccountActivity.class)));
    }

    private void setupRecyclerView() {
        RecyclerView rv = findViewById(R.id.rvCartItems);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CartAdapter(cartItems, new CartAdapter.CartItemListener() {

            @Override
            public void onQtyChange(Map<String, Object> item, int newQty) {
                String productId = (String) item.get("id");
                item.put("qty", (long) newQty);
                adapter.notifyDataSetChanged();
                updateSummary();
                updateCartTitle();

                // CT event
                fireCTCartEvent("Cart Quantity Updated", item, newQty);

                // Firestore update
                if (currentUid != null) {
                    FirebaseHelper.updateCartQty(currentUid, productId, newQty,
                            new FirebaseHelper.ProfileCallback() {
                                @Override public void onSuccess(Map<String, Object> p) {}
                                @Override public void onFailure(String e) {}
                            });
                }
            }

            @Override
            public void onRemoveItem(Map<String, Object> item) {
                String productId = (String) item.get("id");
                cartItems.remove(item);
                adapter.notifyDataSetChanged();
                updateSummary();
                updateCartTitle();
                showEmptyOrContent();

                // CT event
                fireCTCartEvent("Removed from Cart", item, 0);

                // Firestore update
                if (currentUid != null) {
                    FirebaseHelper.updateCartQty(currentUid, productId, 0,
                            new FirebaseHelper.ProfileCallback() {
                                @Override public void onSuccess(Map<String, Object> p) {}
                                @Override public void onFailure(String e) {}
                            });
                }
            }
        });
        rv.setAdapter(adapter);
    }

    private void loadCart() {
        if (currentUid == null) {
            showEmptyOrContent();
            return;
        }
        FirebaseHelper.getCartItems(currentUid, new FirebaseHelper.CartItemsCallback() {
            @Override public void onSuccess(List<Map<String, Object>> items) {
                cartItems.clear();
                cartItems.addAll(items);
                adapter.updateItems(cartItems);
                showEmptyOrContent();
                updateSummary();
                fireCTCartViewed();
            }
            @Override public void onFailure(String error) {
                showEmptyOrContent();
            }
        });
    }

    private void showEmptyOrContent() {
        LinearLayout layoutEmpty   = findViewById(R.id.layoutEmpty);
        LinearLayout layoutContent = findViewById(R.id.layoutCartContent);

        if (cartItems.isEmpty()) {
            layoutEmpty.setVisibility(View.VISIBLE);
            layoutContent.setVisibility(View.GONE);
            findViewById(R.id.btnContinueShopping).setOnClickListener(v -> finish());
        } else {
            layoutEmpty.setVisibility(View.GONE);
            layoutContent.setVisibility(View.VISIBLE);

            int totalQty = getTotalQty();
            ((TextView) findViewById(R.id.tvCartTitle)).setText(
                    "My Cart (" + totalQty + ")");
            ((TextView) findViewById(R.id.tvItemsHeader)).setText(
                    cartItems.size() + " item" + (cartItems.size() > 1 ? "s" : "") + " in your cart");
        }
    }

    private void updateSummary() {
        long mrp      = getMRP();
        long subtotal = getSubtotal();
        long discount = mrp - subtotal;
        long couponSaving = couponApplied ? (subtotal * couponDiscount / 100) : 0;
        long total    = subtotal - couponSaving;
        long delivery = total >= 499 ? 0 : 40;
        long finalTotal = total + delivery;
        long totalSavings = discount + couponSaving + (delivery == 0 ? 40 : 0);

        int totalQty = getTotalQty();
        ((TextView) findViewById(R.id.tvMRPLabel)).setText(
                "Price (" + totalQty + " item" + (totalQty > 1 ? "s" : "") + ")");
        ((TextView) findViewById(R.id.tvMRPValue)).setText("₹" + String.format("%,d", mrp));
        ((TextView) findViewById(R.id.tvDiscountValue)).setText(
                "−₹" + String.format("%,d", discount));
        ((TextView) findViewById(R.id.tvDeliveryValue)).setText(
                delivery == 0 ? "FREE" : "₹" + delivery);
        ((TextView) findViewById(R.id.tvTotalValue)).setText(
                "₹" + String.format("%,d", finalTotal));

        // Coupon row
        LinearLayout couponRow = findViewById(R.id.layoutCouponRow);
        if (couponApplied) {
            couponRow.setVisibility(View.VISIBLE);
            ((TextView) findViewById(R.id.tvCouponLabel)).setText(
                    "Coupon (" + couponCode + ")");
            ((TextView) findViewById(R.id.tvCouponValue)).setText(
                    "−₹" + String.format("%,d", couponSaving));
        } else {
            couponRow.setVisibility(View.GONE);
        }

        // Savings message
        TextView tvSavings = findViewById(R.id.tvSavings);
        if (totalSavings > 0) {
            tvSavings.setVisibility(View.VISIBLE);
            tvSavings.setText("🎉 You're saving ₹" + String.format("%,d", totalSavings) +
                    " on this order!");
        } else {
            tvSavings.setVisibility(View.GONE);
        }

        // Checkout button
        ((Button) findViewById(R.id.btnCheckout)).setOnClickListener(v -> handleCheckout());
    }

    private void updateCartTitle() {
        int totalQty = getTotalQty();
        ((TextView) findViewById(R.id.tvCartTitle)).setText(
                "My Cart (" + totalQty + ")");
        ((TextView) findViewById(R.id.tvItemsHeader)).setText(
                cartItems.size() + " item" + (cartItems.size() > 1 ? "s" : "") +
                        " in your cart");
    }

    private void setupCoupon() {
        Button btnApply = findViewById(R.id.btnApplyCoupon);
        btnApply.setOnClickListener(v -> {
            EditText etCoupon = findViewById(R.id.etCoupon);
            String code = etCoupon.getText().toString().trim().toUpperCase();
            if (code.isEmpty()) {
                Toast.makeText(this, "Please enter a coupon code", Toast.LENGTH_SHORT).show();
                return;
            }
            applyCoupon(code);
        });

        findViewById(R.id.btnRemoveCoupon).setOnClickListener(v -> removeCoupon());
    }

    private void removeCoupon() {
        couponApplied  = false;
        couponCode     = "";
        couponDiscount = 0;

        TextView tvStatus = findViewById(R.id.tvCouponStatus);
        tvStatus.setVisibility(View.GONE);
        findViewById(R.id.btnRemoveCoupon).setVisibility(View.GONE);

        EditText etCoupon = findViewById(R.id.etCoupon);
        etCoupon.setText("");

        updateSummary();
        Toast.makeText(this, "Coupon removed", Toast.LENGTH_SHORT).show();
    }

    private void applyCoupon(String code) {
        TextView tvStatus = findViewById(R.id.tvCouponStatus);
        tvStatus.setVisibility(View.VISIBLE);

        CleverTapAPI ct = CleverTapAPI.getDefaultInstance(this);

        if (VALID_COUPONS.containsKey(code)) {
            couponApplied  = true;
            couponCode     = code;
            couponDiscount = VALID_COUPONS.get(code);
            tvStatus.setText("✓ Coupon " + code + " applied! " + couponDiscount + "% off");
            tvStatus.setTextColor(getColor(android.R.color.holo_green_dark));
            findViewById(R.id.btnRemoveCoupon).setVisibility(View.VISIBLE);
            updateSummary();

            if (ct != null) {
                HashMap<String, Object> props = new HashMap<>();
                props.put("coupon_code",  code);
                props.put("discount_pct", couponDiscount);
                props.put("cart_value",   getSubtotal());
                props.put("vertical",     "bazario");
                ct.pushEvent("Coupon Applied", props);
            }
        } else {
            tvStatus.setText("✗ Invalid coupon code");
            tvStatus.setTextColor(getColor(R.color.error_red));
            findViewById(R.id.btnRemoveCoupon).setVisibility(View.GONE);

            if (ct != null) {
                HashMap<String, Object> props = new HashMap<>();
                props.put("coupon_code", code);
                props.put("cart_value",  getSubtotal());
                props.put("vertical",    "bazario");
                ct.pushEvent("Coupon Failed", props);
            }
        }
    }

    private void handleCheckout() {
        if (cartItems.isEmpty()) return;

        // Fire Checkout Initiated
        CleverTapAPI ct = CleverTapAPI.getDefaultInstance(this);
        if (ct != null) {
            HashMap<String, Object> props = new HashMap<>();
            props.put("item_count",      getTotalQty());
            props.put("cart_value",      getSubtotal());
            props.put("top_category",    getMostFrequentCategory());
            props.put("category_count",  getUniqueCategoryCount());
            props.put("coupon_applied",  couponApplied);
            props.put("coupon_code",     couponCode);
            props.put("vertical",        "bazario");
            ct.pushEvent("Checkout Initiated", props);
        }

        startActivity(new Intent(this, CheckoutActivity.class));
    }

    // ── CT event helpers ──────────────────────────────────────────────────────

    private void fireCTCartViewed() {
        CleverTapAPI ct = CleverTapAPI.getDefaultInstance(this);
        if (ct != null) {
            HashMap<String, Object> props = new HashMap<>();
            props.put("item_count",        getTotalQty());
            props.put("cart_value",        getSubtotal());
            props.put("top_category",      getMostFrequentCategory());
            props.put("unique_categories", getUniqueCategoryCount());
            props.put("vertical",          "bazario");
            ct.pushEvent("Cart Viewed", props);
        }
    }

    private void fireCTCartEvent(String eventName, Map<String, Object> item, int qty) {
        CleverTapAPI ct = CleverTapAPI.getDefaultInstance(this);
        if (ct != null) {
            HashMap<String, Object> props = new HashMap<>();
            props.put("product_id",   item.get("id"));
            props.put("product_name", item.get("name"));
            props.put("category",     item.get("category"));
            props.put("price",        item.get("price"));
            props.put("quantity",     qty);
            props.put("cart_value",   getSubtotal());
            props.put("vertical",     "bazario");
            ct.pushEvent(eventName, props);
        }
    }

    // ── Calculation helpers ────────────────────────────────────────────────────

    private long getMRP() {
        long mrp = 0;
        for (Map<String, Object> item : cartItems) {
            long op  = toLong(item.get("originalPrice"));
            long qty = toLong(item.get("qty"));
            mrp += op * qty;
        }
        return mrp;
    }

    private long getSubtotal() {
        long total = 0;
        for (Map<String, Object> item : cartItems) {
            long price = toLong(item.get("price"));
            long qty   = toLong(item.get("qty"));
            total += price * qty;
        }
        return total;
    }

    private int getTotalQty() {
        int qty = 0;
        for (Map<String, Object> item : cartItems) qty += (int) toLong(item.get("qty"));
        return qty;
    }

    private String getMostFrequentCategory() {
        Map<String, Integer> freq = new HashMap<>();
        for (Map<String, Object> item : cartItems) {
            String cat = (String) item.get("category");
            int qty = (int) toLong(item.get("qty"));
            freq.put(cat, freq.getOrDefault(cat, 0) + qty);
        }
        return freq.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("");
    }

    private int getUniqueCategoryCount() {
        Set<String> cats = new HashSet<>();
        for (Map<String, Object> item : cartItems) cats.add((String) item.get("category"));
        return cats.size();
    }

    private long toLong(Object val) {
        if (val instanceof Long)    return (Long) val;
        if (val instanceof Integer) return ((Integer) val).longValue();
        if (val instanceof Double)  return ((Double) val).longValue();
        return 0L;
    }
}
