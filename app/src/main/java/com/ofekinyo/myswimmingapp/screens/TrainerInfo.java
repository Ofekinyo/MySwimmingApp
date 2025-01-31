package com.ofekinyo.myswimmingapp.screens;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.ofekinyo.myswimmingapp.R;

public class TrainerInfo extends AppCompatActivity {  // Renamed to TrainerInfo

    private TextView tvTrainerName, tvTrainerDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainer_info);  // Updated layout name

        // Initialize views
        tvTrainerName = findViewById(R.id.tvTrainerName);
        tvTrainerDetails = findViewById(R.id.tvTrainerDetails);

        // Get the trainer details passed through the Intent
        Intent intent = getIntent();
        String trainerName = intent.getStringExtra("trainerName");
        String trainerInfo = intent.getStringExtra("trainerInfo");

        // Set the data on the views
        tvTrainerName.setText(trainerName);
        tvTrainerDetails.setText(trainerInfo);
    }
}
