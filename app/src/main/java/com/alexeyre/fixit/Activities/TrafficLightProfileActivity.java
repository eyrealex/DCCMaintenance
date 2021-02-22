package com.alexeyre.fixit.Activities;

import android.os.Bundle;
import android.util.Log;
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

public class TrafficLightProfileActivity extends AppCompatActivity {

    private TrafficLightModel trafficLightModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_traffic_light_profile);

        //Get Traffic light ID from bundle
        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            //Get ID from bundle
            String trafficLightID = bundle.getBundle("bundle").getString("traffic_light_id");

            //check if the ID was gotten
            if (trafficLightID != null) {
                //Fetch the profile data from Firebase
                getTrafficLightProfile(trafficLightID);

                //Init the components like onClicks etc
                initComponents();

            } else {
                //Quit activity as there is no ID
            }

        } else {
            //Display error to user
            Toast.makeText(this, "The data was not passed in correctly", Toast.LENGTH_SHORT).show();

            //Quit the activity
            finish();
        }

    }

    private void initComponents() {

    }

    /**
     * Get the traffic light object from Firebase
     *
     * @param trafficLightID : String traffic light ID for building path to dataSnapshot
     */
    private void getTrafficLightProfile(String trafficLightID) {
        //Do firebase call here
        FirebaseDatabase.getInstance().getReference().child("coordinates").child(trafficLightID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //Data acquired
                trafficLightModel = snapshot.getValue(TrafficLightModel.class);
                trafficLightModel.setkey(snapshot.getKey());

                //Convert into object (class level variable)

                //if object != null update the UI with the data acquired
                if (trafficLightModel != null)
                    updateUI();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //Data either not available or there was an error reading firebase data

                //Handle what happens in this event. Maybe quit activity?


            }
        });
    }

    /**
     * Render the object data into the view
     */
    private void updateUI() {
        ((TextInputEditText) findViewById(R.id.id_tv)).setText(trafficLightModel.getkey());
        ((TextInputEditText) findViewById(R.id.location_tv)).setText(trafficLightModel.getname()); //Actually the location

        if (trafficLightModel.getinspection() == null) {
            ((TextInputEditText) findViewById(R.id.next_inspec_tv)).setText("N/A");
            ((TextInputEditText) findViewById(R.id.last_inspec_tv)).setText("N/A");
            ((TextInputEditText) findViewById(R.id.last_employee_tv)).setText("N/A");
        } else {
            //If you have inspection

            //Get the timestamp of the last,
            //For the next, add 6 months to last
            //For person, get the most recent one and get the name from it
        }

        //If there are no inspections
        //Set next, last and who to "N/A"
    }

    public void create_inspection(View view) {
    }

    public void previous_inspections(View view) {
    }
}