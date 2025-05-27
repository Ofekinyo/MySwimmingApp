package com.ofekinyo.myswimmingapp.screens;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ofekinyo.myswimmingapp.R;
import com.ofekinyo.myswimmingapp.models.Request;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ContactTrainer extends AppCompatActivity {

    private CheckBox cbGoal1, cbGoal2, cbGoal3, cbOther;
    private EditText etOtherGoal, etDate, etTime, etLocation, etNotes;
    private Button btnSubmit;
    private TextView tvTrainerName;

    private String trainerId = "EXAMPLE_TRAINER_ID"; // Replace with intent extra or data from previous screen
    private String traineeId;
    private DatabaseReference requestsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_trainer);

        // Firebase setup
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        requestsRef = db.getReference("SessionRequests");

        // Get current user (trainee)
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            traineeId = currentUser.getUid();
        }

        // Get trainerId and trainerName from Intent (if passed)
        if (getIntent().hasExtra("trainerId")) {
            trainerId = getIntent().getStringExtra("trainerId");
        }
        String trainerName = "שם המדריך"; // Default label if none provided
        if (getIntent().hasExtra("trainerName")) {
            trainerName = getIntent().getStringExtra("trainerName");
        }

        // Initialize UI elements
        tvTrainerName = findViewById(R.id.tvTrainerNameC);
        cbGoal1 = findViewById(R.id.cbGoal1C);
        cbGoal2 = findViewById(R.id.cbGoal2C);
        cbGoal3 = findViewById(R.id.cbGoal3C);
        cbOther = findViewById(R.id.cbOtherC);
        etOtherGoal = findViewById(R.id.etOtherGoalC);
        etDate = findViewById(R.id.etDateC);
        etTime = findViewById(R.id.etTimeC);
        etLocation = findViewById(R.id.etLocationC);
        etNotes = findViewById(R.id.etDetailsC);
        btnSubmit = findViewById(R.id.btnSubmitContact);

        // Set the dynamic trainer name in the TextView
        tvTrainerName.setText(trainerName);

        // Show or hide otherGoal EditText based on checkbox
        cbOther.setOnCheckedChangeListener((buttonView, isChecked) -> {
            etOtherGoal.setVisibility(isChecked ? View.VISIBLE : View.GONE);
        });

        // Setup date picker dialog on etDate click
        etDate.setFocusable(false);
        etDate.setClickable(true);
        etDate.setOnClickListener(v -> showDatePicker());

        // Setup time picker dialog on etTime click
        etTime.setFocusable(false);
        etTime.setClickable(true);
        etTime.setOnClickListener(v -> showTimePicker());

        // Set onClick listener to submit the request
        btnSubmit.setOnClickListener(v -> sendRequestToFirebase());
    }

    private void showDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                ContactTrainer.this,
                (DatePicker view, int selectedYear, int selectedMonth, int selectedDay) -> {
                    // Note: month is 0-based, so add 1
                    String formattedDate = String.format("%02d/%02d/%04d", selectedDay, selectedMonth + 1, selectedYear);
                    etDate.setText(formattedDate);
                },
                year, month, day);
        datePickerDialog.show();
    }

    private void showTimePicker() {
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(
                ContactTrainer.this,
                (TimePicker view, int selectedHour, int selectedMinute) -> {
                    // Convert 24h to 12h format with AM/PM
                    String amPm = (selectedHour >= 12) ? "PM" : "AM";
                    int hour12 = selectedHour % 12;
                    if (hour12 == 0) hour12 = 12; // 0 hour means 12 AM or PM

                    String formattedTime = String.format("%02d:%02d %s", hour12, selectedMinute, amPm);
                    etTime.setText(formattedTime);
                },
                hour, minute, false); // false means 12-hour format with AM/PM
        timePickerDialog.show();
    }

    private void sendRequestToFirebase() {
        List<String> goals = new ArrayList<>();
        if (cbGoal1.isChecked()) goals.add("שיפור טכניקה");
        if (cbGoal2.isChecked()) goals.add("הגברת סיבולת");
        if (cbGoal3.isChecked()) goals.add("הגברת מהירות");

        String otherGoal = cbOther.isChecked() ? etOtherGoal.getText().toString().trim() : "";
        String date = etDate.getText().toString().trim();
        String time = etTime.getText().toString().trim();
        String location = etLocation.getText().toString().trim();
        String notes = etNotes.getText().toString().trim();

        if (date.isEmpty() || time.isEmpty() || location.isEmpty()) {
            Toast.makeText(this, "נא למלא את התאריך, השעה והמיקום", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get trainerName from TextView or Intent
        String trainerName = tvTrainerName.getText().toString();

        String requestId = requestsRef.child(trainerName).push().getKey();

        Request request = new Request(
                requestId,
                trainerId,
                traineeId,
                trainerName,
                goals,
                otherGoal,
                date,
                time,
                location,
                notes,
                "pending"
        );

        // Save under /SessionRequests/<TrainerName>/<RequestId>
        requestsRef.child(trainerName).child(requestId).setValue(request)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(ContactTrainer.this, "הבקשה נשלחה בהצלחה!", Toast.LENGTH_SHORT).show();
                    finish(); // Optionally close the screen
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(ContactTrainer.this, "שגיאה בשליחת הבקשה", Toast.LENGTH_SHORT).show();
                    Log.e("Firebase", "Error:", e);
                });
    }
}
