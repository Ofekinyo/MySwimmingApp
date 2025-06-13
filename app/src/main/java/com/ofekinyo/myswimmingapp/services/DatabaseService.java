package com.ofekinyo.myswimmingapp.services;

import android.util.Log;

import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.ofekinyo.myswimmingapp.models.Admin;
import com.ofekinyo.myswimmingapp.models.Session;
import com.ofekinyo.myswimmingapp.models.Swimmer;
import com.ofekinyo.myswimmingapp.models.Tutor;
import com.ofekinyo.myswimmingapp.models.User;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseService {

    private static final String TAG = "DatabaseService";
    private static final String USERS_PATH = "Users";
    private static final String TUTORS_PATH = USERS_PATH + "/Tutors";
    private static final String SWIMMERS_PATH = USERS_PATH + "/Swimmers";
    private static final String ADMINS_PATH = USERS_PATH + "/Admins";
    private static final String SESSIONS_PATH = "Sessions";

    public interface DatabaseCallback<T> {
        void onCompleted(T object);
        void onFailed(Exception e);
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

    public void createNewTutor(@NotNull final Tutor tutor, @Nullable final DatabaseCallback<Void> callback) {
        writeData(TUTORS_PATH + "/" + tutor.getId(), tutor, callback);
    }

    public void createNewSwimmer(@NotNull final Swimmer swimmer, @Nullable final DatabaseCallback<Void> callback) {
        writeData(SWIMMERS_PATH + "/" + swimmer.getId(), swimmer, callback);
    }

    public void createNewAdmin(@NotNull final Admin admin, @Nullable final DatabaseCallback<Void> callback) {
        writeData(ADMINS_PATH + "/" + admin.getId(), admin, callback);
    }

    public void getTutor(@NotNull final String tutorId, @NotNull final DatabaseCallback<Tutor> callback) {
        getData(TUTORS_PATH + "/" + tutorId, Tutor.class, callback);
    }

    public void getSwimmer(@NotNull final String swimmerId, @NotNull final DatabaseCallback<Swimmer> callback) {
        getData(SWIMMERS_PATH + "/" + swimmerId, Swimmer.class, callback);
    }

    public void getAdmin(@NotNull final String adminId, @NotNull final DatabaseCallback<Admin> callback) {
        getData(ADMINS_PATH+"/"+ adminId, Admin.class, callback);
    }


    public void getAllTutors(@NotNull final DatabaseCallback<List<Tutor>> callback) {
        getDataList(TUTORS_PATH, Tutor.class, callback);
    }

    public void getAllSwimmers(@NotNull final DatabaseCallback<List<Swimmer>> callback) {
        getDataList(SWIMMERS_PATH, Swimmer.class, callback);
    }

    public void getAllAdmins(@NotNull final DatabaseCallback<List<Admin>> callback) {
        getDataList(ADMINS_PATH, Admin.class, callback);
    }

    public void updateSessionStatus(Session session, DatabaseCallback<Void> callback) {
        DatabaseReference sessionRef = databaseReference.child(SESSIONS_PATH).child(session.getId());
        Map<String, Object> updates = new HashMap<>();
        updates.put("isAccepted", session.getIsAccepted());
        
        sessionRef.updateChildren(updates)
            .addOnSuccessListener(aVoid -> callback.onCompleted(null))
            .addOnFailureListener(e -> callback.onFailed(e));
    }

    public void getSessions(@NotNull final DatabaseCallback<List<Session>> callback) {
        getDataList(SESSIONS_PATH, Session.class, callback);
    }

    public String generateSessionId() {
        return generateNewId(SESSIONS_PATH);
    }

    public void createNewSession(@NotNull final Session session, @Nullable final DatabaseCallback<Void> callback) {
        writeData(SESSIONS_PATH + "/" + session.getId(), session, callback);
    }


    public void getUser(String uid, @NotNull final DatabaseCallback<User> callback) {
        getTutor(uid, new DatabaseCallback<Tutor>() {
            @Override
            public void onCompleted(Tutor tutor) {
                if (tutor == null) {
                    getSwimmer(uid, new DatabaseCallback<Swimmer>() {
                        @Override
                        public void onCompleted(Swimmer swimmer) {
                            if (swimmer == null) {
                                getAdmin(uid, new DatabaseCallback<Admin>() {
                                    @Override
                                    public void onCompleted(Admin admin) {
                                        callback.onCompleted(admin);
                                    }

                                    @Override
                                    public void onFailed(Exception e) {
                                        callback.onFailed(e);
                                    }
                                });
                                return;
                            }
                            callback.onCompleted(swimmer);
                        }

                        @Override
                        public void onFailed(Exception e) {
                            callback.onFailed(e);
                        }
                    });
                    return;
                }
                callback.onCompleted(tutor);
            }

            @Override
            public void onFailed(Exception e) {
                callback.onFailed(e);
            }
        });
    }
}
