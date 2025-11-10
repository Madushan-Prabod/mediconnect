package com.example.mediconnectnew;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ConversationActivity extends AppCompatActivity {
    private static final String TAG = "ConversationActivity";

    private ImageButton btnBack, btnSend;
    private TextView tvContactName, tvContactStatus;
    private EditText etMessage;
    private RecyclerView recyclerViewMessages;
    private ConversationAdapter messageAdapter;
    private List<Message> messages;
    private DatabaseHelper dbHelper;
    private Handler refreshHandler;
    private Runnable refreshRunnable;

    private String currentUserEmail;
    private String contactEmail;
    private String contactName;
    private String conversationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);

        getIntentData();
        initViews();
        setupRecyclerView();
        setupClickListeners();
        loadMessages();
        startMessageRefresh();
    }

    private void getIntentData() {
        contactEmail = getIntent().getStringExtra("contact_email");
        contactName = getIntent().getStringExtra("contact_name");

        SharedPreferences prefs = getSharedPreferences("MediConnect", MODE_PRIVATE);
        currentUserEmail = prefs.getString("userEmail", "");

        // Create conversation ID (consistent for both users)
        if (currentUserEmail.compareTo(contactEmail) < 0) {
            conversationId = currentUserEmail + "_" + contactEmail;
        } else {
            conversationId = contactEmail + "_" + currentUserEmail;
        }
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        btnSend = findViewById(R.id.btnSend);
        tvContactName = findViewById(R.id.tvContactName);
        tvContactStatus = findViewById(R.id.tvContactStatus);
        etMessage = findViewById(R.id.etMessage);
        recyclerViewMessages = findViewById(R.id.recyclerViewMessages);
        dbHelper = new DatabaseHelper(this);

        tvContactName.setText(contactName != null ? contactName : "Unknown Contact");
        tvContactStatus.setText("Online"); // You can implement real status checking
    }

    private void setupRecyclerView() {
        messages = new ArrayList<>();
        messageAdapter = new ConversationAdapter(messages, currentUserEmail);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true); // Start from bottom
        recyclerViewMessages.setLayoutManager(layoutManager);
        recyclerViewMessages.setAdapter(messageAdapter);
    }

    private void setupClickListeners() {
        btnBack.setOnClickListener(v -> finish());

        btnSend.setOnClickListener(v -> sendMessage());

        // Send message on enter key
        etMessage.setOnEditorActionListener((v, actionId, event) -> {
            sendMessage();
            return true;
        });
    }

    private void sendMessage() {
        String messageText = etMessage.getText().toString().trim();

        if (TextUtils.isEmpty(messageText)) {
            return;
        }

        try {
            String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                    .format(new Date());

            Message message = new Message(conversationId, currentUserEmail, contactEmail,
                    messageText, timestamp);

            long result = dbHelper.addMessage(message);

            if (result != -1) {
                etMessage.setText("");
                loadMessages();

                // Update conversation in message_conversations table
                updateConversation(messageText, timestamp);
            } else {
                Toast.makeText(this, "Failed to send message", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.e(TAG, "Error sending message: " + e.getMessage());
            Toast.makeText(this, "Error sending message", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateConversation(String lastMessage, String timestamp) {
        try {
            // Check if conversation exists
            MessageConversation existingConversation = dbHelper.getConversation(currentUserEmail, contactEmail);

            if (existingConversation == null) {
                // Create new conversation
                MessageConversation conversation = new MessageConversation();
                conversation.setUserEmail(currentUserEmail);
                conversation.setContactName(contactName);
                conversation.setContactType("doctor");
                conversation.setLastMessage(lastMessage);
                conversation.setLastMessageTime(timestamp);
                conversation.setUnreadCount(0);

                dbHelper.addConversation(conversation);

                // Also create conversation for the doctor
                MessageConversation doctorConversation = new MessageConversation();
                doctorConversation.setUserEmail(contactEmail);
                doctorConversation.setContactName(getCurrentUserName());
                doctorConversation.setContactType("patient");
                doctorConversation.setLastMessage(lastMessage);
                doctorConversation.setLastMessageTime(timestamp);
                doctorConversation.setUnreadCount(1);

                dbHelper.addConversation(doctorConversation);
            } else {
                // Update existing conversation
                dbHelper.updateConversation(currentUserEmail, contactEmail, lastMessage, timestamp);
                dbHelper.updateConversation(contactEmail, currentUserEmail, lastMessage, timestamp);
                dbHelper.incrementUnreadCount(contactEmail, currentUserEmail);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error updating conversation: " + e.getMessage());
        }
    }

    private String getCurrentUserName() {
        SharedPreferences prefs = getSharedPreferences("MediConnect", MODE_PRIVATE);
        return prefs.getString("userName", "User");
    }

    private void loadMessages() {
        try {
            messages.clear();
            List<Message> loadedMessages = dbHelper.getConversationMessages(conversationId);

            for (Message message : loadedMessages) {
                message.setSentByCurrentUser(message.getSenderEmail().equals(currentUserEmail));
                messages.add(message);
            }

            messageAdapter.notifyDataSetChanged();

            if (!messages.isEmpty()) {
                recyclerViewMessages.scrollToPosition(messages.size() - 1);
            }

            // Mark messages as read
            dbHelper.markMessagesAsRead(conversationId, currentUserEmail);

        } catch (Exception e) {
            Log.e(TAG, "Error loading messages: " + e.getMessage());
        }
    }

    private void startMessageRefresh() {
        refreshHandler = new Handler();
        refreshRunnable = new Runnable() {
            @Override
            public void run() {
                loadMessages();
                refreshHandler.postDelayed(this, 3000); // Refresh every 3 seconds
            }
        };
        refreshHandler.postDelayed(refreshRunnable, 3000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (refreshHandler != null && refreshRunnable != null) {
            refreshHandler.removeCallbacks(refreshRunnable);
        }
    }
}

