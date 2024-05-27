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
 * Use the {@link AccReq#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccReq extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";



    private RecyclerView AcceptedRecyclerView;
    private DatabaseReference curr_user , root,root2;
    private ArrayList<AcceptedPostModel> list;
    private AcceptedPostAdapter adapter;
    private String UID;

    private  AcceptedMapAdapter mapAdapter;

    private ArrayList<AcceptedMapModel>maplist;
    private RecyclerView MapRecyclerView;







    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AccReq() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AccReq.
     */
    // TODO: Rename and change types and number of parameters
    public static AccReq newInstance(String param1, String param2) {
        AccReq fragment = new AccReq();
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
        AcceptedRecyclerView = v.findViewById(R.id.ReviewRecyclerView);
        AcceptedRecyclerView.setLayoutManager(new LinearLayoutManager(v.getContext()));

        list = new ArrayList<>();
        adapter = new AcceptedPostAdapter(v.getContext(),list);
        AcceptedRecyclerView.setAdapter(adapter);
        root = FirebaseDatabase.getInstance().getReference().child("AcceptedRequests");


        MapRecyclerView = v.findViewById(R.id.MapRecyclerView);
        LinearLayoutManager layoutManager  = new LinearLayoutManager(v.getContext() ,  LinearLayoutManager.HORIZONTAL , false);
        MapRecyclerView.setLayoutManager(layoutManager);
        maplist = new ArrayList<>();
        mapAdapter = new AcceptedMapAdapter(v.getContext() , maplist);
        MapRecyclerView.setAdapter(mapAdapter);
        root2 = FirebaseDatabase.getInstance().getReference().child("AcceptedMap");




        root2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                maplist.clear();
                for (DataSnapshot data : snapshot.getChildren()) {

                    String profile = data.child("Profile").getValue(String.class);
                    String city = data.child("City").getValue(String.class);
                    String userName = data.child("UserName").getValue(String.class);



                    AcceptedMapModel modelimg = new AcceptedMapModel(profile,userName,city);
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
                    AcceptedPostModel modelimg = new AcceptedPostModel(data.child("PostImage").getValue(String.class),data.child("UserName").getValue(String.class),data.child("Caption").getValue(String.class));
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