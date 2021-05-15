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
import com.alexeyre.fixit.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class MaintenanceActivity extends AppCompatActivity {

    //class variables
    private ValueEventListener listener;
    private DatabaseReference maintenanceRef = FirebaseDatabase.getInstance().getReference().child(Constants.MAINTENANCE).child(Constants.OPEN);
    private ArrayList<MaintenanceModel> maintenanceModelList = new ArrayList<>();
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child(Constants.COORDINATES);
    private DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference().child(Constants.COORDINATES);
    private SearchView searchView;
    private Calendar calendar = Calendar.getInstance();
    private DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    private String getMyCurrentDateTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintenance);

        //hooks
        searchView = findViewById(R.id.search_field_maintenance);
        searchView.setQueryHint("Search all fields ...");


        if(maintenanceRef != null){
            listener = maintenanceRef.addValueEventListener(new ValueEventListener() { //create a listener using the database reference and get the values
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    maintenanceModelList.clear(); //clear the maintenance list on start
                    for (DataSnapshot ds : snapshot.getChildren()){
                        for(DataSnapshot snap : ds.getChildren()){
                            MaintenanceModel maintenanceModel = new MaintenanceModel();
                            maintenanceModel.setkey(ds.getKey());
                            maintenanceModel.settimestamp(snap.getKey());
                            maintenanceModel.setcreated_by(snap.child("created_by").getValue(String.class));
                            maintenanceModel.setname(snap.child("location").getValue(String.class));
                            maintenanceModelList.add(maintenanceModel); //add the values in the database to the list
                        }
                    }
                    setAdapter(maintenanceModelList); //add the list to the adapter
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
        ArrayList<MaintenanceModel> list = new ArrayList<>(); //create a new list to search through
        for(MaintenanceModel object : maintenanceModelList){ //create an object to get each users values
            //convert timestamp to string date format
            String newDate = object.gettimestamp();
            calendar.setTimeInMillis(Long.parseLong(newDate));
            getMyCurrentDateTime = formatter.format(calendar.getTime());
            if(object.getname().toLowerCase().contains(str.toLowerCase()) || getMyCurrentDateTime.toLowerCase().contains(str.toLowerCase()) || object.getkey().toLowerCase().contains(str.toLowerCase()) || object.getcreated_by().toLowerCase().contains(str.toLowerCase())){
                list.add(object);
            }
        }

        setSearchAdapter(list); //add the list to an adapter to populate a template
    }

    private void setSearchAdapter(ArrayList<MaintenanceModel> list) {
        RecyclerView recyclerView = findViewById(R.id.maintenance_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new MaintenanceAdapter(this, list));
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
            maintenanceRef.removeEventListener(listener); // stop listening if there is values in the path
        }
    }
}