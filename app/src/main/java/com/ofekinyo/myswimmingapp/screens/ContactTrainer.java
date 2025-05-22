package com.ofekinyo.myswimmingapp.screens;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.ofekinyo.myswimmingapp.R;
import com.ofekinyo.myswimmingapp.models.Request;
import com.ofekinyo.myswimmingapp.models.Trainee;
import com.ofekinyo.myswimmingapp.models.Trainer;
import com.ofekinyo.myswimmingapp.services.AuthenticationService;
import com.ofekinyo.myswimmingapp.services.DatabaseService;

import java.util.Calendar;
import java.util.Date;

public class ContactTrainer extends AppCompatActivity implements View.OnClickListener {
    private EditText etDetails, etOtherGoal;

    String details;
    private CheckBox cbGoal1, cbGoal2, cbGoal3, cbOther;
    private Button btnSubmit;

    TextView tvTrainer;

    Calendar calendar;
    Date date;


    private Trainer trainer = null;
    private Trainee trainee = null;

    DatabaseService databaseService;
    AuthenticationService authenticationService;
    private Intent takeit;

    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_contact_trainer);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        calendar = Calendar.getInstance();
        date = calendar.getTime();

        authenticationService = AuthenticationService.getInstance();
        uid = authenticationService.getCurrentUserId();

        databaseService = DatabaseService.getInstance();
        databaseService.getTrainee(uid, new DatabaseService.DatabaseCallback<Trainee>() {
            @Override
            public void onCompleted(Trainee object) {
                trainee = object;

            }

            @Override
            public void onFailed(Exception e) {

            }
        });


        // Initialize UI components
        etDetails = findViewById(R.id.etDetailsC);
        tvTrainer = findViewById(R.id.tvTrainerNameC);
        etOtherGoal = findViewById(R.id.etOtherGoalC);
        cbGoal1 = findViewById(R.id.cbGoal1C);
        cbGoal2 = findViewById(R.id.cbGoal2C);
        cbGoal3 = findViewById(R.id.cbGoal3C);
        cbOther = findViewById(R.id.cbOtherC);
        btnSubmit = findViewById(R.id.btnSubmitContact);

        btnSubmit.setOnClickListener(this);

        takeit = getIntent();

        // Get the trainer's ID passed from the previous activity
        trainer = (Trainer) takeit.getSerializableExtra("trainer");

        if (trainer == null) {
            Log.e("TrainerInfo", "Trainer ID is null!");
            return;
        } else if (trainer != null) {
            tvTrainer.setText(trainer.getName());
        }


        // Show/hide additional goal input field
        cbOther.setOnCheckedChangeListener((buttonView, isChecked) -> etOtherGoal.setVisibility(isChecked ? View.VISIBLE : View.GONE));


    }

    @Override
    public void onClick(View v) {

        details = etDetails.getText().toString();
        // Collect goals
        StringBuilder goals = new StringBuilder();
        if (cbGoal1.isChecked()) goals.append("Improve technique, ");
        if (cbGoal2.isChecked()) goals.append("Increase stamina, ");
        if (cbGoal3.isChecked()) goals.append("Speed improvement, ");
        if (cbOther.isChecked() && !etOtherGoal.getText().toString().isEmpty()) {
            goals.append(etOtherGoal.getText().toString()).append(", ");
        }

        if (goals.length() > 2) goals.setLength(goals.length() - 2); // Remove last comma

        if (goals.length() == 0) {
            Snackbar.make(findViewById(android.R.id.content), "Please select at least one goal", Snackbar.LENGTH_SHORT).show();
            return;
        }


        Request request = new Request(trainee, trainer, date, goals.toString(), details);

        databaseService.createNewRequest(request, new DatabaseService.DatabaseCallback<Void>() {
            @Override
            public void onCompleted(Void object) {

            }

            @Override
            public void onFailed(Exception e) {

            }
        });


    }

}