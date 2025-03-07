package com.ofekinyo.myswimmingapp.screens;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ofekinyo.myswimmingapp.R;
import com.ofekinyo.myswimmingapp.models.Trainer;

import java.util.ArrayList;

public class TrainersList extends AppCompatActivity {

    private DatabaseReference trainersDatabaseRef;
    private ArrayList<Trainer> trainers;
    private TrainerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainers_list);

        ListView lvTrainers = findViewById(R.id.lvTrainers);

        // Initialize Firebase Database reference
        trainersDatabaseRef = FirebaseDatabase.getInstance().getReference("Trainers");

        // ArrayList to store trainers
        trainers = new ArrayList<>();

        // Attach a listener to retrieve trainers from Firebase
        trainersDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                trainers.clear();  // Clear the list before adding new data

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Trainer trainer = snapshot.getValue(Trainer.class);
                    if (trainer != null) {
                        trainers.add(trainer);  // Add trainer to the list
                    }
                }

                // Set the adapter to the ListView
                adapter = new TrainerAdapter(TrainersList.this, trainers);
                lvTrainers.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle possible errors
            }
        });
    }

    // Custom Adapter for Trainer List
    public static class TrainerAdapter extends android.widget.BaseAdapter {

        private final Context context;
        private final ArrayList<Trainer> trainers;

        public TrainerAdapter(Context context, ArrayList<Trainer> trainers) {
            this.context = context;
            this.trainers = trainers;
        }

        @Override
        public int getCount() {
            return trainers.size();
        }

        @Override
        public Object getItem(int position) {
            return trainers.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(context);
                convertView = inflater.inflate(R.layout.activity_each_trainer, parent, false);
            }

            Trainer trainer = trainers.get(position);

            TextView tvTrainerName = convertView.findViewById(R.id.tvTrainerName);
            tvTrainerName.setText(trainer.getName());  // Show the trainer's name

            Button btnRequestSession = convertView.findViewById(R.id.btnRequestSession);
            Button btnMoreInfo = convertView.findViewById(R.id.btnMoreInfo);

            // Handle the Request Session button click
            btnRequestSession.setOnClickListener(v -> {
                Intent intent = new Intent(context, SendRequest.class);
                intent.putExtra("trainerName", trainer.getName());
                context.startActivity(intent);
            });

            // Handle the More Info button click
            btnMoreInfo.setOnClickListener(v -> {
                // Pass the trainer's ID to the TrainerInfo activity
                Intent intent = new Intent(context, TrainerInfo.class);
                intent.putExtra("trainerId", trainer.getId());  // Pass the trainer's ID
                context.startActivity(intent);
            });

            return convertView;
        }
    }
}
