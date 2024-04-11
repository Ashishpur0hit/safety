package com.example.safety;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.example.safety.databinding.ActivitySignUpUserBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Objects;

public class SignUp_User extends AppCompatActivity {
    private ActivitySignUpUserBinding binding;
    private String Name ,Address,Email,Password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        binding = ActivitySignUpUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Objects.requireNonNull(getSupportActionBar()).hide();



        // --------------------------------------------------- Registering User and saving Details ------------------------------------------------------------------------//

        binding.btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Name = binding.SignupName.getText().toString();
                Email = binding.SignupEmail.getText().toString();
                Password = binding.SignUpPassword.getText().toString();
                Address = binding.SignUpAddress.getText().toString();

                if((!Name.isEmpty() ) && !Email.isEmpty() && !Password.isEmpty())
                {
                    if(Patterns.EMAIL_ADDRESS.matcher(Email).matches()  && Password.length()>=8)
                    {
                        CreateUser();
                    }
                    else if(!Patterns.EMAIL_ADDRESS.matcher(Email).matches() ) binding.SignupEmail.setError("Enter a vaild Email");
                    else binding.SignUpPassword.setError("Password should be atleast of 8 characters");
                }
                else if(Name.isEmpty())
                {
                    binding.SignupName.setError("Empty Feild are not allowed");
                }
                else if(Email.isEmpty()) binding.SignupEmail.setError("Empty Feilds are not allowed");
                else if(Address.isEmpty()) binding.SignUpAddress.setError("Empty Felids are not allowed");
                else Toast.makeText(SignUp_User.this, "Fill Details Properly", Toast.LENGTH_SHORT).show();


            }
//----------------------------------------------------------Creating User----------------------------------------------------------------------------------------------------//

            private void CreateUser() {

                FirebaseAuth auth = FirebaseAuth.getInstance();
                FirebaseDatabase DB = FirebaseDatabase.getInstance();
                final DatabaseReference[] User_ref = new DatabaseReference[1];
                final String[] UID = new String[1];
                final DatabaseReference[] curr_user = new DatabaseReference[1];

                auth.createUserWithEmailAndPassword(Email , Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(SignUp_User.this, "Registration Successfull", Toast.LENGTH_SHORT).show();
                            User_ref[0] = DB.getReference().child("Users");
                            UID[0] = Objects.requireNonNull(auth.getCurrentUser()).getUid();
                            curr_user[0] = User_ref[0].child(UID[0]);
                            StoreData();
                            Intent it = new Intent(SignUp_User.this , MainActivity.class);
                            startActivity(it);

                        }
                        else
                        {
                            Toast.makeText(SignUp_User.this, "registration Failed", Toast.LENGTH_SHORT).show();
                        }
                    }

                    //----------------------------------------------------------------------------Storing User Profile ----------------------------------------------------------------------------//
                    private void StoreData() {

                        String Profile = "";

                        HashMap<String , String> UserMap = new HashMap<>();

                        UserMap.put("UserName" , Name);
                        UserMap.put("Email" , Email);
                        UserMap.put("Password" , Password);
                        UserMap.put("Address" , Address);
                        UserMap.put("Profile" , Profile);
                        UserMap.put("Gender" , "");
                        UserMap.put("UID" , UID[0]);


                        curr_user[0].setValue(UserMap);
                        binding.SignupEmail.setText("");
                        binding.SignUpAddress.setText("");
                        binding.SignupName.setText("");
                        binding.SignUpPassword.setText("");


                    }
                });

            }
        });





//-------------------------------------//---------------------------------------------------------------------------------//
//        binding.btnSignUp.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(SignUp_User.this,MainActivity.class));
//            }
//        });
    }
}