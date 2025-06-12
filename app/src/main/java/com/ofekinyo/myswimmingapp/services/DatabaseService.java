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
    private static final String REQUESTS_PATH = "Requests";
    private static final String SWIMMER_REQUESTS_PATH = REQUESTS_PATH + "/Swimmers";
    private static final String TUTOR_REQUESTS_PATH = REQUESTS_PATH + "/Tutors";
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

    public void getUserData(String userId, String role, @NotNull final DatabaseCallback<User> callback) {
        String path;
        switch (role) {
            case "Tutor":
                path = TUTORS_PATH;
                break;
            case "Swimmer":
                path = SWIMMERS_PATH;
                break;
            case "Admin":
                path = ADMINS_PATH;
                break;
            default:
                callback.onFailed(new Exception("Invalid role"));
                return;
        }

        readData(path + "/" + userId).get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                callback.onFailed(task.getException());
                return;
            }

            User user = null;
            switch (role) {
                case "Tutor":
                    user = task.getResult().getValue(Tutor.class);
                    break;
                case "Swimmer":
                    user = task.getResult().getValue(Swimmer.class);
                    break;
                case "Admin":
                    user = task.getResult().getValue(Admin.class);
                    break;
            }

            if (user != null) {
                callback.onCompleted(user);
            } else {
                callback.onFailed(new Exception("User not found"));
            }
        });
    }

    public void getUserRole(String userId, @NotNull final DatabaseCallback<String> callback) {
        // First check Tutors
        readData(TUTORS_PATH + "/" + userId).get().addOnCompleteListener(tutorTask -> {
            if (tutorTask.isSuccessful() && tutorTask.getResult().exists()) {
                callback.onCompleted("Tutor");
                return;
            }

            // Then check Swimmers
            readData(SWIMMERS_PATH + "/" + userId).get().addOnCompleteListener(swimmerTask -> {
                if (swimmerTask.isSuccessful() && swimmerTask.getResult().exists()) {
                    callback.onCompleted("Swimmer");
                    return;
                }

                // Finally check Admins
                readData(ADMINS_PATH + "/" + userId).get().addOnCompleteListener(adminTask -> {
                    if (adminTask.isSuccessful() && adminTask.getResult().exists()) {
                        callback.onCompleted("Admin");
                    } else {
                        callback.onFailed(new Exception("User role not found"));
                    }
                });
            });
        });
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
        getData(TUTORS_PATH + "/" + uid, User.class, new DatabaseCallback<User>() {
            @Override
            public void onCompleted(User user) {
                if (user != null) {
                    callback.onCompleted(user);
                } else {
                    getData(SWIMMERS_PATH + "/" + uid, User.class, new DatabaseCallback<User>() {
                        @Override
                        public void onCompleted(User user) {
                            if (user != null) {
                                callback.onCompleted(user);
                            } else {
                                getData(ADMINS_PATH + "/" + uid, User.class, callback);
                            }
                        }

                        @Override
                        public void onFailed(Exception e) {
                            callback.onFailed(e);
                        }
                    });
                }
            }

            @Override
            public void onFailed(Exception e) {
                callback.onFailed(e);
            }
        });
    }

    public void getCurrentUser(@NotNull final DatabaseCallback<User> callback) {
        if (firebaseAuth.getCurrentUser() == null) {
            callback.onFailed(new Exception("No authenticated user found"));
            return;
        }
        String uid = firebaseAuth.getCurrentUser().getUid();
        getUser(uid, callback);
    }

    public void getUserList(@NotNull final DatabaseCallback<List<User>> callback) {
        getDataList(USERS_PATH, User.class, callback);
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

    public void createNewRequest(@NotNull final Session request, @Nullable final DatabaseCallback<Void> callback) {
        String swimmerId = request.getSwimmerId();
        String tutorId = request.getTutorId();

        getSwimmer(swimmerId, new DatabaseCallback<Swimmer>() {
            @Override
            public void onCompleted(Swimmer swimmer) {
                if (swimmer != null) {
                    getTutor(tutorId, new DatabaseCallback<Tutor>() {
                        @Override
                        public void onCompleted(Tutor tutor) {
                            if (tutor != null) {
                                // Removed setting swimmerName and tutorName here
                                request.setIsAccepted(null); // pending

                                writeData(SWIMMER_REQUESTS_PATH + "/" + swimmerId + "/" + tutorId, request, null);
                                writeData(TUTOR_REQUESTS_PATH + "/" + tutorId + "/" + swimmerId, request, callback);
                            } else {
                                if (callback != null) callback.onFailed(new Exception("Tutor not found"));
                            }
                        }

                        @Override
                        public void onFailed(Exception e) {
                            if (callback != null) callback.onFailed(e);
                        }
                    });
                } else {
                    if (callback != null) callback.onFailed(new Exception("Swimmer not found"));
                }
            }

            @Override
            public void onFailed(Exception e) {
                if (callback != null) callback.onFailed(e);
            }
        });
    }

    public void getAllTutors(@NotNull final DatabaseCallback<List<Tutor>> callback) {
        readData(TUTORS_PATH).get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                callback.onFailed(task.getException());
                return;
            }
            List<Tutor> tutors = new ArrayList<>();
            task.getResult().getChildren().forEach(dataSnapshot -> {
                Tutor tutor = dataSnapshot.getValue(Tutor.class);
                if (tutor != null) tutors.add(tutor);
            });
            callback.onCompleted(tutors);
        });
    }

    public void getAllSwimmers(@NotNull final DatabaseCallback<List<Swimmer>> callback) {
        readData(SWIMMERS_PATH).get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                callback.onFailed(task.getException());
                return;
            }
            List<Swimmer> swimmers = new ArrayList<>();
            task.getResult().getChildren().forEach(dataSnapshot -> {
                Swimmer swimmer = dataSnapshot.getValue(Swimmer.class);
                if (swimmer != null) swimmers.add(swimmer);
            });
            callback.onCompleted(swimmers);
        });
    }

    public void getPendingRequestsForTutor(String tutorId, @NotNull final DatabaseCallback<List<Session>> callback) {
        readData(SESSIONS_PATH).get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                callback.onFailed(task.getException());
                return;
            }

            List<Session> pendingRequests = new ArrayList<>();
            task.getResult().getChildren().forEach(dataSnapshot -> {
                Session session = dataSnapshot.getValue(Session.class);
                if (session != null && session.getTutorId().equals(tutorId) && !session.getIsAccepted()) {
                    pendingRequests.add(session);
                }
            });

            callback.onCompleted(pendingRequests);
        });
    }

    public void updateSessionStatus(Session session, DatabaseCallback<Void> callback) {
        DatabaseReference sessionRef = databaseReference.child(SESSIONS_PATH).child(session.getId());
        Map<String, Object> updates = new HashMap<>();
        updates.put("isAccepted", session.getIsAccepted());
        
        sessionRef.updateChildren(updates)
            .addOnSuccessListener(aVoid -> callback.onCompleted(null))
            .addOnFailureListener(e -> callback.onFailed(e));
    }

    public void getSessions(String userId, @NotNull final DatabaseCallback<List<Session>> callback) {
        DatabaseReference sessionsRef = databaseReference.child(SESSIONS_PATH);
        sessionsRef.get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e(TAG, "Error getting sessions", task.getException());
                callback.onFailed(task.getException());
                return;
            }

            List<Session> sessions = new ArrayList<>();
            DataSnapshot snapshot = task.getResult();
            
            for (DataSnapshot sessionSnapshot : snapshot.getChildren()) {
                Session session = sessionSnapshot.getValue(Session.class);
                if (session != null && 
                    session.getTutorId().equals(userId) && 
                    session.getIsAccepted() == null) { // pending status
                    session.setId(sessionSnapshot.getKey());
                    sessions.add(session);
                }
            }
            
            callback.onCompleted(sessions);
        });
    }
}
