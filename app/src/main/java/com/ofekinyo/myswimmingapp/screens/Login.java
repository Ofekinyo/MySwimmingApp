package com.ofekinyo.myswimmingapp.screens;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ofekinyo.myswimmingapp.R;
import com.ofekinyo.myswimmingapp.models.User;
import com.ofekinyo.myswimmingapp.utils.SharedPreferencesUtil;

public class Login extends AppCompatActivity {

    private EditText etEmail;
    private EditText etPassword;
    private Button btnLogin;

    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;

    User user=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check if user is already logged in
        if (SharedPreferencesUtil.isUserLoggedIn(this)) {
            User user = SharedPreferencesUtil.getUser(this);
            if (user != null) {
                redirectToUserPage(user.getRole());
                return; // Ensure it returns here to stop execution
            }
        }

        setContentView(R.layout.activity_login);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        // Initialize views
        etEmail = findViewById(R.id.et_login_email);
        etPassword = findViewById(R.id.et_login_password);
        btnLogin = findViewById(R.id.btn_login_login);
        user=SharedPreferencesUtil.getUser(this);
        if(user!=null){

            etEmail.setText(user.getEmail());
            etPassword.setText(user.getPassword());
        }



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
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                fetchUserData(user.getUid());
                            }
                        } else {
                            Toast.makeText(Login.this, "Login failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        });
    }

    // Fetch user data from Firebase after login
    private void fetchUserData(String userId) {
        databaseReference.child("Trainees").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    User user = snapshot.getValue(User.class);
                    if (user != null) {
                        user.setId(userId);
                        user.setRole("Trainee");
                        SharedPreferencesUtil.saveUser(Login.this, user);
                        navigateToPage(TraineePage.class);
                    }
                } else {
                    databaseReference.child("Trainers").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                User user = snapshot.getValue(User.class);
                                if (user != null) {
                                    user.setId(userId);
                                    user.setRole("Trainer");
                                    SharedPreferencesUtil.saveUser(Login.this, user);
                                    navigateToPage(TrainerPage.class);
                                }
                            } else {
                                // If user is not found in both nodes
                                Toast.makeText(Login.this, "User not found in database", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(Login.this, "Database error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Login.this, "Database error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Redirect user based on role
    private void redirectToUserPage(String role) {
        if ("Trainee".equals(role)) {
            navigateToPage(TraineePage.class);
        } else if ("Trainer".equals(role)) {
            navigateToPage(TrainerPage.class);
        }
    }

    private void navigateToPage(Class<?> destination) {
        Intent intent = new Intent(Login.this, destination);
        startActivity(intent);
        finish();
    }
}
