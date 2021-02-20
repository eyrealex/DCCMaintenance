package com.alexeyre.fixit.Activities;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alexeyre.fixit.Adapters.InspectionAdapter;
import com.alexeyre.fixit.Constants.Constants;
import com.alexeyre.fixit.Helpers.TrafficLightModel;
import com.alexeyre.fixit.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class InspectionsAllActivity extends AppCompatActivity {

    //variables
    private ArrayList<TrafficLightModel> mInspectionTemplateList;
    InspectionAdapter mInspectionAdapter;
    RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_traffic_light_inspections);

        //initialize variables to be used
        mRecyclerView = (RecyclerView) findViewById(R.id.job_inspections_recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(InspectionsAllActivity.this);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        //path to the traffic light locations using the "coordinates" path.
        FirebaseDatabase.getInstance().getReference().child(Constants.COORDINATES).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mInspectionTemplateList = new ArrayList<>(); //create an array list of using the template to show all of the information for each location

                for (DataSnapshot trafficLightDataSnapshot : snapshot.getChildren()) {
                    if (trafficLightDataSnapshot != null && trafficLightDataSnapshot.hasChildren()) {
                        TrafficLightModel trafficLightModel = trafficLightDataSnapshot.getValue(TrafficLightModel.class); //get the values stored in the traffic light model class
                        trafficLightModel.setkey(trafficLightDataSnapshot.getKey());//We can assume all data is present
                        mInspectionTemplateList.add(trafficLightModel); //add the data to the array list of templates
                    }
                }

                mInspectionAdapter = new InspectionAdapter(InspectionsAllActivity.this, mInspectionTemplateList);
                mRecyclerView.setAdapter(mInspectionAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("FIREBASE", "onCancelled: " + error.toString());
            }

        });

    }


}