package com.example.workeloapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class EmployerActivity extends AppCompatActivity {

    private BottomNavigationView bt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash_jobber);

        bt = findViewById(R.id.bottomNavigationView);
        bt.setOnNavigationItemSelectedListener(bottomNavMethod);
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new HomeFragmentCash()).commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener bottomNavMethod = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment = null;
            switch (item.getItemId()){
                case R.id.home:
                    fragment = new HomeFragmentCash();
                    break;
                case R.id.add:
                    fragment = new AddFragmentCash();
                    break;
                case R.id.inbox:
                    fragment = new InboxFragmentCash();
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, fragment).commit();
            return true;
        }
    };
}