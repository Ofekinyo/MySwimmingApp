package com.ofekinyo.myswimmingapp.screens;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import com.ofekinyo.myswimmingapp.R;
import com.ofekinyo.myswimmingapp.models.SwimStudent;
import com.ofekinyo.myswimmingapp.models.Trainer;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Button to connect trainers
        Button trainerButton = findViewById(R.id.btnTrainer);
        trainerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent trainerIntent = new Intent(MainActivity.this, Trainer.class);
                startActivity(trainerIntent);
            }
        });

        // Button to connect trainees
        Button traineeButton = findViewById(R.id.btnTrainee);
        traineeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent traineeIntent = new Intent(MainActivity.this, SwimStudent.class);
                startActivity(traineeIntent);
            }
        });
    }
}