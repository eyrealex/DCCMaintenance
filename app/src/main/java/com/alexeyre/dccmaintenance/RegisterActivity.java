package com.alexeyre.dccmaintenance;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    //variables

    TextInputLayout regName, regUsername, regEmail, regPhoneNumber, regPassword;
    Button accountBtn, regBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //hooks
        regBtn = findViewById(R.id.reg_btn);
        accountBtn = findViewById(R.id.reg_login_btn);
        regName = findViewById(R.id.reg_name);
        regUsername = findViewById(R.id.reg_username);
        regEmail = findViewById(R.id.reg_email);
        regPhoneNumber = findViewById(R.id.reg_phone_number);
        regPassword = findViewById(R.id.reg_password);

        //transfer to the login page
        accountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }//end on create method

    private Boolean validateName() {
        String value = regName.getEditText().getText().toString();

        if (value.isEmpty()) {
            regName.setError("Field cannot be empty");
            return false;
        } else {
            regName.setError(null);
            regName.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validateUsername() {
        String value = regUsername.getEditText().getText().toString();
        String noWhiteSpace = "\\A\\w{2,20}\\z";

        if (value.isEmpty()) {
            regUsername.setError("Field cannot be empty");
            return false;
        } else if (value.length() > 15) {
            regUsername.setError("Username is too long");
            return false;
        } else if (!value.matches(noWhiteSpace)) {
            regUsername.setError("White Spaces are not allowed");
            return false;
        } else {
            regUsername.setError(null);
            regUsername.setEnabled(false);
            return true;
        }
    }

    private Boolean validateEmail() {
        String value = regEmail.getEditText().getText().toString();
        String emailRegex = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (value.isEmpty()) {
            regEmail.setError("Field cannot be empty");
            return false;
        } else if (!value.matches(emailRegex)) {
            regEmail.setError("Invalid email address");
            return false;
        } else {
            regEmail.setError(null);
            regEmail.setEnabled(false);
            return true;
        }
    }

    private Boolean validatePhoneNumber() {
        String value = regPhoneNumber.getEditText().getText().toString();

        if (value.isEmpty()) {
            regPhoneNumber.setError("Field cannot be empty");
            return false;
        } else {
            regPhoneNumber.setError(null);
            regPhoneNumber.setEnabled(false);
            return true;
        }
    }

    private Boolean validatePassword() {
        String value = regPassword.getEditText().getText().toString();
        String passwordRegex = "^"
                + "(?=.*[0-9])" //atlease one digit
                + "(?=.*[a-zA-Z])" //any letter
                + "(?=.*[@#$%^&+=!?])"  //atleast one special character
                + "(?=\\S+$)"  //no white space
                + ".{4,}" //atleast 6 characters in length
                + "$";

        if (value.isEmpty()) {
            regPassword.setError("Field cannot be empty");
            return false;
        } else if (!value.matches(passwordRegex)) {
            regPassword.setError("Password is too weak");
            return false;
        } else {
            regPassword.setError(null);
            regPassword.setEnabled(false);
            return true;
        }
    }


    //method to register a new user
    public void registerUser(View view) {

        //check to see if values for registration follow above methods, if not return error
        if (!validateName() | !validateUsername() | !validateEmail() | !validatePhoneNumber() | !validatePassword()) {
            return;
        }

        //initialize the database structure to save data to
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        //get all values
        String name = regName.getEditText().getText().toString();
        String userName = regUsername.getEditText().getText().toString();
        String email = regEmail.getEditText().getText().toString();
        String phoneNumber = regPhoneNumber.getEditText().getText().toString();
        String password = regPassword.getEditText().getText().toString();
        UserProfileModel userProfile = new UserProfileModel(name, userName, email, phoneNumber, password);
        reference.child(userName).setValue(userProfile);

        //after successful register take the user to the dashboard
        startActivity(new Intent(RegisterActivity.this, MainActivity.class));
        finish();
    }//end register method


    //when the back button is pressed
    @Override
    public void onBackPressed() {
        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
        finish();
    }

}//end register activity class