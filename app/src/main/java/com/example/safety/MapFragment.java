package com.example.safety;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.INTERNET;

import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback,GoogleMap.OnMapClickListener{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    MapView mapView;
    GoogleMap googleMap;
    private MarkerOptions marker;
    String Longitude , Latitude;
    EditText city , roadname , longitude , latitude;
    CardView imagepost;

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
        View v =  inflater.inflate(R.layout.fragment_map, container, false);





        mapView = v.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync((OnMapReadyCallback) this);
        city = v.findViewById(R.id.etCity);
        longitude = v.findViewById(R.id.etLongitude);
        latitude = v.findViewById(R.id.etlatitude);
        roadname = v.findViewById(R.id.etRoadName);
        marker = new MarkerOptions();






        return v;
    }



    public void onMapReady(GoogleMap map) {
        googleMap = map;
        // Now you can customize the map if needed
        // For example:
        // googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        googleMap.setOnMapClickListener((GoogleMap.OnMapClickListener) this);

        // Add a marker in New Delhi and move the camera
        LatLng newDelhi = new LatLng(28.6139, 77.2090);
        googleMap.addMarker(new MarkerOptions().position(newDelhi).title("Marker in New Delhi"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newDelhi, 10)); // Zoom level 10

        // Optional: Enable zoom controls
        googleMap.getUiSettings().setZoomControlsEnabled(true);
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