package com.ofekinyo.myswimmingapp.screens;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ofekinyo.myswimmingapp.R;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private TextView welcomeTextView;
    private FirebaseAuth mAuth;
    private Button loginButton, registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();

        welcomeTextView = findViewById(R.id.tvWelcome);
        loginButton = findViewById(R.id.btnLogin);
        registerButton = findViewById(R.id.btnRegister);

        // Set up button click listeners
        loginButton.setOnClickListener(v -> {
            Intent loginIntent = new Intent(MainActivity.this, Login.class);
            startActivity(loginIntent);
        });

        registerButton.setOnClickListener(v -> {
            Intent registerIntent = new Intent(MainActivity.this, Register.class);
            startActivity(registerIntent);
        });

        // Check user status
        checkUserStatus();
    }

    private void checkUserStatus() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // If a user is logged in, log them out and stay on the MainActivity
            Log.d(TAG, "User is logged in. Logging out...");
            mAuth.signOut();
        } else {
            // No user is logged in, stay on MainActivity
            Log.d(TAG, "No user logged in. Staying on MainActivity.");
        }
    }
}
