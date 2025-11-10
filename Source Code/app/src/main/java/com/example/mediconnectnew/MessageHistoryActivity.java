package com.example.mediconnectnew;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MessageHistoryActivity extends AppCompatActivity {
    private static final String TAG = "MessageHistoryActivity";
    private ImageButton btnBack, btnSearch, btnCloseSearch;
    private LinearLayout layoutSearch, layoutEmptyMessages;
    private EditText etSearch;
    private TextView tabDoctors, tabAI;
    private RecyclerView recyclerViewMessages;
    private MessageConversationAdapter messageAdapter;
    private List<MessageConversation> conversations;
    private DatabaseHelper dbHelper;
    private String currentTab = "doctors";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_history);

        initViews();
        setupRecyclerView();
        setupClickListeners();
        loadConversations();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        btnSearch = findViewById(R.id.btnSearch);
        btnCloseSearch = findViewById(R.id.btnCloseSearch);
        layoutSearch = findViewById(R.id.layoutSearch);
        layoutEmptyMessages = findViewById(R.id.layoutEmptyMessages);
        etSearch = findViewById(R.id.etSearch);
        tabDoctors = findViewById(R.id.tabDoctors);
        tabAI = findViewById(R.id.tabAI);
        recyclerViewMessages = findViewById(R.id.recyclerViewMessages);
        dbHelper = new DatabaseHelper(this);
    }

    private void setupRecyclerView() {
        conversations = new ArrayList<>();
        recyclerViewMessages.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setupClickListeners() {
        btnBack.setOnClickListener(v -> finish());

        btnSearch.setOnClickListener(v -> {
            layoutSearch.setVisibility(View.VISIBLE);
            etSearch.requestFocus();
        });

        btnCloseSearch.setOnClickListener(v -> {
            layoutSearch.setVisibility(View.GONE);
            etSearch.setText("");
            loadConversations();
        });

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchConversations(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        tabDoctors.setOnClickListener(v -> switchTab("doctors"));
        tabAI.setOnClickListener(v -> switchTab("ai"));
    }

    private void switchTab(String tab) {
        currentTab = tab;

        // Reset tab styles
        resetTabStyles();

        // Set active tab style
        if (tab.equals("doctors")) {
            tabDoctors.setTextColor(getColor(R.color.primary_green));
            tabDoctors.setBackgroundColor(getColor(R.color.light_green));
        } else {
            tabAI.setTextColor(getColor(R.color.primary_green));
            tabAI.setBackgroundColor(getColor(R.color.light_green));
        }

        loadConversations();
    }

    private void resetTabStyles() {
        tabDoctors.setTextColor(getColor(R.color.text_secondary));
        tabDoctors.setBackgroundColor(getColor(R.color.white));
        tabAI.setTextColor(getColor(R.color.text_secondary));
        tabAI.setBackgroundColor(getColor(R.color.white));
    }

    private List<ChatMessage> loadAIChatMessagesFromFile() {
        List<ChatMessage> chatMessages = new ArrayList<>();
        Gson gson = new Gson();

        try (FileInputStream fis = openFileInput("chat_history.json");
             InputStreamReader isr = new InputStreamReader(fis);
             BufferedReader reader = new BufferedReader(isr)) {

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            String json = sb.toString();
            Type type = new TypeToken<List<ChatMessage>>(){}.getType();
            chatMessages = gson.fromJson(json, type);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return chatMessages;
    }

    private void loadConversations() {
        try {
            SharedPreferences prefs = getSharedPreferences("MediConnect", MODE_PRIVATE);
            String userEmail = prefs.getString("userEmail", "");

            if ("ai".equals(currentTab)) {
                List<ChatMessage> chatMessages = loadAIChatMessagesFromFile();

                conversations = new ArrayList<>();

                if (chatMessages.isEmpty()) {
                    layoutEmptyMessages.setVisibility(View.VISIBLE);
                    recyclerViewMessages.setVisibility(View.GONE);
                } else {
                    layoutEmptyMessages.setVisibility(View.GONE);
                    recyclerViewMessages.setVisibility(View.VISIBLE);

                    ChatMessage lastMsg = chatMessages.get(chatMessages.size() - 1);
                    String lastMessage = lastMsg.getMessage();

                    String lastMessageTime = new SimpleDateFormat("hh:mm a").format(new Date());

                    MessageConversation aiConversation = new MessageConversation();
                    aiConversation.setUserEmail(userEmail);
                    aiConversation.setContactName("AI Health Assistant");
                    aiConversation.setContactType("ai");
                    aiConversation.setLastMessage(lastMessage);
                    aiConversation.setLastMessageTime(lastMessageTime);
                    aiConversation.setUnreadCount(0);

                    conversations.add(aiConversation);

                    messageAdapter = new MessageConversationAdapter(conversations, conversation -> {
                        openConversation(conversation);
                    });
                    recyclerViewMessages.setAdapter(messageAdapter);
                }
            } else {
                conversations = dbHelper.getUserConversations(userEmail, currentTab);

                if (conversations.isEmpty()) {
                    layoutEmptyMessages.setVisibility(View.VISIBLE);
                    recyclerViewMessages.setVisibility(View.GONE);
                } else {
                    layoutEmptyMessages.setVisibility(View.GONE);
                    recyclerViewMessages.setVisibility(View.VISIBLE);

                    messageAdapter = new MessageConversationAdapter(conversations, conversation -> {
                        openConversation(conversation);
                    });
                    recyclerViewMessages.setAdapter(messageAdapter);
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Error loading conversations: " + e.getMessage());
            Toast.makeText(this, "Error loading conversations", Toast.LENGTH_SHORT).show();
        }
    }

    private void searchConversations(String query) {
        try {
            if (query.isEmpty()) {
                loadConversations();
                return;
            }

            SharedPreferences prefs = getSharedPreferences("MediConnect", MODE_PRIVATE);
            String userEmail = prefs.getString("userEmail", "");

            conversations = dbHelper.searchUserConversations(userEmail, currentTab, query);

            if (conversations.isEmpty()) {
                layoutEmptyMessages.setVisibility(View.VISIBLE);
                recyclerViewMessages.setVisibility(View.GONE);
            } else {
                layoutEmptyMessages.setVisibility(View.GONE);
                recyclerViewMessages.setVisibility(View.VISIBLE);

                messageAdapter = new MessageConversationAdapter(conversations, conversation -> {
                    openConversation(conversation);
                });
                recyclerViewMessages.setAdapter(messageAdapter);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error searching conversations: " + e.getMessage());
            Toast.makeText(this, "Error searching conversations", Toast.LENGTH_SHORT).show();
        }
    }

    private void openConversation(MessageConversation conversation) {
        if ("ai".equals(conversation.getContactType())) {
            startActivity(new Intent(this, AIChatActivity.class));
        } else {
            Toast.makeText(this, "Opening conversation with " + conversation.getContactName(), Toast.LENGTH_SHORT).show();
            // TODO: Open detailed conversation view for doctors, etc.
        }
    }
}
