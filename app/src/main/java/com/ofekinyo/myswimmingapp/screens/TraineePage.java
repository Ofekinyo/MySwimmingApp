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

            // Get button references
            btnTrainersList = findViewById(R.id.btnTrainersList);
            btnBasicExercises = findViewById(R.id.btnBasicExercises);  // Change button reference
            btnAccount = findViewById(R.id.btnAccount);
            btnTraineeSchedule = findViewById(R.id.btnTraineeSchedule);
            btnLogout = findViewById(R.id.btnLogout);
            btnAbout = findViewById(R.id.btnAbout);



            // Set button click listeners
            btnTrainersList.setOnClickListener(v -> {
                Intent intent = new Intent(TraineePage.this, TrainersList.class);
                startActivity(intent);
            });

            btnBasicExercises.setOnClickListener(v -> {  // Change to navigate to BasicExercisesActivity
                Intent intent = new Intent(TraineePage.this, BasicExercisesActivity.class);
                startActivity(intent);
            });

            btnAccount.setOnClickListener(v -> {
                Intent intent = new Intent(TraineePage.this, Account.class);
                startActivity(intent);
            });

            btnTraineeSchedule.setOnClickListener(v -> {
                Intent intent = new Intent(TraineePage.this, TraineeScheduleActivity.class);
                startActivity(intent);
            });

            btnLogout.setOnClickListener(v -> {
                SharedPreferencesUtil.signOutUser(TraineePage.this);
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(TraineePage.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            });

            btnAbout.setOnClickListener(v -> {
                Intent intent = new Intent(TraineePage.this, About.class);
                startActivity(intent);
            });

    }
    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menutest, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_profile) {
            Toast.makeText(this, "Profile clicked", Toast.LENGTH_SHORT).show();
            btnBack = findViewById(R.id.btnBack);
            return true;
        } else if (id == R.id.menu_about) {
            Toast.makeText(this, "About clicked", Toast.LENGTH_SHORT).show();
            // Add navigation code here
            return true;
        } else if (id == R.id.menu_back) {
            Toast.makeText(this, "Returned", Toast.LENGTH_SHORT).show();
            // Add back navigation logic here
            return true;
        } else if (id == R.id.menu_logout) {
            Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show();
            // Add logout logic here
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
*/

}


