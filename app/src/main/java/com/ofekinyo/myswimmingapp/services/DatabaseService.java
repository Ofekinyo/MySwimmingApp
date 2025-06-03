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
    private static final String USERS_PATH = "Users";
    private static final String TUTORS_PATH = USERS_PATH + "/Tutors";
    private static final String SWIMMERS_PATH = USERS_PATH + "/Swimmers";
    private static final String ADMINS_PATH = USERS_PATH + "/Admins";
    private static final String REQUESTS_PATH = "Requests";
    private static final String SWIMMER_REQUESTS_PATH = REQUESTS_PATH + "/Swimmers";
    private static final String TUTOR_REQUESTS_PATH = REQUESTS_PATH + "/Tutors";

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
        // First try Tutors
        getData(TUTORS_PATH + "/" + uid, User.class, new DatabaseCallback<User>() {
            @Override
            public void onCompleted(User user) {
                if (user != null) {
                    callback.onCompleted(user);
                } else {
                    // Try Swimmers
                    getData(SWIMMERS_PATH + "/" + uid, User.class, new DatabaseCallback<User>() {
                        @Override
                        public void onCompleted(User user) {
                            if (user != null) {
                                callback.onCompleted(user);
                            } else {
                                // Try Admins
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

    public void getTutor(@NotNull final String tutorId, @NotNull final DatabaseCallback<Tutor> callback) {
        getData(TUTORS_PATH + "/" + tutorId, Tutor.class, callback);
    }

    public void getSwimmer(@NotNull final String swimmerId, @NotNull final DatabaseCallback<Swimmer> callback) {
        getData(SWIMMERS_PATH + "/" + swimmerId, Swimmer.class, callback);
    }

    public void createNewRequest(@NotNull final SessionRequest request, @Nullable final DatabaseCallback<Void> callback) {
        Log.d(TAG, "Creating new request - SwimmerId: " + request.getSwimmerId() + 
              ", TutorId: " + request.getTutorId());
              
        String swimmerId = request.getSwimmerId();
        String tutorId = request.getTutorId();

        // Get swimmer details
        getSwimmer(swimmerId, new DatabaseCallback<Swimmer>() {
            @Override
            public void onCompleted(Swimmer swimmer) {
                if (swimmer != null) {
                    Log.d(TAG, "Found swimmer: " + swimmer.getFname() + " " + swimmer.getLname());
                    // Get tutor details
                    getTutor(tutorId, new DatabaseCallback<Tutor>() {
                        @Override
                        public void onCompleted(Tutor tutor) {
                            if (tutor != null) {
                                Log.d(TAG, "Found tutor: " + tutor.getFname() + " " + tutor.getLname());
                                // Update request with names
                                request.setSwimmerName(swimmer.getFname() + " " + swimmer.getLname());
                                request.setTutorName(tutor.getFname() + " " + tutor.getLname());

                                Log.d(TAG, "Saving request with names - Swimmer: " + request.getSwimmerName() + 
                                      ", Tutor: " + request.getTutorName());

                                // Save the request in both paths
                                writeData(SWIMMER_REQUESTS_PATH + "/" + swimmerId + "/" + tutorId, request, null);
                                writeData(TUTOR_REQUESTS_PATH + "/" + tutorId + "/" + swimmerId, request, callback);
                            } else {
                                Log.e(TAG, "Tutor not found for ID: " + tutorId);
                                if (callback != null) callback.onFailed(new Exception("Tutor not found"));
                            }
                        }

                        @Override
                        public void onFailed(Exception e) {
                            Log.e(TAG, "Error getting tutor details", e);
                            if (callback != null) callback.onFailed(e);
                        }
                    });
                } else {
                    Log.e(TAG, "Swimmer not found for ID: " + swimmerId);
                    if (callback != null) callback.onFailed(new Exception("Swimmer not found"));
                }
            }

            @Override
            public void onFailed(Exception e) {
                Log.e(TAG, "Error getting swimmer details", e);
                if (callback != null) callback.onFailed(e);
            }
        });
    }

    public void getAllTutors(@NotNull final DatabaseCallback<List<Tutor>> callback) {
        readData(TUTORS_PATH).get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e(TAG, "Error getting tutors", task.getException());
                callback.onFailed(task.getException());
                return;
            }
            List<Tutor> tutors = new ArrayList<>();

            task.getResult().getChildren().forEach(dataSnapshot -> {
                try {
                    Tutor tutor = dataSnapshot.getValue(Tutor.class);
                    if (tutor != null) {
                        tutors.add(tutor);
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Error parsing tutor data", e);
                }
            });

            callback.onCompleted(tutors);
        });
    }

    public void getAllSwimmers(@NotNull final DatabaseCallback<List<Swimmer>> callback) {
        readData(SWIMMERS_PATH).get().addOnCompleteListener(task -> {
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
    public void getPendingRequestsForTutor(String tutorId, @NotNull final DatabaseCallback<List<SessionRequest>> callback) {
        Log.d(TAG, "Getting pending requests for tutor: " + tutorId);
        readData(TUTOR_REQUESTS_PATH + "/" + tutorId).get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e(TAG, "Error getting tutor requests", task.getException());
                callback.onFailed(task.getException());
                return;
            }

            Log.d(TAG, "Successfully retrieved data from path: " + TUTOR_REQUESTS_PATH + "/" + tutorId);
            Log.d(TAG, "Number of children: " + task.getResult().getChildrenCount());

            List<SessionRequest> requests = new ArrayList<>();
            
            // Process each request
            task.getResult().getChildren().forEach(requestSnapshot -> {
                Log.d(TAG, "Processing request with key: " + requestSnapshot.getKey());
                SessionRequest request = requestSnapshot.getValue(SessionRequest.class);
                
                if (request != null) {
                    Log.d(TAG, "Request details - SwimmerId: " + request.getSwimmerId() + 
                          ", TutorId: " + request.getTutorId() + 
                          ", Status: " + request.getStatus());
                    
                    if (request.getSwimmerId() != null) {
                        // Get swimmer details to ensure we have the name
                        getSwimmer(request.getSwimmerId(), new DatabaseCallback<Swimmer>() {
                            @Override
                            public void onCompleted(Swimmer swimmer) {
                                if (swimmer != null) {
                                    Log.d(TAG, "Found swimmer: " + swimmer.getFname() + " " + swimmer.getLname());
                                    // Update the swimmer name in the request
                                    request.setSwimmerName(swimmer.getFname() + " " + swimmer.getLname());
                                    requests.add(request);
                                    
                                    Log.d(TAG, "Current requests size: " + requests.size() + 
                                          ", Total expected: " + task.getResult().getChildrenCount());
                                    
                                    // If this is the last request, return the list
                                    if (requests.size() == task.getResult().getChildrenCount()) {
                                        Log.d(TAG, "All requests processed. Returning list of size: " + requests.size());
                                        callback.onCompleted(requests);
                                    }
                                } else {
                                    Log.e(TAG, "Swimmer not found for ID: " + request.getSwimmerId());
                                }
                            }

                            @Override
                            public void onFailed(Exception e) {
                                Log.e(TAG, "Error getting swimmer details for ID: " + 
                                      request.getSwimmerId(), e);
                            }
                        });
                    } else {
                        Log.e(TAG, "Request has null swimmerId");
                    }
                } else {
                    Log.e(TAG, "Failed to parse request from snapshot: " + requestSnapshot.getKey());
                }
            });

            // If there are no requests, return empty list
            if (task.getResult().getChildrenCount() == 0) {
                Log.d(TAG, "No requests found for tutor: " + tutorId);
                callback.onCompleted(requests);
            }
        });
    }

    public void getUserRole(@NotNull final String userId, @NotNull final DatabaseCallback<String> callback) {
        // Check Tutors first
        readData(TUTORS_PATH + "/" + userId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult().exists()) {
                callback.onCompleted("Tutor");
                return;
            }
            // Check Swimmers next
            readData(SWIMMERS_PATH + "/" + userId).get().addOnCompleteListener(task2 -> {
                if (task2.isSuccessful() && task2.getResult().exists()) {
                    callback.onCompleted("Swimmer");
                    return;
                }
                // Finally check Admins
                readData(ADMINS_PATH + "/" + userId).get().addOnCompleteListener(task3 -> {
                    if (task3.isSuccessful() && task3.getResult().exists()) {
                        callback.onCompleted("Admin");
                    } else {
                        callback.onCompleted(null);
                    }
                });
            });
        });
    }

    public void getUserData(@NotNull final String userId, @NotNull final String role, @NotNull final DatabaseCallback<User> callback) {
        String path;
        Class<?> userClass;
        
        switch (role) {
            case "Tutor":
                path = TUTORS_PATH + "/" + userId;
                userClass = Tutor.class;
                break;
            case "Swimmer":
                path = SWIMMERS_PATH + "/" + userId;
                userClass = Swimmer.class;
                break;
            case "Admin":
                path = ADMINS_PATH + "/" + userId;
                userClass = User.class;
                break;
            default:
                callback.onFailed(new Exception("Invalid role: " + role));
                return;
        }

        readData(path).get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                callback.onFailed(task.getException());
                return;
            }
            
            try {
                User user = (User) task.getResult().getValue(userClass);
                if (user == null) {
                    callback.onFailed(new Exception("User data not found"));
                } else {
                    callback.onCompleted(user);
                }
            } catch (Exception e) {
                callback.onFailed(new Exception("Error parsing user data: " + e.getMessage()));
            }
        });
    }
}
