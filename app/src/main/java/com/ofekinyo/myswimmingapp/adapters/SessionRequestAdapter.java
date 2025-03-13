/* package com.ofekinyo.myswimmingapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ofekinyo.myswimmingapp.R;
import com.ofekinyo.myswimmingapp.models.SessionRequest;

import java.util.List;

public class SessionRequestAdapter extends RecyclerView.Adapter<SessionRequestAdapter.SessionRequestViewHolder> {

    private List<SessionRequest> sessionRequests;

    public SessionRequestAdapter(List<SessionRequest> sessionRequests) {
        this.sessionRequests = sessionRequests;
    }

    @NonNull
    @Override
    public SessionRequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_request_item, parent, false);
        return new SessionRequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SessionRequestViewHolder holder, int position) {
        SessionRequest request = sessionRequests.get(position);
        holder.tvSession.setText("Session: " + request.getSessionType());
        holder.tvDate.setText("Date: " + request.getDate());
        holder.tvGoals.setText("Goals: " + request.getGoals());

        // Fetch trainee name from Firebase
        String traineeId = request.getTraineeId(); // Assuming SessionRequest has traineeId

        if (traineeId != null) {
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(traineeId);
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String traineeName = snapshot.child("name").getValue(String.class);
                        holder.tvTrainee.setText("Trainee: " + (traineeName != null ? traineeName : "Unknown"));
                    } else {
                        holder.tvTrainee.setText("Trainee: Unknown");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    holder.tvTrainee.setText("Trainee: Error");
                }
            });
        } else {
            holder.tvTrainee.setText("Trainee: Unknown");
        }
    }

    @Override
    public int getItemCount() {
        return sessionRequests.size();
    }

    public static class SessionRequestViewHolder extends RecyclerView.ViewHolder {
        TextView tvTrainee, tvSession, tvDate, tvGoals;
        Button btnApprove, btnDecline;

        public SessionRequestViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTrainee = itemView.findViewById(R.id.tvTrainee);
            tvSession = itemView.findViewById(R.id.tvSession);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvGoals = itemView.findViewById(R.id.tvGoals);
            btnApprove = itemView.findViewById(R.id.btnApprove);
            btnDecline = itemView.findViewById(R.id.btnDecline);
        }
    }
}
 */
