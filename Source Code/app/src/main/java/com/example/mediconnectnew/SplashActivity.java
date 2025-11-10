package com.example.mediconnectnew;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen; // Jetpack SplashScreen API (API 31+)

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_DURATION = 2000; // Optional delay (2 seconds)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Install the system splash screen (Android 12+)
        SplashScreen.installSplashScreen(this);
        super.onCreate(savedInstanceState);

        // Optional: Load your splash layout (e.g., logo)
        setContentView(R.layout.activity_splash);

        // Delay and check login
        new Handler(Looper.getMainLooper()).postDelayed(this::checkLoginStatus, SPLASH_DURATION);
    }

    private void checkLoginStatus() {
        SharedPreferences prefs = getSharedPreferences("MediConnect", MODE_PRIVATE);
        boolean isLoggedIn = prefs.getBoolean("isLoggedIn", false);

        Class<?> nextActivity = isLoggedIn ? DashboardActivity.class : WelcomeActivity.class;
        startActivity(new Intent(SplashActivity.this, nextActivity));
        finish();
    }
}
