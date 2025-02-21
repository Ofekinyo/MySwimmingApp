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
        View view =
