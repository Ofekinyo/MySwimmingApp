package com.ofekinyo.myswimmingapp.screens;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ofekinyo.myswimmingapp.R;
import com.ofekinyo.myswimmingapp.models.Swimmer;
import com.ofekinyo.myswimmingapp.models.Tutor;
import com.ofekinyo.myswimmingapp.models.User;
import com.ofekinyo.myswimmingapp.services.AuthenticationService;
import com.ofekinyo.myswimmingapp.services.DatabaseService;
import com.ofekinyo.myswimmingapp.utils.SharedPreferencesUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Register extends AppCompatActivity {

    private static final String TAG = "Register";
    private EditText etFName, etLName, etEmail, etPhone, etPassword, etGender, etAge;
    private EditText etExperience, etPrice; // Tutor fields
    private EditText etHeight, etWeight, etGoal; // Swimmer fields
    private CheckBox cbBeginner, cbAdvanced, cbCompetitive, cbSafety, cbRehab, cbInfants;
    private Button btnRegister;
    private Spinner spCity;
    private RadioGroup radioGroup;
    private RadioButton radioTutor, radioSwimmer;
    private LinearLayout tutorFieldsContainer, swimmerFieldsContainer;

    private AuthenticationService authService;
    private DatabaseService dbService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        authService = AuthenticationService.getInstance();
        dbService = DatabaseService.getInstance();

        initViews();
        setupCitySpinner();
        setupRoleSelection();

        btnRegister.setOnClickListener(v -> handleRegistration());
    }

    private void initViews() {
        // Basic fields
        etFName = findViewById(R.id.etFirstName);
        etLName = findViewById(R.id.etLastName);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        etPassword = findViewById(R.id.etPassword);
        spCity = findViewById(R.id.spinnerCities);
        etGender = findViewById(R.id.etGender);
        etAge = findViewById(R.id.etAge);
        btnRegister = findViewById(R.id.btnRegister);
        radioGroup = findViewById(R.id.radioGroup);
        radioTutor = findViewById(R.id.radioTutor);
        radioSwimmer = findViewById(R.id.radioSwimmer);

        // Containers
        tutorFieldsContainer = findViewById(R.id.tutorFieldsContainer);
        swimmerFieldsContainer = findViewById(R.id.swimmerFieldsContainer);

        // Tutor fields
        etExperience = findViewById(R.id.etExperience);
        etPrice = findViewById(R.id.etPrice);
        cbBeginner = findViewById(R.id.cbBeginner);
        cbAdvanced = findViewById(R.id.cbAdvanced);
        cbCompetitive = findViewById(R.id.cbCompetitive);
        cbSafety = findViewById(R.id.cbSafety);
        cbRehab = findViewById(R.id.cbRehab);
        cbInfants = findViewById(R.id.cbInfants);

        // Swimmer fields
        etHeight = findViewById(R.id.etHeight);
        etWeight = findViewById(R.id.etWeight);
        etGoal = findViewById(R.id.etGoal);
    }

    private void setupCitySpinner() {
        ArrayList<String> cities = new ArrayList<>(Arrays.asList(
                "Tel Aviv", "Jerusalem", "Haifa", "Eilat", "Beersheba", "Nazareth",
                "Petah Tikva", "Ashdod", "Netanya", "Rishon Lezion"
        ));
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, cities);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCity.setAdapter(adapter);
    }

    private void setupRoleSelection() {
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            tutorFieldsContainer.setVisibility(checkedId == R.id.radioTutor ? View.VISIBLE : View.GONE);
            swimmerFieldsContainer.setVisibility(checkedId == R.id.radioSwimmer ? View.VISIBLE : View.GONE);
        });
    }

    private void handleRegistration() {
        // Get basic user information
        String fname = etFName.getText().toString().trim();
        String lname = etLName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String city = spCity.getSelectedItem().toString().trim();
        String gender = etGender.getText().toString().trim();
        String ageStr = etAge.getText().toString().trim();

        // Validate basic inputs
        if (!validateBasicInputs(fname, lname, email, phone, password, city, gender, ageStr)) {
            return;
        }

        // Get role
        String role = radioTutor.isChecked() ? "Tutor" : radioSwimmer.isChecked() ? "Swimmer" : "";
        if (role.isEmpty()) {
            Toast.makeText(Register.this, "יש לבחור תפקיד", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validate role-specific fields
        if (role.equals("Tutor")) {
            if (!validateTutorFields()) return;
        } else {
            if (!validateSwimmerFields()) return;
        }

        // Create user account
        int age = Integer.parseInt(ageStr);
        createUserAccount(fname, lname, email, phone, password, city, gender, age, role);
    }

    private boolean validateBasicInputs(String fname, String lname, String email, String phone, 
                                      String password, String city, String gender, String ageStr) {
        if (TextUtils.isEmpty(fname)) { etFName.setError("יש להזין שם פרטי"); return false; }
        if (TextUtils.isEmpty(lname)) { etLName.setError("יש להזין שם משפחה"); return false; }
        if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("אימייל לא תקין"); return false;
        }
        if (TextUtils.isEmpty(phone) || !phone.matches("\\d{10}")) {
            etPhone.setError("מספר טלפון לא תקין"); return false;
        }
        if (TextUtils.isEmpty(password) || password.length() < 6) {
            etPassword.setError("הסיסמה חייבת להיות לפחות 6 תווים"); return false;
        }
        if (TextUtils.isEmpty(city)) {
            Toast.makeText(Register.this, "יש להזין עיר", Toast.LENGTH_SHORT).show(); return false;
        }
        if (TextUtils.isEmpty(gender)) { etGender.setError("יש להזין מגדר"); return false; }
        if (TextUtils.isEmpty(ageStr) || !ageStr.matches("\\d+") || Integer.parseInt(ageStr) < 1) {
            etAge.setError("יש להזין גיל חוקי"); return false;
        }
        return true;
    }

    private boolean validateTutorFields() {
        String experience = etExperience.getText().toString().trim();
        String price = etPrice.getText().toString().trim();

        if (TextUtils.isEmpty(experience)) {
            etExperience.setError("יש להזין מספר שנות ניסיון");
            return false;
        }
        if (TextUtils.isEmpty(price)) {
            etPrice.setError("יש להזין מחיר לשיעור");
            return false;
        }

        boolean hasSelectedType = cbBeginner.isChecked() || cbAdvanced.isChecked() || 
                                cbCompetitive.isChecked() || cbSafety.isChecked() || 
                                cbRehab.isChecked() || cbInfants.isChecked();
        if (!hasSelectedType) {
            Toast.makeText(this, "יש לבחור לפחות סוג שיעור אחד", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private boolean validateSwimmerFields() {
        String height = etHeight.getText().toString().trim();
        String weight = etWeight.getText().toString().trim();
        String goal = etGoal.getText().toString().trim();

        if (TextUtils.isEmpty(height)) {
            etHeight.setError("יש להזין גובה");
            return false;
        }
        if (TextUtils.isEmpty(weight)) {
            etWeight.setError("יש להזין משקל");
            return false;
        }
        if (TextUtils.isEmpty(goal)) {
            etGoal.setError("יש להזין מטרת אימון");
            return false;
        }

        return true;
    }

    private void createUserAccount(String fname, String lname, String email, String phone,
                                 String password, String city, String gender, int age, String role) {
        authService.signUp(email, password, role, new AuthenticationService.AuthCallback<String>() {
            @Override
            public void onCompleted(String userId) {
                if (role.equals("Tutor")) {
                    createTutor(userId, fname, lname, email, phone, city, gender, age, password);
                } else {
                    createSwimmer(userId, fname, lname, email, phone, city, gender, age, password);
                }
            }

            @Override
            public void onFailed(Exception e) {
                Log.e(TAG, "Error creating user account", e);
                Toast.makeText(Register.this, "שגיאה בהרשמה: " + e.getMessage(), 
                             Toast.LENGTH_LONG).show();
            }
        });
    }

    private void createTutor(String userId, String fname, String lname, String email, String phone,
                           String city, String gender, int age, String password) {
        // Create tutor with null values for optional fields first
        Tutor tutor = new Tutor(userId, fname, lname, phone, email, age, password, gender, city, "Tutor", null, null, null);
        
        // Add tutor-specific fields
        tutor.setExperience(Integer.parseInt(etExperience.getText().toString().trim()));
        tutor.setPrice(Double.parseDouble(etPrice.getText().toString().trim()));

        List<String> sessionTypes = new ArrayList<>();
        if (cbBeginner.isChecked()) sessionTypes.add("Beginner Swimming");
        if (cbAdvanced.isChecked()) sessionTypes.add("Advanced Technique");
        if (cbCompetitive.isChecked()) sessionTypes.add("Competitive Training");
        if (cbSafety.isChecked()) sessionTypes.add("Water Safety & Survival");
        if (cbRehab.isChecked()) sessionTypes.add("Rehabilitation & Therapy");
        if (cbInfants.isChecked()) sessionTypes.add("Infants & Toddlers");
        tutor.setSessionTypes(sessionTypes);

        // Save tutor data
        dbService.createNewTutor(tutor, new DatabaseService.DatabaseCallback<Void>() {
            @Override
            public void onCompleted(Void aVoid) {
                SharedPreferencesUtil.saveUser(Register.this, tutor);
                navigateToNextScreen("Tutor");
            }

            @Override
            public void onFailed(Exception e) {
                Log.e(TAG, "Error saving tutor data", e);
                Toast.makeText(Register.this, "שגיאה בשמירת פרטי המדריך", 
                             Toast.LENGTH_LONG).show();
            }
        });
    }

    private void createSwimmer(String userId, String fname, String lname, String email, String phone,
                             String city, String gender, int age, String password) {
        double height = Double.parseDouble(etHeight.getText().toString().trim());
        double weight = Double.parseDouble(etWeight.getText().toString().trim());
        
        Swimmer swimmer = new Swimmer(userId, fname, lname, phone, email, age, gender, city, password, "Swimmer", height, weight);
        swimmer.setGoal(etGoal.getText().toString().trim());

        // Save swimmer data
        dbService.createNewSwimmer(swimmer, new DatabaseService.DatabaseCallback<Void>() {
            @Override
            public void onCompleted(Void aVoid) {
                SharedPreferencesUtil.saveUser(Register.this, swimmer);
                navigateToNextScreen("Swimmer");
            }

            @Override
            public void onFailed(Exception e) {
                Log.e(TAG, "Error saving swimmer data", e);
                Toast.makeText(Register.this, "שגיאה בשמירת פרטי השחיין", 
                             Toast.LENGTH_LONG).show();
            }
        });
    }

    private void navigateToNextScreen(String role) {
        Toast.makeText(Register.this, "הרשמה בוצעה בהצלחה!", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(Register.this, 
            role.equals("Tutor") ? TutorPage.class : SwimmerPage.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
