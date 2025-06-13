package com.ofekinyo.myswimmingapp.screens;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ofekinyo.myswimmingapp.R;
import com.ofekinyo.myswimmingapp.adapters.SessionAdapter;
import com.ofekinyo.myswimmingapp.base.BaseActivity;
import com.ofekinyo.myswimmingapp.models.Session;
import com.ofekinyo.myswimmingapp.models.Swimmer;
import com.ofekinyo.myswimmingapp.models.Tutor;
import com.ofekinyo.myswimmingapp.services.AuthenticationService;
import com.ofekinyo.myswimmingapp.services.DatabaseService;

import java.util.List;


public class SessionRequests extends BaseActivity {
    private static final String TAG = "SessionRequests";
    private RecyclerView recyclerView;
    private SessionAdapter adapter;
    private View progressBar;
    private View emptyStateLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session_requests);
        setupToolbar("SwimLink");


        // Initialize UI
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SessionAdapter(this);
        recyclerView.setAdapter(adapter);

        progressBar = findViewById(R.id.progressBar);
        emptyStateLayout = findViewById(R.id.emptyStateLayout);

        // Check authentication
        if (!authenticationService.isUserSignedIn()) {
            Toast.makeText(this, "לא נמצא משתמש מחובר", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Load requests in background
        loadSessions();

    }

    private void loadSessions() {
        String userId = getIntent().getStringExtra("userId");
        if (userId == null) {
            Toast.makeText(this, "Error: User ID not found", Toast.LENGTH_SHORT).show();
            return;
        }

        // Show loading indicator
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);

        // Load sessions
        databaseService.getSessions(new DatabaseService.DatabaseCallback<List<Session>>() {
            @Override
            public void onCompleted(List<Session> sessions) {
                sessions.removeIf(session -> !session.getTutorId().equals(userId) && session.getIsAccepted() != null);
                adapter.setRequestList(sessions);

                progressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailed(Exception e) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(SessionRequests.this, "Error loading sessions", Toast.LENGTH_SHORT).show();
            }
        });

        databaseService.getAllTutors(new DatabaseService.DatabaseCallback<List<Tutor>>() {
            @Override
            public void onCompleted(List<Tutor> tutors) {
                adapter.setTutorList(tutors);
            }

            @Override
            public void onFailed(Exception e) {

            }
        });

        databaseService.getAllSwimmers(new DatabaseService.DatabaseCallback<List<Swimmer>>() {
            @Override
            public void onCompleted(List<Swimmer> swimmers) {
                adapter.setSwimmerList(swimmers);
            }

            @Override
            public void onFailed(Exception e) {

            }
        });
    }

}
