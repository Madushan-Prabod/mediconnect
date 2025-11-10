package com.example.mediconnectnew;

public class HealthRecord {
    private int id;
    private String userEmail;
    private String title;
    private String description;
    private String filePath;
    private String dateCreated;
    private String fileType;

    public HealthRecord() {}

    public HealthRecord(String userEmail, String title, String description, String filePath, String dateCreated) {
        this.userEmail = userEmail;
        this.title = title;
        this.description = description;
        this.filePath = filePath;
        this.dateCreated = dateCreated;
        this.fileType = getFileTypeFromPath(filePath);
    }

    private String getFileTypeFromPath(String path) {
        if (path == null) return "File";
        String lowerPath = path.toLowerCase();
        if (lowerPath.contains(".pdf")) return "PDF";
        if (lowerPath.contains(".jpg") || lowerPath.contains(".jpeg") || lowerPath.contains(".png")) return "Image";
        if (lowerPath.contains(".doc") || lowerPath.contains(".docx")) return "Document";
        return "File";
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

    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }

    public String getDateCreated() { return dateCreated; }
    public void setDateCreated(String dateCreated) { this.dateCreated = dateCreated; }

    public String getFileType() { return fileType; }
    public void setFileType(String fileType) { this.fileType = fileType; }
}
