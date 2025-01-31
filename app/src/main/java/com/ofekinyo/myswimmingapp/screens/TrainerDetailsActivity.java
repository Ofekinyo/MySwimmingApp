package com.ofekinyo.myswimmingapp.screens;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ofekinyo.myswimmingapp.R;

public class TrainerDetailsActivity extends AppCompatActivity {

    private EditText etExperience, etBio;
    private Button btnSaveTrainerDetails, btnUploadFile;
    private Uri selectedFileUri; // To hold the URI of the selected file

    private static final int PICK_FILE_REQUEST = 1; // Code to identify file picker intent

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainer_details);

        // Initialize the views
        etExperience = findViewById(R.id.etExperience);
        etBio = findViewById(R.id.etBio);
        btnSaveTrainerDetails = findViewById(R.id.btnSaveTrainerDetails);
        btnUploadFile = findViewById(R.id.btnUploadFile);

        // Set up the file upload button
        btnUploadFile.setOnClickListener(v -> {
            // Open file picker to select a file
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*"); // Allows any file type
            startActivityForResult(Intent.createChooser(intent, "בחר קובץ"), PICK_FILE_REQUEST);
        });

        // Save Button functionality
        btnSaveTrainerDetails.setOnClickListener(v -> {
            // Retrieve the text entered by the user
            String experience = etExperience.getText().toString().trim();
            String bio = etBio.getText().toString().trim();

            // Validate the input fields
            if (experience.isEmpty() || selectedFileUri == null || bio.isEmpty()) {
                Toast.makeText(TrainerDetailsActivity.this, "נא למלא את כל השדות הנדרשים ולהעלות קובץ.", Toast.LENGTH_SHORT).show();
            } else {
                // If validation is passed, save the trainer's details
                // Logic to save the details (e.g., save in a database or send to a server)

                // Show a success message
                Toast.makeText(TrainerDetailsActivity.this, "פרטי המאמן נשמרו בהצלחה!", Toast.LENGTH_SHORT).show();

                // Redirect to TrainerPage after saving the details
                Intent intent = new Intent(TrainerDetailsActivity.this, TrainerPage.class);
                startActivity(intent);
                finish();
            }
        });
    }

    // Handle the result of the file picker
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == PICK_FILE_REQUEST) {
            // Retrieve the URI of the selected file
            selectedFileUri = data.getData();
            if (selectedFileUri != null) {
                // Optionally, display the file URI or file name in a TextView or log it
                Toast.makeText(this, "קובץ נבחר: " + selectedFileUri.getPath(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
