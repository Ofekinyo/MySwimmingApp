package com.ofekinyo.myswimmingapp.screens;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ofekinyo.myswimmingapp.R;
import com.ofekinyo.myswimmingapp.adapters.ExerciseAdapter;
import com.ofekinyo.myswimmingapp.models.Exercise;

import java.util.ArrayList;
import java.util.List;

public class BasicExercisesActivity extends AppCompatActivity {
    private RecyclerView rvExercises;
    private ExerciseAdapter adapter;
    private List<Exercise> exercisesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_exercises);
        rvExercises = findViewById(R.id.rvExercises);
        rvExercises.setLayoutManager(new LinearLayoutManager(this));
        // Sample exercises
        exercisesList = new ArrayList<>();
        exercisesList.add(new Exercise("Floating", "Learn how to float on water easily.", "https://youtu.be/jyA-Q7j2UOs?si=UZwLYix70Os__BD2"));
        exercisesList.add(new Exercise("Freestyle Basics", "Improve your front crawl technique.", "https://youtu.be/y5LZKOy7qZ4?si=dnRIypPtese1xgxj"));
        exercisesList.add(new Exercise("Breathing Techniques", "Proper breathing while swimming.", "https://youtu.be/qMSP3cZzy-8?si=V0VsnsRsebMydXxr"));

        adapter = new ExerciseAdapter(this, exercisesList);
        rvExercises.setAdapter(adapter);
    }
}
