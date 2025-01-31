package com.ofekinyo.myswimmingapp.screens;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.ofekinyo.myswimmingapp.R;

public class Account extends AppCompatActivity {

    // Declare UI components
    private TextView tvFirstName, tvLastName, tvEmail, tvPhone, tvCity, tvGender, tvAge;
    private Button btnEditDetails, btnBack;
    private ImageView ivProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        // Initialize UI components
        Toolbar toolbar = findViewById(R.id.toolbar);
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

        // Set up the toolbar
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("My Account");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Load user details (replace with real data fetching)
        loadUserDetails();

        // Set button click listeners
        btnEditDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to EditDetailsActivity (replace with your actual activity)
                Intent intent = new Intent(Account.this, EditAccount.class);
                startActivity(intent);
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the current activity and return to the previous screen
                finish();
            }
        });
    }

    private void loadUserDetails() {
        // Simulate fetching user data (replace with real data from Firebase or other source)
        ivProfile.setImageResource(R.drawable.default_profile); // Set a default profile image
        tvFirstName.setText("שם פרטי: John");
        tvLastName.setText("שם משפחה: Doe");
        tvEmail.setText("אימייל: johndoe@example.com");
        tvPhone.setText("טלפון: 123-456-7890");
        tvCity.setText("עיר: Tel Aviv");
        tvGender.setText("מגדר: Male");
        tvAge.setText("גיל: 30");
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
