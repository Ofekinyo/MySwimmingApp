package com.ofekinyo.myswimmingapp.screens;

import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ofekinyo.myswimmingapp.R;
import com.ofekinyo.myswimmingapp.utils.SharedPreferencesUtil;

public class TraineePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainee_page);

        // Get button references
        Button btnTrainersList = findViewById(R.id.btnTrainersList);
        Button btnBasicExercises = findViewById(R.id.btnBasicExercises);  // Change button reference
        Button btnSessionDetails = findViewById(R.id.btnSessionDetails);
        Button btnLogout = findViewById(R.id.btnLogout);
        Button btnAbout = findViewById(R.id.btnAbout);

        // Set button click listeners
        btnTrainersList.setOnClickListener(v -> {
            Intent intent = new Intent(TraineePage.this, TrainersList.class);
            startActivity(intent);
        });

        btnBasicExercises.setOnClickListener(v -> {  // Change to navigate to BasicExercisesActivity
            Intent intent = new Intent(TraineePage.this, BasicExercisesActivity.class);
            startActivity(intent);
        });

        btnSessionDetails.setOnClickListener(v -> {
            Intent intent = new Intent(TraineePage.this, SessionDetails.class);
            startActivity(intent);
        });

        btnLogout.setOnClickListener(v -> {
            SharedPreferencesUtil.signOutUser(TraineePage.this);
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(TraineePage.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        btnAbout.setOnClickListener(v -> {
            Intent intent = new Intent(TraineePage.this, About.class);
            startActivity(intent);
        });

    }
}
