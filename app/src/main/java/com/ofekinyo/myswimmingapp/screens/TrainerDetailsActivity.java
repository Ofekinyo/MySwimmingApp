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
import com.ofekinyo.myswimmingapp.R;

import java.util.HashMap;
import java.util.Map;

public class TrainerDetailsActivity extends AppCompatActivity {

    private EditText etExperience, etDomain, etPrice;
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
        etDomain = findViewById(R.id.etDomain);
        etPrice = findViewById(R.id.etPrice);
        btnSaveTrainerDetails = findViewById(R.id.btnSaveTrainerDetails);

        btnSaveTrainerDetails.setOnClickListener(v -> saveTrainerDetails());
    }

    private void saveTrainerDetails() {
        String experience = etExperience.getText().toString().trim();
        String domain = etDomain.getText().toString().trim();
        String priceStr = etPrice.getText().toString().trim();

        if (TextUtils.isEmpty(experience)) {
            etExperience.setError("יש להזין מספר שנות ניסיון");
            return;
        }
        if (TextUtils.isEmpty(domain)) {
            etDomain.setError("יש להזין תחום התמחות");
            return;
        }
        if (TextUtils.isEmpty(priceStr)) {
            etPrice.setError("יש להזין מחיר לשיעור");
            return;
        }

        double price = Double.parseDouble(priceStr);

        Map<String, Object> trainerDetails = new HashMap<>();
        trainerDetails.put("experience", Integer.parseInt(experience));
        trainerDetails.put("domain", domain);
        trainerDetails.put("price", price);

        trainerDatabaseRef.updateChildren(trainerDetails).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(TrainerDetailsActivity.this, "פרטי המדריך נשמרו בהצלחה!", Toast.LENGTH_LONG).show();

                // Redirect to TrainerPage
                Intent intent = new Intent(TrainerDetailsActivity.this, TrainerPage.class);
                startActivity(intent);
                finish(); // Close the current activity
            } else {
                Toast.makeText(TrainerDetailsActivity.this, "שגיאה בשמירת הנתונים", Toast.LENGTH_LONG).show();
            }
        });
    }

}
