package com.ctplayground;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.clevertap.android.sdk.CleverTapAPI;
import com.ctplayground.utils.FirebaseHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class AccountActivity extends AppCompatActivity {

    private String currentUid;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) currentUid = currentUser.getUid();

        setupNavbar();
        setupMenuItems();
        loadProfile();
        loadStats();

        fireCTEvent("Account Page Viewed", new HashMap<>());
    }

    private void setupNavbar() {
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
    }

    private void setupMenuItems() {
        findViewById(R.id.menuMyOrders).setOnClickListener(v ->
                startActivity(new Intent(this, MyOrdersActivity.class)));

        findViewById(R.id.menuWishlist).setOnClickListener(v ->
                startActivity(new Intent(this, WishlistActivity.class)));

        findViewById(R.id.menuPersonalInfo).setOnClickListener(v ->
                startActivity(new Intent(this, PersonalInfoActivity.class)));

        findViewById(R.id.menuAddresses).setOnClickListener(v ->
                Toast.makeText(this, "Manage Addresses — coming in a future update",
                        Toast.LENGTH_SHORT).show());

        findViewById(R.id.menuPrime).setOnClickListener(v ->
                Toast.makeText(this, "Bazario Prime — coming in a future update",
                        Toast.LENGTH_SHORT).show());

        findViewById(R.id.menuSignOut).setOnClickListener(v -> confirmSignOut());
    }

    private void loadProfile() {
        if (currentUid == null) return;

        // Show email immediately (synchronous)
        if (currentUser.getEmail() != null) {
            ((TextView) findViewById(R.id.tvProfileEmail)).setText(currentUser.getEmail());
        }

        // Load full profile from Firestore
        FirebaseHelper.getUserProfile(currentUid, new FirebaseHelper.ProfileCallback() {
            @Override
            public void onSuccess(Map<String, Object> profile) {
                if (profile.isEmpty()) return;

                String name = profile.containsKey("name")
                        ? String.valueOf(profile.get("name")) : "";
                String email = profile.containsKey("email")
                        ? String.valueOf(profile.get("email")) : currentUser.getEmail();
                boolean isPrime = profile.containsKey("isPrime")
                        && Boolean.TRUE.equals(profile.get("isPrime"));

                // Avatar: first letter of name
                String avatar = name.isEmpty() ? "?" :
                        String.valueOf(name.charAt(0)).toUpperCase();

                ((TextView) findViewById(R.id.tvAvatar)).setText(avatar);
                ((TextView) findViewById(R.id.tvProfileName)).setText(
                        name.isEmpty() ? "Bazario User" : name);
                ((TextView) findViewById(R.id.tvProfileEmail)).setText(email);

                // Personal info subtitle
                String phone = profile.containsKey("phone")
                        ? String.valueOf(profile.get("phone")) : "";
                String city  = profile.containsKey("city")
                        ? String.valueOf(profile.get("city")) : "";
                String sub = (!phone.isEmpty() ? phone : "") +
                        (!phone.isEmpty() && !city.isEmpty() ? " · " : "") +
                        (!city.isEmpty() ? city : "");
                if (!sub.isEmpty()) {
                    ((TextView) findViewById(R.id.tvPersonalInfoSub)).setText(sub);
                }

                // Prime badge
                if (isPrime) {
                    findViewById(R.id.tvPrimeBadge).setVisibility(
                            android.view.View.VISIBLE);
                }

                // Total orders stat
                Object totalOrders = profile.get("totalOrders");
                if (totalOrders != null) {
                    ((TextView) findViewById(R.id.tvTotalOrders)).setText(
                            String.valueOf(totalOrders));
                }
            }

            @Override
            public void onFailure(String error) {}
        });
    }

    private void loadStats() {
        if (currentUid == null) return;

        // Wishlist count
        FirebaseHelper.getWishlistIds(currentUid, new FirebaseHelper.WishlistCallback() {
            @Override
            public void onSuccess(Set<String> ids) {
                ((TextView) findViewById(R.id.tvWishlistCount)).setText(
                        String.valueOf(ids.size()));
            }
            @Override public void onFailure(String error) {}
        });

        // Cart count
        FirebaseHelper.getCartState(currentUid, new FirebaseHelper.CartStateCallback() {
            @Override
            public void onSuccess(Map<String, Integer> state) {
                int total = 0;
                for (int qty : state.values()) total += qty;
                ((TextView) findViewById(R.id.tvCartCount)).setText(
                        String.valueOf(total));
            }
            @Override public void onFailure(String error) {}
        });
    }

    private void confirmSignOut() {
        new AlertDialog.Builder(this)
                .setTitle("Sign Out")
                .setMessage("Are you sure you want to sign out?")
                .setPositiveButton("Sign Out", (dialog, which) -> signOut())
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void signOut() {
        fireCTEvent("User Logged Out", new HashMap<>());
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this, VerticalSelectorActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void fireCTEvent(String eventName, HashMap<String, Object> props) {
        CleverTapAPI ct = CleverTapAPI.getDefaultInstance(this);
        if (ct != null) {
            props.put("vertical", "bazario");
            ct.pushEvent(eventName, props);
        }
    }
}
