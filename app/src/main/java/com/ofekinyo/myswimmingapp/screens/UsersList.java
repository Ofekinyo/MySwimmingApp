package com.ofekinyo.myswimmingapp.screens;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ofekinyo.myswimmingapp.R;
import com.ofekinyo.myswimmingapp.adapters.UserAdapter;
import com.ofekinyo.myswimmingapp.models.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
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
        setContentView(R.layout.activity_users_list);

        databaseService = DatabaseService.getInstance();

        recyclerView = findViewById(R.id.recyclerViewUsers);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        users = new ArrayList<>();

        databaseService.getUserList(new DatabaseService.DatabaseCallback<List<User>>() {
            @Override
            public void onCompleted(List<User> us) {
                Log.d(TAG, "onCompleted: successfully received users");
                users = us;
                adapter = new UserAdapter(UsersList.this, users);
                recyclerView.setAdapter(adapter);

                for(User u : users){
                    Log.d(TAG, u.getFname() + " SIGMAAAAAAAAA");
                }
            }

            @Override
            public void onFailed(Exception e) {
                Log.e(TAG, "onFailed: failed to receive users");
            }
        });
    }
}
