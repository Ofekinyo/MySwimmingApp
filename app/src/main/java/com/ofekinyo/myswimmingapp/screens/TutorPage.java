package com.ofekinyo.myswimmingapp.screens;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.ofekinyo.myswimmingapp.R;
import com.ofekinyo.myswimmingapp.utils.SharedPreferencesUtil;

public class TutorPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_page);

        // Get button references
        Button btnSchedule = findViewById(R.id.btnSchedule);
        Button btnSessionRequests = findViewById(R.id.btnSessionRequests); // Updated button ID
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("SwimLink");
        // Set button click listeners
        btnSchedule.setOnClickListener(v -> {
            Intent intent = new Intent(TutorPage.this, TutorScheduleActivity.class);
            startActivity(intent);
        });

        btnSessionRequests.setOnClickListener(v -> {
             //When the tutor clicks "Session Requests", navigate to SessionRequestsActivity
            Intent intent = new Intent(TutorPage.this, SessionRequests.class);
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
