package com.ofekinyo.myswimmingapp.screens;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ofekinyo.myswimmingapp.R;
import com.ofekinyo.myswimmingapp.adapters.TrainerAdapter;
import com.ofekinyo.myswimmingapp.models.Trainer;

import java.util.ArrayList;
import java.util.List;

public class TrainersList extends AppCompatActivity {
    private DatabaseReference trainersDatabaseRef;
    private List<Trainer> trainers, filteredTrainers;
    private TrainerAdapter adapter;
    private EditText searchBar;
    private Spinner searchSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainers_list);

        RecyclerView rvTrainers = findViewById(R.id.rvTrainers); // Changed to RecyclerView
        searchBar = findViewById(R.id.searchBar);
        searchSpinner = findViewById(R.id.searchSpinner);

        // Set up the Spinner with options
        String[] searchOptions = {"Name", "City", "Price", "Experience", "Gender", "Age", "Training Type"};
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, searchOptions);
        searchSpinner.setAdapter(spinnerAdapter);

        trainersDatabaseRef = FirebaseDatabase.getInstance().getReference("Trainers");
        trainers = new ArrayList<>();
        filteredTrainers = new ArrayList<>();

        adapter = new TrainerAdapter(this, filteredTrainers);
        rvTrainers.setAdapter(adapter);  // Set adapter to RecyclerView

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
            public void onCancelled(DatabaseError databaseError) {}
        });

        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                filterTrainers(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }

    private void filterTrainers(String query) {
        filteredTrainers.clear();
        if (query.isEmpty()) {
            filteredTrainers.addAll(trainers);
        } else {
            String selectedOption = searchSpinner.getSelectedItem().toString().toLowerCase();
            query = query.toLowerCase();

            for (Trainer trainer : trainers) {
                boolean matches = false;
                switch (selectedOption) {
                    case "name":
                        matches = trainer.getName().toLowerCase().contains(query);
                        break;
                    case "city":
                        matches = trainer.getCity().toLowerCase().contains(query);
                        break;
                    case "price":
                        matches = String.valueOf(trainer.getPrice()).contains(query);
                        break;
                    case "experience":
                        matches = String.valueOf(trainer.getExperience()).contains(query);
                        break;
                    case "gender":
                        matches = trainer.getGender().toLowerCase().contains(query);
                        break;
                    case "age":
                        matches = String.valueOf(trainer.getAge()).contains(query);
                        break;
                    case "training type":
                        matches = trainer.getTrainingTypes().toString().toLowerCase().contains(query);
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
