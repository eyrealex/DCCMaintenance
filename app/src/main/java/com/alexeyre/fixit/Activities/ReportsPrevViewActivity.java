package com.alexeyre.fixit.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.alexeyre.fixit.Constants.Constants;
import com.alexeyre.fixit.Models.InspectionReceiptModel;
import com.alexeyre.fixit.Models.TrafficLightReportModel;
import com.alexeyre.fixit.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ReportsPrevViewActivity extends AppCompatActivity {

    private InspectionReceiptModel inspectionReceiptModel;
    private TrafficLightReportModel trafficLightReportModel;
    private String key, name, timestamp, created_by;
    private ImageView signatureIv, photoIv;
    private EditText notes;
    private CheckBox cb1, cb2, cb3, cb4, cb5, cb6, cb7;
    private Calendar calendar = Calendar.getInstance();
    private DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    private String getMyCurrentDateTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports_prev_view);

        //hooks
        notes = findViewById(R.id.prev_view_notes_tv);
        cb1 = findViewById(R.id.prev_view_physical_issue_cb);
        cb2 = findViewById(R.id.prev_view_electrical_issue_cb);
        cb3 = findViewById(R.id.prev_view_lights_cb);
        cb4 = findViewById(R.id.prev_view_buttons_cb);
        cb5 = findViewById(R.id.prev_view_sounds_cb);
        cb6 = findViewById(R.id.prev_view_sequence_cb);
        cb7 = findViewById(R.id.prev_view_repairs_needed_cb);
        signatureIv = findViewById(R.id.prev_view_add_signature_iv);
        photoIv = findViewById(R.id.prev_view_photo_iv);

        //use a bundle to set the ID and Location
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            key = bundle.getBundle("bundle").getString("prev_key");
            name = bundle.getBundle("bundle").getString("prev_name");
            timestamp = bundle.getBundle("bundle").getString("prev_timestamp");
            created_by = bundle.getBundle("bundle").getString("prev_created");
            updateUI();

            //if the bundle is not empty, pass the instance of the bundle to a method to get the data
            if (key != null) {
                getKey(key);

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

    private void updateUI() {
        ((TextInputEditText) findViewById(R.id.prev_view_id_tv)).setText(key);
        ((TextInputEditText) findViewById(R.id.prev_view_location_tv)).setText(name); //Actually the location
        //convert timestamp to string date format
        String newDate = timestamp;
        calendar.setTimeInMillis(Long.parseLong(newDate));
        getMyCurrentDateTime = formatter.format(calendar.getTime());
        ((TextInputEditText) findViewById(R.id.prev_view_reported_tv)).setText(getMyCurrentDateTime); //Actually the location
        ((TextInputEditText) findViewById(R.id.prev_view_created_by_tv)).setText(created_by); //Actually the location
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

    /**
     * Get the traffic light object from Firebase
     *
     * @param key : String traffic light ID for building path to dataSnapshot
     */
    private void getKey(String key) {
        FirebaseDatabase.getInstance().getReference().child(Constants.COORDINATES).child(key).child(Constants.INSPECTIONS).child(timestamp).addValueEventListener(new ValueEventListener() {
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

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }



}