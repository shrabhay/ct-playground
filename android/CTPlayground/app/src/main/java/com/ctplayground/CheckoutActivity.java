package com.ctplayground;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.clevertap.android.sdk.CleverTapAPI;
import com.ctplayground.utils.FirebaseHelper;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

public class CheckoutActivity extends AppCompatActivity {

    private int currentStep = 1;
    private String currentUid;
    private FirebaseUser currentUser;

    private List<Map<String, Object>> addresses = new ArrayList<>();
    private List<Map<String, Object>> cartItems = new ArrayList<>();
    private int selectedAddressIndex = -1;
    private String selectedPaymentMethod = "upi";

    // Step views
    private View layoutStep1, layoutStep2, layoutStep3;
    private Button btnStepBack, btnStepNext;

    // Step indicators
    private TextView tvStep1, tvStep2, tvStep3;
    private TextView tvStep1Label, tvStep2Label, tvStep3Label;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) currentUid = currentUser.getUid();

        initViews();
        loadCartItems();
        loadAddresses();
        fireCTPageViewed();

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
    }

    private void initViews() {
        layoutStep1 = findViewById(R.id.layoutStep1);
        layoutStep2 = findViewById(R.id.layoutStep2);
        layoutStep3 = findViewById(R.id.layoutStep3);
        btnStepBack = findViewById(R.id.btnStepBack);
        btnStepNext = findViewById(R.id.btnStepNext);
        tvStep1 = findViewById(R.id.tvStep1);
        tvStep2 = findViewById(R.id.tvStep2);
        tvStep3 = findViewById(R.id.tvStep3);
        tvStep1Label = findViewById(R.id.tvStep1Label);
        tvStep2Label = findViewById(R.id.tvStep2Label);
        tvStep3Label = findViewById(R.id.tvStep3Label);

        btnStepNext.setOnClickListener(v -> handleNext());
        btnStepBack.setOnClickListener(v -> handleBack());

        // Add new address button
        findViewById(R.id.btnAddNewAddress).setOnClickListener(v -> {
            LinearLayout form = findViewById(R.id.layoutAddressForm);
            form.setVisibility(form.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
        });

        // Save address button
        findViewById(R.id.btnSaveAddress).setOnClickListener(v -> saveNewAddress());
    }

    private void loadCartItems() {
        if (currentUid == null) return;
        FirebaseHelper.getCartItems(currentUid, new FirebaseHelper.CartItemsCallback() {
            @Override public void onSuccess(List<Map<String, Object>> items) {
                cartItems = items;
            }
            @Override public void onFailure(String error) {}
        });
    }

    private void loadAddresses() {
        if (currentUid == null) return;
        FirebaseHelper.getAddresses(currentUid, new FirebaseHelper.CartItemsCallback() {
            @Override public void onSuccess(List<Map<String, Object>> items) {
                addresses = items;
                renderAddressList();
            }
            @Override public void onFailure(String error) {
                renderAddressList();
            }
        });
    }

    private void renderAddressList() {
        LinearLayout container = findViewById(R.id.layoutAddressList);
        container.removeAllViews();

        if (addresses.isEmpty()) {
            // Auto-show the add form if no addresses
            findViewById(R.id.layoutAddressForm).setVisibility(View.VISIBLE);
            return;
        }

        for (int i = 0; i < addresses.size(); i++) {
            final int index = i;
            Map<String, Object> addr = addresses.get(i);

            View card = getLayoutInflater().inflate(R.layout.item_address, container, false);

            ((TextView) card.findViewById(R.id.tvAddressType)).setText(
                    String.valueOf(addr.get("type")));
            ((TextView) card.findViewById(R.id.tvAddressName)).setText(
                    String.valueOf(addr.get("name")));
            ((TextView) card.findViewById(R.id.tvAddressLine)).setText(
                    String.valueOf(addr.get("line")));
            ((TextView) card.findViewById(R.id.tvAddressPhone)).setText(
                    String.valueOf(addr.get("phone")));

            // Pre-select first address
            if (i == 0 && selectedAddressIndex == -1) {
                selectedAddressIndex = 0;
                updateAddressSelection(card, true);
            } else {
                updateAddressSelection(card, i == selectedAddressIndex);
            }

            card.setOnClickListener(v -> {
                selectedAddressIndex = index;
                renderAddressList(); // refresh to update selection UI
            });

            container.addView(card);
        }
    }

    private void updateAddressSelection(View card, boolean selected) {
        TextView indicator = card.findViewById(R.id.tvAddressSelected);
        indicator.setText(selected ? "●" : "○");
        indicator.setTextColor(getColor(selected ? R.color.bazario_orange : R.color.text_gray));
    }

    private void saveNewAddress() {
        String name  = getInputText(R.id.etAddrName);
        String phone = getInputText(R.id.etAddrPhone);
        String line  = getInputText(R.id.etAddrLine);

        if (name.isEmpty() || phone.isEmpty() || line.isEmpty()) {
            Toast.makeText(this, "Please fill in all address fields", Toast.LENGTH_SHORT).show();
            return;
        }

        RadioGroup rg = findViewById(R.id.rgAddressType);
        String type = "HOME";
        int checked = rg.getCheckedRadioButtonId();
        if (checked == R.id.rbWork)  type = "WORK";
        if (checked == R.id.rbOther) type = "OTHER";

        Map<String, Object> address = new HashMap<>();
        address.put("name",  name);
        address.put("phone", phone);
        address.put("line",  line);
        address.put("type",  type);

        String finalType = type;
        FirebaseHelper.saveAddress(currentUid, address,
                new FirebaseHelper.CartItemsCallback() {
                    @Override public void onSuccess(List<Map<String, Object>> items) {
                        addresses = items;
                        selectedAddressIndex = addresses.size() - 1;
                        findViewById(R.id.layoutAddressForm).setVisibility(View.GONE);
                        renderAddressList();
                        Toast.makeText(CheckoutActivity.this,
                                "Address saved!", Toast.LENGTH_SHORT).show();
                    }
                    @Override public void onFailure(String error) {
                        Toast.makeText(CheckoutActivity.this,
                                "Failed to save address", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void handleNext() {
        if (currentStep == 1) {
            if (selectedAddressIndex < 0 || selectedAddressIndex >= addresses.size()) {
                Toast.makeText(this, "Please select a delivery address", Toast.LENGTH_SHORT).show();
                return;
            }
            // Fire address selected event
            Map<String, Object> addr = addresses.get(selectedAddressIndex);
            fireCTAddressSelected(addr);
            goToStep(2);

        } else if (currentStep == 2) {
            selectedPaymentMethod = getSelectedPaymentMethod();
            fireCTPaymentSelected(selectedPaymentMethod);
            buildReviewScreen();
            goToStep(3);

        } else if (currentStep == 3) {
            placeOrder();
        }
    }

    private void handleBack() {
        if (currentStep > 1) goToStep(currentStep - 1);
        else finish();
    }

    private void goToStep(int step) {
        currentStep = step;
        layoutStep1.setVisibility(step == 1 ? View.VISIBLE : View.GONE);
        layoutStep2.setVisibility(step == 2 ? View.VISIBLE : View.GONE);
        layoutStep3.setVisibility(step == 3 ? View.VISIBLE : View.GONE);

        btnStepBack.setVisibility(step > 1 ? View.VISIBLE : View.GONE);
        btnStepNext.setText(step == 3 ? "Place Order 🎉" : "Continue");

        updateStepIndicator(step);
    }

    private void updateStepIndicator(int step) {
        int orange = getColor(R.color.bazario_orange);
        int gray   = getColor(R.color.text_gray);
        int white  = getColor(android.R.color.white);

        tvStep1.setBackgroundColor(step >= 1 ? orange : gray);
        tvStep2.setBackgroundColor(step >= 2 ? orange : gray);
        tvStep3.setBackgroundColor(step >= 3 ? orange : gray);

        tvStep1Label.setTextColor(step >= 1 ? orange : gray);
        tvStep2Label.setTextColor(step >= 2 ? orange : gray);
        tvStep3Label.setTextColor(step >= 3 ? orange : gray);

        tvStep1Label.setTypeface(null, step == 1
                ? android.graphics.Typeface.BOLD : android.graphics.Typeface.NORMAL);
        tvStep2Label.setTypeface(null, step == 2
                ? android.graphics.Typeface.BOLD : android.graphics.Typeface.NORMAL);
        tvStep3Label.setTypeface(null, step == 3
                ? android.graphics.Typeface.BOLD : android.graphics.Typeface.NORMAL);
    }

    private void buildReviewScreen() {
        Map<String, Object> addr = addresses.get(selectedAddressIndex);

        // Address
        String addrText = addr.get("name") + "\n" +
                addr.get("line") + "\n" +
                "Phone: " + addr.get("phone");
        ((TextView) findViewById(R.id.tvReviewAddress)).setText(addrText);

        // Payment
        ((TextView) findViewById(R.id.tvReviewPayment)).setText(
                getPaymentLabel(selectedPaymentMethod));

        // Items
        LinearLayout itemsContainer = findViewById(R.id.layoutReviewItems);
        itemsContainer.removeAllViews();
        for (Map<String, Object> item : cartItems) {
            TextView tv = new TextView(this);
            long qty = toLong(item.get("qty"));
            long price = toLong(item.get("price"));
            tv.setText(item.get("emoji") + "  " + item.get("name") +
                    " × " + qty + "  —  ₹" + String.format("%,d", price * qty));
            tv.setTextSize(12f);
            tv.setTextColor(getColor(R.color.bazario_dark));
            tv.setPadding(0, 4, 0, 4);
            itemsContainer.addView(tv);
        }

        // Price summary
        long mrp      = getMRP();
        long subtotal = getSubtotal();
        long discount = mrp - subtotal;
        long delivery = subtotal >= 499 ? 0 : 40;
        long total    = subtotal + delivery;

        int qty = getTotalQty();
        ((TextView) findViewById(R.id.tvReviewMRPLabel)).setText(
                "Price (" + qty + " item" + (qty > 1 ? "s" : "") + ")");
        ((TextView) findViewById(R.id.tvReviewMRP)).setText(
                "₹" + String.format("%,d", mrp));
        ((TextView) findViewById(R.id.tvReviewDiscount)).setText(
                "−₹" + String.format("%,d", discount));
        ((TextView) findViewById(R.id.tvReviewDelivery)).setText(
                delivery == 0 ? "FREE" : "₹" + delivery);
        ((TextView) findViewById(R.id.tvReviewTotal)).setText(
                "₹" + String.format("%,d", total));
    }

    private void placeOrder() {
        if (cartItems.isEmpty() || currentUid == null) return;
        btnStepNext.setEnabled(false);

        String orderId = "BZ-" + (10000000 + new Random().nextInt(90000000));
        String now = new SimpleDateFormat(
                "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()).format(new Date());

        Map<String, Object> addr = addresses.get(selectedAddressIndex);
        String addrLine = addr.get("name") + ", " + addr.get("line");

        long total = getSubtotal() + (getSubtotal() >= 499 ? 0 : 40);

        Map<String, Object> order = new HashMap<>();
        order.put("orderId",       orderId);
        order.put("date",          now);
        order.put("placedAt",      now);
        order.put("address",       addrLine);
        order.put("paymentMethod", selectedPaymentMethod);
        order.put("total",         total);
        order.put("items",         cartItems);

        FirebaseHelper.placeOrder(currentUid, orderId, order,
                new FirebaseHelper.OrderCallback() {
                    @Override public void onSuccess(String id) {
                        // Clear cart
                        FirebaseHelper.clearCart(currentUid,
                                new FirebaseHelper.ProfileCallback() {
                                    @Override public void onSuccess(Map<String, Object> p) {}
                                    @Override public void onFailure(String e) {}
                                });

                        // Update user order stats
                        FirebaseHelper.updateUserOrderStats(currentUid, total,
                                new FirebaseHelper.ProfileCallback() {
                                    @Override public void onSuccess(Map<String, Object> p) {
                                        // Update CT profile
                                        Object to = p.get("totalOrders");
                                        Object lv = p.get("lifetimeValue");
                                        CleverTapAPI ct = CleverTapAPI.getDefaultInstance(
                                                CheckoutActivity.this);
                                        if (ct != null && to != null && lv != null) {
                                            HashMap<String, Object> ctProfile = new HashMap<>();
                                            ctProfile.put("Total Orders",   to);
                                            ctProfile.put("Lifetime Value", lv);
                                            ct.pushProfile(ctProfile);
                                        }
                                    }
                                    @Override public void onFailure(String e) {}
                                });

                        // Fire CT Charged event
                        fireChargedEvent(orderId, total);

                        // Navigate to confirmation
                        Intent intent = new Intent(
                                CheckoutActivity.this, OrderConfirmationActivity.class);
                        intent.putExtra("order_id",       orderId);
                        intent.putExtra("order_total",    total);
                        intent.putExtra("payment_method", selectedPaymentMethod);
                        startActivity(intent);
                        finish();
                    }
                    @Override public void onFailure(String error) {
                        Toast.makeText(CheckoutActivity.this,
                                "Failed to place order. Try again.", Toast.LENGTH_SHORT).show();
                        btnStepNext.setEnabled(true);
                    }
                });
    }

    // ── CT Events ─────────────────────────────────────────────────────────────

    private void fireCTPageViewed() {
        CleverTapAPI ct = CleverTapAPI.getDefaultInstance(this);
        if (ct != null) {
            HashMap<String, Object> props = new HashMap<>();
            props.put("item_count",  getTotalQty());
            props.put("cart_value",  getSubtotal());
            props.put("step",        "address");
            props.put("vertical",    "bazario");
            ct.pushEvent("Checkout Page Viewed", props);
        }
    }

    private void fireCTAddressSelected(Map<String, Object> addr) {
        CleverTapAPI ct = CleverTapAPI.getDefaultInstance(this);
        if (ct != null) {
            HashMap<String, Object> props = new HashMap<>();
            props.put("address_type", addr.get("type"));
            props.put("cart_value",   getSubtotal());
            props.put("vertical",     "bazario");
            ct.pushEvent("Address Selected", props);
        }
    }

    private void fireCTPaymentSelected(String method) {
        CleverTapAPI ct = CleverTapAPI.getDefaultInstance(this);
        if (ct != null) {
            HashMap<String, Object> props = new HashMap<>();
            props.put("payment_method", method);
            props.put("cart_value",     getSubtotal());
            props.put("vertical",       "bazario");
            ct.pushEvent("Payment Method Selected", props);
        }
    }

    private void fireChargedEvent(String orderId, long total) {
        CleverTapAPI ct = CleverTapAPI.getDefaultInstance(this);
        if (ct == null) return;

        // CT reserved Charged event — specific format required
        HashMap<String, Object> chargeDetails = new HashMap<>();
        chargeDetails.put("Amount",       total);
        chargeDetails.put("Payment mode", getPaymentLabel(selectedPaymentMethod));
        chargeDetails.put("Charged ID",   orderId);

        ArrayList<HashMap<String, Object>> itemsList = new ArrayList<>();
        for (Map<String, Object> item : cartItems) {
            HashMap<String, Object> ctItem = new HashMap<>();
            ctItem.put("Product name", item.get("name"));
            ctItem.put("Quantity",     toLong(item.get("qty")));
            ctItem.put("Amount",       toLong(item.get("price")) * toLong(item.get("qty")));
            ctItem.put("Category",     item.get("category"));
            itemsList.add(ctItem);
        }

        ct.pushChargedEvent(chargeDetails, itemsList);

        // Also fire Order Confirmed custom event
        HashMap<String, Object> props = new HashMap<>();
        props.put("order_id",       orderId);
        props.put("order_value",    total);
        props.put("item_count",     getTotalQty());
        props.put("payment_method", selectedPaymentMethod);
        props.put("vertical",       "bazario");
        ct.pushEvent("Order Confirmed", props);
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private String getSelectedPaymentMethod() {
        RadioGroup rg = findViewById(R.id.rgPayment);
        int id = rg.getCheckedRadioButtonId();
        if (id == R.id.rbCard)       return "card";
        if (id == R.id.rbCOD)        return "cod";
        if (id == R.id.rbNetBanking) return "netbanking";
        if (id == R.id.rbEMI)        return "emi";
        return "upi";
    }

    private String getPaymentLabel(String method) {
        switch (method) {
            case "card":       return "💳 Credit / Debit Card";
            case "cod":        return "💵 Cash on Delivery";
            case "netbanking": return "🏦 Net Banking";
            case "emi":        return "📆 EMI";
            default:           return "📱 UPI";
        }
    }

    private String getInputText(int viewId) {
        TextInputEditText et = findViewById(viewId);
        return et.getText() != null ? et.getText().toString().trim() : "";
    }

    private long getMRP() {
        long mrp = 0;
        for (Map<String, Object> item : cartItems)
            mrp += toLong(item.get("originalPrice")) * toLong(item.get("qty"));
        return mrp;
    }

    private long getSubtotal() {
        long total = 0;
        for (Map<String, Object> item : cartItems)
            total += toLong(item.get("price")) * toLong(item.get("qty"));
        return total;
    }

    private int getTotalQty() {
        int qty = 0;
        for (Map<String, Object> item : cartItems) qty += (int) toLong(item.get("qty"));
        return qty;
    }

    private long toLong(Object val) {
        if (val instanceof Long)    return (Long) val;
        if (val instanceof Integer) return ((Integer) val).longValue();
        if (val instanceof Double)  return ((Double) val).longValue();
        return 0L;
    }
}
