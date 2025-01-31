package com.ofekinyo.myswimmingapp.screens;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import com.ofekinyo.myswimmingapp.R;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // List of Trainers Button
        Button trainersButton = findViewById(R.id.btnTrainers);
        trainersButton.setOnClickListener(v -> {
            // Launch List of Trainers Activity
            Intent trainersIntent = new Intent(HomeActivity.this, TrainersList.class);
            startActivity(trainersIntent);
        });

        // Sessions Page View Button
        Button sessionsButton = findViewById(R.id.btnSessions);
        sessionsButton.setOnClickListener(v -> {
            // Launch Sessions Page View Activity
            Intent sessionsIntent = new Intent(HomeActivity.this, AllSessions.class);
            startActivity(sessionsIntent);
        });

        // Sending Requests Button
        Button sendingRequestsButton = findViewById(R.id.btnSendingRequests);
        sendingRequestsButton.setOnClickListener(v -> {
            // Launch Sending Requests Activity
            Intent sendingRequestsIntent = new Intent(HomeActivity.this, SessionDetails.class);
            startActivity(sendingRequestsIntent);
        });

        // Logout Button
        Button logoutButton = findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(v -> {
            // Handle logout logic here
        });
    }
}
