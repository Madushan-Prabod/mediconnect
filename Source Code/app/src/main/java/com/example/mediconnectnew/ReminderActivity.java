package com.example.mediconnectnew;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ReminderActivity extends AppCompatActivity {
    private ImageButton btnBack;
    private EditText etTitle, etDescription, etDateTime;
    private Button btnAddReminder;
    private RecyclerView recyclerViewReminders;
    private ReminderAdapter reminderAdapter;
    private DatabaseHelper dbHelper;
    private Calendar selectedDateTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);

        initViews();
        setupRecyclerView();
        setupClickListeners();
        loadReminders();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        etTitle = findViewById(R.id.etTitle);
        etDescription = findViewById(R.id.etDescription);
        etDateTime = findViewById(R.id.etDateTime);
        btnAddReminder = findViewById(R.id.btnAddReminder);
        recyclerViewReminders = findViewById(R.id.recyclerViewReminders);
        dbHelper = new DatabaseHelper(this);
        selectedDateTime = Calendar.getInstance();
    }

    private void setupRecyclerView() {
        recyclerViewReminders.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setupClickListeners() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        etDateTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimePicker();
            }
        });

        btnAddReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addReminder();
            }
        });
    }

    private void showDateTimePicker() {
        Calendar currentDate = Calendar.getInstance();

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        selectedDateTime.set(year, monthOfYear, dayOfMonth);
                        showTimePicker();
                    }
                }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    private void showTimePicker() {
        Calendar currentTime = Calendar.getInstance();

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        selectedDateTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        selectedDateTime.set(Calendar.MINUTE, minute);
                        updateDateTimeDisplay();
                    }
                }, currentTime.get(Calendar.HOUR_OF_DAY), currentTime.get(Calendar.MINUTE), false);

        timePickerDialog.show();
    }

    private void updateDateTimeDisplay() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        etDateTime.setText(sdf.format(selectedDateTime.getTime()));
    }

    private void addReminder() {
        String title = etTitle.getText().toString().trim();
        String description = etDescription.getText().toString().trim();
        String dateTime = etDateTime.getText().toString().trim();

        if (title.isEmpty() || dateTime.isEmpty()) {
            Toast.makeText(this, "Please fill required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences prefs = getSharedPreferences("MediConnect", MODE_PRIVATE);
        String userEmail = prefs.getString("userEmail", "");

        Reminder reminder = new Reminder(userEmail, title, description, dateTime);
        long result = dbHelper.addReminder(reminder);

        if (result != -1) {
            Toast.makeText(this, "Reminder added successfully", Toast.LENGTH_SHORT).show();
            scheduleNotification(title, description, selectedDateTime.getTimeInMillis(), (int) result);
            clearFields();
            loadReminders();
        } else {
            Toast.makeText(this, "Failed to add reminder", Toast.LENGTH_SHORT).show();
        }
    }

    private void scheduleNotification(String title, String description, long timeInMillis, int reminderId) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, ReminderReceiver.class);
        intent.putExtra("title", title);
        intent.putExtra("description", description);
        intent.putExtra("reminderId", reminderId);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, reminderId, intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        if (alarmManager != null) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent);
        }
    }

    private void clearFields() {
        etTitle.setText("");
        etDescription.setText("");
        etDateTime.setText("");
    }

    private void loadReminders() {
        SharedPreferences prefs = getSharedPreferences("MediConnect", MODE_PRIVATE);
        String userEmail = prefs.getString("userEmail", "");

        List<Reminder> reminders = dbHelper.getUserReminders(userEmail);
        reminderAdapter = new ReminderAdapter(reminders, new ReminderAdapter.OnReminderActionListener() {
            @Override
            public void onCompleteClick(Reminder reminder) {
                dbHelper.completeReminder(reminder.getId());
                loadReminders();
                Toast.makeText(ReminderActivity.this, "Reminder completed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDeleteClick(Reminder reminder) {
                dbHelper.deleteReminder(reminder.getId());
                loadReminders();
                Toast.makeText(ReminderActivity.this, "Reminder deleted", Toast.LENGTH_SHORT).show();
            }
        });
        recyclerViewReminders.setAdapter(reminderAdapter);
    }
}
