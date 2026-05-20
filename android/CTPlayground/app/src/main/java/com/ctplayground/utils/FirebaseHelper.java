package com.ctplayground.utils;

import android.util.Log;
import com.ctplayground.data.Product;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FirebaseHelper {

    private static final FirebaseFirestore db = FirebaseFirestore.getInstance();

    // ── Callbacks ─────────────────────────────────────────────────────────────

    public interface ProfileCallback {
        void onSuccess(Map<String, Object> profile);
        void onFailure(String error);
    }

    public interface WishlistCallback {
        void onSuccess(Set<String> wishlistedIds);
        void onFailure(String error);
    }

    public interface CartStateCallback {
        void onSuccess(Map<String, Integer> cartState); // productId → qty
        void onFailure(String error);
    }

    // ── User Profile ──────────────────────────────────────────────────────────

    public static void createUserProfile(String uid, String name, String email,
                                         String phone, String gender, Date dob,
                                         String city, ProfileCallback callback) {
        Map<String, Object> profile = new HashMap<>();
        profile.put("name",              name);
        profile.put("email",             email);
        profile.put("phone",             phone);
        profile.put("gender",            gender);
        profile.put("dob",               dob);
        profile.put("city",              city);
        profile.put("joinedAt",          new Date());
        profile.put("isPrime",           false);
        profile.put("totalOrders",       0);
        profile.put("lifetimeValue",     0);
        profile.put("preferredCategory", "");
        profile.put("vertical",          "Bazario");

        db.collection("users").document(uid)
                .set(profile)
                .addOnSuccessListener(unused -> callback.onSuccess(profile))
                .addOnFailureListener(e -> callback.onFailure(e.getMessage()));
    }

    public static void getUserProfile(String uid, ProfileCallback callback) {
        db.collection("users").document(uid)
                .get()
                .addOnSuccessListener((DocumentSnapshot doc) -> {
                    if (doc.exists()) callback.onSuccess(doc.getData());
                    else              callback.onSuccess(new HashMap<>());
                })
                .addOnFailureListener(e -> callback.onFailure(e.getMessage()));
    }

    // ── Wishlist ──────────────────────────────────────────────────────────────

    public static void getWishlistIds(String uid, WishlistCallback callback) {
        db.collection("wishlists").document(uid)
                .get()
                .addOnSuccessListener(doc -> {
                    Set<String> ids = new HashSet<>();
                    if (doc.exists()) {
                        List<Map<String, Object>> items =
                                (List<Map<String, Object>>) doc.get("items");
                        if (items != null) {
                            for (Map<String, Object> item : items) {
                                if (item.containsKey("id")) ids.add((String) item.get("id"));
                            }
                        }
                    }
                    callback.onSuccess(ids);
                })
                .addOnFailureListener(e -> callback.onFailure(e.getMessage()));
    }

    public static void addToWishlist(String uid, Product product, ProfileCallback callback) {
        DocumentReference ref = db.collection("wishlists").document(uid);
        ref.get().addOnSuccessListener(doc -> {
            List<Map<String, Object>> items = new ArrayList<>();
            if (doc.exists() && doc.get("items") != null) {
                items = new ArrayList<>((List<Map<String, Object>>) doc.get("items"));
                for (Map<String, Object> item : items) {
                    if (product.id.equals(item.get("id"))) {
                        callback.onSuccess(new HashMap<>());
                        return;
                    }
                }
            }
            Map<String, Object> newItem = new HashMap<>();
            newItem.put("id",            product.id);
            newItem.put("name",          product.name);
            newItem.put("brand",         product.brand);
            newItem.put("category",      product.category);
            newItem.put("price",         product.price);
            newItem.put("originalPrice", product.originalPrice);
            newItem.put("discount",      product.discount);
            newItem.put("rating",        product.rating);
            newItem.put("emoji",         product.emoji);
            newItem.put("addedAt",       new Date());
            items.add(newItem);

            Map<String, Object> data = new HashMap<>();
            data.put("items", items);
            ref.set(data)
                    .addOnSuccessListener(unused -> callback.onSuccess(newItem))
                    .addOnFailureListener(e -> callback.onFailure(e.getMessage()));
        }).addOnFailureListener(e -> callback.onFailure(e.getMessage()));
    }

    public static void removeFromWishlist(String uid, String productId,
                                          ProfileCallback callback) {
        DocumentReference ref = db.collection("wishlists").document(uid);
        ref.get().addOnSuccessListener(doc -> {
            if (!doc.exists()) { callback.onSuccess(new HashMap<>()); return; }
            List<Map<String, Object>> items =
                    (List<Map<String, Object>>) doc.get("items");
            if (items == null) { callback.onSuccess(new HashMap<>()); return; }

            List<Map<String, Object>> filtered = new ArrayList<>();
            for (Map<String, Object> item : items) {
                if (!productId.equals(item.get("id"))) filtered.add(item);
            }
            Map<String, Object> data = new HashMap<>();
            data.put("items", filtered);
            ref.set(data)
                    .addOnSuccessListener(unused -> callback.onSuccess(new HashMap<>()))
                    .addOnFailureListener(e -> callback.onFailure(e.getMessage()));
        }).addOnFailureListener(e -> callback.onFailure(e.getMessage()));
    }

    // ── Cart ──────────────────────────────────────────────────────────────────

    public static void getCartState(String uid, CartStateCallback callback) {
        db.collection("carts").document(uid)
                .get()
                .addOnSuccessListener(doc -> {
                    Map<String, Integer> state = new HashMap<>();
                    if (doc.exists() && doc.get("items") != null) {
                        List<Map<String, Object>> items =
                                (List<Map<String, Object>>) doc.get("items");
                        for (Map<String, Object> item : items) {
                            String id = (String) item.get("id");
                            Object qtyObj = item.get("qty");
                            int qty = 1;
                            if (qtyObj instanceof Long)    qty = ((Long) qtyObj).intValue();
                            else if (qtyObj instanceof Integer) qty = (Integer) qtyObj;
                            if (id != null) state.put(id, qty);
                        }
                    }
                    callback.onSuccess(state);
                })
                .addOnFailureListener(e -> callback.onFailure(e.getMessage()));
    }

    public static void addToCart(String uid, Product product, ProfileCallback callback) {
        DocumentReference ref = db.collection("carts").document(uid);
        ref.get().addOnSuccessListener(doc -> {
            List<Map<String, Object>> items = new ArrayList<>();
            if (doc.exists() && doc.get("items") != null) {
                items = new ArrayList<>((List<Map<String, Object>>) doc.get("items"));
                for (Map<String, Object> item : items) {
                    if (product.id.equals(item.get("id"))) {
                        // Already in cart — increment qty
                        Object qtyObj = item.get("qty");
                        int qty = 1;
                        if (qtyObj instanceof Long)    qty = ((Long) qtyObj).intValue();
                        else if (qtyObj instanceof Integer) qty = (Integer) qtyObj;
                        item.put("qty", qty + 1);
                        Map<String, Object> data = new HashMap<>();
                        data.put("items", items);
                        ref.set(data)
                                .addOnSuccessListener(unused -> callback.onSuccess(item))
                                .addOnFailureListener(e -> callback.onFailure(e.getMessage()));
                        return;
                    }
                }
            }
            // New item
            Map<String, Object> newItem = new HashMap<>();
            newItem.put("id",            product.id);
            newItem.put("name",          product.name);
            newItem.put("brand",         product.brand);
            newItem.put("category",      product.category);
            newItem.put("price",         product.price);
            newItem.put("originalPrice", product.originalPrice);
            newItem.put("discount",      product.discount);
            newItem.put("rating",        product.rating);
            newItem.put("emoji",         product.emoji);
            newItem.put("qty",           1);
            newItem.put("variant",       "");
            items.add(newItem);

            Map<String, Object> data = new HashMap<>();
            data.put("items", items);
            ref.set(data)
                    .addOnSuccessListener(unused -> callback.onSuccess(newItem))
                    .addOnFailureListener(e -> {
                        Log.e("FirebaseHelper", "Cart write FAILED: " + e.getMessage());
                        callback.onFailure(e.getMessage());
                    });
        }).addOnFailureListener(e -> {
            Log.e("FirebaseHelper", "Cart read FAILED: " + e.getMessage());
            callback.onFailure(e.getMessage());
        });
    }

    public static void updateCartQty(String uid, String productId, int newQty,
                                     ProfileCallback callback) {
        DocumentReference ref = db.collection("carts").document(uid);
        ref.get().addOnSuccessListener(doc -> {
            if (!doc.exists()) { callback.onSuccess(new HashMap<>()); return; }
            List<Map<String, Object>> items =
                    (List<Map<String, Object>>) doc.get("items");
            if (items == null) { callback.onSuccess(new HashMap<>()); return; }

            List<Map<String, Object>> updated = new ArrayList<>();
            for (Map<String, Object> item : items) {
                if (productId.equals(item.get("id"))) {
                    if (newQty > 0) {
                        item.put("qty", newQty);
                        updated.add(item);
                    }
                    // qty == 0: skip (removes item)
                } else {
                    updated.add(item);
                }
            }
            Map<String, Object> data = new HashMap<>();
            data.put("items", updated);
            ref.set(data)
                    .addOnSuccessListener(unused -> callback.onSuccess(new HashMap<>()))
                    .addOnFailureListener(e -> callback.onFailure(e.getMessage()));
        }).addOnFailureListener(e -> callback.onFailure(e.getMessage()));
    }
}
