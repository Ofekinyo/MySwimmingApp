package com.ofekinyo.myswimmingapp.services;

import android.util.Log;

import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ofekinyo.myswimmingapp.models.Swimmer;
import com.ofekinyo.myswimmingapp.models.Tutor;
import com.ofekinyo.myswimmingapp.models.User;

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
        // Store the user's information under the appropriate node based on their role (Tutor or Swimmer)
        if ("Tutor".equals(role)) {
            mDatabase.getReference("Tutors").child(userId).setValue(true);  // Add any relevant user data as needed
        } else if ("Swimmer".equals(role)) {
            mDatabase.getReference("Swimmers").child(userId).setValue(true);  // Add any relevant user data as needed
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

    // New method to fetch full current user data (either Tutor or Swimmer)
    public void getCurrentUser(@NotNull final AuthCallback<User> callback) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            callback.onFailed(new Exception("No authenticated user found"));
            return;
        }
        String uid = currentUser.getUid();

        // First try to fetch from Tutors node
        DatabaseReference tutorRef = mDatabase.getReference("Tutors").child(uid);
        tutorRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NotNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Tutor tutor = snapshot.getValue(Tutor.class);
                    if (tutor != null) {
                        callback.onCompleted(tutor);
                        return;
                    }
                }
                // If not a tutor, try Swimmers node
                DatabaseReference swimmerRef = mDatabase.getReference("Swimmers").child(uid);
                swimmerRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NotNull DataSnapshot swimmerSnapshot) {
                        if (swimmerSnapshot.exists()) {
                            Swimmer swimmer = swimmerSnapshot.getValue(Swimmer.class);
                            if (swimmer != null) {
                                callback.onCompleted(swimmer);
                                return;
                            }
                        }
                        callback.onFailed(new Exception("User data not found in Tutors or Swimmers nodes"));
                    }

                    @Override
                    public void onCancelled(@NotNull DatabaseError error) {
                        callback.onFailed(error.toException());
                    }
                });
            }

            @Override
            public void onCancelled(@NotNull DatabaseError error) {
                callback.onFailed(error.toException());
            }
        });
    }
}
