package com.alexeyre.dccmaintenance;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    //variables
    TextInputLayout regFirstName, regSurname, regEmail, regPhoneNumber, regPassword;
    Button regLoginBtn, regBtn;

    FirebaseDatabase rootNode;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //find fields
        regBtn = findViewById(R.id.regBtn);
        regLoginBtn = findViewById(R.id.regLoginBtn);
        regFirstName = findViewById(R.id.reg_firstname);
        regSurname = findViewById(R.id.reg_surname);
        regEmail = findViewById(R.id.reg_email);
        regPhoneNumber = findViewById(R.id.reg_phonenumber);
        regPassword = findViewById(R.id.reg_password);

        regLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rootNode = FirebaseDatabase.getInstance();
                reference = rootNode.getReference("users");

                //get all values
                String firstName = regFirstName.getEditText().getText().toString();
                String surname = regSurname.getEditText().getText().toString();
                String email = regEmail.getEditText().getText().toString();
                String phoneNumber = regPhoneNumber.getEditText().getText().toString();
                String password = regPassword.getEditText().getText().toString();

                UserProfileModel userProfile = new UserProfileModel(firstName, surname, email, phoneNumber, password);
                reference.child(phoneNumber).setValue(userProfile);

                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }//end on create method

    //close the keyboard when clicking off the screen
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.
                INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        return true;
    }//end on touch event method

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}//end register activity class