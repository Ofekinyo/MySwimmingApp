package com.ofekinyo.myswimmingapp.screens;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.ofekinyo.myswimmingapp.R;
import com.ofekinyo.myswimmingapp.models.Schedule;
import com.ofekinyo.myswimmingapp.utils.SharedPreferencesUtil;

public class TrainerPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainer_page);

        // Get button references
        Button btnSchedule = findViewById(R.id.btnSchedule);
        Button btnMySessions = findViewById(R.id.btnMySessions);
        Button btnSessionRequests = findViewById(R.id.btnSessionRequests); // Updated button ID
        Button btnLogout = findViewById(R.id.btnLogout);
        Button btnAbout = findViewById(R.id.btnAbout);
        Button btnAdminPage = findViewById(R.id.btnAdminPage); // Admin Page button

        // Set button click listeners
        btnSchedule.setOnClickListener(v -> {
            Intent intent = new Intent(TrainerPage.this, Schedule.class);
            startActivity(intent);
        });

        btnMySessions.setOnClickListener(v -> {
            Intent intent = new Intent(TrainerPage.this, MySessions.class);
            startActivity(intent);
        });

        btnSessionRequests.setOnClickListener(v -> {
            // When the trainer clicks "Session Requests", navigate to SessionRequestsActivity
            Intent intent = new Intent(TrainerPage.this, TrainerRequests.class);
            startActivity(intent);
        });

        btnLogout.setOnClickListener(v -> {
            SharedPreferencesUtil.signOutUser(TrainerPage.this);
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(TrainerPage.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        btnAbout.setOnClickListener(v -> {
            Intent intent = new Intent(TrainerPage.this, About.class);
            startActivity(intent);
        });

        // Navigate to Admin Page
        btnAdminPage.setOnClickListener(v -> {
            Intent intent = new Intent(TrainerPage.this, AdminVerification.class);
            startActivity(intent);
        });
    }
}
