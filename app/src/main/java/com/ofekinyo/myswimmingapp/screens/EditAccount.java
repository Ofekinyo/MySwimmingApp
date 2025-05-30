package com.ofekinyo.myswimmingapp.screens;

import android.os.Bundle;
import android.view.View;
import android.widget.*;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ofekinyo.myswimmingapp.R;
import com.ofekinyo.myswimmingapp.base.BaseActivity;
import com.ofekinyo.myswimmingapp.models.Swimmer;
import com.ofekinyo.myswimmingapp.models.Tutor;
import com.ofekinyo.myswimmingapp.models.User;

import java.util.*;

public class EditAccount extends BaseActivity {

    private EditText etFname, etLname, etPhone, etEmail, etAge, etGender, etCity, etPassword;
    private EditText etHeight, etWeight; // for Swimmer
    private EditText etExperience, etPrice; // for Tutor
    private EditText etSessionTypes; // comma-separated for Tutor
    private Button btnSave;

    private DatabaseReference userRef;
    private String userId;
    private String role;

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
        role = getIntent().getStringExtra("role");

        userRef = FirebaseDatabase.getInstance().getReference(role + "s").child(userId);

        loadUserData();

        btnSave.setOnClickListener(v -> saveUserData());
    }

    private void loadUserData() {
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (role.equals("Tutor")) {
                    Tutor tutor = snapshot.getValue(Tutor.class);
                    if (tutor != null) fillCommonFields(tutor);
                    etExperience.setText(String.valueOf(tutor.getExperience()));
                    etPrice.setText(String.valueOf(tutor.getPrice()));
                    etSessionTypes.setText(String.join(", ", tutor.getSessionTypes()));
                    setTutorVisibility(true);
                } else if (role.equals("Swimmer")) {
                    Swimmer swimmer = snapshot.getValue(Swimmer.class);
                    if (swimmer != null) fillCommonFields(swimmer);
                    etHeight.setText(String.valueOf(swimmer.getHeight()));
                    etWeight.setText(String.valueOf(swimmer.getWeight()));
                    setTutorVisibility(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(EditAccount.this, "Failed to load data.", Toast.LENGTH_SHORT).show();
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
        User user = new User();
        user.setId(userId);
        user.setFname(etFname.getText().toString());
        user.setLname(etLname.getText().toString());
        user.setPhone(etPhone.getText().toString());
        user.setEmail(etEmail.getText().toString());
        user.setAge(Integer.parseInt(etAge.getText().toString()));
        user.setGender(etGender.getText().toString());
        user.setCity(etCity.getText().toString());
        user.setPassword(etPassword.getText().toString());
        user.setRole(role);

        if (role.equals("Tutor")) {
            Tutor tutor = new Tutor();
            tutor.setId(user.getId());
            tutor.setFname(user.getFname());
            tutor.setLname(user.getLname());
            tutor.setPhone(user.getPhone());
            tutor.setEmail(user.getEmail());
            tutor.setAge(user.getAge());
            tutor.setGender(user.getGender());
            tutor.setCity(user.getCity());
            tutor.setPassword(user.getPassword());
            tutor.setRole(user.getRole());
            tutor.setExperience(Integer.parseInt(etExperience.getText().toString()));
            tutor.setPrice(Double.parseDouble(etPrice.getText().toString()));
            tutor.setSessionTypes(Arrays.asList(etSessionTypes.getText().toString().split(",\\s*")));

            userRef.setValue(tutor);

        } else if (role.equals("Swimmer")) {
            Swimmer swimmer = new Swimmer();
            swimmer.setId(user.getId());
            swimmer.setFname(user.getFname());
            swimmer.setLname(user.getLname());
            swimmer.setPhone(user.getPhone());
            swimmer.setEmail(user.getEmail());
            swimmer.setAge(user.getAge());
            swimmer.setGender(user.getGender());
            swimmer.setCity(user.getCity());
            swimmer.setPassword(user.getPassword());
            swimmer.setRole(user.getRole());
            swimmer.setHeight(Double.parseDouble(etHeight.getText().toString()));
            swimmer.setWeight(Double.parseDouble(etWeight.getText().toString()));

            userRef.setValue(swimmer);
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
