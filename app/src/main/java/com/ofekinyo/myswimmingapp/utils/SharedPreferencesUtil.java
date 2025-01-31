package com.ofekinyo.myswimmingapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.ofekinyo.myswimmingapp.models.User;

public class SharedPreferencesUtil {

    private static final String PREF_NAME = "com.example.testapp.PREFERENCE_FILE_KEY";

    // Save a user object to shared preferences
    public static void saveUser(Context context, User user) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("uid", user.getId());
        editor.putString("email", user.getEmail());
        editor.putString("password", user.getPassword());
        editor.putString("Fname", user.getFname());
        editor.putString("Lname", user.getLname());
        editor.putInt("phone", user.getPhone());
        editor.putInt("age", user.getAge());
        editor.putString("city", user.getCity());
        editor.putString("gender", user.getGender());
        editor.putString("role", user.getRole());  // Storing the user's role (Trainee or Trainer)
        editor.apply();
    }

    // Get the user object from shared preferences
    public static User getUser(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        if (!isUserLoggedIn(context)) {
            return null;
        }
        String uid = sharedPreferences.getString("uid", "");
        String email = sharedPreferences.getString("email", "");
        String password = sharedPreferences.getString("password", "");
        String Fname = sharedPreferences.getString("Fname", "");
        String Lname = sharedPreferences.getString("Lname", "");
        int phone = sharedPreferences.getInt("phone", 0);
        int age = sharedPreferences.getInt("age", 0);
        String city = sharedPreferences.getString("city", "");
        String gender = sharedPreferences.getString("gender", "");
        String role = sharedPreferences.getString("role", "");  // Retrieving role

        return new User(uid, Fname, Lname, phone, email, age, gender, city, password, role);
    }

    // Check if a user is logged in by checking if the user id is present in shared preferences
    public static boolean isUserLoggedIn(Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).contains("uid");
    }

    // Sign out the user by removing user data from shared preferences
    public static void signOutUser(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}
