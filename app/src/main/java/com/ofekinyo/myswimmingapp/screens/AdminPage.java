package com.ofekinyo.myswimmingapp.screens;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.ofekinyo.myswimmingapp.R;
import com.ofekinyo.myswimmingapp.base.BaseActivity;
import com.ofekinyo.myswimmingapp.models.User;
import com.ofekinyo.myswimmingapp.services.AuthenticationService;
import com.ofekinyo.myswimmingapp.services.DatabaseService;

public class AdminPage extends BaseActivity {

    private static final String TAG = "AdminPage";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate started");
        
        try {
            setContentView(R.layout.activity_admin_page);
            Log.d(TAG, "setContentView completed");
            
            setupToolbar("SwimLink");
            Log.d(TAG, "Toolbar setup completed");

            Log.d(TAG, "AuthService initialized");

            // Verify admin privileges
            verifyAdminAccess();
            
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

    private void verifyAdminAccess() {
        String currentUserId = AuthenticationService.getInstance().getCurrentUserId();
        if (currentUserId == null) {
            // User not logged in, redirect to login
            Intent intent = new Intent(this, Login.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            return;
        }

        databaseService.getUser(currentUserId, new DatabaseService.DatabaseCallback<User>() {
            @Override
            public void onCompleted(User user) {
                if (user != null && user.getAdmin() != null && user.getAdmin()) {
                    // User is admin, proceed with initialization
                    initializeButtons();
                    Log.d(TAG, "Button initialization completed");
                } else {
                    // User is not admin, redirect to login
                    Toast.makeText(AdminPage.this, "Access denied. Admin privileges required.", Toast.LENGTH_LONG).show();
                    authenticationService.signOut();
                    Intent intent = new Intent(AdminPage.this, Login.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onFailed(Exception e) {
                Log.e(TAG, "Error verifying admin status", e);
                Toast.makeText(AdminPage.this, "Error verifying admin status", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(AdminPage.this, Login.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
    }

    private void initializeButtons() {
        try {
            // Initialize buttons
            Button editUserButton = findViewById(R.id.btnUsersList);
            Button addUserButton = findViewById(R.id.btnAddUser);
            Button btnSchedule = findViewById(R.id.btnAdminSchedule);

            Log.d(TAG, "All buttons found successfully");

            // Navigate to EditUser Activity
            editUserButton.setOnClickListener(v -> {
                Intent intent = new Intent(AdminPage.this, UsersList.class);
                startActivity(intent);
            });

            // Add User Button functionality
            addUserButton.setOnClickListener(v -> {
                Intent intent = new Intent(AdminPage.this, Register.class);
                intent.putExtra("admin_verified", true);
                startActivity(intent);
            });

            btnSchedule.setOnClickListener(v -> {
                Intent intent = new Intent(AdminPage.this, ScheduleActivity.class);
                intent.putExtra("admin_access", true);
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
}
