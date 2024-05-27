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
import java.util.Objects;

public class MapAdapter extends RecyclerView.Adapter<MapAdapter.ViewHolder>{
    private Context context;
    private ArrayList<MapreqModel> list;

    public MapAdapter (Context context , ArrayList<MapreqModel>list)
    {

        this.context = context;
        this.list = list;

    }


    @NonNull
    @Override
    public MapAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.mapcard , parent , false);
        ViewHolder postview = new MapAdapter.ViewHolder(v);
        return postview;

    }



    @Override
    public void onBindViewHolder(@NonNull MapAdapter.ViewHolder holder, int position) {
        Glide.with(context).load(list.get(position).getProfile()).into(holder.Profile);
        holder.City.setText(list.get(position).City);
        holder.Coordinate.setText(list.get(position).Coordinatel);
        holder.UserName.setText(list.get(position).UserName);
        holder.RoadName.setText(list.get(position).RoadName);

        if(Objects.equals(list.get(position).getStatus(), "Accepted"))
        {
            holder.MapStatus.setTextColor(context.getResources().getColor(R.color.green));
        }
        holder.MapStatus.setText(list.get(position).getStatus());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends  RecyclerView.ViewHolder
    {

        TextView Coordinate, UserName, City ,RoadName,MapStatus;
        ImageView Profile;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            Coordinate = itemView.findViewById(R.id.Longitude);
            Profile  = itemView.findViewById(R.id.ProfilePicMap);
            UserName = itemView.findViewById(R.id.UserNameMap) ;
            City = itemView.findViewById(R.id.etCity);
            RoadName = itemView.findViewById(R.id.RoadName);
            MapStatus = itemView.findViewById(R.id.MapStatus);

        }
    }

}
