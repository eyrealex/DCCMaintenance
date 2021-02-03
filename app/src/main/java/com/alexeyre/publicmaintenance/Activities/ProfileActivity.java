package com.alexeyre.publicmaintenance.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.alexeyre.publicmaintenance.R;
import com.google.firebase.auth.FirebaseAuth;


public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


    }

    public void signoutBtn(View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
    }
}