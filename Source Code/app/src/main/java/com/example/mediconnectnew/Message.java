package com.example.mediconnectnew;

public class Message {
    private int id;
    private String conversationId;
    private String senderEmail;
    private String receiverEmail;
    private String messageText;
    private String timestamp;
    private boolean isRead;
    private String messageType; // "text", "image", "file"
    private String senderName;
    private boolean isSentByCurrentUser;

    public Message() {}

    public Message(String conversationId, String senderEmail, String receiverEmail,
                   String messageText, String timestamp) {
        this.conversationId = conversationId;
        this.senderEmail = senderEmail;
        this.receiverEmail = receiverEmail;
        this.messageText = messageText;
        this.timestamp = timestamp;
        this.isRead = false;
        this.messageType = "text";
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getConversationId() { return conversationId; }
    public void setConversationId(String conversationId) { this.conversationId = conversationId; }

    public String getSenderEmail() { return senderEmail; }
    public void setSenderEmail(String senderEmail) { this.senderEmail = senderEmail; }

    public String getReceiverEmail() { return receiverEmail; }
    public void setReceiverEmail(String receiverEmail) { this.receiverEmail = receiverEmail; }

    public String getMessageText() { return messageText; }
    public void setMessageText(String messageText) { this.messageText = messageText; }

    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }

    public boolean isRead() { return isRead; }
    public void setRead(boolean read) { isRead = read; }

    public String getMessageType() { return messageType; }
    public void setMessageType(String messageType) { this.messageType = messageType; }

    public String getSenderName() { return senderName; }
    public void setSenderName(String senderName) { this.senderName = senderName; }

    public boolean isSentByCurrentUser() { return isSentByCurrentUser; }
    public void setSentByCurrentUser(boolean sentByCurrentUser) { isSentByCurrentUser = sentByCurrentUser; }
}

