package com.ofekinyo.myswimmingapp.screens;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ofekinyo.myswimmingapp.R;
import java.util.ArrayList;

public class TrainerInfo extends AppCompatActivity {

    private TextView tvTrainerName, tvEmail, tvPhone, tvAge, tvGender, tvCity, tvPrice, tvExperience, tvTrainingTypes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainer_info);

        tvTrainerName = findViewById(R.id.tvTrainerName);
        tvEmail = findViewById(R.id.tvEmail);
        tvPhone = findViewById(R.id.tvPhone);
        tvAge = findViewById(R.id.tvAge);
        tvGender = findViewById(R.id.tvGender);
        tvCity = findViewById(R.id.tvCity);
        tvPrice = findViewById(R.id.tvPrice);
        tvExperience = findViewById(R.id.tvExperience);
        tvTrainingTypes = findViewById(R.id.tvTrainingTypes);

        // Get the trainer's ID passed from the previous activity
        String trainerId = getIntent().getStringExtra("trainerId");

        if (trainerId == null) {
            Log.e("TrainerInfo", "Trainer ID is null!");
            return;
        }

        // Fetch trainer details from Firebase based on the trainer's ID
        DatabaseReference trainerRef = FirebaseDatabase.getInstance().getReference("Users").child("Trainers").child(trainerId);

        trainerRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Log.d("TrainerInfo", "DataSnapshot keys and values:");
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        Log.d("TrainerInfo", child.getKey() + ": " + child.getValue());
                    }

                    // Extract trainer details from the database
                    String firstName = dataSnapshot.child("fname").getValue(String.class);
                    String lastName = dataSnapshot.child("lname").getValue(String.class);
                    String email = dataSnapshot.child("email").getValue(String.class);
                    String phone = dataSnapshot.child("phone").getValue(String.class);
                    Integer age = dataSnapshot.child("age").getValue(Integer.class);
                    String gender = dataSnapshot.child("gender").getValue(String.class);
                    String city = dataSnapshot.child("city").getValue(String.class);
                    Double price = dataSnapshot.child("price").getValue(Double.class);
                    Integer experience = dataSnapshot.child("experience").getValue(Integer.class);

                    // Log the fetched data for debugging
                    Log.d("TrainerInfo", "Fetched Data: fname = " + firstName + ", lname = " + lastName + ", Email = " + email);

                    // Check if firstName or lastName is null
                    if (firstName == null) {
                        Log.w("TrainerInfo", "First name is null for trainer " + trainerId);
                        firstName = "Unknown";  // Default to "Unknown" if null
                    }
                    if (lastName == null) {
                        Log.w("TrainerInfo", "Last name is null for trainer " + trainerId);
                        lastName = "Unknown";  // Default to "Unknown" if null
                    }

                    // Log the updated names
                    Log.d("TrainerInfo", "Trainer Name: " + firstName + " " + lastName);

                    // Display the trainer's details in the corresponding TextViews
                    String name = firstName + " " + lastName;  // Concatenate first and last name
                    tvTrainerName.setText(name);
                    tvEmail.setText(email != null ? email : "Not available");
                    tvPhone.setText(phone != null ? phone : "Not available");
                    tvAge.setText(age != null ? String.valueOf(age) : "Not available");
                    tvGender.setText(gender != null ? gender : "Not available");
                    tvCity.setText(city != null ? city : "Not available");
                    tvPrice.setText(price != null ? String.format("$%.2f", price) : "Not available");
                    tvExperience.setText(experience != null ? String.valueOf(experience) : "Not available");

                    // Display trainingTypes as a comma-separated string
                    ArrayList<String> trainingTypes = new ArrayList<>();
                    for (DataSnapshot typeSnap : dataSnapshot.child("trainingTypes").getChildren()) {
                        String type = typeSnap.getValue(String.class);
                        if (type != null) trainingTypes.add(type);
                    }
                    if (!trainingTypes.isEmpty()) {
                        tvTrainingTypes.setText(String.join(", ", trainingTypes));
                    } else {
                        tvTrainingTypes.setText("Not available");
                    }
                } else {
                    Log.e("TrainerInfo", "Trainer not found in the database");
                }
            }



            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("TrainerInfo", "Failed to read data", databaseError.toException());
            }
        });
    }
}
