package com.alexeyre.fixit.Activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.alexeyre.fixit.Constants.Constants;
import com.alexeyre.fixit.Models.InspectionReceiptModel;
import com.alexeyre.fixit.Models.TrafficLightModel;
import com.alexeyre.fixit.Models.TrafficLightReportModel;
import com.alexeyre.fixit.Models.UserSingletonModel;
import com.alexeyre.fixit.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.alexeyre.fixit.Constants.Constants.COORDINATES;
import static com.alexeyre.fixit.Constants.Constants.INSPECTIONS;
import static com.alexeyre.fixit.Constants.Constants.MAINTENANCE;


public class ReportActivity extends AppCompatActivity implements SignatureDialog.Signature_DialogInterface {

    //class variables
    private static final int CAMERA_PERMISSION_CODE = 1;
    public static final int CAMERA_REQUEST_CODE = 2;
    private TrafficLightModel trafficLightModel;
    private TrafficLightReportModel trafficLightReportModel;
    ArrayList<TrafficLightReportModel> reportModelArrayList = new ArrayList<>();
    private DatabaseReference databaseReference;
    private DatabaseReference maintenanceReference;
    StorageReference storageReference;
    private String downloadURL, signatureURL;
    String value;
    Bitmap bitmapclone;
    Uri contentSignatureUri;
    private CheckBox cb1, cb2, cb3, cb4, cb5, cb6, cb7;
    private EditText notes;
    private ImageView btnImage, signatureBtn;
    private String key;
    private String bundleInfo;
    private String timestamp = String.valueOf(System.currentTimeMillis());
    private String currentPhotoPath;
    private String myCurrentDateTime = DateFormat.getDateTimeInstance()
            .format(Calendar.getInstance().getTime());


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
        maintenanceReference = FirebaseDatabase.getInstance().getReference().child(COORDINATES).child(key).child(MAINTENANCE);
        storageReference = FirebaseStorage.getInstance().getReference();
        trafficLightReportModel = new TrafficLightReportModel();
        btnImage = findViewById(R.id.imageBtn);
        signatureBtn = findViewById(R.id.add_signature_btn);
        cb1 = findViewById(R.id.physical_issue_cb);
        cb2 = findViewById(R.id.electrical_issue_cb);
        cb3 = findViewById(R.id.lights_cb);
        cb4 = findViewById(R.id.buttons_cb);
        cb5 = findViewById(R.id.sounds_cb);
        cb6 = findViewById(R.id.sequence_cb);
        cb7 = findViewById(R.id.repairs_needed_cb);
        notes = findViewById(R.id.notes_box);


