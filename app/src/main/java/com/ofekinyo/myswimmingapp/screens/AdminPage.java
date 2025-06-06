package com.ofekinyo.myswimmingapp.screens;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;

import com.ofekinyo.myswimmingapp.R;
import com.ofekinyo.myswimmingapp.base.BaseActivity;
import com.ofekinyo.myswimmingapp.services.AuthenticationService;

public class AdminPage extends BaseActivity {

    private static final String TAG = "AdminPage";
    private AuthenticationService authService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate started");
        
        try {
            setContentView(R.layout.activity_admin_page);
            Log.d(TAG, "setContentView completed");
            
            setupToolbar("SwimLink");
            Log.d(TAG, "Toolbar setup completed");

            authService = AuthenticationService.getInstance();
            Log.d(TAG, "AuthService initialized");

            initializeButtons();
            Log.d(TAG, "Button initialization completed");
            
        } catch (Exception e) {
            Log.e(TAG, "Error in onCreate", e);
            Toast.makeText(this, "שגיאה בטעינת הדף", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            
            // Attempt to recover by returning to login
            Intent intent = new Intent(this, Login.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
    }

    private void initializeButtons() {
        try {
            // Initialize buttons
            Button editUserButton = findViewById(R.id.btnUsersList);
            Button deleteUserButton = findViewById(R.id.btnDeleteUser);
            Button addUserButton = findViewById(R.id.btnAddUser);

            Log.d(TAG, "All buttons found successfully");

            // Navigate to EditUser Activity
            editUserButton.setOnClickListener(v -> {
                Log.d(TAG, "Edit user button clicked");
                Intent intent = new Intent(AdminPage.this, UsersList.class);
                startActivity(intent);
            });

            // Delete User Button functionality
            deleteUserButton.setOnClickListener(v -> {
                Log.d(TAG, "Delete user button clicked");
                Intent intent = new Intent(AdminPage.this, DeleteUser.class);
                startActivity(intent);
            });

            // Add User Button functionality
            addUserButton.setOnClickListener(v -> {
                Log.d(TAG, "Add user button clicked");
                Intent intent = new Intent(AdminPage.this, Register.class);
                intent.putExtra("admin_verified", true);
                startActivity(intent);
            });

            Log.d(TAG, "All button click listeners set up successfully");
            
        } catch (Exception e) {
            Log.e(TAG, "Error initializing buttons", e);
            throw e; // Re-throw to be caught by the outer try-catch
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume called");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.d(TAG, "Back button pressed");
        new AlertDialog.Builder(this)
                .setTitle("יציאה")
                .setMessage("האם אתה רוצה לצאת מהאפליקציה?")
                .setPositiveButton("כן", (dialog, which) -> {
                    Log.d(TAG, "User confirmed exit");
                    finishAffinity(); // This will close all activities and exit the app
                })
                .setNegativeButton("ביטול", (dialog, which) -> {
                    Log.d(TAG, "User cancelled exit");
                    dialog.dismiss();
                })
                .show();
    }
}
