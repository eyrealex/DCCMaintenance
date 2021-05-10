package com.alexeyre.fixit.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alexeyre.fixit.Adapters.PrevReportsAdapter;
import com.alexeyre.fixit.Adapters.ReportListAdapter;
import com.alexeyre.fixit.Constants.Constants;
import com.alexeyre.fixit.Models.TrafficLightInspectionModel;
import com.alexeyre.fixit.Models.TrafficLightModel;
import com.alexeyre.fixit.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ReportsPrevActivity extends AppCompatActivity {
    private ArrayList<TrafficLightModel> reportModelList;
    private TrafficLightModel trafficLightModel;
    private PrevReportsAdapter prevReportsAdapter;
    private RecyclerView recyclerView;
    private DatabaseReference databaseReference;
    private String id, location;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports_prev);

        //hooks
        searchView = findViewById(R.id.search_field_prev_reports);
        searchView.setQueryHint("Search by employee or timestamp ...");

        //use a bundle to set the ID and Location
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            id = bundle.getBundle("bundle").getString("traffic_light_id");
            location = bundle.getBundle("bundle").getString("traffic_light_location");
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


        databaseReference = FirebaseDatabase.getInstance().getReference().child(Constants.COORDINATES).child(id).child(Constants.INSPECTIONS);

        if(databaseReference != null){
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    reportModelList = new ArrayList<>();
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        String timestamp = ds.getKey();
                        if (snapshot != null && snapshot.hasChildren()) {
                            trafficLightModel = snapshot.getValue(TrafficLightModel.class);
                            trafficLightModel.setname(location);
                            trafficLightModel.setkey(id);
                            trafficLightModel.setcreated_by(ds.child("created_by").getValue(String.class));
                            trafficLightModel.settimestamp(timestamp);
                            reportModelList.add(trafficLightModel);
                        }
                    }
                    setAdapter(reportModelList);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }if(searchView != null){
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    search(s);
                    return true;
                }
            });
        }




    }

    private void search(String str) {
        ArrayList<TrafficLightModel> list = new ArrayList<>();
        for (TrafficLightModel object : reportModelList) {
            if (object.gettimestamp().toLowerCase().contains(str.toLowerCase()) || object.getcreated_by().toLowerCase().contains(str.toLowerCase())) {
                list.add(object);
            }
        }

        setSearchAdapter(list);
    }

    private void setSearchAdapter(ArrayList<TrafficLightModel> list) {
        RecyclerView recyclerView = findViewById(R.id.prev_report_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new PrevReportsAdapter(this, list));
    }

    private void setAdapter(ArrayList<TrafficLightModel> reportModelList) {
        RecyclerView recyclerView = findViewById(R.id.prev_report_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new PrevReportsAdapter(this, reportModelList));
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
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}