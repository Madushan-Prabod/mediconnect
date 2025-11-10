package com.example.mediconnectnew;

public class NotificationItem {
    private int id;
    private String userEmail;
    private String title;
    private String message;
    private String type; // "reminder", "message", "system"
    private String timestamp;
    private boolean isRead;

    public NotificationItem() {}

    public NotificationItem(String userEmail, String title, String message, String type, String timestamp) {
        this.userEmail = userEmail;
        this.title = title;
        this.message = message;
        this.type = type;
        this.timestamp = timestamp;
        this.isRead = false;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }

    public boolean isRead() { return isRead; }
    public void setRead(boolean read) { isRead = read; }
}
