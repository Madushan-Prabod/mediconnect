package com.example.mediconnectnew;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class HospitalActivity extends AppCompatActivity {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;

    private ImageButton btnBack;
    private RecyclerView recyclerViewHospitals;
    private HospitalAdapter hospitalAdapter;
    private FusedLocationProviderClient fusedLocationClient;
    private List<Hospital> allHospitals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital);

        btnBack = findViewById(R.id.btnBack);
        recyclerViewHospitals = findViewById(R.id.recyclerViewHospitals);
        recyclerViewHospitals.setLayoutManager(new LinearLayoutManager(this));

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        btnBack.setOnClickListener(v -> finish());

        loadHospitalsFromJson();
        checkLocationPermissionAndLoad();
    }

    private void loadHospitalsFromJson() {
        allHospitals = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(getAssets().open("hospitals.json"))
            );
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) builder.append(line);
            reader.close();

            JSONArray jsonArray = new JSONArray(builder.toString());

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);

                String name = obj.optString("Title", "Unknown Hospital");
                String address = obj.optString("Address", "No Address");
                String phone = obj.optString("Phone Number", "");
                double latitude = obj.optDouble("Latitude", 0);
                double longitude = obj.optDouble("Longitude", 0);

                allHospitals.add(new Hospital(name, address, phone, latitude, longitude));
            }

        } catch (Exception e) {
            Toast.makeText(this, "Failed to load hospitals: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void checkLocationPermissionAndLoad() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            loadHospitalsNearUser();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadHospitalsNearUser();
            } else {
                Toast.makeText(this,
                        "Location permission required to find nearby hospitals",
                        Toast.LENGTH_LONG).show();
                for (Hospital h : allHospitals) h.setDistance(0);
                displayHospitals(allHospitals);
            }
        }
    }

    private void loadHospitalsNearUser() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) return;

        fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
            if (location != null) {
                filterAndShowHospitals(location);
            } else {
                Toast.makeText(this, "Could not get current location", Toast.LENGTH_SHORT).show();
                for (Hospital h : allHospitals) h.setDistance(0);
                displayHospitals(allHospitals);
            }
        });
    }

    private void filterAndShowHospitals(Location userLocation) {
        List<Hospital> nearbyHospitals = new ArrayList<>();

        for (Hospital hospital : allHospitals) {
            float[] results = new float[1];
            Location.distanceBetween(userLocation.getLatitude(), userLocation.getLongitude(),
                    hospital.getLatitude(), hospital.getLongitude(), results);

            double distanceInKm = results[0] / 1000.0;
            hospital.setDistance(distanceInKm);

            if (distanceInKm <= 50) {
                nearbyHospitals.add(hospital);
            }
        }

        if (nearbyHospitals.isEmpty()) {
            Toast.makeText(this, "No government hospitals found within 50 km", Toast.LENGTH_LONG).show();
        }

        displayHospitals(nearbyHospitals);
    }

    private void displayHospitals(List<Hospital> hospitals) {
        hospitalAdapter = new HospitalAdapter(hospitals, new HospitalAdapter.OnHospitalClickListener() {
            @Override
            public void onCallClick(Hospital hospital) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + hospital.getPhone()));
                startActivity(intent);
            }

            @Override
            public void onDirectionsClick(Hospital hospital) {
                String uri = "geo:0,0?q=" + Uri.encode(hospital.getLatitude() + "," + hospital.getLongitude() + "(" + hospital.getName() + ")");
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                intent.setPackage("com.google.android.apps.maps");
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                } else {
                    Toast.makeText(HospitalActivity.this, "Google Maps not installed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        recyclerViewHospitals.setAdapter(hospitalAdapter);
    }
}
