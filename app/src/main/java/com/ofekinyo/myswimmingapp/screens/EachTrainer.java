package com.ofekinyo.myswimmingapp.screens;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.ofekinyo.myswimmingapp.R;

public class EachTrainer extends AppCompatActivity {

    private TextView tvTrainerName;
    private Button btnRequestSession, btnMoreInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_each_trainer); // This layout is where tvTrainerName, btnRequestSession, btnMoreInfo exist

        // Initialize UI components
        tvTrainerName = findViewById(R.id.tvTrainerName);
        btnRequestSession = findViewById(R.id.btnRequestSession);
        btnMoreInfo = findViewById(R.id.btnMoreInfo);

        // Get trainer data from the Intent
        Intent intent = getIntent();
        String trainerName = intent.getStringExtra("trainerName");

        // Set trainer name on the TextView
        tvTrainerName.setText(trainerName);

        // Handle Request Session button click
        btnRequestSession.setOnClickListener(v -> {
            // Pass the trainer's name to the SendRequest activity
            Intent requestIntent = new Intent(EachTrainer.this, SendRequest.class);
            requestIntent.putExtra("trainerName", trainerName);
            startActivity(requestIntent);
        });

        // Handle More Info button click
        btnMoreInfo.setOnClickListener(v -> {
            // Show more info about the trainer (if necessary)
            // Since you only need the name, we don’t need to pass the email here
            Intent moreInfoIntent = new Intent(EachTrainer.this, TrainerInfo.class);
            moreInfoIntent.putExtra("trainerName", trainerName); // Only passing the name
            startActivity(moreInfoIntent);
        });
    }
}
