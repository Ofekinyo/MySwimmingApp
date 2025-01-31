package com.ofekinyo.myswimmingapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.ofekinyo.myswimmingapp.models.User;

public class SharedPreferencesUtil {

    private static final String PREF_NAME = "com.example.testapp.PREFERENCE_FILE_KEY";

    // Save a string to shared preferences
    private static void saveString(Context context, String key, String value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    // Get a string from shared preferences
    private static String getString(Context context, String key, String defaultValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, defaultValue);
    }

    // Save an integer to shared preferences
    private static void saveInt(Context context, String key, int value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    // Get an integer from shared preferences
    private static int getInt(Context context, String key, int defaultValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(key, defaultValue);
    }

    // Clear all data from shared preferences
    public static void clear(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    // Remove a specific key from shared preferences
    private static void remove(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key);
        editor.apply();
    }

    // Check if a key exists in shared preferences
    private static boolean contains(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.contains(key);
    }

    // Save user object to shared preferences
    public static void saveUser(Context context, User user, String role) {
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
        editor.putString("role", role);  // Save the role as Trainer or Trainee
        editor.apply();
    }

    // Get user object from shared preferences
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
        String role = sharedPreferences.getString("role", "");  // Retrieve the role

        // Create and return the User object with role
        return new User(uid, Fname, Lname, phone, email, age, city, gender, password, role);
    }

    // Sign out user by removing user data from shared preferences
    public static void signOutUser(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("uid");
        editor.remove("email");
        editor.remove("password");
        editor.remove("Fname");
        editor.remove("Lname");
        editor.remove("phone");
        editor.remove("age");
        editor.remove("city");
        editor.remove("gender");
        editor.remove("role");  // Remove role as well
        editor.apply();
    }

    // Check if a user is logged in
    public static boolean isUserLoggedIn(Context context) {
        return contains(context, "uid");
    }

    // Get the user's role (Trainer/Trainee)
    public static String getUserRole(Context context) {
        return getString(context, "role", "");  // Retrieve the role from shared preferences
    }
}
