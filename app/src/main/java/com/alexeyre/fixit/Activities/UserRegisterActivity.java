package com.alexeyre.fixit.Activities;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Pair;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.alexeyre.fixit.R;
import com.alexeyre.fixit.Models.UserProfileModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class UserRegisterActivity extends AppCompatActivity {

    //variables
    private TextInputLayout mName, mEmail, mPhone, mPassword;
    private Button mAccountBtn;
    private Button mRegisterBtn;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);

        //hooks
        mRegisterBtn = findViewById(R.id.reg_btn);
        mAccountBtn = findViewById(R.id.reg_login_btn);
        mName = findViewById(R.id.reg_name);
        mEmail = findViewById(R.id.reg_email);
        mPhone = findViewById(R.id.reg_phone_number);
        mPassword = findViewById(R.id.reg_password);

        mAuth = FirebaseAuth.getInstance();

        //transfer to the login page
        mAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserRegisterActivity.this, UserLoginActivity.class);
                //create pairs for animation
                Pair[] pairs = new Pair[3];
                pairs[0] = new Pair<View, String>(mEmail, "username_tran");
                pairs[1] = new Pair<View, String>(mPassword, "password_tran");
                pairs[2] = new Pair<View, String>(mAccountBtn, "account_tran");

                //Call next activity by attaching the animation with it.
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(UserRegisterActivity.this, pairs);
                    startActivity(intent, options.toBundle());
                }
            }
        });

        registerUser();
    }//end on create method

    //methods to validate user registration fields
    private Boolean validateName() {
        String name = mName.getEditText().getText().toString();

        if (name.isEmpty()) {
            mName.setError("Name is required");
            mName.requestFocus();
            return false;
        } else {
            mName.setError(null);
            mName.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validateEmail() {
        String email = mEmail.getEditText().getText().toString();

        if (email.isEmpty()) {
            mEmail.setError("Email is required");
            mEmail.requestFocus();
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mEmail.setError("Invalid Email");
            mEmail.requestFocus();
            return false;
        } else {
            mEmail.setError(null);
            mEmail.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validatePhoneNumber() {
        String phone = mPhone.getEditText().getText().toString();

        if (phone.isEmpty()) {
            mPhone.setError("Phone Number is required");
            mPhone.requestFocus();
            return false;
        }
        if (!Patterns.PHONE.matcher(phone).matches()) {
            mPhone.setError("Invalid Phone Number");
            mPhone.requestFocus();
            return false;
        } else {
            mPhone.setError(null);
            mPhone.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validatePassword() {
        String password = mPassword.getEditText().getText().toString();

        if (password.isEmpty()) {
            mPassword.setError("Password is required");
            mPassword.requestFocus();
            return false;
        }
        if (password.length() < 6) {
            mPassword.setError("Minimum Password length should be 6 characters");
            mPassword.requestFocus();
            return false;
        } else {
            mPassword.setError(null);
            mPassword.setErrorEnabled(false);
            return true;
        }
    }

    //register a new user method
    private void registerUser() {
        findViewById(R.id.reg_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //check to see if values for registration follow above methods, if not return error
                if (!validateName() | !validateEmail() | !validatePhoneNumber() | !validatePassword()) {
                    return;
                }

                //sweet dialog animations
                //TODO: Create a custom dialog using the https://github.com/pedant/sweet-alert-dialog github library
                SweetAlertDialog loadingDialog = new SweetAlertDialog(UserRegisterActivity.this, SweetAlertDialog.PROGRESS_TYPE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    loadingDialog.getProgressHelper().setBarColor(getColor(R.color.colorPrimary));
                }
                loadingDialog.setTitleText("Logging In");
                loadingDialog.setContentText("Please wait...");
                loadingDialog.setCancelable(false);
                loadingDialog.show();

                loadingDialog.dismissWithAnimation();

                //initialize the database structure to save data to
                String name = mName.getEditText().getText().toString().trim();
                String email = mEmail.getEditText().getText().toString().trim();
                String phone = mPhone.getEditText().getText().toString().trim();
                String password = mPassword.getEditText().getText().toString().trim();

                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            UserProfileModel user = new UserProfileModel(name, email, phone, FirebaseAuth.getInstance().getCurrentUser().getUid());

                            FirebaseDatabase.getInstance().getReference("users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(UserRegisterActivity.this, "User registered successfully", Toast.LENGTH_LONG).show();
                                        loadingDialog.dismissWithAnimation();
                                        //redirect user to dashboard
                                        startActivity(new Intent(UserRegisterActivity.this, MainActivity.class));

                                    } else {
                                        Toast.makeText(UserRegisterActivity.this, "Failed to register! Try again", Toast.LENGTH_LONG).show();
                                        loadingDialog.dismissWithAnimation();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(UserRegisterActivity.this, "Failed to register! Try again", Toast.LENGTH_LONG).show();
                            loadingDialog.dismissWithAnimation();
                        }
                    }
                }); //end createUserWithEmailAndPassword listener

            }//end register onclick
        });
    }//end register button method


    //when the back button is pressed
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(UserRegisterActivity.this, UserLoginActivity.class);
        //create pairs for animation
        Pair[] pairs = new Pair[3];
        pairs[0] = new Pair<View, String>(mEmail, "username_tran");
        pairs[1] = new Pair<View, String>(mPassword, "password_tran");
        pairs[2] = new Pair<View, String>(mAccountBtn, "account_tran");

        //Call next activity by attaching the animation with it.
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(UserRegisterActivity.this, pairs);
            startActivity(intent, options.toBundle());
        }
    }


}//end register activity class