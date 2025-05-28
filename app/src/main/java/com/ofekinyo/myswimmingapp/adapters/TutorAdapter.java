package com.ofekinyo.myswimmingapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ofekinyo.myswimmingapp.R;
import com.ofekinyo.myswimmingapp.models.Tutor;
import com.ofekinyo.myswimmingapp.screens.SendRequest;
import com.ofekinyo.myswimmingapp.screens.TutorInfo;

import java.util.List;

public class TutorAdapter extends RecyclerView.Adapter<TutorAdapter.ViewHolder> {
    private Context context;
    private List<Tutor> tutors;
    //private String traineeId, traineeName;

    public TutorAdapter(Context context, List<Tutor> tutors, String traineeId, String traineeName) {
        this.context = context;
        this.tutors = tutors;
       //this.traineeId = traineeId;
        //this.traineeName = traineeName;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_each_tutor, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Tutor tutor = tutors.get(position);
        holder.tvTrainerName.setText(tutor.getName());

        // Example of setting experience, price, training types, etc. (keep your original code)

        // Handle Request Session button click directly here
        holder.btnRequestSession.setOnClickListener(v -> {
            Intent requestIntent = new Intent(context, SendRequest.class);

            requestIntent.putExtra("trainer", tutor);

          //  requestIntent.putExtra("traineeName", traineeName);
            context.startActivity(requestIntent);
        });

        // Handle More Info button click directly here
        holder.btnMoreInfo.setOnClickListener(v -> {
            Intent moreInfoIntent = new Intent(context, TutorInfo.class);
            moreInfoIntent.putExtra("trainer", tutor);
            context.startActivity(moreInfoIntent);
        });
    }

    @Override
    public int getItemCount() {
        return tutors.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTrainerName;
        Button btnRequestSession, btnMoreInfo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTrainerName = itemView.findViewById(R.id.tvTrainerName);
            btnRequestSession = itemView.findViewById(R.id.btnRequestSession);
            btnMoreInfo = itemView.findViewById(R.id.btnMoreInfo);
        }
    }
}
