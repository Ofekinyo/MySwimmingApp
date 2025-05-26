package com.ofekinyo.myswimmingapp.screens;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ofekinyo.myswimmingapp.R;
import com.ofekinyo.myswimmingapp.adapters.UserAdapter;
import com.ofekinyo.myswimmingapp.models.User;
import com.ofekinyo.myswimmingapp.services.DatabaseService;

import java.util.ArrayList;
import java.util.List;

public class UsersList extends AppCompatActivity {

    private static final String TAG = "UsersList";
    private RecyclerView recyclerView;
    private UserAdapter adapter;
    private List<User> users;
    private DatabaseService databaseService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_list);  // ✅ This layout must match the modern one we wrote earlier

        // Initialize Firebase service and RecyclerView
        databaseService = DatabaseService.getInstance();
        recyclerView = findViewById(R.id.recyclerViewUsers);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        users = new ArrayList<>();

        // Fetch users from Firebase
        databaseService.getUserList(new DatabaseService.DatabaseCallback<List<User>>() {
            @Override
            public void onCompleted(List<User> us) {
                Log.d(TAG, "onCompleted: successfully received users");
                users = us;
                adapter = new UserAdapter(UsersList.this, users);  // ✅ Uses modern card layout (user_item.xml)
                recyclerView.setAdapter(adapter);

                for (User u : users) {
                    Log.d(TAG, u.getFname() + " loaded");
                }
            }

            @Override
            public void onFailed(Exception e) {
                Log.e(TAG, "onFailed: failed to receive users", e);
            }
        });
    }
}
