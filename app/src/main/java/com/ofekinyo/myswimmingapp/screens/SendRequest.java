package com.ofekinyo.myswimmingapp.screens;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ofekinyo.myswimmingapp.R;
import com.ofekinyo.myswimmingapp.base.BaseActivity;
import com.ofekinyo.myswimmingapp.models.Session;
import com.ofekinyo.myswimmingapp.models.Tutor;
import com.ofekinyo.myswimmingapp.services.AuthenticationService;
import com.ofekinyo.myswimmingapp.services.DatabaseService;

import java.util.ArrayList;
import java.util.Calendar;

public class SendRequest extends BaseActivity {

    private TextView tvTutorName;
    private EditText etDate, etTime, etLocation, etNotes, etOtherGoal;
    private CheckBox cbGoal1, cbGoal2, cbGoal3, cbOther;
    private Button btnSubmit;

    private Tutor tutor;
    private String swimmerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_request);
        setupToolbar("שליחת בקשה");

        swimmerId = AuthenticationService.getInstance().getCurrentUserId();

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
        tutor = getIntent().getSerializableExtra("tutor", Tutor.class);

        // Display tutor name
        assert tutor != null;
        tvTutorName.setText(tutor.getName());

        // Pickers
        etDate.setOnClickListener(v -> showDatePicker());
        etTime.setOnClickListener(v -> showTimePicker());

        // Submit request
        btnSubmit.setOnClickListener(v -> submitRequest());
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

        String sessionId = databaseService.generateSessionId();

        Session session = new Session(
                sessionId,
                swimmerId,
                tutor.getId(),
                date,
                time,
                selectedGoals,
                location,
                notes,
                null // isAccepted set to null initially
        );

        databaseService.createNewSession(session, new DatabaseService.DatabaseCallback<Void>() {
            @Override
            public void onCompleted(Void object) {
                Toast.makeText(SendRequest.this, "הבקשה לשיעור נשלחה בהצלחה", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailed(Exception e) {
                Toast.makeText(SendRequest.this, "שגיאה בשליחת הבקשה לשיעור", Toast.LENGTH_SHORT).show();

            }
        });

    }

}
