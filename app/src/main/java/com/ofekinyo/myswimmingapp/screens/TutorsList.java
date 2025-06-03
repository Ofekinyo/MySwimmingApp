package com.ofekinyo.myswimmingapp.screens;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ofekinyo.myswimmingapp.R;
import com.ofekinyo.myswimmingapp.adapters.TutorAdapter;
import com.ofekinyo.myswimmingapp.base.BaseActivity;
import com.ofekinyo.myswimmingapp.models.Tutor;
import com.ofekinyo.myswimmingapp.services.DatabaseService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TutorsList extends BaseActivity {
    private static final String TAG = "TutorsList";
    private DatabaseReference tutorsDatabaseRef;
    private FirebaseAuth mAuth;
    private List<Tutor> tutors;
    private TutorAdapter adapter;
    private EditText searchBar;
    private Spinner searchSpinner;

    private String swimmerId, swimmerName;
    DatabaseService databaseService;

    // Mapping from Hebrew labels to internal keys based on the new categories
    private final Map<String, String> filterMap = new HashMap<String, String>() {{
        put("שם", "fname");
        put("עיר", "city");
        put("אימייל", "email");
        put("ניסיון", "experience");
        put("שם משפחה", "lname");
        put("מין", "gender");
        put("תעודת זהות", "id");
        put("מחיר", "price");
        put("סוג שיעור", "sessionTypes");
    }};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutors_list);
        setupToolbar("רשימת מדריכים");

        // Initialize Firebase components
        mAuth = FirebaseAuth.getInstance();
        tutorsDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Tutors");
        databaseService = DatabaseService.getInstance();

        // Get swimmer data from intent
        swimmerId = getIntent().getStringExtra("swimmerId");
        swimmerName = getIntent().getStringExtra("swimmerName");

        // Initialize UI components
        searchBar = findViewById(R.id.searchBar);
        searchSpinner = findViewById(R.id.searchSpinner);

        // Set up RecyclerView
        RecyclerView recyclerView = findViewById(R.id.rvTutors);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize empty list and adapter
        tutors = new ArrayList<>();
        adapter = new TutorAdapter(this, tutors, swimmerId, swimmerName);
        recyclerView.setAdapter(adapter);

        // Load tutors
        loadTutors();

        // Set up search functionality
        setupSearch();

        setupBackButton();
    }

    private void setupSearch() {
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String searchText = s.toString().toLowerCase();
                String selectedFilter = filterMap.get(searchSpinner.getSelectedItem().toString());
                filterTutors(searchText, selectedFilter);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void filterTutors(String searchText, String filterType) {
        List<Tutor> filteredList = new ArrayList<>();
        for (Tutor tutor : tutors) {
            boolean matchFound = false;
            switch (filterType) {
                case "fname":
                    matchFound = tutor.getFname().toLowerCase().contains(searchText);
                    break;
                case "lname":
                    matchFound = tutor.getLname().toLowerCase().contains(searchText);
                    break;
                case "email":
                    matchFound = tutor.getEmail().toLowerCase().contains(searchText);
                    break;
                case "city":
                    matchFound = tutor.getCity().toLowerCase().contains(searchText);
                    break;
                case "gender":
                    matchFound = tutor.getGender().toLowerCase().contains(searchText);
                    break;
                case "id":
                    matchFound = tutor.getId().toLowerCase().contains(searchText);
                    break;
                case "price":
                    if (tutor.getPrice() != null) {
                        matchFound = String.valueOf(tutor.getPrice()).contains(searchText);
                    }
                    break;
                case "experience":
                    if (tutor.getExperience() != null) {
                        matchFound = String.valueOf(tutor.getExperience()).contains(searchText);
                    }
                    break;
                case "sessionTypes":
                    if (tutor.getSessionTypes() != null) {
                        for (String type : tutor.getSessionTypes()) {
                            if (type.toLowerCase().contains(searchText)) {
                                matchFound = true;
                                break;
                            }
                        }
                    }
                    break;
            }
            if (matchFound) {
                filteredList.add(tutor);
            }
        }
        adapter.updateTutors(filteredList);
    }

    private void loadTutors() {
        databaseService.getAllTutors(new DatabaseService.DatabaseCallback<List<Tutor>>() {
            @Override
            public void onCompleted(List<Tutor> tutorsList) {
                tutors.clear();
                tutors.addAll(tutorsList);
                adapter.notifyDataSetChanged();

                if (tutors.isEmpty()) {
                    Toast.makeText(TutorsList.this, "לא נמצאו מדריכים", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailed(Exception e) {
                Log.e(TAG, "Error loading tutors", e);
                Toast.makeText(TutorsList.this, "שגיאה בטעינת המדריכים: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setupBackButton() {
        Button backButton = findViewById(R.id.btnBack);
        backButton.setOnClickListener(v -> {
            FirebaseUser currentUser = mAuth.getCurrentUser();
            if (currentUser != null) {
                String userId = currentUser.getUid();
                DatabaseReference tutorRef = FirebaseDatabase.getInstance().getReference("Tutors").child(userId);

                tutorRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            startActivity(new Intent(TutorsList.this, TutorPage.class));
                        } else {
                            startActivity(new Intent(TutorsList.this, SwimmerPage.class));
                        }
                        finish();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(TutorsList.this, "Database error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
