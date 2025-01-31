package com.ofekinyo.myswimmingapp.screens;

import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;
import com.ofekinyo.myswimmingapp.R;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

public class TraineePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainee_page);

        // Get button references
        Button btnTrainersList = findViewById(R.id.btnTrainersList);
        Button btnAllSessions = findViewById(R.id.btnAllSessions);
        Button btnSessionDetails = findViewById(R.id.btnSessionDetails);

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
    }
}