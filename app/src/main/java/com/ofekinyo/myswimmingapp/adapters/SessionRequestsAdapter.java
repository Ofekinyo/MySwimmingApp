package com.ofekinyo.myswimmingapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ofekinyo.myswimmingapp.R;
import com.ofekinyo.myswimmingapp.models.SessionRequest;

import java.util.ArrayList;
import java.util.List;

public class SessionRequestsAdapter extends RecyclerView.Adapter<SessionRequestsAdapter.ViewHolder> {

    private List<SessionRequest> requestList;

    public SessionRequestsAdapter() {
        this.requestList = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_session_request, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SessionRequest request = requestList.get(position);

        // Change Trainee to Swimmer terminology
        holder.swimmerId.setText("שחיין: " + request.getSwimmerId());  // Assuming you renamed getter from getTraineeId() to getSwimmerId()
        holder.dateTime.setText("תאריך: " + request.getDate() + " שעה: " + request.getTime());
        holder.location.setText("מיקום: " + request.getLocation());

        List<String> goals = request.getGoals();
        StringBuilder goalText = new StringBuilder("מטרות: ");
        if (goals != null && !goals.isEmpty()) {
            goalText.append(String.join(", ", goals));
        }
        if (request.getOtherGoal() != null && !request.getOtherGoal().isEmpty()) {
            if (goals != null && !goals.isEmpty()) {
                goalText.append(", ");
            }
            goalText.append(request.getOtherGoal());
        }
        holder.goals.setText(goalText.toString());

        holder.notes.setText("הערות: " + (request.getNotes() != null && !request.getNotes().isEmpty() ? request.getNotes() : "אין"));
    }

    @Override
    public int getItemCount() {
        return requestList.size();
    }

    public void setRequestList(List<SessionRequest> requestList) {
        this.requestList.clear();
        this.requestList.addAll(requestList);
        this.notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView swimmerId, dateTime, location, goals, notes;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            swimmerId = itemView.findViewById(R.id.textSwimmerId);
            dateTime = itemView.findViewById(R.id.textDateTime);
            location = itemView.findViewById(R.id.textLocation);
            goals = itemView.findViewById(R.id.textGoals);
            notes = itemView.findViewById(R.id.textNotes);
        }
    }
}
