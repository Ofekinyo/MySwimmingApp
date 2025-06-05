package com.ofekinyo.myswimmingapp.screens;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;

import com.ofekinyo.myswimmingapp.R;
import com.ofekinyo.myswimmingapp.base.BaseActivity;

public class TutorPage extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_page);
        setupToolbar("SwimLink");

        // Get button references
        Button btnSchedule = findViewById(R.id.btnTutorSchedule);
        Button btnSessionRequests = findViewById(R.id.btnSessionRequests);

        // Set button click listeners
        btnSchedule.setOnClickListener(v -> {
            Intent intent = new Intent(TutorPage.this, TutorScheduleActivity.class);
            startActivity(intent);
        });

        btnSessionRequests.setOnClickListener(v -> {
            try {
                // Navigate to SessionRequests
                Intent intent = new Intent(TutorPage.this, SessionRequests.class);
                startActivity(intent);
            } catch (Exception e) {
                Toast.makeText(this, "מצטערים, התכונה הזו עדיין לא זמינה", Toast.LENGTH_SHORT).show();
            }
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
