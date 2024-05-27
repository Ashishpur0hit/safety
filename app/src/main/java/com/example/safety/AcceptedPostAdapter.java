package com.example.safety;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class AcceptedPostAdapter extends RecyclerView.Adapter<AcceptedPostAdapter.ViewHolder>{

    Context context;
    ArrayList<AcceptedPostModel>list;

    public AcceptedPostAdapter(Context context, ArrayList<AcceptedPostModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.accept_post , parent , false);
        AcceptedPostAdapter.ViewHolder postview = new AcceptedPostAdapter.ViewHolder(v);
        return postview;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context).load(list.get(position).getPostImage()).into(holder.PostImage);
        holder.USerNAme.setText(list.get(position).getUserName());
        holder.caption.setText(list.get(position).getCaption());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends  RecyclerView.ViewHolder
    {

        ImageView PostImage ;
        TextView USerNAme , caption;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            PostImage = itemView.findViewById(R.id.accImage);
            USerNAme = itemView.findViewById(R.id.AcceptedUserName);
            caption=itemView.findViewById(R.id.AcceptedCaption);


        }
    }


}
