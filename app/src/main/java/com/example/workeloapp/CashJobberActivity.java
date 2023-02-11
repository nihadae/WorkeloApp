package com.example.workeloapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class CashJobberActivity extends AppCompatActivity {

    private BottomNavigationView bt;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash_jobber2);

        bt = findViewById(R.id.bottomNavigationViewUser);
        bt.setOnNavigationItemSelectedListener(bottomNavMethod);
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout1, new HomeFragmentUser()).commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener bottomNavMethod = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment = null;
            switch (item.getItemId()){
                case R.id.home1:
                    fragment = new HomeFragmentUser();
                    break;
                case R.id.jobs:
                    fragment = new JobsFragmentUser();
                    break;
                case R.id.inbox1:
                    fragment = new InboxFragmentUser();
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout1, fragment).commit();
            return true;
        }
    };
}