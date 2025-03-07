package com.ofekinyo.myswimmingapp.screens;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ofekinyo.myswimmingapp.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TrainerDetailsActivity extends AppCompatActivity {

    private EditText etExperience, etPrice;
    private CheckBox cbBeginner, cbAdvanced, cbCompetitive, cbSafety, cbRehab, cbInfants;
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

        etExperience = findViewById(R.id.etExperience);
        etPrice = findViewById(R.id.etPrice);
        cbBeginner = findViewById(R.id.cbBeginner);
        cbAdvanced = findViewById(R.id.cbAdvanced);
        cbCompetitive = findViewById(R.id.cbCompetitive);
        cbSafety = findViewById(R.id.cbSafety);
        cbRehab = findViewById(R.id.cbRehab);
        cbInfants = findViewById(R.id.cbInfants);
        btnSaveTrainerDetails = findViewById(R.id.btnSaveTrainerDetails);

        btnSaveTrainerDetails.setOnClickListener(v -> saveTrainerDetails());
    }

    private void saveTrainerDetails() {
        String experience = etExperience.getText().toString().trim();
        String priceStr = etPrice.getText().toString().trim();

        if (TextUtils.isEmpty(experience)) {
            etExperience.setError("יש להזין מספר שנות ניסיון");
            return;
        }
        if (TextUtils.isEmpty(priceStr)) {
            etPrice.setError("יש להזין מחיר לשיעור");
            return;
        }

        double price = Double.parseDouble(priceStr);
        List<String> selectedTrainingTypes = new ArrayList<>();

        if (cbBeginner.isChecked()) selectedTrainingTypes.add("Beginner Swimming");
        if (cbAdvanced.isChecked()) selectedTrainingTypes.add("Advanced Technique");
        if (cbCompetitive.isChecked()) selectedTrainingTypes.add("Competitive Training");
        if (cbSafety.isChecked()) selectedTrainingTypes.add("Water Safety & Survival");
        if (cbRehab.isChecked()) selectedTrainingTypes.add("Rehabilitation & Therapy");
        if (cbInfants.isChecked()) selectedTrainingTypes.add("Infants & Toddlers");

        if (selectedTrainingTypes.isEmpty()) {
            Toast.makeText(this, "בחר לפחות סוג אחד של שיעור", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> trainerDetails = new HashMap<>();
        trainerDetails.put("experience", Integer.parseInt(experience));
        trainerDetails.put("trainingTypes", selectedTrainingTypes);
        trainerDetails.put("price", price);

        trainerDatabaseRef.updateChildren(trainerDetails).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(TrainerDetailsActivity.this, "פרטי המדריך נשמרו בהצלחה!", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(TrainerDetailsActivity.this, TrainerPage.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(TrainerDetailsActivity.this, "שגיאה בשמירת הנתונים", Toast.LENGTH_LONG).show();
            }
        });
    }
}
