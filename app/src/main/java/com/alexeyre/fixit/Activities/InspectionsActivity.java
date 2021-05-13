package com.alexeyre.fixit.Activities;

import android.os.Bundle;
import android.widget.SearchView;

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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class InspectionsActivity extends AppCompatActivity {

    //variables
    private ArrayList<TrafficLightModel> reportModelList;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child(Constants.USERS)
            .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(Constants.INSPECTIONS);
    private SearchView searchView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspections);
        searchView = findViewById(R.id.search_field_inspections);
        searchView.setQueryHint("Search by location or timestamp ...");

        if (databaseReference != null) {
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
                    setAdapter(reportModelList);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        if (searchView != null) {
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
            if (object.getname().toLowerCase().contains(str.toLowerCase()) || object.gettimestamp().toLowerCase().contains(str.toLowerCase())) {
                list.add(object);
            }
        }

        setSearchAdapter(list);
    }

    private void setSearchAdapter(ArrayList<TrafficLightModel> list) {
        RecyclerView recyclerView = findViewById(R.id.inspection_report_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new ReportListAdapter(this, list));
    }

    private void setAdapter(ArrayList<TrafficLightModel> reportModelList) {
        RecyclerView recyclerView = findViewById(R.id.inspection_report_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new ReportListAdapter(this, reportModelList));
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }


}//end class