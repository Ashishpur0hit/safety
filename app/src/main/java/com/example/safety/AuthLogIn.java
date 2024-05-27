package com.example.safety;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.example.safety.databinding.ActivityAuthLogInBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class AuthLogIn extends AppCompatActivity {
    private ActivityAuthLogInBinding binding;
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private String Email ,Password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAuthLogInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Objects.requireNonNull(getSupportActionBar()).hide();



        //----------------------------------------------------------------------------Already LogIn--------------------------------------------------------------------------//
        if(auth.getCurrentUser()!=null)
        {
            startActivity(new Intent(AuthLogIn.this, AuthNav.class));
            finish();
        }


        //-------------------------------------------------------------------LogIn button ------------------------------------------------------------------------------------//
        binding.btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Email = binding.EmployeeEmail.getText().toString();
                Password = binding.Password.getText().toString();

                if(!Email.isEmpty() && !Password.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(Email).matches() )
                {
                    LogInUser();
                }
                else Toast.makeText(AuthLogIn.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
            }

            private void LogInUser() {

                auth.signInWithEmailAndPassword(Email,Password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Toast.makeText(AuthLogIn.this, "LogIn Successfull", Toast.LENGTH_SHORT).show();
                        Intent it = new Intent(AuthLogIn.this,AuthNav.class);
                        it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(it);

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AuthLogIn.this, "LogIn Failed"+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });


        binding.NewAuth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AuthLogIn.this, authority.class));
            }
        });

    }
}