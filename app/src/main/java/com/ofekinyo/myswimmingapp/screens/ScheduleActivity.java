package com.ofekinyo.myswimmingapp.screens;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ofekinyo.myswimmingapp.R;
import com.ofekinyo.myswimmingapp.adapters.ScheduleAdapter;
import com.ofekinyo.myswimmingapp.base.BaseActivity;
import com.ofekinyo.myswimmingapp.models.Session;
import com.ofekinyo.myswimmingapp.models.SimpleDate;
import com.ofekinyo.myswimmingapp.models.SimpleTime;
import com.ofekinyo.myswimmingapp.models.User;
import com.ofekinyo.myswimmingapp.services.AuthenticationService;
import com.ofekinyo.myswimmingapp.services.DatabaseService;

import java.util.ArrayList;
import java.util.List;

public class ScheduleActivity extends BaseActivity {
    private RecyclerView recyclerView;
    private ScheduleAdapter adapter;
    private Button btnRefresh;
    private ProgressBar progressBar;
    private TextView tvNoSchedules;
    private boolean isAdminAccess = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        
        // Check if this is admin access
        isAdminAccess = getIntent().getBooleanExtra("admin_access", false);
        
        if (isAdminAccess) {
            setupToolbar("כל השיעורים");
        } else {
            setupToolbar("SwimLink");
        }

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
        String currentUserId = AuthenticationService.getInstance().getCurrentUserId();

