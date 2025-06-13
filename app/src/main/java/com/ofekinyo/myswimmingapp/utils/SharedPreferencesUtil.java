package com.ofekinyo.myswimmingapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.ofekinyo.myswimmingapp.models.User;

public class SharedPreferencesUtil {

    private static final String PREF_NAME = "SwimLinkPrefs";

    private static SharedPreferences getPrefs(Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static void clearAll(Context context) {
        getPrefs(context).edit().clear().apply();
    }

    // Save user data (excluding password for security)
    public static void saveUser(Context context, User user) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("uid", user.getId());
        editor.putString("email", user.getEmail());
        editor.putString("Fname", user.getFname());
        editor.putString("Lname", user.getLname());
        editor.putString("phone", String.valueOf(user.getPhone()));  // Store as String to avoid issues
        editor.putString("age", String.valueOf(user.getAge()));      // Store as String
        editor.putString("city", user.getCity());
        editor.putString("gender", user.getGender());
        editor.putString("role", user.getRole());  // Save role (Swimmer or Tutor)
        editor.apply();
    }

    // Retrieve user data
    public static User getUser(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        if (!isUserLoggedIn(context)) {
            return null;
        }

        String uid = sharedPreferences.getString("uid", "");
        String email = sharedPreferences.getString("email", "");
        String Fname = sharedPreferences.getString("Fname", "");
        String Lname = sharedPreferences.getString("Lname", "");
        String phone = (sharedPreferences.getString("phone", "0"));  // Convert safely
        int age = Integer.parseInt(sharedPreferences.getString("age", "0"));
        String city = sharedPreferences.getString("city", "");
        String gender = sharedPreferences.getString("gender", "");
        String role = sharedPreferences.getString("role", "");  // Retrieve role

        return new User(uid, Fname, Lname, phone, email, age, gender, city, "", role);
    }

    // Check if user is logged in
    public static boolean isUserLoggedIn(Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).contains("uid");
    }

    // Sign out user (clear stored data)
    public static void signOutUser(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}
