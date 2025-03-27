package com.ofekinyo.myswimmingapp.screens;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ofekinyo.myswimmingapp.R;
import com.ofekinyo.myswimmingapp.models.SessionRequest;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class SendRequest extends AppCompatActivity {

    private EditText etDate, etTime, etOtherGoal;
    private CheckBox cbGoal1, cbGoal2, cbGoal3, cbOther;
    private Button btnSubmit;

    private DatabaseReference requestsRef;
    private String trainerId, traineeId, trainerName, traineeName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_request);

        // Initialize Firebase reference
        requestsRef = FirebaseDatabase.getInstance().getReference("SessionRequests");

        // Retrieve data passed from EachTrainer activity
        trainerId = getIntent().getStringExtra("trainerId");
        traineeId = getIntent().getStringExtra("traineeId");
        trainerName = getIntent().getStringExtra("trainerName");
        traineeName = getIntent().getStringExtra("traineeName");

        Log.d("SendRequest", "Trainer ID: " + trainerId);
        Log.d("SendRequest", "Trainee ID: " + traineeId);
        Log.d("SendRequest", "Trainer Name: " + trainerName);
        Log.d("SendRequest", "Trainee Name: " + traineeName);

        // Initialize UI components
        etDate = findViewById(R.id.etDate);
        etTime = findViewById(R.id.etTime);
        etOtherGoal = findViewById(R.id.etOtherGoal);
        cbGoal1 = findViewById(R.id.cbGoal1);
        cbGoal2 = findViewById(R.id.cbGoal2);
        cbGoal3 = findViewById(R.id.cbGoal3);
        cbOther = findViewById(R.id.cbOther);
        btnSubmit = findViewById(R.id.btnSubmit);

        // Set up Date Picker
        etDate.setOnClickListener(v -> showDatePicker());

        // Set up Time Picker
        etTime.setOnClickListener(v -> showTimePicker());

        // Show/hide additional goal input field
        cbOther.setOnCheckedChangeListener((buttonView, isChecked) -> etOtherGoal.setVisibility(isChecked ? View.VISIBLE : View.GONE));

        // Submit button listener
        btnSubmit.setOnClickListener(v -> submitSessionRequest());
    }

    // Show Date Picker Dialog
    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year1, monthOfYear, dayOfMonth) -> {
                    // Format date to 'yyyy-MM-dd'
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    calendar.set(year1, monthOfYear, dayOfMonth);
                    etDate.setText(sdf.format(calendar.getTime()));
                },
                year, month, day);

        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    // Show Time Picker Dialog in 24-hour format
    private void showTimePicker() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                (view, hourOfDay, minute1) -> etTime.setText(String.format("%02d:%02d", hourOfDay, minute1)),
                hour, minute, true); // true for 24-hour format

        timePickerDialog.show();
    }

    // Submit session request to Firebase
    private void submitSessionRequest() {
        String date = etDate.getText().toString();
        String time = etTime.getText().toString();

        if (date.isEmpty() || time.isEmpty()) {
            Snackbar.make(findViewById(android.R.id.content), "Please select date and time", Snackbar.LENGTH_SHORT).show();
            return;
        }

        if (trainerId == null || traineeId == null || trainerName == null || traineeName == null) {
            Snackbar.make(findViewById(android.R.id.content), "Error: Missing trainer or trainee information", Snackbar.LENGTH_SHORT).show();
            return;
        }

        // Collect goals
        StringBuilder goals = new StringBuilder();
        if (cbGoal1.isChecked()) goals.append("Improve technique, ");
        if (cbGoal2.isChecked()) goals.append("Increase stamina, ");
        if (cbGoal3.isChecked()) goals.append("Speed improvement, ");
        if (cbOther.isChecked() && !etOtherGoal.getText().toString().isEmpty()) {
            goals.append(etOtherGoal.getText().toString()).append(", ");
        }

        if (goals.length() > 2) goals.setLength(goals.length() - 2); // Remove last comma

        if (goals.length() == 0) {
            Snackbar.make(findViewById(android.R.id.content), "Please select at least one goal", Snackbar.LENGTH_SHORT).show();
            return;
        }

        // Generate unique request ID
        String requestId = requestsRef.push().getKey();
        if (requestId != null) {
            // Create the SessionRequest object
            SessionRequest request = new SessionRequest(date, time, goals.toString(), trainerName, traineeName);

            // Save session request to Firebase
            requestsRef.child(trainerId).child(traineeId).child(requestId).setValue(request)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(SendRequest.this, "Session request submitted successfully!", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(SendRequest.this, "Failed to submit request", Toast.LENGTH_SHORT).show();
                    });
        }
    }
}
