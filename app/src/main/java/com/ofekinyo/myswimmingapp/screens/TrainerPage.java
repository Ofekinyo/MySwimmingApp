package com.ofekinyo.myswimmingapp.screens;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
        Button btnAccount = findViewById(R.id.btnAccount);
        Button btnSessionRequests = findViewById(R.id.btnSessionRequests); // Updated button ID
        Button btnLogout = findViewById(R.id.btnLogout);
        Button btnAbout = findViewById(R.id.btnAbout);

        // Set button click listeners
        btnSchedule.setOnClickListener(v -> {
            Intent intent = new Intent(TrainerPage.this, ScheduleActivity.class);
            startActivity(intent);
        });

        btnAccount.setOnClickListener(v -> {
            Intent intent = new Intent(TrainerPage.this, Account.class);
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
    }
}
