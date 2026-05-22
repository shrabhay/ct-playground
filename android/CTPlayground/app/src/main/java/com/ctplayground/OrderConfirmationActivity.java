package com.ctplayground;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class OrderConfirmationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirmation);

        String orderId       = getIntent().getStringExtra("order_id");
        long   total         = getIntent().getLongExtra("order_total", 0);
        String paymentMethod = getIntent().getStringExtra("payment_method");

        bindViews(orderId, total, paymentMethod);
        setupButtons();
    }

    private void bindViews(String orderId, long total, String paymentMethod) {
        ((TextView) findViewById(R.id.tvOrderId)).setText(orderId);
        ((TextView) findViewById(R.id.tvOrderTotal)).setText(
                "₹" + String.format("%,d", total));
        ((TextView) findViewById(R.id.tvOrderPayment)).setText(
                getPaymentLabel(paymentMethod));
        ((TextView) findViewById(R.id.tvDeliveryDate)).setText(
                getDeliveryDateRange());
    }

    private void setupButtons() {
        ((Button) findViewById(R.id.btnContinueShopping)).setOnClickListener(v -> {
            // Navigate back to Bazario home, clearing the back stack
            Intent intent = new Intent(this, BazarioHomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });

        ((Button) findViewById(R.id.btnViewOrders)).setOnClickListener(v -> {
            startActivity(new Intent(this, AccountActivity.class));
        });
    }

    private String getPaymentLabel(String method) {
        if (method == null) return "UPI";
        switch (method) {
            case "card":       return "Credit / Debit Card";
            case "cod":        return "Cash on Delivery";
            case "netbanking": return "Net Banking";
            case "emi":        return "EMI";
            default:           return "UPI";
        }
    }

    private String getDeliveryDateRange() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, 3);
        String from = new SimpleDateFormat("EEE, dd MMM", Locale.getDefault())
                .format(cal.getTime());
        cal.add(Calendar.DAY_OF_MONTH, 2);
        String to = new SimpleDateFormat("EEE, dd MMM", Locale.getDefault())
                .format(cal.getTime());
        return from + " – " + to;
    }
}
