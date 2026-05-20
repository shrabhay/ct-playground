package com.ctplayground;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.clevertap.android.sdk.CleverTapAPI;
import com.ctplayground.data.Product;
import com.ctplayground.data.ProductCatalogue;
import com.ctplayground.utils.FirebaseHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ProductActivity extends AppCompatActivity {

    private Product product;
    private boolean isWishlisted = false;
    private int cartQty = 0;
    private String currentUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        String productId = getIntent().getStringExtra("product_id");
        product = ProductCatalogue.getById(productId);
        if (product == null) { finish(); return; }

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) currentUid = user.getUid();

        bindViews();
        setupButtons();
        fireCTEvents();
        loadWishlistState();
        loadCartState();
    }

    private void loadWishlistState() {
        if (currentUid == null) return;
        FirebaseHelper.getWishlistIds(currentUid, new FirebaseHelper.WishlistCallback() {
            @Override public void onSuccess(Set<String> ids) {
                isWishlisted = ids.contains(product.id);
                updateWishlistButton();
            }
            @Override public void onFailure(String error) {}
        });
    }

    private void loadCartState() {
        if (currentUid == null) return;
        FirebaseHelper.getCartState(currentUid, new FirebaseHelper.CartStateCallback() {
            @Override public void onSuccess(Map<String, Integer> state) {
                cartQty = state.containsKey(product.id) ? state.get(product.id) : 0;
                updateCartUI();
            }
            @Override public void onFailure(String error) {}
        });
    }

    private void bindViews() {
        ((TextView) findViewById(R.id.tvEmoji)).setText(product.emoji);
        ((TextView) findViewById(R.id.tvDiscountBadge)).setText(product.discount + "% off");
        ((TextView) findViewById(R.id.tvName)).setText(product.name);
        ((TextView) findViewById(R.id.tvBrand)).setText("by " + product.brand);
        ((TextView) findViewById(R.id.tvRating)).setText(product.getFormattedRating());
        ((TextView) findViewById(R.id.tvReviews)).setText(
                "(" + product.getFormattedReviews() + " ratings)");
        ((TextView) findViewById(R.id.tvPrice)).setText(product.getFormattedPrice());

        TextView tvOriginal = findViewById(R.id.tvOriginalPrice);
        tvOriginal.setText(product.getFormattedOriginalPrice());
        tvOriginal.setPaintFlags(
                tvOriginal.getPaintFlags() | android.graphics.Paint.STRIKE_THRU_TEXT_FLAG);

        ((TextView) findViewById(R.id.tvDiscount)).setText(
                product.discount + "% off  ·  Save " + product.getFormattedSaving());
        ((TextView) findViewById(R.id.tvDelivery)).setText(
                "Free delivery by Tomorrow · Bazario Prime");

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < product.tags.length; i++) {
            sb.append("• ").append(product.tags[i]);
            if (i < product.tags.length - 1) sb.append("\n");
        }
        ((TextView) findViewById(R.id.tvHighlights)).setText(sb.toString());
        ((TextView) findViewById(R.id.tvDescription)).setText(product.desc);
    }

    private void setupButtons() {
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
        findViewById(R.id.btnCartNav).setOnClickListener(v ->
                startActivity(new Intent(this, CartActivity.class)));
        findViewById(R.id.btnAccountNav).setOnClickListener(v ->
                startActivity(new Intent(this, AccountActivity.class)));
        findViewById(R.id.btnWishlistNav).setOnClickListener(v ->
                startActivity(new Intent(this, WishlistActivity.class)));

        // Add to cart buttons
        findViewById(R.id.btnAddToCart).setOnClickListener(v -> handleAddToCart());
        findViewById(R.id.btnAddToCartSticky).setOnClickListener(v -> handleAddToCart());

        // Buy now
        findViewById(R.id.btnBuyNow).setOnClickListener(v -> handleBuyNow());
        findViewById(R.id.btnBuyNowSticky).setOnClickListener(v -> handleBuyNow());

        // Qty controls — product area
        findViewById(R.id.btnIncreaseProduct).setOnClickListener(v -> handleQtyChange(1));
        findViewById(R.id.btnDecreaseProduct).setOnClickListener(v -> handleQtyChange(-1));

        // Qty controls — sticky bar
        findViewById(R.id.btnIncreaseSticky).setOnClickListener(v -> handleQtyChange(1));
        findViewById(R.id.btnDecreaseSticky).setOnClickListener(v -> handleQtyChange(-1));

        // Wishlist
        findViewById(R.id.btnWishlist).setOnClickListener(v -> handleWishlist());
    }

    private void handleAddToCart() {
        cartQty = 1;
        updateCartUI();
        fireCTCartEvent("Added to Cart", 1);
        if (currentUid == null) return;
        FirebaseHelper.addToCart(currentUid, product,
                new FirebaseHelper.ProfileCallback() {
                    @Override public void onSuccess(Map<String, Object> p) {}
                    @Override public void onFailure(String e) {}
                });
    }

    private void handleQtyChange(int delta) {
        cartQty += delta;
        if (cartQty < 0) cartQty = 0;
        updateCartUI();

        String event = cartQty == 0 ? "Removed from Cart" : "Cart Quantity Updated";
        fireCTCartEvent(event, cartQty);

        if (currentUid == null) return;
        FirebaseHelper.updateCartQty(currentUid, product.id, cartQty,
                new FirebaseHelper.ProfileCallback() {
                    @Override public void onSuccess(Map<String, Object> p) {}
                    @Override public void onFailure(String e) {}
                });
    }

    private void updateCartUI() {
        Button btnAddToCart   = findViewById(R.id.btnAddToCart);
        Button btnAddToCartSticky = findViewById(R.id.btnAddToCartSticky);
        LinearLayout layoutQty       = findViewById(R.id.layoutQtyProduct);
        LinearLayout layoutQtySticky = findViewById(R.id.layoutQtySticky);
        TextView tvQty       = findViewById(R.id.tvQtyProduct);
        TextView tvQtySticky = findViewById(R.id.tvQtySticky);

        if (cartQty > 0) {
            btnAddToCart.setVisibility(android.view.View.GONE);
            btnAddToCartSticky.setVisibility(android.view.View.GONE);
            layoutQty.setVisibility(android.view.View.VISIBLE);
            layoutQtySticky.setVisibility(android.view.View.VISIBLE);
            tvQty.setText(String.valueOf(cartQty));
            tvQtySticky.setText(String.valueOf(cartQty));
        } else {
            btnAddToCart.setVisibility(android.view.View.VISIBLE);
            btnAddToCartSticky.setVisibility(android.view.View.VISIBLE);
            layoutQty.setVisibility(android.view.View.GONE);
            layoutQtySticky.setVisibility(android.view.View.GONE);
        }
    }

    private void handleBuyNow() {
        if (cartQty == 0) handleAddToCart();
        fireCTCartEvent("Buy Now Clicked", cartQty);
        startActivity(new Intent(this, CartActivity.class));
    }

    private void handleWishlist() {
        isWishlisted = !isWishlisted;
        updateWishlistButton();

        CleverTapAPI ct = CleverTapAPI.getDefaultInstance(this);
        if (ct != null) {
            HashMap<String, Object> props = new HashMap<>();
            props.put("product_id",   product.id);
            props.put("product_name", product.name);
            props.put("category",     product.category);
            props.put("price",        product.price);
            props.put("source",       "product_page");
            props.put("vertical",     "bazario");
            ct.pushEvent(isWishlisted ? "Added to Wishlist" : "Removed from Wishlist", props);
        }

        if (currentUid == null) return;
        if (isWishlisted) {
            FirebaseHelper.addToWishlist(currentUid, product,
                    new FirebaseHelper.ProfileCallback() {
                        @Override public void onSuccess(Map<String, Object> p) {}
                        @Override public void onFailure(String e) {}
                    });
        } else {
            FirebaseHelper.removeFromWishlist(currentUid, product.id,
                    new FirebaseHelper.ProfileCallback() {
                        @Override public void onSuccess(Map<String, Object> p) {}
                        @Override public void onFailure(String e) {}
                    });
        }
    }

    private void updateWishlistButton() {
        Button btn = findViewById(R.id.btnWishlist);
        btn.setText(isWishlisted ? "♥  Wishlisted" : "♡  Add to Wishlist");
        btn.setTextColor(getColor(
                isWishlisted ? R.color.bazario_orange : R.color.bazario_dark));
    }

    private void fireCTCartEvent(String eventName, int qty) {
        CleverTapAPI ct = CleverTapAPI.getDefaultInstance(this);
        if (ct != null) {
            HashMap<String, Object> props = new HashMap<>();
            props.put("product_id",   product.id);
            props.put("product_name", product.name);
            props.put("category",     product.category);
            props.put("brand",        product.brand);
            props.put("price",        product.price);
            props.put("quantity",     qty);
            props.put("source",       "product_page");
            props.put("vertical",     "bazario");
            ct.pushEvent(eventName, props);
        }
    }

    private void fireCTEvents() {
        CleverTapAPI ct = CleverTapAPI.getDefaultInstance(this);
        if (ct != null) {
            HashMap<String, Object> props = new HashMap<>();
            props.put("product_id",   product.id);
            props.put("product_name", product.name);
            props.put("category",     product.category);
            props.put("brand",        product.brand);
            props.put("price",        product.price);
            props.put("discount",     product.discount);
            props.put("rating",       product.rating);
            props.put("source", getIntent().getStringExtra("source") != null
                    ? getIntent().getStringExtra("source") : "listing_page");
            props.put("vertical", "bazario");
            ct.pushEvent("Product Viewed", props);
        }
    }
}
