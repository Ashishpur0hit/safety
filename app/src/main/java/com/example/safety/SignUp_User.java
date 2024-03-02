package com.example.safety;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;

import com.example.safety.databinding.ActivitySignUpUserBinding;

public class SignUp_User extends AppCompatActivity {
    private ActivitySignUpUserBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        binding = ActivitySignUpUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUp_User.this,MainActivity.class));
            }
        });
    }
}