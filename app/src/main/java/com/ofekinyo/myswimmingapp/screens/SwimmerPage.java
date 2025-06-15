package com.ofekinyo.myswimmingapp.screens;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.ofekinyo.myswimmingapp.R;
import com.ofekinyo.myswimmingapp.base.BaseActivity;

public class SwimmerPage extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swimmer_page);

        setupToolbar("SwimLink");

        // Get button references
        Button btnTutorsList = findViewById(R.id.btnTutorsList);
        Button btnBasicExercises = findViewById(R.id.btnBasicExercises);
        Button btnSwimmerSchedule = findViewById(R.id.btnSwimmerSchedule);

        // Set button click listeners
        btnTutorsList.setOnClickListener(v -> {
            Intent intent = new Intent(SwimmerPage.this, TutorsList.class);
            startActivity(intent);
        });

        btnBasicExercises.setOnClickListener(v -> {
            Intent intent = new Intent(SwimmerPage.this, BasicExercisesActivity.class);
            startActivity(intent);
        });

        btnSwimmerSchedule.setOnClickListener(v -> {
            Intent intent = new Intent(SwimmerPage.this, ScheduleActivity.class);
            startActivity(intent);
        });
    }

}


