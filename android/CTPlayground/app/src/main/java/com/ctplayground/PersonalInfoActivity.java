package com.ctplayground;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.clevertap.android.sdk.CleverTapAPI;
import com.ctplayground.utils.FirebaseHelper;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class PersonalInfoActivity extends AppCompatActivity {

    private boolean isEditing = false;
    private String currentUid;
    private Map<String, Object> currentProfile = new HashMap<>();

    private TextInputEditText etName, etPhone, etCity, etDob;
    private RadioGroup rgGender;
    private TextView btnEdit;
    private LinearLayout layoutSaveBtn;

    // Stored dob for DatePickerDialog
    private int dobYear, dobMonth, dobDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            currentUid = user.getUid();
            ((TextView) findViewById(R.id.tvEmail)).setText(user.getEmail());
        }

        etName       = findViewById(R.id.etName);
        etPhone      = findViewById(R.id.etPhone);
        etCity       = findViewById(R.id.etCity);
        etDob        = findViewById(R.id.etDob);
        rgGender     = findViewById(R.id.rgGender);
        btnEdit      = findViewById(R.id.btnEdit);
        layoutSaveBtn = findViewById(R.id.layoutSaveBtn);

        etDob.setOnClickListener(v -> { if (isEditing) showDatePicker(); });

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
        btnEdit.setOnClickListener(v -> toggleEditMode());
        findViewById(R.id.btnSave).setOnClickListener(v -> saveChanges());

        loadProfile();
    }

    private void loadProfile() {
        if (currentUid == null) return;
        FirebaseHelper.getUserProfile(currentUid, new FirebaseHelper.ProfileCallback() {
            @Override
            public void onSuccess(Map<String, Object> profile) {
                currentProfile = profile;
                populateFields(profile);
            }
            @Override public void onFailure(String error) {}
        });
    }

    private void populateFields(Map<String, Object> profile) {
        String name   = getString(profile, "name");
        String phone  = getString(profile, "phone");
        String city   = getString(profile, "city");
        String dob    = getString(profile, "dob");
        String gender = getString(profile, "gender");

        etName.setText(name);
        etPhone.setText(phone);
        etCity.setText(city);

        // Format dob from yyyy-MM-dd to dd/MM/yyyy for display
        if (!dob.isEmpty()) {
            etDob.setText(formatDobForDisplay(dob));
            // Parse into year/month/day for DatePicker
            try {
                String[] parts = dob.split("-");
                if (parts.length == 3) {
                    dobYear  = Integer.parseInt(parts[0]);
                    dobMonth = Integer.parseInt(parts[1]) - 1; // 0-indexed
                    dobDay   = Integer.parseInt(parts[2]);
                }
            } catch (NumberFormatException ignored) {}
        }

        // Gender radio
        switch (gender) {
            case "Male":   ((RadioButton) findViewById(R.id.rbMale)).setChecked(true);   break;
            case "Female": ((RadioButton) findViewById(R.id.rbFemale)).setChecked(true); break;
            case "Other":  ((RadioButton) findViewById(R.id.rbOther)).setChecked(true);  break;
        }
    }

    private void toggleEditMode() {
        isEditing = !isEditing;
        btnEdit.setText(isEditing ? "Cancel" : "Edit");

        // Enable/disable all editable fields
        setFieldsEnabled(isEditing);

        layoutSaveBtn.setVisibility(isEditing ? View.VISIBLE : View.GONE);

        if (!isEditing) {
            // Cancelled — reload original values
            populateFields(currentProfile);
        }
    }

    private void setFieldsEnabled(boolean enabled) {
        etName.setEnabled(enabled);
        etPhone.setEnabled(enabled);
        etCity.setEnabled(enabled);
        etDob.setEnabled(enabled);
        for (int i = 0; i < rgGender.getChildCount(); i++)
            rgGender.getChildAt(i).setEnabled(enabled);
    }

    private void showDatePicker() {
        int year  = dobYear  > 0 ? dobYear  : Calendar.getInstance().get(Calendar.YEAR) - 25;
        int month = dobMonth > 0 ? dobMonth : Calendar.getInstance().get(Calendar.MONTH);
        int day   = dobDay   > 0 ? dobDay   : Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

        new DatePickerDialog(this, (view, y, m, d) -> {
            dobYear = y; dobMonth = m; dobDay = d;
            etDob.setText(String.format(Locale.getDefault(),
                    "%02d/%02d/%04d", d, m + 1, y));
        }, year, month, day).show();
    }

    private void saveChanges() {
        String name  = getText(etName);
        String phone = getText(etPhone);
        String city  = getText(etCity);

        if (name.isEmpty()) {
            Toast.makeText(this, "Name cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        // Gender — final via if/else
        final String gender;
        int checkedId = rgGender.getCheckedRadioButtonId();
        if (checkedId == R.id.rbMale)        gender = "Male";
        else if (checkedId == R.id.rbFemale) gender = "Female";
        else if (checkedId == R.id.rbOther)  gender = "Other";
        else                                  gender = "";

        // DOB — final via if/else
        final String dob;
        if (dobYear > 0)
            dob = String.format(Locale.getDefault(),
                    "%04d-%02d-%02d", dobYear, dobMonth + 1, dobDay);
        else
            dob = "";

        Map<String, Object> updates = new HashMap<>();
        updates.put("name",   name);
        updates.put("phone",  phone);
        updates.put("city",   city);
        updates.put("gender", gender);
        updates.put("dob",    dob);

        if (currentUid == null) return;

        FirebaseHelper.updateUserProfile(currentUid, updates,
                new FirebaseHelper.ProfileCallback() {
                    @Override
                    public void onSuccess(Map<String, Object> p) {
                        // Update local copy
                        currentProfile.putAll(updates);

                        // Push to CT
                        pushCTProfileUpdate(name, phone, gender, city, dob);

                        // Fire CT event
                        CleverTapAPI ct = CleverTapAPI.getDefaultInstance(
                                PersonalInfoActivity.this);
                        if (ct != null) {
                            List<String> updatedFields = new ArrayList<>();
                            if (!name.isEmpty())   updatedFields.add("name");
                            if (!phone.isEmpty())  updatedFields.add("phone");
                            if (!city.isEmpty())   updatedFields.add("city");
                            if (!gender.isEmpty()) updatedFields.add("gender");
                            if (!dob.isEmpty())    updatedFields.add("dob");

                            HashMap<String, Object> props = new HashMap<>();
                            props.put("fields_updated", String.join(",", updatedFields));
                            props.put("vertical",        "bazario");
                            ct.pushEvent("Profile Updated", props);
                        }

                        Toast.makeText(PersonalInfoActivity.this,
                                "Profile updated successfully ✓", Toast.LENGTH_SHORT).show();

                        // Exit edit mode
                        isEditing = true;
                        toggleEditMode();
                    }
                    @Override
                    public void onFailure(String error) {
                        Toast.makeText(PersonalInfoActivity.this,
                                "Failed to save. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void pushCTProfileUpdate(String name, String phone,
                                     String gender, String city, String dob) {
        CleverTapAPI ct = CleverTapAPI.getDefaultInstance(this);
        if (ct == null) return;

        HashMap<String, Object> ctProfile = new HashMap<>();
        if (!name.isEmpty())   ctProfile.put("Name",   name);
        if (!phone.isEmpty())  ctProfile.put("Phone",  phone);
        if (!city.isEmpty())   ctProfile.put("City",   city);
        if (!gender.isEmpty()) ctProfile.put("Gender",
                gender.equals("Male") ? "M" : gender.equals("Female") ? "F" : "O");
        if (!dob.isEmpty()) {
            try {
                Date parsedDob = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                        .parse(dob);
                if (parsedDob != null) ctProfile.put("DOB", parsedDob);
            } catch (ParseException ignored) {}
        }
        ct.pushProfile(ctProfile);
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private String getString(Map<String, Object> profile, String key) {
        Object val = profile.get(key);
        return (val != null) ? val.toString() : "";
    }

    private String getText(TextInputEditText et) {
        return et.getText() != null ? et.getText().toString().trim() : "";
    }

    private String formatDobForDisplay(String isoDate) {
        // yyyy-MM-dd → dd/MM/yyyy
        try {
            String[] parts = isoDate.split("-");
            if (parts.length == 3)
                return String.format(Locale.getDefault(),
                        "%s/%s/%s", parts[2], parts[1], parts[0]);
        } catch (Exception ignored) {}
        return isoDate;
    }
}
