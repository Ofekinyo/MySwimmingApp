package com.ofekinyo.myswimmingapp.screens;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.ofekinyo.myswimmingapp.R;
import com.ofekinyo.myswimmingapp.base.BaseActivity;
import com.ofekinyo.myswimmingapp.models.Tutor;
import com.ofekinyo.myswimmingapp.services.DatabaseService.DatabaseCallback;

public class TutorInfo extends BaseActivity {

    private TextView tvTutorName, tvEmail, tvPhone, tvAge, tvGender, tvCity, tvPrice, tvExperience, tvSessionTypes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_info);
        setupToolbar("פרטי מדריך");

        initializeViews();

        // Try to get tutor object first
        Tutor tutor = getIntent().getSerializableExtra("tutor", Tutor.class);
        if (tutor == null) {
            Toast.makeText(this, "ahhhhhhhhhh", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        displayTutorInfo(tutor);
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

    private void displayTutorInfo(Tutor tutor) {
        tvTutorName.setText(tutor.getName());
        tvEmail.setText(tutor.getEmail());
        tvPhone.setText(tutor.getPhone());
        tvAge.setText(String.valueOf(tutor.getAge()));
        tvGender.setText(tutor.getGender());
        tvCity.setText(tutor.getCity());
        tvPrice.setText(String.format("₪%.2f", tutor.getPrice()));
        tvExperience.setText(String.format("%d years", tutor.getExperience()));
        
        if (tutor.getSessionTypes() != null && !tutor.getSessionTypes().isEmpty()) {
            tvSessionTypes.setText(String.join(", ", tutor.getSessionTypes()));
        } else {
            tvSessionTypes.setText("Not available");
        }
    }
}
