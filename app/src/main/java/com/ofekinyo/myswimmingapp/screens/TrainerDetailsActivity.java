package com.ofekinyo.myswimmingapp.screens;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ofekinyo.myswimmingapp.R;

public class TrainerDetailsActivity extends AppCompatActivity {

    private EditText etExperience, etBio;
    private Button btnSaveTrainerDetails;
    private FirebaseAuth auth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainer_details);

        // Initialize Firebase Auth and Database Reference
        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Trainers");

        // Initialize the views
        etExperience = findViewById(R.id.etExperience);
        etBio = findViewById(R.id.etBio);
        btnSaveTrainerDetails = findViewById(R.id.btnSaveTrainerDetails);

        // Save Button functionality
        btnSaveTrainerDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveTrainerDetails();
            }
        });
    }

    private void saveTrainerDetails() {
        String experience = etExperience.getText().toString().trim();
        String bio = etBio.getText().toString().trim();
        String userId = auth.getCurrentUser().getUid();

        if (experience.isEmpty() || bio.isEmpty()) {
            Toast.makeText(this, "נא למלא את כל השדות הנדרשים.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a trainer object
        Trainer trainer = new Trainer(experience, bio);

        // Save trainer details under the user's ID in the database
        databaseReference.child(userId).setValue(trainer)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(TrainerDetailsActivity.this, "פרטי המאמן נשמרו בהצלחה!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(TrainerDetailsActivity.this, TrainerPage.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(TrainerDetailsActivity.this, "שגיאה בשמירת הפרטים, נסה שוב.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Trainer class to store data
    public static class Trainer {
        public String experience;
        public String bio;

        public Trainer() {
        }

        public Trainer(String experience, String bio) {
            this.experience = experience;
            this.bio = bio;
        }
    }
}