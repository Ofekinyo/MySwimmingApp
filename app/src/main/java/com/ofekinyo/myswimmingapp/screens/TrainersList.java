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
import java.util.ArrayList;

public class TrainersList extends AppCompatActivity {

    private DatabaseReference trainersDatabaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainers_list);

        ListView lvTrainers = findViewById(R.id.lvTrainers);

        // Initialize Firebase Database reference
        trainersDatabaseRef = FirebaseDatabase.getInstance().getReference("Trainers");

        // ArrayList to store trainers
        ArrayList<Trainer> trainers = new ArrayList<>();

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

                // Set the adapter
                TrainerAdapter adapter = new TrainerAdapter(TrainersList.this, trainers);
                lvTrainers.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle possible errors
            }
        });
    }

    // Trainer class for data
    public static class Trainer {
        String name;
        String email;

        public Trainer() {
            // Default constructor required for calls to DataSnapshot.getValue(Trainer.class)
        }

        public Trainer(String name, String email) {
            this.name = name;
            this.email = email;
        }
    }

    // Custom Adapter
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

            // Get current trainer
            Trainer trainer = trainers.get(position);

            // Set trainer name
            TextView tvTrainerName = convertView.findViewById(R.id.tvTrainerName);
            tvTrainerName.setText(trainer.name);

            // Set button listeners
            Button btnRequestSession = convertView.findViewById(R.id.btnRequestSession);
            Button btnMoreInfo = convertView.findViewById(R.id.btnMoreInfo);

            btnRequestSession.setOnClickListener(v -> {
                // Handle Request Session click
                Intent intent = new Intent(context, SendRequest.class);
                intent.putExtra("trainerName", trainer.name);
                context.startActivity(intent);
            });

            btnMoreInfo.setOnClickListener(v -> {
                // Handle More Info click
                Intent intent = new Intent(context, TrainerInfo.class);
                intent.putExtra("trainerName", trainer.name);
                intent.putExtra("trainerEmail", trainer.email);
                context.startActivity(intent);
            });

            return convertView;
        }
    }
}
