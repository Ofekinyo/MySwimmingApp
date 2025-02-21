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

public class MySessionsAdapter extends RecyclerView.Adapter<MySessionsAdapter.ViewHolder> {
    private List<Session> sessionList;
    private Context context;

    public MySessionsAdapter(List<Session> sessionList, Context context) {
        this.sessionList = sessionList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_item_my_session, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Session session = sessionList.get(position);
        holder.sessionTitle.setText(session.getTitle());
        holder.sessionDate.setText(session.getDate());
        holder.sessionTime.setText(session.getTime());
        holder.sessionLocation.setText(session.getLocation());
    }

    @Override
    public int getItemCount() {
        return sessionList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView sessionTitle, sessionDate, sessionTime, sessionLocation;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            sessionTitle = itemView.findViewById(R.id.tvSessionTitle);
            sessionDate = itemView.findViewById(R.id.tvSessionDate);
            sessionTime = itemView.findViewById(R.id.tvSessionTime);
            sessionLocation = itemView.findViewById(R.id.tvSessionLocation);
        }
    }
}
