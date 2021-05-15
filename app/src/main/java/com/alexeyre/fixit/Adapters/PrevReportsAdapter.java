package com.alexeyre.fixit.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.alexeyre.fixit.Activities.InspectionViewActivity;
import com.alexeyre.fixit.Activities.ReportsPrevViewActivity;
import com.alexeyre.fixit.Models.TrafficLightModel;
import com.alexeyre.fixit.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class PrevReportsAdapter extends RecyclerView.Adapter<PrevReportsViewHolder> {

    //variables
    private Context context;
    private ArrayList<TrafficLightModel> trafficLightModels = new ArrayList<>();
    private int lastPosition = -1;
    private Calendar calendar = Calendar.getInstance();
    private DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    private String getMyCurrentDateTime;


    public PrevReportsAdapter(Context context, ArrayList<TrafficLightModel> trafficLightModels) {
        this.context = context;
        this.trafficLightModels = trafficLightModels;

    }


    @NonNull
    @Override
    public PrevReportsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) { //create the adapter and set the adapter with variables, create on onclick for the adapter
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.prev_report_list_item, viewGroup, false);
        return new PrevReportsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PrevReportsViewHolder prevReportsViewHolder, int position) {
        //set the template lists with data using ID and Location from TrafficLightModel
        prevReportsViewHolder.prev_id.setText(String.format(Locale.ENGLISH, "ID: %s", trafficLightModels.get(position).getkey()).trim());
        prevReportsViewHolder.prev_location.setText(String.format(Locale.ENGLISH, "Location: %s", trafficLightModels.get(position).getname()).trim());

        //set the template lists with data when the inspection was reported from TrafficLightInspectionModel
        //if there has been no inspections previously reported, print out N/A

        if (trafficLightModels.get(position) == null) {
            prevReportsViewHolder.prev_timestamp.setText(String.format(Locale.ENGLISH, "Reported on: N/A"));
        } else {
            //convert timestamp to string date format
            String newDate = trafficLightModels.get(position).gettimestamp().trim();
            calendar.setTimeInMillis(Long.parseLong(newDate));
            getMyCurrentDateTime = formatter.format(calendar.getTime());
            prevReportsViewHolder.prev_timestamp.setText(String.format(Locale.ENGLISH, "Reported on: %s", getMyCurrentDateTime));
        }

        prevReportsViewHolder.prev_created_by.setText(String.format(Locale.ENGLISH, "Created by: %s", trafficLightModels.get(position).getcreated_by()).trim());

        //create on click for each of the items in the list
        prevReportsViewHolder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle ();
                bundle.putString("prev_key", trafficLightModels.get(position).getkey());
                bundle.putString("prev_name", trafficLightModels.get(position).getname());
                bundle.putString("prev_timestamp", trafficLightModels.get(position).gettimestamp());
                bundle.putString("prev_created", trafficLightModels.get(position).getcreated_by());
                Intent inspectionIntent = new Intent(context, ReportsPrevViewActivity.class);
                inspectionIntent.putExtra("bundle", bundle);
                context.startActivity(inspectionIntent);
            }
        });
    }

    private void setAnimation(View viewToAnimate, int position) {
        if (position > lastPosition) {

            ScaleAnimation animation = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f);
            animation.setDuration(1500);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return trafficLightModels.size();
    }

}

class PrevReportsViewHolder extends RecyclerView.ViewHolder {
    TextView prev_id, prev_location, prev_timestamp, prev_created_by;
    CardView parent;

    public PrevReportsViewHolder(@NonNull View itemView) {
        super(itemView);

        prev_id = itemView.findViewById(R.id.prev_report_id_tv);
        prev_location = itemView.findViewById(R.id.prev_report_location_tv);
        prev_timestamp = itemView.findViewById(R.id.prev_report_reported_on_tv);
        prev_created_by = itemView.findViewById(R.id.prev_report_created_by_tv);
        parent = itemView.findViewById(R.id.parent_cv);

    }


}
