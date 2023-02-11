package com.example.workeloapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import java.sql.*;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText emailLogin;
    private EditText passwordLogin;
    private TextView back;
    private TextView forgot;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
// Initialize Firebase Auth
        emailLogin = findViewById(R.id.emailLogin);
        passwordLogin = findViewById(R.id.passwordLogin);
        forgot = findViewById(R.id.forgotLink);
        back = findViewById(R.id.backToMainLogin);
        mAuth = FirebaseAuth.getInstance();
        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
            }
        });
    }

    private void loginUser(){
        String email = emailLogin.getText().toString();
        String password = passwordLogin.getText().toString();

        if(TextUtils.isEmpty(email)){
            emailLogin.setError("Email cannot be empty!");
            emailLogin.requestFocus();
        }else if(TextUtils.isEmpty(password)){
            passwordLogin.setError("Password cannot be empty!");
            passwordLogin.requestFocus();
        }else{
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(LoginActivity.this, "User logged in successfully!", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(LoginActivity.this, SelectionActivity.class));
                    }else{
                        Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }
    public void onLoginPressed(View view) {
        loginUser();
    }
}