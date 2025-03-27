package com.ofekinyo.myswimmingapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ofekinyo.myswimmingapp.R;
import com.ofekinyo.myswimmingapp.models.Session;

import java.util.List;

public class TrainerSessionAdapter extends RecyclerView.Adapter<TrainerSessionAdapter.ViewHolder> {
    private List<Session> sessionList;
    private Context context;

    public TrainerSessionAdapter(List<Session> sessionList, Context context) {
        this.sessionList = sessionList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_item_trainer_session, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Session session = sessionList.get(position);
        holder.trainerId.setText(session.getTrainerId());
        holder.traineeId.setText(session.getTraineeId());

        // Assuming that `getRequest()` returns a `SessionRequest` object
        // Update this to extract the specific fields you want to show from the SessionRequest object
        String requestDetails = "Date: " + session.getRequest().getDate() +
                "\nTime: " + session.getRequest().getTime() +
                "\nGoals: " + session.getRequest().getGoals();
        holder.request.setText(requestDetails);
    }

    @Override
    public int getItemCount() {
        return sessionList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView trainerId, traineeId, request;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            trainerId = itemView.findViewById(R.id.tvTrainerId);
            traineeId = itemView.findViewById(R.id.tvTraineeId);
            request = itemView.findViewById(R.id.lstRequests);
        }
    }
}
