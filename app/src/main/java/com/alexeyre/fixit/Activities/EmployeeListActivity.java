package com.alexeyre.fixit.Activities;

import android.os.Bundle;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alexeyre.fixit.Adapters.UserListAdapter;
import com.alexeyre.fixit.Constants.Constants;
import com.alexeyre.fixit.Models.UserProfileModel;
import com.alexeyre.fixit.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class EmployeeListActivity extends AppCompatActivity {

    //class variables
    private ArrayList<UserProfileModel> allUsersList = new ArrayList<>();
    private ValueEventListener userListListener;
    private DatabaseReference userListReference;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_list);

        //hooks
        searchView = findViewById(R.id.search_field);
        searchView.setQueryHint("Search by name, email or phone ...");
        userListReference = FirebaseDatabase.getInstance().getReference().child(Constants.USERS);

        if (userListReference != null) { //if the path for the users in the database is not null
            userListListener = new ValueEventListener() { //create a listener to perform an action
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.getValue() != null && snapshot.hasChildren()) { //if the paths snapshot value and children are present in the database
                        for (DataSnapshot eachUserSnapshot : snapshot.getChildren()) { //create an object to get the children
                            UserProfileModel userModel = eachUserSnapshot.getValue(UserProfileModel.class); //for each child inside users set the values to the user model class
                            allUsersList.add(userModel); //add the users to a list
                        }

                        setAdapter(allUsersList); //add the list to an adapter to populate templates for each user
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(EmployeeListActivity.this, "Oops, Something went wrong " + error.toString(), Toast.LENGTH_SHORT).show();
                }
            };
            userListReference.addValueEventListener(userListListener);
        }
        if (searchView != null) { //setting up search function
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
        ArrayList<UserProfileModel> list = new ArrayList<>(); //create a new list to search through
        for(UserProfileModel object : allUsersList){ //create an object to get each users values
            if(object.getname().toLowerCase().contains(str.toLowerCase()) || object.getemail().toLowerCase().contains(str.toLowerCase()) || object.getphone().toLowerCase().contains(str.toLowerCase())){
                list.add(object);
            }
        }

        setSearchAdapter(list); //add the list to an adapter to populate a template


    }

    private void setSearchAdapter(ArrayList<UserProfileModel> list) {
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new UserListAdapter(this, list));
    }

    private void setAdapter(ArrayList<UserProfileModel> allUsersList) {
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new UserListAdapter(this, allUsersList));
    }




    @Override
    protected void onStop() { //stop listening if all data is present
        super.onStop();
        if (userListReference != null && userListListener != null) {
            userListReference.removeEventListener(userListListener);
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}