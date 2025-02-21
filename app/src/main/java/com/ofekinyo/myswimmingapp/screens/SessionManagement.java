package com.ofekinyo.myswimmingapp.screens;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;
import com.ofekinyo.myswimmingapp.R;
import com.ofekinyo.myswimmingapp.adapters.SessionsManagementAdapter;
import com.ofekinyo.myswimmingapp.models.Session;

import java.util.ArrayList;
import java.util.List;

public class SessionManagement extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SessionsManagementAdapter sessionAdapter;
    private List<Session> sessionList;
    private DatabaseReference databaseReference;

    private EditText etTitle, etDate, etTime, etLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session_management);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        sessionList = new ArrayList<>();
        sessionAdapter = new SessionsManagementAdapter(sessionList, this);
        recyclerView.setAdapter(sessionAdapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("TrainerSessions");

        etTitle = findViewById(R.id.etTitle);
        etDate = findViewById(R.id.etDate);
        etTime = findViewById(R.id.etTime);
        etLocation = findViewById(R.id.etLocation);

        Button btnAddSession = findViewById(R.id.btnAddSession);
        btnAddSession.setOnClickListener(v -> addSession());

        fetchSessions();
    }

    private void fetchSessions() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                sessionList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Session session = snapshot.getValue(Session.class);
                    if (session != null) {
                        sessionList.add(session);
                    }
                }
                sessionAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(SessionManagement.this, "Error fetching sessions", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addSession() {
        String title = etTitle.getText().toString().trim();
        String date = etDate.getText().toString().trim();
        String time = etTime.getText().toString().trim();
        String location = etLocation.getText().toString().trim();

        if (title.isEmpty() || date.isEmpty() || time.isEmpty() || location.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        String sessionId = databaseReference.push().getKey();
        Session newSession = new Session(sessionId, title, date, time, location);
        databaseReference.child(sessionId).setValue(newSession).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(SessionManagement.this, "Session added successfully", Toast.LENGTH_SHORT).show();
                clearInputFields();
            } else {
                Toast.makeText(SessionManagement.this, "Failed to add session", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void clearInputFields() {
        etTitle.setText("");
        etDate.setText("");
        etTime.setText("");
        etLocation.setText("");
    }
}
