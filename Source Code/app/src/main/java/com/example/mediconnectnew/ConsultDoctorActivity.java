package com.example.mediconnectnew;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ConsultDoctorActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private DoctorAdapter doctorAdapter;
    private ArrayList<User> doctorList = new ArrayList<>();
    private ImageButton btnBack; // Back button reference

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consult_doctor);

        recyclerView = findViewById(R.id.recyclerDoctors);
        progressBar = findViewById(R.id.progressBar);
        btnBack = findViewById(R.id.btnBack); // initialize back button

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        doctorAdapter = new DoctorAdapter(doctorList, new DoctorAdapter.OnDoctorClickListener() {
            @Override
            public void onCallClick(User doctor) {
                if (doctor.getPhone() != null && !doctor.getPhone().isEmpty()) {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + doctor.getPhone()));
                    startActivity(intent);
                } else {
                    Toast.makeText(ConsultDoctorActivity.this, "Phone number not available", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onMessageClick(User doctor) {
                if (doctor.getPhone() != null && !doctor.getPhone().isEmpty()) {
                    String phoneNumber = doctor.getPhone();

                    // Format to international number (Sri Lanka assumed)
                    if (!phoneNumber.startsWith("+")) {
                        phoneNumber = "+94" + phoneNumber.replaceFirst("^0+", "");
                    }

                    // Message to send
                    String message = "Good day, Doctor. I’ve connected with you via the MediConnect application. I appreciate your time and assistance. ";
                    String url = "https://wa.me/" + phoneNumber + "?text=" + Uri.encode(message);

                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(url));
                        intent.setPackage("com.whatsapp");
                        startActivity(intent);
                    } catch (Exception e) {
                        Toast.makeText(ConsultDoctorActivity.this, "WhatsApp not installed.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ConsultDoctorActivity.this, "Phone number not available", Toast.LENGTH_SHORT).show();
                }
            }
        });

        recyclerView.setAdapter(doctorAdapter);
        loadDoctorsFromFirebase();

        // Set back button action
        btnBack.setOnClickListener(v -> finish());
    }

    private void loadDoctorsFromFirebase() {
        progressBar.setVisibility(View.VISIBLE);

        FirebaseDatabase.getInstance().getReference("users")
                .orderByChild("doctor")
                .equalTo(true)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        doctorList.clear();
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            User doctor = ds.getValue(User.class);
                            if (doctor != null) {
                                doctorList.add(doctor);
                            }
                        }
                        doctorAdapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(ConsultDoctorActivity.this, "Failed to load doctors", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }
                });
    }
}
