package com.ofekinyo.myswimmingapp.screens;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ofekinyo.myswimmingapp.R;
import com.ofekinyo.myswimmingapp.adapters.TrainerAdapter;
import com.ofekinyo.myswimmingapp.models.Trainer;
import com.ofekinyo.myswimmingapp.services.DatabaseService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TrainersList extends AppCompatActivity {
    private DatabaseReference trainersDatabaseRef;
    private FirebaseAuth mAuth;
    private List<Trainer> trainers, filteredTrainers;
    private TrainerAdapter adapter;
    private EditText searchBar;
    private Spinner searchSpinner;

    private String traineeId, traineeName;
    DatabaseService databaseService;
    ArrayList<Trainer>trainers2=new ArrayList<>();

    // Mapping from Hebrew labels to internal keys based on the new categories
    private final Map<String, String> filterMap = new HashMap<String, String>() {{
        put("שם", "fname");
        put("עיר", "city");
        put("אימייל", "email");
        put("ניסיון", "experience");
        put("שם משפחה", "lname");
        put("מין", "gender");
        put("תעודת זהות", "id");
        put("סיסמא", "password");
        put("טלפון", "phone");
        put("מחיר", "price");
        put("תפקיד", "role");
        put("סוג שיעור", "trainingTypes");
    }};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainers_list);
        databaseService=DatabaseService.getInstance();


        databaseService.getAllTrainers(new DatabaseService.DatabaseCallback<List<Trainer>>() {
            @Override
            public void onCompleted(List<Trainer> object) {
                trainers2.addAll(object);
            }

            @Override
            public void onFailed(Exception e) {

            }
        });


        traineeId = getIntent().getStringExtra("traineeId");
        traineeName = getIntent().getStringExtra("traineeName");

        RecyclerView rvTrainers = findViewById(R.id.rvTrainers);
        searchBar = findViewById(R.id.searchBar);
        searchSpinner = findViewById(R.id.searchSpinner);

        rvTrainers.setLayoutManager(new LinearLayoutManager(this));

        String[] searchOptions = {"שם", "עיר", "אימייל", "ניסיון", "שם משפחה", "מין", "תעודת זהות", "סיסמא", "טלפון", "מחיר", "תפקיד", "סוג שיעור"};
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, searchOptions);
        searchSpinner.setAdapter(spinnerAdapter);

        trainersDatabaseRef = FirebaseDatabase.getInstance().getReference("Trainers");
        trainers = new ArrayList<>();
        filteredTrainers = new ArrayList<>();

        adapter = new TrainerAdapter(this, filteredTrainers, traineeId, traineeName);
        rvTrainers.setAdapter(adapter);

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
            @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                filterTrainers(charSequence.toString());
            }
            @Override public void afterTextChanged(Editable editable) {}
        });

        searchSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filterTrainers(searchBar.getText().toString());
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });

        setupBackButton();
    }

    private void setupBackButton() {
        Button backButton = findViewById(R.id.btnBack);
        backButton.setOnClickListener(v -> {

            FirebaseUser currentUser = mAuth.getCurrentUser();
            if (currentUser != null) {
                String userId = currentUser.getUid();
                DatabaseReference trainerRef = FirebaseDatabase.getInstance().getReference("Trainers").child(userId);

                trainerRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            startActivity(new Intent(TrainersList.this, TrainerPage.class));
                        } else {
                            startActivity(new Intent(TrainersList.this, TraineePage.class));
                        }
                        finish();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(TrainersList.this, "Database error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void filterTrainers(String query) {
        filteredTrainers.clear();
        if (query.isEmpty()) {
            filteredTrainers.addAll(trainers);
        } else {
            String selectedOption = searchSpinner.getSelectedItem().toString();
            query = query.toLowerCase();
            String key = filterMap.get(selectedOption);

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
