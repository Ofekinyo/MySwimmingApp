package com.ofekinyo.myswimmingapp.screens;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ofekinyo.myswimmingapp.R;
import com.ofekinyo.myswimmingapp.models.Request;
import com.ofekinyo.myswimmingapp.services.AuthenticationService;

import java.util.Calendar;

public class SendRequest extends AppCompatActivity {

    private TextView tvTutorName;
    private EditText etDate, etTime, etLocation, etNotes, etOtherGoal;
    private CheckBox cbGoal1, cbGoal2, cbGoal3, cbOther;
    private Button btnSubmit;

    private String tutorId;
    private String tutorName;
    private String swimmerId;
    private String swimmerName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_request);

        // UI References
        tvTutorName = findViewById(R.id.tvTutorName);
        etDate = findViewById(R.id.etDate);
        etTime = findViewById(R.id.etTime);
        etLocation = findViewById(R.id.etLocation);
        etNotes = findViewById(R.id.etNotes);
        etOtherGoal = findViewById(R.id.etOtherGoal);
        cbGoal1 = findViewById(R.id.cbGoal1);
        cbGoal2 = findViewById(R.id.cbGoal2);
        cbGoal3 = findViewById(R.id.cbGoal3);
        cbOther = findViewById(R.id.cbOther);
        btnSubmit = findViewById(R.id.btnSubmit);

        // Toggle other goal visibility
        cbOther.setOnCheckedChangeListener((buttonView, isChecked) -> {
            etOtherGoal.setVisibility(isChecked ? View.VISIBLE : View.GONE);
        });

        // Get tutor info from Intent
        tutorId = getIntent().getStringExtra("tutorId");
        tutorName = getIntent().getStringExtra("tutorName");

        // Display tutor name
        tvTutorName.setText(tutorName);

        // Get swimmer ID from auth service
        swimmerId = AuthenticationService.getInstance().getCurrentUserId();

        // Load swimmer name
        fetchSwimmerName();

        // Pickers
        etDate.setOnClickListener(v -> showDatePicker());
        etTime.setOnClickListener(v -> showTimePicker());

        // Submit request
        btnSubmit.setOnClickListener(v -> submitRequest());
    }

    private void fetchSwimmerName() {
        FirebaseDatabase.getInstance().getReference("Swimmers")
                .child(swimmerId)
                .child("fname") // assuming name is under fname + lname
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        String fname = snapshot.getValue(String.class);
                        FirebaseDatabase.getInstance().getReference("Swimmers")
                                .child(swimmerId)
                                .child("lname")
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot snapshot) {
                                        String lname = snapshot.getValue(String.class);
                                        swimmerName = fname + " " + lname;
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError error) {
                                        Toast.makeText(SendRequest.this, "שגיאה בטעינת שם המשתמש", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        Toast.makeText(SendRequest.this, "שגיאה בטעינת שם המשתמש", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        new DatePickerDialog(this,
                (view, year, month, dayOfMonth) -> etDate.setText(dayOfMonth + "/" + (month + 1) + "/" + year),
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void showTimePicker() {
        Calendar calendar = Calendar.getInstance();
        new TimePickerDialog(this,
                (view, hourOfDay, minute) -> etTime.setText(String.format("%02d:%02d", hourOfDay, minute)),
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true).show();
    }

    private void submitRequest() {
        String date = etDate.getText().toString();
        String time = etTime.getText().toString();
        String location = etLocation.getText().toString();
        String notes = etNotes.getText().toString();

        if (TextUtils.isEmpty(date) || TextUtils.isEmpty(time) || TextUtils.isEmpty(location)) {
            Toast.makeText(this, "אנא מלא את כל השדות החיוניים", Toast.LENGTH_SHORT).show();
            return;
        }

        ArrayList<String> selectedGoals = new ArrayList<>();
        String otherGoal = "";

        if (cbGoal1.isChecked()) selectedGoals.add("שיפור טכניקה");
        if (cbGoal2.isChecked()) selectedGoals.add("הגברת סיבולת");
        if (cbGoal3.isChecked()) selectedGoals.add("הגברת מהירות");

        if (cbOther.isChecked()) {
            otherGoal = etOtherGoal.getText().toString().trim();
            if (!TextUtils.isEmpty(otherGoal)) {
                selectedGoals.add(otherGoal);
            }
        }

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        String requestId = db.getReference("SessionRequests").push().getKey();

        if (requestId == null) {
            Toast.makeText(this, "שגיאה ביצירת מזהה הבקשה", Toast.LENGTH_SHORT).show();
            return;
        }

        Request request = new Request(
                requestId,
                tutorId,
                swimmerId,
                selectedGoals,
                otherGoal,
                date,
                time,
                location,
                notes,
                "pending"
        );

        db.getReference("SessionRequests")
                .child(requestId)
                .setValue(request)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "הבקשה נשלחה בהצלחה", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "שגיאה בשליחת הבקשה", Toast.LENGTH_SHORT).show();
                });
    }


}
