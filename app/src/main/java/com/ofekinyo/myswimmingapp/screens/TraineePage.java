package com.ofekinyo.myswimmingapp.screens;

import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ofekinyo.myswimmingapp.R;
import com.ofekinyo.myswimmingapp.utils.SharedPreferencesUtil;

public class TraineePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainee_page);

        // Get button references
        Button btnTrainersList = findViewById(R.id.btnTrainersList);
        Button btnAllSessions = findViewById(R.id.btnAllSessions);
        Button btnSessionDetails = findViewById(R.id.btnSessionDetails);
        Button btnLogout = findViewById(R.id.btnLogout);
        Button btnAbout = findViewById(R.id.btnAbout);
        Button btnAdminPage = findViewById(R.id.btnAdminPage); // Admin Page button

        // Set button click listeners
        btnTrainersList.setOnClickListener(v -> {
            Intent intent = new Intent(TraineePage.this, TrainersList.class);
            startActivity(intent);
        });

        btnAllSessions.setOnClickListener(v -> {
            Intent intent = new Intent(TraineePage.this, AllSessions.class);
            startActivity(intent);
        });

        btnSessionDetails.setOnClickListener(v -> {
            Intent intent = new Intent(TraineePage.this, SessionDetails.class);
            startActivity(intent);
        });

        btnLogout.setOnClickListener(v -> {
            SharedPreferencesUtil.signOutUser(TraineePage.this);
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(TraineePage.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        btnAbout.setOnClickListener(v -> {
            Intent intent = new Intent(TraineePage.this, About.class);
            startActivity(intent);
        });

        // Navigate to Admin Page with UID check
        btnAdminPage.setOnClickListener(v -> {
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            if (currentUser != null) {
                String userId = currentUser.getUid();
                String adminUid = "AdminTrainee";  // Your predefined admin UID

                // Log the current UID and admin UID for debugging
                Log.d("AdminCheck", "User UID: " + userId);
                Log.d("AdminCheck", "Admin UID: " + adminUid);

                // Compare the current user ID with the admin ID
                if (userId.equals(adminUid)) {
                    // If the user is the admin, allow access to the admin page
                    Intent intent = new Intent(TraineePage.this, AdminPage.class);
                    startActivity(intent);
                } else {
                    // Show an error message if the user is not the admin
                    Toast.makeText(TraineePage.this, "You are not authorized to access the Admin page", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
