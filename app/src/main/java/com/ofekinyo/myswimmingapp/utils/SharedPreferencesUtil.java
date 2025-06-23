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
        editor.putBoolean("isAdmin", user.getAdmin() != null ? user.getAdmin() : false);  // Save admin status
        editor.apply();
    }

    // Check if user is logged in
    public static boolean isUserAdmin(Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).getBoolean("isAdmin", false);
    }

    public static void setUserIsAdmin(Context context, boolean isAdmin) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isAdmin", isAdmin);
        editor.apply();
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
