package com.ofekinyo.myswimmingapp.base;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.ofekinyo.myswimmingapp.R;
import com.ofekinyo.myswimmingapp.screens.About;
import com.ofekinyo.myswimmingapp.screens.Account;
import com.ofekinyo.myswimmingapp.screens.Login;

public abstract class BaseActivity extends AppCompatActivity {

    protected void setupToolbar(String title) {
        Toolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setDisplayShowHomeEnabled(false);
            TextView titleTextView = toolbar.findViewById(R.id.toolbar_title);
            titleTextView.setText(title);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        
        if (itemId == R.id.action_account) {
            startActivity(new Intent(this, Account.class));
            return true;
        } else if (itemId == R.id.action_about) {
            startActivity(new Intent(this, About.class));
            return true;
        } else if (itemId == R.id.action_logout) {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(this, Login.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            return true;
        } else if (itemId == R.id.action_back) {
            onBackPressed();
            return true;
        }
        
        return super.onOptionsItemSelected(item);
    }
} 