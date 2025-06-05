package com.ofekinyo.myswimmingapp.screens;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ofekinyo.myswimmingapp.R;
import com.ofekinyo.myswimmingapp.base.BaseActivity;

public class EachTutor extends BaseActivity {

    private TextView tvTutorName;
    private Button btnRequestSession, btnMoreInfo;
    private String tutorId, swimmerId, tutorName, swimmerName;
    private DatabaseReference usersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_each_tutor);
        setupToolbar("פרטי מדריך");

        // Initialize UI components
        tvTutorName = findViewById(R.id.tvTutorName);
        btnRequestSession = findViewById(R.id.btnRequestSession);
        btnMoreInfo = findViewById(R.id.btnMoreInfo);

        // Retrieve tutor and swimmer data from Intent
        Intent intent = getIntent();
        tutorId = intent.getStringExtra("tutorId");
        swimmerId = intent.getStringExtra("swimmerId");
        swimmerName = intent.getStringExtra("swimmerName");

        Log.d("EachTutor", "Tutor ID: " + tutorId);
        Log.d("EachTutor", "Swimmer ID: " + swimmerId);

        // Check if the tutorId and swimmerId are null
        if (tutorId == null || swimmerId == null) {
            Log.e("EachTutor", "Error: Tutor ID or Swimmer ID is null");
            Toast.makeText(this, "Error: Missing tutor or swimmer data", Toast.LENGTH_SHORT).show();
            return; // Stop execution if any of these IDs are null
        }

        // Initialize Firebase reference
        usersRef = FirebaseDatabase.getInstance().getReference("Users");

        // Retrieve tutor data from Firebase
        usersRef.child("Tutors").child(tutorId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String fname = dataSnapshot.child("fname").getValue(String.class);
                    String lname = dataSnapshot.child("lname").getValue(String.class);
                    tutorName = (fname != null ? fname : "") + " " + (lname != null ? lname : "");
                    tvTutorName.setText(tutorName);
                    Log.d("EachTutor", "Tutor name: " + tutorName);

                    // Handle Request Session button click
                    btnRequestSession.setOnClickListener(v -> {
                        if (tutorId != null && swimmerId != null && tutorName != null && swimmerName != null) {
                            Intent requestIntent = new Intent(EachTutor.this, SendRequest.class);
                            requestIntent.putExtra("tutorId", tutorId);
                            requestIntent.putExtra("swimmerId", swimmerId);
                            requestIntent.putExtra("tutorName", tutorName);
                            requestIntent.putExtra("swimmerName", swimmerName);
                            startActivity(requestIntent);
                        } else {
                            Toast.makeText(EachTutor.this, "Error: Missing tutor or swimmer information", Toast.LENGTH_SHORT).show();
                        }
                    });

                    // Handle More Info button click - move this here!
                    btnMoreInfo.setOnClickListener(v -> {
                        Intent moreInfoIntent = new Intent(EachTutor.this, TutorInfo.class);
                        moreInfoIntent.putExtra("tutorId", tutorId); // Pass tutorId, NOT tutorName
                        startActivity(moreInfoIntent);
                    });


                } else {
                    Log.e("EachTutor", "Tutor not found in Firebase");
                    Toast.makeText(EachTutor.this, "Error: Tutor not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("EachTutor", "Failed to read tutor data", databaseError.toException());
            }
        });
    }
}
