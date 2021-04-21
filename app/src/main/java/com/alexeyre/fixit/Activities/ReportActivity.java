package com.alexeyre.fixit.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.alexeyre.fixit.Models.InspectionReceiptModel;
import com.alexeyre.fixit.Models.TrafficLightModel;
import com.alexeyre.fixit.Models.TrafficLightReportModel;
import com.alexeyre.fixit.Models.UserProfileModel;
import com.alexeyre.fixit.Models.UserSingletonModel;
import com.alexeyre.fixit.R;
import com.alexeyre.fixit.Repositories.InspectionRepository;
import com.alexeyre.fixit.Repositories.impl.InspectionRepositoryImpl;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.alexeyre.fixit.Constants.Constants.COORDINATES;
import static com.alexeyre.fixit.Constants.Constants.INSPECTIONS;


public class ReportActivity extends AppCompatActivity {
    private TrafficLightModel trafficLightModel;
    private TrafficLightReportModel trafficLightReportModel;
    ArrayList<TrafficLightReportModel> reportModelArrayList = new ArrayList<>();
    private DatabaseReference databaseReference;
    private CheckBox cb1, cb2, cb3, cb4, cb5, cb6, cb7;
    private EditText notes;
    private Button btnSubmit;
    private ImageView btnImage, btnSignature;
    private String key;
    private String bundleInfo;
    String imageUrl;
    private Uri uri;



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
        //create hooks
        databaseReference = FirebaseDatabase.getInstance().getReference().child(COORDINATES).child(key).child(INSPECTIONS);
        trafficLightReportModel = new TrafficLightReportModel();
        btnImage = findViewById(R.id.imageBtn);
        btnSignature = findViewById(R.id.signatureBtn);
        cb1 = findViewById(R.id.physical_issue_cb);
        cb2 = findViewById(R.id.electrical_issue_cb);
        cb3 = findViewById(R.id.lights_cb);
        cb4 = findViewById(R.id.buttons_cb);
        cb5 = findViewById(R.id.sounds_cb);
        cb6 = findViewById(R.id.sequence_cb);
        cb7 = findViewById(R.id.repairs_needed_cb);
        notes = findViewById(R.id.notes_box);


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

        //adding the signature to the report
        btnSignature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //adding photo evidence of the inspection to the report
        btnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent imageSelect = new Intent(Intent.ACTION_PICK);
                imageSelect.setType("image/*"); //used for selecting jpegs, pngs etc
                startActivityForResult(imageSelect, 1);
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


    public void btnSubmit(View view) {
        //Check here
        //Show progress dialog here
        //check for physical damage

        //turnary operator for checklists
        trafficLightReportModel.setphysical_issues(cb1.isChecked() ? "Yes" : "No");
        trafficLightReportModel.setelectrical_issues(cb2.isChecked() ? "Yes" : "No");
        trafficLightReportModel.setlight_issues(cb3.isChecked() ? "Yes" : "No");
        trafficLightReportModel.setbutton_issues(cb4.isChecked() ? "Yes" : "No");
        trafficLightReportModel.setsound_issues(cb5.isChecked() ? "Yes" : "No");
        trafficLightReportModel.setsequence_issues(cb6.isChecked() ? "Yes" : "No");
        trafficLightReportModel.setrepairs_needed(cb7.isChecked() ? "Yes" : "No");

        //for writing notes to the database
        trafficLightReportModel.setnotes(notes.getText().toString());//Returns "" if nothing in the input field

        //Write to database
        String timestamp = String.valueOf(System.currentTimeMillis());
        databaseReference.child(timestamp).setValue(trafficLightReportModel).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                //Create the receipt object/ model
                InspectionReceiptModel receipt = new InspectionReceiptModel();
                receipt.settimestamp(timestamp);
                receipt.setcreated_by(UserSingletonModel.getInstance().getuser_name()); //get the current users name and put it in here
                String path = String.format("%s/%s/%s/%s", COORDINATES, key, INSPECTIONS, timestamp); //create a path for an inspection
                receipt.setpath(path);

                //Build the path to the users node /reports
                FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(INSPECTIONS).child(receipt.gettimestamp())
                        .setValue(receipt).addOnCompleteListener(task1 -> {
                    if (task.isSuccessful()) {
                        finish();
                        startActivity(new Intent(ReportActivity.this, TrafficLightProfileActivity.class));
                    }

                });
            } else {
                //Tell the user there was an error
                Toast.makeText(this, "Error when creating report", Toast.LENGTH_SHORT).show();
            }

        });
    }
}