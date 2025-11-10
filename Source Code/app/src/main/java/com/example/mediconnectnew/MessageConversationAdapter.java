package com.example.mediconnectnew;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;
import java.util.List;

public class MessageConversationAdapter extends RecyclerView.Adapter<MessageConversationAdapter.ConversationViewHolder> {
    private List<MessageConversation> conversations;
    private OnConversationClickListener listener;

    public interface OnConversationClickListener {
        void onConversationClick(MessageConversation conversation);
    }

    public MessageConversationAdapter(List<MessageConversation> conversations, OnConversationClickListener listener) {
        this.conversations = conversations;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ConversationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_message_conversation, parent, false);
        return new ConversationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ConversationViewHolder holder, int position) {
        MessageConversation conversation = conversations.get(position);

        // Set text with null checks
        holder.tvContactName.setText(conversation.getContactName() != null ?
                conversation.getContactName() : "Unknown Contact");

        holder.tvLastMessage.setText(conversation.getLastMessage() != null ?
                conversation.getLastMessage() : "No messages yet");

        holder.tvMessageTime.setText(conversation.getLastMessageTime() != null ?
                conversation.getLastMessageTime() : "");

        // Set contact type or specialization
        String contactInfo = conversation.getContactSpecialization();
        if (TextUtils.isEmpty(contactInfo)) {
            contactInfo = conversation.getContactType();
        }
        holder.tvContactType.setText(contactInfo != null ? contactInfo : "");

        // Show/hide unread count
        if (conversation.getUnreadCount() > 0) {
            holder.tvUnreadCount.setVisibility(View.VISIBLE);
            holder.tvUnreadCount.setText(String.valueOf(conversation.getUnreadCount()));
        } else {
            holder.tvUnreadCount.setVisibility(View.GONE);
        }

        // Set contact photo based on type
        String contactType = conversation.getContactType();
        if (contactType != null && contactType.equals("ai")) {
            holder.ivContactPhoto.setImageResource(R.drawable.ic_robot);
        } else {
            holder.ivContactPhoto.setImageResource(R.drawable.ic_person_placeholder);
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onConversationClick(conversation);
            }
        });
    }

    @Override
    public int getItemCount() {
        return conversations != null ? conversations.size() : 0;
    }

    static class ConversationViewHolder extends RecyclerView.ViewHolder {
        CircleImageView ivContactPhoto;
        TextView tvContactName, tvLastMessage, tvMessageTime, tvContactType, tvUnreadCount;

        public ConversationViewHolder(@NonNull View itemView) {
            super(itemView);
            ivContactPhoto = itemView.findViewById(R.id.ivContactPhoto);
            tvContactName = itemView.findViewById(R.id.tvContactName);
            tvLastMessage = itemView.findViewById(R.id.tvLastMessage);
            tvMessageTime = itemView.findViewById(R.id.tvMessageTime);
            tvContactType = itemView.findViewById(R.id.tvContactType);
            tvUnreadCount = itemView.findViewById(R.id.tvUnreadCount);
        }
    }
}
