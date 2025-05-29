package com.ofekinyo.myswimmingapp.screens;

import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseUser;
import com.ofekinyo.myswimmingapp.R;
import com.ofekinyo.myswimmingapp.adapters.SessionRequestsAdapter;
import com.ofekinyo.myswimmingapp.models.SessionRequest;
import com.ofekinyo.myswimmingapp.services.AuthenticationService;
import com.ofekinyo.myswimmingapp.services.DatabaseService;

import java.util.List;

public class SessionRequests extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SessionRequestsAdapter adapter;
    private AuthenticationService authService;
    private DatabaseService dbService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session_requests);

        recyclerView = findViewById(R.id.recyclerViewRequests);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        authService = AuthenticationService.getInstance();
        dbService = DatabaseService.getInstance();

        adapter = new SessionRequestsAdapter();
        recyclerView.setAdapter(adapter);
        dbService.getPendingRequestsForTutor(new DatabaseService.DatabaseCallback<List<SessionRequest>>() {
            @Override
            public void onCompleted(List<SessionRequest> sessionRequests) {
                adapter.setRequestList(sessionRequests);
            }

            @Override
            public void onFailed(Exception e) {

            }
        });
    }
}
