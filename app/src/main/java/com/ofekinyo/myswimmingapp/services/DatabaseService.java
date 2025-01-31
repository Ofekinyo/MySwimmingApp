package com.ofekinyo.myswimmingapp.services;

import android.util.Log;

import androidx.annotation.Nullable;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ofekinyo.myswimmingapp.models.Trainee;
import com.ofekinyo.myswimmingapp.models.Trainer;
import com.ofekinyo.myswimmingapp.models.User;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class DatabaseService {

    private static final String TAG = "DatabaseService";

    public interface DatabaseCallback<T> {
        void onCompleted(T object);
        void onFailed(Exception e);
    }

    private static DatabaseService instance;
    private final DatabaseReference databaseReference;

    private DatabaseService() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    public static DatabaseService getInstance() {
        if (instance == null) {
            instance = new DatabaseService();
        }
        return instance;
    }

    // Private methods to write and read data from the database

    private void writeData(@NotNull final String path, @NotNull final Object data, final @Nullable DatabaseCallback<Void> callback) {
        databaseReference.child(path).setValue(data).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (callback == null) return;
                callback.onCompleted(task.getResult());
            } else {
                if (callback == null) return;
                callback.onFailed(task.getException());
            }
        });
    }

    private DatabaseReference readData(@NotNull final String path) {
        return databaseReference.child(path);
    }

    public void getUser(@NotNull final String uid, @NotNull final DatabaseCallback<User> callback) {
        getData("users/" + uid, User.class, callback);
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

    private String generateNewId(@NotNull final String path) {
        return databaseReference.child(path).push().getKey();
    }

    // Public methods to interact with the database

    public void createNewTrainer(@NotNull final Trainer trainer, @Nullable final DatabaseCallback<Void> callback) {
        writeData("trainers/" + trainer.getId(), trainer, callback);
        writeData("trainers/" + trainer.getId(), trainer, callback);
    }

    public void createNewTrainee(@NotNull final Trainee swimStudent, @Nullable final DatabaseCallback<Void> callback) {
        writeData("swimStudents/" + swimStudent.getId(), swimStudent, callback);
    }

    // Getters for Trainer, Trainee, Session, and Feedback

    public void getTrainer(@NotNull final String trainerId, @NotNull final DatabaseCallback<Trainer> callback) {
        getData("trainers/" + trainerId, Trainer.class, callback);
    }

    public void getTrainee(@NotNull final String swimStudentId, @NotNull final DatabaseCallback<Trainee> callback) {
        getData("swimStudents/" + swimStudentId, Trainee.class, callback);
    }


    // Generate new IDs for Trainer, Trainee, Session, and Feedback

    public String generateTrainerId() {
        return generateNewId("trainers");
    }

    public String generateTraineeId() {
        return generateNewId("swimStudents");
    }

    public String generateSessionId() {
        return generateNewId("sessions");
    }

    public String generateFeedbackId() {
        return generateNewId("feedbacks");
    }

    // Get all trainers and swim students

    public void getAllTrainers(@NotNull final DatabaseCallback<List<Trainer>> callback) {
        readData("trainers").get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e(TAG, "Error getting trainers", task.getException());
                callback.onFailed(task.getException());
                return;
            }
            List<Trainer> trainers = new ArrayList<>();
            task.getResult().getChildren().forEach(dataSnapshot -> {
                Trainer trainer = dataSnapshot.getValue(Trainer.class);
                trainers.add(trainer);
            });
            callback.onCompleted(trainers);
        });
    }

    public void getAllTrainees(@NotNull final DatabaseCallback<List<Trainee>> callback) {
        readData("swimStudents").get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e(TAG, "Error getting swim students", task.getException());
                callback.onFailed(task.getException());
                return;
            }
            List<Trainee> swimStudents = new ArrayList<>();
            task.getResult().getChildren().forEach(dataSnapshot -> {
                Trainee swimStudent = dataSnapshot.getValue(Trainee.class);
                swimStudents.add(swimStudent);
            });
            callback.onCompleted(swimStudents);
        });
    }
    // Get all sessions for a trainer or swim student
}
