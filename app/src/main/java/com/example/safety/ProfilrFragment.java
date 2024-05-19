package com.example.safety;

import static android.companion.CompanionDeviceManager.RESULT_OK;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfilrFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfilrFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    CircleImageView profile ;
    private Uri ImageUri;
    private String UID;

    public ProfilrFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfilrFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfilrFragment newInstance(String param1, String param2) {
        ProfilrFragment fragment = new ProfilrFragment();
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
        View v =  inflater.inflate(R.layout.fragment_profilr, container, false);

        CardView logout = v.findViewById(R.id.btnLogOut);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        profile =  v.findViewById(R.id.IvProfile);
        UID = auth.getUid();

        DatabaseReference user = FirebaseDatabase.getInstance().getReference("Users").child(UID).child("Profile");

        if(Objects.equals(user.getKey(), ""))
        {
            Toast.makeText(getContext(), "Profile is not set", Toast.LENGTH_SHORT).show();
        }
        else
        {
            user.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // Check if the profile image URL exists in the database
                    if (dataSnapshot.exists()) {
                        // Get the profile image URL
                        String imageUrl = dataSnapshot.getValue(String.class);
                        // Load the image using Glide
                        Glide.with(requireContext()).load(imageUrl).into(profile);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle any errors
                    Toast.makeText(requireContext(), "Failed to load profile image: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }



        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                        Intent gallery = new Intent(Intent.ACTION_GET_CONTENT);
                        gallery.setType("image/*");

                        startActivityForResult(Intent.createChooser(gallery, "Select Picture"), 0);


            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
                startActivity(new Intent(getActivity(),MainActivity.class));
                requireActivity().finish();
            }
        });


        return v;
    }

    private void Updateprofile() {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference().child("images").child(ImageUri+".jpg");

        storageRef.putFile(ImageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String imageUrl = uri.toString();
                                String UID = FirebaseAuth.getInstance().getUid();
                                // Save the image URL to the Realtime Database
                                DatabaseReference user = FirebaseDatabase.getInstance().getReference("Users").child(UID).child("Profile");
                                user.setValue(imageUrl);
                                Toast.makeText(getContext(), "Image uploaded successfully!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Image upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode==RESULT_OK && data!=null && requestCode==0)
        {
            ImageUri= data.getData();
            Glide.with(getActivity()).load(ImageUri).into(profile);

            Updateprofile();
        }
    }
}