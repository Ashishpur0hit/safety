package com.example.safety;

import static android.companion.CompanionDeviceManager.RESULT_OK;

import android.content.Intent;
import android.location.Location;
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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
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

import java.util.Arrays;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UploadFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UploadFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    CardView imagepost ;
    ImageView showimage;
    private final int req_code = 0;
    private Uri ImageUri;
    DatabaseReference Postref , userPostref;
    FirebaseAuth auth;
    String UID;



    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public UploadFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UploadFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UploadFragment newInstance(String param1, String param2) {
        UploadFragment fragment = new UploadFragment();
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
        View v =  inflater.inflate(R.layout.fragment_upload, container, false);
        imagepost = v.findViewById(R.id.cvAddphoto);
        showimage = v.findViewById(R.id.ShowImage);

        auth = FirebaseAuth.getInstance();
        UID = auth.getUid();
        Postref = FirebaseDatabase.getInstance().getReference("Posts");
        userPostref = FirebaseDatabase.getInstance().getReference("Users").child(UID);
        EditText postLoaction = v.findViewById(R.id.etLoaction);
        EditText PostRoadName = v.findViewById(R.id.etToFrom);
        EditText PostCaption = v.findViewById((R.id.etCaption));
        EditText PostSuggestion = v.findViewById(R.id.etSuggestions);
        CheckBox PostProne  = v.findViewById(R.id.cbIsAccProne);
        CardView report = v.findViewById(R.id.btnUpload);
        final String[] UserName = new String[1];
        final String[] DescProfile = new String[1];



        //-------------------------------- viewing UserName and profile-----------------------------------------------------------------------------------------//


            userPostref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists())
                    {
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






        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(ImageUri!=null)
                {
                    if(!postLoaction.getText().toString().isEmpty() && !PostRoadName.getText().toString().isEmpty() && !PostCaption.getText().toString().isEmpty())
                    {
                        String Location = String.valueOf(postLoaction.getText());
                        String RoadName = String.valueOf(PostRoadName.getText());
                        String Caption = String.valueOf(PostCaption.getText());
                        String Suggestions;
                        if(!PostSuggestion.getText().toString().isEmpty()) Suggestions=String.valueOf(PostSuggestion.getText());
                        else Suggestions="";

                        boolean isAccidentProne = false;
                        if(PostProne.isChecked()) isAccidentProne=true;

                        Toast.makeText(getContext(), "All Credentials are correct", Toast.LENGTH_SHORT).show();

                        Storeinfo(Location,RoadName,Caption,Suggestions,isAccidentProne,UserName[0],DescProfile[0]);
                    }
                    if(postLoaction.getText().toString().isEmpty()) postLoaction.setError("Location is Needed");
                    if(PostRoadName.getText().toString().isEmpty()) PostRoadName.setError(" Please Fill Road Info");
                    if(PostCaption.getText().toString().isEmpty()) PostCaption.setError("Please specify the Problem");
                }
                else Toast.makeText(getContext(), "Please select An Image", Toast.LENGTH_SHORT).show();






            }
        });


        //--------------------------------gallery intent----------------------------------------------------------------------------//


        imagepost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery = new Intent(Intent.ACTION_PICK);
                gallery.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(gallery , req_code);
            }
        });



        return v;
    }

    private void Storeinfo(String location, String roadName, String caption, String suggestions, boolean isAccidentProne, String userName, String profile) {
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
                                HashMap<String,String>Map = new HashMap<>();

                                Map.put("Loaction" , location);
                                                Map.put("RoadName" , roadName);
                                                Map.put("Caption",caption);
                                                Map.put("PostImage",imageUrl);
                                                if(isAccidentProne) Map.put("isAccidentProne","true");
                                                else Map.put("isAccidentProne","false");

                                                Map.put("Suggestions",suggestions);
                                                Map.put("Upvote","0");
                                                Map.put("Downvote","0");
                                                Map.put("Status","Not Accepted");
                                                Map.put("UserName", userName);
                                                if(profile!=null) Map.put("Profile", profile);
                                                else Map.put("Profile", "");

                                                userPostref.child("USerPosts").push().setValue(Map);
                                                Postref.push().setValue(Map);

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

        if(resultCode==RESULT_OK && data!=null && requestCode==req_code)
        {
            ImageUri= data.getData();
            Glide.with(getActivity()).load(ImageUri).into(showimage);
        }
    }
}