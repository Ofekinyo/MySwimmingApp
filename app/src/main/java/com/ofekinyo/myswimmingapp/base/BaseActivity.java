package com.ofekinyo.myswimmingapp.base;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.ofekinyo.myswimmingapp.R;
import com.ofekinyo.myswimmingapp.screens.About;
import com.ofekinyo.myswimmingapp.screens.Account;
import com.ofekinyo.myswimmingapp.screens.Login;
import com.ofekinyo.myswimmingapp.services.AuthenticationService;
import com.ofekinyo.myswimmingapp.services.DatabaseService;
import com.ofekinyo.myswimmingapp.utils.SharedPreferencesUtil;

public abstract class BaseActivity extends AppCompatActivity {

    protected AuthenticationService authenticationService;
    protected DatabaseService databaseService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        authenticationService = AuthenticationService.getInstance();
        databaseService = DatabaseService.getInstance();
    }

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
            authenticationService.signOut();
            SharedPreferencesUtil.signOutUser(this);
            Intent intent = new Intent(this, Login.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            return true;
        } else if (itemId == R.id.action_back) {
            return true;
        }
        
        return super.onOptionsItemSelected(item);
    }
} 