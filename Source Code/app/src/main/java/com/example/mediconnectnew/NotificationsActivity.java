package com.example.mediconnectnew;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class NotificationsActivity extends AppCompatActivity {
    private ImageButton btnBack, btnClearAll;
    private TextView tabAll, tabReminders, tabMessages;
    private RecyclerView recyclerViewNotifications;
    private LinearLayout layoutEmptyState;
    private NotificationAdapter notificationAdapter;
    private List<NotificationItem> notifications;
    private DatabaseHelper dbHelper;
    private String currentTab = "all";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        initViews();
        setupRecyclerView();
        setupClickListeners();
        loadNotifications();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        btnClearAll = findViewById(R.id.btnClearAll);
        tabAll = findViewById(R.id.tabAll);
        tabReminders = findViewById(R.id.tabReminders);
        tabMessages = findViewById(R.id.tabMessages);
        recyclerViewNotifications = findViewById(R.id.recyclerViewNotifications);
        layoutEmptyState = findViewById(R.id.layoutEmptyState);
        dbHelper = new DatabaseHelper(this);
    }

    private void setupRecyclerView() {
        notifications = new ArrayList<>();
        recyclerViewNotifications.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setupClickListeners() {
        btnBack.setOnClickListener(v -> finish());

        btnClearAll.setOnClickListener(v -> clearAllNotifications());

        tabAll.setOnClickListener(v -> switchTab("all"));
        tabReminders.setOnClickListener(v -> switchTab("reminders"));
        tabMessages.setOnClickListener(v -> switchTab("messages"));
    }

    private void switchTab(String tab) {
        currentTab = tab;
        
        // Reset tab styles
        resetTabStyles();
        
        // Set active tab style
        switch (tab) {
            case "all":
                tabAll.setTextColor(getColor(R.color.primary_orange));
                tabAll.setBackgroundColor(getColor(R.color.light_orange));
                break;
            case "reminders":
                tabReminders.setTextColor(getColor(R.color.primary_orange));
                tabReminders.setBackgroundColor(getColor(R.color.light_orange));
                break;
            case "messages":
                tabMessages.setTextColor(getColor(R.color.primary_orange));
                tabMessages.setBackgroundColor(getColor(R.color.light_orange));
                break;
        }
        
        loadNotifications();
    }

    private void resetTabStyles() {
        tabAll.setTextColor(getColor(R.color.text_secondary));
        tabAll.setBackgroundColor(getColor(R.color.white));
        tabReminders.setTextColor(getColor(R.color.text_secondary));
        tabReminders.setBackgroundColor(getColor(R.color.white));
        tabMessages.setTextColor(getColor(R.color.text_secondary));
        tabMessages.setBackgroundColor(getColor(R.color.white));
    }

    private void loadNotifications() {
        SharedPreferences prefs = getSharedPreferences("MediConnect", MODE_PRIVATE);
        String userEmail = prefs.getString("userEmail", "");
        
        notifications = dbHelper.getUserNotifications(userEmail, currentTab);
        
        if (notifications.isEmpty()) {
            layoutEmptyState.setVisibility(View.VISIBLE);
            recyclerViewNotifications.setVisibility(View.GONE);
        } else {
            layoutEmptyState.setVisibility(View.GONE);
            recyclerViewNotifications.setVisibility(View.VISIBLE);
            
            notificationAdapter = new NotificationAdapter(notifications, new NotificationAdapter.OnNotificationClickListener() {
                @Override
                public void onNotificationClick(NotificationItem notification) {
                    markAsRead(notification);
                }
            });
            recyclerViewNotifications.setAdapter(notificationAdapter);
        }
    }

    private void markAsRead(NotificationItem notification) {
        dbHelper.markNotificationAsRead(notification.getId());
        loadNotifications();
    }

    private void clearAllNotifications() {
        SharedPreferences prefs = getSharedPreferences("MediConnect", MODE_PRIVATE);
        String userEmail = prefs.getString("userEmail", "");
        
        dbHelper.clearUserNotifications(userEmail);
        loadNotifications();
    }
}
