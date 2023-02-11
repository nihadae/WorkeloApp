package com.example.workeloapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RatingActivity extends AppCompatActivity {

    private Button reviewButton;
    private Button cancelReview;
    private TextView user;
    int i = 0;
    RatingBar simpleRatingBar;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Rating");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);
        cancelReview = findViewById(R.id.cancelButtonRating);
        reviewButton = findViewById(R.id.reviewButton);
        user = findViewById(R.id.empNameReview);
        simpleRatingBar = findViewById(R.id.ratingBar);
        user.setText(MyAdapter.employeeName);
        reviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println(MyAdapter.employeeName);
                if(simpleRatingBar.getRating() != 0.0){
                    databaseReference.child(MyAdapter.employeeName).child(String.valueOf((int)(Math.random()*(1001)))).setValue(simpleRatingBar.getRating()).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(RatingActivity.this, "The feedback has been received.", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(RatingActivity.this, "The feedback has not been received.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                finish();
            }
        });

        cancelReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(RatingActivity.this, "The feedback has not been received based on your action.", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}