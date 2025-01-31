package com.ofekinyo.myswimmingapp.screens;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ofekinyo.myswimmingapp.R;

public class AdminVerification extends AppCompatActivity {

    private static final String ADMIN_KEY = "Key1234";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_verification);

        EditText passwordField = findViewById(R.id.passwordField);
        Button submitButton = findViewById(R.id.submitButton);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String enteredPassword = passwordField.getText().toString();

                if (enteredPassword.equals(ADMIN_KEY)) {
                    // Navigate to AdminPage
                    Intent intent = new Intent(AdminVerification.this, AdminPage.class);
                    startActivity(intent);
                } else {
                    // Show error message
                    Toast.makeText(AdminVerification.this, "Incorrect Password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}