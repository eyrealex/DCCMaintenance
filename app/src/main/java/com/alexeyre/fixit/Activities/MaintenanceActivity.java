package com.alexeyre.fixit.Activities;

import android.os.Bundle;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alexeyre.fixit.Adapters.MaintenanceAdapter;
import com.alexeyre.fixit.Constants.Constants;
import com.alexeyre.fixit.Models.MaintenanceModel;
import com.alexeyre.fixit.Models.MaintenanceReportModel;
import com.alexeyre.fixit.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MaintenanceActivity extends AppCompatActivity {

    //variables
    private ValueEventListener listener;
    private DatabaseReference maintenanceRef = FirebaseDatabase.getInstance().getReference().child("maintenance");
    private ArrayList<MaintenanceModel> maintenanceModelList = new ArrayList<>();
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child(Constants.COORDINATES);
    private DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference().child(Constants.COORDINATES);
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintenance);


        listener = maintenanceRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                maintenanceModelList.clear();
                for (DataSnapshot ds : snapshot.getChildren()){
                    for(DataSnapshot snap : ds.getChildren()){
                        MaintenanceModel maintenanceModel = new MaintenanceModel();
                        maintenanceModel.setkey(ds.getKey());
                        maintenanceModel.settimestamp(snap.getKey());
                        maintenanceModel.setcreated_by(snap.child("created_by").getValue(String.class));
                        maintenanceModel.setname(snap.child("location").getValue(String.class));
                        maintenanceModelList.add(maintenanceModel);
                    }
                }
                setAdapter(maintenanceModelList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void setAdapter(ArrayList<MaintenanceModel> maintenanceModelList) {
        RecyclerView recyclerView = findViewById(R.id.maintenance_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new MaintenanceAdapter(this, maintenanceModelList));
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(maintenanceRef != null && listener !=null){
            maintenanceRef.removeEventListener(listener);
        }
    }
}