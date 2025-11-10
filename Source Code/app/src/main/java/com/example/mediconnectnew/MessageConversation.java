package com.example.mediconnectnew;

public class MessageConversation {
    private int id;
    private String userEmail;
    private String contactName;
    private String contactType; // "doctor", "ai"
    private String lastMessage;
    private String lastMessageTime;
    private int unreadCount;
    private String contactSpecialization;

    public MessageConversation() {}

    public MessageConversation(String userEmail, String contactName, String contactType,
                               String lastMessage, String lastMessageTime) {
        this.userEmail = userEmail;
        this.contactName = contactName;
        this.contactType = contactType;
        this.lastMessage = lastMessage;
        this.lastMessageTime = lastMessageTime;
        this.unreadCount = 0;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }

    public String getContactName() { return contactName; }
    public void setContactName(String contactName) { this.contactName = contactName; }

    public String getContactType() { return contactType; }
    public void setContactType(String contactType) { this.contactType = contactType; }

    public String getLastMessage() { return lastMessage; }
    public void setLastMessage(String lastMessage) { this.lastMessage = lastMessage; }

    public String getLastMessageTime() { return lastMessageTime; }
    public void setLastMessageTime(String lastMessageTime) { this.lastMessageTime = lastMessageTime; }

    public int getUnreadCount() { return unreadCount; }
    public void setUnreadCount(int unreadCount) { this.unreadCount = unreadCount; }

    public String getContactSpecialization() { return contactSpecialization; }
    public void setContactSpecialization(String contactSpecialization) { this.contactSpecialization = contactSpecialization; }
}
