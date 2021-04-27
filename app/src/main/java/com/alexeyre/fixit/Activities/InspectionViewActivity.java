package com.alexeyre.fixit.Activities;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.alexeyre.fixit.Constants.Constants;
import com.alexeyre.fixit.Models.InspectionReceiptModel;
import com.alexeyre.fixit.Models.TrafficLightReportModel;
import com.alexeyre.fixit.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class InspectionViewActivity extends AppCompatActivity {
    private InspectionReceiptModel inspectionReceiptModel;
    private static TrafficLightReportModel trafficLightReportModel;
    private String bundleInfo;
    private String timestamp;
    private EditText notes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspection_view);

        System.out.println("Path url in on create ***********************" + inspectionReceiptModel.getpath());

        notes = findViewById(R.id.notes_tv);

        //use a bundle to set the ID and Location
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            bundleInfo = bundle.getBundle("bundle").getString("inspection_key");
            timestamp = bundle.getBundle("bundle").getString("inspection_timestamp");
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
    }

    /**
     * Get the traffic light object from Firebase
     *
     * @param bundleInfo : String traffic light ID for building path to dataSnapshot
     */
    private void getBundleInfo(String bundleInfo) {
        FirebaseDatabase.getInstance().getReference().child(Constants.USERS).child(FirebaseAuth.getInstance().getCurrentUser()
                .getUid()).child(Constants.INSPECTIONS).child(timestamp).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                inspectionReceiptModel = snapshot.getValue(InspectionReceiptModel.class);
                inspectionReceiptModel.setid(bundleInfo);

                if (inspectionReceiptModel != null) {
                    updateUI();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        FirebaseDatabase.getInstance().getReference().child(Constants.USERS).child(FirebaseAuth.getInstance().getCurrentUser()
                .getUid()).child(Constants.INSPECTIONS).child(timestamp).child("path").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String pathURL = snapshot.getValue(String.class);
                inspectionReceiptModel.setpath(pathURL);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void updateUI() {
        ((TextInputEditText) findViewById(R.id.inspection_id_tv)).setText(inspectionReceiptModel.getid());
        ((TextInputEditText) findViewById(R.id.inspection_location_tv)).setText(inspectionReceiptModel.getlocation()); //Actually the location
        ((TextInputEditText) findViewById(R.id.inspection_reported_tv)).setText(inspectionReceiptModel.gettimestamp()); //Actually the location
        ((TextInputEditText) findViewById(R.id.inspection_created_by_tv)).setText(inspectionReceiptModel.getcreated_by()); //Actually the location
        System.out.println("Path url after on create ***********************" + inspectionReceiptModel.getpath());
        //notes.setText(trafficLightReportModel.getnotes());


    }
}