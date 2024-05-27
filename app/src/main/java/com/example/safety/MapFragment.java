package com.example.safety;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.INTERNET;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;

import com.google.android.gms.location.LocationServices;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Executor;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMapClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    MapView mapView;
    GoogleMap googleMap;
    private MarkerOptions marker;
    String Longitude, Latitude;
    EditText city, roadname, longitude, latitude;
    CardView Report;

    DatabaseReference MapReq;
    private FusedLocationProviderClient fusedLocationClient;

    String userName, profile;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MapFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MapFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MapFragment newInstance(String param1, String param2) {
        MapFragment fragment = new MapFragment();
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
        View v = inflater.inflate(R.layout.fragment_map, container, false);


        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());
        mapView = v.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync((OnMapReadyCallback) this);
        city = v.findViewById(R.id.etCity);
        longitude = v.findViewById(R.id.etLongitude);
        latitude = v.findViewById(R.id.etlatitude);
        roadname = v.findViewById(R.id.etRoadName);
        marker = new MarkerOptions();
        Report = v.findViewById(R.id.btnReport);
        MapReq = FirebaseDatabase.getInstance().getReference("MapRequests");
        String UID = FirebaseAuth.getInstance().getUid();
        DatabaseReference userMapReq = FirebaseDatabase.getInstance().getReference("Users").child(UID);
        final String[] UserName = new String[1];
        final String[] DescProfile = new String[1];


        userMapReq.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    UserName[0] = String.valueOf(snapshot.child("UserName").getValue());
                    DescProfile[0] = String.valueOf(snapshot.child("Profile").getValue());

                    Toast.makeText(getContext(), "Found Details", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Unable To get Name and Profile", Toast.LENGTH_SHORT).show();
            }
        });


//----------------------------------------------------------------------------------------------------------------------------------------//
        Report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!longitude.getText().toString().isEmpty() & !roadname.getText().toString().isEmpty() & !city.getText().toString().isEmpty() & !latitude.getText().toString().isEmpty()) {
                    HashMap<String, String> Map = new HashMap<>();
                    Map.put("Latitude", latitude.getText().toString());
                    Map.put("RoadName", roadname.getText().toString());
                    Map.put("Longitude", longitude.getText().toString());
                    Map.put("City", city.getText().toString());
                    Map.put("Upvote", "0");
                    Map.put("Downvote", "0");
                    Map.put("Status", "Not Accepted");
                    Map.put("UserName", UserName[0]);
                    if (DescProfile[0] != null) Map.put("Profile", DescProfile[0]);
                    else Map.put("Profile", "");

                    MapReq.push().setValue(Map);
                    userMapReq.child("MapReq").push().setValue(Map);

                    longitude.setText("");
                    latitude.setText("");
                    city.setText("");
                    roadname.setText("");

                    Toast.makeText(getContext(), "Request Stored", Toast.LENGTH_SHORT).show();

                } else
                    Toast.makeText(getContext(), "Details are not filled Correctly", Toast.LENGTH_SHORT).show();
            }
        });


        return v;
    }


    public void onMapReady(GoogleMap map) {
        googleMap = map;
        // Now you can customize the map if needed
        // For example:
        // googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        googleMap.setOnMapClickListener((GoogleMap.OnMapClickListener) this);

        // Add a marker in New Delhi and move the camera


        if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Toast.makeText(getContext(), "Give Location Permission", Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions((Activity) getContext(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            return;


        }
        googleMap.setMyLocationEnabled(true);

        fusedLocationClient.getLastLocation().addOnSuccessListener((getActivity()), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object
                            LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
                            googleMap.addMarker(new MarkerOptions().position(userLocation).title("You are here"));
                            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 13));
                        }
                    }
                });
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, reload the map
                onMapReady(googleMap);
            }
        }
    }

    @Override
    public void onMapClick(@NonNull LatLng latLng) {
        googleMap.clear();
        marker.position(latLng);
        googleMap.addMarker(marker);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10)); // Zoom level 10
        Longitude = String.valueOf(latLng.longitude);
        Latitude = String.valueOf(latLng.latitude);


        longitude.setText(Longitude);
        latitude.setText(Latitude);
        if(ActivityCompat.checkSelfPermission(requireActivity(), ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireActivity(),INTERNET) == PackageManager.PERMISSION_GRANTED) {
            Geocoder geocoder = new Geocoder(requireActivity(), Locale.getDefault());
            List<Address> addresses;

            try {
                addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                assert addresses != null;
                if (!addresses.isEmpty()) {
                    // Get the first address from the list
                    Address address = addresses.get(0);
                    String cityName = address.getLocality(); // Get the city name
                    String roadName = address.getThoroughfare(); // Get the road name

                    // If road name is null, use locality or sub locality
                    if (roadName == null) {
                        roadName = address.getSubLocality();
                        if (roadName == null) {
                            roadName = cityName;
                        }
                    }

                    // Update UI or display the road name and city name
                    roadname.setText(roadName);
                    city.setText(cityName);
                } else {
                    // No address found
                    Toast.makeText(getActivity(), "Address not found", Toast.LENGTH_SHORT).show();
                }
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        else Toast.makeText(getActivity(), "permission", Toast.LENGTH_SHORT).show();

//        String message = "Clicked coordinates: " + latLng.latitude + ", " + latLng.longitude;
//        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}