package com.ofekinyo.myswimmingapp.screens;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.ofekinyo.myswimmingapp.R;

public class About extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        // Set app information
        TextView appInfo = findViewById(R.id.tvAppInfo);
        appInfo.setText("Welcome to SwimLink! This app connects trainers and trainees for a seamless swimming experience. You can track schedules, goals, and progress! With a user-friendly interface and advanced features, it’s perfect for all levels of swimming enthusiasts.");

        // Set more information about the app
        TextView moreInfo = findViewById(R.id.tvMoreInfo);
        moreInfo.setText("Features:\n- Link trainers and trainees\n- Track progress and goals\n- Manage training schedules\n- Receive personalized swimming tips\n\nBenefits:\n- Enhances learning experience\n- Connects users with experts\n- Promotes healthy living");

        // Set contact email
        TextView contactEmail = findViewById(R.id.tvContactEmail);
        contactEmail.setText("For inquiries, contact us at: contact@swimlink.com");

        // Set up email button functionality
        Button emailButton = findViewById(R.id.btnContactEmail);
        emailButton.setOnClickListener(v -> {
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
            emailIntent.setData(Uri.parse("mailto:contact@swimlink.com"));
            startActivity(Intent.createChooser(emailIntent, "Send Email"));
        });

        // Set up social media buttons
        Button facebookButton = findViewById(R.id.btnFacebook);
        facebookButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/SwimLink"));
            startActivity(intent);
        });

        Button twitterButton = findViewById(R.id.btnTwitter);
        twitterButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/SwimLink"));
            startActivity(intent);
        });
    }
}
