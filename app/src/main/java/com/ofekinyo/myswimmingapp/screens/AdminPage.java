package com.ofekinyo.myswimmingapp.screens;

import android.content.Intent;
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
        Button editUserButton = findViewById(R.id.btnUsersList);
        Button deleteUserButton = findViewById(R.id.btnDeleteUser);
        Button addUserButton = findViewById(R.id.btnAddUser);
        Button disableUserButton = findViewById(R.id.btnDisableUser);

        // Navigate to EditUser Activity
        editUserButton.setOnClickListener(v -> {
            Intent intent = new Intent(AdminPage.this, UsersList.class);
            startActivity(intent);
        });

        // Delete User Button functionality
        deleteUserButton.setOnClickListener(v -> {
            Toast.makeText(AdminPage.this, "Delete User clicked", Toast.LENGTH_SHORT).show();
        });

        // Add New User Button functionality
        addUserButton.setOnClickListener(v -> {
            Toast.makeText(AdminPage.this, "Add New User clicked", Toast.LENGTH_SHORT).show();
        });

        // Disable User Button functionality
        disableUserButton.setOnClickListener(v -> {
            Toast.makeText(AdminPage.this, "Disable User clicked", Toast.LENGTH_SHORT).show();
        });
    }
}
