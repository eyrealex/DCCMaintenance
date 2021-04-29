package com.alexeyre.fixit.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.alexeyre.fixit.R;


public class MetricsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_metric);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}