package com.ofekinyo.myswimmingapp.screens;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ofekinyo.myswimmingapp.R;

public class EachTutor extends AppCompatActivity {

    private TextView tvTrainerName;
    private Button btnRequestSession, btnMoreInfo;
    private String trainerId, traineeId, trainerName, traineeName;
    private DatabaseReference usersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_each_tutor);

        // Initialize UI components
        tvTrainerName = findViewById(R.id.tvTrainerName);
        btnRequestSession = findViewById(R.id.btnRequestSession);
        btnMoreInfo = findViewById(R.id.btnMoreInfo);

        // Retrieve trainer and trainee data from Intent
        Intent intent = getIntent();
        trainerId = intent.getStringExtra("trainerId");
        traineeId = intent.getStringExtra("traineeId");
        traineeName = intent.getStringExtra("traineeName");

        Log.d("EachTrainer", "Trainer ID: " + trainerId);
        Log.d("EachTrainer", "Trainee ID: " + traineeId);

        // Check if the trainerId and traineeId are null
        if (trainerId == null || traineeId == null) {
            Log.e("EachTrainer", "Error: Trainer ID or Trainee ID is null");
            Toast.makeText(this, "Error: Missing trainer or trainee data", Toast.LENGTH_SHORT).show();
            return; // Stop execution if any of these IDs are null
        }

        // Initialize Firebase reference
        usersRef = FirebaseDatabase.getInstance().getReference("Users");

        // Retrieve trainer data from Firebase
        usersRef.child("Trainers").child(trainerId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    trainerName = dataSnapshot.child("name").getValue(String.class);
                    tvTrainerName.setText(trainerName);
                    Log.d("EachTrainer", "Trainer name: " + trainerName);

                    // Handle Request Session button click
                    btnRequestSession.setOnClickListener(v -> {
                        if (trainerId != null && traineeId != null && trainerName != null && traineeName != null) {
                            Intent requestIntent = new Intent(EachTutor.this, SendRequest.class);
                            requestIntent.putExtra("trainerId", trainerId);
                            requestIntent.putExtra("traineeId", traineeId);
                            requestIntent.putExtra("trainerName", trainerName);
                            requestIntent.putExtra("traineeName", traineeName);
                            startActivity(requestIntent);
                        } else {
                            Toast.makeText(EachTutor.this, "Error: Missing trainer or trainee information", Toast.LENGTH_SHORT).show();
                        }
                    });

                    // Handle More Info button click - move this here!
                    btnMoreInfo.setOnClickListener(v -> {
                        Intent moreInfoIntent = new Intent(EachTutor.this, TutorInfo.class);
                        moreInfoIntent.putExtra("trainerId", trainerId); // Pass trainerId, NOT trainerName
                        startActivity(moreInfoIntent);
                    });


                } else {
                    Log.e("EachTrainer", "Trainer not found in Firebase");
                    Toast.makeText(EachTutor.this, "Error: Trainer not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("EachTrainer", "Failed to read trainer data", databaseError.toException());
            }
        });
    }
}
