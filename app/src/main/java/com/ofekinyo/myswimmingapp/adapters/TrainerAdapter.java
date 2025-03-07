package com.ofekinyo.myswimmingapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ofekinyo.myswimmingapp.R;
import com.ofekinyo.myswimmingapp.models.Trainer;
import com.ofekinyo.myswimmingapp.screens.EachTrainer;

import java.util.List;

public class TrainerAdapter extends RecyclerView.Adapter<TrainerAdapter.TrainerViewHolder> {

    private Context context;
    private List<Trainer> trainerList;

    public TrainerAdapter(Context context, List<Trainer> trainerList) {
        this.context = context;
        this.trainerList = trainerList;
    }

    @NonNull
    @Override
    public TrainerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_each_trainer, parent, false);
        return new TrainerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrainerViewHolder holder, int position) {
        Trainer trainer = trainerList.get(position);

        // Set trainer's name
        holder.tvTrainerName.setText(trainer.getName());  // Use the getName method to get the full name

        // On click, navigate to EachTrainer activity
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, EachTrainer.class);
            intent.putExtra("trainerName", trainer.getName());  // Pass trainer's name to the next activity
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return trainerList.size();
    }

    public static class TrainerViewHolder extends RecyclerView.ViewHolder {

        TextView tvTrainerName;

        public TrainerViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTrainerName = itemView.findViewById(R.id.tvTrainerName);
        }
    }
}
