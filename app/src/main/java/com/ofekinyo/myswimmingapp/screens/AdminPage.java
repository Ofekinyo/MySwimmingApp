package com.ofekinyo.myswimmingapp.screens;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ofekinyo.myswimmingapp.R;

public class AdminPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_page);

        // Initialize buttons
        Button editUserButton = findViewById(R.id.btnEditUser);
        Button deleteUserButton = findViewById(R.id.btnDeleteUser);
        Button addUserButton = findViewById(R.id.btnAddUser);
        Button disableUserButton = findViewById(R.id.btnDisableUser);

        // Edit User Button functionality
        editUserButton.setOnClickListener(v -> {
            // Implement user editing logic (e.g., open a form to edit user details)
            Toast.makeText(AdminPage.this, "Edit User clicked", Toast.LENGTH_SHORT).show();
            // Navigate to another activity or show edit form
        });

        // Delete User Button functionality
        deleteUserButton.setOnClickListener(v -> {
            // Implement user deletion logic
            Toast.makeText(AdminPage.this, "Delete User clicked", Toast.LENGTH_SHORT).show();
            // Delete selected user or show a confirmation dialog
        });

        // Add New User Button functionality
        addUserButton.setOnClickListener(v -> {
            // Implement add new user functionality
            Toast.makeText(AdminPage.this, "Add New User clicked", Toast.LENGTH_SHORT).show();
            // Open form to add a new user
        });

        // Disable User Button functionality
        disableUserButton.setOnClickListener(v -> {
            // Implement disable user functionality
            Toast.makeText(AdminPage.this, "Disable User clicked", Toast.LENGTH_SHORT).show();
            // Disable selected user (could be marked as inactive in database)
        });
    }
}
