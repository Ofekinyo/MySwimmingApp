package com.ofekinyo.myswimmingapp.screens;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.ofekinyo.myswimmingapp.R;
import com.ofekinyo.myswimmingapp.base.BaseActivity;
import com.ofekinyo.myswimmingapp.services.AuthenticationService;

public class AdminLoginActivity extends BaseActivity {

    private EditText emailEditText, passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);
        setupToolbar("התחברות מנהל");


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

        authenticationService.signIn(email, password, new AuthenticationService.AuthCallback<String>() {
            @Override
            public void onCompleted(String uid) {
                Intent intent = new Intent(AdminLoginActivity.this, AdminPage.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailed(Exception e) {
                Toast.makeText(AdminLoginActivity.this, "Authentication failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}