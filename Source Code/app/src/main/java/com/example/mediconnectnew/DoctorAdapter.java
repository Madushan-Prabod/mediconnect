package com.example.mediconnectnew;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DoctorAdapter extends RecyclerView.Adapter<DoctorAdapter.DoctorViewHolder> {

    private final List<User> doctors;
    private final OnDoctorClickListener listener;

    public interface OnDoctorClickListener {
        void onCallClick(User doctor);
        void onMessageClick(User doctor);
    }

    public DoctorAdapter(List<User> doctors, OnDoctorClickListener listener) {
        this.doctors = doctors;
        this.listener = listener;
    }

    @NonNull
    @Override
    public DoctorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_doctor, parent, false);
        return new DoctorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DoctorViewHolder holder, int position) {
        User doctor = doctors.get(position);
        holder.bind(doctor);
    }

    @Override
    public int getItemCount() {
        return doctors != null ? doctors.size() : 0;
    }

    class DoctorViewHolder extends RecyclerView.ViewHolder {
        TextView tvDoctorName, tvSpecialization, tvStatus;
        Button btnCall, btnMessage;

        public DoctorViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDoctorName = itemView.findViewById(R.id.tvDoctorName);
            tvSpecialization = itemView.findViewById(R.id.tvSpecialization);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            btnCall = itemView.findViewById(R.id.btnCall);
            btnMessage = itemView.findViewById(R.id.btnMessage);
        }

        public void bind(User doctor) {
            tvDoctorName.setText("Dr. " + doctor.getName());
            tvSpecialization.setText(doctor.getSpecialization());

            boolean isOnline = doctor.isOnline();
            tvStatus.setText(isOnline ? "Online" : "New");
            int color = ContextCompat.getColor(itemView.getContext(),
                    isOnline ? R.color.primary_green : R.color.text_secondary);
            tvStatus.setTextColor(color);

            btnCall.setOnClickListener(v -> listener.onCallClick(doctor));
            btnMessage.setOnClickListener(v -> listener.onMessageClick(doctor));
        }
    }
}
