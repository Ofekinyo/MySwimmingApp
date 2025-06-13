package com.ofekinyo.myswimmingapp.screens;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ofekinyo.myswimmingapp.R;
import com.ofekinyo.myswimmingapp.adapters.ScheduleAdapter;
import com.ofekinyo.myswimmingapp.base.BaseActivity;
import com.ofekinyo.myswimmingapp.models.Schedule;
import com.ofekinyo.myswimmingapp.models.Tutor;
import com.ofekinyo.myswimmingapp.services.AuthenticationService;
import com.ofekinyo.myswimmingapp.services.DatabaseService;

import java.util.List;

public class TutorScheduleActivity extends BaseActivity {
    private RecyclerView recyclerView;
    private ScheduleAdapter adapter;
    private Button btnRefresh;
    private ProgressBar progressBar;
    private TextView tvNoSchedules;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_schedule);
        setupToolbar("SwimLink");

        initializeViews();
        setupRecyclerView();
        fetchSchedules();

        btnRefresh.setOnClickListener(v -> fetchSchedules());
    }

    private void initializeViews() {
        recyclerView = findViewById(R.id.recycler_view);
        btnRefresh = findViewById(R.id.btnRefresh);
        progressBar = findViewById(R.id.progressBar);
        tvNoSchedules = findViewById(R.id.tvNoSchedules);
    }

    private void setupRecyclerView() {
        adapter = new ScheduleAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }


    private void fetchSchedules() {
        showLoading(true);
        String tutorID = AuthenticationService.getInstance().getCurrentUserId();
        databaseService.getTutor(tutorID, new DatabaseService.DatabaseCallback<Tutor>() {
            @Override
            public void onCompleted(Tutor tutor) {
                List<Schedule> schedules = tutor.getSchedules();
                schedules.sort((o1, o2) -> o1.getDate().compareTo(o2.getDate()));
                adapter.setScheduleList(schedules);

                showLoading(false);
                updateUI(schedules);
            }

            @Override
            public void onFailed(Exception e) {

            }
        });
    }

    private void showLoading(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        recyclerView.setVisibility(show ? View.GONE : View.VISIBLE);
        btnRefresh.setEnabled(!show);
    }

    private void updateUI(List<Schedule> schedules) {
        if (schedules.isEmpty()) {
            tvNoSchedules.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            tvNoSchedules.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

}
