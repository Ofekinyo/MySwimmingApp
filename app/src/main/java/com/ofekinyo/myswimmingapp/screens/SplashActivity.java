package com.ofekinyo.myswimmingapp.screens;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.ofekinyo.myswimmingapp.R;
import com.ofekinyo.myswimmingapp.utils.SharedPreferencesUtil;

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = "SplashActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Find the ImageView
        ImageView splashLogo = findViewById(R.id.splashLogo);

        // Load the spinning animation
        Animation rotate = AnimationUtils.loadAnimation(this, R.anim.rotate);
        splashLogo.startAnimation(rotate);

        // Delay and check user login status after 3 seconds
        new Thread(() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                checkUserLoginStatus();
            }
        }).start();
    }

    private void checkUserLoginStatus() {
        // Check if user is logged in using SharedPreferences
        if (SharedPreferencesUtil.isUserLoggedIn(this)) {
            Log.d(TAG, "User is logged in, checking role and admin status");
            
            // Get user role and admin status from SharedPreferences
            String role = getSharedPreferences("SwimLinkPrefs", MODE_PRIVATE).getString("role", "");
            boolean isAdmin = SharedPreferencesUtil.isUserAdmin(this);
            
            Log.d(TAG, "User role: " + role + ", isAdmin: " + isAdmin);
            
            Intent intent = null;
            
            if (isAdmin) {
                // Admin user - go to AdminPage
                Log.d(TAG, "Redirecting admin user to AdminPage");
                intent = new Intent(SplashActivity.this, AdminPage.class);
            } else {
                // Regular user - check role
                switch (role) {
                    case "Tutor":
                        Log.d(TAG, "Redirecting tutor to TutorPage");
                        intent = new Intent(SplashActivity.this, TutorPage.class);
                        break;
                    case "Swimmer":
                        Log.d(TAG, "Redirecting swimmer to SwimmerPage");
                        intent = new Intent(SplashActivity.this, SwimmerPage.class);
                        break;
                    default:
                        Log.w(TAG, "Unknown role: " + role + ", redirecting to MainActivity");
                        intent = new Intent(SplashActivity.this, MainActivity.class);
                        break;
                }
            }
            
            if (intent != null) {
                // Clear activity stack and start the appropriate activity
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
            
        } else {
            // User not logged in - go to MainActivity
            Log.d(TAG, "User not logged in, redirecting to MainActivity");
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
    }
}
