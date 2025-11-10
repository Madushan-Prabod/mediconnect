package com.example.mediconnectnew;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HospitalAdapter extends RecyclerView.Adapter<HospitalAdapter.HospitalViewHolder> {

    private List<Hospital> hospitalList;
    private OnHospitalClickListener listener;

    public interface OnHospitalClickListener {
        void onCallClick(Hospital hospital);
        void onDirectionsClick(Hospital hospital);
    }

    public HospitalAdapter(List<Hospital> hospitalList, OnHospitalClickListener listener) {
        this.hospitalList = hospitalList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public HospitalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_hospital, parent, false);
        return new HospitalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HospitalViewHolder holder, int position) {
        Hospital hospital = hospitalList.get(position);
        holder.textViewName.setText(hospital.getName());
        holder.textViewAddress.setText(hospital.getAddress());
        holder.textViewDistance.setText(String.format("%.2f km", hospital.getDistance()));

        holder.buttonCall.setOnClickListener(v -> listener.onCallClick(hospital));
        holder.buttonDirections.setOnClickListener(v -> listener.onDirectionsClick(hospital));
    }

    @Override
    public int getItemCount() {
        return hospitalList.size();
    }

    public static class HospitalViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName, textViewAddress, textViewDistance;
        Button buttonCall, buttonDirections;

        public HospitalViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.tvHospitalName);
            textViewAddress = itemView.findViewById(R.id.tvHospitalAddress);
            textViewDistance = itemView.findViewById(R.id.tvDistance);
            buttonCall = itemView.findViewById(R.id.btnCall);
            buttonDirections = itemView.findViewById(R.id.btnDirections);
        }
    }
}
