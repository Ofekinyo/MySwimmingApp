package com.ofekinyo.myswimmingapp.screens;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.ofekinyo.myswimmingapp.R;
import com.ofekinyo.myswimmingapp.models.Swimmer;
import com.ofekinyo.myswimmingapp.models.Tutor;
import com.ofekinyo.myswimmingapp.models.UserRole;
import com.ofekinyo.myswimmingapp.services.AuthenticationService;
import com.ofekinyo.myswimmingapp.services.DatabaseService;
import com.ofekinyo.myswimmingapp.utils.SharedPreferencesUtil;
import com.ofekinyo.myswimmingapp.utils.Validator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Register extends AppCompatActivity {

    private static final String TAG = "Register";
    private EditText etFirstName, etLastName, etEmail, etPhone, etPassword, etExperience, etPrice, etHeight, etWeight, etGoal, etGender, etAge;
    private Spinner spinnerCities;
    private RadioGroup radioGroup;
    private LinearLayout tutorFieldsContainer, swimmerFieldsContainer;
    private Button btnRegister, btnLogin;
    private CheckBox cbBeginner, cbAdvanced, cbCompetitive, cbSafety, cbRehab, cbInfants;
    private TextView tvLogin;

    private AuthenticationService authenticationService;
    private DatabaseService databaseService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        authenticationService = AuthenticationService.getInstance();
        databaseService = DatabaseService.getInstance();

        initializeViews();
        setupCitySpinner();
        setupRoleSelection();

        btnRegister.setOnClickListener(v -> registerUser());
        btnLogin.setOnClickListener(v -> navigateToLogin());
        tvLogin.setOnClickListener(v -> navigateToLogin());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);

    }

    private void initializeViews() {
        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        etPassword = findViewById(R.id.etPassword);
        spinnerCities = findViewById(R.id.spinnerCities);
        radioGroup = findViewById(R.id.radioGroup);
        tutorFieldsContainer = findViewById(R.id.tutorFieldsContainer);
        swimmerFieldsContainer = findViewById(R.id.swimmerFieldsContainer);
        etExperience = findViewById(R.id.etExperience);
        etPrice = findViewById(R.id.etPrice);
        etHeight = findViewById(R.id.etHeight);
        etWeight = findViewById(R.id.etWeight);
        etGoal = findViewById(R.id.etGoal);
        etGender = findViewById(R.id.etGender);
        etAge = findViewById(R.id.etAge);
        cbBeginner = findViewById(R.id.cbBeginner);
        cbAdvanced = findViewById(R.id.cbAdvanced);
        cbCompetitive = findViewById(R.id.cbCompetitive);
        cbSafety = findViewById(R.id.cbSafety);
        cbRehab = findViewById(R.id.cbRehab);
        cbInfants = findViewById(R.id.cbInfants);

        btnRegister = findViewById(R.id.btnRegister);
        btnLogin = findViewById(R.id.btnLogin);
        tvLogin = findViewById(R.id.tvLogin);
    }

    private void setupCitySpinner() {
        ArrayList<String> cities = new ArrayList<>(Arrays.asList(
                "Jerusalem",
                "Tel Aviv",
                "Haifa",
                "Rishon Lezion",
                "Petah Tikva",
                "Ashdod",
                "Netanya",
                "Beersheba",
                "Holon",
                "Bat Yam",
                "Ramat Gan",
                "Herzliya",
                "Rehovot",
                "Nazareth",
                "Eilat",
                "Kfar Saba",
                "Hadera",
                "Modiin",
                "Lod",
                "Ramla"
        ));
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, cities);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCities.setAdapter(adapter);
    }

    private void setupRoleSelection() {
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radioTutor) {
                showTutorFields();
            } else if (checkedId == R.id.radioSwimmer) {
                showSwimmerFields();
            }
        });
    }

    private void showTutorFields() {
        tutorFieldsContainer.setVisibility(View.VISIBLE);
        swimmerFieldsContainer.setVisibility(View.GONE);
        btnRegister.setEnabled(true);
    }

    private void showSwimmerFields() {
        tutorFieldsContainer.setVisibility(View.GONE);
        swimmerFieldsContainer.setVisibility(View.VISIBLE);
        btnRegister.setEnabled(true);
    }

    private void registerUser() {
        // Get basic user information
        String fname = etFirstName.getText().toString().trim();
        String lname = etLastName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String city = spinnerCities.getSelectedItem().toString().trim();
        String gender = etGender.getText().toString().trim();
        String ageStr = etAge.getText().toString().trim();

        // Validate basic inputs
        if (!validateBasicInputs(fname, lname, email, phone, password, city, gender, ageStr)) {
            return;
        }

        // Get role
        String role;
        if (radioGroup.getCheckedRadioButtonId() == R.id.radioTutor) {
            role = "Tutor";
        } else if (radioGroup.getCheckedRadioButtonId() == R.id.radioSwimmer) {
            role = "Swimmer";
        } else {
            Toast.makeText(Register.this, "יש לבחור תפקיד", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validate role-specific fields
        if (role.equals("Tutor")) {
            if (!validateTutorFields()) return;
        } else if (role.equals("Swimmer")) {
            if (!validateSwimmerFields()) return;
        }

        // Create user account
        int age = Integer.parseInt(ageStr);
        createUserAccount(fname, lname, email, phone, password, city, gender, age, role);
    }

    private boolean validateBasicInputs(String fname, String lname, String email, String phone, 
                                      String password, String city, String gender, String ageStr) {
        if (TextUtils.isEmpty(fname)) { etFirstName.setError("יש להזין שם פרטי"); return false; }
        if (TextUtils.isEmpty(lname)) { etLastName.setError("יש להזין שם משפחה"); return false; }
        if (!Validator.isEmailValid(email)) {
            etEmail.setError("אימייל לא תקין"); return false;
        }
        if (!Validator.isPhoneValid(phone)) {
            etPhone.setError("מספר טלפון לא תקין"); return false;
        }
        if (!Validator.isPasswordValid(password)) {
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
        authenticationService.signUp(email, password, new AuthenticationService.AuthCallback<String>() {
            @Override
            public void onCompleted(String userId) {
                switch (role) {
                    case "Tutor":
                        createTutor(userId, fname, lname, email, phone, city, gender, age, password);
                        break;
                    case "Swimmer":
                        createSwimmer(userId, fname, lname, email, phone, city, gender, age, password);
                        break;
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
        Tutor tutor = new Tutor(userId, fname, lname, email, phone, city, gender, age, password,false,
                "Tutor", null, 0, 0, new ArrayList<>());
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
        databaseService.createNewTutor(tutor, new DatabaseService.DatabaseCallback<Void>() {
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

    private void createSwimmer(String userId, String fname, String lname,
                               String email, String phone,
                             String city, String gender, int age, String password) {
        double height = Double.parseDouble(etHeight.getText().toString().trim());
        double weight = Double.parseDouble(etWeight.getText().toString().trim());
        
        Swimmer swimmer = new Swimmer(userId, fname, lname, email, phone, city, gender,
                age,  password, false,  "Swimmer", height, weight);
        swimmer.setGoal(etGoal.getText().toString().trim());

        // Save swimmer data
        databaseService.createNewSwimmer(swimmer, new DatabaseService.DatabaseCallback<Void>() {
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
        Log.d(TAG, "Starting navigation to next screen for role: " + role);
        Toast.makeText(Register.this, "הרשמה בוצעה בהצלחה!", Toast.LENGTH_LONG).show();
        Intent intent;
        switch (role) {
            case "Tutor":
                Log.d(TAG, "Creating intent for TutorPage");
                intent = new Intent(Register.this, TutorPage.class);
                break;
            case "Swimmer":
                Log.d(TAG, "Creating intent for SwimmerPage");
                intent = new Intent(Register.this, SwimmerPage.class);
                break;
            default:
                Log.e(TAG, "Unknown role: " + role);
                return;
        }
        try {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        } catch (Exception e) {
            Log.e(TAG, "Error during navigation", e);
            e.printStackTrace();
        }
    }

    private void navigateToLogin() {
        Intent intent = new Intent(Register.this, Login.class);
        startActivity(intent);
        finish();
    }
}
