package com.example.mediconnectnew;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {
    private static final String TAG = "ProfileActivity";

    private ImageButton btnBack, btnEdit;
    private Button btnChangePhoto;
    private TextView tvUserName, tvUserType, tvEmail, tvPhone, tvSpecialization;
    private LinearLayout layoutSpecialization, layoutHealthRecords, layoutMessageHistory,
            layoutNotifications, layoutLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            setContentView(R.layout.activity_profile);
            initViews();
            loadUserData();
            setupClickListeners();
        } catch (Exception e) {
            Log.e(TAG, "Error in onCreate: " + e.getMessage());
            Toast.makeText(this, "Error loading profile", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void initViews() {
        try {
            btnBack = findViewById(R.id.btnBack);
            btnEdit = findViewById(R.id.btnEdit);
            btnChangePhoto = findViewById(R.id.btnChangePhoto);
            CircleImageView ivProfilePicture = findViewById(R.id.ivProfilePicture);
            tvUserName = findViewById(R.id.tvUserName);
            tvUserType = findViewById(R.id.tvUserType);
            tvEmail = findViewById(R.id.tvEmail);
            tvPhone = findViewById(R.id.tvPhone);
            tvSpecialization = findViewById(R.id.tvSpecialization);
            layoutSpecialization = findViewById(R.id.layoutSpecialization);
            layoutHealthRecords = findViewById(R.id.layoutHealthRecords);
//            layoutMessageHistory = findViewById(R.id.layoutMessageHistory);
//            layoutNotifications = findViewById(R.id.layoutNotifications);
            layoutLogout = findViewById(R.id.layoutLogout);

            // Check if any critical views are null
            if (btnBack == null || tvUserName == null || tvEmail == null) {
                throw new RuntimeException("Critical views not found in layout");
            }
        } catch (Exception e) {
            Log.e(TAG, "Error initializing views: " + e.getMessage());
            throw e;
        }
    }

    private void loadUserData() {
        try {
            SharedPreferences prefs = getSharedPreferences("MediConnect", MODE_PRIVATE);
            String userName = prefs.getString("userName", "User");
            String userEmail = prefs.getString("userEmail", "");
            String userPhone = prefs.getString("userPhone", "");
            boolean isDoctor = prefs.getBoolean("isDoctor", false);
            String specialization = prefs.getString("specialization", "");

            // Set user data with null checks
            if (tvUserName != null) {
                tvUserName.setText(userName != null ? userName : "User");
            }

            if (tvUserType != null) {
                tvUserType.setText(isDoctor ? "Healthcare Provider" : "Patient");
            }

            if (tvEmail != null) {
                tvEmail.setText(userEmail != null ? userEmail : "");
            }

            if (tvPhone != null) {
                String displayPhone = (userPhone != null && !userPhone.isEmpty()) ? userPhone : "Not provided";
                tvPhone.setText(displayPhone);
            }


            // Show specialization for doctors
            if (isDoctor && layoutSpecialization != null) {
                layoutSpecialization.setVisibility(View.VISIBLE);
                if (tvSpecialization != null) {
                    tvSpecialization.setText(specialization != null && !specialization.isEmpty() ?
                            specialization : "General Medicine");
                }
            } else if (layoutSpecialization != null) {
                layoutSpecialization.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error loading user data: " + e.getMessage());
            Toast.makeText(this, "Error loading user data", Toast.LENGTH_SHORT).show();
        }
    }

    private void setupClickListeners() {
        try {
            if (btnBack != null) {
                btnBack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
            }

            if (btnEdit != null) {
                btnEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(ProfileActivity.this, "Edit profile feature coming soon", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            if (btnChangePhoto != null) {
                btnChangePhoto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(ProfileActivity.this, "Change photo feature coming soon", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            if (layoutHealthRecords != null) {
                layoutHealthRecords.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showAboutAppDialog();
                    }
                });
            }


//            if (layoutMessageHistory != null) {
//                layoutMessageHistory.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        try {
//                            startActivity(new Intent(ProfileActivity.this, MessageHistoryActivity.class));
//                        } catch (Exception e) {
//                            Log.e(TAG, "Error starting MessageHistoryActivity: " + e.getMessage());
//                            Toast.makeText(ProfileActivity.this, "Feature not available", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//            }

//            if (layoutNotifications != null) {
//                layoutNotifications.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        try {
//                            startActivity(new Intent(ProfileActivity.this, NotificationsActivity.class));
//                        } catch (Exception e) {
//                            Log.e(TAG, "Error starting NotificationsActivity: " + e.getMessage());
//                            Toast.makeText(ProfileActivity.this, "Feature not available", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//            }

            if (layoutLogout != null) {
                layoutLogout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showLogoutDialog();
                    }
                });
            }
        } catch (Exception e) {
            Log.e(TAG, "Error setting up click listeners: " + e.getMessage());
        }
    }

    private void showLogoutDialog() {
        try {
            new AlertDialog.Builder(this)
                    .setTitle("Logout")
                    .setMessage("Are you sure you want to logout?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        logout();
                    })
                    .setNegativeButton("No", null)
                    .show();
        } catch (Exception e) {
            Log.e(TAG, "Error showing logout dialog: " + e.getMessage());
            logout(); // Fallback to direct logout
        }
    }

    private void logout() {
        try {
            SharedPreferences prefs = getSharedPreferences("MediConnect", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.clear();
            editor.apply();

            Intent intent = new Intent(ProfileActivity.this, WelcomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        } catch (Exception e) {
            Log.e(TAG, "Error during logout: " + e.getMessage());
            Toast.makeText(this, "Error during logout", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh user data when returning to profile
        loadUserData();
    }

    private void showAboutAppDialog() {
        String message = "MediConnect is a healthcare app designed to bridge the gap between rural communities and healthcare professionals. "
                + "\n\nKey features include:\n"
                + "- Doctor consultations\n"
                + "- Medication reminders\n"
                + "- Health record tracking\n"
                + "- AI health assistant\n\n"
                + "Version: 1.0.0\nDeveloped by: UOR-DIT";

        new androidx.appcompat.app.AlertDialog.Builder(ProfileActivity.this)
                .setTitle("About MediConnect")
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show();
    }

}
