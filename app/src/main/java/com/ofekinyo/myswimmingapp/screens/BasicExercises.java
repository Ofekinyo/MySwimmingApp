package com.ofekinyo.myswimmingapp.screens;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.ofekinyo.myswimmingapp.R;

public class BasicExercises extends AppCompatActivity {

    private WebView webView;
    private ImageView imageView;
    private TextView explanationText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.basic_exercises);

        webView = findViewById(R.id.webView);
        imageView = findViewById(R.id.imageView);
        explanationText = findViewById(R.id.explanationText);

        // Configure WebView to play YouTube video
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("https://www.youtube.com/embed/jKqQRC_D5aM");

        // Set image from URL
        imageView.setImageResource(R.drawable.swimmer); // Placeholder; use Glide or similar for actual URL

        // Set explanation text
        explanationText.setText("This video demonstrates the basics of freestyle swimming. Watch carefully to understand the proper techniques!");
    }
}
