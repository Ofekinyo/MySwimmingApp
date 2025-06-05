package com.ofekinyo.myswimmingapp.screens;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ofekinyo.myswimmingapp.R;
import com.ofekinyo.myswimmingapp.adapters.SessionRequestsAdapter;
import com.ofekinyo.myswimmingapp.base.BaseActivity;
import com.ofekinyo.myswimmingapp.models.SessionRequest;
import com.ofekinyo.myswimmingapp.services.AuthenticationService;
import com.ofekinyo.myswimmingapp.services.DatabaseService;

import java.util.ArrayList;
import java.util.List;

public class SessionRequests extends BaseActivity {
    private static final String TAG = "SessionRequests";
    private RecyclerView recyclerView;
    private SessionRequestsAdapter adapter;
    private AuthenticationService authService;
    private DatabaseService dbService;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session_requests);
        setupToolbar("SwimLink");

        try {
            // Initialize Firebase Auth
            mAuth = FirebaseAuth.getInstance();
            FirebaseUser currentUser = mAuth.getCurrentUser();
            if (currentUser == null) {
                Toast.makeText(this, "לא נמצא משתמש מחובר", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }

            recyclerView = findViewById(R.id.recyclerViewRequests);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));

            authService = AuthenticationService.getInstance();
            dbService = DatabaseService.getInstance();

            adapter = new SessionRequestsAdapter();
            recyclerView.setAdapter(adapter);

            // Get requests for the current tutor
            String tutorId = currentUser.getUid();
            Log.d(TAG, "Loading requests for tutor: " + tutorId);
            
            dbService.getPendingRequestsForTutor(tutorId, new DatabaseService.DatabaseCallback<List<SessionRequest>>() {
                @Override
                public void onCompleted(List<SessionRequest> sessionRequests) {
                    if (sessionRequests == null) {
                        sessionRequests = new ArrayList<>();
                    }
                    
                    if (sessionRequests.isEmpty()) {
                        Toast.makeText(SessionRequests.this, "אין בקשות ממתינות", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.d(TAG, "Loaded " + sessionRequests.size() + " requests");
                    }
                    
                    adapter.setRequestList(sessionRequests);
                }

                @Override
                public void onFailed(Exception e) {
                    Log.e(TAG, "Error loading requests", e);
                    Toast.makeText(SessionRequests.this, "שגיאה בטעינת הבקשות: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    adapter.setRequestList(new ArrayList<>()); // Set empty list on error
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "Error in onCreate", e);
            Toast.makeText(this, "שגיאה בטעינת המסך: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, TutorPage.class));
        finish();
    }
}
