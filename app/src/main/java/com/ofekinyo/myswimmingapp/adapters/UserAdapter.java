package com.ofekinyo.myswimmingapp.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ofekinyo.myswimmingapp.R;
import com.ofekinyo.myswimmingapp.models.User;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {


    public interface OnUserClickListener {
        public <T extends User> void OnClickSave(T user, int position);
    }

    private Context context;
    private final List<User> userList;
    private OnUserClickListener onUserClickListener;

    public UserAdapter(Context context, OnUserClickListener onUserClickListener) {
        this.context = context;
        this.userList = new ArrayList<>();
        this.onUserClickListener = onUserClickListener;
    }

    public void addUsers(List<? extends User> userList) {
        this.userList.addAll(userList);
        this.notifyDataSetChanged();
    }

    public <T extends User> void updateUser(T user, int index) {
        this.userList.set(index, user);
        this.notifyItemChanged(index);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_item_xml, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = userList.get(position);
        holder.textViewName.setText("שם פרטי: " + user.getFname());
        holder.textViewRole.setText("תפקיד: " + user.getRole());

        holder.buttonEditUser.setOnClickListener(v -> showEditDialog(user, position));
    }

    @Override
    public int getItemCount() {
        return userList != null ? userList.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName, textViewRole;
        Button buttonEditUser;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewRole = itemView.findViewById(R.id.textViewRole);
            buttonEditUser = itemView.findViewById(R.id.buttonEditUser);
        }
    }

    private void showEditDialog(final User user, final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_edit_user, null);
        builder.setView(view);

        // Set up the EditText fields for the dialog
        EditText fname = view.findViewById(R.id.editTextFname);
        EditText lname = view.findViewById(R.id.editTextLname);
        EditText phone = view.findViewById(R.id.editTextPhone);
        EditText age = view.findViewById(R.id.editTextAge);
        EditText gender = view.findViewById(R.id.editTextGender);
        EditText city = view.findViewById(R.id.editTextCity);

        // Pre-fill the fields with the user's current data
        fname.setText(user.getFname());
        lname.setText(user.getLname());
        phone.setText(user.getPhone());
        age.setText(String.valueOf(user.getAge()));
        gender.setText(user.getGender());
        city.setText(user.getCity());

        builder.setPositiveButton("Save", (dialog, which) -> {
            // Update user with the new data
            User user1 = user.clone();
            user1.setFname(fname.getText().toString());
            user1.setLname(lname.getText().toString());
            user1.setPhone(phone.getText().toString());
            user1.setAge(Integer.parseInt(age.getText().toString()));
            user1.setGender(gender.getText().toString());
            user1.setCity(city.getText().toString());

            onUserClickListener.OnClickSave(user1, position);
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
