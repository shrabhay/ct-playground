package com.ctplayground;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.google.firebase.auth.FirebaseAuth;

public class VerticalSelectorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vertical_selector);

        CardView cardBazario      = findViewById(R.id.cardBazario);
        CardView cardFintech      = findViewById(R.id.cardFintech);
        CardView cardSubscription = findViewById(R.id.cardSubscription);
        CardView cardFoodtech     = findViewById(R.id.cardFoodtech);

        cardBazario.setOnClickListener(v -> {
            if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                // Already logged in — go straight to Bazario home
                startActivity(new Intent(this, BazarioHomeActivity.class));
            } else {
                // Not logged in — go to login first
                startActivity(new Intent(this, LoginActivity.class));
            }
        });

        cardFintech.setOnClickListener(v ->
                Toast.makeText(this, "Fintech — Coming Soon", Toast.LENGTH_SHORT).show());
        cardSubscription.setOnClickListener(v ->
                Toast.makeText(this, "Subscription — Coming Soon", Toast.LENGTH_SHORT).show());
        cardFoodtech.setOnClickListener(v ->
                Toast.makeText(this, "Foodtech — Coming Soon", Toast.LENGTH_SHORT).show());
    }
}
