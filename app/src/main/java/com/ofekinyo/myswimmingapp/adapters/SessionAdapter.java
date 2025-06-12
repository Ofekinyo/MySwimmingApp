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

import java.util.List;

public class SessionAdapter extends RecyclerView.Adapter<SessionAdapter.RequestViewHolder> {
    private List<Session> requestList;
    private Context context;

    public SessionAdapter(List<Session> requestList, Context context) {
        this.requestList = requestList;
        this.context = context;
    }

    public void setRequestList(List<Session> newRequestList) {
        this.requestList = newRequestList;
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

        DatabaseService.getInstance().getTutor(request.getTutorId(), new DatabaseService.DatabaseCallback<Tutor>() {
            @Override
            public void onCompleted(Tutor tutor) {
                if (tutor != null) {
                    holder.tutorName.setText("מדריך: " + tutor.getFname() + " " + tutor.getLname());
                } else {
                    holder.tutorName.setText("מדריך לא נמצא");
                }
            }

            @Override
            public void onFailed(Exception e) {
                Log.e("RequestAdapter", "שגיאה בטעינת המדריך: " + e.getMessage());
                holder.tutorName.setText("שגיאה בטעינת המדריך");
            }
        });

        DatabaseService.getInstance().getSwimmer(request.getSwimmerId(), new DatabaseService.DatabaseCallback<Swimmer>() {
            @Override
            public void onCompleted(Swimmer swimmer) {
                if (swimmer != null) {
                    holder.swimmerName.setText("שחיין: " + swimmer.getFname() + " " + swimmer.getLname());
                } else {
                    holder.swimmerName.setText("שחיין לא נמצא");
                }
            }

            @Override
            public void onFailed(Exception e) {
                Log.e("RequestAdapter", "שגיאה בטעינת השחיין: " + e.getMessage());
                holder.swimmerName.setText("שגיאה בטעינת השחיין");
            }
        });
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
        return requestList != null ? requestList.size() : 0;
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
