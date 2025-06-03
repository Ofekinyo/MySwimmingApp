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

        // Display swimmer name if available, otherwise show ID
        String swimmerDisplay = request.getSwimmerName() != null ? 
            request.getSwimmerName() : request.getSwimmerId();
        holder.swimmerName.setText("שחיין: " + swimmerDisplay);

        // Format date and time
        String dateTime = formatDateTime(request.getDate(), request.getTime());
        holder.dateTime.setText(dateTime);

        // Display location
        holder.location.setText("מיקום: " + request.getLocation());

        // Format goals
        String goalsText = formatGoals(request);
        holder.goals.setText(goalsText);

        // Display notes
        String notes = request.getNotes();
        holder.notes.setText("הערות: " + (notes != null && !notes.isEmpty() ? notes : "אין"));

        // Display status if available
        String status = request.getStatus();
        if (status != null && !status.isEmpty()) {
            holder.status.setText("סטטוס: " + status);
            holder.status.setVisibility(View.VISIBLE);
        } else {
            holder.status.setVisibility(View.GONE);
        }
    }

    private String formatDateTime(String date, String time) {
        StringBuilder sb = new StringBuilder();
        if (date != null && !date.isEmpty()) {
            sb.append("תאריך: ").append(date);
        }
        if (time != null && !time.isEmpty()) {
            if (sb.length() > 0) {
                sb.append(" | ");
            }
            sb.append("שעה: ").append(time);
        }
        return sb.toString();
    }

    private String formatGoals(SessionRequest request) {
        StringBuilder goalText = new StringBuilder("מטרות: ");
        List<String> goals = request.getGoals();
        
        if (goals != null && !goals.isEmpty()) {
            goalText.append(String.join(", ", goals));
        }
        
        String otherGoal = request.getOtherGoal();
        if (otherGoal != null && !otherGoal.isEmpty()) {
            if (goals != null && !goals.isEmpty()) {
                goalText.append(", ");
            }
            goalText.append(otherGoal);
        }
        
        if ((goals == null || goals.isEmpty()) && 
            (otherGoal == null || otherGoal.isEmpty())) {
            goalText.append("לא צוינו");
        }
        
        return goalText.toString();
    }

    @Override
    public int getItemCount() {
        return requestList.size();
    }

    public void setRequestList(List<SessionRequest> requestList) {
        this.requestList = requestList != null ? requestList : new ArrayList<>();
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView swimmerName, dateTime, location, goals, notes, status;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            swimmerName = itemView.findViewById(R.id.textSwimmerId);
            dateTime = itemView.findViewById(R.id.textDateTime);
            location = itemView.findViewById(R.id.textLocation);
            goals = itemView.findViewById(R.id.textGoals);
            notes = itemView.findViewById(R.id.textNotes);
            status = itemView.findViewById(R.id.textStatus);
        }
    }
}
