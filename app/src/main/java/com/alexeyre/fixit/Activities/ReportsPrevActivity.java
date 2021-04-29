package com.alexeyre.fixit.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alexeyre.fixit.Adapters.PrevReportsAdapter;
import com.alexeyre.fixit.Adapters.ReportListAdapter;
import com.alexeyre.fixit.Constants.Constants;
import com.alexeyre.fixit.Models.TrafficLightModel;
import com.alexeyre.fixit.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ReportsPrevActivity extends AppCompatActivity {
    private ArrayList<TrafficLightModel> reportModelList;
    PrevReportsAdapter prevReportsAdapter;
    private RecyclerView recyclerView;
    private DatabaseReference databaseReference;
    private String id, location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports_prev);

        //use a bundle to set the ID and Location
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            id = bundle.getBundle("bundle").getString("traffic_light_id");
            location = bundle.getBundle("bundle").getString("traffic_light_location");
            System.out.println("******************** this us bundle id " + id + "***************************** this is bundle location " + location );
            //if the bundle is not empty, pass the instance of the bundle to a method to get the data
            if (id != null) {
                getBundleInfo(id);

            } else {
                //Display error to user
                Toast.makeText(this, "There is no data in the bundle", Toast.LENGTH_SHORT).show();
                //Quit the activity
                finish();
            }
        } else {
            //Display error to user
            Toast.makeText(this, "The data was not passed in correctly", Toast.LENGTH_SHORT).show();
            //Quit the activity
            finish();
        }

       recyclerView = (RecyclerView) findViewById(R.id.prev_report_recycler_view);
        databaseReference = FirebaseDatabase.getInstance().getReference().child(Constants.COORDINATES).child(id);
        //set recycle view of list in a linear fashion
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ReportsPrevActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                reportModelList = new ArrayList<>();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    String key = ds.getKey();
                    String created_by = snapshot.child(key).child("created_by").getValue(String.class);
                    String path = snapshot.child(key).child("path").getValue(String.class);
                    if (snapshot != null && snapshot.hasChildren()) {
                        TrafficLightModel trafficLightModel = snapshot.getValue(TrafficLightModel.class);
                        trafficLightModel.setname(location);
                        trafficLightModel.setkey(id);
                        trafficLightModel.setinspection_by(created_by);
                        trafficLightModel.settimestamp(key);
                        trafficLightModel.setpath(path);
                        reportModelList.add(trafficLightModel);
                    }
                }
                prevReportsAdapter = new PrevReportsAdapter(ReportsPrevActivity.this, reportModelList);
                recyclerView.setAdapter(prevReportsAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void getBundleInfo(String bundleInfo) {
        FirebaseDatabase.getInstance().getReference().child("coordinates").child(bundleInfo).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(ReportsPrevActivity.this, TrafficLightProfileActivity.class));
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}