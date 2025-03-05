package com.ofekinyo.myswimmingapp.screens;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

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

        // Check user status when MainActivity is started
        checkUserStatus();
    }

    private void checkUserStatus() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // User is logged in, but don't sign them out. Just handle it in Login activity.
            Log.d(TAG, "User is logged in.");
            navigateToAppropriatePage();
        }
    }

    private void navigateToAppropriatePage() {
        // Optionally, check the user type here if you want to navigate to a specific page.
        // For now, just stay on MainActivity or redirect them to the appropriate page.
    }
}
