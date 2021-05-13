package com.alexeyre.fixit.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.alexeyre.fixit.Constants.Constants;
import com.alexeyre.fixit.Models.MaintenanceModel;
import com.alexeyre.fixit.Models.MaintenanceReportModel;
import com.alexeyre.fixit.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MaintenanceViewActivity extends AppCompatActivity {

    private DatabaseReference pathRef;
    private MaintenanceModel maintenanceModel;
    private MaintenanceReportModel maintenanceReportModel;
    private String bundleInfo; //contains traffic light id key
    private String path; //contains url for database/coordinates
    private String timestamp; //contains timestamp in millis seconds when report was submitted
    private String created; //contains timestamp in millis seconds when report was submitted
    private String location; //contains timestamp in millis seconds when report was submitted
    private ImageView signatureIv, photoIv;
    private EditText notes;
    private CheckBox cb1, cb2, cb3, cb4, cb5, cb6, cb7;
    private Calendar calendar = Calendar.getInstance();
    private DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    private String getMyCurrentDateTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintenance_view);

        //hooks
        notes = findViewById(R.id.maintenance_notes_tv);
        cb1 = findViewById(R.id.maintenance_physical_issue_cb);
        cb2 = findViewById(R.id.maintenance_electrical_issue_cb);
        cb3 = findViewById(R.id.maintenance_lights_cb);
        cb4 = findViewById(R.id.maintenance_buttons_cb);
        cb5 = findViewById(R.id.maintenance_sounds_cb);
        cb6 = findViewById(R.id.maintenance_sequence_cb);
        cb7 = findViewById(R.id.maintenance_repairs_needed_cb);
        signatureIv = findViewById(R.id.maintenance_add_signature_iv);
        photoIv = findViewById(R.id.maintenance_photo_iv);

        //use a bundle to set the ID and Location
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            bundleInfo = bundle.getBundle("bundle").getString("maintenance_key");
            timestamp = bundle.getBundle("bundle").getString("maintenance_timestamp");
            location = bundle.getBundle("bundle").getString("maintenance_location");
            created = bundle.getBundle("bundle").getString("maintenance_created");

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

        updateUI();
    }

    /**
     * Get the traffic light object from Firebase
     *
     * @param bundleInfo : String traffic light ID for building path to dataSnapshot
     */
    private void getBundleInfo(String bundleInfo) {
        FirebaseDatabase.getInstance().getReference().child(Constants.COORDINATES).child(bundleInfo).child(Constants.MAINTENANCE).child(timestamp).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot != null && snapshot.getValue() != null) {
                    maintenanceReportModel = snapshot.getValue(MaintenanceReportModel.class);

                    if (maintenanceReportModel != null) {
                        updateReport();
                    }
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void updateUI() {
        ((TextInputEditText) findViewById(R.id.maintenance_id_tv)).setText(bundleInfo);
        ((TextInputEditText) findViewById(R.id.maintenance_location_tv)).setText(location); //Actually the location
        //convert timestamp to string date format
        String newDate = timestamp;
        calendar.setTimeInMillis(Long.parseLong(newDate));
        getMyCurrentDateTime = formatter.format(calendar.getTime());
        ((TextInputEditText) findViewById(R.id.maintenance_reported_tv)).setText(getMyCurrentDateTime); //Actually the location
        ((TextInputEditText) findViewById(R.id.maintenance_created_by_tv)).setText(created); //Actually the location
    }

    private void updateReport() {
        //get checkboxes
        if (maintenanceReportModel.getPhysical_issues().contains("Yes")) {
            cb1.setChecked(true);
        } else {
            cb1.setChecked(false);
        }
        if (maintenanceReportModel.getelectrical_issues().contains("Yes")) {
            cb2.setChecked(true);
        } else {
            cb2.setChecked(false);
        }
        if (maintenanceReportModel.getlight_issues().contains("Yes")) {
            cb3.setChecked(true);
        } else {
            cb3.setChecked(false);
        }
        if (maintenanceReportModel.getbutton_issues().contains("Yes")) {
            cb4.setChecked(true);
        } else {
            cb4.setChecked(false);
        }
        if (maintenanceReportModel.getsound_issues().contains("Yes")) {
            cb5.setChecked(true);
        } else {
            cb5.setChecked(false);
        }
        if (maintenanceReportModel.getsequence_issues().contains("Yes")) {
            cb6.setChecked(true);
        } else {
            cb6.setChecked(false);
        }
        if (maintenanceReportModel.getrepairs_needed().contains("Yes")) {
            cb7.setChecked(true);
        } else {
            cb7.setChecked(false);
        }
        //get notes
        notes.setText(maintenanceReportModel.getnotes());

        //get image urls
        Picasso.get().load(maintenanceReportModel.getsignature_url()).into(signatureIv);
        Picasso.get().load(maintenanceReportModel.getimage_url()).into(photoIv);
    }


    public void btnCloseMaintenance(View view) {


        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child(Constants.COORDINATES).child(bundleInfo)
                .child(Constants.MAINTENANCE).child(timestamp);

        databaseReference.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                FirebaseDatabase.getInstance().getReference().child("maintenance").child(bundleInfo).child(timestamp).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(MaintenanceViewActivity.this, "Maintenance report has been close", Toast.LENGTH_SHORT).show();
                        MaintenanceViewActivity.this.finish();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MaintenanceViewActivity.this, "Maintenance report failed to close", Toast.LENGTH_SHORT).show();
            }
        });


    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}