package com.example.safety;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AcceptedMapAdapter extends RecyclerView.Adapter<AcceptedMapAdapter.ViewHolder>{

    Context context;
    ArrayList<AcceptedMapModel>list;


    public AcceptedMapAdapter(Context context, ArrayList<AcceptedMapModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.accept_map , parent , false);
        AcceptedMapAdapter.ViewHolder postview = new AcceptedMapAdapter.ViewHolder(v);
        return postview;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context).load(list.get(position).getProfile()).into(holder.Profile);
        holder.CityName.setText(list.get(position).getCity());
        holder.UserName.setText(list.get(position).getUserName());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends  RecyclerView.ViewHolder
    {
        CircleImageView Profile;
        TextView UserName , CityName;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

           Profile  = itemView.findViewById(R.id.AccmapProfile);
           UserName = itemView.findViewById(R.id.accmapUser);
           CityName = itemView.findViewById(R.id.accmapCity);

        }
    }

}
