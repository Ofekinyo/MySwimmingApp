package com.ofekinyo.myswimmingapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.ofekinyo.myswimmingapp.R;
import com.ofekinyo.myswimmingapp.models.SessionRequest;
import java.util.List;

public class SessionAdapter extends RecyclerView.Adapter<SessionAdapter.SessionViewHolder> {
    private List<SessionRequest> sessionList;

    public SessionAdapter(List<SessionRequest> sessionList) {
        this.sessionList = sessionList;
    }

    @NonNull
    @Override
    public SessionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_item_my_session, parent, false);
        return new SessionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SessionViewHolder holder, int position) {
        SessionRequest session = sessionList.get(position);
        holder.tvSessionType.setText(session.getSessionType());
        holder.tvDuration.setText(session.getDuration());
        holder.tvDate.setText(session.getDate());
        holder.tvTrainerName.setText(session.getTrainerName());
    }

    @Override
    public int getItemCount() {
        return sessionList.size();
    }

    public static class SessionViewHolder extends RecyclerView.ViewHolder {
        TextView tvSessionType, tvDuration, tvDate, tvTrainerName;

        public SessionViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSessionType = itemView.findViewById(R.id.tvSessionType);
            tvDuration = itemView.findViewById(R.id.tvSessionDuration);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvTrainerName = itemView.findViewById(R.id.tvTrainerName);
        }
    }
}
