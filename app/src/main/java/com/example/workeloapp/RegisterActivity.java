package com.example.workeloapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText usernameReg;
    private EditText passwordReg;
    private EditText emailReg;
    private EditText phoneNumReg;
    private TextView back;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        usernameReg = findViewById(R.id.usernameReg);
        passwordReg = findViewById(R.id.passwordReg);
        emailReg = findViewById(R.id.emailReg);
        phoneNumReg = findViewById(R.id.phoneNumReg);
        back = findViewById(R.id.backToMainReg);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
            }
        });
        mAuth = FirebaseAuth.getInstance();
    }

    private void createUser(){
        String email = emailReg.getText().toString();
        String password = passwordReg.getText().toString();
        String username = usernameReg.getText().toString();
        String phoneNum = phoneNumReg.getText().toString();

        if(TextUtils.isEmpty(email)){
            emailReg.setError("Email cannot be empty!");
            emailReg.requestFocus();
        }else if(TextUtils.isEmpty(password)){
            passwordReg.setError("Password cannot be empty!");
            passwordReg.requestFocus();
        }else if(TextUtils.isEmpty(phoneNum)){
            phoneNumReg.setError("Phone Number cannot be empty!");
            phoneNumReg.requestFocus();
        }else if(TextUtils.isEmpty(username)){
            usernameReg.setError("Username cannot be empty!");
            usernameReg.requestFocus();
        }else {
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(RegisterActivity.this, "User registered successfuly.", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                    }else{
                        Toast.makeText(RegisterActivity.this, "Registration failed. Due to: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    public void onRegisterPressed(View view){
        createUser();
    }
}