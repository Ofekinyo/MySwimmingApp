package com.ofekinyo.myswimmingapp.screens;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ofekinyo.myswimmingapp.R;

public class SessionDetails extends AppCompatActivity {

    private TextView tvSessionType, tvSessionTypeValue, tvLength, tvLengthValue, tvDate, tvDateValue, tvGoals, tvGoalsValue, tvTrainerDescription, tvTrainerDescriptionValue;
    private Button btnAccept, btnDecline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session_details);

        // Initialize UI components
        tvSessionType = findViewById(R.id.tvSessionType);
        tvSessionTypeValue = findViewById(R.id.tvSessionTypeValue);
        tvLength = findViewById(R.id.tvLength);
        tvLengthValue = findViewById(R.id.tvLengthValue);
        tvDate = findViewById(R.id.tvDate);
        tvDateValue = findViewById(R.id.tvDateValue);
        tvGoals = findViewById(R.id.tvGoals);
        tvGoalsValue = findViewById(R.id.tvGoalsValue);
        tvTrainerDescription = findViewById(R.id.tvTrainerDescription);
        tvTrainerDescriptionValue = findViewById(R.id.tvTrainerDescriptionValue);
        btnAccept = findViewById(R.id.btnAccept);
        btnDecline = findViewById(R.id.btnDecline);

        // Example data - you can replace these with dynamic data (from Firebase, for example)
        tvSessionTypeValue.setText("Speed");
        tvLengthValue.setText("45 minutes");
        tvDateValue.setText("2025-01-25");
        tvGoalsValue.setText("Increase swimming speed");
        tvTrainerDescriptionValue.setText("We will focus on technique and speed improvement for this session.");

        // Accept button click listener
        btnAccept.setOnClickListener(v -> {
            // Logic for accepting the session (could be sending data to a database, etc.)
            Toast.makeText(SessionDetails.this, "Session Accepted!", Toast.LENGTH_SHORT).show();
            finish(); // Close the current activity
        });

        // Decline button click listener
        btnDecline.setOnClickListener(v -> {
            // Logic for declining the session
            Toast.makeText(SessionDetails.this, "Session Declined.", Toast.LENGTH_SHORT).show();
            finish(); // Close the current activity
        });
    }
}
