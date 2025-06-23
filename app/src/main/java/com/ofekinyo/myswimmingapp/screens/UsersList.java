package com.ofekinyo.myswimmingapp.screens;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ofekinyo.myswimmingapp.R;
import com.ofekinyo.myswimmingapp.adapters.UserAdapter;
import com.ofekinyo.myswimmingapp.base.BaseActivity;
import com.ofekinyo.myswimmingapp.models.Swimmer;
import com.ofekinyo.myswimmingapp.models.Tutor;
import com.ofekinyo.myswimmingapp.models.User;
import com.ofekinyo.myswimmingapp.services.AuthenticationService;
import com.ofekinyo.myswimmingapp.services.DatabaseService;

import java.util.ArrayList;
import java.util.List;

public class UsersList extends BaseActivity {

    private static final String TAG = "UsersList";
    private RecyclerView recyclerView;
    private UserAdapter adapter;
    private List<User> users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_list);
        setupToolbar("רשימת משתמשים");

        // Verify admin privileges first
        verifyAdminAccess();
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
                    initializeViews();
                } else {
                    // User is not admin, redirect to login
                    Toast.makeText(UsersList.this, "Access denied. Admin privileges required.", Toast.LENGTH_LONG).show();
                    authenticationService.signOut();
                    Intent intent = new Intent(UsersList.this, Login.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onFailed(Exception e) {
                Log.e(TAG, "Error verifying admin status", e);
                Toast.makeText(UsersList.this, "Error verifying admin status", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(UsersList.this, Login.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
    }

    private void initializeViews() {
        recyclerView = findViewById(R.id.recyclerViewUsers);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        
        users = new ArrayList<>();
        adapter = new UserAdapter(this, new UserAdapter.OnUserClickListener() {
            @Override
            public <T extends User> void OnClickSave(T user, int position) {

                DatabaseService.DatabaseCallback<Void> callback = new DatabaseService.DatabaseCallback<Void>() {
                    @Override
                    public void onCompleted(Void object) {
                        adapter.updateUser(user, position);
                    }

                    @Override
                    public void onFailed(Exception e) {

                    }
                };
                switch (user.getRole()) {
                    case "Tutor":
                        databaseService.createNewTutor((Tutor) user, callback);
                        break;
                    case "Swimmer":
                        databaseService.createNewSwimmer((Swimmer) user, callback);
                        break;
                }
            }
        });
        recyclerView.setAdapter(adapter);
        
        loadUsers();
    }

    private void loadUsers() {
        // Fetch users from Firebase
        databaseService.getAllTutors(new DatabaseService.DatabaseCallback<List<Tutor>>() {
            @Override
            public void onCompleted(List<Tutor> tutors) {
                adapter.addUsers(tutors);
            }

            @Override
            public void onFailed(Exception e) {

            }
        });
        databaseService.getAllSwimmers(new DatabaseService.DatabaseCallback<List<Swimmer>>() {
            @Override
            public void onCompleted(List<Swimmer> swimmers) {
                adapter.addUsers(swimmers);
            }

            @Override
            public void onFailed(Exception e) {

            }
        });
    }
}
