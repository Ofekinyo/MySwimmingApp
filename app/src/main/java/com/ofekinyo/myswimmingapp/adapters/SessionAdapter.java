package com.ofekinyo.myswimmingapp.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ofekinyo.myswimmingapp.R;
import com.ofekinyo.myswimmingapp.models.Session;
import com.ofekinyo.myswimmingapp.models.Swimmer;
import com.ofekinyo.myswimmingapp.models.Tutor;
import com.ofekinyo.myswimmingapp.services.DatabaseService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SessionAdapter extends RecyclerView.Adapter<SessionAdapter.RequestViewHolder> {
    private final List<Session> requestList;
    private final List<Tutor> tutorList;
    private final List<Swimmer> swimmerList;
    private final Context context;

    public SessionAdapter(Context context) {
        this.requestList = new ArrayList<>();
        this.swimmerList = new ArrayList<>();
        this.tutorList = new ArrayList<>();
        this.context = context;
    }

    public void setRequestList(List<Session> newRequestList) {
        this.requestList.clear();
        this.requestList.addAll(newRequestList);
        notifyDataSetChanged();
    }

    public void setTutorList(List<Tutor> tutorList) {
        this.tutorList.clear();
        this.tutorList.addAll(tutorList);
        notifyDataSetChanged();
    }

    public void setSwimmerList(List<Swimmer> swimmerList) {
        this.swimmerList.clear();
        this.swimmerList.addAll(swimmerList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_session_request, parent, false);
        return new RequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestViewHolder holder, int position) {
        Session request = requestList.get(position);

        holder.dateTime.setText("תאריך: " + request.getDate() + " שעה: " + request.getTime());
        holder.location.setText("מיקום: " + request.getLocation());
        holder.notes.setText("הערות: " + request.getNotes());

        // Join goals list into a single string
        StringBuilder goalsBuilder = new StringBuilder();
        for (String goal : request.getGoals()) {
            goalsBuilder.append(goal).append(", ");
        }
        holder.goals.setText("מטרות: " + goalsBuilder.toString());

        // Set up accept/reject buttons
        holder.btnAccept.setOnClickListener(v -> {
            request.setIsAccepted(true);
            updateSessionStatus(request);
        });

        holder.btnReject.setOnClickListener(v -> {
            request.setIsAccepted(false);
            updateSessionStatus(request);
        });

        Optional<Tutor> tutor = this.tutorList.stream()
                .filter(tutor1 -> tutor1.getId().equals(request.getTutorId()))
                .findFirst();
        Optional<Swimmer> swimmer = this.swimmerList.stream()
                .filter(swimmer1 -> swimmer1.getId().equals(request.getSwimmerId()))
                .findFirst();

        if (tutor.isPresent()) {
            holder.tutorName.setText("מדריך: " + tutor.get().getFname() + " " + tutor.get().getLname());
        } else {
            holder.tutorName.setText("מדריך לא נמצא");
        }

        if (swimmer.isPresent()) {
            holder.swimmerName.setText("שחיין: " + swimmer.get().getFname() + " " + swimmer.get().getLname());
        } else {
            holder.swimmerName.setText("שחיין לא נמצא");
        }

    }

    private void updateSessionStatus(Session session) {
        DatabaseService.getInstance().updateSessionStatus(session, new DatabaseService.DatabaseCallback<Void>() {
            @Override
            public void onCompleted(Void aVoid) {
                Toast.makeText(context, 
                    session.getIsAccepted() ? "השיעור אושר בהצלחה" : "השיעור נדחה", 
                    Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailed(Exception e) {
                Toast.makeText(context, "שגיאה בעדכון סטטוס השיעור: " + e.getMessage(), 
                    Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        if (tutorList.isEmpty()) return 0;
        if (swimmerList.isEmpty()) return 0;
        return requestList.size();
    }

    public static class RequestViewHolder extends RecyclerView.ViewHolder {
        TextView swimmerName, dateTime, location, goals, notes, tutorName;
        Button btnAccept, btnReject;

        public RequestViewHolder(@NonNull View itemView) {
            super(itemView);
            swimmerName = itemView.findViewById(R.id.tvSwimmerName);
            dateTime = itemView.findViewById(R.id.tvDateTime);
            location = itemView.findViewById(R.id.tvLocation);
            goals = itemView.findViewById(R.id.tvGoals);
            notes = itemView.findViewById(R.id.tvNotes);
            tutorName = itemView.findViewById(R.id.tvTutorName);
            btnAccept = itemView.findViewById(R.id.btnAccept);
            btnReject = itemView.findViewById(R.id.btnReject);
        }
    }
}
