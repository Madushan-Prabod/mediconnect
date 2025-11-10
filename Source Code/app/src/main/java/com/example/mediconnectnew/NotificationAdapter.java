package com.example.mediconnectnew;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {
    private List<NotificationItem> notifications;
    private OnNotificationClickListener listener;

    public interface OnNotificationClickListener {
        void onNotificationClick(NotificationItem notification);
    }

    public NotificationAdapter(List<NotificationItem> notifications, OnNotificationClickListener listener) {
        this.notifications = notifications;
        this.listener = listener;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_notification, parent, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        NotificationItem notification = notifications.get(position);
        
        // Set title and message with null checks
        holder.tvNotificationTitle.setText(notification.getTitle() != null ? 
                                         notification.getTitle() : "Notification");
        holder.tvNotificationMessage.setText(notification.getMessage() != null ? 
                                          notification.getMessage() : "");
        
        // Format time ago
        holder.tvNotificationTime.setText(getTimeAgo(notification.getTimestamp()));
        
        // Set icon based on notification type
        String type = notification.getType();
        if (type != null) {
            switch (type) {
                case "reminder":
                    holder.ivNotificationIcon.setImageResource(R.drawable.ic_alarm);
                    holder.ivNotificationIcon.setColorFilter(holder.itemView.getContext().getColor(R.color.primary_orange));
                    break;
                case "message":
                    holder.ivNotificationIcon.setImageResource(R.drawable.ic_message);
                    holder.ivNotificationIcon.setColorFilter(holder.itemView.getContext().getColor(R.color.primary_green));
                    break;
                default:
                    holder.ivNotificationIcon.setImageResource(R.drawable.ic_notifications);
                    holder.ivNotificationIcon.setColorFilter(holder.itemView.getContext().getColor(R.color.primary_blue));
                    break;
            }
        } else {
            holder.ivNotificationIcon.setImageResource(R.drawable.ic_notifications);
            holder.ivNotificationIcon.setColorFilter(holder.itemView.getContext().getColor(R.color.primary_blue));
        }
        
        // Show/hide unread indicator
        holder.viewUnreadIndicator.setVisibility(notification.isRead() ? View.GONE : View.VISIBLE);
        
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onNotificationClick(notification);
            }
        });
    }

    @Override
    public int getItemCount() {
        return notifications != null ? notifications.size() : 0;
    }

    private String getTimeAgo(String timestamp) {
        if (TextUtils.isEmpty(timestamp)) {
            return "Just now";
        }
        
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            Date past = sdf.parse(timestamp);
            Date now = new Date();
            
            if (past == null) {
                return "Just now";
            }
            
            long seconds = TimeUnit.MILLISECONDS.toSeconds(now.getTime() - past.getTime());
            long minutes = TimeUnit.MILLISECONDS.toMinutes(now.getTime() - past.getTime());
            long hours = TimeUnit.MILLISECONDS.toHours(now.getTime() - past.getTime());
            long days = TimeUnit.MILLISECONDS.toDays(now.getTime() - past.getTime());
            
            if (seconds < 60) {
                return seconds + " sec ago";
            } else if (minutes < 60) {
                return minutes + " min ago";
            } else if (hours < 24) {
                return hours + " hr ago";
            } else {
                return days + " day" + (days > 1 ? "s" : "") + " ago";
            }
        } catch (ParseException e) {
            return "Recently";
        }
    }

    static class NotificationViewHolder extends RecyclerView.ViewHolder {
        ImageView ivNotificationIcon;
        TextView tvNotificationTitle, tvNotificationMessage, tvNotificationTime;
        View viewUnreadIndicator;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            ivNotificationIcon = itemView.findViewById(R.id.ivNotificationIcon);
            tvNotificationTitle = itemView.findViewById(R.id.tvNotificationTitle);
            tvNotificationMessage = itemView.findViewById(R.id.tvNotificationMessage);
            tvNotificationTime = itemView.findViewById(R.id.tvNotificationTime);
            viewUnreadIndicator = itemView.findViewById(R.id.viewUnreadIndicator);
        }
    }
}
