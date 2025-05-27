package com.ofekinyo.myswimmingapp.services;

import android.util.Log;

import androidx.annotation.Nullable;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.ofekinyo.myswimmingapp.models.Request;
import com.ofekinyo.myswimmingapp.models.Swimmer;
import com.ofekinyo.myswimmingapp.models.Turor;
import com.ofekinyo.myswimmingapp.models.User;

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

    public void writeData(@NotNull final String path, @NotNull final Object data, final @Nullable DatabaseCallback<Void> callback) {
        databaseReference.child(path).setValue(data).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (callback == null) return;
                callback.onCompleted(null);
            } else {
                if (callback == null) return;
                callback.onFailed(task.getException());
            }
        });
    }

    private DatabaseReference readData(@NotNull final String path) {
        return databaseReference.child(path);
    }

    /// get a list of data from the database at a specific path
    /// @param path the path to get the data from
    /// @param clazz the class of the objects to return
    /// @param callback the callback to call when the operation is completed
    private <T> void getDataList(@NotNull final String path, @NotNull final Class<T> clazz, @NotNull Map<String, String> filter, @NotNull final DatabaseCallback<List<T>> callback) {
        Query dbRef = readData(path);

        for (Map.Entry<String, String> entry : filter.entrySet()) {
            dbRef = dbRef.orderByChild(entry.getKey()).equalTo(entry.getValue());
        }

        dbRef.get().addOnCompleteListener(task -> {
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

    /// get all the users from the database
    /// @param callback the callback to call when the operation is completed
    ///              the callback will receive a list of user objects
    ///            if the operation fails, the callback will receive an exception
    /// @see DatabaseCallback
    /// @see List
    /// @see User
    public void getUserList(@NotNull final DatabaseCallback<List<User>> callback) {
        getDataList("users/", User.class, new HashMap<>(), callback);
    }

    /// get data from the database at a specific path
    /// @param path the path to get the data from
    /// @param clazz the class of the object to return
    /// @param callback the callback to call when the operation is completed
    /// @see DatabaseCallback
    /// @see Class
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

    public void createNewTrainer(@NotNull final Turor turor, @Nullable final DatabaseCallback<Void> callback) {
        writeData("Trainers/" + turor.getId(), turor, callback);
    }

    public void createNewTrainee(@NotNull final Swimmer swimStudent, @Nullable final DatabaseCallback<Void> callback) {
        writeData("Trainees/" + swimStudent.getId(), swimStudent, callback);
    }

    public void getTrainer(@NotNull final String trainerId, @NotNull final DatabaseCallback<Turor> callback) {
        getData("Trainers/" + trainerId, Turor.class, callback);
    }

    public void getTrainee(@NotNull final String swimStudentId, @NotNull final DatabaseCallback<Swimmer> callback) {
        getData("Trainees/" + swimStudentId, Swimmer.class, callback);
    }



    public void createNewRequest(@NotNull final Request request, @Nullable final DatabaseCallback<Void> callback) {
        String traineeId = request.getTraineeId();
        String trainerId = request.getTrainerId();

        writeData("TraineeRequest/" + traineeId + "/" + trainerId, request, callback);
        writeData("TrainerRequest/" + trainerId + "/" + traineeId, request, callback);
    }



    public void getAllTrainers(@NotNull final DatabaseCallback<List<Turor>> callback) {
        readData("Trainers").get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e(TAG, "Error getting trainers", task.getException());
                callback.onFailed(task.getException());
                return;
            }
            List<Turor> turors = new ArrayList<>();

            task.getResult().getChildren().forEach(dataSnapshot -> {
                Turor turor = dataSnapshot.getValue(Turor.class);

                turor =new Turor(turor);
                turors.add(turor);
            });
            callback.onCompleted(turors);
        });
    }

    public void getAllTrainees(@NotNull final DatabaseCallback<List<Swimmer>> callback) {
        readData("Trainees").get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e(TAG, "Error getting trainees", task.getException());
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
}
