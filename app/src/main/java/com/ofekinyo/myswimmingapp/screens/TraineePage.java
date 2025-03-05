package com.ofekinyo.myswimmingapp.screens;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
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
        Button btnAbout = findViewById(R.id.btnAbout); // New "About" button

        // Set button click listeners
        btnTrainersList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to TrainersList activity
                Intent intent = new Intent(TraineePage.this, TrainersList.class);
                startActivity(intent);
            }
        });

        btnAllSessions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to AllSessions activity
                Intent intent = new Intent(TraineePage.this, AllSessions.class);
                startActivity(intent);
            }
        });

        btnSessionDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to SessionDetails activity
                Intent intent = new Intent(TraineePage.this, SessionDetails.class);
                startActivity(intent);
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Logout the user
                SharedPreferencesUtil.signOutUser(TraineePage.this);
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(TraineePage.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        // Set listener for "About" button
        btnAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to About activity
                Intent intent = new Intent(TraineePage.this, About.class);
                startActivity(intent);
            }
        });
    }
}
