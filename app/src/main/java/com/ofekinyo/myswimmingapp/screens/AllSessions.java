package com.ofekinyo.myswimmingapp.screens;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ofekinyo.myswimmingapp.R;
import com.ofekinyo.myswimmingapp.adapters.OnSessionClickListener;
import com.ofekinyo.myswimmingapp.adapters.SessionsAdapter; // Make sure to create this adapter
import com.ofekinyo.myswimmingapp.models.Session;

import java.util.ArrayList;
import java.util.List;

public class AllSessions extends AppCompatActivity implements OnSessionClickListener {
    private RecyclerView recyclerView;
    private SessionsAdapter sessionsAdapter;
    private List<Session> sessionList;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_sessions);

        // Initialize views
        recyclerView = findViewById(R.id.recycler_view_sessions);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        sessionList = new ArrayList<>();
        sessionsAdapter = new SessionsAdapter(sessionList, this);
        recyclerView.setAdapter(sessionsAdapter);

        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("Sessions"); // Update with the correct path

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
                sessionsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(AllSessions.this, "Error fetching sessions", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onSessionClick(Session session) {
        Intent intent = new Intent(AllSessions.this, SessionDetails.class);
        intent.putExtra("sessionId", session.getSessionId()); // Pass session ID to details activity
        startActivity(intent);
    }
}
