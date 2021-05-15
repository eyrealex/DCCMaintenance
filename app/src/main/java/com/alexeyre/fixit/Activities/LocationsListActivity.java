package com.alexeyre.fixit.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alexeyre.fixit.Adapters.LocationsListAdapter;
import com.alexeyre.fixit.Adapters.ReportListAdapter;
import com.alexeyre.fixit.Constants.Constants;
import com.alexeyre.fixit.Models.TrafficLightModel;
import com.alexeyre.fixit.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class LocationsListActivity extends AppCompatActivity {

    //variables
    private ArrayList<TrafficLightModel> trafficLightModels;
    private SearchView searchView;
    private DatabaseReference databaseReference =  FirebaseDatabase.getInstance().getReference().child(Constants.COORDINATES);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locations_list);

        //hooks
        searchView = findViewById(R.id.search_field_locations);
        searchView.setQueryHint("Search by id ...");

        if(databaseReference != null){
            //reference the database path for the coordinates
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    trafficLightModels = new ArrayList<>();

                    for (DataSnapshot trafficLightDataSnapshot : snapshot.getChildren()) {
                        try{
                            if (trafficLightDataSnapshot != null && trafficLightDataSnapshot.hasChildren()) {
                                TrafficLightModel trafficLightModel = trafficLightDataSnapshot.getValue(TrafficLightModel.class);
                                trafficLightModel.setkey(trafficLightDataSnapshot.getKey());//We can assume all data is present
                                trafficLightModels.add(trafficLightModel);


                            }
                        }catch (Exception e){

                        }

                    }
                    setAdapter(trafficLightModels);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.d("FIREBASE", "onCancelled: " + error.toString());
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
        for (TrafficLightModel object : trafficLightModels) {
            if (object.getkey().toLowerCase().contains(str.toLowerCase())) {
                list.add(object);
            }
        }

        setSearchAdapter(list);
    }

    private void setSearchAdapter(ArrayList<TrafficLightModel> list) {
        RecyclerView recyclerView = findViewById(R.id.job_inspections_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new LocationsListAdapter(this, list));
    }

    private void setAdapter(ArrayList<TrafficLightModel> trafficLightModels) {
        RecyclerView recyclerView = findViewById(R.id.job_inspections_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new LocationsListAdapter(this, trafficLightModels));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    public void map_view_btn(View view) {
        startActivity(new Intent(LocationsListActivity.this, LocationsMapActivity.class));
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}//end JobInspectionActivity