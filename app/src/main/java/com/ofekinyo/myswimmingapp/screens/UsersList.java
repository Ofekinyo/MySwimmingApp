package com.ofekinyo.myswimmingapp.screens;

import android.os.Bundle;
import android.util.Log;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ofekinyo.myswimmingapp.R;
import com.ofekinyo.myswimmingapp.adapters.UserAdapter;
import com.ofekinyo.myswimmingapp.base.BaseActivity;
import com.ofekinyo.myswimmingapp.models.Admin;
import com.ofekinyo.myswimmingapp.models.Swimmer;
import com.ofekinyo.myswimmingapp.models.Tutor;
import com.ofekinyo.myswimmingapp.models.User;
import com.ofekinyo.myswimmingapp.services.DatabaseService;

import java.util.ArrayList;
import java.util.List;

public class UsersList extends BaseActivity {

    private static final String TAG = "UsersList";
    private RecyclerView recyclerView;
    private UserAdapter adapter;
    private List<User> users;
    private DatabaseService databaseService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_list);
        setupToolbar("רשימת משתמשים");

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
                    case "Admin":
                        databaseService.createNewAdmin((Admin) user, callback);
                        break;
                }
            }
        });
        recyclerView.setAdapter(adapter);
        
        databaseService = DatabaseService.getInstance();
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
        databaseService.getAllAdmins(new DatabaseService.DatabaseCallback<List<Admin>>() {
            @Override
            public void onCompleted(List<Admin> admins) {
                adapter.addUsers(admins);
            }

            @Override
            public void onFailed(Exception e) {

            }
        });
    }
}
