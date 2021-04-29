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

import com.alexeyre.fixit.Activities.TrafficLightProfileActivity;
import com.alexeyre.fixit.Models.TrafficLightModel;
import com.alexeyre.fixit.R;

import java.util.ArrayList;
import java.util.Locale;

public class LocationsListAdapter extends RecyclerView.Adapter<LocationsListViewHolder> {

    //variables
    private Context mContext;
    private ArrayList<TrafficLightModel> trafficLightModels = new ArrayList<>();
    private int lastPosition = -1;

    public LocationsListAdapter(Context mContext, ArrayList<TrafficLightModel> trafficLightModels) {
        this.mContext = mContext;
        this.trafficLightModels = trafficLightModels;
    }

    //create a template for the recycler
    @NonNull
    @Override
    public LocationsListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View mView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.locations_list_item, viewGroup, false);
        return new LocationsListViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull LocationsListViewHolder locationsListViewHolder, int position) {

        //set the template lists with data using ID, location and Next Due
        locationsListViewHolder.id_tv.setText(String.format(Locale.ENGLISH, "ID: %s", trafficLightModels.get(position).getkey()).trim());
        locationsListViewHolder.location_tv.setText(String.format(Locale.ENGLISH, "Location: %s", trafficLightModels.get(position).getname()).trim());

        //if there is no data relating to next due, set it to N/A
        if (trafficLightModels.get(position).getnext_due_inpection() == null) {
            locationsListViewHolder.next_inspec_tv.setText(String.format(Locale.ENGLISH, "Due: N/A"));
        } else {
            locationsListViewHolder.next_inspec_tv.setText(String.format(Locale.ENGLISH, "Due: %s", trafficLightModels.get(position).getinspection().get(position).getnext_due_inpection()));
        }


        //On Click for each item
        locationsListViewHolder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Create bundle
                Bundle bundle = new Bundle();

                //Add traffic light ID to bundle
                bundle.putString("traffic_light_id", trafficLightModels.get(position).getkey()); //get id from object at the current position

                //Create intent
                Intent trafficLightIntent = new Intent(mContext, TrafficLightProfileActivity.class);

                //Add bundle to intent
                trafficLightIntent.putExtra("bundle", bundle);

                //Start Traffic Light Profile Activity
                mContext.startActivity(trafficLightIntent);
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

class LocationsListViewHolder extends RecyclerView.ViewHolder {
    TextView id_tv, location_tv, next_inspec_tv;
    CardView parent;


    public LocationsListViewHolder(@NonNull View itemView) {
        super(itemView);

        id_tv = itemView.findViewById(R.id.id_tv);
        location_tv = itemView.findViewById(R.id.location_tv);
        next_inspec_tv = itemView.findViewById(R.id.next_inspec_tv);
        parent = itemView.findViewById(R.id.parent_cv);
    }


}



