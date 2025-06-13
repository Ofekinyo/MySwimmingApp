package com.ofekinyo.myswimmingapp.screens;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ofekinyo.myswimmingapp.R;
import com.ofekinyo.myswimmingapp.base.BaseActivity;
import com.ofekinyo.myswimmingapp.models.Swimmer;
import com.ofekinyo.myswimmingapp.models.Tutor;
import com.ofekinyo.myswimmingapp.models.User;
import com.ofekinyo.myswimmingapp.services.DatabaseService;

public class Account extends BaseActivity {

    private TextView tvFirstName, tvLastName, tvEmail, tvPhone, tvCity, tvGender, tvAge;
    private Button btnEditDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        setupToolbar("SwimLink");

        tvFirstName = findViewById(R.id.tvFirstName);
        tvLastName = findViewById(R.id.tvLastName);
        tvEmail = findViewById(R.id.tvEmail);
        tvPhone = findViewById(R.id.tvPhone);
        tvCity = findViewById(R.id.tvCity);
        tvGender = findViewById(R.id.tvGender);
        tvAge = findViewById(R.id.tvAge);
        btnEditDetails = findViewById(R.id.btnEditDetails);


        if (!authenticationService.isUserSignedIn()) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            finish();
        }

        tvEmail.setText(authenticationService.getCurrentUserEmail());
        getUserType(authenticationService.getCurrentUserId());


        btnEditDetails.setOnClickListener(v -> {
            Intent intent = new Intent(Account.this, EditAccount.class);
            startActivity(intent);
        });
    }

    private void getUserType(String uid) {
        databaseService.getUser(uid, new DatabaseService.DatabaseCallback<User>() {
            @Override
            public void onCompleted(User user) {
                updateUI(user);
                if (user instanceof Tutor) {
                    Tutor tutor = (Tutor) user;
                } else if (user instanceof Swimmer) {
                    Swimmer swimmer = (Swimmer) user;
                }
            }

            @Override
            public void onFailed(Exception e) {

            }
        });
    }

    private void updateUI(User user) {
        tvFirstName.setText("שם פרטי: " + user.getFname());
        tvLastName.setText("שם משפחה: " + user.getLname());
        tvPhone.setText("טלפון: " + user.getPhone());
        tvCity.setText("עיר: " + user.getCity());
        tvGender.setText("מגדר: " + user.getGender());
        tvAge.setText("גיל: " + user.getAge().toString());
    }
}
