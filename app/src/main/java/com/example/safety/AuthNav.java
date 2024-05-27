package com.example.safety;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.safety.databinding.ActivityAuthNavBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Objects;

public class AuthNav extends AppCompatActivity {
    private ActivityAuthNavBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAuthNavBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Objects.requireNonNull(getSupportActionBar()).hide();

        binding.NavigationBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int id = item.getItemId();

                if(id==R.id.Requests)
                {
                    FragmentManager fm = getSupportFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.add( R.id.Container , new AuthHome());
                    ft.commit();
                }
                else if(id==R.id.Map)
                {
                    FragmentManager fm = getSupportFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.replace( R.id.Container , new AuthMap());
                    ft.commit();
                }
                else if(id==R.id.Accepted)
                {
                    FragmentManager fm = getSupportFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.replace( R.id.Container , new AccReq());
                    ft.commit();
                }else
                {
                    FragmentManager fm = getSupportFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.add( R.id.Container , new authProfile());
                    ft.commit();
                }

                return true;
            }
        });

        binding.NavigationBar.setSelectedItemId(R.id.Requests);



    }
}