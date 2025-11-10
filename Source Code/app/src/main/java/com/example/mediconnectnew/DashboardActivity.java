package com.example.mediconnectnew;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class DashboardActivity extends AppCompatActivity {
    private TextView tvWelcome;
    private CardView cardAI, cardSymptom, cardDoctor, cardHospital, cardReminder, cardRecords;
    private ImageView ivProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        initViews();
        setupWelcomeMessage();
        setupClickListeners();
    }

    private void initViews() {
        tvWelcome = findViewById(R.id.tvWelcome);
        cardAI = findViewById(R.id.cardAI);
        cardSymptom = findViewById(R.id.cardSymptom);
        cardDoctor = findViewById(R.id.cardDoctor);
        cardHospital = findViewById(R.id.cardHospital);
        cardReminder = findViewById(R.id.cardReminder);
        cardRecords = findViewById(R.id.cardRecords);

        // Find profile icon if it exists
        ivProfile = findViewById(R.id.ivProfile);
    }

    private void setupWelcomeMessage() {
        SharedPreferences prefs = getSharedPreferences("MediConnect", MODE_PRIVATE);
        String userName = prefs.getString("userName", "User");
        tvWelcome.setText(getString(R.string.hello_user, userName));
    }

    private void setupClickListeners() {
        cardAI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashboardActivity.this, AIChatActivity.class));
            }
        });

        cardSymptom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashboardActivity.this, SymptomCheckerActivity.class));
            }
        });

        cardDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashboardActivity.this, ConsultDoctorActivity.class));
            }
        });

        cardHospital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashboardActivity.this, HospitalActivity.class));
            }
        });

        cardReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashboardActivity.this, ReminderActivity.class));
            }
        });

        cardRecords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashboardActivity.this, HealthRecordsActivity.class));
            }
        });

        // Add profile navigation
        if (ivProfile != null) {
            ivProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(DashboardActivity.this, ProfileActivity.class));
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        // Show exit confirmation dialog instead of immediately going back
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("Exit App");
        builder.setMessage("Are you sure you want to exit?");
        builder.setPositiveButton("Yes", (dialog, which) -> {
            finishAffinity(); // Close all activities
        });
        builder.setNegativeButton("No", null);
        builder.show();
    }
}
