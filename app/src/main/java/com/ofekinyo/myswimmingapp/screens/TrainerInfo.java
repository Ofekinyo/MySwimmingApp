package com.ofekinyo.myswimmingapp.screens;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuHost;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ofekinyo.myswimmingapp.R;
import com.ofekinyo.myswimmingapp.models.Trainer;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.Executor;

public class TrainerInfo extends AppCompatActivity {

    private TextView tvTrainerName, tvEmail, tvPhone, tvAge, tvGender, tvCity, tvPrice, tvExperience, tvTrainingTypes;


    Intent takeit;

    Trainer trainer =null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainer_info);

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
        trainer = (Trainer) takeit.getSerializableExtra("trainer");

        if (trainer == null) {
            Log.e("TrainerInfo", "Trainer ID is null!");
            return;
        } else if (trainer != null) {


            // Display the trainer's details in the corresponding TextViews
            String name = trainer.getFname() + " " + trainer.getLname();  // Concatenate first and last name
            tvTrainerName.setText(name);
            tvEmail.setText(trainer.getEmail());
            tvPhone.setText(trainer.getPhone());
            tvAge.setText((trainer.getAge()) + "");
            tvGender.setText(trainer.getGender());
            tvCity.setText(trainer.getCity());
            tvPrice.setText(trainer.getPrice() + "");
            tvExperience.setText(trainer.getExperience() + "");

//             Display trainingTypes as a comma-separated string

            if (trainer.getTrainingTypes().size() >= 0) {
                tvTrainingTypes.setText(String.join(", ", trainer.getTrainingTypes()));
            } else {
                tvTrainingTypes.setText("Not available");
            }

        }
    }
}
