package com.alexeyre.fixit.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.alexeyre.fixit.Helpers.TrafficLightModel;
import com.alexeyre.fixit.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ReportActivity extends AppCompatActivity {

    private TrafficLightModel trafficLightModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

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
    }
}