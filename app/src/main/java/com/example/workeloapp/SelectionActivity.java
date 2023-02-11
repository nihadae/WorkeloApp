package com.example.workeloapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SelectionActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection);
        mAuth = FirebaseAuth.getInstance();
    }

    public void onEmployerPressed(View view){
        Intent intent = new Intent(this, EmployerActivity.class);
        startActivity(intent);
    }

    public void onCashJobbersPressed(View view){
        Intent intent = new Intent(this, CashJobberActivity.class);
        startActivity(intent);
    }

    public void onLogOutPressed(View view){
        mAuth.signOut();
        startActivity(new Intent(SelectionActivity.this, LoginActivity.class));
    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser == null){
            startActivity(new Intent(SelectionActivity.this, LoginActivity.class));
        }
    }
}