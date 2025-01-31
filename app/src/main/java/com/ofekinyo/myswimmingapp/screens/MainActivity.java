package com.ofekinyo.myswimmingapp.screens;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.ofekinyo.myswimmingapp.R;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private TextView welcomeTextView;

    private final ActivityResultLauncher<Intent> loginActivityLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    // Login was successful
                    Log.d(TAG, "Login successful, navigating to HomeActivity.");
                    Intent homeIntent = new Intent(MainActivity.this, HomeActivity.class);
                    startActivity(homeIntent);
                    finish();  // Optionally finish the MainActivity to prevent going back
                } else {
                    Log.d(TAG, "Login failed or canceled.");
                }
            });

    private final ActivityResultLauncher<Intent> registerActivityLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    // Registration was successful
                    Intent homeIntent = new Intent(MainActivity.this, HomeActivity.class);
                    startActivity(homeIntent);
                    finish();  // Optionally finish the MainActivity to prevent going back
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        welcomeTextView = findViewById(R.id.tvWelcome);

        Button loginButton = findViewById(R.id.btnLogin);
        loginButton.setOnClickListener(v -> {
            Intent loginIntent = new Intent(MainActivity.this, Login.class);
            loginActivityLauncher.launch(loginIntent);
        });

        Button registerButton = findViewById(R.id.btnRegister);
        registerButton.setOnClickListener(v -> {
            Intent registerIntent = new Intent(MainActivity.this, Register.class);
            registerActivityLauncher.launch(registerIntent);
        });
    }

    private void updateWelcomeMessage(String firstName) {
        welcomeTextView.setText("Welcome to SwimLink, " + firstName + "!");
        Log.d(TAG, "Updated welcome message: Welcome to SwimLink, " + firstName + "!");
    }
}
