package com.example.mediconnectnew;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText etEmail, etPassword;
    private Button btnLogin;
    private TextView tvRegister;
    private DatabaseHelper dbHelper;
    private DatabaseReference firebaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initViews();
        setupClickListeners();
        dbHelper = new DatabaseHelper(this);
        firebaseRef = FirebaseDatabase.getInstance("https://mediconnect-ed634-default-rtdb.firebaseio.com/")
                .getReference("users");
    }

    private void initViews() {
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegister = findViewById(R.id.tvRegister);
    }

    private void setupClickListeners() {
        btnLogin.setOnClickListener(v -> performLogin());
        tvRegister.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            finish();
        });
    }

    private void performLogin() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check from local SQLite DB first
        User user = dbHelper.loginUser(email, password);
        if (user != null) {
            saveLogin(user);
            return;
        }

        // Firebase uses encoded email as key
        String encodedEmail = encodeEmail(etEmail.getText().toString().trim());

        firebaseRef.child(encodedEmail)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            String firebasePassword = snapshot.child("password").getValue(String.class);
                            if (firebasePassword != null && firebasePassword.equals(password)) {
                                String name = snapshot.child("name").getValue(String.class);
                                String phone = snapshot.child("phone").getValue(String.class);
                                Boolean isDoctor = snapshot.child("isDoctor").getValue(Boolean.class);
                                String specialization = snapshot.child("specialization").getValue(String.class);

                                User firebaseUser = new User(
                                        name,
                                        email, // keep original email
                                        phone,
                                        password,
                                        isDoctor != null && isDoctor,
                                        specialization
                                );

                                dbHelper.registerUser(firebaseUser);
                                saveLogin(firebaseUser);
                            } else {
                                Toast.makeText(LoginActivity.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(LoginActivity.this, "User not found", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(LoginActivity.this, "Firebase error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void saveLogin(User user) {
        SharedPreferences prefs = getSharedPreferences("MediConnect", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("isLoggedIn", true);
        editor.putString("userEmail", user.getEmail());
        editor.putString("userName", user.getName());
        editor.putBoolean("isDoctor", user.isDoctor());
        editor.apply();

        Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
        finish();
    }

    // Replace dots with underscores for Firebase key
    private String encodeEmail(String email) {
        return email.replace(".", ",");
    }
}
