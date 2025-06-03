package com.ofekinyo.myswimmingapp.screens;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.ofekinyo.myswimmingapp.R;

public class AdminPasskeyActivity extends AppCompatActivity {

    private static final String ADMIN_PASSKEY = "NewAdminCreate!";
    private EditText etPasskey;
    private Button btnVerify, btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_passkey);

        etPasskey = findViewById(R.id.etPasskey);
        btnVerify = findViewById(R.id.btnVerify);
        btnBack = findViewById(R.id.btnBack);

        btnVerify.setOnClickListener(v -> verifyPasskey());
        btnBack.setOnClickListener(v -> finish());
    }

    private void verifyPasskey() {
        String enteredPasskey = etPasskey.getText().toString().trim();
        
        if (enteredPasskey.equals(ADMIN_PASSKEY)) {
            // Passkey is correct, proceed to registration
            Intent intent = new Intent(this, Register.class);
            intent.putExtra("isAdmin", true);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "קוד לא נכון", Toast.LENGTH_SHORT).show();
            etPasskey.setText("");
        }
    }
} 