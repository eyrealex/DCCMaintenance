package com.alexeyre.fixit.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.squareup.picasso.Picasso;

public class InspectionViewActivity extends AppCompatActivity {
    private DatabaseReference pathRef;
    private InspectionReceiptModel inspectionReceiptModel;
    private TrafficLightReportModel trafficLightReportModel;
    private String bundleInfo; //contains traffic light id key
    private String path; //contains url for database/coordinates
    private String timestamp; //contains timestamp in millis seconds when report was submitted
    private ImageView signatureIv, photoIv;
    private EditText notes;
    private CheckBox cb1, cb2, cb3, cb4, cb5, cb6, cb7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspection_view);

        //hooks
        notes = findViewById(R.id.notes_tv);
        cb1 = findViewById(R.id.physical_issue_cb);
        cb2 = findViewById(R.id.electrical_issue_cb);
        cb3 = findViewById(R.id.lights_cb);
        cb4 = findViewById(R.id.buttons_cb);
        cb5 = findViewById(R.id.sounds_cb);
        cb6 = findViewById(R.id.sequence_cb);
        cb7 = findViewById(R.id.repairs_needed_cb);
        signatureIv = findViewById(R.id.add_signature_iv);
        photoIv = findViewById(R.id.photo_iv);

        //use a bundle to set the ID and Location
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            bundleInfo = bundle.getBundle("bundle").getString("inspection_key");
            timestamp = bundle.getBundle("bundle").getString("inspection_timestamp");
            path = bundle.getBundle("bundle").getString("inspection_path");

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

                pathRef = FirebaseDatabase.getInstance().getReferenceFromUrl(path);
                pathRef.child(bundleInfo).child(Constants.INSPECTIONS).child(timestamp).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        trafficLightReportModel = snapshot.getValue(TrafficLightReportModel.class);
                        trafficLightReportModel.setbutton_issues(snapshot.child("button_issues").getValue(String.class));
                        trafficLightReportModel.setelectrical_issues(snapshot.child("electrical_issues").getValue(String.class));
                        trafficLightReportModel.setimage_url(snapshot.child("image_url").getValue(String.class));
                        trafficLightReportModel.setlight_issues(snapshot.child("light_issues").getValue(String.class));
                        trafficLightReportModel.setnotes(snapshot.child("notes").getValue(String.class));
                        trafficLightReportModel.setphysical_issues(snapshot.child("physical_issues").getValue(String.class));
                        trafficLightReportModel.setrepairs_needed(snapshot.child("repairs_needed").getValue(String.class));
                        trafficLightReportModel.setsequence_issues(snapshot.child("sequence_issues").getValue(String.class));
                        trafficLightReportModel.setsignature_url(snapshot.child("signature_url").getValue(String.class));
                        trafficLightReportModel.setsound_issues(snapshot.child("sound_issues").getValue(String.class));

                        if(trafficLightReportModel != null){
                            updateData();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }


                });

                if (inspectionReceiptModel != null) {
                    updateUI();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void updateData() {
        //get checkboxes
        if(trafficLightReportModel.getPhysical_issues().contains("Yes")){
            cb1.setChecked(true);
        }else{
            cb1.setChecked(false);
        }
        if(trafficLightReportModel.getelectrical_issues().contains("Yes")){
            cb2.setChecked(true);
        }else{
            cb2.setChecked(false);
        }
        if(trafficLightReportModel.getlight_issues().contains("Yes")){
            cb3.setChecked(true);
        }else{
            cb3.setChecked(false);
        }
        if(trafficLightReportModel.getbutton_issues().contains("Yes")){
            cb4.setChecked(true);
        }else{
            cb4.setChecked(false);
        }
        if(trafficLightReportModel.getsound_issues().contains("Yes")){
            cb5.setChecked(true);
        }else{
            cb5.setChecked(false);
        }
        if(trafficLightReportModel.getsequence_issues().contains("Yes")){
            cb6.setChecked(true);
        }else{
            cb6.setChecked(false);
        }
        if(trafficLightReportModel.getrepairs_needed().contains("Yes")){
            cb7.setChecked(true);
        }else{
            cb7.setChecked(false);
        }
        //get notes
        notes.setText(trafficLightReportModel.getnotes());

        //get image urls
        Picasso.get().load(trafficLightReportModel.getsignature_url()).into(signatureIv);
        Picasso.get().load(trafficLightReportModel.getimage_url()).into(photoIv);
    }


    private void updateUI() {
        ((TextInputEditText) findViewById(R.id.inspection_id_tv)).setText(inspectionReceiptModel.getid());
        ((TextInputEditText) findViewById(R.id.inspection_location_tv)).setText(inspectionReceiptModel.getlocation()); //Actually the location
        ((TextInputEditText) findViewById(R.id.inspection_reported_tv)).setText(inspectionReceiptModel.gettimestamp()); //Actually the location
        ((TextInputEditText) findViewById(R.id.inspection_created_by_tv)).setText(inspectionReceiptModel.getcreated_by()); //Actually the location
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}