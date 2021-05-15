package com.alexeyre.fixit.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;

import com.alexeyre.fixit.Constants.Constants;
import com.alexeyre.fixit.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MetricsCurrentUserInspections extends AppCompatActivity {
    //class variables
    private DatabaseReference userData;
    private PieChart pieChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_metrics_current_user_inspections);

        //hooks
        pieChart = findViewById(R.id.pieChartCurrentUserInspections);
        userData = FirebaseDatabase.getInstance().getReference().child(Constants.USERS).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(Constants.INSPECTIONS);
        userData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int count = (int) snapshot.getChildrenCount(); //get the amount of children in the database path and cast in to an integer
                setGraph(count); //pass the number of children to the graph method to set the metrics
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setGraph(int count) {
        //create data
        ArrayList<PieEntry> reports = new ArrayList<>(); //create an array of data
        reports.add(new PieEntry(count, "2021")); //add data to by chart using the number of children

        //chart properties
        PieDataSet pieDataSet = new PieDataSet(reports, "Current Users Reports");
        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        pieDataSet.setValueTextColor(Color.BLACK);
        pieDataSet.setValueTextSize(20f);

        //chart extra's
        PieData pieData = new PieData(pieDataSet);

        pieChart.setData(pieData);
        pieChart.getDescription().setEnabled(false);
        pieChart.setCenterText("No. of Reports");
        pieChart.animateY(2000);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}