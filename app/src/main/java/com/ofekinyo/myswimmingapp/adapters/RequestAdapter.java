package com.ofekinyo.myswimmingapp.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ofekinyo.myswimmingapp.R;
import com.ofekinyo.myswimmingapp.models.Request; // Assuming Request is your model class for requests

import java.util.List;

public class RequestAdapter extends BaseAdapter {

    private Activity activity;
    private List<Request> requestList;

    public RequestAdapter(Activity activity, List<Request> requestList) {
        this.activity = activity;
        this.requestList = requestList;
    }

    @Override
    public int getCount() {
        return requestList.size();
    }

    @Override
    public Object getItem(int position) {
        return requestList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Inflate the view for each item in the list
        if (convertView == null) {
            LayoutInflater inflater = activity.getLayoutInflater();
            convertView = inflater.inflate(R.layout.activity_list_item_request, parent, false);
        }

        // Get the current request object
        Request request = requestList.get(position);

        // Find the TextViews for the trainer's name and request status
        TextView tvTrainerName = convertView.findViewById(R.id.tvTrainerName);
        TextView tvRequestStatus = convertView.findViewById(R.id.tvRequestStatus);

        // Set the trainer's name and request status
        tvTrainerName.setText(request.getTrainerName());
        tvRequestStatus.setText("Status: " + request.getStatus());

        return convertView;
    }
}
