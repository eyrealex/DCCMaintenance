package com.alexeyre.fixit.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alexeyre.fixit.Adapters.LocationsListAdapter;
import com.alexeyre.fixit.Constants.Constants;
import com.alexeyre.fixit.Helpers.TrafficLightModel;
import com.alexeyre.fixit.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class LocationsListActivity extends AppCompatActivity {

    //variables
    private ArrayList<TrafficLightModel> mInspectionTemplateList;
    LocationsListAdapter locationsListAdapter;
    RecyclerView mRecyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locations_list);

        mRecyclerView = (RecyclerView) findViewById(R.id.job_inspections_recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(LocationsListActivity.this);
        mRecyclerView.setLayoutManager(linearLayoutManager);


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
                locationsListAdapter = new LocationsListAdapter(LocationsListActivity.this, mInspectionTemplateList);
                mRecyclerView.setAdapter(locationsListAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("FIREBASE", "onCancelled: " + error.toString());
            }

        });

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(LocationsListActivity.this, MainActivity.class));
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    public void map_view_btn(View view) {
        startActivity(new Intent(LocationsListActivity.this, LocationsMapActivity.class));
    }
}//end JobInspectionActivity