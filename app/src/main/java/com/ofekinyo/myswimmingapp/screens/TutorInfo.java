package com.ofekinyo.myswimmingapp.screens;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.ofekinyo.myswimmingapp.R;
import com.ofekinyo.myswimmingapp.models.Tutor;

public class TutorInfo extends AppCompatActivity {

    private TextView tvTutorName, tvEmail, tvPhone, tvAge, tvGender, tvCity, tvPrice, tvExperience, tvTrainingTypes;


    Intent takeit;

    Tutor tutor =null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_info);

        tvTutorName = findViewById(R.id.tvTutorName);
        tvEmail = findViewById(R.id.tvEmail);
        tvPhone = findViewById(R.id.tvPhone);
        tvAge = findViewById(R.id.tvAge);
        tvGender = findViewById(R.id.tvGender);
        tvCity = findViewById(R.id.tvCity);
        tvPrice = findViewById(R.id.tvPrice);
        tvExperience = findViewById(R.id.tvExperience);
        tvTrainingTypes = findViewById(R.id.tvTrainingTypes);
        takeit = getIntent();

        // Get the tutor's ID passed from the previous activity
        tutor = (Tutor) takeit.getSerializableExtra("tutor");

        if (tutor == null) {
            Log.e("TutorInfo", "Tutor ID is null!");
            return;
        } else if (tutor != null) {


            // Display the tutor's details in the corresponding TextViews
            String name = tutor.getFname() + " " + tutor.getLname();  // Concatenate first and last name
            tvTutorName.setText(name);
            tvEmail.setText(tutor.getEmail());
            tvPhone.setText(tutor.getPhone());
            tvAge.setText((tutor.getAge()) + "");
            tvGender.setText(tutor.getGender());
            tvCity.setText(tutor.getCity());
            tvPrice.setText(tutor.getPrice() + "");
            tvExperience.setText(tutor.getExperience() + "");

//             Display trainingTypes as a comma-separated string

            if (tutor.getTrainingTypes().size() >= 0) {
                tvTrainingTypes.setText(String.join(", ", tutor.getTrainingTypes()));
            } else {
                tvTrainingTypes.setText("Not available");
            }

        }
    }
}