        if (isAdminAccess) {
            // Admin access - show all sessions without filtering
            DatabaseService.getInstance().getSessions(new DatabaseService.DatabaseCallback<List<Session>>() {
                @Override
                public void onCompleted(List<Session> allSessions) {
                    processSessionsForAdmin(allSessions);
                }

                @Override
                public void onFailed(Exception e) {
                    showLoading(false);
                    Toast.makeText(ScheduleActivity.this, "Error loading sessions", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            // Regular user access - filter sessions as before
            DatabaseService.getInstance().getUser(currentUserId, new DatabaseService.DatabaseCallback<User>() {
                @Override
                public void onCompleted(User user) {
                    if (user == null) {
                        showLoading(false);
                        Toast.makeText(ScheduleActivity.this, "User not found", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    DatabaseService.getInstance().getSessions(new DatabaseService.DatabaseCallback<List<Session>>() {
                        @Override
                        public void onCompleted(List<Session> allSessions) {
                            processSessionsForUser(allSessions, user, currentUserId);
                        }

                        @Override
                        public void onFailed(Exception e) {
                            showLoading(false);
                            Toast.makeText(ScheduleActivity.this, "Error loading sessions", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onFailed(Exception e) {
                    showLoading(false);
                    Toast.makeText(ScheduleActivity.this, "Failed to load user info", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void processSessionsForAdmin(List<Session> allSessions) {
        List<com.ofekinyo.myswimmingapp.models.Schedule> scheduleList = new ArrayList<>();
        final int[] processedCount = {0};
        final int totalSessions = allSessions.size();

        if (totalSessions == 0) {
            adapter.setScheduleList(scheduleList);
            showLoading(false);
            updateUI(scheduleList);
            return;
        }

        for (Session session : allSessions) {
            try {
                SimpleDate date = SimpleDate.fromString(session.getDate());
                String timeStr = session.getTime();
                if (timeStr.matches("^\\d{2}:\\d{2}$")) {
                    timeStr += ":00";
                }
                SimpleTime time = SimpleTime.fromString(timeStr);

                // For admin view, show session status in title
                String status = session.getIsAccepted() == null ? "ממתין" : 
                              session.getIsAccepted() ? "אושר" : "נדחה";
                String title = "סטטוס: " + status;

                com.ofekinyo.myswimmingapp.models.Schedule schedule =
                        new com.ofekinyo.myswimmingapp.models.Schedule(session.getId(), title, date, time);
                scheduleList.add(schedule);

                // Fetch tutor and swimmer names
                fetchUserNamesForSession(session, schedule, () -> {
                    processedCount[0]++;
                    if (processedCount[0] == totalSessions) {
                        // All sessions processed, update UI
                        scheduleList.sort((a, b) -> {
                            int cmp = a.getDate().compareTo(b.getDate());
                            return (cmp != 0) ? cmp : a.getTime().compareTo(b.getTime());
                        });
                        adapter.setScheduleList(scheduleList);
                        showLoading(false);
                        updateUI(scheduleList);
                    }
                });

            } catch (Exception e) {
                Log.e("ScheduleParse", "Invalid date/time format in session: " + session.getId(), e);
                processedCount[0]++;
                if (processedCount[0] == totalSessions) {
                    scheduleList.sort((a, b) -> {
                        int cmp = a.getDate().compareTo(b.getDate());
                        return (cmp != 0) ? cmp : a.getTime().compareTo(b.getTime());
                    });
                    adapter.setScheduleList(scheduleList);
                    showLoading(false);
                    updateUI(scheduleList);
                }
            }
        }
    }

    private void processSessionsForUser(List<Session> allSessions, User user, String currentUserId) {
        List<com.ofekinyo.myswimmingapp.models.Schedule> scheduleList = new ArrayList<>();

        for (Session session : allSessions) {
            boolean shouldAdd = false;

            if (user instanceof com.ofekinyo.myswimmingapp.models.Tutor) {
                shouldAdd = session.getTutorId().equals(currentUserId) && Boolean.TRUE.equals(session.getIsAccepted());
            } else if (user instanceof com.ofekinyo.myswimmingapp.models.Swimmer) {
                shouldAdd = session.getSwimmerId().equals(currentUserId) && Boolean.TRUE.equals(session.getIsAccepted());
            }

            if (shouldAdd) {
                try {
                    SimpleDate date = SimpleDate.fromString(session.getDate());
                    String timeStr = session.getTime();
                    if (timeStr.matches("^\\d{2}:\\d{2}$")) {
                        timeStr += ":00";
                    }
                    SimpleTime time = SimpleTime.fromString(timeStr);

                    String title = ""; // Optionally, show Tutor/Swimmer name
                    com.ofekinyo.myswimmingapp.models.Schedule schedule =
                            new com.ofekinyo.myswimmingapp.models.Schedule(session.getId(), title, date, time);
                    scheduleList.add(schedule);

                    // Fetch tutor and swimmer names
                    fetchUserNamesForSession(session, schedule, null);

                } catch (Exception e) {
                    Log.e("ScheduleParse", "Invalid date/time format in session: " + session.getId(), e);
                    Log.e("ScheduleParse", "Trying to parse time: " + session.getTime());
                }
            }
        }

        scheduleList.sort((a, b) -> {
            int cmp = a.getDate().compareTo(b.getDate());
            return (cmp != 0) ? cmp : a.getTime().compareTo(b.getTime());
        });

        adapter.setScheduleList(scheduleList);
        showLoading(false);
        updateUI(scheduleList);
    }

    private void fetchUserNamesForSession(Session session, com.ofekinyo.myswimmingapp.models.Schedule schedule, Runnable onComplete) {
        final boolean[] tutorFetched = {false};
        final boolean[] swimmerFetched = {false};

        // Fetch tutor name
        DatabaseService.getInstance().getUser(session.getTutorId(), new DatabaseService.DatabaseCallback<User>() {
            @Override
            public void onCompleted(User tutor) {
                if (tutor != null) {
                    String tutorName = tutor.getFname() + " " + tutor.getLname();
                    schedule.setTutorName(tutorName);
                }
                tutorFetched[0] = true;
                checkIfComplete(tutorFetched, swimmerFetched, onComplete);
            }

            @Override
            public void onFailed(Exception e) {
                tutorFetched[0] = true;
                checkIfComplete(tutorFetched, swimmerFetched, onComplete);
            }
        });

        // Fetch swimmer name
        DatabaseService.getInstance().getUser(session.getSwimmerId(), new DatabaseService.DatabaseCallback<User>() {
            @Override
            public void onCompleted(User swimmer) {
                if (swimmer != null) {
                    String swimmerName = swimmer.getFname() + " " + swimmer.getLname();
                    schedule.setSwimmerName(swimmerName);
                }
                swimmerFetched[0] = true;
                checkIfComplete(tutorFetched, swimmerFetched, onComplete);
            }

            @Override
            public void onFailed(Exception e) {
                swimmerFetched[0] = true;
                checkIfComplete(tutorFetched, swimmerFetched, onComplete);
            }
        });
    }

    private void checkIfComplete(boolean[] tutorFetched, boolean[] swimmerFetched, Runnable onComplete) {
        if (tutorFetched[0] && swimmerFetched[0] && onComplete != null) {
            onComplete.run();
        }
    }

    private void showLoading(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        recyclerView.setVisibility(show ? View.GONE : View.VISIBLE);
        btnRefresh.setEnabled(!show);
    }

    private void updateUI(List<com.ofekinyo.myswimmingapp.models.Schedule> schedules) {
        if (schedules.isEmpty()) {
            tvNoSchedules.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            tvNoSchedules.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchSchedules();
    }
}
