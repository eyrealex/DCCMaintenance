package com.alexeyre.fixit.Activities;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

import static com.alexeyre.fixit.Constants.Constants.COORDINATES;
import static com.alexeyre.fixit.Constants.Constants.INSPECTIONS;


public class ReportActivity extends AppCompatActivity implements SignatureDialog.Signature_DialogInterface {
    private static final int CAMERA_PERMISSION_CODE = 1;
    public static final int CAMERA_REQUEST_CODE = 2;
    private TrafficLightModel trafficLightModel;
    private TrafficLightReportModel trafficLightReportModel;
    ArrayList<TrafficLightReportModel> reportModelArrayList = new ArrayList<>();
    private DatabaseReference databaseReference;
    StorageReference storageReference;
    private static String downloadURL, signatureURL;
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

    private void openDialog() {
        SignatureDialog signatureDialog = new SignatureDialog();
        signatureDialog.show(getSupportFragmentManager(), "signature dialog");
    }


    private void askCameraPermission() {
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
        if (signatureURL != null) {
            trafficLightReportModel.setsignature_url(signatureURL);

        } else {
            Toast.makeText(this, "Please add a signature to the report", Toast.LENGTH_SHORT).show();
            return;
        }
        //writing image to database
        if (downloadURL != null) {
            trafficLightReportModel.setimage_url(downloadURL);
        } else {
            Toast.makeText(this, "Please add a photo to the report", Toast.LENGTH_SHORT).show();
            return;
        }

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading Report...");
        progressDialog.show();


        //turnary operator for checklists
        trafficLightReportModel.setphysical_issues(cb1.isChecked() ? "Yes" : "No");
        trafficLightReportModel.setelectrical_issues(cb2.isChecked() ? "Yes" : "No");
        trafficLightReportModel.setlight_issues(cb3.isChecked() ? "Yes" : "No");
        trafficLightReportModel.setbutton_issues(cb4.isChecked() ? "Yes" : "No");
        trafficLightReportModel.setsound_issues(cb5.isChecked() ? "Yes" : "No");
        trafficLightReportModel.setsequence_issues(cb6.isChecked() ? "Yes" : "No");
        trafficLightReportModel.setrepairs_needed(cb7.isChecked() ? "Yes" : "No");
        trafficLightReportModel.setcreated_by(UserSingletonModel.getInstance().getuser_name() );

        //for writing notes to the database
        trafficLightReportModel.setnotes(notes.getText().toString());//Returns "" if nothing in the input field



        //Write to database
        databaseReference.child(timestamp).setValue(trafficLightReportModel).addOnCompleteListener(task -> {

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
                        progressDialog.dismiss();
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

    @Override
    public void applyBitmap(Bitmap bitmap) {
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

}