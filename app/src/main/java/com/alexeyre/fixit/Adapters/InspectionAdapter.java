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
import com.alexeyre.fixit.Helpers.TrafficLightModel;
import com.alexeyre.fixit.R;

import java.util.ArrayList;
import java.util.Locale;

public class InspectionAdapter extends RecyclerView.Adapter<InspectionViewHolder> {

    //variables
    private Context mContext;
    ArrayList<TrafficLightModel> trafficLightModelList = new ArrayList<>();
    private int lastPosition = -1;

    public InspectionAdapter(Context mContext, ArrayList<TrafficLightModel> trafficLightModelList) {
        this.mContext = mContext;
        this.trafficLightModelList = trafficLightModelList;
    }

    //create a template for the recycler
    @NonNull
    @Override
    public InspectionViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View mView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.inspection_list_item, viewGroup, false);
        return new InspectionViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull InspectionViewHolder inspectionViewHolder, int position) {
        inspectionViewHolder.id_tv.setText(String.format(Locale.ENGLISH, "ID: %s", trafficLightModelList.get(position).getkey()).trim());
        inspectionViewHolder.location_tv.setText(String.format(Locale.ENGLISH, "Location: %s", trafficLightModelList.get(position).getname()).trim());
        inspectionViewHolder.next_inspec_tv.setText(String.format(Locale.ENGLISH, "Due: %s", trafficLightModelList.get(position).getname()).trim());

        //On Click for each item
        inspectionViewHolder.parent.setOnClickListener(v -> {
            //Create bundle
            Bundle bundle = new Bundle();

            //Add traffic light ID to bundle
            bundle.putString("traffic_light_id", trafficLightModelList.get(position).getkey()); //get id from object at the current position

            //Create intent
            Intent trafficLightIntent = new Intent(mContext, TrafficLightProfileActivity.class);

            //Add bundle to intent
            trafficLightIntent.putExtra("bundle", bundle);

            //Start Traffic Light Profile Activity
            mContext.startActivity(trafficLightIntent);
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
        return trafficLightModelList.size();
    }
}

class InspectionViewHolder extends RecyclerView.ViewHolder {

    TextView id_tv, location_tv, next_inspec_tv;
    CardView parent;


    public InspectionViewHolder(@NonNull View itemView) {
        super(itemView);

        id_tv = itemView.findViewById(R.id.id_tv);
        location_tv = itemView.findViewById(R.id.location_tv);
        next_inspec_tv = itemView.findViewById(R.id.next_inspec_tv);
        parent = itemView.findViewById(R.id.parent_cv);
    }
}


