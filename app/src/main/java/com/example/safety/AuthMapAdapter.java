package com.example.safety;

import android.content.Context;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class AuthMapAdapter extends RecyclerView.Adapter<AuthMapAdapter.ViewHolder>{
    private Context context;
    private ArrayList<AuthMapModel> list;

    public AuthMapAdapter(Context context, ArrayList<AuthMapModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.auth_mapcard , parent , false);
        AuthMapAdapter.ViewHolder postview = new AuthMapAdapter.ViewHolder(v);
        return postview;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder,   int position) {
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



        holder.AccReq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatabaseReference MapRequests = FirebaseDatabase.getInstance().getReference().child("MapRequests");
                String UID = FirebaseAuth.getInstance().getUid();
                DatabaseReference AcceptedMap = FirebaseDatabase.getInstance().getReference().child("AcceptedMap");
                DatabaseReference Usermap = FirebaseDatabase.getInstance().getReference().child("Users").child(UID).child("MapReq");




                Usermap.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot data : snapshot.getChildren())
                        {



                            String coordinate  =  data.child("Latitude").getValue(String.class)+" "+data.child("Longitude").getValue(String.class);

                            if(Objects.equals(list.get(position).getCoordinatel(), coordinate))
                            {

                                Toast.makeText(context, "Found Req", Toast.LENGTH_SHORT).show();
                                String status = data.child("Status").getValue(String.class);

                                if(Objects.equals(status, "Accepted"))
                                {
                                    Toast.makeText(context, "Already Accepted", Toast.LENGTH_SHORT).show();

                                    break;
                                }

                                data.getRef().child("Status").setValue("Accepted");
                                Toast.makeText(context, "Ui Updated On User", Toast.LENGTH_SHORT).show();
                                break;
                            }
                    }}

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(context, "Cancelled", Toast.LENGTH_SHORT).show();
                    }
                });
//
//
                MapRequests.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {



                        for(DataSnapshot data : snapshot.getChildren())
                        {



                            String coordinate  =  data.child("Latitude").getValue(String.class)+" "+data.child("Longitude").getValue(String.class);

                            if(Objects.equals(list.get(position).getCoordinatel(), coordinate))
                            {

                                String status = data.child("Status").getValue(String.class);

                                if(Objects.equals(status, "Accepted"))
                                {
                                    Toast.makeText(context, "Already Accepted", Toast.LENGTH_SHORT).show();

                                    break;
                                }


                                HashMap<String, String> map = new HashMap<>();
                                map.put("Latitude", data.child("Latitude").getValue(String.class));
                                map.put("City", data.child("City").getValue(String.class));
                                map.put("RoadName", data.child("RoadName").getValue(String.class));
                                map.put("Longitude", data.child("Longitude").getValue(String.class));
                                map.put("Upvote", "0");
                                map.put("Downvote", "0");
                                map.put("Status", "Accepted");
                                map.put("UserName", data.child("UserName").getValue(String.class));
                                map.put("Profile", data.child("Profile").getValue(String.class));


                                AcceptedMap.push().setValue(map);

                                data.getRef().child("Status").setValue("Accepted");
                                Toast.makeText(context, "Accpted", Toast.LENGTH_SHORT).show();
                                break;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(context, "Cancelled", Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });






    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends  RecyclerView.ViewHolder
    {

        TextView Coordinate, UserName, City ,RoadName,MapStatus;
        ImageView Profile,AccReq;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            Coordinate = itemView.findViewById(R.id.Longitude);
            Profile  = itemView.findViewById(R.id.ProfilePicMap);
            UserName = itemView.findViewById(R.id.UserNameMap) ;
            City = itemView.findViewById(R.id.etCity);
            RoadName = itemView.findViewById(R.id.RoadName);
            AccReq = itemView.findViewById(R.id.AccMapReq);
            MapStatus = itemView.findViewById(R.id.MapStatus);
        }
    }
}
