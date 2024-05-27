package com.example.safety;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.safety.databinding.ActivityIntroBinding;

import java.util.Objects;

public class Intro extends AppCompatActivity {

    private ActivityIntroBinding binding ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding =  ActivityIntroBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Objects.requireNonNull(getSupportActionBar()).hide();

        binding.LoginUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(Intro.this,MainActivity.class);
                startActivity(it);
            }
        });


        binding.AuthLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(Intro.this, AuthLogIn.class);
                startActivity(it);
            }
        });
    }
}