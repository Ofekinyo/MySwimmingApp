package com.ofekinyo.myswimmingapp.screens;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
import com.ofekinyo.myswimmingapp.adapters.ScheduleAdapter;
import com.ofekinyo.myswimmingapp.models.Session;
import java.util.ArrayList;
import java.util.List;

public class Schedule extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ScheduleAdapter scheduleAdapter;
    private List<Session> sessionList;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        // Initialize views
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        sessionList = new ArrayList<>();
        scheduleAdapter = new ScheduleAdapter(sessionList);
        recyclerView.setAdapter(scheduleAdapter);

        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("Sessions");

        // Fetch sessions from Firebase
        fetchSessions();

        // Optional: Add button to refresh schedule
        Button btnRefresh = findViewById(R.id.btnRefresh);
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchSessions(); // Refresh sessions when button is clicked
            }
        });
    }

    private void fetchSessions() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                sessionList.clear(); // Clear existing sessions
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Session session = snapshot.getValue(Session.class);
                    sessionList.add(session); // Add session to list
                }
                scheduleAdapter.notifyDataSetChanged(); // Notify adapter to refresh
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Schedule.this, "Error fetching data: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
