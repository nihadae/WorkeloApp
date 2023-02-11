package com.example.workeloapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {

    private TextView usernameTop;
    private Button logOutButton;
    private TextView cancelBack;
    private TextView rating;
    private FirebaseAuth mAuth;
    private String email;
    private double ratingShow;
    private ArrayList<Double> ratingList = new ArrayList<>();
    private int count;
    @SuppressLint({"MissingInflatedId", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        int atLocation = mAuth.getCurrentUser().getEmail().indexOf('@');
        email = mAuth.getCurrentUser().getEmail().substring(0,atLocation);
        setContentView(R.layout.activity_profile);
        usernameTop = (TextView) findViewById(R.id.profileName);
        logOutButton = findViewById(R.id.logoutButtonProfile);
        cancelBack = findViewById(R.id.cancelToPage);
        usernameTop.setText("Hi, " + email);
        rating = findViewById(R.id.rating);
        FirebaseDatabase.getInstance().getReference().child("Rating").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                for(DataSnapshot snapshot: task.getResult().getChildren()){
                    if (snapshot.getKey().equals(email)){
                        for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                            System.out.println(dataSnapshot.getValue());
                            ratingShow += dataSnapshot.getValue(Double.class);
                            count++;
                        }
                        rating.setText("Rating: " + (ratingShow/count) + "★");
                        return;
                    }else{
                        rating.setText("Rating: Not available.");
                    }
                }
            }
        });

        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
            }
        });

        cancelBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}