package com.alexeyre.fixit.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.alexeyre.fixit.Helpers.HomePageWidgetModel;
import com.alexeyre.fixit.R;

import java.util.ArrayList;

public class ControlsAdapter extends RecyclerView.Adapter<ControlsAdapter.ControlsViewHolder> {

    //variables
    private Context mContext;
    private ArrayList<HomePageWidgetModel> controls;

    public ControlsAdapter(Context mContext, ArrayList<HomePageWidgetModel> controls) {
        this.mContext = mContext;
        this.controls = controls;
    }

    @NonNull
    @Override
    public ControlsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View mView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.home_page_widget, viewGroup, false);
        return new ControlsViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull ControlsViewHolder holder, int position) {
        holder.setIsRecyclable(false);

        holder.icon.setImageDrawable(mContext.getResources().getDrawable(controls.get(position).getdrawable()));
        holder.title.setText(controls.get(position).gettitle());
        holder.parent.setOnClickListener(v -> {
            mContext.startActivity(new Intent(mContext, controls.get(position).getclassToOpen()));
        });
    }


    @Override
    public int getItemCount() {
        return controls.size();
    }


    static class ControlsViewHolder extends RecyclerView.ViewHolder {
        ImageView icon;
        TextView title;
        CardView parent;

        public ControlsViewHolder(@NonNull View itemView) {
            super(itemView);

            icon = itemView.findViewById(R.id.icon_iv);
            title = itemView.findViewById(R.id.title_tv);
            parent = itemView.findViewById(R.id.parent_cv);
        }
    }
}