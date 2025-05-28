package com.ofekinyo.myswimmingapp.screens;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ofekinyo.myswimmingapp.R;

public class Account extends AppCompatActivity {

    private TextView tvFirstName, tvLastName, tvEmail, tvPhone, tvCity, tvGender, tvAge;
    private Button btnEditDetails, btnBack;
    private ImageView ivProfile;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private String userType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        ivProfile = findViewById(R.id.ivProfile);
        tvFirstName = findViewById(R.id.tvFirstName);
        tvLastName = findViewById(R.id.tvLastName);
        tvEmail = findViewById(R.id.tvEmail);
        tvPhone = findViewById(R.id.tvPhone);
        tvCity = findViewById(R.id.tvCity);
        tvGender = findViewById(R.id.tvGender);
        tvAge = findViewById(R.id.tvAge);
        btnEditDetails = findViewById(R.id.btnEditDetails);
        btnBack = findViewById(R.id.btnBack);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("My Account");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            tvEmail.setText(user.getEmail());
            getUserType(user.getUid());
        } else {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            finish();
        }

        btnEditDetails.setOnClickListener(v -> {
            Intent intent = new Intent(Account.this, EditAccount.class);
            startActivity(intent);
        });

        btnBack.setOnClickListener(v -> finish());
    }

    private void getUserType(String uid) {
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();

        rootRef.child("Tutors").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    userType = "Trainers";
                    fetchUserData(uid, userType);
                } else {
                    rootRef.child("Swimmers").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                userType = "Swimmers";
                                fetchUserData(uid, userType);
                            } else {
                                Toast.makeText(Account.this, "User data not found", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(Account.this, "Database Error", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Account.this, "Database Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchUserData(String uid, String userType) {
        databaseReference = FirebaseDatabase.getInstance().getReference().child(userType).child(uid);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    tvFirstName.setText("שם פרטי: " + snapshot.child("fname").getValue(String.class));
                    tvLastName.setText("שם משפחה: " + snapshot.child("lname").getValue(String.class));
                    tvPhone.setText("טלפון: " + snapshot.child("phone").getValue(String.class));
                    tvCity.setText("עיר: " + snapshot.child("city").getValue(String.class));
                    tvGender.setText("מגדר: " + snapshot.child("gender").getValue(String.class));

                    // FIX: Fetch age correctly
                    Long ageValue = snapshot.child("age").getValue(Long.class);
                    if (ageValue != null) {
                        tvAge.setText("גיל: " + ageValue.toString());
                    } else {
                        tvAge.setText("גיל: N/A");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Account.this, "Failed to load data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
