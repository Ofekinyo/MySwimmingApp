package com.ofekinyo.myswimmingapp.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ofekinyo.myswimmingapp.R;
import com.ofekinyo.myswimmingapp.models.User;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private Context context;
    private List<User> userList;

    public UserAdapter(Context context, List<User> userList) {
        this.context = context;
        this.userList = userList;
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
        holder.fname.setText("First Name: " + user.getFname());
        holder.lname.setText("Last Name: " + user.getLname());
        holder.phone.setText("Phone Number: " + user.getPhone());
        holder.age.setText("Age: " + user.getAge());
        holder.gender.setText("Gender: " + user.getGender());
        holder.city.setText("City: " + user.getCity());
        holder.role.setText("Role: " + user.getRole());

        // Display email, but don't allow editing
        holder.email.setText("Email: " + user.getEmail());

        // Set up the edit button
        holder.editButton.setOnClickListener(v -> showEditDialog(user));
    }

    @Override
    public int getItemCount() {
        return userList != null ? userList.size() : 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView fname, lname, phone, age, gender, city, role, email;
        Button editButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            fname = itemView.findViewById(R.id.editTextFname);
            lname = itemView.findViewById(R.id.editTextLname);
            phone = itemView.findViewById(R.id.editTextPhone);
            age = itemView.findViewById(R.id.editTextAge);
            gender = itemView.findViewById(R.id.editTextGender);
            city = itemView.findViewById(R.id.editTextCity);
            role = itemView.findViewById(R.id.textViewRole);
            email = itemView.findViewById(R.id.textViewEmail); // Email TextView
            editButton = itemView.findViewById(R.id.buttonSaveUser);
        }
    }

    private void showEditDialog(User user) {
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
            user.setFname(fname.getText().toString());
            user.setLname(lname.getText().toString());
            user.setPhone(phone.getText().toString());
            user.setAge(Integer.parseInt(age.getText().toString()));
            user.setGender(gender.getText().toString());
            user.setCity(city.getText().toString());

            // Save updated user to Firebase
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
            databaseReference.child(user.getId()).setValue(user);

            // Notify the adapter that the data has been updated
            notifyDataSetChanged();
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
