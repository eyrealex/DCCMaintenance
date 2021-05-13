package com.alexeyre.fixit.Activities;

import android.os.Bundle;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.alexeyre.fixit.Constants.Constants;
import com.alexeyre.fixit.Models.MaintenanceModel;
import com.alexeyre.fixit.Models.TrafficLightModel;
import com.alexeyre.fixit.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MaintenanceActivity extends AppCompatActivity {

    //variables
    private ArrayList<TrafficLightModel> reportModelList;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child(Constants.COORDINATES);
    private DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference().child(Constants.COORDINATES);
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintenance);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    MaintenanceModel maintenanceModel = new MaintenanceModel();
                    String key = ds.getKey();
                    maintenanceModel.setkey(key);
                    getTimestamp(key);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getTimestamp(String key) {
        databaseRef.child(key).child(Constants.INSPECTIONS).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    MaintenanceModel maintenanceModel = new MaintenanceModel();
                    String timestamp = ds.getKey();
                    maintenanceModel.settimestamp(timestamp);
                    getReports(key, timestamp);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getReports(String key, String timestamp) {
        System.out.println("This is all the keys " + key);
        System.out.println("This is all the timestamps " + timestamp);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}