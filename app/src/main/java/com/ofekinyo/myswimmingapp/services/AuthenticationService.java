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
    private static final String USERS_PATH = "Users";
    private static final String TUTORS_PATH = USERS_PATH + "/Tutors";
    private static final String SWIMMERS_PATH = USERS_PATH + "/Swimmers";
    private static final String ADMINS_PATH = USERS_PATH + "/Admins";

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
        String path = "";
        switch (role.toLowerCase()) {
            case "tutor":
                path = TUTORS_PATH;
                break;
            case "swimmer":
                path = SWIMMERS_PATH;
                break;
            case "admin":
                path = ADMINS_PATH;
                break;
            default:
                Log.e(TAG, "Invalid role: " + role);
                return;
        }
        mDatabase.getReference(path).child(userId).setValue(true);
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

    public void getCurrentUser(@NotNull final AuthCallback<User> callback) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            callback.onFailed(new Exception("No authenticated user found"));
            return;
        }
        String uid = currentUser.getUid();

        // First try to fetch from Tutors node
        DatabaseReference tutorRef = mDatabase.getReference(TUTORS_PATH).child(uid);
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
                DatabaseReference swimmerRef = mDatabase.getReference(SWIMMERS_PATH).child(uid);
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
                        // If not a swimmer, try Admins node
                        DatabaseReference adminRef = mDatabase.getReference(ADMINS_PATH).child(uid);
                        adminRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NotNull DataSnapshot adminSnapshot) {
                                if (adminSnapshot.exists()) {
                                    User admin = adminSnapshot.getValue(User.class);
                                    if (admin != null) {
                                        callback.onCompleted(admin);
                                        return;
                                    }
                                }
                                callback.onFailed(new Exception("User data not found in any role nodes"));
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

            @Override
            public void onCancelled(@NotNull DatabaseError error) {
                callback.onFailed(error.toException());
            }
        });
    }
}
