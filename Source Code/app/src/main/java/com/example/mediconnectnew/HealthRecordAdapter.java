package com.example.mediconnectnew;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class HealthRecordAdapter extends RecyclerView.Adapter<HealthRecordAdapter.RecordViewHolder> {
    private List<HealthRecord> records;
    private OnRecordActionListener listener;

    public interface OnRecordActionListener {
        void onViewClick(HealthRecord record);
        void onShareClick(HealthRecord record);
        void onDeleteClick(HealthRecord record);
    }

    public HealthRecordAdapter(List<HealthRecord> records, OnRecordActionListener listener) {
        this.records = records;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_health_record, parent, false);
        return new RecordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecordViewHolder holder, int position) {
        HealthRecord record = records.get(position);
        
        // Set text with null checks
        holder.tvRecordTitle.setText(record.getTitle() != null ? 
                                   record.getTitle() : "Untitled Record");
        
        holder.tvRecordDate.setText(record.getDateCreated() != null ? 
                                  record.getDateCreated() : "No date");
        
        holder.tvRecordDescription.setText(record.getDescription() != null && !TextUtils.isEmpty(record.getDescription()) ? 
                                         record.getDescription() : "No description");
        
        // Set file type icon
        String fileType = record.getFileType();
        if (fileType != null) {
            switch (fileType.toLowerCase()) {
                case "pdf":
                    holder.ivFileType.setImageResource(R.drawable.ic_file_pdf);
                    holder.ivFileType.setColorFilter(holder.itemView.getContext().getColor(R.color.primary_red));
                    break;
                case "image":
                    holder.ivFileType.setImageResource(R.drawable.ic_file_image);
                    holder.ivFileType.setColorFilter(holder.itemView.getContext().getColor(R.color.primary_green));
                    break;
                default:
                    holder.ivFileType.setImageResource(R.drawable.ic_file_document);
                    holder.ivFileType.setColorFilter(holder.itemView.getContext().getColor(R.color.primary_blue));
                    break;
            }
        } else {
            holder.ivFileType.setImageResource(R.drawable.ic_file_document);
            holder.ivFileType.setColorFilter(holder.itemView.getContext().getColor(R.color.primary_blue));
        }

        holder.btnView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onViewClick(record);
            }
        });
        
        holder.btnShare.setOnClickListener(v -> {
            if (listener != null) {
                listener.onShareClick(record);
            }
        });
        
        holder.btnDelete.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDeleteClick(record);
            }
        });
        
        holder.btnMore.setOnClickListener(v -> {
            // Show options menu
            android.widget.PopupMenu popup = new android.widget.PopupMenu(holder.itemView.getContext(), holder.btnMore);
            popup.getMenuInflater().inflate(R.menu.record_options_menu, popup.getMenu());
            popup.setOnMenuItemClickListener(item -> {
                int id = item.getItemId();
                if (id == R.id.menu_view) {
                    if (listener != null) {
                        listener.onViewClick(record);
                    }
                    return true;
                } else if (id == R.id.menu_share) {
                    if (listener != null) {
                        listener.onShareClick(record);
                    }
                    return true;
                } else if (id == R.id.menu_delete) {
                    if (listener != null) {
                        listener.onDeleteClick(record);
                    }
                    return true;
                }
                return false;
            });
            popup.show();
        });
    }

    @Override
    public int getItemCount() {
        return records != null ? records.size() : 0;
    }

    static class RecordViewHolder extends RecyclerView.ViewHolder {
        ImageView ivFileType;
        TextView tvRecordTitle, tvRecordDate, tvRecordDescription;
        Button btnView, btnShare, btnDelete;
        ImageButton btnMore;

        public RecordViewHolder(@NonNull View itemView) {
            super(itemView);
            ivFileType = itemView.findViewById(R.id.ivFileType);
            tvRecordTitle = itemView.findViewById(R.id.tvRecordTitle);
            tvRecordDate = itemView.findViewById(R.id.tvRecordDate);
            tvRecordDescription = itemView.findViewById(R.id.tvRecordDescription);
            btnView = itemView.findViewById(R.id.btnView);
            btnShare = itemView.findViewById(R.id.btnShare);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnMore = itemView.findViewById(R.id.btnMore);
        }
    }
}
