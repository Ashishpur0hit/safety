package com.example.safety;

import static com.example.safety.HomeFragment.ARG_PARAM1;
import static com.example.safety.HomeFragment.ARG_PARAM2;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

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

public class ProfilrFragment extends Fragment {

    private static final int REQUEST_IMAGE_PICK = 1; // Define the request code

    CircleImageView profile;
    private Uri imageUri;
    private String uid;

    public ProfilrFragment() {
        // Required empty public constructor
    }

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
            String mParam1 = getArguments().getString(ARG_PARAM1);
            String mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_profilr, container, false);

        CardView logout = v.findViewById(R.id.btnLogOut);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        profile = v.findViewById(R.id.IvProfile);
        uid = auth.getUid();

        if (uid != null) {
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(uid).child("Profile");

            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String imageUrl = dataSnapshot.getValue(String.class);
                        Glide.with(requireContext()).load(imageUrl).into(profile);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(requireContext(), "Failed to load profile image: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(getContext(), "User not logged in", Toast.LENGTH_SHORT).show();
        }

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gallery = new Intent(Intent.ACTION_GET_CONTENT);
                gallery.setType("image/*");
                startActivityForResult(Intent.createChooser(gallery, "Select Picture"), REQUEST_IMAGE_PICK);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
                startActivity(new Intent(getActivity(), MainActivity.class));
                requireActivity().finish();
            }
        });

        return v;
    }

    private void updateProfile() {
        if (imageUri != null) {
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference().child("images/" + uid + ".jpg");

            storageRef.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String imageUrl = uri.toString();
                                    DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(uid).child("Profile");
                                    userRef.setValue(imageUrl);
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
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_IMAGE_PICK && data != null) {
            imageUri = data.getData();
            Glide.with(getActivity()).load(imageUri).into(profile);
            updateProfile();
        }
    }
}
