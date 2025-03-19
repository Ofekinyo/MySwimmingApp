package com.ofekinyo.myswimmingapp.screens;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ofekinyo.myswimmingapp.R;
import com.ofekinyo.myswimmingapp.models.Admin;
import com.ofekinyo.myswimmingapp.models.User;

public class AdminLoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText emailEditText, passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

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

                        // Check if the logged-in user is an instance of Admin
                        if (firebaseUser != null && isAdmin(firebaseUser)) {
                            // User is an Admin, proceed to the Admin page
                            Intent intent = new Intent(AdminLoginActivity.this, AdminPage.class);
                            startActivity(intent);
                            finish(); // Close the AdminLoginActivity
                        } else {
                            // Not an admin, show error message
                            Toast.makeText(AdminLoginActivity.this, "You are not authorized as an Admin", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(AdminLoginActivity.this, "Authentication failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // This method checks if the logged-in user is an Admin by checking the 'isAdmin' property
    private boolean isAdmin(FirebaseUser firebaseUser) {
        // Retrieve the admin's user data (from Firebase or a local model)
        User user = getUserData(firebaseUser); // Implement your own method to fetch user data

        return user instanceof Admin && ((Admin) user).isAdmin(); // Check if the user is an instance of Admin and if isAdmin is true
    }

    // A placeholder method to simulate retrieving user data (you need to implement this)
    private User getUserData(FirebaseUser firebaseUser) {
        // Fetch user data from your database (Firebase Realtime Database, Firestore, etc.)
        // This is just a placeholder to demonstrate the concept
        // In reality, you'd retrieve the data from your Firebase database based on the FirebaseUser
        return new Admin(firebaseUser.getUid(), "Ofek", "Ahouvim", "0522206675", firebaseUser.getEmail(), 18, "Male", "Rishon Letzion", "Ofek1agam", "Admin", true);
    }
}