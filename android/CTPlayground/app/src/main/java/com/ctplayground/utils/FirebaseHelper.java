package com.ctplayground.utils;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class FirebaseHelper {

    private static final FirebaseFirestore db = FirebaseFirestore.getInstance();

    // ── Create user profile in Firestore on signup ──────────────────────────
    // Mirrors the web's createUserProfile() in firebase-bazario.js
    public interface ProfileCallback {
        void onSuccess(Map<String, Object> profile);
        void onFailure(String error);
    }

    public static void createUserProfile(String uid, String name, String email,
                                         String phone, String gender, Date dob,
                                         String city, ProfileCallback callback) {
        Map<String, Object> profile = new HashMap<>();
        profile.put("name",           name);
        profile.put("email",          email);
        profile.put("phone",          phone);
        profile.put("gender",         gender);
        profile.put("dob",            dob);
        profile.put("city",           city);
        profile.put("joinedAt",       new Date());
        profile.put("isPrime",        false);
        profile.put("totalOrders",    0);
        profile.put("lifetimeValue",  0);
        profile.put("preferredCategory", "");
        profile.put("vertical",       "Bazario");

        db.collection("users").document(uid)
                .set(profile)
                .addOnSuccessListener(unused -> callback.onSuccess(profile))
                .addOnFailureListener(e -> callback.onFailure(e.getMessage()));
    }

    // ── Read user profile from Firestore on login ────────────────────────────
    public static void getUserProfile(String uid, ProfileCallback callback) {
        db.collection("users").document(uid)
                .get()
                .addOnSuccessListener((DocumentSnapshot doc) -> {
                    if (doc.exists()) {
                        callback.onSuccess(doc.getData());
                    } else {
                        // Profile doesn't exist — return empty map (guest or old user)
                        callback.onSuccess(new HashMap<>());
                    }
                })
                .addOnFailureListener(e -> callback.onFailure(e.getMessage()));
    }
}
