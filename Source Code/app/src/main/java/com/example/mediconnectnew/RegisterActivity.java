package com.example.mediconnectnew;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private TextInputEditText etName, etEmail, etPhone, etPassword, etSpecialization;
    private TextInputLayout tilSpecialization;
    private CheckBox cbDoctor;
    private Button btnRegister;
    private TextView tvLogin;
    private DatabaseHelper dbHelper;

    private DatabaseReference firebaseDbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initViews();
        setupClickListeners();

        dbHelper = new DatabaseHelper(this);

        firebaseDbRef = FirebaseDatabase.getInstance("https://mediconnect-ed634-default-rtdb.firebaseio.com/")
                .getReference("users");
    }

    private void initViews() {
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        etPassword = findViewById(R.id.etPassword);
        etSpecialization = findViewById(R.id.etSpecialization);
        tilSpecialization = findViewById(R.id.tilSpecialization);
        cbDoctor = findViewById(R.id.cbDoctor);
        btnRegister = findViewById(R.id.btnRegister);
        tvLogin = findViewById(R.id.tvLogin);
    }

    private void setupClickListeners() {
        cbDoctor.setOnCheckedChangeListener((buttonView, isChecked) ->
                tilSpecialization.setVisibility(isChecked ? View.VISIBLE : View.GONE)
        );

        btnRegister.setOnClickListener(v -> performRegistration());

        tvLogin.setOnClickListener(v -> {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            finish();
        });
    }

    private void performRegistration() {
        String name = etName.getText() != null ? etName.getText().toString().trim() : "";
        String email = etEmail.getText() != null ? etEmail.getText().toString().trim() : "";
        String phone = etPhone.getText() != null ? etPhone.getText().toString().trim() : "";
        String password = etPassword.getText() != null ? etPassword.getText().toString().trim() : "";
        String specialization = etSpecialization.getText() != null ? etSpecialization.getText().toString().trim() : "";
        boolean isDoctor = cbDoctor.isChecked();

        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (isDoctor && specialization.isEmpty()) {
            Toast.makeText(this, "Please enter your specialization", Toast.LENGTH_SHORT).show();
            return;
        }

        User user = new User(name, email, phone, password, isDoctor, specialization);

        long result = dbHelper.registerUser(user);

        // Sanitize Firebase key: replace "." in email to avoid key conflict
        String firebaseKey = email.replace(".", ",");

        firebaseDbRef.child(firebaseKey).setValue(user);

        if (result != -1) {
            Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show();

            if (isDoctor) {
                sendWhatsAppVerification(phone, name);
            }

            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            finish();
        } else {
            Toast.makeText(this, "Registration failed. Email might already exist.", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendWhatsAppVerification(String phone, String name) {
        String message = "Hello " + name + ", welcome to MediConnect! Your doctor account has been registered successfully. You can now start receiving patient consultations.";
        try {
            Uri uri = Uri.parse("https://wa.me/" + phone + "?text=" + Uri.encode(message));
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "WhatsApp not installed or error occurred", Toast.LENGTH_SHORT).show();
        }
    }
}
