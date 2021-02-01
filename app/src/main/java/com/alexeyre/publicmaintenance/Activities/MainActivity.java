package com.alexeyre.publicmaintenance.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.alexeyre.publicmaintenance.R;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    //launch jobs activity
    public void Jobs(View view) {
        startActivity(new Intent(MainActivity.this, JobInspectionsActivity.class));
    }

    //launch the locations activity
    public void locations(View view) {
        startActivity(new Intent(MainActivity.this, MapsActivity.class));
    }

    //launch the employees activity
    public void Employees(View view) {
        startActivity(new Intent(MainActivity.this, EmployeeActivity.class));
    }

    //launch the metrics activity
    public void Metrics(View view) {
        startActivity(new Intent(MainActivity.this, MetricActivity.class));
    }

    //launch the settings activity
    public void Settings(View view) {
        startActivity(new Intent(MainActivity.this, SettingActivity.class));
    }

    //launch profile activity
    public void Profile(View view) {
        startActivity(new Intent(MainActivity.this, TrafficLightProfileActivity.class));
    }

    //signout the user
    public void logoutBtn(View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }



}
