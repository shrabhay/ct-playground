package com.ctplayground;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
public class ListingActivity extends AppCompatActivity {
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String category = getIntent().getStringExtra("category");
        TextView tv = new TextView(this);
        tv.setText("Listing — " + (category != null ? category : "All"));
        tv.setTextSize(20f); tv.setPadding(64, 120, 64, 0);
        setContentView(tv);
    }
}
