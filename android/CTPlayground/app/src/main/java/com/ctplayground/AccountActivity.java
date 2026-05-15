package com.ctplayground;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
public class AccountActivity extends AppCompatActivity {
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView tv = new TextView(this);
        tv.setText("Account — coming soon");
        tv.setTextSize(20f); tv.setPadding(64, 120, 64, 0);
        setContentView(tv);
    }
}
