package com.alexeyre.publicmaintenance.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alexeyre.publicmaintenance.Adapters.InspectionAdapter;
import com.alexeyre.publicmaintenance.Constants.Constants;
import com.alexeyre.publicmaintenance.Helpers.TrafficLightModel;
import com.alexeyre.publicmaintenance.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TrafficLightLocationsActivity extends AppCompatActivity {

    //variables
    private ArrayList<TrafficLightModel> mInspectionTemplateList;
    InspectionAdapter mInspectionAdapter;
    RecyclerView mRecyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.taffic_light_locations);

        mRecyclerView = (RecyclerView) findViewById(R.id.job_inspections_recycler_view);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(TrafficLightLocationsActivity.this, 1);
        mRecyclerView.setLayoutManager(gridLayoutManager);


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

                mInspectionAdapter = new InspectionAdapter(TrafficLightLocationsActivity.this, mInspectionTemplateList);
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
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    public void map_view_btn(View view) {
        startActivity(new Intent(TrafficLightLocationsActivity.this, MapsActivity.class));
    }
}//end JobInspectionActivity