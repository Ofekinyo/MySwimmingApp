package com.ofekinyo.myswimmingapp.screens;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ofekinyo.myswimmingapp.R;
import com.ofekinyo.myswimmingapp.models.Admin;
import com.ofekinyo.myswimmingapp.models.User;
import com.ofekinyo.myswimmingapp.base.BaseActivity;

public class AdminLoginActivity extends BaseActivity {

    private FirebaseAuth mAuth;
    private EditText emailEditText, passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);
        setupToolbar("התחברות מנהל");

        mAuth = FirebaseAuth.getInstance();

        emailEditText = findViewById(R.id.etAdminUsername);
        passwordEditText = findViewById(R.id.etAdminPassword);

        findViewById(R.id.btnAdminLogin).setOnClickListener(v -> performAdminLogin());
    }

    private void performAdminLogin() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(AdminLoginActivity.this, "Please fill in both fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Firebase Authentication to sign in
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        if (firebaseUser != null) {
                            Intent intent = new Intent(AdminLoginActivity.this, AdminPage.class);
                            startActivity(intent);
                            finish();
                        }
                    } else {
                        Toast.makeText(AdminLoginActivity.this, "Authentication failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}