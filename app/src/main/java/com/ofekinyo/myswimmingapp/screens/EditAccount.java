package com.ofekinyo.myswimmingapp.screens;

import android.os.Bundle;
import android.view.View;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ofekinyo.myswimmingapp.R;
import com.ofekinyo.myswimmingapp.models.Swimmer;
import com.ofekinyo.myswimmingapp.models.Turor;
import com.ofekinyo.myswimmingapp.models.User;

import java.util.*;

public class EditAccount extends AppCompatActivity {

    private EditText etFname, etLname, etPhone, etEmail, etAge, etGender, etCity, etPassword;
    private EditText etHeight, etWeight; // for Trainee
    private EditText etExperience, etPrice; // for Trainer
    private EditText etTrainingTypes; // comma-separated for Trainer
    private Button btnSave;

    private DatabaseReference userRef;
    private String userId;
    private String role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_account);

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
        etTrainingTypes = findViewById(R.id.etTrainingTypes);

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
                if (role.equals("Trainer")) {
                    Turor turor = snapshot.getValue(Turor.class);
                    if (turor != null) fillCommonFields(turor);
                    etExperience.setText(String.valueOf(turor.getExperience()));
                    etPrice.setText(String.valueOf(turor.getPrice()));
                    etTrainingTypes.setText(String.join(", ", turor.getTrainingTypes()));
                    setTrainerVisibility(true);
                } else if (role.equals("Trainee")) {
                    Swimmer swimmer = snapshot.getValue(Swimmer.class);
                    if (swimmer != null) fillCommonFields(swimmer);
                    etHeight.setText(String.valueOf(swimmer.getHeight()));
                    etWeight.setText(String.valueOf(swimmer.getWeight()));
                    setTrainerVisibility(false);
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

        if (role.equals("Trainer")) {
            Turor turor = new Turor();
            turor.setId(user.getId());
            turor.setFname(user.getFname());
            turor.setLname(user.getLname());
            turor.setPhone(user.getPhone());
            turor.setEmail(user.getEmail());
            turor.setAge(user.getAge());
            turor.setGender(user.getGender());
            turor.setCity(user.getCity());
            turor.setPassword(user.getPassword());
            turor.setRole(user.getRole());
            turor.setExperience(Integer.parseInt(etExperience.getText().toString()));
            turor.setPrice(Double.parseDouble(etPrice.getText().toString()));
            turor.setTrainingTypes(Arrays.asList(etTrainingTypes.getText().toString().split(",\\s*")));


            userRef.setValue(turor);

        } else if (role.equals("Trainee")) {
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
    }

    private void setTrainerVisibility(boolean isTrainer) {
        etExperience.setVisibility(isTrainer ? View.VISIBLE : View.GONE);
        etPrice.setVisibility(isTrainer ? View.VISIBLE : View.GONE);
        etTrainingTypes.setVisibility(isTrainer ? View.VISIBLE : View.GONE);

        etHeight.setVisibility(isTrainer ? View.GONE : View.VISIBLE);
        etWeight.setVisibility(isTrainer ? View.GONE : View.VISIBLE);
    }
}
