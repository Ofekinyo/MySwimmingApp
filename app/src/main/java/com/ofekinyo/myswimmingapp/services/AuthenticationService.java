package com.ofekinyo.myswimmingapp.services;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

public class AuthenticationService {

    private static final String TAG = "AuthenticationService";

    public interface AuthCallback<T> {
        void onCompleted(T object);

        void onFailed(Exception e);
    }

    private static AuthenticationService instance;
    private final FirebaseAuth mAuth;
    private final FirebaseDatabase mDatabase;

    private AuthenticationService() {
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
    }

    public static AuthenticationService getInstance() {
        if (instance == null) {
            instance = new AuthenticationService();
        }
        return instance;
    }

    public void signIn(@NotNull final String email, @NotNull final String password, @NotNull final AuthCallback<String> callback) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                callback.onCompleted(getCurrentUserId());
            } else {
                Log.e(TAG, "Error signing in", task.getException());
                callback.onFailed(task.getException());
            }
        });
    }

    public void signUp(@NotNull final String email, @NotNull final String password, @NotNull final String role, @NotNull final AuthCallback<String> callback) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null) {
                    String userId = user.getUid();
                    saveUserRoleToDatabase(userId, role);
                    callback.onCompleted(userId);
                }
            } else {
                Log.e(TAG, "Error signing up", task.getException());
                callback.onFailed(task.getException());
            }
        });
    }

    private void saveUserRoleToDatabase(String userId, String role) {
        // Store the user's information under the appropriate node based on their role (Trainer or Trainee)
        if ("Trainer".equals(role)) {
            mDatabase.getReference("Trainers").child(userId).setValue(true);  // Add any relevant user data as needed
        } else if ("Trainee".equals(role)) {
            mDatabase.getReference("Trainees").child(userId).setValue(true);  // Add any relevant user data as needed
        }
    }

    public void signOut() {
        mAuth.signOut();
    }

    public String getCurrentUserId() {
        if (mAuth.getCurrentUser() == null) {
            return null;
        }
        return mAuth.getCurrentUser().getUid();
    }

    public boolean isUserSignedIn() {
        return mAuth.getCurrentUser() != null;
    }
}
