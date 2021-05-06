package com.alexeyre.fixit.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.alexeyre.fixit.R;

import java.util.ArrayList;


public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {
    private RelativeLayout about, darkmode, language, likeapp, bugreport, contact, copyright, policy, terms;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        //hooks
        about = findViewById(R.id.about_layout);
        darkmode = findViewById(R.id.dark_mode_layout);
        language = findViewById(R.id.language_layout);
        likeapp = findViewById(R.id.like_layout);
        bugreport = findViewById(R.id.bug_layout);
        contact = findViewById(R.id.contact_layout);
        policy = findViewById(R.id.policy_layout);
        terms = findViewById(R.id.terms_layout);
        copyright = findViewById(R.id.copyright_layout);

        initComponents();
    }

    private void initComponents() {
        about.setOnClickListener(this);
        darkmode.setOnClickListener(this);
        language.setOnClickListener(this);
        likeapp.setOnClickListener(this);
        bugreport.setOnClickListener(this);
        contact.setOnClickListener(this);
        copyright.setOnClickListener(this);
        policy.setOnClickListener(this);
        terms.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.about_layout:
                break;
            case R.id.dark_mode_layout:
                break;
            case R.id.language_layout:
                break;
            case R.id.like_layout:
                Intent likeIntent = new Intent(Intent.ACTION_SEND);
                likeIntent.setType("plain/text");
                likeIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"x18108008@student.ncirl.ie"});
                likeIntent.putExtra(Intent.EXTRA_SUBJECT, "FixIt Feedback");
                likeIntent.putExtra(Intent.EXTRA_TEXT, "<Email us to give us feedback about the app and what we can do to improve>");
                startActivity(Intent.createChooser(likeIntent, ""));
                break;
            case R.id.bug_layout:
                Intent bugintent = new Intent(Intent.ACTION_SEND);
                bugintent.setType("plain/text");
                bugintent.putExtra(Intent.EXTRA_EMAIL, new String[]{"x18108008@student.ncirl.ie"});
                bugintent.putExtra(Intent.EXTRA_SUBJECT, "FixIt Bug Report");
                bugintent.putExtra(Intent.EXTRA_TEXT, "<Email us about any bugs found>");
                startActivity(Intent.createChooser(bugintent, ""));
                break;
            case R.id.copyright_layout:
                Intent copyrightIntent = new Intent("android.intent.action.VIEW", Uri.parse("https://www.termsfeed.com/live/8ef2acca-e1ee-4934-9990-f8cea8424a41"));
                startActivity(copyrightIntent);
                break;
            case R.id.contact_layout:
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("plain/text");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"x18108008@student.ncirl.ie"});
                intent.putExtra(Intent.EXTRA_SUBJECT, "FixIt Contact Support");
                intent.putExtra(Intent.EXTRA_TEXT, "<Email us to get in contact with our support team>");
                startActivity(Intent.createChooser(intent, ""));
                break;
            case R.id.policy_layout:
                Intent policyIntent = new Intent("android.intent.action.VIEW", Uri.parse("https://fixit.flycricket.io/privacy.html"));
                startActivity(policyIntent);
                break;
            case R.id.terms_layout:
                Intent termsIntent = new Intent("android.intent.action.VIEW", Uri.parse("https://fixit.flycricket.io/terms.html"));
                startActivity(termsIntent);
                break;
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}