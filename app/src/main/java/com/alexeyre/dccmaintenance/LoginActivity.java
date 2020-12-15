package com.alexeyre.dccmaintenance;

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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class LoginActivity extends AppCompatActivity {

    //variables
    Button loginBtn, accountBtn;
    ImageView image;
    TextView logo, desc;
    TextInputLayout loginUsername, loginPassword;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //hooks
        loginUsername = findViewById(R.id.login_username);
        loginPassword = findViewById(R.id.login_password);
        image = findViewById(R.id.login_image);
        logo = findViewById(R.id.login_text);
        desc = findViewById(R.id.login_desc);
        loginBtn = findViewById(R.id.login_btn);
        accountBtn = findViewById(R.id.login_reg_btn);

        //transfer to the login page
        accountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);

                Pair[] pairs = new Pair[7];
                pairs[0] = new Pair<View, String>(image, "app_image");
                pairs[1] = new Pair<View, String>(logo, "app_text");
                pairs[2] = new Pair<View, String>(desc, "desc_tran");
                pairs[3] = new Pair<View, String>(loginUsername, "username_tran");
                pairs[4] = new Pair<View, String>(loginPassword, "password_tran");
                pairs[5] = new Pair<View, String>(loginBtn, "btn_tran");
                pairs[6] = new Pair<View, String>(accountBtn, "account_tran");

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(LoginActivity.this, pairs);
                    startActivity(intent, options.toBundle());
                }
            }
        });

    }

    private Boolean validateUsername() {
        String value = loginUsername.getEditText().getText().toString();

        if (value.isEmpty()) {
            loginUsername.setError("Field cannot be empty");
            return false;
        } else {
            loginUsername.setError(null);
            return true;
        }
    }

    private Boolean validatePassword() {
        String value = loginPassword.getEditText().getText().toString();
        if (value.isEmpty()) {
            loginPassword.setError("Field cannot be empty");
            return false;
        } else {
            loginPassword.setError(null);
            return true;
        }
    }

    public void loginUser(View view) {
        if (!validateUsername() | !validatePassword()) {
            return;
        } else {
            isUser();
        }
    }

    private void isUser() {
        final String userName = loginUsername.getEditText().getText().toString().trim();
        final String userPassword = loginPassword.getEditText().getText().toString().trim();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query checkUser = reference.orderByChild("username").equalTo(userName);

        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    loginUsername.setError(null);

                    String passwordFromDB = dataSnapshot.child(userName).child("password").getValue(String.class);

                    if (passwordFromDB.equals(userPassword)) {

                        loginUsername.setError(null);

                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    } else {
                        loginPassword.setError("Wrong Password");
                        loginPassword.requestFocus();
                    }
                } else {
                    loginUsername.setError("No Username Found");
                    loginUsername.requestFocus();
                }
            }//end onDataChange

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }//end isUser class

    //when the back button is pressed
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }


}
