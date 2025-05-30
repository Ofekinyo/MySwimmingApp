package com.ofekinyo.myswimmingapp.screens;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

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
import com.ofekinyo.myswimmingapp.base.BaseActivity;

public class About extends BaseActivity {

    private FirebaseAuth mAuth;
    private Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        setupToolbar("אודות");

        mAuth = FirebaseAuth.getInstance();

        // Initialize Back Button
        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> navigateToCorrectPage());
    }

    private void navigateToCorrectPage() {
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            String userId = currentUser.getUid();

            btnBack.setOnClickListener(v -> {
                DatabaseReference tutorRef = FirebaseDatabase.getInstance().getReference("Tutors").child(userId);

                tutorRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            // User is a tutor
                            Intent intent = new Intent(About.this, TutorPage.class);
                            startActivity(intent);
                            finish(); // Optional: if you want to close the current activity
                        } else {
                            // User is not a tutor, assume swimmer
                            Intent intent = new Intent(About.this, SwimmerPage.class);
                            startActivity(intent);
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(About.this, "Database error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            });
        }
    }

    private String getUserType(String userId) {
        // Your logic to check the user type (tutor or swimmer)
        // This could be a database lookup in Firebase or some other method.
        return "tutor"; // Placeholder - replace with actual implementation
    }
}
