package com.alexeyre.fixit.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.alexeyre.fixit.Adapters.ControlsAdapter;
import com.alexeyre.fixit.Helpers.HomePageWidgetModel;
import com.alexeyre.fixit.Helpers.UserProfileModel;
import com.alexeyre.fixit.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

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
                    if (currentUser != null) {
                        setupControls();
                        setupUI();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Database Error " + error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupUI() {
        ((TextView) findViewById(R.id.profile_name)).setText(currentUser.getname());
        if (currentUser.getprofile_photo_url() != null && !currentUser.getprofile_photo_url().equals("")) {
            Picasso.get().load(currentUser.getprofile_photo_url()).placeholder(R.drawable.progress_animation).into(((ImageView) findViewById(R.id.profile_pic)));
        }
    }

    /**
     * Ordering matters when adding Controls
     */
    private void setupControls() {
        controls.clear(); //Clear previous controls

        //Admin ONLY
        if (currentUser.getadmin() != null && currentUser.getadmin()) {
            controls.add(new HomePageWidgetModel(R.drawable.icon_employees, "Employees", EmployeeActivity.class));
        }

        //Added for everyone
        controls.add(new HomePageWidgetModel(R.drawable.icon_inspections, "Inspections", TrafficLightInspections.class));
        controls.add(new HomePageWidgetModel(R.drawable.icon_chart, "Metrics", MetricActivity.class));
        controls.add(new HomePageWidgetModel(R.drawable.icon_locations, "Traffic Lights", TrafficLightLocationsActivity.class));
        controls.add(new HomePageWidgetModel(R.drawable.icon_settings, "Settings", SettingActivity.class));

        setAdapter(controls);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private void setAdapter(ArrayList<HomePageWidgetModel> controls) {
        RecyclerView controls_rv = findViewById(R.id.widget_rv);
        controls_rv.setAdapter(new ControlsAdapter(this, controls));

    }

    public void Profile(View view) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("object", currentUser);
        startActivity(new Intent(MainActivity.this, ProfileActivity.class).putExtra("bundle", bundle));
    }


    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}
