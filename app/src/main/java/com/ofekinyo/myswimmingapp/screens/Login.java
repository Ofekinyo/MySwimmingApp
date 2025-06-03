package com.ofekinyo.myswimmingapp.screens;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.ofekinyo.myswimmingapp.R;
import com.ofekinyo.myswimmingapp.models.User;
import com.ofekinyo.myswimmingapp.services.AuthenticationService;
import com.ofekinyo.myswimmingapp.services.DatabaseService;
import com.ofekinyo.myswimmingapp.utils.SharedPreferencesUtil;

public class Login extends AppCompatActivity {

    private static final String TAG = "Login";
    private EditText etLoginEmail, etLoginPassword;
    private Button btnLogin, btnSignup;
    private AuthenticationService authService;
    private DatabaseService dbService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        authService = AuthenticationService.getInstance();
        dbService = DatabaseService.getInstance();

        etLoginEmail = findViewById(R.id.et_login_email);
        etLoginPassword = findViewById(R.id.et_login_password);
        btnLogin = findViewById(R.id.btn_login);
        btnSignup = findViewById(R.id.btnSignup);

        // Check if user is already logged in
        String currentUserId = authService.getCurrentUserId();
        if (currentUserId != null) {
            checkUserType(currentUserId);
        }

        btnLogin.setOnClickListener(v -> loginUser());
        btnSignup.setOnClickListener(v -> startActivity(new Intent(Login.this, Register.class)));
    }

    private void loginUser() {
        String email = etLoginEmail.getText().toString().trim();
        String password = etLoginPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        authService.signIn(email, password, new AuthenticationService.AuthCallback<String>() {
            @Override
            public void onCompleted(String userId) {
                checkUserType(userId);
            }

            @Override
            public void onFailed(Exception e) {
                Toast.makeText(Login.this, "Login failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkUserType(String userId) {
        dbService.getUserRole(userId, new DatabaseService.DatabaseCallback<String>() {
            @Override
            public void onCompleted(String role) {
                if (role != null) {
                    // Get full user data based on role
                    dbService.getUserData(userId, role, new DatabaseService.DatabaseCallback<User>() {
                        @Override
                        public void onCompleted(User user) {
                            // Save user data to SharedPreferences
                            SharedPreferencesUtil.saveUser(Login.this, user);
                            
                            // Navigate based on role
                            Intent intent;
                            switch (role) {
                                case "Admin":
                                    intent = new Intent(Login.this, AdminPage.class);
                                    break;
                                case "Tutor":
                                    intent = new Intent(Login.this, TutorPage.class);
                                    break;
                                case "Swimmer":
                                    intent = new Intent(Login.this, SwimmerPage.class);
                                    break;
                                default:
                                    Toast.makeText(Login.this, "Invalid user role", Toast.LENGTH_SHORT).show();
                                    return;
                            }
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        }

                        @Override
                        public void onFailed(Exception e) {
                            Toast.makeText(Login.this, "Error loading user data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(Login.this, "User role not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailed(Exception e) {
                Toast.makeText(Login.this, "Error checking user type: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
