package com.ofekinyo.myswimmingapp.screens;

import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.widget.Button;
import androidx.appcompat.app.AlertDialog;

import com.ofekinyo.myswimmingapp.R;
import com.ofekinyo.myswimmingapp.base.BaseActivity;

public class SwimmerPage extends BaseActivity {

    private Button btnTutorsList, btnBasicExercises, btnSwimmerSchedule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swimmer_page);
        
        // Log intent extras if any
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            for (String key : extras.keySet()) {
                Object value = extras.get(key);
                Log.d("IntentDebug", key + ": " + value);
            }
        }

        setupToolbar("SwimLink");

        // Get button references
        btnTutorsList = findViewById(R.id.btnTutorsList);
        btnBasicExercises = findViewById(R.id.btnBasicExercises);
        btnSwimmerSchedule = findViewById(R.id.btnSwimmerSchedule);

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
            Intent intent = new Intent(SwimmerPage.this, SwimmerScheduleActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("יציאה")
                .setMessage("האם אתה רוצה לצאת מהאפליקציה?")
                .setPositiveButton("כן", (dialog, which) -> {
                    finishAffinity(); // This will close all activities and exit the app
                })
                .setNegativeButton("ביטול", (dialog, which) -> {
                    dialog.dismiss();
                })
                .show();
    }
}


