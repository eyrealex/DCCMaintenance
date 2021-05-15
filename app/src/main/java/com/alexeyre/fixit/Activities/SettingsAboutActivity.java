package com.alexeyre.fixit.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.alexeyre.fixit.R;

public class SettingsAboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_about);
    }

    public void githubLink(View view) { //open github link
        Intent gitHubIntent = new Intent("android.intent.action.VIEW", Uri.parse("https://github.com/eyrealex"));
        startActivity(gitHubIntent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}