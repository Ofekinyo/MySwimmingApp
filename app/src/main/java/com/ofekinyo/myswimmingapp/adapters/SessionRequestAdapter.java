package com.ofekinyo.myswimmingapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.ofekinyo.myswimmingapp.R;
import com.ofekinyo.myswimmingapp.models.Request;

import java.util.List;

public class SessionRequestAdapter extends RecyclerView.Adapter<SessionRequestAdapter.ViewHolder> {

    private final List<Request> requestList;

    public SessionRequestAdapter(List<Request> requestList) {
        this.requestList = requestList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvSwimmerId, tvDateTime, tvLocation, tvGoals, tvNotes;

        public ViewHolder(View view) {
            super(view);
            tvSwimmerId = view.findViewById(R.id.tvSwimmerId);
            tvDateTime = view.findViewById(R.id.tvDateTime);
            tvLocation = view.findViewById(R.id.tvLocation);
            tvGoals = view.findViewById(R.id.tvGoals);
            tvNotes = view.findViewById(R.id.tvNotes);
        }
    }

    @NonNull
    @Override
    public SessionRequestAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_session_request, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SessionRequestAdapter.ViewHolder holder, int position) {
        Request request = requestList.get(position);
        holder.tvSwimmerId.setText("שחיין: " + request.getSwimmerId());
        holder.tvDateTime.setText("תאריך: " + request.getDate() + " שעה: " + request.getTime());
        holder.tvLocation.setText("מיקום: " + request.getLocation());
        holder.tvGoals.setText("מטרות: " + String.join(", ", request.getGoals()));
        holder.tvNotes.setText("הערות: " + request.getNotes());
    }

    @Override
    public int getItemCount() {
        return requestList.size();
    }
}