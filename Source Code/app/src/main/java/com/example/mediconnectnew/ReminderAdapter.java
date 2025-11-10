package com.example.mediconnectnew;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.ReminderViewHolder> {
    private List<Reminder> reminders;
    private OnReminderActionListener listener;

    public interface OnReminderActionListener {
        void onCompleteClick(Reminder reminder);
        void onDeleteClick(Reminder reminder);
    }

    public ReminderAdapter(List<Reminder> reminders, OnReminderActionListener listener) {
        this.reminders = reminders;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ReminderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_reminder, parent, false);
        return new ReminderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReminderViewHolder holder, int position) {
        Reminder reminder = reminders.get(position);

        holder.tvReminderTitle.setText(reminder.getTitle() != null ? reminder.getTitle() : "Reminder");
        holder.tvReminderDescription.setText(reminder.getDescription() != null ? reminder.getDescription() : "");
        holder.tvReminderDateTime.setText(reminder.getDateTime() != null ? reminder.getDateTime() : "No date set");

        holder.btnComplete.setOnClickListener(v -> {
            if (listener != null) {
                listener.onCompleteClick(reminder);
            }
        });

        holder.btnDelete.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDeleteClick(reminder);
            }
        });
    }

    @Override
    public int getItemCount() {
        return reminders != null ? reminders.size() : 0;
    }

    static class ReminderViewHolder extends RecyclerView.ViewHolder {
        TextView tvReminderTitle, tvReminderDescription, tvReminderDateTime;
        Button btnComplete, btnDelete;

        public ReminderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvReminderTitle = itemView.findViewById(R.id.tvReminderTitle);
            tvReminderDescription = itemView.findViewById(R.id.tvReminderDescription);
            tvReminderDateTime = itemView.findViewById(R.id.tvReminderDateTime);
            btnComplete = itemView.findViewById(R.id.btnComplete);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
