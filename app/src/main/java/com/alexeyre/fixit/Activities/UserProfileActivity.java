package com.alexeyre.fixit.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.alexeyre.fixit.Helpers.UserProfileModel;
import com.alexeyre.fixit.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;


public class UserProfileActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;
    private StorageReference storageReference;
    private FirebaseStorage firebaseStorage;
    private Uri imageUri;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private ImageView editBtn, updateBtn;
    private String imageUrl, currentUserID;
    private CircleImageView profileImage;
    private UserProfileModel userModel;
    private View.OnClickListener listener;
    private String downloadURL;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //init components for uploading profile picture
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        profileImage = findViewById(R.id.profile_image);


        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            userModel = (UserProfileModel) bundle.getBundle("bundle").getSerializable("object");

            setupUI();

            //Setup onClick
            listener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UserProfileActivity.this.findViewById(R.id.profile_name_tv).setFocusableInTouchMode(true);
                    UserProfileActivity.this.findViewById(R.id.profile_number_tv).setFocusableInTouchMode(true);
                    UserProfileActivity.this.findViewById(R.id.sign_out_btn).setVisibility(View.GONE);
                    UserProfileActivity.this.findViewById(R.id.updateBtn).setVisibility(View.GONE);

                    //Show the save button
                    //We can reuse the edit button?
                    //Swap icon for check
                    ((ImageView) UserProfileActivity.this.findViewById(R.id.editBtn)).setImageDrawable(UserProfileActivity.this.getResources().getDrawable(R.drawable.save_icon));
                    UserProfileActivity.this.findViewById(R.id.updateBtn).setVisibility(View.VISIBLE);
                    Toast.makeText(UserProfileActivity.this, "Update Profile", Toast.LENGTH_SHORT).show();

                    //Set a new on click
                    UserProfileActivity.this.findViewById(R.id.editBtn).setOnClickListener((View view) -> UserProfileActivity.this.updateUserData());
                    UserProfileActivity.this.findViewById(R.id.updateBtn).setOnClickListener((View view) -> UserProfileActivity.this.selectImage());
                }
            };
        }


    }

    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();

            //convert URI file to Bitmap for uploading
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);

                if (bitmap != null) {
                    uploadImage(bitmap);
                }
            } catch (IOException e) {
                e.printStackTrace();
                Log.d("TAG", "onActivityResult: Failed to convert to Bitmap");
            }


//            profileImage.setImageURI(imageUri);
//            uploadImage();

        }
    }

    private void uploadImage(Bitmap bitmap) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading Image...");
        progressDialog.show();
        StorageReference fileRef = storageReference.child("profile_images/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/profile");


        ByteArrayOutputStream baos = new ByteArrayOutputStream(); //Make empty byteStreams
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);//Compress bitmap to 50% of quality and also to jpeg
        byte[] data = baos.toByteArray();//Convert to byte array because firebase likes this

        UploadTask uploadTask = fileRef.putBytes(data); //Make upload task object ready to execute
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("TAG", "onFailure: ");
            }
        }).addOnSuccessListener(taskSnapshot -> {
            //Get download url
            taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(uri -> {
                downloadURL = uri.toString();
                progressDialog.dismiss();

                Picasso.get().load(downloadURL).placeholder(R.drawable.progress_animation).into(((ImageView) findViewById(R.id.profile_image)));
            });
        });
    }

    private void setupUI() {
        //Check if != null && !.equals("")
        if (userModel.getprofile_photo_url() != null && !userModel.getprofile_photo_url().equals("")) {
            Picasso.get().load(userModel.getprofile_photo_url()).into(((ImageView) findViewById(R.id.profile_image)));
        }
        ((TextView) findViewById(R.id.profile_name_banner)).setText(userModel.getname());
        ((TextView) findViewById(R.id.profile_email_banner)).setText(userModel.getemail());

        ((TextView) findViewById(R.id.profile_name_tv)).setText(userModel.getname());
        ((TextView) findViewById(R.id.profile_email_tv)).setText(userModel.getemail());
        ((TextView) findViewById(R.id.profile_number_tv)).setText(userModel.getphone());

        //If the user is looking at their own profile, let them edit
        //For some reason, it appears no one is logged in :: BUG
        if (FirebaseAuth.getInstance().getCurrentUser() == null || FirebaseAuth.getInstance().getCurrentUser().getUid() == null) {
            Toast.makeText(this, "No User Apparently logged in", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        if (userModel.getuid().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
            findViewById(R.id.editBtn).setVisibility(View.VISIBLE);
            findViewById(R.id.sign_out_btn).setVisibility(View.VISIBLE);

            findViewById(R.id.editBtn).setOnClickListener(v -> listener.onClick(v));

            //Make when editing the name, the top name view also changes with each character
            ((TextInputEditText) findViewById(R.id.profile_name_tv)).addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence inputData, int start, int before, int count) {
                    //What do we want to do when the text changes?
                    //Maybe update the textview above? So it looks like their both being edited
                    ((TextView) findViewById(R.id.profile_name_banner)).setText(inputData.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }
    }

    private void updateUserData() {
        //Create update HashMap of user data to update
        HashMap<String, Object> updateMap = new HashMap<>();
        updateMap.put("name", ((TextInputEditText) findViewById(R.id.profile_name_tv)).getText().toString().trim());
        updateMap.put("phone", ((TextInputEditText) findViewById(R.id.profile_number_tv)).getText().toString().trim());
        if (downloadURL != null) {
            updateMap.put("profile_photo_url", downloadURL);
        }


        FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).updateChildren(updateMap, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                //When it is complete
                //Check if it was succesful first
                if (error == null) {
                    //All good!
                    Snackbar.make(findViewById(android.R.id.content), "Profile Updated Successfully", Snackbar.LENGTH_LONG).show();
                    //Swap icon back
                    ((ImageView) findViewById(R.id.editBtn)).setImageDrawable(UserProfileActivity.this.getResources().getDrawable(R.drawable.ic_edit));
                    UserProfileActivity.this.findViewById(R.id.updateBtn).setVisibility(View.GONE);
                    findViewById(R.id.sign_out_btn).setVisibility(View.VISIBLE);

                    //Stop user editing data anymore
                    findViewById(R.id.profile_name_tv).setFocusableInTouchMode(false);
                    findViewById(R.id.profile_number_tv).setFocusableInTouchMode(false);
                    findViewById(R.id.sign_out_btn).setVisibility(View.VISIBLE);

                    //Set the edit button onClick back to make it editable
                    findViewById(R.id.editBtn).setOnClickListener(listener);

                    //Hide Keyboard
                    InputMethodManager imm = (InputMethodManager) UserProfileActivity.this.getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(findViewById(R.id.profile_name_tv).getWindowToken(), 0);

                    //Clear focus so cursor isn#t showing after update
                } else {
                    //Error happened now what do I tell the user? And do I keep everything editbile or default everything locked
                }
            }
        });
    }


    private void resetPassword(View v) {
        FirebaseAuth.getInstance().sendPasswordResetEmail(userModel.getemail()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                //When password reset email has been sent... Do stuff here
            }
        });
    }


    public void sign_out(View view) {
        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Sign out?")
                .setContentText("You will need to login again!")
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

                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(UserProfileActivity.this, UserLoginActivity.class));


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


}//end profile activity class