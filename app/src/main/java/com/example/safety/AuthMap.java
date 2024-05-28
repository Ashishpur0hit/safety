package com.example.safety;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.transition.Transition;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class AuthMap extends Fragment implements OnMapReadyCallback {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private MapView mapView;
    private GoogleMap googleMap;
    private DatabaseReference root2;

    public AuthMap() {
        // Required empty public constructor
    }

    public static AuthMap newInstance(String param1, String param2) {
        AuthMap fragment = new AuthMap();
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

        View v = inflater.inflate(R.layout.fragment_auth_map, container, false);

        mapView = v.findViewById(R.id.authMap);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        root2 = FirebaseDatabase.getInstance().getReference("MapRequests");




        return v;
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
        LatLng newDelhi = new LatLng(28.6139, 77.2090);
        googleMap.addMarker(new MarkerOptions().position(newDelhi).title("Marker in New Delhi"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newDelhi, 10));
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        loadMarkers();
    }


    private void loadMarkers() {
        root2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                googleMap.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    try {
                        String lng = String.valueOf(data.child("Longitude").getValue());
                        String lat = String.valueOf(data.child("Latitude").getValue());
                        String city = data.child("City").getValue(String.class);
                        String roadName = data.child("RoadName").getValue(String.class);
                        String status = data.child("Status").getValue(String.class);
                        if (lat != null && lng != null) {
                            LatLng location = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));
                            if(Objects.equals(status, "Accepted"))
                            {
                                Marker marker = googleMap.addMarker(new MarkerOptions().position(location).title(city + " " + roadName).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                            }
                           else
                            {
                                Marker marker = googleMap.addMarker(new MarkerOptions().position(location).title(city + " " + roadName));
                            }



                        }
                    } catch (Exception e) {

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }





    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }





}
