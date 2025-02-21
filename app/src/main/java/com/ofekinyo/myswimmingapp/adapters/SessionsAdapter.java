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

public class SessionsAdapter extends RecyclerView.Adapter<SessionsAdapter.SessionViewHolder> {
    private List<Session> sessionList;
    private OnSessionClickListener listener;

    public SessionsAdapter(List<Session> sessionList, OnSessionClickListener listener) {
        this.sessionList = sessionList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SessionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_item_session, parent, false);
        return new SessionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SessionViewHolder holder, int position) {
        Session session = sessionList.get(position);
        holder.bind(session, listener);
    }

    @Override
    public int getItemCount() {
        return sessionList.size();
    }

    static class SessionViewHolder extends RecyclerView.ViewHolder {
        TextView sessionTitle, sessionDate, sessionTime;

        public SessionViewHolder(@NonNull View itemView) {
            super(itemView);
            sessionTitle = itemView.findViewById(R.id.tvSessionTitle);
            sessionDate = itemView.findViewById(R.id.tvSessionDate);
            sessionTime = itemView.findViewById(R.id.tvSessionTime);
        }

        public void bind(final Session session, final OnSessionClickListener listener) {
            sessionTitle.setText(session.getTitle());
            sessionDate.setText(session.getDate());
            sessionTime.setText(session.getTime());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onSessionClick(session);
                }
            });
        }
    }
}
