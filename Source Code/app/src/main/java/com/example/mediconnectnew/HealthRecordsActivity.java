package com.example.mediconnectnew;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.dhaval2404.imagepicker.ImagePicker;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HealthRecordsActivity extends AppCompatActivity {
    private static final String TAG = "HealthRecordsActivity";
    private static final int STORAGE_PERMISSION_CODE = 1001;
    private static final int FILE_PICKER_CODE = 1002;
    private static final int CAMERA_PERMISSION_CODE = 1003;

    private ImageButton btnBack, btnAddRecord;
    private CardView cardUpload;
    private EditText etRecordTitle, etRecordDescription;
    private Button btnSelectFile, btnTakePhoto, btnUpload, btnCancel;
    private TextView tvSelectedFile;
    private RecyclerView recyclerViewRecords;
    private HealthRecordAdapter recordAdapter;
    private List<HealthRecord> healthRecords;
    private DatabaseHelper dbHelper;
    private Uri selectedFileUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_records);

        initViews();
        setupRecyclerView();
        setupClickListeners();
        checkPermissions();
        loadHealthRecords();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        btnAddRecord = findViewById(R.id.btnAddRecord);
        cardUpload = findViewById(R.id.cardUpload);
        etRecordTitle = findViewById(R.id.etRecordTitle);
        etRecordDescription = findViewById(R.id.etRecordDescription);
        btnSelectFile = findViewById(R.id.btnSelectFile);
        btnTakePhoto = findViewById(R.id.btnTakePhoto);
        btnUpload = findViewById(R.id.btnUpload);
        btnCancel = findViewById(R.id.btnCancel);
        tvSelectedFile = findViewById(R.id.tvSelectedFile);
        recyclerViewRecords = findViewById(R.id.recyclerViewRecords);
        dbHelper = new DatabaseHelper(this);
    }

    private void setupRecyclerView() {
        healthRecords = new ArrayList<>();
        recyclerViewRecords.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setupClickListeners() {
        btnBack.setOnClickListener(v -> finish());

        btnAddRecord.setOnClickListener(v -> {
            cardUpload.setVisibility(cardUpload.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
        });

        btnSelectFile.setOnClickListener(v -> {
            if (checkStoragePermission()) {
                selectFile();
            } else {
                requestStoragePermission();
            }
        });

        btnTakePhoto.setOnClickListener(v -> {
            if (checkCameraPermission()) {
                takePhoto();
            } else {
                requestCameraPermission();
            }
        });

        btnUpload.setOnClickListener(v -> uploadRecord());

        btnCancel.setOnClickListener(v -> {
            cardUpload.setVisibility(View.GONE);
            clearUploadForm();
        });
    }

    private boolean checkStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // For Android 10 and above, no READ_EXTERNAL_STORAGE permission required for this use case
            return true;
        }
        return ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // No need to request for Android 10+
            selectFile();
            return;
        }
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                STORAGE_PERMISSION_CODE);
    }

    private boolean checkCameraPermission() {
        return ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CAMERA},
                CAMERA_PERMISSION_CODE);
    }

    private void checkPermissions() {
        if (!checkStoragePermission()) {
            requestStoragePermission();
        }
        if (!checkCameraPermission()) {
            requestCameraPermission();
        }
    }

    private void selectFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        try {
            startActivityForResult(Intent.createChooser(intent, "Select File"), FILE_PICKER_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "Please install a File Manager.", Toast.LENGTH_SHORT).show();
        }
    }

    private void takePhoto() {
        try {
            ImagePicker.with(this)
                    .cameraOnly()
                    .compress(1024)
                    .maxResultSize(1080, 1080)
                    .start();
        } catch (Exception e) {
            Log.e(TAG, "Error taking photo: " + e.getMessage());
            Toast.makeText(this, "Error taking photo", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadRecord() {
        String title = etRecordTitle.getText().toString().trim();
        String description = etRecordDescription.getText().toString().trim();

        if (title.isEmpty()) {
            Toast.makeText(this, "Please enter a title", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedFileUri == null) {
            Toast.makeText(this, "Please select a file", Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences prefs = getSharedPreferences("MediConnect", MODE_PRIVATE);
        String userEmail = prefs.getString("userEmail", "");

        // Get current date
        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        // Determine file type
        String fileType = getFileType(selectedFileUri);

        HealthRecord record = new HealthRecord(userEmail, title, description,
                selectedFileUri.toString(), currentDate);
        record.setFileType(fileType);

        long result = dbHelper.addHealthRecord(record);

        if (result != -1) {
            Toast.makeText(this, "Record uploaded successfully", Toast.LENGTH_SHORT).show();
            cardUpload.setVisibility(View.GONE);
            clearUploadForm();
            loadHealthRecords();
        } else {
            Toast.makeText(this, "Failed to upload record", Toast.LENGTH_SHORT).show();
        }
    }

    private String getFileType(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        String type = mime.getExtensionFromMimeType(cR.getType(uri));

        if (type == null) {
            // Try to determine from URI path
            String path = uri.getPath();
            if (path != null) {
                if (path.toLowerCase().endsWith(".pdf")) return "PDF";
                if (path.toLowerCase().endsWith(".jpg") || path.toLowerCase().endsWith(".jpeg") ||
                        path.toLowerCase().endsWith(".png")) return "Image";
                if (path.toLowerCase().endsWith(".doc") || path.toLowerCase().endsWith(".docx")) return "Document";
            }
            return "File";
        }

        switch (type.toLowerCase()) {
            case "pdf":
                return "PDF";
            case "jpg":
            case "jpeg":
            case "png":
                return "Image";
            case "doc":
            case "docx":
                return "Document";
            default:
                return "File";
        }
    }

    private void clearUploadForm() {
        etRecordTitle.setText("");
        etRecordDescription.setText("");
        tvSelectedFile.setText("No file selected");
        selectedFileUri = null;
    }

    private void loadHealthRecords() {
        SharedPreferences prefs = getSharedPreferences("MediConnect", MODE_PRIVATE);
        String userEmail = prefs.getString("userEmail", "");

        healthRecords = dbHelper.getUserHealthRecords(userEmail);
        recordAdapter = new HealthRecordAdapter(healthRecords, new HealthRecordAdapter.OnRecordActionListener() {
            @Override
            public void onViewClick(HealthRecord record) {
                viewRecord(record);
            }

            @Override
            public void onShareClick(HealthRecord record) {
                shareRecord(record);
            }

            @Override
            public void onDeleteClick(HealthRecord record) {
                confirmDeleteRecord(record);
            }
        });
        recyclerViewRecords.setAdapter(recordAdapter);
    }

    private void viewRecord(HealthRecord record) {
        try {
            Uri fileUri = Uri.parse(record.getFilePath());
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(fileUri, getMimeType(fileUri));
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(intent);
        } catch (Exception e) {
            Log.e(TAG, "Error viewing record: " + e.getMessage());
            Toast.makeText(this, "Cannot open file", Toast.LENGTH_SHORT).show();
        }
    }



    private String getMimeTypeFromExtension(String fileName) {
        String extension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase(Locale.ROOT);

        switch (extension) {
            case "pdf":
                return "application/pdf";
            case "jpg":
            case "jpeg":
            case "png":
                return "image/*";
            case "doc":
                return "application/msword";
            case "docx":
                return "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
            case "txt":
                return "text/plain";
            case "ppt":
            case "pptx":
                return "application/vnd.ms-powerpoint";
            case "xls":
            case "xlsx":
                return "application/vnd.ms-excel";
            case "mp4":
                return "video/mp4";
            default:
                return null;
        }
    }




    private String getMimeType(Uri uri) {
        String mimeType = getContentResolver().getType(uri);
        if (mimeType == null) {
            String ext = MimeTypeMap.getFileExtensionFromUrl(uri.toString());
            if (ext != null) {
                mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(ext.toLowerCase());
            }
        }
        return mimeType != null ? mimeType : "*/*";
    }


    private void shareRecord(HealthRecord record) {
        try {
            Uri fileUri = Uri.parse(record.getFilePath());
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType(getMimeType(fileUri));
            shareIntent.putExtra(Intent.EXTRA_STREAM, fileUri);
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, record.getTitle());
            shareIntent.putExtra(Intent.EXTRA_TEXT, "Sharing medical record: " + record.getTitle());
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(Intent.createChooser(shareIntent, "Share Record"));
        } catch (Exception e) {
            Log.e(TAG, "Error sharing record: " + e.getMessage());
            Toast.makeText(this, "Cannot share file", Toast.LENGTH_SHORT).show();
        }
    }

    private void confirmDeleteRecord(HealthRecord record) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Record")
                .setMessage("Are you sure you want to delete this record?")
                .setPositiveButton("Yes", (dialog, which) -> deleteRecord(record))
                .setNegativeButton("No", null)
                .show();
    }

    private void deleteRecord(HealthRecord record) {
        boolean success = dbHelper.deleteHealthRecord(record.getId());
        if (success) {
            loadHealthRecords();
            Toast.makeText(this, "Record deleted", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Failed to delete record", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("Range")
    private String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            try (Cursor cursor = getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            }
        }
        if (result == null) {
            result = uri.getLastPathSegment();
        }
        return result;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            Uri originalUri = data.getData();
            if (originalUri != null) {
                Uri savedUri = saveFileToInternalStorage(originalUri);  // Save to internal
                if (savedUri != null) {
                    selectedFileUri = savedUri;  // Save this for your DB
                    tvSelectedFile.setText("File selected: " + getFileName(originalUri));
                } else {
                    Toast.makeText(this, "Failed to save file", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private Uri saveFileToInternalStorage(Uri sourceUri) {
        try {
            String fileName = getFileName(sourceUri);
            File dir = new File(getFilesDir(), "health_records");
            if (!dir.exists()) dir.mkdirs();

            File destFile = new File(dir, fileName);
            try (InputStream in = getContentResolver().openInputStream(sourceUri);
                 OutputStream out = new FileOutputStream(destFile)) {
                byte[] buffer = new byte[1024];
                int len;
                while ((len = in.read(buffer)) > 0) {
                    out.write(buffer, 0, len);
                }
            }

            return FileProvider.getUriForFile(this,
                    "com.example.mediconnectnew.fileprovider", destFile);
        } catch (Exception e) {
            Log.e("FileSave", "Error: " + e.getMessage());
            return null;
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Storage permission granted", Toast.LENGTH_SHORT).show();
                selectFile();
            } else {
                Toast.makeText(this, "Storage permission required for file operations", Toast.LENGTH_LONG).show();
            }
        } else if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Camera permission granted", Toast.LENGTH_SHORT).show();
                takePhoto();
            } else {
                Toast.makeText(this, "Camera permission required for taking photos", Toast.LENGTH_LONG).show();
            }
        }
    }
}
