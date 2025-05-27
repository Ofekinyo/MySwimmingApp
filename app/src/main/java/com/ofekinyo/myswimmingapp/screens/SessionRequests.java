package com.ofekinyo.myswimmingapp.screens;

import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.*;
import com.ofekinyo.myswimmingapp.R;
import com.ofekinyo.myswimmingapp.models.Request;
import com.ofekinyo.myswimmingapp.adapters.SessionRequestAdapter;
import java.util.ArrayList;
import java.util.List;

public class SessionRequests extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SessionRequestAdapter adapter;
    private List<Request> requestList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session_requests);

        recyclerView = findViewById(R.id.recycler_view_requests);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        requestList = new ArrayList<>();
        adapter = new SessionRequestAdapter(requestList);
        recyclerView.setAdapter(adapter);

        fetchPendingRequests();
    }

    private void fetchPendingRequests() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("SessionRequests");

        ref.orderByChild("status").equalTo("pending")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        requestList.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Request request = dataSnapshot.getValue(Request.class);
                            requestList.add(request);
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(SessionRequests.this, "שגיאה בטעינת בקשות", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
