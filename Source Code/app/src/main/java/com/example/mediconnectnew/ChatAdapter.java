package com.example.mediconnectnew;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {
    private List<ChatMessage> chatMessages;

    public ChatAdapter(List<ChatMessage> chatMessages) {
        this.chatMessages = chatMessages;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_chat_message, parent, false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        ChatMessage message = chatMessages.get(position);

        if (message.isUser()) {
            holder.layoutUserMessage.setVisibility(View.VISIBLE);
            holder.layoutAiMessage.setVisibility(View.GONE);
            holder.tvUserMessage.setText(message.getMessage());
        } else {
            holder.layoutUserMessage.setVisibility(View.GONE);
            holder.layoutAiMessage.setVisibility(View.VISIBLE);

            // Convert markdown-like syntax to HTML
            String formattedMessage = markdownToHtml(message.getMessage());

            // Apply HTML formatting to the AI message
            holder.tvAiMessage.setText(Html.fromHtml(formattedMessage, Html.FROM_HTML_MODE_LEGACY));
        }
    }

    private String markdownToHtml(String markdown) {
        return markdown
                .replaceAll("\\*\\*(.*?)\\*\\*", "<b>$1</b>")      // Bold
                .replaceAll("\\*(.*?)\\*", "<i>$1</i>")            // Italic
                .replaceAll("(?m)^\\* (.*?)$", "<br>&bull; $1")    // Bullet points
                .replaceAll("\n", "<br>");                         // Line breaks
    }



    @Override
    public int getItemCount() {
        return chatMessages.size();
    }

    static class ChatViewHolder extends RecyclerView.ViewHolder {
        LinearLayout layoutUserMessage, layoutAiMessage;
        TextView tvUserMessage, tvAiMessage;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            layoutUserMessage = itemView.findViewById(R.id.layoutUserMessage);
            layoutAiMessage = itemView.findViewById(R.id.layoutAiMessage);
            tvUserMessage = itemView.findViewById(R.id.tvUserMessage);
            tvAiMessage = itemView.findViewById(R.id.tvAiMessage);
        }
    }
}
