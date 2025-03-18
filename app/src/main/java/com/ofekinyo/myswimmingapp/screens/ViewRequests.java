package com.ofekinyo.myswimmingapp.screens;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ofekinyo.myswimmingapp.R;
import com.ofekinyo.myswimmingapp.adapters.RequestAdapter;
import com.ofekinyo.myswimmingapp.models.Request;

import java.util.ArrayList;
import java.util.List;

public class ViewRequests extends AppCompatActivity {

    private ListView lvRequests;
    private DatabaseReference requestsRef;
    private String traineeId;
    private List<Request> requestList;
    private RequestAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_requests);

        lvRequests = findViewById(R.id.lvRequests);
        traineeId = getIntent().getStringExtra("traineeId");
        requestList = new ArrayList<>();
        adapter = new RequestAdapter(this, requestList);

        lvRequests.setAdapter(adapter);
        requestsRef = FirebaseDatabase.getInstance().getReference("Requests");

        // Fetch trainee's requests
        requestsRef.orderByChild("traineeId").equalTo(traineeId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                requestList.clear();
                for (DataSnapshot trainerSnapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot requestSnapshot : trainerSnapshot.getChildren()) {
                        Request request = requestSnapshot.getValue(Request.class);
                        if (request != null) {
                            requestList.add(request);
                        }
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ViewRequests.this, "Failed to load requests.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
