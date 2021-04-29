package com.alexeyre.fixit.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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


public class InspectionsActivity extends AppCompatActivity {

    //variables
    private ArrayList<TrafficLightModel> reportModelList;
    private ReportListAdapter reportListAdapter;
    private RecyclerView recyclerView;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child(Constants.USERS)
            .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(Constants.INSPECTIONS);
    private DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference().child(Constants.COORDINATES);


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspections);

        //create hooks
        recyclerView = (RecyclerView) findViewById(R.id.inspection_report_recycler_view);

        //set recycle view of list in a linear fashion
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(InspectionsActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                reportModelList = new ArrayList<>();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    String key = ds.getKey();
                    String created_by = snapshot.child(key).child("created_by").getValue(String.class);
                    String location = snapshot.child(key).child("location").getValue(String.class);
                    String id = snapshot.child(key).child("id").getValue(String.class);
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
                reportListAdapter = new ReportListAdapter(InspectionsActivity.this, reportModelList);
                recyclerView.setAdapter(reportListAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }


}//end class