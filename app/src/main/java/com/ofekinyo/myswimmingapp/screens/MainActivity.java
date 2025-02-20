package com.ofekinyo.myswimmingapp.screens;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ofekinyo.myswimmingapp.R;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private TextView welcomeTextView;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private Button loginButton, registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        welcomeTextView = findViewById(R.id.tvWelcome);
        loginButton = findViewById(R.id.btnLogin);
        registerButton = findViewById(R.id.btnRegister);

        loginButton.setOnClickListener(v -> {
            Intent loginIntent = new Intent(MainActivity.this, Login.class);
            startActivity(loginIntent);
        });

        registerButton.setOnClickListener(v -> {
            Intent registerIntent = new Intent(MainActivity.this, Register.class);
            startActivity(registerIntent);
        });

        // Ensure Firebase is completely ready before checking anything
   checkUserStatus();
    }

    private void checkUserStatus() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Log.d(TAG, "No user logged in. Staying on MainActivity.");
            return; // No user is logged in, stay on the login/register screen
        }

        Log.d(TAG, "User detected. Checking role..."+ currentUser.getUid());
        checkUserRole(currentUser.getUid());
    }

    private void checkUserRole(String userId) {
        databaseReference.child("Trainees").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Log.d(TAG, "User is a Trainee. Navigating to TraineePage.");
                    navigateToPage(TraineePage.class);
                } else {
                    checkTrainerRole(userId);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Database error: " + error.getMessage());
            }
        });
    }

    private void checkTrainerRole(String userId) {
        databaseReference.child("Trainers").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Log.d(TAG, "User is a Trainer. Navigating to TrainerPage.");
                    navigateToPage(TrainerPage.class);
                } else {
                    Log.d(TAG, "User role not found. Staying on MainActivity.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Database error: " + error.getMessage());
            }
        });
    }

    private void navigateToPage(Class<?> destination) {
        Intent intent = new Intent(MainActivity.this, destination);
        startActivity(intent);
        finish();
    }
}
