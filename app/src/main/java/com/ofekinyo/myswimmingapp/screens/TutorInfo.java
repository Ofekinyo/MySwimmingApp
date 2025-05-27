package com.ofekinyo.myswimmingapp.screens;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.ofekinyo.myswimmingapp.R;
import com.ofekinyo.myswimmingapp.models.Turor;

public class TutorInfo extends AppCompatActivity {

    private TextView tvTrainerName, tvEmail, tvPhone, tvAge, tvGender, tvCity, tvPrice, tvExperience, tvTrainingTypes;


    Intent takeit;

    Turor turor =null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_info);

        tvTrainerName = findViewById(R.id.tvTrainerName);
        tvEmail = findViewById(R.id.tvEmail);
        tvPhone = findViewById(R.id.tvPhone);
        tvAge = findViewById(R.id.tvAge);
        tvGender = findViewById(R.id.tvGender);
        tvCity = findViewById(R.id.tvCity);
        tvPrice = findViewById(R.id.tvPrice);
        tvExperience = findViewById(R.id.tvExperience);
        tvTrainingTypes = findViewById(R.id.tvTrainingTypes);
        takeit = getIntent();

        // Get the trainer's ID passed from the previous activity
        turor = (Turor) takeit.getSerializableExtra("trainer");

        if (turor == null) {
            Log.e("TrainerInfo", "Trainer ID is null!");
            return;
        } else if (turor != null) {


            // Display the trainer's details in the corresponding TextViews
            String name = turor.getFname() + " " + turor.getLname();  // Concatenate first and last name
            tvTrainerName.setText(name);
            tvEmail.setText(turor.getEmail());
            tvPhone.setText(turor.getPhone());
            tvAge.setText((turor.getAge()) + "");
            tvGender.setText(turor.getGender());
            tvCity.setText(turor.getCity());
            tvPrice.setText(turor.getPrice() + "");
            tvExperience.setText(turor.getExperience() + "");

//             Display trainingTypes as a comma-separated string

            if (turor.getTrainingTypes().size() >= 0) {
                tvTrainingTypes.setText(String.join(", ", turor.getTrainingTypes()));
            } else {
                tvTrainingTypes.setText("Not available");
            }

        }
    }
}
