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

public class SessionsManagementAdapter extends RecyclerView.Adapter<SessionsManagementAdapter.ViewHolder> {
    private List<Session> sessionList;
    private Context context;
    private OnSessionClickListener listener;

    public SessionsManagementAdapter(List<Session> sessionList, Context context, OnSessionClickListener listener) {
        this.sessionList = sessionList;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_item_session, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Session session = sessionList.get(position);
        holder.sessionTitle.setText(session.getTitle());
        holder.sessionTime.setText(session.getTime());

        // Handle click event
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onSessionClick(session);
            }
        });
    }

    @Override
    public int getItemCount() {
        return sessionList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView sessionTitle, sessionTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            sessionTitle = itemView.findViewById(R.id.tvSessionTitle);
            sessionTime = itemView.findViewById(R.id.tvSessionTime);
        }
    }
}
