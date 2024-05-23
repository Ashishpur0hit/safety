package com.example.safety;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class UserPostAdapter extends RecyclerView.Adapter<UserPostAdapter.ViewHolder> {


    private Context context;
    private ArrayList<UserPostModel> list;







    public UserPostAdapter (Context context , ArrayList<UserPostModel>list)
    {

        this.context = context;
        this.list = list;

    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.postcard , parent , false);
        ViewHolder postview = new ViewHolder(v);
        return postview;

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.Caption.setText(list.get(position).Caption);
        Glide.with(context).load(list.get(position).getPostImage()).into(holder.Image);
        holder.Name.setText(list.get(position).Name);
        Glide.with(context).load(list.get(position).getProfile()).into(holder.Profile);
        holder.Suggestions.setText(list.get(position).Suggestions);
        holder.Upvote.setText(String.valueOf(list.get(position).Upvote));
        holder.Status.setText(String.valueOf(list.get(position).status));


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends  RecyclerView.ViewHolder
    {

        TextView Caption, Name,Suggestions,Upvote,Status;
        ImageView Image , Profile,Heart,share;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            Caption = itemView.findViewById(R.id.caption);
            Image = itemView.findViewById(R.id.UploadedImage);
            Profile  = itemView.findViewById(R.id.ProfilePic);
            Name = itemView.findViewById(R.id.UserName) ;
            Suggestions = itemView.findViewById(R.id.Institute);
            Upvote = itemView.findViewById(R.id.by);
            Heart=itemView.findViewById(R.id.filledlike);
            share = itemView.findViewById(R.id.share);
            Status=itemView.findViewById(R.id.Status);
        }
    }
}