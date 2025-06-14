package com.ofekinyo.myswimmingapp.screens;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.ofekinyo.myswimmingapp.R;
import com.ofekinyo.myswimmingapp.base.BaseActivity;
import com.ofekinyo.myswimmingapp.services.AuthenticationService;

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
                String currentUserId = AuthenticationService.getInstance().getCurrentUserId();
                intent.putExtra("userId", currentUserId);
                startActivity(intent);
            } catch (Exception e) {
                Toast.makeText(this, "מצטערים, התכונה הזו עדיין לא זמינה", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
