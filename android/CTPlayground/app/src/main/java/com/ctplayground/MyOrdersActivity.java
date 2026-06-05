package com.ctplayground;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.clevertap.android.sdk.CleverTapAPI;
import com.ctplayground.data.Product;
import com.ctplayground.data.ProductCatalogue;
import com.ctplayground.utils.FirebaseHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MyOrdersActivity extends AppCompatActivity {

    private String currentUid;
    private List<Map<String, Object>> orders = new ArrayList<>();
    private RecyclerView rvOrders;
    private ProgressBar progressBar;
    private LinearLayout layoutEmpty;
    private LinearLayout layoutFooter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) currentUid = user.getUid();

        rvOrders     = findViewById(R.id.rvOrders);
        progressBar  = findViewById(R.id.progressBar);
        layoutEmpty  = findViewById(R.id.layoutEmpty);
        layoutFooter = findViewById(R.id.layoutFooter);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
        findViewById(R.id.btnContinueShopping).setOnClickListener(v ->
                startActivity(new Intent(this, ListingActivity.class)));

        rvOrders.setLayoutManager(new LinearLayoutManager(this));
        loadOrders();
    }

    private void loadOrders() {
        if (currentUid == null) { showEmpty(); return; }

        FirebaseHelper.getOrders(currentUid, new FirebaseHelper.CartItemsCallback() {
            @Override
            public void onSuccess(List<Map<String, Object>> items) {
                progressBar.setVisibility(View.GONE);
                orders = items;
                if (orders.isEmpty()) {
                    showEmpty();
                } else {
                    updateTitle(orders.size());
                    rvOrders.setVisibility(View.VISIBLE);
                    layoutFooter.setVisibility(View.VISIBLE);
                    rvOrders.setAdapter(new OrdersAdapter());
                    fireCTOrderHistoryViewed();
                }
            }
            @Override
            public void onFailure(String error) {
                progressBar.setVisibility(View.GONE);
                showEmpty();
            }
        });
    }

    private void showEmpty() {
        progressBar.setVisibility(View.GONE);
        layoutEmpty.setVisibility(View.VISIBLE);
        rvOrders.setVisibility(View.GONE);
        layoutFooter.setVisibility(View.GONE);
    }

    private void updateTitle(int count) {
        ((TextView) findViewById(R.id.tvTitle)).setText(
                "My Orders (" + count + ")");
    }

    // ── Reorder ───────────────────────────────────────────────────────────────

    private void handleReorder(Map<String, Object> order) {
        List<Map<String, Object>> items =
                (List<Map<String, Object>>) order.get("items");
        if (items == null || items.isEmpty() || currentUid == null) return;

        Toast.makeText(this, "Adding items to cart...", Toast.LENGTH_SHORT).show();

        // int[] trick — allows mutation inside lambdas
        int[] remaining = {items.size()};

        for (Map<String, Object> item : items) {
            String  productId = String.valueOf(item.get("id"));
            Product product   = ProductCatalogue.getById(productId);

            if (product == null) {
                remaining[0]--;
                if (remaining[0] == 0) navigateToCart(order);
                continue;
            }

            FirebaseHelper.addToCart(currentUid, product,
                    new FirebaseHelper.ProfileCallback() {
                        @Override public void onSuccess(Map<String, Object> p) {
                            remaining[0]--;
                            if (remaining[0] == 0) navigateToCart(order);
                        }
                        @Override public void onFailure(String e) {
                            remaining[0]--;
                            if (remaining[0] == 0) navigateToCart(order);
                        }
                    });
        }

        // CT event
        CleverTapAPI ct = CleverTapAPI.getDefaultInstance(this);
        if (ct != null) {
            HashMap<String, Object> props = new HashMap<>();
            props.put("order_id",   order.get("orderId"));
            props.put("item_count", items.size());
            props.put("vertical",   "bazario");
            ct.pushEvent("Reorder Initiated", props);
        }
    }

    private void navigateToCart(Map<String, Object> order) {
        runOnUiThread(() -> {
            Toast.makeText(this,
                    "Items added! Redirecting to cart...", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, CartActivity.class));
        });
    }

    // ── CT Events ─────────────────────────────────────────────────────────────

    private void fireCTOrderHistoryViewed() {
        CleverTapAPI ct = CleverTapAPI.getDefaultInstance(this);
        if (ct != null) {
            HashMap<String, Object> props = new HashMap<>();
            props.put("order_count", orders.size());
            props.put("vertical",    "bazario");
            ct.pushEvent("Order History Viewed", props);
        }
    }

    // ── Adapter ───────────────────────────────────────────────────────────────

    private class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.ViewHolder> {

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_order, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder h, int position) {
            Map<String, Object> order = orders.get(position);

            h.tvOrderId.setText(String.valueOf(order.get("orderId")));
            h.tvOrderDate.setText(formatDate(String.valueOf(order.get("date"))));
            h.tvOrderTotal.setText("₹" + String.format("%,d", toLong(order.get("total"))));
            h.tvOrderPayment.setText(
                    getPaymentLabel(String.valueOf(order.get("paymentMethod"))));

            // Items preview
            h.layoutOrderItems.removeAllViews();
            List<Map<String, Object>> items =
                    (List<Map<String, Object>>) order.get("items");
            if (items != null) {
                int preview = Math.min(items.size(), 2);
                for (int i = 0; i < preview; i++) {
                    Map<String, Object> item = items.get(i);
                    TextView tv = new TextView(MyOrdersActivity.this);
                    long qty = toLong(item.get("qty"));
                    tv.setText(item.get("emoji") + "  " + item.get("name") +
                            (qty > 1 ? "  ×" + qty : ""));
                    tv.setTextSize(12f);
                    tv.setTextColor(0xFF555555);
                    tv.setMaxLines(1);
                    tv.setEllipsize(android.text.TextUtils.TruncateAt.END);
                    tv.setPadding(0, 0, 0, 4);
                    h.layoutOrderItems.addView(tv);
                }
                if (items.size() > 2) {
                    TextView more = new TextView(MyOrdersActivity.this);
                    more.setText("+" + (items.size() - 2) + " more item" +
                            (items.size() - 2 > 1 ? "s" : ""));
                    more.setTextSize(12f);
                    more.setTextColor(0xFF2874F0);
                    h.layoutOrderItems.addView(more);
                }
            }

            h.btnReorder.setOnClickListener(v -> handleReorder(order));
        }

        @Override public int getItemCount() { return orders.size(); }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView     tvOrderId, tvOrderDate, tvOrderTotal, tvOrderPayment;
            LinearLayout layoutOrderItems;
            Button       btnReorder;

            ViewHolder(@NonNull View v) {
                super(v);
                tvOrderId        = v.findViewById(R.id.tvOrderId);
                tvOrderDate      = v.findViewById(R.id.tvOrderDate);
                tvOrderTotal     = v.findViewById(R.id.tvOrderTotal);
                tvOrderPayment   = v.findViewById(R.id.tvOrderPayment);
                layoutOrderItems = v.findViewById(R.id.layoutOrderItems);
                btnReorder       = v.findViewById(R.id.btnReorder);
            }
        }
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private String formatDate(String isoDate) {
        try {
            Date date = new SimpleDateFormat(
                    "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()).parse(isoDate);
            return date != null ? new SimpleDateFormat(
                    "dd MMM yyyy, hh:mm a", Locale.getDefault()).format(date) : isoDate;
        } catch (ParseException e) { return isoDate; }
    }

    private String getPaymentLabel(String method) {
        switch (method) {
            case "card":       return "Credit / Debit Card";
            case "cod":        return "Cash on Delivery";
            case "netbanking": return "Net Banking";
            case "emi":        return "EMI";
            default:           return "UPI";
        }
    }

    private long toLong(Object val) {
        if (val instanceof Long)    return (Long) val;
        if (val instanceof Integer) return ((Integer) val).longValue();
        if (val instanceof Double)  return ((Double) val).longValue();
        return 0L;
    }
}
