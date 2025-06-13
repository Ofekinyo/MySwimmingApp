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

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

    private List<Tutor> tutors;
    private TutorAdapter adapter;
    private EditText searchBar;
    private Spinner searchSpinner;

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
        setupToolbar("SwimLink");

        // Initialize UI components
        searchBar = findViewById(R.id.searchBar);
        searchSpinner = findViewById(R.id.searchSpinner);

        // Set up RecyclerView
        RecyclerView recyclerView = findViewById(R.id.rvTutors);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize empty list and adapter
        adapter = new TutorAdapter(this);
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
                    matchFound = String.valueOf(tutor.getPrice()).contains(searchText);
                    break;
                case "experience":
                    matchFound = String.valueOf(tutor.getExperience()).contains(searchText);
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
                tutors = tutorsList;
                adapter.updateTutors(tutorsList);
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
            startActivity(new Intent(TutorsList.this, SwimmerPage.class));
            finish();
        });
    }

}
