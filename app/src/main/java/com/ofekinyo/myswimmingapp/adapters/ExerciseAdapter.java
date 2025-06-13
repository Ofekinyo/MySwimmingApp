package com.ofekinyo.myswimmingapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ofekinyo.myswimmingapp.R;
import com.ofekinyo.myswimmingapp.models.Exercise;

import java.util.ArrayList;
import java.util.List;

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder> {

    public interface OnExerciseListener {
        public void onWatchClick(Exercise exercise);
    }

    private Context context;
    private List<Exercise> exerciseList;
    private OnExerciseListener exerciseListener;

    public ExerciseAdapter(Context context, OnExerciseListener exerciseListener) {
        this.context = context;
        this.exerciseList = new ArrayList<>();
        this.exerciseListener = exerciseListener;
    }

    public void setExerciseList(List<Exercise> exerciseList) {
        this.exerciseList.clear();
        this.exerciseList.addAll(exerciseList);
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_exercise, parent, false);
        return new ExerciseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseViewHolder holder, int position) {
        Exercise exercise = exerciseList.get(position);
        holder.tvTitle.setText(exercise.getTitle());
        holder.tvDescription.setText(exercise.getDescription());

        // Open YouTube link when the button is clicked
        holder.btnWatch.setOnClickListener(v -> {
            exerciseListener.onWatchClick(exercise);
        });
    }

    @Override
    public int getItemCount() {
        return exerciseList.size();
    }

    public static class ExerciseViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvDescription;
        Button btnWatch;

        public ExerciseViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvExerciseTitle);
            tvDescription = itemView.findViewById(R.id.tvExerciseDescription);
            btnWatch = itemView.findViewById(R.id.btnWatch);
        }
    }
}
