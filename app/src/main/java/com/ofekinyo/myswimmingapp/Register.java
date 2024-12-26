package com.ofekinyo.myswimmingapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity {

    private EditText etFName, etLName, etEmail, etPhone, etPassword;
    private Button btnRegister;

    private FirebaseAuth mAuth;
    private DatabaseReference userDatabaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        userDatabaseRef = FirebaseDatabase.getInstance().getReference("users");

        // Initialize views
        initViews();

        // Set up register button click listener
        btnRegister.setOnClickListener(v -> {
            String fname = etFName.getText().toString().trim();
            String lname = etLName.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String phone = etPhone.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (!validateInputs(fname, lname, email, phone, password)) {
                return;
            }

            registerUser(fname, lname, email, phone, password);
        });
    }

    private void initViews() {
        etFName = findViewById(R.id.etFirstName);
        etLName = findViewById(R.id.etLastName);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        etPassword = findViewById(R.id.etPassword);
        btnRegister = findViewById(R.id.btnRegister);
    }

    private boolean validateInputs(String fname, String lname, String email, String phone, String password) {
        if (TextUtils.isEmpty(fname)) {
            etFName.setError("יש להזין שם פרטי");
            return false;
        }
        if (TextUtils.isEmpty(lname)) {
            etLName.setError("יש להזין שם משפחה");
            return false;
        }
        if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("אימייל לא תקין");
            return false;
        }
        if (TextUtils.isEmpty(phone) || !phone.matches("\\d{10}")) {
            etPhone.setError("מספר טלפון לא תקין (חייב להיות 10 ספרות)");
            return false;
        }
        if (TextUtils.isEmpty(password) || password.length() < 6) {
            etPassword.setError("הסיסמה חייבת להיות לפחות 6 תווים");
            return false;
        }
        return true;
    }

    private void registerUser(String fname, String lname, String email, String phone, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Save user data to Firebase Realtime Database
                        String userId = mAuth.getCurrentUser().getUid();
                        User user = new User(fname, lname, email, phone);
                        userDatabaseRef.child(userId).setValue(user)
                                .addOnCompleteListener(dbTask -> {
                                    if (dbTask.isSuccessful()) {
                                        Toast.makeText(Register.this, "הרשמה בוצעה בהצלחה!", Toast.LENGTH_LONG).show();
                                        // Navigate to login or home screen
                                        startActivity(new Intent(Register.this, Login.class));
                                        finish();
                                    } else {
                                        Toast.makeText(Register.this, "שגיאה בשמירת נתוני המשתמש", Toast.LENGTH_LONG).show();
                                    }
                                });
                    } else {
                        Toast.makeText(Register.this, "שגיאה בהרשמה: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    // User class for Firebase database
    public static class User {
        public String fname, lname, email, phone;

        public User() {
            // Default constructor required for calls to DataSnapshot.getValue(User.class)
        }

        public User(String fname, String lname, String email, String phone) {
            this.fname = fname;
            this.lname = lname;
            this.email = email;
            this.phone = phone;
        }
    }
}
