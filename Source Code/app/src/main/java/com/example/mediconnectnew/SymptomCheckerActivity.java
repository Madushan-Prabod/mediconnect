package com.example.mediconnectnew;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class SymptomCheckerActivity extends AppCompatActivity {
    private ImageButton btnBack;
    private CheckBox cbFever, cbCough, cbHeadache, cbNausea, cbFatigue, cbSoreThroat, cbBodyAche, cbDiarrhea;
    private CheckBox cbShortnessOfBreath, cbChestPain, cbDizziness, cbLossOfTasteOrSmell, cbRunnyNose, cbVomiting;
    private CheckBox cbChills, cbSweating, cbRapidHeartbeat, cbConfusion, cbSwelling, cbSkinRash, cbEyeRedness;
    private CheckBox cbWeightLoss, cbDifficultySwallowing, cbPersistentVomiting, cbNightSweats;

    private Button btnCheckSymptoms, btnConsultDoctor;
    private TextView tvResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_symptom_checker);

        initViews();
        setupClickListeners();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);

        cbFever = findViewById(R.id.cbFever);
        cbCough = findViewById(R.id.cbCough);
        cbHeadache = findViewById(R.id.cbHeadache);
        cbNausea = findViewById(R.id.cbNausea);
        cbFatigue = findViewById(R.id.cbFatigue);
        cbSoreThroat = findViewById(R.id.cbSoreThroat);
        cbBodyAche = findViewById(R.id.cbBodyAche);
        cbDiarrhea = findViewById(R.id.cbDiarrhea);

        cbShortnessOfBreath = findViewById(R.id.cbShortnessOfBreath);
        cbChestPain = findViewById(R.id.cbChestPain);
        cbDizziness = findViewById(R.id.cbDizziness);
        cbLossOfTasteOrSmell = findViewById(R.id.cbLossOfTasteOrSmell);
        cbRunnyNose = findViewById(R.id.cbRunnyNose);
        cbVomiting = findViewById(R.id.cbVomiting);

        cbChills = findViewById(R.id.cbChills);
        cbSweating = findViewById(R.id.cbSweating);
        cbRapidHeartbeat = findViewById(R.id.cbRapidHeartbeat);
        cbConfusion = findViewById(R.id.cbConfusion);
        cbSwelling = findViewById(R.id.cbSwelling);
        cbSkinRash = findViewById(R.id.cbSkinRash);
        cbEyeRedness = findViewById(R.id.cbEyeRedness);

        cbWeightLoss = findViewById(R.id.cbWeightLoss);
        cbDifficultySwallowing = findViewById(R.id.cbDifficultySwallowing);
        cbPersistentVomiting = findViewById(R.id.cbPersistentVomiting);
        cbNightSweats = findViewById(R.id.cbNightSweats);

        btnCheckSymptoms = findViewById(R.id.btnCheckSymptoms);
        btnConsultDoctor = findViewById(R.id.btnConsultDoctor);
        tvResults = findViewById(R.id.tvResults);

        btnConsultDoctor.setVisibility(View.GONE);
        tvResults.setVisibility(View.GONE);
    }

    private void setupClickListeners() {
        btnBack.setOnClickListener(v -> finish());

        btnCheckSymptoms.setOnClickListener(v -> checkSymptoms());

        btnConsultDoctor.setOnClickListener(v -> startActivity(new Intent(SymptomCheckerActivity.this, ConsultDoctorActivity.class)));
    }

    private void checkSymptoms() {
        List<String> symptoms = new ArrayList<>();

        if (cbFever.isChecked()) symptoms.add("Fever");
        if (cbCough.isChecked()) symptoms.add("Cough");
        if (cbHeadache.isChecked()) symptoms.add("Headache");
        if (cbNausea.isChecked()) symptoms.add("Nausea");
        if (cbFatigue.isChecked()) symptoms.add("Fatigue");
        if (cbSoreThroat.isChecked()) symptoms.add("Sore Throat");
        if (cbBodyAche.isChecked()) symptoms.add("Body Ache");
        if (cbDiarrhea.isChecked()) symptoms.add("Diarrhea");

        if (cbShortnessOfBreath.isChecked()) symptoms.add("Shortness of Breath");
        if (cbChestPain.isChecked()) symptoms.add("Chest Pain");
        if (cbDizziness.isChecked()) symptoms.add("Dizziness");
        if (cbLossOfTasteOrSmell.isChecked()) symptoms.add("Loss of Taste or Smell");
        if (cbRunnyNose.isChecked()) symptoms.add("Runny Nose");
        if (cbVomiting.isChecked()) symptoms.add("Vomiting");

        if (cbChills.isChecked()) symptoms.add("Chills");
        if (cbSweating.isChecked()) symptoms.add("Sweating");
        if (cbRapidHeartbeat.isChecked()) symptoms.add("Rapid Heartbeat");
        if (cbConfusion.isChecked()) symptoms.add("Confusion");
        if (cbSwelling.isChecked()) symptoms.add("Swelling");
        if (cbSkinRash.isChecked()) symptoms.add("Skin Rash");
        if (cbEyeRedness.isChecked()) symptoms.add("Eye Redness or Irritation");

        if (cbWeightLoss.isChecked()) symptoms.add("Weight Loss");
        if (cbDifficultySwallowing.isChecked()) symptoms.add("Difficulty Swallowing");
        if (cbPersistentVomiting.isChecked()) symptoms.add("Persistent Vomiting");
        if (cbNightSweats.isChecked()) symptoms.add("Night Sweats");

        if (symptoms.isEmpty()) {
            tvResults.setText("Please select at least one symptom.");
            tvResults.setVisibility(View.VISIBLE);
            btnConsultDoctor.setVisibility(View.GONE);
            return;
        }

        String result = analyzeSymptoms(symptoms);
        tvResults.setText(result);
        tvResults.setVisibility(View.VISIBLE);

        if (isSevere(symptoms)) {
            btnConsultDoctor.setVisibility(View.VISIBLE);
        } else {
            btnConsultDoctor.setVisibility(View.GONE);
        }
    }

    private String analyzeSymptoms(List<String> symptoms) {
        StringBuilder result = new StringBuilder("Based on your symptoms:\n\n");

        if (symptoms.contains("Fever") && symptoms.contains("Cough")) {
            result.append("• Possible respiratory infection (flu, common cold, COVID-19)\n");
            result.append("• Monitor symptoms, seek medical help if worsening or breathing difficulty occurs.\n\n");
        }

        if (symptoms.contains("Headache") && symptoms.contains("Fever")) {
            result.append("• Possible viral infection such as meningitis or influenza\n");
            result.append("• Rest, stay hydrated, and consult healthcare if symptoms persist.\n\n");
        }

        if (symptoms.contains("Nausea") && symptoms.contains("Diarrhea")) {
            result.append("• Likely gastroenteritis or food poisoning\n");
            result.append("• Maintain hydration and avoid solid foods until symptoms improve.\n\n");
        }

        if (symptoms.contains("Shortness of Breath") || symptoms.contains("Chest Pain")) {
            result.append("• Possible serious respiratory or cardiac issue\n");
            result.append("• Seek immediate medical attention.\n\n");
        }

        if (symptoms.contains("Loss of Taste or Smell")) {
            result.append("• Common symptom of COVID-19 or other viral infections\n");
            result.append("• Consider COVID-19 testing and isolate as per guidelines.\n\n");
        }

        if (symptoms.contains("Fatigue") && symptoms.contains("Body Ache")) {
            result.append("• Could indicate viral infection, flu, or chronic fatigue\n");
            result.append("• Rest and monitor symptoms.\n\n");
        }

        if (symptoms.contains("Sore Throat") && symptoms.contains("Runny Nose")) {
            result.append("• Likely common cold or allergic rhinitis\n");
            result.append("• Over-the-counter medications may help relieve symptoms.\n\n");
        }

        if (symptoms.contains("Dizziness") && symptoms.contains("Headache")) {
            result.append("• Possible dehydration, migraine, or low blood pressure\n");
            result.append("• Drink fluids and seek medical advice if severe.\n\n");
        }

        if (symptoms.contains("Vomiting") && symptoms.contains("Diarrhea")) {
            result.append("• Suggests stomach infection\n");
            result.append("• Hydration critical; seek medical attention if vomiting persists.\n\n");
        }

        if (symptoms.contains("Chills") && symptoms.contains("Sweating")) {
            result.append("• May indicate fever or infection\n");
            result.append("• Monitor temperature and consult if fever is high.\n\n");
        }

        if (symptoms.contains("Rapid Heartbeat")) {
            result.append("• Could be due to anxiety, fever, dehydration, or cardiac issues\n");
            result.append("• Seek medical advice if persistent.\n\n");
        }

        if (symptoms.contains("Confusion")) {
            result.append("• Serious symptom, could indicate infection or neurological issues\n");
            result.append("• Immediate medical evaluation required.\n\n");
        }

        if (symptoms.contains("Swelling")) {
            result.append("• May indicate allergic reaction, infection or circulatory problem\n");
            result.append("• Consult a healthcare professional.\n\n");
        }

        if (symptoms.contains("Skin Rash")) {
            result.append("• Could be allergic reaction, infection, or other dermatological conditions\n");
            result.append("• Monitor and consult a dermatologist if worsening.\n\n");
        }

        if (symptoms.contains("Eye Redness or Irritation")) {
            result.append("• Possible conjunctivitis or allergies\n");
            result.append("• Avoid touching eyes; seek treatment if persistent.\n\n");
        }

        if (symptoms.contains("Weight Loss")) {
            result.append("• Unintentional weight loss is a concern\n");
            result.append("• Consult doctor for evaluation.\n\n");
        }

        if (symptoms.contains("Difficulty Swallowing")) {
            result.append("• Could indicate infection or neurological condition\n");
            result.append("• Prompt medical assessment recommended.\n\n");
        }

        if (symptoms.contains("Persistent Vomiting")) {
            result.append("• Serious symptom; risk of dehydration\n");
            result.append("• Seek urgent medical care.\n\n");
        }

        if (symptoms.contains("Night Sweats")) {
            result.append("• Possible infection, hormonal issues, or other chronic conditions\n");
            result.append("• Consult healthcare professional for evaluation.\n\n");
        }

        result.append("⚠️ This is not a medical diagnosis. Please consult a healthcare professional for proper evaluation.");

        return result.toString();
    }

    private boolean isSevere(List<String> symptoms) {
        // Severe if 3+ symptoms or any critical symptoms
        return symptoms.size() >= 3 ||
                (symptoms.contains("Fever") && symptoms.contains("Headache")) ||
                (symptoms.contains("Nausea") && symptoms.contains("Diarrhea")) ||
                symptoms.contains("Shortness of Breath") ||
                symptoms.contains("Chest Pain") ||
                symptoms.contains("Confusion") ||
                symptoms.contains("Persistent Vomiting") ||
                symptoms.contains("Difficulty Swallowing");
    }
}
