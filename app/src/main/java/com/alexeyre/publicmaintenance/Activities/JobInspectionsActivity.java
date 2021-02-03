package com.alexeyre.publicmaintenance.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alexeyre.publicmaintenance.Adapters.InspectionAdapter;
import com.alexeyre.publicmaintenance.Constants.Constants;
import com.alexeyre.publicmaintenance.Helpers.TrafficLightInspectionModel;
import com.alexeyre.publicmaintenance.Helpers.TrafficLightModel;
import com.alexeyre.publicmaintenance.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class JobInspectionsActivity extends AppCompatActivity {

    //variables
    private ArrayList<TrafficLightModel> mInspectionTemplateList;
    InspectionAdapter mInspectionAdapter;
    RecyclerView mRecyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_inspections);

        mRecyclerView = (RecyclerView) findViewById(R.id.job_inspections_recycler_view);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(JobInspectionsActivity.this, 1);
        mRecyclerView.setLayoutManager(gridLayoutManager);

//        //create an array list for the job inspections
//        mInspectionTemplateList = new ArrayList<>();
//        mInspectionAdapter = new InspectionAdapter(JobInspectionsActivity.this, mInspectionTemplateList);
//        mRecyclerView.setAdapter(mInspectionAdapter);

        //reference the database path for the coordinates
        FirebaseDatabase.getInstance().getReference().child(Constants.COORDINATES).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mInspectionTemplateList = new ArrayList<>();

                for (DataSnapshot trafficLightDataSnapshot : snapshot.getChildren()) {
                    if (trafficLightDataSnapshot != null && trafficLightDataSnapshot.hasChildren()) {
                        TrafficLightModel trafficLightModel = trafficLightDataSnapshot.getValue(TrafficLightModel.class);
                        trafficLightModel.setkey(trafficLightDataSnapshot.getKey());//We can assume all data is present
                        mInspectionTemplateList.add(trafficLightModel);
                    }
                }

                mInspectionAdapter = new InspectionAdapter(JobInspectionsActivity.this, mInspectionTemplateList);
                mRecyclerView.setAdapter(mInspectionAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("FIREBASE", "onCancelled: " + error.toString());
            }

        });

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(JobInspectionsActivity.this, MainActivity.class));
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}//end JobInspectionActivity