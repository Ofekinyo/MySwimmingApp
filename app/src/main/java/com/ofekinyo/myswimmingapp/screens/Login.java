package com.ofekinyo.myswimmingapp.screens;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.ofekinyo.myswimmingapp.R;

public class Login extends AppCompatActivity {

    private EditText etEmail;
    private EditText etPassword;
    private Button btnLogin;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Initialize views
        etEmail = findViewById(R.id.et_login_email);
        etPassword = findViewById(R.id.et_login_password);
        btnLogin = findViewById(R.id.btn_login_login);

        // Set up login button click listener
        btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                Toast.makeText(Login.this, "Please fill in both fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // Attempt to sign in
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            // Login successful
                            Intent resultIntent = new Intent();
                            setResult(RESULT_OK, resultIntent);
                            finish();  // Return result to MainActivity
                        } else {
                            // If sign-in fails, display a message to the user
                            Toast.makeText(Login.this, "Login failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        });
    }
}
