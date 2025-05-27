package com.ofekinyo.myswimmingapp.screens;

import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ofekinyo.myswimmingapp.R;
import com.ofekinyo.myswimmingapp.models.Trainee;
import com.ofekinyo.myswimmingapp.utils.SharedPreferencesUtil;

public class TraineePage extends AppCompatActivity {

    private Button btnBack, btnTrainersList, btnBasicExercises, btnAccount,btnTraineeSchedule, btnLogout, btnAbout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainee_page);
        // Log intent extras if any
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            for (String key : extras.keySet()) {
                Object value = extras.get(key);
                Log.d("IntentDebug", key + ": " + value);
            }
        }
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Get button references
            btnTrainersList = findViewById(R.id.btnTrainersList);
            btnBasicExercises = findViewById(R.id.btnBasicExercises);  // Change button reference
            btnTraineeSchedule = findViewById(R.id.btnTraineeSchedule);



            // Set button click listeners
            btnTrainersList.setOnClickListener(v -> {
                Intent intent = new Intent(TraineePage.this, TrainersList.class);
                startActivity(intent);
            });

            btnBasicExercises.setOnClickListener(v -> {  // Change to navigate to BasicExercisesActivity
                Intent intent = new Intent(TraineePage.this, BasicExercisesActivity.class);
                startActivity(intent);
            });

            btnTraineeSchedule.setOnClickListener(v -> {
                Intent intent = new Intent(TraineePage.this, TraineeScheduleActivity.class);
                startActivity(intent);
            });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_profile) {
            Intent intent = new Intent(this, Account.class);
            startActivity(intent);
            return true;

        } else if (id == R.id.menu_about) {
            Intent intent = new Intent(this, About.class);
            startActivity(intent);
            return true;

        } else if (id == R.id.menu_logout) {
            SharedPreferencesUtil.signOutUser(this);
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}


