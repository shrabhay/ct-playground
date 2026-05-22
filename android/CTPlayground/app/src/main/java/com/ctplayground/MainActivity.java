package com.ctplayground;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.clevertap.android.sdk.CleverTapAPI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Re-identify in CT on every launch for persisted Firebase sessions.
            // Uses email synchronously — no Firestore round-trip needed here.
            CleverTapAPI ct = CleverTapAPI.getDefaultInstance(this);
            if (ct != null) {
                HashMap<String, Object> ctProfile = new HashMap<>();
                ctProfile.put("Identity",      user.getEmail());
                ctProfile.put("Email",         user.getEmail());
                ctProfile.put("Vertical",      "Bazario");
                ctProfile.put("Vertical Type", "Ecommerce");
                ct.onUserLogin(ctProfile);
            }
            startActivity(new Intent(this, VerticalSelectorActivity.class));
        } else {
            startActivity(new Intent(this, LoginActivity.class));
        }
        finish();
    }
}
