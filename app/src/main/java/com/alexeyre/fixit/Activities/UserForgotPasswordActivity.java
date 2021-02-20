package com.alexeyre.fixit.Activities;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.alexeyre.fixit.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;


public class UserForgotPasswordActivity extends AppCompatActivity {

    private TextInputLayout mEmail;
    private Button mResetBtn;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        mEmail = findViewById(R.id.reset_email);
        mResetBtn = findViewById(R.id.reset_btn);
        mAuth = FirebaseAuth.getInstance();

        mResetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword();
            }
        });
    }

    //validate email field
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

    //reset password
    private void resetPassword() {
        String email = mEmail.getEditText().getText().toString().trim();

        //check to see if values for registration follow above methods, if not return error
        if (!validateEmail()) {
            return;
        }

        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(UserForgotPasswordActivity.this, "Check your email to reset your account", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(UserForgotPasswordActivity.this, "Failed to reset password! Try again", Toast.LENGTH_LONG).show();
                }
            }
        });


    }//end reset password method

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }


}//end forgot password activity