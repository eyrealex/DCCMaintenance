package com.alexeyre.fixit.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class ReportActivity extends AppCompatActivity {
    private TrafficLightModel trafficLightModel;
    private TrafficLightReportModel trafficLightReportModel;
    ArrayList<TrafficLightReportModel> reportModelArrayList = new ArrayList<>();
    private DatabaseReference databaseReference;
    private CheckBox cb1, cb2, cb3, cb4, cb5, cb6, cb7, cb8;
    private Button btnSubmit;
    private String key;
    private String bundleInfo;
    int i = 0;
    private String Yes = "Yes", No = "No";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        //use a bundle to set the ID and Location
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            bundleInfo = bundle.getBundle("bundle").getString("traffic_light_data");
            //if the bundle is not empty, pass the instance of the bundle to a method to get the data
            if (bundleInfo != null) {
                getBundleInfo(bundleInfo);
                key = bundleInfo;

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


        String myCurrentDateTime = DateFormat.getDateTimeInstance()
                .format(Calendar.getInstance().getTime());

        //create hooks
        databaseReference = FirebaseDatabase.getInstance().getReference().child(Constants.COORDINATES).child(key).child(Constants.INSPECTIONS).child(myCurrentDateTime);
        trafficLightReportModel = new TrafficLightReportModel();
        btnSubmit = findViewById(R.id.submitBtn);
        cb1 = findViewById(R.id.physical_damage_cb);
        cb2 = findViewById(R.id.electrical_issue_cb);
        cb3 = findViewById(R.id.lights_cb);
        cb4 = findViewById(R.id.pedestrian_light_cb);
        cb5 = findViewById(R.id.pedestrian_button_cb);
        cb6 = findViewById(R.id.sounds_cb);
        cb7 = findViewById(R.id.sequence_cb);
        cb8 = findViewById(R.id.repairs_needed_cb);


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                reportModelArrayList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    try {
                        TrafficLightReportModel trafficLightReportModelObject = ds.getValue(TrafficLightReportModel.class);
                        reportModelArrayList.add(trafficLightReportModelObject);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check for physical damage
                if (cb1.isChecked()) {
                    trafficLightReportModel.setphysical_damage(Yes);
                    databaseReference.child(String.valueOf(i + 1)).setValue(trafficLightReportModel);
                } else {
                    trafficLightReportModel.setphysical_damage(No);
                    databaseReference.child(String.valueOf(i + 1)).setValue(trafficLightReportModel);
                }

                //check for electrical issues
                if (cb2.isChecked()) {
                    trafficLightReportModel.setelectrical_damage(Yes);
                    databaseReference.child(String.valueOf(i + 1)).setValue(trafficLightReportModel);
                } else {
                    trafficLightReportModel.setelectrical_damage(No);
                    databaseReference.child(String.valueOf(i + 1)).setValue(trafficLightReportModel);
                }

                //check for lights
                if (cb3.isChecked()) {
                    trafficLightReportModel.setlights_ok(Yes);
                    databaseReference.child(String.valueOf(i + 1)).setValue(trafficLightReportModel);
                } else {
                    trafficLightReportModel.setlights_ok(No);
                    databaseReference.child(String.valueOf(i + 1)).setValue(trafficLightReportModel);
                }

                //check for pedestrian lights
                if (cb4.isChecked()) {
                    trafficLightReportModel.setpedestrian_lights_ok(Yes);
                    databaseReference.child(String.valueOf(i + 1)).setValue(trafficLightReportModel);
                } else {
                    trafficLightReportModel.setpedestrian_lights_ok(No);
                    databaseReference.child(String.valueOf(i + 1)).setValue(trafficLightReportModel);
                }

                //check for pedestrian button
                if (cb5.isChecked()) {
                    trafficLightReportModel.setpedestrian_button_ok(Yes);
                    databaseReference.child(String.valueOf(i + 1)).setValue(trafficLightReportModel);
                } else {
                    trafficLightReportModel.setpedestrian_button_ok(No);
                    databaseReference.child(String.valueOf(i + 1)).setValue(trafficLightReportModel);
                }

                //check for sounds
                if (cb6.isChecked()) {
                    trafficLightReportModel.setsounds_ok(Yes);
                    databaseReference.child(String.valueOf(i + 1)).setValue(trafficLightReportModel);
                } else {
                    trafficLightReportModel.setsounds_ok(No);
                    databaseReference.child(String.valueOf(i + 1)).setValue(trafficLightReportModel);
                }

                //check for sequence
                if (cb7.isChecked()) {
                    trafficLightReportModel.setsequence_ok(Yes);
                    databaseReference.child(String.valueOf(i + 1)).setValue(trafficLightReportModel);
                } else {
                    trafficLightReportModel.setsequence_ok(No);
                    databaseReference.child(String.valueOf(i + 1)).setValue(trafficLightReportModel);
                }

                //check for repairs
                if (cb8.isChecked()) {
                    trafficLightReportModel.setrepairs_needed(Yes);
                    databaseReference.child(String.valueOf(i + 1)).setValue(trafficLightReportModel);
                } else {
                    trafficLightReportModel.setrepairs_needed(No);
                    databaseReference.child(String.valueOf(i + 1)).setValue(trafficLightReportModel);
                }
            }
        });


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

}