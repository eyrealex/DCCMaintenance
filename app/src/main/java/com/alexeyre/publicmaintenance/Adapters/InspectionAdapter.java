package com.alexeyre.publicmaintenance.Adapters;

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

import com.alexeyre.publicmaintenance.Helpers.TrafficLightModel;
import com.alexeyre.publicmaintenance.R;

import java.util.ArrayList;
import java.util.Locale;

public class InspectionAdapter extends RecyclerView.Adapter<InspectionViewHolder> {

    //variables
    private Context mContext;
    private ArrayList<TrafficLightModel> trafficLightModelList = new ArrayList<>();
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
        inspectionViewHolder.id_tv.setText(String.format(Locale.ENGLISH, "ID: %s", trafficLightModelList.get(position).getkey()));
        inspectionViewHolder.id_tv.setText(String.format(Locale.ENGLISH, "ID: %s", trafficLightModelList.get(position).getkey()));


        //load in the job inspections template from the database with an animation
        //Glide.with(mContext)
        // .load(myInspectionTemplateList.get(i).getId())
        //.into(inspectionViewHolder.mId);

//        inspectionViewHolder.mLocation.setText(myInspectionTemplateList.get(i).getLocation());
        //inspectionViewHolder.mLastInspection.setText(myInspectionTemplateList.get(i).getLast_inspec());
        //inspectionViewHolder.mNextInspection.setText(myInspectionTemplateList.get(i).getNext_inspec());
        // inspectionViewHolder.mStatus.setText(myInspectionTemplateList.get(i).getStatus());

        /*
        inspectionViewHolder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                             Intent intent = new Intent(mContext, TrafficLightProfileActivity.class);
                           //intent.putExtra("ID", myInspectionTemplateList.get(inspectionViewHolder.getAdapterPosition()).getId());
                           intent.putExtra("Location", myInspectionTemplateList.get(inspectionViewHolder.getAdapterPosition()).getLocation());
//                intent.putExtra("Last Inspection", myInspectionTemplateList.get(inspectionViewHolder.getAdapterPosition()).getLast_inspec());
                //              intent.putExtra("Next Inspection", myInspectionTemplateList.get(inspectionViewHolder.getAdapterPosition()).getNext_inspec());
                //              intent.putExtra("Status", myInspectionTemplateList.get(inspectionViewHolder.getAdapterPosition()).getStatus());
                mContext.startActivity(intent);
            }
        });

         */

        //setAnimation(inspectionViewHolder.itemView, i);
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

    TextView id_tv, template_location;

    //template variables
    //private TextView mId, mLocation, mLastInspection, mNextInspection, mStatus;
    TextView mLocation;
    CardView mCardView;

    public InspectionViewHolder(@NonNull View itemView) {
        super(itemView);

        id_tv = itemView.findViewById(R.id.id_tv);

        //template hooks
        // mId = itemView.findViewById(R.id.template_id);
//        mLocation = itemView.findViewById(R.id.template_location);
        //mLastInspection = itemView.findViewById(R.id.template_last_inspection);
        //mNextInspection = itemView.findViewById(R.id.template_next_inspection);
        //mStatus = itemView.findViewById(R.id.template_status);
    }
}


