package com.alexeyre.fixit.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.alexeyre.fixit.Activities.UserProfileActivity;
import com.alexeyre.fixit.Models.UserProfileModel;
import com.alexeyre.fixit.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.UserListViewHolder> {

    //variables
    private Context mContext;
    private ArrayList<UserProfileModel> UserList;

    public UserListAdapter(Context mContext, ArrayList<UserProfileModel> UserList) {
        this.mContext = mContext;
        this.UserList = UserList;
    }

    @NonNull
    @Override
    public UserListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View mView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.user_list_item, viewGroup, false);
        return new UserListViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull UserListViewHolder holder, int position) { //create the adapter and set the adapter with variables, create on onclick for the adapter
        holder.setIsRecyclable(false);

        //Load profile photo from Firebase storage
        if (UserList.get(position).getprofile_photo_url() != null && !UserList.get(position).getprofile_photo_url().equals("")) {
            Picasso.get().load(UserList.get(position).getprofile_photo_url()).resize(100, 100).placeholder(R.drawable.progress_animation).into(holder.profile_image);
        }

        holder.name.setText(UserList.get(position).getname());
        holder.email.setText(UserList.get(position).getemail());
        holder.phone.setText(UserList.get(position).getphone());

        holder.parent.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable("object", UserList.get(position));
            mContext.startActivity(new Intent(mContext, UserProfileActivity.class).putExtra("bundle", bundle));
        });
    }

    @Override
    public int getItemCount() {
        return UserList.size();
    }


    static class UserListViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView email;
        TextView phone;
        ImageView profile_image;
        CardView parent;

        public UserListViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name_tv);
            email = itemView.findViewById(R.id.email_tv);
            phone = itemView.findViewById(R.id.phone_tv);
            profile_image = itemView.findViewById(R.id.profile_image_iv);
            parent = itemView.findViewById(R.id.parent_cv);
        }
    }
}