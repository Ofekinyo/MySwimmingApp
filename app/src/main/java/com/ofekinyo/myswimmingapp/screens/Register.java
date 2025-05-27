package com.ofekinyo.myswimmingapp.screens;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ofekinyo.myswimmingapp.R;
import com.ofekinyo.myswimmingapp.models.User;
import com.ofekinyo.myswimmingapp.utils.SharedPreferencesUtil;

import java.util.ArrayList;
import java.util.Arrays;

public class Register extends AppCompatActivity {

    private EditText etFName, etLName, etEmail, etPhone, etPassword, etGender, etAge;
    private Button btnRegister;
    private Spinner spCity;
    private RadioButton radioTrainer, radioTrainee;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        initViews();
        setupCitySpinner();

        btnRegister.setOnClickListener(v -> {
            String fname = etFName.getText().toString().trim();
            String lname = etLName.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String phone = etPhone.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String city = spCity.getSelectedItem().toString().trim();
            String gender = etGender.getText().toString().trim();
            String ageStr = etAge.getText().toString().trim();

            if (!validateInputs(fname, lname, email, phone, password, city, gender, ageStr)) {
                return;
            }

            String role = radioTrainer.isChecked() ? "Trainer" : radioTrainee.isChecked() ? "Trainee" : "";
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
        spCity = findViewById(R.id.spinnerCities);
        etGender = findViewById(R.id.etGender);
        etAge = findViewById(R.id.etAge);
        btnRegister = findViewById(R.id.btnRegister);
        radioTrainer = findViewById(R.id.radioTrainer);
        radioTrainee = findViewById(R.id.radioTrainee);
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

    private boolean validateInputs(String fname, String lname, String email, String phone, String password, String city, String gender, String ageStr) {
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

    private void registerUser(String fname, String lname, String email, String phone, String password, String city, String gender, int age, String role) {
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    String userId = mAuth.getCurrentUser().getUid();
                    String userPath = role.equals("Trainer") ? "Trainers" : "Trainees";
                    DatabaseReference roleDatabaseRef = FirebaseDatabase.getInstance().getReference(userPath);

                    User user = new User(userId, fname, lname,  phone+"", email, age, gender, city,password,role);
                    SharedPreferencesUtil.saveUser(this,user);
                    roleDatabaseRef.child(userId).setValue(user)
                        .addOnCompleteListener(dbTask -> {
                            if (dbTask.isSuccessful()) {
                                Toast.makeText(Register.this, "הרשמה בוצעה בהצלחה!", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(Register.this, role.equals("Trainer") ? TutorDetailsActivity.class : SwimmerDetailsActivity.class);
                                startActivity(intent);
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


}
