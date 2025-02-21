package com.ofekinyo.myswimmingapp.screens;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ofekinyo.myswimmingapp.R;

import java.util.HashMap;
import java.util.Map;

public class TrainerDetailsActivity extends AppCompatActivity {

    private EditText etExperienceYears, etCompetitiveHistory, etTrainingLocations;
    private CheckBox cbFreestyle, cbBackstroke, cbBreaststroke, cbButterfly, cbLifeguardCertified;
    private Button btnSaveTrainerDetails;
    private DatabaseReference trainerDatabaseRef;
    private FirebaseAuth mAuth;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainer_details);

        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getCurrentUser().getUid();
        trainerDatabaseRef = FirebaseDatabase.getInstance().getReference("Trainers").child(userId);

        etExperienceYears = findViewById(R.id.etExperienceYears);
        etCompetitiveHistory = findViewById(R.id.etCompetitiveHistory);
        etTrainingLocations = findViewById(R.id.etTrainingLocations);

        cbFreestyle = findViewById(R.id.cbFreestyle);
        cbBackstroke = findViewById(R.id.cbBackstroke);
        cbBreaststroke = findViewById(R.id.cbBreaststroke);
        cbButterfly = findViewById(R.id.cbButterfly);
        cbLifeguardCertified = findViewById(R.id.cbLifeguardCertified);

        btnSaveTrainerDetails = findViewById(R.id.btnSaveTrainerDetails);

        btnSaveTrainerDetails.setOnClickListener(v -> saveTrainerDetails());
    }

    private void saveTrainerDetails() {
        String experienceYears = etExperienceYears.getText().toString().trim();
        String competitiveHistory = etCompetitiveHistory.getText().toString().trim();
        String trainingLocations = etTrainingLocations.getText().toString().trim();

        if (TextUtils.isEmpty(experienceYears)) {
            etExperienceYears.setError("יש להזין מספר שנות ניסיון");
            return;
        }

        if (TextUtils.isEmpty(trainingLocations)) {
            etTrainingLocations.setError("יש להזין מקומות אימון");
            return;
        }

        // Get selected swimming styles
        StringBuilder swimmingStyles = new StringBuilder();
        if (cbFreestyle.isChecked()) swimmingStyles.append("Freestyle, ");
        if (cbBackstroke.isChecked()) swimmingStyles.append("Backstroke, ");
        if (cbBreaststroke.isChecked()) swimmingStyles.append("Breaststroke, ");
        if (cbButterfly.isChecked()) swimmingStyles.append("Butterfly, ");

        // Remove trailing comma
        if (swimmingStyles.length() > 0) {
            swimmingStyles.setLength(swimmingStyles.length() - 2);
        }

        boolean isLifeguardCertified = cbLifeguardCertified.isChecked();

        Map<String, Object> trainerDetails = new HashMap<>();
        trainerDetails.put("experienceYears", experienceYears);
        trainerDetails.put("competitiveHistory", competitiveHistory);
        trainerDetails.put("trainingLocations", trainingLocations);
        trainerDetails.put("swimmingStyles", swimmingStyles.toString());
        trainerDetails.put("isLifeguardCertified", isLifeguardCertified);

        trainerDatabaseRef.updateChildren(trainerDetails).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(TrainerDetailsActivity.this, "פרטי המאמן נשמרו בהצלחה!", Toast.LENGTH_LONG).show();
                // Redirect to TrainerPage (not implemented here, add as needed)
                // Intent intent = new Intent(TrainerDetailsActivity.this, TrainerPage.class);
                // startActivity(intent);
                // finish();
            } else {
                Toast.makeText(TrainerDetailsActivity.this, "שגיאה בשמירת הנתונים", Toast.LENGTH_LONG).show();
            }
        });
    }
}
