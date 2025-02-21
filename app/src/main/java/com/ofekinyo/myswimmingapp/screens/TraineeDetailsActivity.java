package com.ofekinyo.myswimmingapp.screens;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.ofekinyo.myswimmingapp.R;

import java.util.HashMap;
import java.util.Map;

public class TraineeDetailsActivity extends AppCompatActivity {

    private EditText etSwimmingGoals, etTrainingLocations;
    private Button btnSaveTraineeDetails;
    private DatabaseReference traineeDatabaseRef;
    private FirebaseAuth mAuth;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainee_details);

        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getCurrentUser().getUid();
        traineeDatabaseRef = FirebaseDatabase.getInstance().getReference("Trainees").child(userId);

        etSwimmingGoals = findViewById(R.id.etSwimmingGoals);
        etTrainingLocations = findViewById(R.id.etTrainingLocations);

        btnSaveTraineeDetails = findViewById(R.id.btnSaveTraineeDetails);

        btnSaveTraineeDetails.setOnClickListener(v -> saveTraineeDetails());
    }

    private void saveTraineeDetails() {
        String swimmingGoals = etSwimmingGoals.getText().toString().trim();
        String trainingLocations = etTrainingLocations.getText().toString().trim();

        if (TextUtils.isEmpty(swimmingGoals)) {
            etSwimmingGoals.setError("יש להזין מטרות שחייה");
            return;
        }

        if (TextUtils.isEmpty(trainingLocations)) {
            etTrainingLocations.setError("יש להזין מקומות אימון");
            return;
        }

        Map<String, Object> traineeDetails = new HashMap<>();
        traineeDetails.put("swimmingGoals", swimmingGoals);
        traineeDetails.put("trainingLocations", trainingLocations);

        traineeDatabaseRef.updateChildren(traineeDetails).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(TraineeDetailsActivity.this, "פרטי הטרייני נשמרו בהצלחה!", Toast.LENGTH_LONG).show();
                navigateToPage(); // Navigate to the appropriate page after saving
            } else {
                Toast.makeText(TraineeDetailsActivity.this, "שגיאה בשמירת הנתונים", Toast.LENGTH_LONG).show();
            }
        });
    }

    // Method to navigate to TraineePage after saving details
    private void navigateToPage() {
        Intent intent = new Intent(TraineeDetailsActivity.this, TraineePage.class);
        startActivity(intent);
        finish(); // Close current activity
    }
}
