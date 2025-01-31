package com.ofekinyo.myswimmingapp.screens;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ofekinyo.myswimmingapp.R;

import java.util.ArrayList;
import java.util.Arrays;

public class Register extends AppCompatActivity {

    private EditText etFName;
    private EditText etLName;
    private EditText etEmail;
    private EditText etPhone;
    private EditText etPassword;
    private EditText etGender;
    private EditText etAge;
    private Button btnRegister;
    private Spinner spCity;
    private RadioButton radioTrainer;
    private RadioButton radioTrainee;

    private FirebaseAuth mAuth;
    private DatabaseReference userDatabaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        userDatabaseRef = FirebaseDatabase.getInstance().getReference("users");

        // Initialize views
        initViews();

        // Set up city spinner
        setupCitySpinner();

        // Set up register button click listener
        btnRegister.setOnClickListener(v -> {
            String fname = etFName.getText().toString().trim();
            String lname = etLName.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String phone = etPhone.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String city = spCity.getSelectedItem().toString().trim();  // Get the selected city from spinner
            String gender = etGender.getText().toString().trim();
            String ageStr = etAge.getText().toString().trim();

            if (!validateInputs(fname, lname, email, phone, password, city, gender, ageStr)) {
                return;
            }

            // Get the selected role
            String role = "";
            if (radioTrainer.isChecked()) {
                role = "Trainer";
            } else if (radioTrainee.isChecked()) {
                role = "Trainee";
            }

            if (role.isEmpty()) {
                Toast.makeText(Register.this, "יש לבחור תפקיד", Toast.LENGTH_SHORT).show();
                return;
            }

            int age = Integer.parseInt(ageStr);
            registerUser(fname, lname, email, phone, password, city, gender, age, role);
        });
    }

    private void initViews() {
        etFName = findViewById(R.id.etFirstName);
        etLName = findViewById(R.id.etLastName);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        etPassword = findViewById(R.id.etPassword);
        spCity = findViewById(R.id.spinnerCities);  // Use the Spinner for city
        etGender = findViewById(R.id.etGender);
        etAge = findViewById(R.id.etAge);
        btnRegister = findViewById(R.id.btnRegister);

        // Initialize role RadioButtons
        RadioGroup radioGroupRole = findViewById(R.id.radioGroupRole);
        radioTrainer = findViewById(R.id.radioTrainer);
        radioTrainee = findViewById(R.id.radioTrainee);
    }

    private void setupCitySpinner() {
        // List of cities in Israel
        ArrayList<String> cities = new ArrayList<>(Arrays.asList(
                "Tel Aviv", "Jerusalem", "Haifa", "Eilat", "Beersheba", "Nazareth",
                "Petah Tikva", "Ashdod", "Netanya", "Rishon Lezion"
        ));

        // Create an ArrayAdapter for the spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, cities);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCity.setAdapter(adapter);

        // You can add an item selected listener to handle events when a city is selected (optional)
        spCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Handle city selection if needed
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Handle no city selected if needed
            }
        });
    }

    private boolean validateInputs(String fname, String lname, String email, String phone, String password, String city, String gender, String ageStr) {
        if (TextUtils.isEmpty(fname)) {
            etFName.setError("יש להזין שם פרטי");
            return false;
        }
        if (TextUtils.isEmpty(lname)) {
            etLName.setError("יש להזין שם משפחה");
            return false;
        }
        if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("אימייל לא תקין");
            return false;
        }
        if (TextUtils.isEmpty(phone) || !phone.matches("\\d{10}")) {
            etPhone.setError("מספר טלפון לא תקין (חייב להיות 10 ספרות)");
            return false;
        }
        if (TextUtils.isEmpty(password) || password.length() < 6) {
            etPassword.setError("הסיסמה חייבת להיות לפחות 6 תווים");
            return false;
        }
        // Validate the city selection from the spinner
        if (TextUtils.isEmpty(city) || city.equals("בחר עיר")) {  // or add your default "Please select" option to match the first item
            Toast.makeText(Register.this, "יש להזין עיר", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(gender)) {
            etGender.setError("יש להזין מגדר");
            return false;
        }
        if (TextUtils.isEmpty(ageStr) || !ageStr.matches("\\d+") || Integer.parseInt(ageStr) < 1) {
            etAge.setError("יש להזין גיל חוקי");
            return false;
        }
        return true;
    }

    private void registerUser(String fname, String lname, String email, String phone, String password, String city, String gender, int age, String role) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Save user data to Firebase Realtime Database
                        String userId = mAuth.getCurrentUser().getUid();
                        User user = new User(fname, lname, email, phone, city, gender, age, role);
                        userDatabaseRef.child(userId).setValue(user)
                                .addOnCompleteListener(dbTask -> {
                                    if (dbTask.isSuccessful()) {
                                        Toast.makeText(Register.this, "הרשמה בוצעה בהצלחה!", Toast.LENGTH_LONG).show();
                                        // Navigate to the next page based on role
                                        if (role.equals("Trainer")) {
                                            startActivity(new Intent(Register.this, TrainerDetailsActivity.class));
                                        } else {
                                            startActivity(new Intent(Register.this, TraineeDetailsActivity.class));
                                        }
                                        finish();
                                    } else {
                                        Toast.makeText(Register.this, "שגיאה בשמירת נתוני המשתמש", Toast.LENGTH_LONG).show();
                                    }
                                });
                    } else {
                        Log.e("joe", task.getException().getMessage());
                        Toast.makeText(Register.this, "שגיאה בהרשמה: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    // User class for Firebase database
    public static class User {
        public String fname, lname, email, phone, city, gender, role;
        public int age;

        public User() {
            // Default constructor required for calls to DataSnapshot.getValue(User.class)
        }

        public User(String fname, String lname, String email, String phone, String city, String gender, int age, String role) {
            this.fname = fname;
            this.lname = lname;
            this.email = email;
            this.phone = phone;
            this.city = city;
            this.gender = gender;
            this.age = age;
            this.role = role;
        }
    }
}
