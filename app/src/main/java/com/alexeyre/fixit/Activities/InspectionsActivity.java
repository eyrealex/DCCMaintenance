package com.alexeyre.fixit.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alexeyre.fixit.Adapters.ReportListAdapter;
import com.alexeyre.fixit.Constants.Constants;
import com.alexeyre.fixit.Helpers.TrafficLightModel;
import com.alexeyre.fixit.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class InspectionsActivity extends AppCompatActivity {

    //variables
    private ArrayList<TrafficLightModel> trafficLightModels;
    private ReportListAdapter reportListAdapter;
    private RecyclerView recyclerView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspections);

        //create hooks
        recyclerView = (RecyclerView) findViewById(R.id.inspection_report_recycler_view);

        //set recycle view of list in a linear fashion
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(InspectionsActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);

        FirebaseDatabase.getInstance().getReference().child(Constants.COORDINATES).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                trafficLightModels = new ArrayList<>();

                for (DataSnapshot trafficLightDataSnapthot : snapshot.getChildren()) {
                    if (trafficLightDataSnapthot != null && trafficLightDataSnapthot.hasChildren()) {
                        TrafficLightModel trafficLightModel = trafficLightDataSnapthot.getValue(TrafficLightModel.class);
                        trafficLightModel.setkey(trafficLightDataSnapthot.getKey());
                        trafficLightModels.add(trafficLightModel);
                    }
                }
                reportListAdapter = new ReportListAdapter(InspectionsActivity.this, trafficLightModels);
                recyclerView.setAdapter(reportListAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("FIREBASE", "onCancelled: " + error.toString());
            }
        });


    }//end on create

    @Override
    public void onBackPressed() {
        startActivity(new Intent(InspectionsActivity.this, MainActivity.class));
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }


}//end class