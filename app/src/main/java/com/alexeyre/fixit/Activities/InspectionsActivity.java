package com.alexeyre.fixit.Activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alexeyre.fixit.Adapters.ReportListAdapter;
import com.alexeyre.fixit.Constants.Constants;
import com.alexeyre.fixit.Models.TrafficLightModel;
import com.alexeyre.fixit.Models.TrafficLightReportModel;
import com.alexeyre.fixit.R;
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
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child(Constants.COORDINATES);


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
                    databaseReference.child(key).child(Constants.INSPECTIONS).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                try {
                                    if(dataSnapshot != null && dataSnapshot.hasChildren()){
                                        TrafficLightModel trafficLightReportModel = dataSnapshot.getValue(TrafficLightModel.class);
                                        trafficLightReportModel.setkey(dataSnapshot.getKey());
                                        reportModelList.add(trafficLightReportModel);
                                    }
                                }catch (Exception e){
                                    e.printStackTrace();
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
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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