package com.ctplayground;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
public class ProductActivity extends AppCompatActivity {
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String id = getIntent().getStringExtra("product_id");
        TextView tv = new TextView(this);
        tv.setText("Product Detail — " + id + "\nComing soon");
        tv.setTextSize(18f); tv.setPadding(64, 120, 64, 0);
        setContentView(tv);
    }
}
