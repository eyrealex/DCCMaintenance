package com.alexeyre.fixit.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class ProfileActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private ImageView editBtn;

    private UserProfileModel userModel;

    private View.OnClickListener listener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            userModel = (UserProfileModel) bundle.getBundle("bundle").getSerializable("object");

            setupUI();

            //Setup onClick
            listener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ProfileActivity.this.findViewById(R.id.profile_name_tv).setFocusableInTouchMode(true);
                    ProfileActivity.this.findViewById(R.id.profile_number_tv).setFocusableInTouchMode(true);
                    ProfileActivity.this.findViewById(R.id.sign_out_btn).setVisibility(View.GONE);

                    //Show the save button
                    //We can reuse the edit button?
                    //Swap icon for check
                    ((ImageView) ProfileActivity.this.findViewById(R.id.editBtn)).setImageDrawable(ProfileActivity.this.getResources().getDrawable(R.drawable.save_icon));
                    Toast.makeText(ProfileActivity.this, "Update User Data", Toast.LENGTH_SHORT).show();

                    //Set a new on click
                    ProfileActivity.this.findViewById(R.id.editBtn).setOnClickListener((View view) -> ProfileActivity.this.updateUserData());
                    ProfileActivity.this.findViewById(R.id.update_image_button).setOnClickListener((View view) -> ProfileActivity.this.updateProfilePicture());
                }
            };
        }
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


        FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).updateChildren(updateMap, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                //When it is complete
                //Check if it was succesful first
                if (error == null) {
                    //All good!
                    Toast.makeText(ProfileActivity.this, "Update Successful", Toast.LENGTH_SHORT).show();
                    //Swap icon back
                    ((ImageView) findViewById(R.id.editBtn)).setImageDrawable(ProfileActivity.this.getResources().getDrawable(R.drawable.ic_edit));
                    findViewById(R.id.sign_out_btn).setVisibility(View.VISIBLE);

                    //Stop user editing data anymore
                    findViewById(R.id.profile_name_tv).setFocusableInTouchMode(false);
                    findViewById(R.id.profile_number_tv).setFocusableInTouchMode(false);
                    findViewById(R.id.sign_out_btn).setVisibility(View.VISIBLE);

                    //Set the edit button onClick back to make it editable
                    findViewById(R.id.editBtn).setOnClickListener(listener);

                    //Hide Keyboard
                    InputMethodManager imm = (InputMethodManager) ProfileActivity.this.getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(findViewById(R.id.profile_name_tv).getWindowToken(), 0);

                    //Clear focus so cursor isn#t showing after update
                } else {
                    //Error happened now what do I tell the user? And do I keep everything editbile or default everything locked
                }
            }
        });
    }

    private void updateProfilePicture() {

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
                        startActivity(new Intent(ProfileActivity.this, LoginActivity.class));


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