package com.ofekinyo.myswimmingapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.ofekinyo.myswimmingapp.R;
import com.ofekinyo.myswimmingapp.models.Schedule;

import java.util.ArrayList;
import java.util.List;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ScheduleViewHolder> {
    private List<Schedule> scheduleList;

    public ScheduleAdapter() {
        this.scheduleList = new ArrayList<>();
    }

    public void setScheduleList(List<Schedule> scheduleList) {
        this.scheduleList.clear();
        this.scheduleList.addAll(scheduleList);
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ScheduleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_item_schedule, parent, false);
        return new ScheduleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScheduleViewHolder holder, int position) {
        Schedule schedule = scheduleList.get(position);
        holder.scheduleTitle.setText(schedule.getTitle());
        
        // Display tutor name
        if (schedule.getTutorName() != null && !schedule.getTutorName().isEmpty()) {
            holder.tutorName.setText("מדריך: " + schedule.getTutorName());
            holder.tutorName.setVisibility(View.VISIBLE);
        } else {
            holder.tutorName.setVisibility(View.GONE);
        }
        
        // Display swimmer name
        if (schedule.getSwimmerName() != null && !schedule.getSwimmerName().isEmpty()) {
            holder.swimmerName.setText("שחיין: " + schedule.getSwimmerName());
            holder.swimmerName.setVisibility(View.VISIBLE);
        } else {
            holder.swimmerName.setVisibility(View.GONE);
        }
        
        holder.scheduleDate.setText("תאריך: " + schedule.getDate());
        holder.scheduleTime.setText("שעה: " + schedule.getTime());
    }

    @Override
    public int getItemCount() {
        return scheduleList.size();
    }

    static class ScheduleViewHolder extends RecyclerView.ViewHolder {
        TextView scheduleTitle, scheduleDate, scheduleTime, tutorName, swimmerName;

        public ScheduleViewHolder(@NonNull View itemView) {
            super(itemView);
            scheduleTitle = itemView.findViewById(R.id.tvScheduleTitle);
            tutorName = itemView.findViewById(R.id.tvTutorName);
            swimmerName = itemView.findViewById(R.id.tvSwimmerName);
            scheduleDate = itemView.findViewById(R.id.tvScheduleDate);
            scheduleTime = itemView.findViewById(R.id.tvScheduleTime);
        }
    }
}
