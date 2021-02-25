package com.alexeyre.fixit.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.alexeyre.fixit.Constants.Constants;
import com.alexeyre.fixit.Helpers.TrafficLightModel;
import com.alexeyre.fixit.Helpers.TrafficLightReportModel;
import com.alexeyre.fixit.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ReportActivity extends AppCompatActivity {

    private TrafficLightModel trafficLightModel;
    private TrafficLightReportModel trafficLightReportModel;
    private DatabaseReference databaseReference;
    private CheckBox cb1, cb2, cb3, cb4, cb5, cb6, cb7, cb8;
    private String Yes = "Yes", No = "No";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        //create hooks
        cb1 = findViewById(R.id.physical_damage_cb);
        cb2 = findViewById(R.id.electrical_issue_cb);
        cb3 = findViewById(R.id.lights_cb);
        cb4 = findViewById(R.id.pedestrian_light_cb);
        cb5 = findViewById(R.id.pedestrian_button_cb);
        cb6 = findViewById(R.id.sounds_cb);
        cb7 = findViewById(R.id.sequence_cb);
        cb8 = findViewById(R.id.repairs_needed_cb);


        databaseReference = FirebaseDatabase.getInstance().getReference().child(Constants.COORDINATES).child(Constants.INSPECTIONS).child(Constants.REPORTS);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        //use a bundle to set the ID and Location
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String bundleInfo = bundle.getBundle("bundle").getString("traffic_light_data");
            //if the bundle is not empty, pass the instance of the bundle to a method to get the data
            if (bundleInfo != null) {
                getBundleInfo(bundleInfo);
            } else {
                //Display error to user
                Toast.makeText(this, "There is no data in the bundle", Toast.LENGTH_SHORT).show();
                //Quit the activity
                finish();
            }
        } else {
            //Display error to user
            Toast.makeText(this, "The data was not passed in correctly", Toast.LENGTH_SHORT).show();
            //Quit the activity
            finish();
        }


    }//end onCreate

    private void getBundleInfo(String bundleInfo) {

        FirebaseDatabase.getInstance().getReference().child("coordinates").child(bundleInfo).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                trafficLightModel = snapshot.getValue(TrafficLightModel.class);
                trafficLightModel.setkey(snapshot.getKey());

                if (trafficLightModel != null) {
                    updateUI();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void updateUI() {
        ((TextInputEditText) findViewById(R.id.create_id_tv)).setText(trafficLightModel.getkey());
        ((TextInputEditText) findViewById(R.id.create_location_tv)).setText(trafficLightModel.getname()); //Actually the location
    }


    public void submit_report(View view) {

        //check for physical damage
        if (cb1.isChecked()) {
        } else {

        }

        //check for electrical issues
        if (cb2.isChecked()) {

        } else {

        }

        //check for lights
        if (cb3.isChecked()) {

        } else {

        }

        //check for pedestrian lights
        if (cb4.isChecked()) {

        } else {

        }

        //check for pedestrian button
        if (cb5.isChecked()) {

        } else {

        }

        //check for sounds
        if (cb6.isChecked()) {

        } else {

        }

        //check for sequence
        if (cb7.isChecked()) {

        } else {

        }

        //check for repairs
        if (cb8.isChecked()) {

        } else {

        }
    }//end submit report
}