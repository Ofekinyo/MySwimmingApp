package com.ofekinyo.myswimmingapp.screens;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ofekinyo.myswimmingapp.R;

public class TrainerInfo extends AppCompatActivity {
    private TextView experienceTextView, certificationsTextView, bioTextView;
    private Button downloadCertButton;
    private DatabaseReference trainerRef;
    private String trainerId, certUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainer_info);

        // Initialize UI components
        experienceTextView = findViewById(R.id.experienceTextView);
        certificationsTextView = findViewById(R.id.certificationsTextView);
        bioTextView = findViewById(R.id.bioTextView);
        downloadCertButton = findViewById(R.id.btnDownloadCert);

        // Get Trainer ID from Intent
        trainerId = getIntent().getStringExtra("trainerId");

        // Reference to Firebase
        trainerRef = FirebaseDatabase.getInstance().getReference("Trainers").child(trainerId);

        // Fetch Trainer Data
        loadTrainerInfo();

        // Handle Certification File Download
        downloadCertButton.setOnClickListener(v -> {
            if (certUrl != null) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(certUrl));
                startActivity(browserIntent);
            } else {
                Toast.makeText(TrainerInfo.this, "No certification file found", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadTrainerInfo() {
        trainerRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String experience = snapshot.child("experience").getValue(String.class);
                    String certifications = snapshot.child("certifications").getValue(String.class);
                    String bio = snapshot.child("bio").getValue(String.class);
                    certUrl = snapshot.child("certFileUrl").getValue(String.class);

                    // Set values to UI
                    experienceTextView.setText(experience + " שנים");
                    certificationsTextView.setText(certifications);
                    bioTextView.setText(bio);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(TrainerInfo.this, "Failed to load trainer info", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
