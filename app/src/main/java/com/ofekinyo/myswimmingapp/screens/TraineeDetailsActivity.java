package com.ofekinyo.myswimmingapp.screens;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ofekinyo.myswimmingapp.R;

import java.util.ArrayList;
import java.util.List;

public class TraineeDetailsActivity extends AppCompatActivity {

    private CheckBox cbLoseWeight, cbGainWeight, cbHoldBreath, cbSwimFaster, cbBuildMuscle, cbImproveTechnique;
    private EditText etWeight, etHeight;
    private Spinner spFitnessLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainee_details);

        // Initialize views
        cbLoseWeight = findViewById(R.id.cbLoseWeight);
        cbGainWeight = findViewById(R.id.cbGainWeight);
        cbHoldBreath = findViewById(R.id.cbHoldBreath);
        cbSwimFaster = findViewById(R.id.cbSwimFaster);
        cbBuildMuscle = findViewById(R.id.cbBuildMuscle);
        cbImproveTechnique = findViewById(R.id.cbImproveTechnique);

        etWeight = findViewById(R.id.etWeight);
        etHeight = findViewById(R.id.etHeight);
        spFitnessLevel = findViewById(R.id.spFitnessLevel);

        Button btnSaveDetails = findViewById(R.id.btnSaveTraineeDetails);

        // Set up fitness level spinner
        List<String> fitnessLevels = new ArrayList<>();
        fitnessLevels.add("מתחיל");
        fitnessLevels.add("ממוצע");
        fitnessLevels.add("מתקדם");
        fitnessLevels.add("מקצוען");

        ArrayAdapter<String> fitnessLevelAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, fitnessLevels);
        fitnessLevelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spFitnessLevel.setAdapter(fitnessLevelAdapter);

        // Save Button functionality
        btnSaveDetails.setOnClickListener(v -> {
            List<String> selectedGoals = new ArrayList<>();

            // Check if each checkbox is selected and add to selectedGoals list
            if (cbLoseWeight.isChecked()) {
                selectedGoals.add("לרדת במשקל");
            }
            if (cbGainWeight.isChecked()) {
                selectedGoals.add("לעלות במשקל");
            }
            if (cbHoldBreath.isChecked()) {
                selectedGoals.add("לעצור נשימה יותר זמן");
            }
            if (cbSwimFaster.isChecked()) {
                selectedGoals.add("לשחות מהר יותר");
            }
            if (cbBuildMuscle.isChecked()) {
                selectedGoals.add("לבנות שריר");
            }
            if (cbImproveTechnique.isChecked()) {
                selectedGoals.add("לשפר טכניקה בשחייה");
            }

            // Get selected fitness level
            String selectedFitnessLevel = spFitnessLevel.getSelectedItem().toString();

            // Show a toast with selected fitness level and goals (optional)
            String goalsMessage = "Fitness level: " + selectedFitnessLevel + "\nGoals selected: " + selectedGoals.toString();
            Toast.makeText(TraineeDetailsActivity.this, goalsMessage, Toast.LENGTH_SHORT).show();

            // Navigate back to TraineePage
            Intent intent = new Intent(TraineeDetailsActivity.this, TraineePage.class);
            startActivity(intent);

            // Optional: Finish current activity so it doesn't stay in the back stack
            finish();
        });
    }
}
