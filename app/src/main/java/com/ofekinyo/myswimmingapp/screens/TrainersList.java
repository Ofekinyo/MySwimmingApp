package com.ofekinyo.myswimmingapp.screens;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ofekinyo.myswimmingapp.R;
import com.ofekinyo.myswimmingapp.adapters.TrainerAdapter;
import com.ofekinyo.myswimmingapp.models.Trainer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TrainersList extends AppCompatActivity {
    private DatabaseReference trainersDatabaseRef;
    private List<Trainer> trainers, filteredTrainers;
    private TrainerAdapter adapter;
    private EditText searchBar;
    private Spinner searchSpinner;

    // Mapping from Hebrew labels to internal keys based on the new categories
    private final Map<String, String> filterMap = new HashMap<String, String>() {{
        put("שם", "fname");       // First name
        put("עיר", "city");       // City
        put("אימייל", "email");   // Email
        put("ניסיון", "experience");  // Experience
        put("שם משפחה", "lname");    // Last name
        put("מין", "gender");     // Gender
        put("תעודת זהות", "id");  // ID
        put("סיסמא", "password"); // Password
        put("טלפון", "phone");    // Phone number
        put("מחיר", "price");     // Price
        put("תפקיד", "role");    // Role
        put("סוג שיעור", "trainingTypes");  // Training Types
    }};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainers_list);

        RecyclerView rvTrainers = findViewById(R.id.rvTrainers);
        searchBar = findViewById(R.id.searchBar);
        searchSpinner = findViewById(R.id.searchSpinner);

        rvTrainers.setLayoutManager(new LinearLayoutManager(this));

        // Spinner options in Hebrew
        String[] searchOptions = {"שם", "עיר", "אימייל", "ניסיון", "שם משפחה", "מין", "תעודת זהות", "סיסמא", "טלפון", "מחיר", "תפקיד", "סוג שיעור"};
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, searchOptions);
        searchSpinner.setAdapter(spinnerAdapter);

        trainersDatabaseRef = FirebaseDatabase.getInstance().getReference("Trainers");
        trainers = new ArrayList<>();
        filteredTrainers = new ArrayList<>();

        adapter = new TrainerAdapter(this, filteredTrainers);
        rvTrainers.setAdapter(adapter);

        // Realtime updates (instead of one-time fetch)
        trainersDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                trainers.clear();
                filteredTrainers.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Trainer trainer = snapshot.getValue(Trainer.class);
                    if (trainer != null) {
                        trainers.add(trainer);
                    }
                }

                filteredTrainers.addAll(trainers);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                filterTrainers(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        searchSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filterTrainers(searchBar.getText().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void filterTrainers(String query) {
        filteredTrainers.clear();
        if (query.isEmpty()) {
            filteredTrainers.addAll(trainers);
        } else {
            String selectedOption = searchSpinner.getSelectedItem().toString().toLowerCase();
            query = query.toLowerCase();

            String key = filterMap.get(selectedOption); // Get internal key for selected option

            for (Trainer trainer : trainers) {
                boolean matches = false;
                switch (key) {
                    case "fname":
                        matches = trainer.getFname() != null && trainer.getFname().toLowerCase().contains(query);
                        break;
                    case "city":
                        matches = trainer.getCity() != null && trainer.getCity().toLowerCase().contains(query);
                        break;
                    case "email":
                        matches = trainer.getEmail() != null && trainer.getEmail().toLowerCase().contains(query);
                        break;
                    case "experience":
                        matches = trainer.getExperience() != null && String.valueOf(trainer.getExperience()).contains(query);
                        break;
                    case "lname":
                        matches = trainer.getLname() != null && trainer.getLname().toLowerCase().contains(query);
                        break;
                    case "gender":
                        matches = trainer.getGender() != null && trainer.getGender().toLowerCase().contains(query);
                        break;
                    case "id":
                        matches = trainer.getId() != null && trainer.getId().toLowerCase().contains(query);
                        break;
                    case "password":
                        matches = trainer.getPassword() != null && trainer.getPassword().toLowerCase().contains(query);
                        break;
                    case "phone":
                        matches = trainer.getPhone() != null && trainer.getPhone().toLowerCase().contains(query);
                        break;
                    case "price":
                        matches = trainer.getPrice() != null && String.valueOf(trainer.getPrice()).contains(query);
                        break;
                    case "role":
                        matches = trainer.getRole() != null && trainer.getRole().toLowerCase().contains(query);
                        break;
                    case "trainingTypes":
                        matches = trainer.getTrainingTypes() != null && trainer.getTrainingTypes().toString().toLowerCase().contains(query);
                        break;
                }

                if (matches) {
                    filteredTrainers.add(trainer);
                }
            }
        }
        adapter.notifyDataSetChanged();
    }
}