        databaseReference.addValueEventListener(new ValueEventListener() { //get the values in the database path
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                reportModelArrayList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    try {
                        TrafficLightReportModel trafficLightReportModelObject = ds.getValue(TrafficLightReportModel.class);
                        reportModelArrayList.add(trafficLightReportModelObject); //add the values to the list
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        signatureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });


        //adding photo evidence of the inspection to the report
        btnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                askCameraPermission();
            }
        });


    }//end onCreate


    private void openDialog() { //create a signature dialog
        SignatureDialog signatureDialog = new SignatureDialog();
        signatureDialog.show(getSupportFragmentManager(), "signature dialog");
    }


    private void askCameraPermission() { //ask for permissions to use the camera
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) { //check if the user has given permission to camera to be used
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        } else {
            dispatchTakePictureIntent(); //if user grants permission open camera
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) { //if the user has granted camera permissions
                dispatchTakePictureIntent();
            } else {
                Toast.makeText(this, "Camera Permission Required", Toast.LENGTH_SHORT).show(); //else the user has denied permissions
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            File imageFile = new File(currentPhotoPath);
            Uri contentUri = Uri.fromFile(imageFile);
            uploadImage(imageFile.getName(), contentUri);
        }
    }

    private void uploadImage(String name, Uri contentUri) {
        StorageReference imageRef = storageReference.child("inspection_images/").child(key).child(myCurrentDateTime).child(name);
        imageRef.putFile(contentUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(btnImage);
                        downloadURL = uri.toString();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ReportActivity.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
            }
        });

    }

    //method to store get and store photo url from android developers documentation
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) { //check if camera is present in the device
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.alexeyre.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
            }
        }
    }


    private void getBundleInfo(String bundleInfo) { //get information stored in the bundle from previous activity

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

    private void updateUI() { //update the ui using information from the bundle
        ((TextInputEditText) findViewById(R.id.create_id_tv)).setText(trafficLightModel.getkey());
        ((TextInputEditText) findViewById(R.id.create_location_tv)).setText(trafficLightModel.getname()); //Actually the location
    }


    public void btnSubmit(View view) { //when the user clicks the submit button
        trafficLightReportModel = new TrafficLightReportModel();
        if (signatureURL != null) { //make sure the user has added a signature first
            trafficLightReportModel.setsignature_url(signatureURL);

        } else {
            Toast.makeText(this, "Please add a signature to the report", Toast.LENGTH_SHORT).show();
            return;
        }
        //writing image to database
        if (downloadURL != null) { //make sure the user has added a photo first
            trafficLightReportModel.setimage_url(downloadURL);
        } else {
            Toast.makeText(this, "Please add a photo to the report", Toast.LENGTH_SHORT).show();
            return;
        }

        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE) //create a dialog to let the user know they are submitting the report
                .setTitleText("Submit Report?")
                .setContentText("Is the Report Complete?")
                .setCancelText("No")
                .setConfirmText("Yes")
                .showCancelButton(true)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();

                        //allow for a smooth animation
                        try {
                            Thread.sleep(500);

                        } catch (InterruptedException ie) {
                            Thread.currentThread().interrupt();
                        }

                        sDialog.getProgressHelper().setBarColor(R.color.colorAccent);
                        sDialog.setTitleText("Uploading...");
                        sDialog.setCancelable(false);
                        sDialog.show();



                        //turnary operator for checklists
                        trafficLightReportModel.setphysical_issues(cb1.isChecked() ? "Yes" : "No");
                        trafficLightReportModel.setelectrical_issues(cb2.isChecked() ? "Yes" : "No");
                        trafficLightReportModel.setlight_issues(cb3.isChecked() ? "Yes" : "No");
                        trafficLightReportModel.setbutton_issues(cb4.isChecked() ? "Yes" : "No");
                        trafficLightReportModel.setsound_issues(cb5.isChecked() ? "Yes" : "No");
                        trafficLightReportModel.setsequence_issues(cb6.isChecked() ? "Yes" : "No");
                        trafficLightReportModel.setrepairs_needed(cb7.isChecked() ? "Yes" : "No");
                        trafficLightReportModel.setcreated_by(UserSingletonModel.getInstance().getuser_name());
                        trafficLightReportModel.setlocation(trafficLightModel.getname());

                        //for writing notes to the database
                        trafficLightReportModel.setnotes(notes.getText().toString());//Returns "" if nothing in the input field


                        //Write to database
                        databaseReference.child(timestamp).setValue(trafficLightReportModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful()) {
                                    //Create the receipt object/ model
                                    InspectionReceiptModel receipt = new InspectionReceiptModel();
                                    receipt.settimestamp(timestamp);
                                    receipt.setlocation(trafficLightModel.getname());
                                    receipt.setid(trafficLightModel.getkey());
                                    receipt.setcreated_by(UserSingletonModel.getInstance().getuser_name()); //get the current users name and put it in here
                                    String pathURL = "https://fixit-d41f4-default-rtdb.firebaseio.com/coordinates";
                                    //String path = String.format("%s/%s/%s/%s", COORDINATES, key, INSPECTIONS, timestamp); //create a path for an inspection
                                    receipt.setpath(pathURL);

                                    //Build the path to the users node /reports
                                    FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(INSPECTIONS).child(receipt.gettimestamp())
                                            .setValue(receipt).addOnCompleteListener(task1 -> {
                                        if (task.isSuccessful()) {
                                            sDialog.dismissWithAnimation();
                                            ReportActivity.this.finish();
                                        }

                                    });
                                } else {
                                    //Tell the user there was an error
                                    Toast.makeText(ReportActivity.this, "Error when creating report", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });

                        maintenanceReference.child(timestamp).setValue(trafficLightReportModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                            }
                        });

                        FirebaseDatabase.getInstance().getReference().child(MAINTENANCE).child(Constants.OPEN).child(key).child(timestamp).setValue(trafficLightReportModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                            }
                        });


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
    public void applyBitmap(Bitmap bitmap) { //save the photos to the firebase storage
        bitmapclone = bitmap;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmapclone.compress(Bitmap.CompressFormat.PNG, 90, baos);
        byte[] data = baos.toByteArray();
        StorageReference bitmapRef = storageReference.child("signature_images/").child(key).child(myCurrentDateTime);
        UploadTask uploadTask = bitmapRef.putBytes(data);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        contentSignatureUri = uri;
                        signatureURL = uri.toString();
                        Picasso.get().load(signatureURL).placeholder(R.drawable.progress_animation).into(((ImageView) findViewById(R.id.add_signature_btn)));
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ReportActivity.this, "Failed to get signature url", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() { //when the user attempts to leave the activity let them know with a dialogue to prevent errors with the report
        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Cancel Report?")
                .setContentText("Are you sure you want to cancel the report?")
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

                        ReportActivity.this.finish();
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);


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

}