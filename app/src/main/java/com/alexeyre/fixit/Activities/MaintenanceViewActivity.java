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
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
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

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MaintenanceViewActivity extends AppCompatActivity {

    //class variables
    private DatabaseReference pathRef;
    private MaintenanceModel maintenanceModel;
    private MaintenanceReportModel maintenanceReportModel;
    private MaintenanceReportModel maintenanceReportModelClone;
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
                    maintenanceReportModelClone = snapshot.getValue(MaintenanceReportModel.class);

                    if (maintenanceReportModel != null) {
                        updateReport(); //update the ui using the data from the model
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

    private void updateReport() { //setting the report information
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


    public void btnCloseMaintenance(View view) { //closing the report and deleting it from open maintenance


        //create database path hooks
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child(Constants.COORDINATES).child(bundleInfo)
                .child(Constants.MAINTENANCE).child(timestamp);

        final DatabaseReference closedRef = FirebaseDatabase.getInstance().getReference().child(Constants.MAINTENANCE).child(Constants.CLOSED)
                .child(bundleInfo).child(timestamp);

        final DatabaseReference openRef = FirebaseDatabase.getInstance().getReference().child(Constants.MAINTENANCE).child(Constants.OPEN)
                .child(bundleInfo).child(timestamp);


        //create dialog alert when pressing the button
        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Close Report?")
                .setContentText("Are you sure you want to close the report?")
                .setCancelText("No")
                .setConfirmText("Yes")
                .showCancelButton(true)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();

                        //allow for a smooth animation signout
                        try {
                            Thread.sleep(200);

                        } catch (InterruptedException ie) {
                            Thread.currentThread().interrupt();
                        }

                        sDialog.getProgressHelper().setBarColor(R.color.colorAccent);
                        sDialog.setTitleText("Closing report...");
                        sDialog.setCancelable(false);
                        sDialog.show();

                        ValueEventListener valueEventListener = new ValueEventListener() { //create a listener to get data from database
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                closedRef.setValue(snapshot.getValue()).addOnCompleteListener(new OnCompleteListener<Void>() { //add data to the closed path in the database
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isComplete()) { //if successful
                                            openRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() { //remove the data from the openref database path
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    databaseReference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() { //remove data from the coordinates path
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                sDialog.dismissWithAnimation();
                                                                MaintenanceViewActivity.this.finish(); //end the activity
                                                            } else {
                                                                Toast.makeText(MaintenanceViewActivity.this, "ERROR, Report failed to close", Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });

                                                }
                                            });
                                        } else {
                                            Toast.makeText(MaintenanceViewActivity.this, "ERROR, Report failed to close", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        };
                        openRef.addListenerForSingleValueEvent(valueEventListener);


                    }
                })
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                    }
                })
                .show();


    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}