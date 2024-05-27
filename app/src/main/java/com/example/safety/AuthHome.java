package com.example.safety;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AuthHome#newInstance} factory method to
 * create an instance of this fragment.
 */
public class  AuthHome extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FirebaseDatabase DB;
    private RecyclerView AuthPostRecycler;
    private DatabaseReference curr_user , root,root2;
    private ArrayList<AuthPostModel> list;
    private AuthPostAdapter adapter;
    private String UID;

    private  AuthMapAdapter mapAdapter;

    private ArrayList<AuthMapModel>maplist;
    private RecyclerView MapRecyclerView;



    public AuthHome() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AuthHome.
     */
    // TODO: Rename and change types and number of parameters
    public static AuthHome newInstance(String param1, String param2) {
        AuthHome fragment = new AuthHome();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_auth_home, container, false);
        UID = FirebaseAuth.getInstance().getUid();
        AuthPostRecycler = v.findViewById(R.id.ReviewRecyclerView);
        AuthPostRecycler.setLayoutManager(new LinearLayoutManager(v.getContext()));
        String UID = FirebaseAuth.getInstance().getUid();
        list = new ArrayList<>();
        adapter = new AuthPostAdapter(v.getContext(),list);
        AuthPostRecycler.setAdapter(adapter);
        root = FirebaseDatabase.getInstance().getReference().child("Posts");


        MapRecyclerView = v.findViewById(R.id.MapRecyclerView);
        LinearLayoutManager layoutManager  = new LinearLayoutManager(v.getContext() ,  LinearLayoutManager.HORIZONTAL , false);
        MapRecyclerView.setLayoutManager(layoutManager);
        maplist = new ArrayList<>();
        mapAdapter = new AuthMapAdapter(v.getContext() , maplist);
        MapRecyclerView.setAdapter(mapAdapter);

        root2 = FirebaseDatabase.getInstance().getReference().child("MapRequests");

        root2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                maplist.clear();
                for (DataSnapshot data : snapshot.getChildren()) {

                    String profile = data.child("Profile").getValue(String.class);
                    String city = data.child("City").getValue(String.class);
                    String roadName = data.child("RoadName").getValue(String.class);
                    String lng = String.valueOf(data.child("Longitude").getValue());
                    String lat = String.valueOf(data.child("Latitude").getValue());
                    String userName = data.child("UserName").getValue(String.class);
                    String status = data.child("Status").getValue(String.class);


                    AuthMapModel  modelimg = new AuthMapModel(profile, city, roadName, lat + " " + lng, userName,status);
                    maplist.add(0, modelimg);


                }
                mapAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(v.getContext(), "" + error.getDetails(), Toast.LENGTH_SHORT).show();
            }
        });



        root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for(DataSnapshot data : snapshot.getChildren())
                {
                    AuthPostModel modelimg = new AuthPostModel( data.child("PostImage").getValue(String.class) , data.child("Caption").getValue(String.class) , data.child("Profile").getValue(String.class) , data.child("UserName").getValue(String.class),data.child("Suggestions").getValue(String.class),Integer.valueOf(Objects.requireNonNull(data.child("Upvote").getValue(String.class))),Integer.valueOf(Objects.requireNonNull(data.child("Downvote").getValue(String.class))),data.child("isAccidentProne").getValue(String.class),data.child("Status").getValue(String.class),data.child("isAccidentProne").getValue(String.class),data.child("Location").getValue(String.class));
                    list.add(0,modelimg);
                }

                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(v.getContext(), ""+error.getDetails(), Toast.LENGTH_SHORT).show();
            }
        });







        return v;
    }
}