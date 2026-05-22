package com.ctplayground;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.clevertap.android.sdk.CleverTapAPI;
import com.ctplayground.adapters.WishlistAdapter;
import com.ctplayground.data.Product;
import com.ctplayground.data.ProductCatalogue;
import com.ctplayground.utils.FirebaseHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WishlistActivity extends AppCompatActivity {

    private List<Map<String, Object>> wishlistItems = new ArrayList<>();
    private WishlistAdapter adapter;
    private String currentUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlist);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) currentUid = user.getUid();

        setupNavbar();
        setupRecyclerView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadWishlist();
    }

    private void setupNavbar() {
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
        findViewById(R.id.btnCart).setOnClickListener(v ->
                startActivity(new Intent(this, CartActivity.class)));
    }

    private void setupRecyclerView() {
        RecyclerView rv = findViewById(R.id.rvWishlist);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new WishlistAdapter(wishlistItems,
                new WishlistAdapter.WishlistItemListener() {

                    @Override
                    public void onItemClick(Map<String, Object> item) {
                        String productId = (String) item.get("id");
                        Intent intent = new Intent(WishlistActivity.this, ProductActivity.class);
                        intent.putExtra("product_id", productId);
                        startActivity(intent);
                    }

                    @Override
                    public void onMoveToCart(Map<String, Object> item) {
                        String productId = (String) item.get("id");
                        Product product  = ProductCatalogue.getById(productId);
                        if (product == null) return;

                        // Add to cart in Firestore
                        if (currentUid != null) {
                            FirebaseHelper.addToCart(currentUid, product,
                                    new FirebaseHelper.ProfileCallback() {
                                        @Override public void onSuccess(Map<String, Object> p) {
                                            // Remove from wishlist
                                            removeItem(item);
                                            Toast.makeText(WishlistActivity.this,
                                                    product.name + " moved to cart!",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                        @Override public void onFailure(String e) {
                                            Toast.makeText(WishlistActivity.this,
                                                    "Failed to add to cart", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }

                        // CT event
                        CleverTapAPI ct = CleverTapAPI.getDefaultInstance(WishlistActivity.this);
                        if (ct != null) {
                            HashMap<String, Object> props = new HashMap<>();
                            props.put("product_id",   product.id);
                            props.put("product_name", product.name);
                            props.put("category",     product.category);
                            props.put("price",        product.price);
                            props.put("source",       "wishlist_page");
                            props.put("vertical",     "bazario");
                            ct.pushEvent("Moved to Cart from Wishlist", props);
                        }
                    }

                    @Override
                    public void onRemove(Map<String, Object> item) {
                        removeItem(item);

                        // CT event
                        CleverTapAPI ct = CleverTapAPI.getDefaultInstance(WishlistActivity.this);
                        if (ct != null) {
                            HashMap<String, Object> props = new HashMap<>();
                            props.put("product_id",   item.get("id"));
                            props.put("product_name", item.get("name"));
                            props.put("source",       "wishlist_page");
                            props.put("vertical",     "bazario");
                            ct.pushEvent("Removed from Wishlist", props);
                        }
                    }
                });
        rv.setAdapter(adapter);
    }

    private void loadWishlist() {
        if (currentUid == null) { showEmptyOrContent(); return; }
        loadWishlistItems();
    }

    private void loadWishlistItems() {
        if (currentUid == null) return;
        // Read full items array from Firestore
        com.google.firebase.firestore.FirebaseFirestore.getInstance()
                .collection("wishlists").document(currentUid)
                .get()
                .addOnSuccessListener(doc -> {
                    wishlistItems.clear();
                    if (doc.exists() && doc.get("items") != null) {
                        List<Map<String, Object>> items =
                                (List<Map<String, Object>>) doc.get("items");
                        if (items != null) wishlistItems.addAll(items);
                    }
                    adapter.updateItems(wishlistItems);
                    showEmptyOrContent();
                    fireCTWishlistViewed();
                })
                .addOnFailureListener(e -> showEmptyOrContent());
    }

    private void removeItem(Map<String, Object> item) {
        String productId = (String) item.get("id");
        wishlistItems.remove(item);
        adapter.updateItems(wishlistItems);
        updateTitle();        // ← add this
        showEmptyOrContent();

        if (currentUid != null) {
            FirebaseHelper.removeFromWishlist(currentUid, productId,
                    new FirebaseHelper.ProfileCallback() {
                        @Override public void onSuccess(Map<String, Object> p) {}
                        @Override public void onFailure(String e) {}
                    });
        }
    }

    private void updateTitle() {
        int count = wishlistItems.size();
        ((TextView) findViewById(R.id.tvWishlistTitle)).setText(
                "My Wishlist" + (count > 0 ? " (" + count + ")" : ""));
        if (count > 0) {
            ((TextView) findViewById(R.id.tvItemCount)).setText(
                    count + " item" + (count > 1 ? "s" : "") + " saved");
        }
    }

    private void showEmptyOrContent() {
        LinearLayout layoutEmpty   = findViewById(R.id.layoutEmpty);
        LinearLayout layoutContent = findViewById(R.id.layoutContent);

        if (wishlistItems.isEmpty()) {
            layoutEmpty.setVisibility(View.VISIBLE);
            layoutContent.setVisibility(View.GONE);
            findViewById(R.id.btnExploreCatalogue).setOnClickListener(v -> {
                startActivity(new Intent(this, BazarioHomeActivity.class));
                finish();
            });
        } else {
            layoutEmpty.setVisibility(View.GONE);
            layoutContent.setVisibility(View.VISIBLE);
            int count = wishlistItems.size();
            ((TextView) findViewById(R.id.tvWishlistTitle)).setText(
                    "My Wishlist (" + count + ")");
            ((TextView) findViewById(R.id.tvItemCount)).setText(
                    count + " item" + (count > 1 ? "s" : "") + " saved");
        }
    }

    private void fireCTWishlistViewed() {
        CleverTapAPI ct = CleverTapAPI.getDefaultInstance(this);
        if (ct != null) {
            HashMap<String, Object> props = new HashMap<>();
            props.put("item_count", wishlistItems.size());
            props.put("vertical",   "bazario");
            ct.pushEvent("Wishlist Viewed", props);
        }
    }
}
