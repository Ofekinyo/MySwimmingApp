package com.ofekinyo.myswimmingapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.ofekinyo.myswimmingapp.R;
import com.ofekinyo.myswimmingapp.models.Session;

import java.util.List;

public class SessionsManagementAdapter extends RecyclerView.Adapter<SessionsManagementAdapter.ViewHolder> {
    private List<Session> sessionList;
    private Context context;

    public SessionsManagementAdapter(List<Session> sessionList, Context context) {
        this.sessionList = sessionList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_session_management, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Session session = sessionList.get(position);
        holder.sessionTitle.setText(session.getTitle());
        holder.sessionTime.setText(session.getTime());

        holder.btnEdit.setOnClickListener(v -> {
            // Implement edit functionality
        });

        holder.btnDelete.setOnClickListener(v -> {
            // Implement delete functionality
        });
    }

    @Override
    public int getItemCount() {
        return sessionList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView sessionTitle, sessionTime;
        Button btnEdit, btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            sessionTitle = itemView.findViewById(R.id.tvSessionTitle);
            sessionTime = itemView.findViewById(R.id.tvSessionTime);
            btnEdit = itemView.findViewById(R.id.btnEditSession);
            btnDelete = itemView.findViewById(R.id.btnDeleteSession);
        }
    }
}
