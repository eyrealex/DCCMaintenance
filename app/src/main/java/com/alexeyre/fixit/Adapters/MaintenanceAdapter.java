package com.alexeyre.fixit.Adapters;

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
import com.alexeyre.fixit.Models.TrafficLightModel;
import com.alexeyre.fixit.R;

import java.util.ArrayList;
import java.util.Locale;

public class MaintenanceAdapter extends RecyclerView.Adapter<MaintenanceViewHolder> {

    //variables
    private Context context;
    private ArrayList<TrafficLightModel> trafficLightModels = new ArrayList<>();
    private int lastPosition = -1;

    public MaintenanceAdapter(Context context, ArrayList<TrafficLightModel> trafficLightModels) {
        this.context = context;
        this.trafficLightModels = trafficLightModels;
    }


    @NonNull
    @Override
    public MaintenanceViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.inspections_list_item, viewGroup, false);
        return new MaintenanceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MaintenanceViewHolder maintenanceViewHolder, int position) {

        //set the template lists with data using ID and Location from TrafficLightModel
        maintenanceViewHolder.id_tv.setText(String.format(Locale.ENGLISH, "ID: %s", trafficLightModels.get(position).getkey()).trim());
        maintenanceViewHolder.location_tv.setText(String.format(Locale.ENGLISH, "Location: %s", trafficLightModels.get(position).getname()).trim());

        //set the template lists with data when the inspection was reported from TrafficLightInspectionModel
        //if there has been no inspections previously reported, print out N/A

        if (trafficLightModels.get(position) == null) {
            maintenanceViewHolder.reported_on_tv.setText(String.format(Locale.ENGLISH, "Reported on: N/A"));
        } else {
            maintenanceViewHolder.reported_on_tv.setText(String.format(Locale.ENGLISH, "Reported on: %s", trafficLightModels.get(position).gettimestamp()).trim());
        }

        maintenanceViewHolder.created_by.setText(String.format(Locale.ENGLISH, "Created by: %s", trafficLightModels.get(position).getinspection_by()).trim());

        //create onclick for each list item
        maintenanceViewHolder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle ();
                bundle.putString("inspection_key", trafficLightModels.get(position).getkey());
                bundle.putString("inspection_timestamp", trafficLightModels.get(position).gettimestamp());
                bundle.putString("inspection_path", trafficLightModels.get(position).getpath());
                Intent inspectionIntent = new Intent(context, InspectionViewActivity.class);
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

class MaintenanceViewHolder extends RecyclerView.ViewHolder {
    TextView id_tv, location_tv, reported_on_tv, created_by;
    CardView parent;


    public MaintenanceViewHolder(@NonNull View itemView) {
        super(itemView);

        id_tv = itemView.findViewById(R.id.id_tv);
        location_tv = itemView.findViewById(R.id.location_tv);
        reported_on_tv = itemView.findViewById(R.id.reported_on_tv);
        created_by = itemView.findViewById(R.id.created_by_tv);
        parent = itemView.findViewById(R.id.parent_cv);
    }
}
