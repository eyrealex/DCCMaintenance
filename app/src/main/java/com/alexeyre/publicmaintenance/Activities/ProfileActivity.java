package com.alexeyre.publicmaintenance.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.alexeyre.publicmaintenance.Helpers.UserProfileModel;
import com.alexeyre.publicmaintenance.R;
import com.google.firebase.auth.FirebaseAuth;


public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            UserProfileModel userModel = (UserProfileModel) bundle.getBundle("bundle").getSerializable("object");
            Log.d("TAG123", "onCreate: ");

            //Update the UI
            //((ImageView) findViewById(R.id.profile_image)).setImageDrawable(userModel);
            ((TextView) findViewById(R.id.profile_name_banner)).setText(userModel.getname());
            ((TextView) findViewById(R.id.profile_email_banner)).setText(userModel.getemail());
            ((TextView) findViewById(R.id.profile_name_tv)).setText(userModel.getname());
            ((TextView) findViewById(R.id.profile_email_tv)).setText(userModel.getemail());
            ((TextView) findViewById(R.id.profile_number_tv)).setText(userModel.getphone());


        }


    }

    public void signoutBtn(View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
    }
}