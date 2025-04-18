package com.ofekinyo.myswimmingapp.screens;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ofekinyo.myswimmingapp.R;

import java.util.HashMap;
import java.util.Map;

public class TraineeDetailsActivity extends AppCompatActivity {

    private EditText etHeight, etWeight, etGoal; // Add the new input field for goal
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

        etHeight = findViewById(R.id.etHeight);
        etWeight = findViewById(R.id.etWeight);
        etGoal = findViewById(R.id.etGoal); // Initialize the new input field
        btnSaveTraineeDetails = findViewById(R.id.btnSaveTraineeDetails);

        btnSaveTraineeDetails.setOnClickListener(v -> saveTraineeDetails());
    }

    private void saveTraineeDetails() {
        String heightStr = etHeight.getText().toString().trim();
        String weightStr = etWeight.getText().toString().trim();
        String goal = etGoal.getText().toString().trim(); // Get the value of the new field

        if (TextUtils.isEmpty(heightStr)) {
            etHeight.setError("יש להזין גובה");
            return;
        }
        if (TextUtils.isEmpty(weightStr)) {
            etWeight.setError("יש להזין משקל");
            return;
        }
        if (TextUtils.isEmpty(goal)) {  // Check if goal is empty
            etGoal.setError("יש להזין מטרת אימון");
            return;
        }

        double height = Double.parseDouble(heightStr);
        double weight = Double.parseDouble(weightStr);

        Map<String, Object> traineeDetails = new HashMap<>();
        traineeDetails.put("height", height);
        traineeDetails.put("weight", weight);
        traineeDetails.put("goal", goal); // Add the goal to the data

        traineeDatabaseRef.updateChildren(traineeDetails).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(TraineeDetailsActivity.this, "פרטי השחיין נשמרו בהצלחה!", Toast.LENGTH_LONG).show();
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
