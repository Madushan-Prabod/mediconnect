package com.example.mediconnectnew;

public class Reminder {
    private int id;
    private String userEmail;
    private String title;
    private String description;
    private String dateTime;
    private boolean isCompleted;

    public Reminder() {}

    public Reminder(String userEmail, String title, String description, String dateTime) {
        this.userEmail = userEmail;
        this.title = title;
        this.description = description;
        this.dateTime = dateTime;
        this.isCompleted = false;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getDateTime() { return dateTime; }
    public void setDateTime(String dateTime) { this.dateTime = dateTime; }

    public boolean isCompleted() { return isCompleted; }
    public void setCompleted(boolean completed) { isCompleted = completed; }
}
