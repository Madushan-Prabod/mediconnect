package com.example.mediconnectnew;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import androidx.core.text.HtmlCompat;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class AIChatActivity extends AppCompatActivity {

    private RecyclerView recyclerViewChat;
    private EditText etMessage;
    private Button btnSend;
    private ImageButton btnBack;
    private ChatAdapter chatAdapter;
    private List<ChatMessage> chatMessages;
    private AIService aiService;

    private static final String CHAT_HISTORY_FILE = "chat_history.json";
    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ai_chat);

        gson = new Gson();

        initViews();
        setupRecyclerView();
        setupClickListeners();

        loadChatHistoryFromFile();

        chatAdapter = new ChatAdapter(chatMessages);
        recyclerViewChat.setAdapter(chatAdapter);

        aiService = new AIService();
    }

    private void initViews() {
        recyclerViewChat = findViewById(R.id.recyclerViewChat);
        etMessage = findViewById(R.id.etMessage);
        btnSend = findViewById(R.id.btnSend);
        btnBack = findViewById(R.id.btnBack);
    }

    private void setupRecyclerView() {
        recyclerViewChat.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setupClickListeners() {
        btnBack.setOnClickListener(v -> finish());

        btnSend.setOnClickListener(v -> sendMessage());
    }

    private void loadChatHistoryFromFile() {
        try (FileInputStream fis = openFileInput(CHAT_HISTORY_FILE);
             InputStreamReader isr = new InputStreamReader(fis);
             BufferedReader reader = new BufferedReader(isr)) {

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            String json = sb.toString();
            Type type = new TypeToken<List<ChatMessage>>() {}.getType();
            chatMessages = gson.fromJson(json, type);

        } catch (Exception e) {
            // File might not exist at first launch, so initialize with welcome message
            chatMessages = new ArrayList<>();
            chatMessages.add(new ChatMessage(
                    "👋 **Welcome to MediConnect AI Health Assistant**\n\n" +
                            "🔒 *Disclaimer:* I am an AI assistant, not a licensed medical professional. I cannot diagnose, treat, or prescribe. For urgent or serious issues, please consult a qualified healthcare provider.\n\n" +
                            "📌 **Here’s what I can help you with:**\n" +
                            "• 🤒 General symptoms (e.g., fever, cold, cough)\n" +
                            "• 🍽️ Healthy lifestyle and nutrition tips\n" +
                            "• 🧠 Understanding common health conditions\n" +
                            "• 💊 General information about non-prescription medicines\n\n" +
                            "🚫 **Please avoid asking about:**\n" +
                            "• Emergency assistance or crisis situations\n" +
                            "• Prescription drugs or treatments\n" +
                            "• Specific diagnoses or mental health therapy\n\n" +
                            "💬 *Ask your health-related question to get started!* 😊", false));
            saveChatHistoryToFile();
        }
    }

    private void saveChatHistoryToFile() {
        String json = gson.toJson(chatMessages);
        try (FileOutputStream fos = openFileOutput(CHAT_HISTORY_FILE, MODE_PRIVATE);
             OutputStreamWriter writer = new OutputStreamWriter(fos)) {
            writer.write(json);
            writer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendMessage() {
        String message = etMessage.getText().toString().trim();
        if (message.isEmpty()) return;

        if (!isHealthRelated(message)) {
            ChatMessage warning = new ChatMessage("⚠️ This assistant is only for general healthcare questions. Please ask health-related queries only.", false);
            chatMessages.add(warning);
            chatAdapter.notifyItemInserted(chatMessages.size() - 1);
            recyclerViewChat.scrollToPosition(chatMessages.size() - 1);
            etMessage.setText("");
            return;
        }

        // Add user message
        chatMessages.add(new ChatMessage((message + " in healthcare."), true));
        chatAdapter.notifyItemInserted(chatMessages.size() - 1);
        recyclerViewChat.scrollToPosition(chatMessages.size() - 1);
        saveChatHistoryToFile();

        etMessage.setText("");
        btnSend.setEnabled(false);

        // Show typing message
        ChatMessage typing = new ChatMessage("Typing...", false);
        chatMessages.add(typing);
        int typingIndex = chatMessages.size() - 1;
        chatAdapter.notifyItemInserted(typingIndex);
        recyclerViewChat.scrollToPosition(typingIndex);

        aiService.getAIResponse(message, new AIService.AIResponseCallback() {
            @Override
            public void onResponse(String response) {
                runOnUiThread(() -> {
                    chatMessages.remove(typingIndex);
                    chatAdapter.notifyItemRemoved(typingIndex);

                    chatMessages.add(new ChatMessage(response, false));
                    chatAdapter.notifyItemInserted(chatMessages.size() - 1);
                    recyclerViewChat.scrollToPosition(chatMessages.size() - 1);
                    saveChatHistoryToFile();

                    btnSend.setEnabled(true);
                });
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    chatMessages.remove(typingIndex);
                    chatAdapter.notifyItemRemoved(typingIndex);

                    chatMessages.add(new ChatMessage("Sorry, something went wrong: " + error, false));
                    chatAdapter.notifyItemInserted(chatMessages.size() - 1);
                    recyclerViewChat.scrollToPosition(chatMessages.size() - 1);
                    saveChatHistoryToFile();

                    btnSend.setEnabled(true);
                    Toast.makeText(AIChatActivity.this, error, Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    private boolean isHealthRelated(String message) {
        String lower = message.toLowerCase();
        String[] keywords = {
                // 🩺 Symptoms & Conditions
                "symptom", "pain", "fever", "cold", "flu", "cough", "headache", "vomiting", "nausea",
                "chills", "fatigue", "dizziness", "sore throat", "breathing", "rash", "itching",
                "diarrhea", "constipation", "swelling", "bleeding", "numbness", "cramps", "infection",
                "inflammation", "burning", "fracture", "injury", "wound", "ulcer", "stiffness",
                "high temperature", "low temperature", "irritation", "body ache", "stomach pain", "back pain",

                // 🩺 Sinhala
                "ලක්ෂණ", "වලිල්ල", "ජwaraය", "සීතල", "සෙම්ප්‍රතිශ්‍යාව", "උගුරා වේදනා", "වමනය",
                "හිස්වැදි", "වියළීම", "බරපතළ", "දියවැඩියාව", "කැස්ස", "ඇඟිලි වේදනාව", "අසනීපය", "ඇඟේ වේදනාව",

                // 🩺 Tamil
                "அறிகுறி", "வலி", "காய்ச்சல்", "சளி", "இருமல்", "தலைவலி", "வாந்தி", "மயக்கம்", "மனச்சோர்வு",
                "நீக்கம்", "நச்சு", "நோய்கள்", "அரிப்பு", "வீக்கம்", "நோய்த்தொற்று", "இரத்தம்", "மலச்சிக்கல்",

                // 🧠 Mental Health
                "anxiety", "depression", "stress", "panic", "mood swings", "bipolar", "trauma", "ptsd",
                "adhd", "mental health", "psychological", "emotional", "insomnia", "sleep disorder",
                "addiction", "loneliness", "counseling", "therapy", "grief",

                // 🧠 Sinhala
                "භීතිය", "මානසික ආතතිය", "පීඩනය", "සංකටය", "මානසික සෞඛ්‍යය", "නිදි නැති වීම", "සෙරෙප්‍රසියාව",
                "නරක සිතිවිලි", "අවපැහැදුණු බව", "සෙරෙප්‍රසියාව", "ඇහැර සිටීමේ ගැටලු", "බැඳීමේ ගැටලු",

                // 🧠 Tamil
                "பீதி", "மனச்சோர்வு", "மனஅழுத்தம்", "அதிர்ச்சி", "பைபோலார்", "மனநலம்", "துயில் கோளாறு",
                "உணர்ச்சி", "தீவிர நோய்", "உதவி", "ஆற்றல் இழப்பு", "துக்கம்", "போதைப்பொருள் பழக்கம்",

                // 🧪 Tests & Services
                "doctor", "hospital", "clinic", "checkup", "appointment", "scan", "x-ray", "blood test",
                "urine test", "ultrasound", "ecg", "mri", "surgery", "admission", "diagnosis", "prescription",

                // 🧪 Sinhala
                "ඩොක්ටර්", "රෝහල", "සෙරෙප්‍රසියාව", "පරීක්ෂාව", "ඇඳුම්", "එක්ස්-රේ", "උල්ට්‍රා සවුන්ඩ්",
                "ලෙඩ දින සටහන්", "ප්‍රතිකාර", "ලේ පරීක්ෂාව", "මූත්‍ර පරීක්ෂාව", "ශල්‍යකර්මය",

                // 🧪 Tamil
                "மருத்துவர்", "மருத்துவமனை", "மருத்துவ பரிசோதனை", "அப்பாய்ண்ட்மெண்ட்", "எக்ஸ்ரே",
                "இரத்த பரிசோதனை", "சிகிச்சை", "மூத்திர பரிசோதனை", "அல்ட்ராசவுண்ட்", "மிகுதியான சோதனை",

                // 💊 Medication & Treatment
                "medicine", "tablet", "pill", "capsule", "syrup", "injection", "prescription", "dose",
                "painkiller", "antibiotic", "treatment", "ointment", "vaccine", "immunization",

                // 💊 Sinhala
                "ඖෂධය", "ගිලන්කෑම", "ටැබ්ලට්", "කැප්සියුලය", "ටිකක්", "ඖෂධ මාරු කිරීම", "ප්‍රතිකාර", "ටිකා",

                // 💊 Tamil
                "மருந்து", "மருந்துக்கொள்கை", "முழு மருந்து", "காப்சூல்", "இஞ்செக்‌ஷன்", "மருந்தளவு",
                "சிகிச்சை", "மருந்து பரிந்துரை", "தடுப்பூசி", "தடுப்பூசி சிகிச்சை",

                // 🧍 Lifestyle & Personal
                "health", "wellness", "fitness", "diet", "nutrition", "exercise", "hydration", "yoga",
                "heart rate", "bmi", "weight", "sleep", "pulse", "temperature",

                // 🧍 Sinhala
                "සෞඛ්‍යය", "පෝෂණය", "මෝදනය", "ව්‍යායාමය", "අතුරු ආහාර", "ඔබගේ බර", "නිදා ගැනීම", "ශරීර බර", "උණ",

                // 🧍 Tamil
                "ஆரோக்கியம்", "உணவு", "ஆராய்ச்சி", "உடற்பயிற்சி", "பராமரிப்பு", "உடல் எடை", "தூக்கம்", "நிறை",

                // 🗣️ Pronouns & Personal
                "i", "me", "my", "mine", "myself",
                "you", "your", "yours", "yourself",
                "he", "him", "his", "himself",
                "she", "her", "hers", "herself",
                "they", "them", "their", "theirs", "themselves",
                "we", "us", "our", "ours", "ourselves",

                // 🗣️ Sinhala Pronouns
                "මම", "මගේ", "ඔබ", "ඔයා", "ඔයාගේ", "අපි", "අපගේ", "ඔවුන්", "ඇය", "ඔහු", "මට",

                // 🗣️ Tamil Pronouns
                "நான்", "என்னை", "என்", "எனது", "நீ", "உன்", "உனது", "அவன்", "அவள்", "அவர்கள்", "நாம்", "எங்களுக்கு"
        };

        for (String keyword : keywords) {
            if (lower.contains(keyword)) return true;
        }
        return false;
    }
}
