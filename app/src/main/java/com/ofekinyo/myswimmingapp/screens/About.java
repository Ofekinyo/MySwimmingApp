package com.ofekinyo.myswimmingapp.screens;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ofekinyo.myswimmingapp.R;

public class About extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        mAuth = FirebaseAuth.getInstance();

        // Initialize Back Button
        Button btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> navigateToCorrectPage());
    }

    private void navigateToCorrectPage() {
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            // Check if the user is a trainer or trainee (Assume there is a field to distinguish them)
            String userType = getUserType(currentUser.getUid()); // Replace with your method to check the user type

            if ("trainer".equals(userType)) {
                // Navigate to TrainerPage
                Intent intent = new Intent(About.this, TrainerPage.class);
                startActivity(intent);
            } else {
                // Navigate to TraineePage
                Intent intent = new Intent(About.this, TraineePage.class);
                startActivity(intent);
            }
        }
    }

    private String getUserType(String userId) {
        // Your logic to check the user type (trainer or trainee)
        // This could be a database lookup in Firebase or some other method.
        return "trainer"; // Placeholder - replace with actual implementation
    }
}
