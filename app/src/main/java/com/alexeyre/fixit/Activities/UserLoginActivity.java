package com.alexeyre.fixit.Activities;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Pair;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.alexeyre.fixit.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class UserLoginActivity extends AppCompatActivity {

    //variables
    private Button mLoginBtn, mAccountBtn, mResetBtn;
    private ImageView mImage;
    private TextView mLogo, mDesc;
    private TextInputLayout input_email, input_password;
    private EditText email_et, password_et;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Check if the user is already logged in
        //if so, redirect straight to Home Page
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            finish();
            startActivity(new Intent(this, MainActivity.class));
            return;
        }

        setContentView(R.layout.activity_user_login);

        //hooks
        input_email = findViewById(R.id.text_input_email);
        input_password = findViewById(R.id.text_input_password);
        email_et = findViewById(R.id.email_et);
        password_et = findViewById(R.id.password_et);
        mImage = findViewById(R.id.login_image);
        mLogo = findViewById(R.id.login_text);
        mLoginBtn = findViewById(R.id.login_btn);
        mDesc = findViewById(R.id.login_desc);
        mResetBtn = findViewById(R.id.login_forgot_btn);
        mAccountBtn = findViewById(R.id.login_reg_btn);

        mAuth = FirebaseAuth.getInstance();


        mAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //To call next activity
                Intent intent = new Intent(UserLoginActivity.this, UserRegisterActivity.class);

                //create pairs for animation
                Pair[] pairs = new Pair[7];
                pairs[0] = new Pair<View, String>(mImage, "app_image");
                pairs[1] = new Pair<View, String>(mLogo, "app_text");
                pairs[2] = new Pair<View, String>(input_email, "username_tran");
                pairs[3] = new Pair<View, String>(input_password, "password_tran");
                pairs[4] = new Pair<View, String>(mLoginBtn, "btn_tran");
                pairs[5] = new Pair<View, String>(mAccountBtn, "account_tran");
                pairs[6] = new Pair<View, String>(mDesc, "desc_tran");

                //Call next activity by attaching the animation with it.
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(UserLoginActivity.this, pairs);
                    startActivity(intent, options.toBundle());
                }
            }
        });

        mResetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserLoginActivity.this, UserForgotPasswordActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });


        loginUser();


    }//end on create method

    private Boolean validateEmail() {
        String email = input_email.getEditText().getText().toString();

        if (email.isEmpty()) {
            input_email.setError("Email is required");
            input_email.requestFocus();
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            input_email.setError("Please enter a valid email");
            input_email.requestFocus();
            return false;
        } else {
            input_email.setError(null);
            input_email.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validatePassword() {
        String password = input_password.getEditText().getText().toString();

        if (password.isEmpty()) {
            input_password.setError("Password is required");
            input_password.requestFocus();
            return false;
        }
        if (password.length() < 6) {
            input_password.setError("Minimum Password length should be 6 characters");
            input_password.requestFocus();
            return false;
        } else {
            input_password.setError(null);
            input_password.setErrorEnabled(false);
            return true;
        }
    }

    //login method
    private void loginUser() {
        findViewById(R.id.login_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = email_et.getText().toString().trim();
                String password = password_et.getText().toString().trim();

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
                                SweetAlertDialog loadingDialog = new SweetAlertDialog(UserLoginActivity.this, SweetAlertDialog.PROGRESS_TYPE);
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    loadingDialog.getProgressHelper().setBarColor(getColor(R.color.colorPrimary));
                                }
                                loadingDialog.setTitleText("Logging In");
                                loadingDialog.setContentText("Please wait...");
                                loadingDialog.setCancelable(false);
                                loadingDialog.show();
                                loadingDialog.dismissWithAnimation();


                                //redirect user to dashboard
                                startActivity(new Intent(UserLoginActivity.this, MainActivity.class));
                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                            } else {
                                user.sendEmailVerification();
                                Toast.makeText(UserLoginActivity.this, "Check your email to verify account", Toast.LENGTH_LONG).show();
                            }

                        } else {
                            Toast.makeText(UserLoginActivity.this, "Failed to login! Try again", Toast.LENGTH_LONG).show();
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
