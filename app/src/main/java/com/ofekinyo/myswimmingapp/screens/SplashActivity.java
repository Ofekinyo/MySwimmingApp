package com.ofekinyo.myswimmingapp.screens;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.ofekinyo.myswimmingapp.R;
import com.ofekinyo.myswimmingapp.models.Admin;
import com.ofekinyo.myswimmingapp.models.Swimmer;
import com.ofekinyo.myswimmingapp.models.Tutor;
import com.ofekinyo.myswimmingapp.models.User;
import com.ofekinyo.myswimmingapp.models.UserRole;
import com.ofekinyo.myswimmingapp.services.AuthenticationService;
import com.ofekinyo.myswimmingapp.services.DatabaseService;

public class SplashActivity extends AppCompatActivity {

    DatabaseService databaseService = DatabaseService.getInstance();
    AuthenticationService authenticationService = AuthenticationService.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Find the ImageView
        ImageView splashLogo = findViewById(R.id.splashLogo);

        // Load the spinning animation
        Animation rotate = AnimationUtils.loadAnimation(this, R.anim.rotate);
        splashLogo.startAnimation(rotate);

        // Delay and move to MainActivity after 3 seconds
        new Thread(() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {

//                if (authenticationService.isUserSignedIn()) {
//                    String uid = authenticationService.getCurrentUserId();
//
//                    databaseService.getUser(uid, new DatabaseService.DatabaseCallback<User>() {
//                        @Override
//                        public void onCompleted(User user) {
//                            String role;
//
//                            if (user instanceof Tutor) {
//                                role = "tutor";
//                            } else if (user instanceof Swimmer) {
//                                role = "swimmer";
//                            } else if (user instanceof Admin) {
//                                role = "admin";
//                            } else {
//                                role = "unknown";
//                            }
//
//                            switch (role) {
//                                case "tutor":
//                                    startActivity(new Intent(SplashActivity.this, TutorPage.class));
//                                    finish();
//                                    break;
//                                case "swimmer":
//                                    startActivity(new Intent(SplashActivity.this, SwimmerPage.class));
//                                    finish();
//                                    break;
//                                case "admin":
//                                    startActivity(new Intent(SplashActivity.this, AdminPage.class));
//                                    finish();
//                                    break;
//                                default:
//                                    Log.d("CheckRole", "User role is unknown");
//                                    // handle unknown role
//                                    break;
//                            }
//                        }
//
//                        @Override
//                        public void onFailed(Exception e) {
//                            Log.e("CheckRole", "Error getting user", e);
//                        }
//                    });
//
//                } else {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();
                //}
            }
        }).start();
    }
}
