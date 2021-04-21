package com.alexeyre.fixit.Activities;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alexeyre.fixit.Adapters.UserListAdapter;
import com.alexeyre.fixit.Models.UserProfileModel;
import com.alexeyre.fixit.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class EmployeeListActivity extends AppCompatActivity {

    private ArrayList<UserProfileModel> allUsersList = new ArrayList<>();
    private ValueEventListener userListListener;
    private DatabaseReference userListReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_list);

        //Get a list of all employee from Firebase
        userListReference = FirebaseDatabase.getInstance().getReference().child("users");
        userListListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() != null && snapshot.hasChildren()) {
                    for (DataSnapshot eachUserSnapshot : snapshot.getChildren()) {
                        UserProfileModel userModel = eachUserSnapshot.getValue(UserProfileModel.class);
                        allUsersList.add(userModel);
                    }

                    setAdapter(allUsersList);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(EmployeeListActivity.this, "Oops, Something went wrong " + error.toString(), Toast.LENGTH_SHORT).show();
            }
        };


        userListReference.addValueEventListener(userListListener);


        //Pass data into adapter and apply it to a recyclerview

        //When we click each item, it will display that user data in the user profile page
    }

    private void setAdapter(ArrayList<UserProfileModel> allUsersList) {
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new UserListAdapter(this, allUsersList));
    }

    @Override
    protected void onStop() {
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