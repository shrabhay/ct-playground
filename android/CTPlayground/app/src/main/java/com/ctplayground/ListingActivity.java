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
import com.ctplayground.utils.FirebaseHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ListingActivity extends AppCompatActivity {

    private String category;
    private String currentSort = "relevance";
    private List<Product> allProducts = new ArrayList<>();
    private ProductAdapter adapter;
    private TextView tvResultCount, tvCategoryTitle;
    private String currentUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listing);

        category = getIntent().getStringExtra("category");
        if (category == null) category = "All";

        tvCategoryTitle = findViewById(R.id.tvCategoryTitle);
        tvResultCount   = findViewById(R.id.tvResultCount);

        if ("Deals".equals(category)) tvCategoryTitle.setText("Today's Deals 🔥");
        else                           tvCategoryTitle.setText(category);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) currentUid = user.getUid();

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

    @Override
    protected void onResume() {
        super.onResume();
        loadWishlistState();
        loadCartState();
    }

    private void loadWishlistState() {
        if (currentUid == null) return;
        FirebaseHelper.getWishlistIds(currentUid, new FirebaseHelper.WishlistCallback() {
            @Override public void onSuccess(Set<String> ids) {
                adapter.updateWishlistState(ids);
            }
            @Override public void onFailure(String error) {}
        });
    }

    private void loadCartState() {
        if (currentUid == null) return;
        FirebaseHelper.getCartState(currentUid, new FirebaseHelper.CartStateCallback() {
            @Override public void onSuccess(Map<String, Integer> state) {
                adapter.updateCartState(state);
            }
            @Override public void onFailure(String error) {}
        });
    }

    private void setupRecyclerView() {
        RecyclerView rv = findViewById(R.id.rvProducts);
        rv.setLayoutManager(new GridLayoutManager(this, 2));
        adapter = new ProductAdapter(new ArrayList<>(),
                new ProductAdapter.OnProductClickListener() {

                    @Override
                    public void onProductClick(Product product) {
                        Intent intent = new Intent(ListingActivity.this, ProductActivity.class);
                        intent.putExtra("product_id", product.id);
                        startActivity(intent);
                    }

                    @Override
                    public void onAddToCartClick(Product product) {
                        fireCTCartEvent(product, "Added to Cart", 1);
                        if (currentUid == null) return;
                        FirebaseHelper.addToCart(currentUid, product,
                                new FirebaseHelper.ProfileCallback() {
                                    @Override public void onSuccess(Map<String, Object> p) {}
                                    @Override public void onFailure(String e) {}
                                });
                    }

                    @Override
                    public void onCartQtyChange(Product product, int newQty) {
                        String event = newQty == 0 ? "Removed from Cart" : "Cart Quantity Updated";
                        fireCTCartEvent(product, event, newQty);
                        if (currentUid == null) return;
                        FirebaseHelper.updateCartQty(currentUid, product.id, newQty,
                                new FirebaseHelper.ProfileCallback() {
                                    @Override public void onSuccess(Map<String, Object> p) {}
                                    @Override public void onFailure(String e) {}
                                });
                    }

                    @Override
                    public void onWishlistClick(Product product, boolean isCurrentlyWishlisted,
                                                int position) {
                        CleverTapAPI ct = CleverTapAPI.getDefaultInstance(ListingActivity.this);
                        if (ct != null) {
                            HashMap<String, Object> props = new HashMap<>();
                            props.put("product_id",   product.id);
                            props.put("product_name", product.name);
                            props.put("category",     product.category);
                            props.put("price",        product.price);
                            props.put("source",       "listing_page");
                            props.put("vertical",     "bazario");
                            ct.pushEvent(isCurrentlyWishlisted
                                    ? "Removed from Wishlist" : "Added to Wishlist", props);
                        }
                        if (currentUid == null) return;
                        if (isCurrentlyWishlisted) {
                            FirebaseHelper.removeFromWishlist(currentUid, product.id,
                                    new FirebaseHelper.ProfileCallback() {
                                        @Override public void onSuccess(Map<String, Object> p) {}
                                        @Override public void onFailure(String e) {}
                                    });
                        } else {
                            FirebaseHelper.addToWishlist(currentUid, product,
                                    new FirebaseHelper.ProfileCallback() {
                                        @Override public void onSuccess(Map<String, Object> p) {}
                                        @Override public void onFailure(String e) {}
                                    });
                        }
                    }
                });
        rv.setAdapter(adapter);
    }

    private void fireCTCartEvent(Product product, String eventName, int qty) {
        CleverTapAPI ct = CleverTapAPI.getDefaultInstance(this);
        if (ct != null) {
            HashMap<String, Object> props = new HashMap<>();
            props.put("product_id",   product.id);
            props.put("product_name", product.name);
            props.put("category",     product.category);
            props.put("price",        product.price);
            props.put("quantity",     qty);
            props.put("source",       "listing_page");
            props.put("vertical",     "bazario");
            ct.pushEvent(eventName, props);
        }
    }

    private void loadProducts() {
        if ("Deals".equals(category))    allProducts = ProductCatalogue.getDeals();
        else if ("All".equals(category)) allProducts = ProductCatalogue.getAll();
        else                             allProducts = ProductCatalogue.getByCategory(category);
        applySort();
    }

    private void applySort() {
        List<Product> sorted = new ArrayList<>(allProducts);
        switch (currentSort) {
            case "price_low":  sorted.sort(Comparator.comparingInt(p -> p.price)); break;
            case "price_high": sorted.sort((a, b) -> b.price - a.price); break;
            case "discount":   sorted.sort((a, b) -> b.discount - a.discount); break;
            case "rating":     sorted.sort((a, b) -> Double.compare(b.rating, a.rating)); break;
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
                if (q.isEmpty()) loadProducts();
                else {
                    allProducts = ProductCatalogue.search(q);
                    applySort();
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
            props.put("source", getIntent().getStringExtra("source") != null
                    ? getIntent().getStringExtra("source") : "direct");
            props.put("vertical", "bazario");
            ct.pushEvent("Category Viewed", props);
        }
    }
}
