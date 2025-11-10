package com.example.mediconnectnew;

import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

public class AIService {
    // ✅ Replace with your actual Gemini API key
    private static final String API_KEY = "AIzaSyD9V1PsR8lSS9XIgEbcQHABz4gL2gAoSPY";

    // ✅ Gemini Flash model endpoint
    private static final String API_URL =
            "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=" + API_KEY;

    private final OkHttpClient client;

    public interface AIResponseCallback {
        void onResponse(String response);
        void onError(String error);
    }

    public AIService() {
        client = new OkHttpClient();
    }

    public void getAIResponse(String userMessage, AIResponseCallback callback) {
        try {
            // Build the request JSON
            JSONObject jsonBody = new JSONObject();
            JSONArray contents = new JSONArray();

            // Add the user's message as a part
            JSONObject userContent = new JSONObject();
            JSONArray parts = new JSONArray();
            parts.put(new JSONObject().put("text", userMessage));
            userContent.put("parts", parts);

            contents.put(userContent);
            jsonBody.put("contents", contents);

            // Optional: Add safetySettings or generationConfig if needed
            // jsonBody.put("generationConfig", new JSONObject().put("temperature", 0.7));

            RequestBody requestBody = RequestBody.create(
                    jsonBody.toString(),
                    MediaType.parse("application/json")
            );

            Request request = new Request.Builder()
                    .url(API_URL)
                    .post(requestBody)
                    .addHeader("Content-Type", "application/json")
                    .build();

            // Send the request
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    callback.onError("Network error: " + e.getMessage());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (!response.isSuccessful()) {
                        callback.onError("API error: " + response.code());
                        return;
                    }

                    try {
                        String responseBody = response.body().string();
                        JSONObject jsonResponse = new JSONObject(responseBody);

                        JSONArray candidates = jsonResponse.optJSONArray("candidates");
                        if (candidates != null && candidates.length() > 0) {
                            JSONObject content = candidates.getJSONObject(0).getJSONObject("content");
                            JSONArray parts = content.getJSONArray("parts");
                            String text = parts.getJSONObject(0).getString("text");
                            callback.onResponse(text);
                        } else {
                            callback.onError("No candidates returned by Gemini");
                        }
                    } catch (Exception e) {
                        callback.onError("Response parse error: " + e.getMessage());
                    }
                }
            });

        } catch (Exception e) {
            callback.onError("Request build error: " + e.getMessage());
        }
    }
}
