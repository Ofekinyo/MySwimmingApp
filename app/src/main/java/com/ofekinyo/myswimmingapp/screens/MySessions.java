package com.ofekinyo.myswimmingapp.screens;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.ofekinyo.myswimmingapp.R;
import com.ofekinyo.myswimmingapp.adapters.TrainerSessionAdapter;
import com.ofekinyo.myswimmingapp.models.Session;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MySessions extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TrainerSessionAdapter mySessionsAdapter;
    private List<Session> mySessionList;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_sessions);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mySessionList = new ArrayList<>();
        mySessionsAdapter = new TrainerSessionAdapter(mySessionList, this);
        recyclerView.setAdapter(mySessionsAdapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("TrainerSessions"); // Adjust as necessary

        fetchMySessions();
    }

    private void fetchMySessions() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mySessionList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Session session = snapshot.getValue(Session.class);
                    if (session != null) {
                        mySessionList.add(session);
                    }
                }
                mySessionsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MySessions.this, "Error fetching sessions", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
