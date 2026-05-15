package com.ctplayground;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.clevertap.android.sdk.CleverTapAPI;
import com.ctplayground.utils.FirebaseHelper;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private boolean isLoginMode = true;

    private TextInputEditText etName, etEmail, etPassword, etPhone, etDob, etCity;
    private TextInputLayout tilName, tilPhone, tilDob, tilCity;
    private LinearLayout layoutGender;
    private RadioGroup rgGender;
    private Button btnLoginTab, btnSignupTab, btnSubmit;
    private TextView tvError;
    private FirebaseAuth mAuth;

    private int dobYear, dobMonth, dobDay;  // stores selected DOB

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        etName     = findViewById(R.id.etName);
        etEmail    = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etPhone    = findViewById(R.id.etPhone);
        etDob      = findViewById(R.id.etDob);
        etCity     = findViewById(R.id.etCity);

        tilName    = findViewById(R.id.tilName);
        tilPhone   = findViewById(R.id.tilPhone);
        tilDob     = findViewById(R.id.tilDob);
        tilCity    = findViewById(R.id.tilCity);

        layoutGender = findViewById(R.id.layoutGender);
        rgGender     = findViewById(R.id.rgGender);

        btnLoginTab  = findViewById(R.id.btnLoginTab);
        btnSignupTab = findViewById(R.id.btnSignupTab);
        btnSubmit    = findViewById(R.id.btnSubmit);
        tvError      = findViewById(R.id.tvError);

        // DOB field opens date picker on tap
        etDob.setOnClickListener(v -> showDatePicker());

        btnLoginTab.setOnClickListener(v -> switchMode(true));
        btnSignupTab.setOnClickListener(v -> switchMode(false));
        btnSubmit.setOnClickListener(v -> handleSubmit());
    }

    private void switchMode(boolean loginMode) {
        isLoginMode = loginMode;

        int signupVisibility = loginMode ? View.GONE : View.VISIBLE;
        tilName.setVisibility(signupVisibility);
        tilPhone.setVisibility(signupVisibility);
        layoutGender.setVisibility(signupVisibility);
        tilDob.setVisibility(signupVisibility);
        tilCity.setVisibility(signupVisibility);

        btnSubmit.setText(loginMode ? "Login" : "Sign Up");
        tvError.setVisibility(View.GONE);

        btnLoginTab.setBackgroundTintList(
                getColorStateList(loginMode ? R.color.bazario_orange : android.R.color.darker_gray));
        btnLoginTab.setTextColor(getColor(loginMode ? android.R.color.white : R.color.bazario_dark));
        btnSignupTab.setBackgroundTintList(
                getColorStateList(loginMode ? android.R.color.darker_gray : R.color.bazario_orange));
        btnSignupTab.setTextColor(getColor(loginMode ? R.color.bazario_dark : android.R.color.white));
    }

    private void showDatePicker() {
        Calendar cal = Calendar.getInstance();
        new DatePickerDialog(this, (view, year, month, day) -> {
            dobYear = year; dobMonth = month; dobDay = day;
            String formatted = String.format(Locale.getDefault(), "%02d/%02d/%04d", day, month + 1, year);
            etDob.setText(formatted);
        },
                cal.get(Calendar.YEAR) - 25,  // default: 25 years ago
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void handleSubmit() {
        String email    = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        tvError.setVisibility(View.GONE);

        if (email.isEmpty() || password.isEmpty()) {
            showError("Please fill in all fields");
            return;
        }

        btnSubmit.setEnabled(false);

        if (isLoginMode) {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnSuccessListener(result -> {
                        FirebaseHelper.getUserProfile(result.getUser().getUid(),
                                new FirebaseHelper.ProfileCallback() {
                                    @Override public void onSuccess(Map<String, Object> profile) {
                                        onAuthSuccess(result.getUser(), false, profile);
                                    }
                                    @Override public void onFailure(String error) {
                                        onAuthSuccess(result.getUser(), false, new HashMap<>());
                                    }
                                });
                    })
                    .addOnFailureListener(e -> {
                        showError(e.getMessage());
                        btnSubmit.setEnabled(true);
                    });

        } else {
            // Signup validation
            String name  = etName.getText().toString().trim();
            String phone = etPhone.getText().toString().trim();
            String city  = etCity.getText().toString().trim();

            if (name.isEmpty()) { showError("Please enter your name"); btnSubmit.setEnabled(true); return; }
            if (password.length() < 6) { showError("Password must be at least 6 characters"); btnSubmit.setEnabled(true); return; }

            // Gender
            String gender = "";
            int selectedId = rgGender.getCheckedRadioButtonId();
            if (selectedId != -1) {
                RadioButton rb = findViewById(selectedId);
                gender = rb.getText().toString();
            }

            // DOB as Date (null if not selected)
            Date dob = null;
            if (dobYear > 0) {
                Calendar cal = Calendar.getInstance();
                cal.set(dobYear, dobMonth, dobDay, 0, 0, 0);
                cal.set(Calendar.MILLISECOND, 0);
                dob = cal.getTime();
            }

            String finalGender = gender;
            Date finalDob = dob;

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnSuccessListener(result -> {
                        String uid = result.getUser().getUid();
                        FirebaseHelper.createUserProfile(uid, name, email, phone,
                                finalGender, finalDob, city,
                                new FirebaseHelper.ProfileCallback() {
                                    @Override public void onSuccess(Map<String, Object> profile) {
                                        onAuthSuccess(result.getUser(), true, profile);
                                    }
                                    @Override public void onFailure(String error) {
                                        onAuthSuccess(result.getUser(), true, new HashMap<>());
                                    }
                                });
                    })
                    .addOnFailureListener(e -> {
                        showError(e.getMessage());
                        btnSubmit.setEnabled(true);
                    });
        }
    }

    private void onAuthSuccess(FirebaseUser user, boolean isNewUser, Map<String, Object> profile) {
        String name   = profile.containsKey("name")   ? (String) profile.get("name")   : "";
        String phone  = profile.containsKey("phone")  ? (String) profile.get("phone")  : "";
        String gender = profile.containsKey("gender") ? (String) profile.get("gender") : "";
        String city   = profile.containsKey("city")   ? (String) profile.get("city")   : "";
        Date dob      = profile.containsKey("dob")    ? (Date)   profile.get("dob")    : null;

        CleverTapAPI ct = CleverTapAPI.getDefaultInstance(this);
        if (ct != null) {
            HashMap<String, Object> ctProfile = new HashMap<>();
            ctProfile.put("Email",         user.getEmail());
            ctProfile.put("Vertical",      "Bazario");
            ctProfile.put("Vertical Type", "Ecommerce");
            if (!name.isEmpty())   ctProfile.put("Name",   name);
            if (!phone.isEmpty())  ctProfile.put("Phone",  "+91" + phone);
            if (!gender.isEmpty()) ctProfile.put("Gender",
                    gender.equals("Male") ? "M" : gender.equals("Female") ? "F" : "O");
            if (!city.isEmpty())   ctProfile.put("City",   city);
            if (dob != null)       ctProfile.put("DOB",    dob);
            Object joinedAt = profile.get("joinedAt");
            if (joinedAt instanceof Date) {
                ctProfile.put("Joined At", joinedAt);
            }

            Object isPrime = profile.get("isPrime");
            if (isPrime != null) {
                ctProfile.put("Is Prime", isPrime);
            }
            ct.onUserLogin(ctProfile);

            HashMap<String, Object> props = new HashMap<>();
            props.put("vertical", "bazario");
            ct.pushEvent(isNewUser ? "User Signed Up" : "User Logged In", props);
        }

        startActivity(new Intent(this, BazarioHomeActivity.class));
        finish();
    }

    private void showError(String message) {
        tvError.setText(message);
        tvError.setVisibility(View.VISIBLE);
    }
}
