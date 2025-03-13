package com.ofekinyo.myswimmingapp.screens;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ofekinyo.myswimmingapp.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SendRequest extends AppCompatActivity {

    private EditText etLength, etDate;
    private LinearLayout llGoals;
    private CheckBox cbGoal1, cbGoal2, cbGoal3, cbOther;
    private EditText etOtherGoal;
    private Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_request);

        // Initialize UI components
        etLength = findViewById(R.id.etLength);
        etDate = findViewById(R.id.etDate);
        llGoals = findViewById(R.id.llGoals);
        cbGoal1 = findViewById(R.id.cbGoal1);
        cbGoal2 = findViewById(R.id.cbGoal2);
        cbGoal3 = findViewById(R.id.cbGoal3);
        cbOther = findViewById(R.id.cbOther);
        etOtherGoal = findViewById(R.id.etOtherGoal);
        btnSubmit = findViewById(R.id.btnSubmit);

        // Set a listener for the "Other" checkbox to show or hide the "Other" goal input field
        cbOther.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                etOtherGoal.setVisibility(View.VISIBLE); // Show the input field for "Other"
            } else {
                etOtherGoal.setVisibility(View.GONE); // Hide the input field for "Other"
            }
        });

        // Set a listener for the date input field
        etDate.setOnClickListener(v -> {
            // Get current date
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            // Create DatePickerDialog
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    SendRequest.this,
                    (view, year1, monthOfYear, dayOfMonth) -> {
                        // Set the selected date in the EditText field
                        etDate.setText(year1 + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                    },
                    year, month, day);

            // Show the DatePickerDialog
            datePickerDialog.show();
        });

        // Submit button click listener
        btnSubmit.setOnClickListener(v -> {
            // Collect inputs
            String length = etLength.getText().toString();
            String date = etDate.getText().toString();

            // Collect goals
            List<String> selectedGoals = new ArrayList<>();
            if (cbGoal1.isChecked()) selectedGoals.add(cbGoal1.getText().toString());
            if (cbGoal2.isChecked()) selectedGoals.add(cbGoal2.getText().toString());
            if (cbGoal3.isChecked()) selectedGoals.add(cbGoal3.getText().toString());
            if (cbOther.isChecked()) {
                String otherGoal = etOtherGoal.getText().toString();
                if (!otherGoal.isEmpty()) {
                    selectedGoals.add(otherGoal);
                }
            }

            // Validation check
            if (length.isEmpty() || date.isEmpty() || selectedGoals.isEmpty()) {
                Toast.makeText(SendRequest.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            } else {
                // Handle the request submission logic here
                // For example, saving the data or sending it to a server
                Toast.makeText(SendRequest.this, "Request Submitted\nGoals: " + selectedGoals, Toast.LENGTH_SHORT).show();
                clearFields();
            }
        });
    }

    // Method to clear input fields after submission
    private void clearFields() {
        etLength.setText("");
        etDate.setText("");
        etOtherGoal.setText("");
        cbGoal1.setChecked(false);
        cbGoal2.setChecked(false);
        cbGoal3.setChecked(false);
        cbOther.setChecked(false);
        etOtherGoal.setVisibility(View.GONE);
    }
}
