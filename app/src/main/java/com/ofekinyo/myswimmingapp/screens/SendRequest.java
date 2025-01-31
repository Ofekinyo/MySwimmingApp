package com.ofekinyo.myswimmingapp.screens;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ofekinyo.myswimmingapp.R;

public class SendRequest extends AppCompatActivity {

    private Spinner spinnerSessionType;
    private EditText etLength, etDate, etGoals;
    private Button btnSubmit;
    private String selectedSessionType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_request);

        // Initialize UI components
        spinnerSessionType = findViewById(R.id.spinnerSessionType);
        etLength = findViewById(R.id.etLength);
        etDate = findViewById(R.id.etDate);
        etGoals = findViewById(R.id.etGoals);
        btnSubmit = findViewById(R.id.btnSubmit);

        // Set up the Spinner with session types
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.session_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSessionType.setAdapter(adapter);

        // Spinner item selection listener
        spinnerSessionType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                selectedSessionType = parentView.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                selectedSessionType = null;
            }
        });

        // Submit button click listener
        btnSubmit.setOnClickListener(v -> {
            // Collect inputs
            String length = etLength.getText().toString();
            String date = etDate.getText().toString();
            String goals = etGoals.getText().toString();

            // Validation check
            if (selectedSessionType == null || length.isEmpty() || date.isEmpty() || goals.isEmpty()) {
                Toast.makeText(SendRequest.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            } else {
                // Handle the request submission logic here
                // For example, saving the data or sending it to a server
                Toast.makeText(SendRequest.this, "Request Submitted: " + selectedSessionType, Toast.LENGTH_SHORT).show();
                clearFields();
            }
        });
    }

    // Method to clear input fields after submission
    private void clearFields() {
        etLength.setText("");
        etDate.setText("");
        etGoals.setText("");
        spinnerSessionType.setSelection(0);
    }
}
