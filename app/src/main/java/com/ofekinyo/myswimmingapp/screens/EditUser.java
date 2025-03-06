package com.ofekinyo.myswimmingapp.screens;

import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.*;
import com.ofekinyo.myswimmingapp.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditUser extends AppCompatActivity {

    private Spinner spinnerUsers;
    private EditText etFirstName, etLastName, etEmail, etPhone, etRole;
    private Button btnSaveChanges;
    private DatabaseReference usersRef;
    private Map<String, String> userIdMap = new HashMap<>();
    private String selectedUserId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.ofekinyo.myswimmingapp.R.layout.activity_edit_user);

        usersRef = FirebaseDatabase.getInstance().getReference("Users");

        spinnerUsers = findViewById(com.ofekinyo.myswimmingapp.R.id.spinnerUsers);
        etFirstName = findViewById(com.ofekinyo.myswimmingapp.R.id.etFirstName);
        etLastName = findViewById(com.ofekinyo.myswimmingapp.R.id.etLastName);
        etEmail = findViewById(com.ofekinyo.myswimmingapp.R.id.etEmail);
        etPhone = findViewById(com.ofekinyo.myswimmingapp.R.id.etPhone);
        etRole = findViewById(com.ofekinyo.myswimmingapp.R.id.etRole); // Read-only field
        btnSaveChanges = findViewById(R.id.btnSaveChanges);

        loadUsers();

        spinnerUsers.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String userName = parent.getItemAtPosition(position).toString();
                selectedUserId = userIdMap.get(userName);
                if (selectedUserId != null) {
                    loadUserData(selectedUserId);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        btnSaveChanges.setOnClickListener(v -> saveUserData());
    }

    private void loadUsers() {
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<String> userNames = new ArrayList<>();
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    String userId = userSnapshot.getKey();
                    String fullName = userSnapshot.child("firstName").getValue(String.class) + " " +
                            userSnapshot.child("lastName").getValue(String.class);
                    userNames.add(fullName);
                    userIdMap.put(fullName, userId);
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(EditUser.this, android.R.layout.simple_spinner_dropdown_item, userNames);
                spinnerUsers.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(EditUser.this, "Failed to load users", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadUserData(String userId) {
        usersRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                etFirstName.setText(snapshot.child("firstName").getValue(String.class));
                etLastName.setText(snapshot.child("lastName").getValue(String.class));
                etEmail.setText(snapshot.child("email").getValue(String.class));
                etPhone.setText(snapshot.child("phone").getValue(String.class));
                etRole.setText(snapshot.child("role").getValue(String.class)); // Display role
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private void saveUserData() {
        if (selectedUserId == null) return;

        Map<String, Object> updates = new HashMap<>();
        updates.put("firstName", etFirstName.getText().toString());
        updates.put("lastName", etLastName.getText().toString());
        updates.put("email", etEmail.getText().toString());
        updates.put("phone", etPhone.getText().toString());

        usersRef.child(selectedUserId).updateChildren(updates).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(EditUser.this, "User updated", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
