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

import com.ofekinyo.myswimmingapp.R;

import java.util.ArrayList;

public class TrainersList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainers_list);

        ListView lvTrainers = findViewById(R.id.lvTrainers);

        // Dummy data for trainers
        ArrayList<Trainer> trainers = new ArrayList<>();
        trainers.add(new Trainer("Chris Nissan", "john@example.com"));
        trainers.add(new Trainer("Jane Smith", "jane@example.com"));
        trainers.add(new Trainer("Emily Brown", "emily@example.com"));

        // Set the adapter
        TrainerAdapter adapter = new TrainerAdapter(this, trainers);
        lvTrainers.setAdapter(adapter);
    }

    // Trainer class for data
    public static class Trainer {
        String name;
        String email;

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
                convertView = inflater.inflate(R.layout.activity_each_session, parent, false);
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
                Intent intent = new Intent(context, TrainerDetailsActivity.class);
                intent.putExtra("trainerName", trainer.name);
                intent.putExtra("trainerEmail", trainer.email);
                context.startActivity(intent);
            });

            return convertView;
        }
    }
}