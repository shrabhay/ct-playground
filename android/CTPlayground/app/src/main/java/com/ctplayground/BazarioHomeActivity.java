package com.ctplayground;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.clevertap.android.sdk.CleverTapAPI;
import com.ctplayground.adapters.CategoryAdapter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class BazarioHomeActivity extends AppCompatActivity {

    // Same 8 categories as the web catalogue.js
    private final List<String[]> CATEGORIES = Arrays.asList(
            new String[]{"📱", "Electronics"},
            new String[]{"👗", "Fashion"},
            new String[]{"🏠", "Home & Kitchen"},
            new String[]{"📚", "Books"},
            new String[]{"⚽", "Sports"},
            new String[]{"💄", "Beauty"},
            new String[]{"🧸", "Toys"},
            new String[]{"🛒", "Groceries"}
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bazario_home);

        setupCategoryGrid();
        setupNavbar();
        fireCTEvents();
    }

    private void setupCategoryGrid() {
        RecyclerView rvCategories = findViewById(R.id.rvCategories);
        rvCategories.setLayoutManager(new GridLayoutManager(this, 4));
        rvCategories.setAdapter(new CategoryAdapter(CATEGORIES, this::onCategoryClick));
    }

    private void setupNavbar() {
        findViewById(R.id.btnAccount).setOnClickListener(v ->
                startActivity(new Intent(this, AccountActivity.class)));

        findViewById(R.id.btnCart).setOnClickListener(v ->
                startActivity(new Intent(this, CartActivity.class)));

        findViewById(R.id.btnWishlist).setOnClickListener(v ->
                startActivity(new Intent(this, WishlistActivity.class)));

        findViewById(R.id.btnShopNow).setOnClickListener(v -> {
            CleverTapAPI ct = CleverTapAPI.getDefaultInstance(this);
            if (ct != null) {
                HashMap<String, Object> props = new HashMap<>();
                props.put("banner", "mega_sale");
                props.put("vertical", "bazario");
                ct.pushEvent("Banner Clicked", props);
            }
            Intent intent = new Intent(this, ListingActivity.class);
            intent.putExtra("category", "Deals");
            startActivity(intent);
        });

        findViewById(R.id.etSearch).setOnClickListener(v ->
                startActivity(new Intent(this, ListingActivity.class)));
    }

    private void onCategoryClick(String categoryName) {
        // Fire CT event
        CleverTapAPI ct = CleverTapAPI.getDefaultInstance(this);
        if (ct != null) {
            HashMap<String, Object> props = new HashMap<>();
            props.put("category", categoryName);
            props.put("source",   "home_grid");
            props.put("vertical", "bazario");
            ct.pushEvent("Category Clicked", props);
        }

        // Navigate to listing
        Intent intent = new Intent(this, ListingActivity.class);
        intent.putExtra("category", categoryName);
        startActivity(intent);
    }

    private void fireCTEvents() {
        CleverTapAPI ct = CleverTapAPI.getDefaultInstance(this);
        if (ct != null) {
            HashMap<String, Object> props = new HashMap<>();
            props.put("vertical", "bazario");
            ct.pushEvent("Home Page Viewed", props);
        }
    }
}
