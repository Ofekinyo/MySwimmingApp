package com.ofekinyo.myswimmingapp.screens;

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

import java.util.List;

public class EachSession extends RecyclerView.Adapter<EachSession.SessionViewHolder> {
    private Context context;
    private List<String> sessionList;
    private OnItemClickListener listener;

    public EachSession(Context context, List<String> sessionList, OnItemClickListener listener) {
        this.context = context;
        this.sessionList = sessionList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SessionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_each_session, parent, false);
        return new SessionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SessionViewHolder holder, int position) {
        String sessionDetails = sessionList.get(position);
        holder.tvSessionDetails.setText(sessionDetails);

        holder.btnRequestSession.setOnClickListener(v -> listener.onRequestSessionClicked(sessionDetails));

        // Handle More Info Button to navigate to TrainerInfo activity
        holder.btnMoreInfo.setOnClickListener(v -> {
            // Assuming sessionDetails contains the trainer's name
            String trainerName = sessionDetails;
            String trainerInfo = "Full details about " + trainerName;  // Add actual trainer details here

            // Start TrainerInfo activity and pass the trainer's data
            Intent intent = new Intent(context, TrainerInfo.class);  // Updated to TrainerInfo
            intent.putExtra("trainerName", trainerName);
            intent.putExtra("trainerInfo", trainerInfo);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return sessionList.size();
    }

    public static class SessionViewHolder extends RecyclerView.ViewHolder {
        TextView tvSessionDetails;
        Button btnRequestSession, btnMoreInfo;

        public SessionViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSessionDetails = itemView.findViewById(R.id.tvSessionDetails);
            btnRequestSession = itemView.findViewById(R.id.btnRequestSession);
            btnMoreInfo = itemView.findViewById(R.id.btnMoreInfo);
        }
    }

    public interface OnItemClickListener {
        void onRequestSessionClicked(String sessionDetails);
    }
}
