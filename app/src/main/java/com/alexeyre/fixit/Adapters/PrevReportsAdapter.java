package com.alexeyre.fixit.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.alexeyre.fixit.Models.TrafficLightModel;
import com.alexeyre.fixit.R;

import java.util.ArrayList;
import java.util.Locale;

public class PrevReportsAdapter extends RecyclerView.Adapter<PrevReportsViewHolder> {

    //variables
    private Context context;
    private ArrayList<TrafficLightModel> trafficLightModels = new ArrayList<>();
    private int lastPosition = -1;


    public PrevReportsAdapter(Context context, ArrayList<TrafficLightModel> trafficLightModels) {
        this.context = context;
        this.trafficLightModels = trafficLightModels;

    }


    @NonNull
    @Override
    public PrevReportsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
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
            prevReportsViewHolder.prev_timestamp.setText(String.format(Locale.ENGLISH, "Reported on: %s", trafficLightModels.get(position).gettimestamp()).trim());
        }

        prevReportsViewHolder.prev_created_by.setText(String.format(Locale.ENGLISH, "Created by: %s", trafficLightModels.get(position).getinspection_by()).trim());
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

    }


}
