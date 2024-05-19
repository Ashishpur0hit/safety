package com.example.safety;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
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
    private String Name, Address, Email, Password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        binding = ActivitySignUpUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Objects.requireNonNull(getSupportActionBar()).hide();

        binding.btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Name = binding.SignupName.getText().toString();
                Email = binding.SignupEmail.getText().toString();
                Password = binding.SignUpPassword.getText().toString();
                Address = binding.SignUpAddress.getText().toString();

                if ((!Name.isEmpty()) && !Email.isEmpty() && !Password.isEmpty() && !Address.isEmpty()) {
                    if (Patterns.EMAIL_ADDRESS.matcher(Email).matches() && Password.length() >= 8) {
                        createUser();
                    } else if (!Patterns.EMAIL_ADDRESS.matcher(Email).matches())
                        binding.SignupEmail.setError("Enter a valid Email");
                    else
                        binding.SignUpPassword.setError("Password should be at least 8 characters");
                } else {
                    if (Name.isEmpty())
                        binding.SignupName.setError("Empty fields are not allowed");
                    if (Email.isEmpty())
                        binding.SignupEmail.setError("Empty fields are not allowed");
                    if (Address.isEmpty())
                        binding.SignUpAddress.setError("Empty fields are not allowed");
                    Toast.makeText(SignUp_User.this, "Fill Details Properly", Toast.LENGTH_SHORT).show();
                }
            }

            private void createUser() {
                FirebaseAuth auth = FirebaseAuth.getInstance();

                auth.createUserWithEmailAndPassword(Email, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(SignUp_User.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                            String uid = Objects.requireNonNull(auth.getCurrentUser()).getUid();
                            storeData(uid);

                            Intent it = new Intent(SignUp_User.this, MainActivity.class);
                            startActivity(it);
                        } else {
                            Toast.makeText(SignUp_User.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                        }
                    }

                    private void storeData(String uid) {
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference userRef = database.getReference().child("Users").child(uid);

                        String profile = "";
                        HashMap<String, String> userMap = new HashMap<>();
                        userMap.put("UserName", Name);
                        userMap.put("Email", Email);
                        userMap.put("Password", Password);
                        userMap.put("Address", Address);
                        userMap.put("Profile", profile);
                        userMap.put("Gender", "");
                        userMap.put("UID", uid);

                        userRef.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    binding.SignupEmail.setText("");
                                    binding.SignUpAddress.setText("");
                                    binding.SignupName.setText("");
                                    binding.SignUpPassword.setText("");
                                } else {
                                    Log.d("Exception", "StoreData: " + Objects.requireNonNull(task.getException()).getMessage());
                                }
                            }
                        });
                    }
                });
            }
        });

        binding.AlreadyAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUp_User.this, MainActivity.class));
            }
        });
    }
}
