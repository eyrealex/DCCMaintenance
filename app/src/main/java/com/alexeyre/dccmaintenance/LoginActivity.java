package com.alexeyre.dccmaintenance;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Pair;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class LoginActivity extends AppCompatActivity {

    //variables
    private Button mLoginBtn, mAccountBtn, mResetBtn;
    private ImageView mImage;
    private TextView mLogo, mDesc;
    private TextInputLayout mEmail, mPassword;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //hooks
        mEmail = findViewById(R.id.login_email);
        mPassword = findViewById(R.id.login_password);
        mImage = findViewById(R.id.login_image);
        mLogo = findViewById(R.id.login_text);
        mDesc = findViewById(R.id.login_desc);
        mLoginBtn = findViewById(R.id.login_btn);
        mAccountBtn = findViewById(R.id.login_reg_btn);
        mResetBtn = findViewById(R.id.login_forgot_btn);

        mAuth = FirebaseAuth.getInstance();


        mAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //To call next activity
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);

                //create pairs for animation
                Pair[] pairs = new Pair[7];
                pairs[0] = new Pair<View, String>(mImage, "app_image");
                pairs[1] = new Pair<View, String>(mLogo, "app_text");
                pairs[2] = new Pair<View, String>(mDesc, "desc_tran");
                pairs[3] = new Pair<View, String>(mEmail, "username_tran");
                pairs[4] = new Pair<View, String>(mPassword, "password_tran");
                pairs[5] = new Pair<View, String>(mLoginBtn, "btn_tran");
                pairs[6] = new Pair<View, String>(mAccountBtn, "account_tran");

                //Call next activity by attaching the animation with it.
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(LoginActivity.this, pairs);
                    startActivity(intent, options.toBundle());
                }
            }
        });

        mResetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
            }
        });


        loginUser();


    }//end on create method

    private Boolean validateEmail() {
        String email = mEmail.getEditText().getText().toString();

        if (email.isEmpty()) {
            mEmail.setError("Email is required");
            mEmail.requestFocus();
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mEmail.setError("Please enter a valid email");
            mEmail.requestFocus();
            return false;
        } else {
            mEmail.setError(null);
            mEmail.setErrorEnabled(false);
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

    //login method
    private void loginUser() {
        findViewById(R.id.login_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmail.getEditText().getText().toString().trim();
                String password = mPassword.getEditText().getText().toString().trim();

                //check to see if values for registration follow above methods, if not return error
                if (!validateEmail() | !validatePassword()) {
                    return;
                }

                //authenticate the login using email and password
                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                            //check for email verification before logging in
                            if (user.isEmailVerified()) {
                                //loading animations
                                SweetAlertDialog loadingDialog = new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.PROGRESS_TYPE);
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    loadingDialog.getProgressHelper().setBarColor(getColor(R.color.colorPrimary));
                                }
                                loadingDialog.setTitleText("Logging In");
                                loadingDialog.setContentText("Please wait...");
                                loadingDialog.setCancelable(false);
                                loadingDialog.show();
                                loadingDialog.dismissWithAnimation();


                                //redirect user to dashboard
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));

                            } else {
                                user.sendEmailVerification();
                                Toast.makeText(LoginActivity.this, "Check your email to verify account", Toast.LENGTH_LONG).show();
                            }

                        } else {
                            Toast.makeText(LoginActivity.this, "Failed to login! Try again", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }//end loginUser method


    //when the back button is pressed
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

}//end login activity
