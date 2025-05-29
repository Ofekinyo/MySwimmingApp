package com.ofekinyo.myswimmingapp.services;

import android.util.Log;

import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.ofekinyo.myswimmingapp.models.Request;
import com.ofekinyo.myswimmingapp.models.Swimmer;
import com.ofekinyo.myswimmingapp.models.Tutor;
import com.ofekinyo.myswimmingapp.models.User;
import com.ofekinyo.myswimmingapp.models.SessionRequest;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseService {

    private static final String TAG = "DatabaseService";

    public interface DatabaseCallback<T> {
        void onCompleted(T object);
        void onFailed(Exception e);
    }

    // New callback interface for requests
    public interface RequestsCallback {
        void onRequestsReceived(List<SessionRequest> requests);
        void onError(String errorMessage);
    }

    private static DatabaseService instance;
    private final DatabaseReference databaseReference;
    private final FirebaseAuth firebaseAuth;

    private DatabaseService() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public static DatabaseService getInstance() {
        if (instance == null) {
            instance = new DatabaseService();
        }
        return instance;
    }

    public void writeData(@NotNull final String path, @NotNull final Object data, final @Nullable DatabaseCallback<Void> callback) {
        databaseReference.child(path).setValue(data).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (callback != null) {
                    callback.onCompleted(null);
                }
            } else {
                if (callback != null) {
                    callback.onFailed(task.getException());
                }
            }
        });
    }

    private DatabaseReference readData(@NotNull final String path) {
        return databaseReference.child(path);
    }

    private <T> void getDataList(@NotNull final String path, @NotNull final Class<T> clazz, @NotNull final DatabaseCallback<List<T>> callback) {
        readData(path).get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e(TAG, "Error getting data", task.getException());
                callback.onFailed(task.getException());
                return;
            }
            List<T> tList = new ArrayList<>();
            task.getResult().getChildren().forEach(dataSnapshot -> {
                T t = dataSnapshot.getValue(clazz);
                tList.add(t);
            });

            callback.onCompleted(tList);
        });
    }

    public void getUser(@NotNull final String uid, @NotNull final DatabaseCallback<User> callback) {
        getData("users/" + uid, User.class, callback);
    }

    /**
     * Method to get currently authenticated user's full data from DB
     */
    public void getCurrentUser(@NotNull final DatabaseCallback<User> callback) {
        if (firebaseAuth.getCurrentUser() == null) {
            callback.onFailed(new Exception("No authenticated user found"));
            return;
        }
        String uid = firebaseAuth.getCurrentUser().getUid();
        getUser(uid, callback);
    }

    public void getUserList(@NotNull final DatabaseCallback<List<User>> callback) {
        getDataList("users/", User.class, callback);
    }

    private <T> void getData(@NotNull final String path, @NotNull final Class<T> clazz, @NotNull final DatabaseCallback<T> callback) {
        readData(path).get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e(TAG, "Error getting data", task.getException());
                callback.onFailed(task.getException());
                return;
            }
            T data = task.getResult().getValue(clazz);
            callback.onCompleted(data);
        });
    }

    public String generateNewId(@NotNull final String path) {
        return databaseReference.child(path).push().getKey();
    }

    public void createNewTutor(@NotNull final Tutor tutor, @Nullable final DatabaseCallback<Void> callback) {
        writeData("Tutors/" + tutor.getId(), tutor, callback);
    }

    public void createNewSwimmer(@NotNull final Swimmer swimStudent, @Nullable final DatabaseCallback<Void> callback) {
        writeData("Swimmers/" + swimStudent.getId(), swimStudent, callback);
    }

    public void getTutor(@NotNull final String tutorId, @NotNull final DatabaseCallback<Tutor> callback) {
        getData("Tutors/" + tutorId, Tutor.class, callback);
    }

    public void getSwimmer(@NotNull final String swimStudentId, @NotNull final DatabaseCallback<Swimmer> callback) {
        getData("Swimmers/" + swimStudentId, Swimmer.class, callback);
    }

    public void createNewRequest(@NotNull final Request request, @Nullable final DatabaseCallback<Void> callback) {
        String swimmerId = request.getSwimmerId();
        String tutorId = request.getTutorId();

        writeData("SwimmerRequest/" + swimmerId + "/" + tutorId, request, callback);
        writeData("TutorRequest/" + tutorId + "/" + swimmerId, request, callback);
    }

    public void getAllTutors(@NotNull final DatabaseCallback<List<Tutor>> callback) {
        readData("Tutors").get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e(TAG, "Error getting tutors", task.getException());
                callback.onFailed(task.getException());
                return;
            }
            List<Tutor> tutors = new ArrayList<>();

            task.getResult().getChildren().forEach(dataSnapshot -> {
                Tutor tutor = dataSnapshot.getValue(Tutor.class);
                tutor = new Tutor(tutor);
                tutors.add(tutor);
            });
            callback.onCompleted(tutors);
        });
    }

    public void getAllSwimmers(@NotNull final DatabaseCallback<List<Swimmer>> callback) {
        readData("Swimmers").get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e(TAG, "Error getting swimmers", task.getException());
                callback.onFailed(task.getException());
                return;
            }
            List<Swimmer> swimmers = new ArrayList<>();
            task.getResult().getChildren().forEach(dataSnapshot -> {
                Swimmer swimmer = dataSnapshot.getValue(Swimmer.class);
                swimmers.add(swimmer);
            });
            callback.onCompleted(swimmers);
        });
    }

    // --- New method to get pending requests for a specific tutor ---
    public void getPendingRequestsForTutor(@Nullable final DatabaseCallback<List<SessionRequest>> callback) {
        getDataList("SessionRequests", SessionRequest.class, new DatabaseCallback<List<SessionRequest>>() {
            @Override
            public void onCompleted(List<SessionRequest> sessionRequests) {
                sessionRequests.removeIf(sessionRequest -> !sessionRequest.getStatus().equalsIgnoreCase("pending"));
                callback.onCompleted(sessionRequests);
            }

            @Override
            public void onFailed(Exception e) {
                callback.onFailed(e);
            }
        });
    }
}
