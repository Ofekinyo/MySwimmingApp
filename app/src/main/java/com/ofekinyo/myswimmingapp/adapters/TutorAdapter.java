package com.ofekinyo.myswimmingapp.adapters;

import android.content.Context;
import android.content.Intent;
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
import com.ofekinyo.myswimmingapp.models.Tutor;
import com.ofekinyo.myswimmingapp.screens.SendRequest;
import com.ofekinyo.myswimmingapp.screens.TutorInfo;

import java.util.ArrayList;
import java.util.List;

public class TutorAdapter extends RecyclerView.Adapter<TutorAdapter.TutorViewHolder> {
    private static final String TAG = "TutorAdapter";
    private Context context;
    private List<Tutor> tutorList;

    public TutorAdapter(Context context) {
        this.context = context;
        this.tutorList = new ArrayList<>();
    }

    public void updateTutors(List<Tutor> newTutors) {
        this.tutorList.clear();
        this.tutorList.addAll(newTutors);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TutorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tutor, parent, false);
        return new TutorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TutorViewHolder holder, int position) {
        Tutor tutor = tutorList.get(position);
        if (tutor == null) return;

        // Set name
        String name = tutor.getName();
        holder.tutorName.setText(name != null ? name : "שם לא זמין");

        // Set experience
        int experience = tutor.getExperience();
        holder.tutorExperience.setText("ניסיון: " + experience + " שנים");

        // Set price
        double price = tutor.getPrice();
        holder.tutorPrice.setText(String.format("מחיר לשיעור: ₪%.2f", price));

        // Set session types
        List<String> sessionTypes = tutor.getSessionTypes();
        if (sessionTypes != null && !sessionTypes.isEmpty()) {
            holder.tutorSessionTypes.setVisibility(View.VISIBLE);
            holder.tutorSessionTypes.setText("סוגי שיעורים: " + String.join(", ", sessionTypes));
        } else {
            holder.tutorSessionTypes.setVisibility(View.GONE);
        }

        // Set up buttons
        holder.btnRequestSession.setOnClickListener(v -> {
            Intent requestIntent = new Intent(context, SendRequest.class);
            requestIntent.putExtra("tutor", tutor);
            context.startActivity(requestIntent);
        });

        holder.btnMoreInfo.setOnClickListener(v -> {
            Intent moreInfoIntent = new Intent(context, TutorInfo.class);
            moreInfoIntent.putExtra("tutor", tutor);
            context.startActivity(moreInfoIntent);
        });
    }

    @Override
    public int getItemCount() {
        return tutorList.size();
    }

    public static class TutorViewHolder extends RecyclerView.ViewHolder {
        TextView tutorName, tutorExperience, tutorPrice, tutorSessionTypes;
        Button btnRequestSession, btnMoreInfo;

        public TutorViewHolder(@NonNull View itemView) {
            super(itemView);
            tutorName = itemView.findViewById(R.id.tvTutorName);
            tutorExperience = itemView.findViewById(R.id.tvTutorExperience);
            tutorPrice = itemView.findViewById(R.id.tvTutorPrice);
            tutorSessionTypes = itemView.findViewById(R.id.tvTutorSessionTypes);
            btnRequestSession = itemView.findViewById(R.id.btnRequestSession);
            btnMoreInfo = itemView.findViewById(R.id.btnMoreInfo);
        }
    }
}
