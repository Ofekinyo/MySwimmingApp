package com.ofekinyo.myswimmingapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ofekinyo.myswimmingapp.R;
import com.ofekinyo.myswimmingapp.models.Trainer;
import com.ofekinyo.myswimmingapp.screens.SendRequest;
import com.ofekinyo.myswimmingapp.screens.TrainerInfo;

import java.util.List;

public class TrainerAdapter extends RecyclerView.Adapter<TrainerAdapter.ViewHolder> {
    private Context context;
    private List<Trainer> trainers;
    private String traineeId, traineeName;

    public TrainerAdapter(Context context, List<Trainer> trainers, String traineeId, String traineeName) {
        this.context = context;
        this.trainers = trainers;
        this.traineeId = traineeId;
        this.traineeName = traineeName;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_each_trainer, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Trainer trainer = trainers.get(position);
        holder.tvTrainerName.setText(trainer.getName());

        // Example of setting experience, price, training types, etc. (keep your original code)

        // Handle Request Session button click directly here
        holder.btnRequestSession.setOnClickListener(v -> {
            Intent requestIntent = new Intent(context, SendRequest.class);
            requestIntent.putExtra("trainerId", trainer.getId()); // assuming Trainer has getId()
            requestIntent.putExtra("traineeId", traineeId);
            requestIntent.putExtra("trainerName", trainer.getName());
            requestIntent.putExtra("traineeName", traineeName);
            context.startActivity(requestIntent);
        });

        // Handle More Info button click directly here
        holder.btnMoreInfo.setOnClickListener(v -> {
            Intent moreInfoIntent = new Intent(context, TrainerInfo.class);
            moreInfoIntent.putExtra("trainerName", trainer.getName());
            context.startActivity(moreInfoIntent);
        });
    }

    @Override
    public int getItemCount() {
        return trainers.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTrainerName;
        Button btnRequestSession, btnMoreInfo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTrainerName = itemView.findViewById(R.id.tvTrainerName);
            btnRequestSession = itemView.findViewById(R.id.btnRequestSession);
            btnMoreInfo = itemView.findViewById(R.id.btnMoreInfo);
        }
    }
}
