package com.alexeyre.publicmaintenance.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.alexeyre.publicmaintenance.Adapters.ControlsAdapter;
import com.alexeyre.publicmaintenance.Helpers.HomePageWidgetModel;
import com.alexeyre.publicmaintenance.Helpers.UserProfileModel;
import com.alexeyre.publicmaintenance.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private UserProfileModel currentUser;
    private ArrayList<HomePageWidgetModel> controls = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Get current User Data
        FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() != null && snapshot.hasChildren()) {
                    currentUser = snapshot.getValue(UserProfileModel.class);
                    if (currentUser != null){
                        setupControls();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Database Error " + error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Ordering matters when adding Controls
     */
    private void setupControls() {
        controls.clear(); //Clear previous controls

        //Admin ONLY
        if (currentUser.getadmin()) {
            controls.add(new HomePageWidgetModel(R.drawable.icon_employees, "Employees", EmployeeActivity.class));
        }

        //Added for everyone
        controls.add(new HomePageWidgetModel(R.drawable.icon_inspections, "My Inspection", JobInspectionsActivity.class));
        controls.add(new HomePageWidgetModel(R.drawable.icon_chart, "Metrics", MetricActivity.class));

        setAdapter(controls);
    }

    private void setAdapter(ArrayList<HomePageWidgetModel> controls) {
        RecyclerView controls_rv = findViewById(R.id.widget_rv);

        controls_rv.setAdapter(new ControlsAdapter(this, controls));

    }

    //    //launch jobs activity
//    public void Jobs(View view) {
//        startActivity(new Intent(MainActivity.this, JobInspectionsActivity.class));
//    }
//
//    //launch the locations activity
//    public void locations(View view) {
//        startActivity(new Intent(MainActivity.this, MapsActivity.class));
//    }
//
//    //launch the employees activity
//    public void Employees(View view) {
//        startActivity(new Intent(MainActivity.this, EmployeeActivity.class));
//    }
//
//    //launch the metrics activity
//    public void Metrics(View view) {
//        startActivity(new Intent(MainActivity.this, MetricActivity.class));
//    }
//
//    //launch the settings activity
//    public void Settings(View view) {
//        startActivity(new Intent(MainActivity.this, SettingActivity.class));
//    }
//
    //launch profile activity
    public void Profile(View view) {
        startActivity(new Intent(MainActivity.this, ProfileActivity.class));
    }


    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }


}
