package com.ofekinyo.myswimmingapp.screens;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ofekinyo.myswimmingapp.R;
import com.ofekinyo.myswimmingapp.base.BaseActivity;
import com.ofekinyo.myswimmingapp.models.Tutor;
import com.ofekinyo.myswimmingapp.services.DatabaseService;
import com.ofekinyo.myswimmingapp.services.DatabaseService.DatabaseCallback;

public class TutorInfo extends BaseActivity {

    private TextView tvTutorName, tvEmail, tvPhone, tvAge, tvGender, tvCity, tvPrice, tvExperience, tvSessionTypes;
    private DatabaseService databaseService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_info);
        setupToolbar("פרטי מדריך");

        initializeViews();
        databaseService = DatabaseService.getInstance();

        // Try to get tutor object first
        Tutor tutor = (Tutor) getIntent().getSerializableExtra("tutor");
        
        if (tutor != null) {
            // If we have the full tutor object, display it directly
            displayTutorInfo(tutor);
        } else {
            // If we don't have the full object, try to get the tutorId
            String tutorId = getIntent().getStringExtra("tutorId");
            if (tutorId != null) {
                // Load tutor data from Firebase
                loadTutorData(tutorId);
            } else {
                Toast.makeText(this, "Error: No tutor information provided", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private void initializeViews() {
        tvTutorName = findViewById(R.id.tvTutorName);
        tvEmail = findViewById(R.id.tvEmail);
        tvPhone = findViewById(R.id.tvPhone);
        tvAge = findViewById(R.id.tvAge);
        tvGender = findViewById(R.id.tvGender);
        tvCity = findViewById(R.id.tvCity);
        tvPrice = findViewById(R.id.tvPrice);
        tvExperience = findViewById(R.id.tvExperience);
        tvSessionTypes = findViewById(R.id.tvSessionTypes);
    }

    private void loadTutorData(String tutorId) {
        databaseService.getTutor(tutorId, new DatabaseCallback<Tutor>() {
            @Override
            public void onCompleted(Tutor tutor) {
                if (tutor != null) {
                    displayTutorInfo(tutor);
                } else {
                    Toast.makeText(TutorInfo.this, "Error: Tutor not found", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailed(Exception e) {
                Toast.makeText(TutorInfo.this, "Error loading tutor data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void displayTutorInfo(Tutor tutor) {
        tvTutorName.setText(tutor.getName());
        tvEmail.setText(tutor.getEmail());
        tvPhone.setText(tutor.getPhone());
        tvAge.setText(String.valueOf(tutor.getAge()));
        tvGender.setText(tutor.getGender());
        tvCity.setText(tutor.getCity());
        
        if (tutor.getPrice() != null) {
            tvPrice.setText(String.format("₪%.2f", tutor.getPrice()));
        } else {
            tvPrice.setText("Not available");
        }
        
        if (tutor.getExperience() != null) {
            tvExperience.setText(String.format("%d years", tutor.getExperience()));
        } else {
            tvExperience.setText("Not available");
        }
        
        if (tutor.getSessionTypes() != null && !tutor.getSessionTypes().isEmpty()) {
            tvSessionTypes.setText(String.join(", ", tutor.getSessionTypes()));
        } else {
            tvSessionTypes.setText("Not available");
        }
    }
}
