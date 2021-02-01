package com.alexeyre.publicmaintenance.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.alexeyre.publicmaintenance.R;

public class TrafficLightProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_traffic_light_profile);
    }

    public void prevInspectionsBtn(View view) {
        startActivity(new Intent(TrafficLightProfileActivity.this, PrevInspectionsActivity.class));
    }

    public void createInspectionBtn(View view) {
        startActivity(new Intent(TrafficLightProfileActivity.this, CreateInspectionsActivity.class));
    }
}