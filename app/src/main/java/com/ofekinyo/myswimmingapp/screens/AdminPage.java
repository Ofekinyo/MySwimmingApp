package com.ofekinyo.myswimmingapp.screens;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.ofekinyo.myswimmingapp.R;

public class AdminPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_page);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("SwimLink");
        // Initialize buttons
        Button editUserButton = findViewById(R.id.btnUsersList);
        Button deleteUserButton = findViewById(R.id.btnDeleteUser);
        Button addUserButton = findViewById(R.id.btnAddUser);

        // Navigate to EditUser Activity
        editUserButton.setOnClickListener(v -> {
            Intent intent = new Intent(AdminPage.this, UsersList.class);
            startActivity(intent);
        });

        // Delete User Button functionality
        deleteUserButton.setOnClickListener(v -> {
            Intent intent = new Intent(AdminPage.this, DeleteUser.class);
            startActivity(intent);
        });
    }
}
