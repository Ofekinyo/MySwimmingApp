package com.ofekinyo.myswimmingapp.screens;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDialog;

import com.ofekinyo.myswimmingapp.R;
import com.ofekinyo.myswimmingapp.models.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditUserDialog {

    private Context context;
    private User user;
    private DatabaseReference databaseReference;

    public EditUserDialog(Context context, User user) {
        this.context = context;
        this.user = user;
        this.databaseReference = FirebaseDatabase.getInstance().getReference("Users");
    }

    public void show() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_edit_user, null);

        EditText fname = dialogView.findViewById(R.id.editTextFname);
        EditText lname = dialogView.findViewById(R.id.editTextLname);
        EditText phone = dialogView.findViewById(R.id.editTextPhone);
        EditText age = dialogView.findViewById(R.id.editTextAge);
        EditText gender = dialogView.findViewById(R.id.editTextGender);
        EditText city = dialogView.findViewById(R.id.editTextCity);

        // Set current user values in the fields
        fname.setText(user.getFname());
        lname.setText(user.getLname());
        phone.setText(user.getPhone());
        age.setText(String.valueOf(user.getAge()));
        gender.setText(user.getGender());
        city.setText(user.getCity());

        // Build dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(dialogView)
                .setPositiveButton("Save", (dialog, which) -> {
                    // Get updated data from dialog fields
                    user.setFname(fname.getText().toString());
                    user.setLname(lname.getText().toString());
                    user.setPhone(phone.getText().toString());
                    user.setAge(Integer.parseInt(age.getText().toString()));
                    user.setGender(gender.getText().toString());
                    user.setCity(city.getText().toString());

                    // Update user in Firebase database
                    databaseReference.child(user.getId()).setValue(user)
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(context, "User updated successfully!", Toast.LENGTH_SHORT).show();
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(context, "Failed to update user.", Toast.LENGTH_SHORT).show();
                            });
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .create()
                .show();
    }
}