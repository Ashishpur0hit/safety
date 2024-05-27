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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class AuthPostAdapter extends RecyclerView.Adapter<AuthPostAdapter.ViewHolder>{

    private Context context;
    private ArrayList<AuthPostModel> list;

    public AuthPostAdapter (Context context , ArrayList<AuthPostModel>list)
    {

        this.context = context;
        this.list = list;

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.auth_postcard, parent , false);
        ViewHolder postview = new AuthPostAdapter.ViewHolder(v);
        return postview;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        AuthPostModel post = list.get(position);

        holder.Caption.setText(post.getCaption());
        Glide.with(context).load(post.getPostImage()).into(holder.Image);
        holder.Name.setText(post.getName());
        Glide.with(context).load(post.getProfile()).into(holder.Profile);
        holder.Suggestions.setText(post.getSuggestions());
        if(Objects.equals(post.getStatus(), "Accepted"))
        {
            holder.Status.setTextColor(context.getResources().getColor(R.color.green));
        }
        holder.Status.setText(post.getStatus());

        holder.AcceptReq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference postRef = FirebaseDatabase.getInstance().getReference().child("Posts");
                DatabaseReference acceptRef = FirebaseDatabase.getInstance().getReference().child("AcceptedRequests");
                String UID = FirebaseAuth.getInstance().getUid();
                DatabaseReference Userref = FirebaseDatabase.getInstance().getReference().child("Users").child(UID).child("USerPosts");




                Userref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot data : snapshot.getChildren()) {
                            String postImage = data.child("PostImage").getValue(String.class);
                            String caption = data.child("Caption").getValue(String.class);






                            if (postImage != null && postImage.equals(list.get(position).getPostImage()) &&
                                    caption != null && caption.equals(list.get(position).getCaption())) {

                                String status = data.child("Status").getValue(String.class);

                                if(Objects.equals(status, "Accepted"))
                                {
                                    Toast.makeText(context, "Already Accepted", Toast.LENGTH_SHORT).show();
                                    break;
                                }



                                data.getRef().child("Status").setValue("Accepted");


                                Toast.makeText(context, "UI Updated to User", Toast.LENGTH_SHORT).show();
                                // Exit the loop after finding the post
                                break;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


                postRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot data : snapshot.getChildren()) {
                            String postImage = data.child("PostImage").getValue(String.class);
                            String caption = data.child("Caption").getValue(String.class);


                            if (postImage != null && postImage.equals(list.get(position).getPostImage()) &&
                                    caption != null && caption.equals(list.get(position).getCaption())) {

                                String status = data.child("Status").getValue(String.class);


                                if(Objects.equals(status, "Accepted")) break;

                                data.getRef().child("Status").setValue("Accepted");

                                HashMap<String, String> map = new HashMap<>();
                                map.put("Location", data.child("Location").getValue(String.class));
                                map.put("RoadName", data.child("RoadName").getValue(String.class));
                                map.put("Caption", data.child("Caption").getValue(String.class));
                                map.put("PostImage", data.child("PostImage").getValue(String.class));
                                map.put("isAccidentProne", data.child("isAccidentProne").getValue(String.class));
                                map.put("Suggestions", data.child("Suggestions").getValue(String.class));
                                map.put("Upvote", "0");
                                map.put("Downvote", "0");
                                map.put("Status", "Accepted");
                                map.put("UserName", data.child("UserName").getValue(String.class));
                                map.put("Profile", data.child("Profile").getValue(String.class));


                                acceptRef.push().setValue(map);
                                Toast.makeText(context, "Request Accepted", Toast.LENGTH_SHORT).show();
                                // Exit the loop after finding the post
                                break;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle possible errors.
                    }
                });
            }
        });
//
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends  RecyclerView.ViewHolder
    {

        TextView Caption, Name,Suggestions,Upvote,Status,AcceptReq;
        ImageView Image , Profile;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            Caption = itemView.findViewById(R.id.AuthCaption);
            Image = itemView.findViewById(R.id.AuthUploadedImage);
            Profile  = itemView.findViewById(R.id.AuthProfile);
            Name = itemView.findViewById(R.id.AuthUserName) ;
            Suggestions = itemView.findViewById(R.id.AuthSuggestions);
            Status=itemView.findViewById(R.id.AuthStatus);
            AcceptReq =itemView.findViewById(R.id.AccReq);
        }
    }
}
