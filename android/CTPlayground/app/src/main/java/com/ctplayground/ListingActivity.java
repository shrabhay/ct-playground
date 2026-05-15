package com.ctplayground;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.clevertap.android.sdk.CleverTapAPI;
import com.ctplayground.adapters.ProductAdapter;
import com.ctplayground.data.Product;
import com.ctplayground.data.ProductCatalogue;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class ListingActivity extends AppCompatActivity {

    private String category;
    private String currentSort = "relevance";
    private List<Product> allProducts = new ArrayList<>();
    private ProductAdapter adapter;
    private TextView tvResultCount, tvCategoryTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listing);

        category = getIntent().getStringExtra("category");
        if (category == null) category = "All";

        tvCategoryTitle  = findViewById(R.id.tvCategoryTitle);
        tvResultCount    = findViewById(R.id.tvResultCount);

        // Set title matching web breadcrumb
        if ("Deals".equals(category))  tvCategoryTitle.setText("Today's Deals 🔥");
        else                            tvCategoryTitle.setText(category);

        setupRecyclerView();
        setupSearch();
        setupSortButtons();
        loadProducts();
        fireCTEvents();

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
        findViewById(R.id.btnCart).setOnClickListener(v ->
                startActivity(new Intent(this, CartActivity.class)));
        findViewById(R.id.btnAccount).setOnClickListener(v ->
                startActivity(new Intent(this, AccountActivity.class)));
    }

    private void setupRecyclerView() {
        RecyclerView rv = findViewById(R.id.rvProducts);
        rv.setLayoutManager(new GridLayoutManager(this, 2));
        adapter = new ProductAdapter(new ArrayList<>(), new ProductAdapter.OnProductClickListener() {
            @Override
            public void onProductClick(Product product) {
                // Fire CT event + navigate to product screen
                CleverTapAPI ct = CleverTapAPI.getDefaultInstance(ListingActivity.this);
                if (ct != null) {
                    HashMap<String, Object> props = new HashMap<>();
                    props.put("product_id",   product.id);
                    props.put("product_name", product.name);
                    props.put("category",     product.category);
                    props.put("brand",        product.brand);
                    props.put("price",        product.price);
                    props.put("discount",     product.discount);
                    props.put("source",       "listing_page");
                    props.put("vertical",     "bazario");
                    ct.pushEvent("Product Viewed", props);
                }
                Intent intent = new Intent(ListingActivity.this, ProductActivity.class);
                intent.putExtra("product_id", product.id);
                startActivity(intent);
            }

            @Override
            public void onWishlistClick(Product product) {
                CleverTapAPI ct = CleverTapAPI.getDefaultInstance(ListingActivity.this);
                if (ct != null) {
                    HashMap<String, Object> props = new HashMap<>();
                    props.put("product_id",   product.id);
                    props.put("product_name", product.name);
                    props.put("category",     product.category);
                    props.put("price",        product.price);
                    props.put("source",       "listing_page");
                    props.put("vertical",     "bazario");
                    ct.pushEvent("Added to Wishlist", props);
                }
            }

            @Override
            public void onAddToCartClick(Product product) {
                // Will wire to Firestore cart in Step 6
                CleverTapAPI ct = CleverTapAPI.getDefaultInstance(ListingActivity.this);
                if (ct != null) {
                    HashMap<String, Object> props = new HashMap<>();
                    props.put("product_id",   product.id);
                    props.put("product_name", product.name);
                    props.put("category",     product.category);
                    props.put("price",        product.price);
                    props.put("source",       "listing_page");
                    props.put("vertical",     "bazario");
                    ct.pushEvent("Added to Cart", props);
                }
            }
        });
        rv.setAdapter(adapter);
    }

    private void loadProducts() {
        if ("Deals".equals(category))       allProducts = ProductCatalogue.getDeals();
        else if ("All".equals(category))    allProducts = ProductCatalogue.getAll();
        else                                allProducts = ProductCatalogue.getByCategory(category);
        applySort();
    }

    private void applySort() {
        List<Product> sorted = new ArrayList<>(allProducts);
        switch (currentSort) {
            case "price_low":   sorted.sort(Comparator.comparingInt(p -> p.price)); break;
            case "price_high":  sorted.sort((a, b) -> b.price - a.price); break;
            case "discount":    sorted.sort((a, b) -> b.discount - a.discount); break;
            case "rating":      sorted.sort((a, b) -> Double.compare(b.rating, a.rating)); break;
        }
        adapter.updateProducts(sorted);
        tvResultCount.setText(sorted.size() + " products");
    }

    private void setupSearch() {
        EditText etSearch = findViewById(R.id.etSearch);
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int st, int c, int a) {}
            @Override public void onTextChanged(CharSequence s, int st, int b, int c) {}
            @Override public void afterTextChanged(Editable s) {
                String q = s.toString().trim();
                if (q.isEmpty()) {
                    loadProducts();
                } else {
                    allProducts = ProductCatalogue.search(q);
                    applySort();
                    // Fire CT event
                    CleverTapAPI ct = CleverTapAPI.getDefaultInstance(ListingActivity.this);
                    if (ct != null) {
                        HashMap<String, Object> props = new HashMap<>();
                        props.put("query",         q);
                        props.put("results_count", allProducts.size());
                        props.put("vertical",      "bazario");
                        ct.pushEvent("Product Searched", props);
                    }
                }
            }
        });
    }

    private void setupSortButtons() {
        Button btnSortPrice    = findViewById(R.id.btnSortPrice);
        Button btnSortDiscount = findViewById(R.id.btnSortDiscount);

        btnSortPrice.setOnClickListener(v -> {
            currentSort = currentSort.equals("price_low") ? "price_high" : "price_low";
            btnSortPrice.setText(currentSort.equals("price_low") ? "Price ↑" : "Price ↓");
            applySort();
            fireSortEvent(currentSort);
        });

        btnSortDiscount.setOnClickListener(v -> {
            currentSort = "discount";
            applySort();
            fireSortEvent("discount");
        });
    }

    private void fireSortEvent(String sortType) {
        CleverTapAPI ct = CleverTapAPI.getDefaultInstance(this);
        if (ct != null) {
            HashMap<String, Object> props = new HashMap<>();
            props.put("sort_type", sortType);
            props.put("category",  category);
            props.put("vertical",  "bazario");
            ct.pushEvent("Sort Applied", props);
        }
    }

    private void fireCTEvents() {
        CleverTapAPI ct = CleverTapAPI.getDefaultInstance(this);
        if (ct != null) {
            HashMap<String, Object> props = new HashMap<>();
            props.put("category_name",  category);
            props.put("total_products", allProducts.size());
            props.put("source",         getIntent().getStringExtra("source") != null
                    ? getIntent().getStringExtra("source") : "direct");
            props.put("vertical",       "bazario");
            ct.pushEvent("Category Viewed", props);
        }
    }
}
