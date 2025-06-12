package com.ofekinyo.myswimmingapp.screens;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ofekinyo.myswimmingapp.R;
import com.ofekinyo.myswimmingapp.adapters.SessionAdapter;
import com.ofekinyo.myswimmingapp.base.BaseActivity;
import com.ofekinyo.myswimmingapp.models.Session;
import com.ofekinyo.myswimmingapp.services.AuthenticationService;
import com.ofekinyo.myswimmingapp.services.DatabaseService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SessionRequests extends BaseActivity {
    private static final String TAG = "SessionRequests";
    private RecyclerView recyclerView;
    private SessionAdapter adapter;
    private AuthenticationService authService;
    private DatabaseService dbService;
    private FirebaseAuth mAuth;
    private ExecutorService executorService;
    private View progressBar;
    private View emptyStateLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session_requests);
        setupToolbar("SwimLink");

        try {
            // Initialize services
            executorService = Executors.newSingleThreadExecutor();
            mAuth = FirebaseAuth.getInstance();
            authService = AuthenticationService.getInstance();
            dbService = DatabaseService.getInstance();

            // Initialize UI
            recyclerView = findViewById(R.id.recyclerView);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            adapter = new SessionAdapter(new ArrayList<>(), this);
            recyclerView.setAdapter(adapter);

            progressBar = findViewById(R.id.progressBar);
            emptyStateLayout = findViewById(R.id.emptyStateLayout);

            // Check authentication
            FirebaseUser currentUser = mAuth.getCurrentUser();
            if (currentUser == null) {
                Toast.makeText(this, "לא נמצא משתמש מחובר", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }

            // Load requests in background
            loadSessions();

        } catch (Exception e) {
            Log.e(TAG, "Error in onCreate", e);
            Toast.makeText(this, "שגיאה בטעינת המסך: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            finish();
        }
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
        dbService.getSessions(userId, new DatabaseService.DatabaseCallback<List<Session>>() {
            @Override
            public void onCompleted(List<Session> sessions) {
                progressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);

                if (sessions == null || sessions.isEmpty()) {
                    // Show empty state
                    recyclerView.setVisibility(View.GONE);
                    emptyStateLayout.setVisibility(View.VISIBLE);
                } else {
                    // Show sessions
                    recyclerView.setVisibility(View.VISIBLE);
                    emptyStateLayout.setVisibility(View.GONE);
                    adapter.setRequestList(sessions);
                }
            }

            @Override
            public void onFailed(Exception e) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(SessionRequests.this, "Error loading sessions", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (executorService != null) {
            executorService.shutdown();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, TutorPage.class));
        finish();
    }
}
