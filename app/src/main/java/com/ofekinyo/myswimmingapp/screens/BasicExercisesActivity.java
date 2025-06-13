package com.ofekinyo.myswimmingapp.screens;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ofekinyo.myswimmingapp.R;
import com.ofekinyo.myswimmingapp.adapters.ExerciseAdapter;
import com.ofekinyo.myswimmingapp.base.BaseActivity;
import com.ofekinyo.myswimmingapp.models.Exercise;

import java.util.ArrayList;
import java.util.List;

public class BasicExercisesActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_exercises);
        setupToolbar("SwimLink");

        RecyclerView rvExercises = findViewById(R.id.rvExercises);
        rvExercises.setLayoutManager(new LinearLayoutManager(this));

        // Sample exercises
        List<Exercise> exercisesList = new ArrayList<>();
        exercisesList.add(new Exercise("Floating", "Learn how to float on water.", "https://youtu.be/jyA-Q7j2UOs?si=UZwLYix70Os__BD2"));
        exercisesList.add(new Exercise("Freestyle Basics", "Improve your front crawl technique.", "https://youtu.be/y5LZKOy7qZ4?si=dnRIypPtese1xgxj"));
        exercisesList.add(new Exercise("Breathing Techniques", "Proper breathing while swimming.", "https://youtu.be/qMSP3cZzy-8?si=V0VsnsRsebMydXxr"));

        ExerciseAdapter adapter = new ExerciseAdapter(this, new ExerciseAdapter.OnExerciseListener() {
            @Override
            public void onWatchClick(Exercise exercise) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(exercise.getVideoUrl()));
                startActivity(intent);
            }
        });
        rvExercises.setAdapter(adapter);

        adapter.setExerciseList(exercisesList);
    }
}