package com.ofekinyo.myswimmingapp.screens;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.ofekinyo.myswimmingapp.R;
import com.ofekinyo.myswimmingapp.adapters.SessionAdapter;
import com.ofekinyo.myswimmingapp.models.SessionRequest;

import java.util.ArrayList;
import java.util.List;

public class TraineeScheduleActivity extends AppCompatActivity {
    private RecyclerView rvUpcomingSessions, rvPendingRequests;
    private SessionAdapter upcomingSessionsAdapter, pendingRequestsAdapter;
    private List<SessionRequest> upcomingSessionsList, pendingRequestsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainee_schedule);

        // Initialize UI components
        rvUpcomingSessions = findViewById(R.id.rvUpcomingSessions);
        rvPendingRequests = findViewById(R.id.rvPendingRequests);

        // Setup RecyclerViews
        rvUpcomingSessions.setLayoutManager(new LinearLayoutManager(this));
        rvPendingRequests.setLayoutManager(new LinearLayoutManager(this));

        // Example Data (Replace with Firebase data)
        upcomingSessionsList = new ArrayList<>();
        pendingRequestsList = new ArrayList<>();

        // Example upcoming session
        upcomingSessionsList.add(new SessionRequest("Speed Training", "45 minutes", "2025-03-25", "Coach John"));

        // Example pending request
        pendingRequestsList.add(new SessionRequest("Endurance Training", "60 minutes", "Pending Approval", "Coach Sarah"));

        // Setup Adapters
        upcomingSessionsAdapter = new SessionAdapter(upcomingSessionsList);
        pendingRequestsAdapter = new SessionAdapter(pendingRequestsList);

        rvUpcomingSessions.setAdapter(upcomingSessionsAdapter);
        rvPendingRequests.setAdapter(pendingRequestsAdapter);
    }
}
