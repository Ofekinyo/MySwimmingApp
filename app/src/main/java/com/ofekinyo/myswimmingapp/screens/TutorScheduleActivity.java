package com.ofekinyo.myswimmingapp.screens;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
import com.ofekinyo.myswimmingapp.models.Schedule;
import java.util.ArrayList;
import java.util.List;

public class TutorScheduleActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ScheduleAdapter adapter;
    private List<Schedule> scheduleList;
    private DatabaseReference databaseReference;
    private Button btnRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_schedule);

        recyclerView = findViewById(R.id.recycler_view);
        btnRefresh = findViewById(R.id.btnRefresh);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        scheduleList = new ArrayList<>();
        adapter = new ScheduleAdapter(scheduleList);
        recyclerView.setAdapter(adapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("Schedules");
        fetchSchedules();

        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchSchedules();
            }
        });
    }

    private void fetchSchedules() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                scheduleList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Schedule schedule = dataSnapshot.getValue(Schedule.class);
                    if (schedule != null) {
                        scheduleList.add(schedule);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle potential errors
            }
        });
    }
}
