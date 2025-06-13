package com.ofekinyo.myswimmingapp.screens;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ofekinyo.myswimmingapp.R;
import com.ofekinyo.myswimmingapp.base.BaseActivity;
import com.ofekinyo.myswimmingapp.models.Swimmer;
import com.ofekinyo.myswimmingapp.models.Tutor;
import com.ofekinyo.myswimmingapp.models.User;
import com.ofekinyo.myswimmingapp.models.UserRole;
import com.ofekinyo.myswimmingapp.services.AuthenticationService;
import com.ofekinyo.myswimmingapp.services.DatabaseService;

import java.util.Arrays;

public class EditAccount extends BaseActivity {

    private EditText etFname, etLname, etPhone, etEmail, etAge, etGender, etCity, etPassword;
    private EditText etHeight, etWeight; // for Swimmer
    private EditText etExperience, etPrice; // for Tutor
    private EditText etSessionTypes; // comma-separated for Tutor
    private Button btnSave;

    private String userId;
    private UserRole role;

    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_account);
        setupToolbar("עריכת חשבון");

        etFname = findViewById(R.id.etFname);
        etLname = findViewById(R.id.etLname);
        etPhone = findViewById(R.id.etPhone);
        etEmail = findViewById(R.id.etEmail);
        etAge = findViewById(R.id.etAge);
        etGender = findViewById(R.id.etGender);
        etCity = findViewById(R.id.etCity);
        etPassword = findViewById(R.id.etPassword);

        etHeight = findViewById(R.id.etHeight);
        etWeight = findViewById(R.id.etWeight);
        etExperience = findViewById(R.id.etExperience);
        etPrice = findViewById(R.id.etPrice);
        etSessionTypes = findViewById(R.id.etSessionTypes);

        btnSave = findViewById(R.id.btnSave);

        // Assume userId and role passed via Intent
        userId = getIntent().getStringExtra("userId");
        if (userId == null) {
            userId = AuthenticationService.getInstance().getCurrentUserId();
        }

        loadUserData();

        btnSave.setOnClickListener(v -> saveUserData());
    }

    private void loadUserData() {
        databaseService.getUser(userId, new DatabaseService.DatabaseCallback<User>() {
            @Override
            public void onCompleted(User user) {
                currentUser = user;
                fillCommonFields(user);
                if (user instanceof Tutor) {
                    role = UserRole.Tutor;
                    Tutor tutor = (Tutor) user;
                    etExperience.setText(String.valueOf(tutor.getExperience()));
                    etPrice.setText(String.valueOf(tutor.getPrice()));
                    etSessionTypes.setText(String.join(", ", tutor.getSessionTypes()));
                    setTutorVisibility(true);
                } else if (user instanceof Swimmer) {
                    role = UserRole.Swimmer;
                    Swimmer swimmer = (Swimmer) user;
                    etHeight.setText(String.valueOf(swimmer.getHeight()));
                    etWeight.setText(String.valueOf(swimmer.getWeight()));
                    setTutorVisibility(false);
                }
            }

            @Override
            public void onFailed(Exception e) {

            }
        });
    }

    private void fillCommonFields(User user) {
        etFname.setText(user.getFname());
        etLname.setText(user.getLname());
        etPhone.setText(user.getPhone());
        etEmail.setText(user.getEmail());
        etAge.setText(String.valueOf(user.getAge()));
        etGender.setText(user.getGender());
        etCity.setText(user.getCity());
        etPassword.setText(user.getPassword());
    }

    private void saveUserData() {
        currentUser.setFname(etFname.getText().toString());
        currentUser.setLname(etLname.getText().toString());
        currentUser.setPhone(etPhone.getText().toString());
        currentUser.setEmail(etEmail.getText().toString());
        currentUser.setAge(Integer.parseInt(etAge.getText().toString()));
        currentUser.setGender(etGender.getText().toString());
        currentUser.setCity(etCity.getText().toString());
        currentUser.setPassword(etPassword.getText().toString());

        if (role == UserRole.Tutor) {
            Tutor tutor = (Tutor) currentUser;
            tutor.setExperience(Integer.parseInt(etExperience.getText().toString()));
            tutor.setPrice(Double.parseDouble(etPrice.getText().toString()));
            tutor.setSessionTypes(Arrays.asList(etSessionTypes.getText().toString().split(",\\s*")));

            databaseService.createNewTutor(tutor, new DatabaseService.DatabaseCallback<Void>() {
                @Override
                public void onCompleted(Void object) {

                }

                @Override
                public void onFailed(Exception e) {

                }
            });

        } else if (role == UserRole.Swimmer) {
            Swimmer swimmer = (Swimmer) currentUser;

            swimmer.setHeight(Double.parseDouble(etHeight.getText().toString()));
            swimmer.setWeight(Double.parseDouble(etWeight.getText().toString()));

            databaseService.createNewSwimmer(swimmer, new DatabaseService.DatabaseCallback<Void>() {
                @Override
                public void onCompleted(Void object) {

                }

                @Override
                public void onFailed(Exception e) {

                }
            });
        }

        Toast.makeText(this, "Account updated successfully", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void setTutorVisibility(boolean isTutor) {
        etExperience.setVisibility(isTutor ? View.VISIBLE : View.GONE);
        etPrice.setVisibility(isTutor ? View.VISIBLE : View.GONE);
        etSessionTypes.setVisibility(isTutor ? View.VISIBLE : View.GONE);

        etHeight.setVisibility(isTutor ? View.GONE : View.VISIBLE);
        etWeight.setVisibility(isTutor ? View.GONE : View.VISIBLE);
    }
}
