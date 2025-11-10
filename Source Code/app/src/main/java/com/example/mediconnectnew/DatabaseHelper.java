package com.example.mediconnectnew;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "DatabaseHelper";
    private static final String DATABASE_NAME = "MediConnect.db";
    private static final int DATABASE_VERSION = 1;

    // Users table
    private static final String TABLE_USERS = "users";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PHONE = "phone";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_IS_DOCTOR = "is_doctor";
    private static final String COLUMN_SPECIALIZATION = "specialization";
    private static final String COLUMN_IS_ONLINE = "is_online";

    // Reminders table
    private static final String TABLE_REMINDERS = "reminders";
    private static final String COLUMN_REMINDER_ID = "reminder_id";
    private static final String COLUMN_USER_EMAIL = "user_email";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_DATE_TIME = "date_time";
    private static final String COLUMN_IS_COMPLETED = "is_completed";

    // Health Records table
    private static final String TABLE_HEALTH_RECORDS = "health_records";
    private static final String COLUMN_RECORD_ID = "id";
    private static final String COLUMN_FILE_PATH = "file_path";
    private static final String COLUMN_DATE_CREATED = "date_created";
    private static final String COLUMN_FILE_TYPE = "file_type";

    // Notifications table
    private static final String TABLE_NOTIFICATIONS = "notifications";
    private static final String COLUMN_NOTIFICATION_ID = "id";
    private static final String COLUMN_MESSAGE = "message";
    private static final String COLUMN_TYPE = "type";
    private static final String COLUMN_TIMESTAMP = "timestamp";
    private static final String COLUMN_IS_READ = "is_read";

    // Message Conversations table
    private static final String TABLE_CONVERSATIONS = "message_conversations";
    private static final String COLUMN_CONVERSATION_ID = "id";
    private static final String COLUMN_CONTACT_NAME = "contact_name";
    private static final String COLUMN_CONTACT_TYPE = "contact_type";
    private static final String COLUMN_LAST_MESSAGE = "last_message";
    private static final String COLUMN_LAST_MESSAGE_TIME = "last_message_time";
    private static final String COLUMN_UNREAD_COUNT = "unread_count";
    private static final String COLUMN_CONTACT_SPECIALIZATION = "contact_specialization";

    private static final String TABLE_MESSAGES = "messages";
    private static final String COLUMN_MESSAGE_ID = "id";

    private static final String COLUMN_SENDER_EMAIL = "sender_email";
    private static final String COLUMN_RECEIVER_EMAIL = "receiver_email";
    private static final String COLUMN_MESSAGE_TEXT = "message_text";
    private static final String COLUMN_MESSAGE_TIMESTAMP = "timestamp";
    private static final String COLUMN_MESSAGE_IS_READ = "is_read";
    private static final String COLUMN_MESSAGE_TYPE = "message_type";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            // Create users table
            String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_NAME + " TEXT NOT NULL,"
                    + COLUMN_EMAIL + " TEXT UNIQUE NOT NULL,"
                    + COLUMN_PHONE + " TEXT NOT NULL,"
                    + COLUMN_PASSWORD + " TEXT NOT NULL,"
                    + COLUMN_IS_DOCTOR + " INTEGER DEFAULT 0,"
                    + COLUMN_SPECIALIZATION + " TEXT,"
                    + COLUMN_IS_ONLINE + " INTEGER DEFAULT 0"
                    + ")";
            db.execSQL(CREATE_USERS_TABLE);

            // Create reminders table
            String CREATE_REMINDERS_TABLE = "CREATE TABLE " + TABLE_REMINDERS + "("
                    + COLUMN_REMINDER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_USER_EMAIL + " TEXT NOT NULL,"
                    + COLUMN_TITLE + " TEXT NOT NULL,"
                    + COLUMN_DESCRIPTION + " TEXT,"
                    + COLUMN_DATE_TIME + " TEXT NOT NULL,"
                    + COLUMN_IS_COMPLETED + " INTEGER DEFAULT 0"
                    + ")";
            db.execSQL(CREATE_REMINDERS_TABLE);

            // Create health_records table
            String CREATE_HEALTH_RECORDS_TABLE = "CREATE TABLE " + TABLE_HEALTH_RECORDS + "("
                    + COLUMN_RECORD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_USER_EMAIL + " TEXT NOT NULL,"
                    + COLUMN_TITLE + " TEXT NOT NULL,"
                    + COLUMN_DESCRIPTION + " TEXT,"
                    + COLUMN_FILE_PATH + " TEXT NOT NULL,"
                    + COLUMN_DATE_CREATED + " TEXT NOT NULL,"
                    + COLUMN_FILE_TYPE + " TEXT"
                    + ")";
            db.execSQL(CREATE_HEALTH_RECORDS_TABLE);

            // Create notifications table
            String CREATE_NOTIFICATIONS_TABLE = "CREATE TABLE " + TABLE_NOTIFICATIONS + "("
                    + COLUMN_NOTIFICATION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_USER_EMAIL + " TEXT NOT NULL,"
                    + COLUMN_TITLE + " TEXT NOT NULL,"
                    + COLUMN_MESSAGE + " TEXT NOT NULL,"
                    + COLUMN_TYPE + " TEXT NOT NULL,"
                    + COLUMN_TIMESTAMP + " TEXT NOT NULL,"
                    + COLUMN_IS_READ + " INTEGER DEFAULT 0"
                    + ")";
            db.execSQL(CREATE_NOTIFICATIONS_TABLE);

            // Create message_conversations table
            String CREATE_CONVERSATIONS_TABLE = "CREATE TABLE " + TABLE_CONVERSATIONS + "("
                    + COLUMN_CONVERSATION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_USER_EMAIL + " TEXT NOT NULL,"
                    + COLUMN_CONTACT_NAME + " TEXT NOT NULL,"
                    + COLUMN_CONTACT_TYPE + " TEXT NOT NULL,"
                    + COLUMN_LAST_MESSAGE + " TEXT,"
                    + COLUMN_LAST_MESSAGE_TIME + " TEXT,"
                    + COLUMN_UNREAD_COUNT + " INTEGER DEFAULT 0,"
                    + COLUMN_CONTACT_SPECIALIZATION + " TEXT"
                    + ")";
            db.execSQL(CREATE_CONVERSATIONS_TABLE);
        } catch (SQLiteException e) {
            Log.e(TAG, "Error creating database tables: " + e.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_REMINDERS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_HEALTH_RECORDS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTIFICATIONS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONVERSATIONS);
            onCreate(db);
        } catch (SQLiteException e) {
            Log.e(TAG, "Error upgrading database: " + e.getMessage());
        }
    }

    public long registerUser(User user) {
        SQLiteDatabase db = null;
        long result = -1;

        try {
            db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(COLUMN_NAME, user.getName());
            values.put(COLUMN_EMAIL, user.getEmail());
            values.put(COLUMN_PHONE, user.getPhone());
            values.put(COLUMN_PASSWORD, user.getPassword());
            values.put(COLUMN_IS_DOCTOR, user.isDoctor() ? 1 : 0);
            values.put(COLUMN_SPECIALIZATION, user.getSpecialization());
            values.put(COLUMN_IS_ONLINE, 0);

            result = db.insert(TABLE_USERS, null, values);
        } catch (SQLiteException e) {
            Log.e(TAG, "Error registering user: " + e.getMessage());
        } finally {
            if (db != null && db.isOpen()) {
                db.close();
            }
        }
        return result;
    }

    @SuppressLint("Range")
    public User loginUser(String email, String password) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        User user = null;

        try {
            db = this.getReadableDatabase();
            String[] columns = {COLUMN_ID, COLUMN_NAME, COLUMN_EMAIL, COLUMN_PHONE,
                               COLUMN_IS_DOCTOR, COLUMN_SPECIALIZATION};
            String selection = COLUMN_EMAIL + " = ? AND " + COLUMN_PASSWORD + " = ?";
            String[] selectionArgs = {email, password};

            cursor = db.query(TABLE_USERS, columns, selection, selectionArgs,
                            null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                user = new User();
                user.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
                user.setName(cursor.getString(cursor.getColumnIndex(COLUMN_NAME)));
                user.setEmail(cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL)));
                user.setPhone(cursor.getString(cursor.getColumnIndex(COLUMN_PHONE)));
                user.setDoctor(cursor.getInt(cursor.getColumnIndex(COLUMN_IS_DOCTOR)) == 1);
                user.setSpecialization(cursor.getString(cursor.getColumnIndex(COLUMN_SPECIALIZATION)));
            }
        } catch (SQLiteException e) {
            Log.e(TAG, "Error logging in user: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null && db.isOpen()) {
                db.close();
            }
        }
        return user;
    }

    @SuppressLint("Range")
    public List<User> getAllDoctors() {
        List<User> doctors = new ArrayList<>();
        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = this.getReadableDatabase();
            String[] columns = {COLUMN_ID, COLUMN_NAME, COLUMN_EMAIL, COLUMN_PHONE,
                               COLUMN_SPECIALIZATION, COLUMN_IS_ONLINE};
            String selection = COLUMN_IS_DOCTOR + " = ?";
            String[] selectionArgs = {"1"};

            cursor = db.query(TABLE_USERS, columns, selection, selectionArgs,
                            null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    User doctor = new User();
                    doctor.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
                    doctor.setName(cursor.getString(cursor.getColumnIndex(COLUMN_NAME)));
                    doctor.setEmail(cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL)));
                    doctor.setPhone(cursor.getString(cursor.getColumnIndex(COLUMN_PHONE)));
                    doctor.setSpecialization(cursor.getString(cursor.getColumnIndex(COLUMN_SPECIALIZATION)));
                    doctor.setOnline(cursor.getInt(cursor.getColumnIndex(COLUMN_IS_ONLINE)) == 1);
                    doctor.setDoctor(true);
                    doctors.add(doctor);
                } while (cursor.moveToNext());
            }
        } catch (SQLiteException e) {
            Log.e(TAG, "Error getting all doctors: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null && db.isOpen()) {
                db.close();
            }
        }
        return doctors;
    }

    public long addReminder(Reminder reminder) {
        SQLiteDatabase db = null;
        long result = -1;

        try {
            db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(COLUMN_USER_EMAIL, reminder.getUserEmail());
            values.put(COLUMN_TITLE, reminder.getTitle());
            values.put(COLUMN_DESCRIPTION, reminder.getDescription());
            values.put(COLUMN_DATE_TIME, reminder.getDateTime());
            values.put(COLUMN_IS_COMPLETED, reminder.isCompleted() ? 1 : 0);

            result = db.insert(TABLE_REMINDERS, null, values);
        } catch (SQLiteException e) {
            Log.e(TAG, "Error adding reminder: " + e.getMessage());
        } finally {
            if (db != null && db.isOpen()) {
                db.close();
            }
        }
        return result;
    }

    @SuppressLint("Range")
    public List<Reminder> getUserReminders(String userEmail) {
        List<Reminder> reminders = new ArrayList<>();
        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = this.getReadableDatabase();
            String[] columns = {COLUMN_REMINDER_ID, COLUMN_TITLE, COLUMN_DESCRIPTION,
                               COLUMN_DATE_TIME, COLUMN_IS_COMPLETED};
            String selection = COLUMN_USER_EMAIL + " = ? AND " + COLUMN_IS_COMPLETED + " = ?";
            String[] selectionArgs = {userEmail, "0"};

            cursor = db.query(TABLE_REMINDERS, columns, selection, selectionArgs,
                            null, null, COLUMN_DATE_TIME + " ASC");

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    Reminder reminder = new Reminder();
                    reminder.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_REMINDER_ID)));
                    reminder.setUserEmail(userEmail);
                    reminder.setTitle(cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)));
                    reminder.setDescription(cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION)));
                    reminder.setDateTime(cursor.getString(cursor.getColumnIndex(COLUMN_DATE_TIME)));
                    reminder.setCompleted(cursor.getInt(cursor.getColumnIndex(COLUMN_IS_COMPLETED)) == 1);
                    reminders.add(reminder);
                } while (cursor.moveToNext());
            }
        } catch (SQLiteException e) {
            Log.e(TAG, "Error getting user reminders: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null && db.isOpen()) {
                db.close();
            }
        }
        return reminders;
    }

    public boolean completeReminder(int reminderId) {
        SQLiteDatabase db = null;
        boolean success = false;

        try {
            db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(COLUMN_IS_COMPLETED, 1);

            int rowsAffected = db.update(TABLE_REMINDERS, values, COLUMN_REMINDER_ID + " = ?",
                     new String[]{String.valueOf(reminderId)});
            success = rowsAffected > 0;
        } catch (SQLiteException e) {
            Log.e(TAG, "Error completing reminder: " + e.getMessage());
        } finally {
            if (db != null && db.isOpen()) {
                db.close();
            }
        }
        return success;
    }

    public boolean deleteReminder(int reminderId) {
        SQLiteDatabase db = null;
        boolean success = false;

        try {
            db = this.getWritableDatabase();
            int rowsAffected = db.delete(TABLE_REMINDERS, COLUMN_REMINDER_ID + " = ?",
                     new String[]{String.valueOf(reminderId)});
            success = rowsAffected > 0;
        } catch (SQLiteException e) {
            Log.e(TAG, "Error deleting reminder: " + e.getMessage());
        } finally {
            if (db != null && db.isOpen()) {
                db.close();
            }
        }
        return success;
    }

    // Health Records methods
    public long addHealthRecord(HealthRecord record) {
        SQLiteDatabase db = null;
        long result = -1;

        try {
            db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(COLUMN_USER_EMAIL, record.getUserEmail());
            values.put(COLUMN_TITLE, record.getTitle());
            values.put(COLUMN_DESCRIPTION, record.getDescription());
            values.put(COLUMN_FILE_PATH, record.getFilePath());
            values.put(COLUMN_DATE_CREATED, record.getDateCreated());
            values.put(COLUMN_FILE_TYPE, record.getFileType());

            result = db.insert(TABLE_HEALTH_RECORDS, null, values);
        } catch (SQLiteException e) {
            Log.e(TAG, "Error adding health record: " + e.getMessage());
        } finally {
            if (db != null && db.isOpen()) {
                db.close();
            }
        }
        return result;
    }

    @SuppressLint("Range")
    public List<HealthRecord> getUserHealthRecords(String userEmail) {
        List<HealthRecord> records = new ArrayList<>();
        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = this.getReadableDatabase();

            cursor = db.query(TABLE_HEALTH_RECORDS, null, COLUMN_USER_EMAIL + " = ?",
                           new String[]{userEmail}, null, null, COLUMN_DATE_CREATED + " DESC");

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    HealthRecord record = new HealthRecord();
                    record.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_RECORD_ID)));
                    record.setUserEmail(cursor.getString(cursor.getColumnIndex(COLUMN_USER_EMAIL)));
                    record.setTitle(cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)));
                    record.setDescription(cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION)));
                    record.setFilePath(cursor.getString(cursor.getColumnIndex(COLUMN_FILE_PATH)));
                    record.setDateCreated(cursor.getString(cursor.getColumnIndex(COLUMN_DATE_CREATED)));
                    record.setFileType(cursor.getString(cursor.getColumnIndex(COLUMN_FILE_TYPE)));
                    records.add(record);
                } while (cursor.moveToNext());
            }
        } catch (SQLiteException e) {
            Log.e(TAG, "Error getting user health records: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null && db.isOpen()) {
                db.close();
            }
        }
        return records;
    }

    public boolean deleteHealthRecord(int recordId) {
        SQLiteDatabase db = null;
        boolean success = false;

        try {
            db = this.getWritableDatabase();
            int rowsAffected = db.delete(TABLE_HEALTH_RECORDS, COLUMN_RECORD_ID + " = ?",
                     new String[]{String.valueOf(recordId)});
            success = rowsAffected > 0;
        } catch (SQLiteException e) {
            Log.e(TAG, "Error deleting health record: " + e.getMessage());
        } finally {
            if (db != null && db.isOpen()) {
                db.close();
            }
        }
        return success;
    }

    // Notifications methods
    public long addNotification(NotificationItem notification) {
        SQLiteDatabase db = null;
        long result = -1;

        try {
            db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(COLUMN_USER_EMAIL, notification.getUserEmail());
            values.put(COLUMN_TITLE, notification.getTitle());
            values.put(COLUMN_MESSAGE, notification.getMessage());
            values.put(COLUMN_TYPE, notification.getType());
            values.put(COLUMN_TIMESTAMP, notification.getTimestamp());
            values.put(COLUMN_IS_READ, notification.isRead() ? 1 : 0);

            result = db.insert(TABLE_NOTIFICATIONS, null, values);
        } catch (SQLiteException e) {
            Log.e(TAG, "Error adding notification: " + e.getMessage());
        } finally {
            if (db != null && db.isOpen()) {
                db.close();
            }
        }
        return result;
    }

    @SuppressLint("Range")
    public List<NotificationItem> getUserNotifications(String userEmail, String type) {
        List<NotificationItem> notifications = new ArrayList<>();
        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = this.getReadableDatabase();

            String selection = COLUMN_USER_EMAIL + " = ?";
            String[] selectionArgs = {userEmail};

            if (!type.equals("all")) {
                selection += " AND " + COLUMN_TYPE + " = ?";
                selectionArgs = new String[]{userEmail, type};
            }

            cursor = db.query(TABLE_NOTIFICATIONS, null, selection, selectionArgs,
                           null, null, COLUMN_TIMESTAMP + " DESC");

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    NotificationItem notification = new NotificationItem();
                    notification.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_NOTIFICATION_ID)));
                    notification.setUserEmail(cursor.getString(cursor.getColumnIndex(COLUMN_USER_EMAIL)));
                    notification.setTitle(cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)));
                    notification.setMessage(cursor.getString(cursor.getColumnIndex(COLUMN_MESSAGE)));
                    notification.setType(cursor.getString(cursor.getColumnIndex(COLUMN_TYPE)));
                    notification.setTimestamp(cursor.getString(cursor.getColumnIndex(COLUMN_TIMESTAMP)));
                    notification.setRead(cursor.getInt(cursor.getColumnIndex(COLUMN_IS_READ)) == 1);
                    notifications.add(notification);
                } while (cursor.moveToNext());
            }
        } catch (SQLiteException e) {
            Log.e(TAG, "Error getting user notifications: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null && db.isOpen()) {
                db.close();
            }
        }
        return notifications;
    }

    public boolean markNotificationAsRead(int notificationId) {
        SQLiteDatabase db = null;
        boolean success = false;

        try {
            db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(COLUMN_IS_READ, 1);

            int rowsAffected = db.update(TABLE_NOTIFICATIONS, values, COLUMN_NOTIFICATION_ID + " = ?",
                     new String[]{String.valueOf(notificationId)});
            success = rowsAffected > 0;
        } catch (SQLiteException e) {
            Log.e(TAG, "Error marking notification as read: " + e.getMessage());
        } finally {
            if (db != null && db.isOpen()) {
                db.close();
            }
        }
        return success;
    }

    public boolean clearUserNotifications(String userEmail) {
        SQLiteDatabase db = null;
        boolean success = false;

        try {
            db = this.getWritableDatabase();
            int rowsAffected = db.delete(TABLE_NOTIFICATIONS, COLUMN_USER_EMAIL + " = ?",
                     new String[]{userEmail});
            success = rowsAffected > 0;
        } catch (SQLiteException e) {
            Log.e(TAG, "Error clearing user notifications: " + e.getMessage());
        } finally {
            if (db != null && db.isOpen()) {
                db.close();
            }
        }
        return success;
    }

    // Message Conversations methods
    @SuppressLint("Range")
    public List<MessageConversation> getUserConversations(String userEmail, String type) {
        List<MessageConversation> conversations = new ArrayList<>();
        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = this.getReadableDatabase();

            String selection = COLUMN_USER_EMAIL + " = ? AND " + COLUMN_CONTACT_TYPE + " = ?";
            String[] selectionArgs = {userEmail, type};

            cursor = db.query(TABLE_CONVERSATIONS, null, selection, selectionArgs,
                           null, null, COLUMN_LAST_MESSAGE_TIME + " DESC");

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    MessageConversation conversation = new MessageConversation();
                    conversation.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_CONVERSATION_ID)));
                    conversation.setUserEmail(cursor.getString(cursor.getColumnIndex(COLUMN_USER_EMAIL)));
                    conversation.setContactName(cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_NAME)));
                    conversation.setContactType(cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_TYPE)));
                    conversation.setLastMessage(cursor.getString(cursor.getColumnIndex(COLUMN_LAST_MESSAGE)));
                    conversation.setLastMessageTime(cursor.getString(cursor.getColumnIndex(COLUMN_LAST_MESSAGE_TIME)));
                    conversation.setUnreadCount(cursor.getInt(cursor.getColumnIndex(COLUMN_UNREAD_COUNT)));
                    conversation.setContactSpecialization(cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_SPECIALIZATION)));
                    conversations.add(conversation);
                } while (cursor.moveToNext());
            }
        } catch (SQLiteException e) {
            Log.e(TAG, "Error getting user conversations: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null && db.isOpen()) {
                db.close();
            }
        }
        return conversations;
    }

    @SuppressLint("Range")
    public List<MessageConversation> searchUserConversations(String userEmail, String type, String query) {
        List<MessageConversation> conversations = new ArrayList<>();
        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = this.getReadableDatabase();

            String selection = COLUMN_USER_EMAIL + " = ? AND " + COLUMN_CONTACT_TYPE + " = ? AND (" +
                             COLUMN_CONTACT_NAME + " LIKE ? OR " + COLUMN_LAST_MESSAGE + " LIKE ?)";
            String[] selectionArgs = {userEmail, type, "%" + query + "%", "%" + query + "%"};

            cursor = db.query(TABLE_CONVERSATIONS, null, selection, selectionArgs,
                           null, null, COLUMN_LAST_MESSAGE_TIME + " DESC");

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    MessageConversation conversation = new MessageConversation();
                    conversation.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_CONVERSATION_ID)));
                    conversation.setUserEmail(cursor.getString(cursor.getColumnIndex(COLUMN_USER_EMAIL)));
                    conversation.setContactName(cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_NAME)));
                    conversation.setContactType(cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_TYPE)));
                    conversation.setLastMessage(cursor.getString(cursor.getColumnIndex(COLUMN_LAST_MESSAGE)));
                    conversation.setLastMessageTime(cursor.getString(cursor.getColumnIndex(COLUMN_LAST_MESSAGE_TIME)));
                    conversation.setUnreadCount(cursor.getInt(cursor.getColumnIndex(COLUMN_UNREAD_COUNT)));
                    conversation.setContactSpecialization(cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_SPECIALIZATION)));
                    conversations.add(conversation);
                } while (cursor.moveToNext());
            }
        } catch (SQLiteException e) {
            Log.e(TAG, "Error searching user conversations: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null && db.isOpen()) {
                db.close();
            }
        }
        return conversations;
    }

    public long addMessage(Message message) {
        SQLiteDatabase db = null;
        long result = -1;

        try {
            db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(COLUMN_CONVERSATION_ID, message.getConversationId());
            values.put(COLUMN_SENDER_EMAIL, message.getSenderEmail());
            values.put(COLUMN_RECEIVER_EMAIL, message.getReceiverEmail());
            values.put(COLUMN_MESSAGE_TEXT, message.getMessageText());
            values.put(COLUMN_MESSAGE_TIMESTAMP, message.getTimestamp());
            values.put(COLUMN_MESSAGE_IS_READ, message.isRead() ? 1 : 0);
            values.put(COLUMN_MESSAGE_TYPE, message.getMessageType());

            result = db.insert(TABLE_MESSAGES, null, values);
        } catch (SQLiteException e) {
            Log.e(TAG, "Error adding message: " + e.getMessage());
        } finally {
            if (db != null && db.isOpen()) {
                db.close();
            }
        }
        return result;
    }


    @SuppressLint("Range")
    public MessageConversation getConversation(String userEmail, String contactEmail) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        MessageConversation conversation = null;

        try {
            db = this.getReadableDatabase();

            cursor = db.query(TABLE_CONVERSATIONS, null,
                    COLUMN_USER_EMAIL + " = ? AND " + COLUMN_CONTACT_NAME + " = ?",
                    new String[]{userEmail, contactEmail},
                    null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                conversation = new MessageConversation();
                conversation.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_CONVERSATION_ID)));
                conversation.setUserEmail(cursor.getString(cursor.getColumnIndex(COLUMN_USER_EMAIL)));
                conversation.setContactName(cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_NAME)));
                conversation.setContactType(cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_TYPE)));
                conversation.setLastMessage(cursor.getString(cursor.getColumnIndex(COLUMN_LAST_MESSAGE)));
                conversation.setLastMessageTime(cursor.getString(cursor.getColumnIndex(COLUMN_LAST_MESSAGE_TIME)));
                conversation.setUnreadCount(cursor.getInt(cursor.getColumnIndex(COLUMN_UNREAD_COUNT)));
                conversation.setContactSpecialization(cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_SPECIALIZATION)));
            }
        } catch (SQLiteException e) {
            Log.e(TAG, "Error getting conversation: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null && db.isOpen()) {
                db.close();
            }
        }
        return conversation;
    }

    public void addConversation(MessageConversation conversation) {
        SQLiteDatabase db = null;
        long result = -1;

        try {
            db = this.getWritableDatabase();
            ContentValues values = getContentValues(conversation);

            result = db.insert(TABLE_CONVERSATIONS, null, values);
        } catch (SQLiteException e) {
            Log.e(TAG, "Error adding conversation: " + e.getMessage());
        } finally {
            if (db != null && db.isOpen()) {
                db.close();
            }
        }
    }

    @NonNull
    private static ContentValues getContentValues(MessageConversation conversation) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_EMAIL, conversation.getUserEmail());
        values.put(COLUMN_CONTACT_NAME, conversation.getContactName());
        values.put(COLUMN_CONTACT_TYPE, conversation.getContactType());
        values.put(COLUMN_LAST_MESSAGE, conversation.getLastMessage());
        values.put(COLUMN_LAST_MESSAGE_TIME, conversation.getLastMessageTime());
        values.put(COLUMN_UNREAD_COUNT, conversation.getUnreadCount());
        values.put(COLUMN_CONTACT_SPECIALIZATION, conversation.getContactSpecialization());
        return values;
    }

    public void updateConversation(String userEmail, String contactEmail, String lastMessage, String timestamp) {
        SQLiteDatabase db = null;

        try {
            db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(COLUMN_LAST_MESSAGE, lastMessage);
            values.put(COLUMN_LAST_MESSAGE_TIME, timestamp);

            db.update(TABLE_CONVERSATIONS, values,
                    COLUMN_USER_EMAIL + " = ? AND " + COLUMN_CONTACT_NAME + " = ?",
                    new String[]{userEmail, contactEmail});
        } catch (SQLiteException e) {
            Log.e(TAG, "Error updating conversation: " + e.getMessage());
        } finally {
            if (db != null && db.isOpen()) {
                db.close();
            }
        }
    }

    public void incrementUnreadCount(String userEmail, String contactEmail) {
        SQLiteDatabase db = null;

        try {
            db = this.getWritableDatabase();
            db.execSQL("UPDATE " + TABLE_CONVERSATIONS +
                            " SET " + COLUMN_UNREAD_COUNT + " = " + COLUMN_UNREAD_COUNT + " + 1" +
                            " WHERE " + COLUMN_USER_EMAIL + " = ? AND " + COLUMN_CONTACT_NAME + " = ?",
                    new String[]{userEmail, contactEmail});
        } catch (SQLiteException e) {
            Log.e(TAG, "Error incrementing unread count: " + e.getMessage());
        } finally {
            if (db != null && db.isOpen()) {
                db.close();
            }
        }
    }

    @SuppressLint("Range")
    public List<Message> getConversationMessages(String conversationId) {
        List<Message> messages = new ArrayList<>();
        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = this.getReadableDatabase();

            cursor = db.query(TABLE_MESSAGES, null,
                    COLUMN_CONVERSATION_ID + " = ?",
                    new String[]{conversationId},
                    null, null,
                    COLUMN_MESSAGE_TIMESTAMP + " ASC");

            if (cursor.moveToFirst()) {
                do {
                    Message message = new Message();
                    message.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_MESSAGE_ID)));
                    message.setConversationId(cursor.getString(cursor.getColumnIndex(COLUMN_CONVERSATION_ID)));
                    message.setSenderEmail(cursor.getString(cursor.getColumnIndex(COLUMN_SENDER_EMAIL)));
                    message.setReceiverEmail(cursor.getString(cursor.getColumnIndex(COLUMN_RECEIVER_EMAIL)));
                    message.setMessageText(cursor.getString(cursor.getColumnIndex(COLUMN_MESSAGE_TEXT)));
                    message.setTimestamp(cursor.getString(cursor.getColumnIndex(COLUMN_MESSAGE_TIMESTAMP)));
                    message.setRead(cursor.getInt(cursor.getColumnIndex(COLUMN_MESSAGE_IS_READ)) == 1);
                    message.setMessageType(cursor.getString(cursor.getColumnIndex(COLUMN_MESSAGE_TYPE)));

                    // Get sender name
                    String senderName = getUserName(message.getSenderEmail());
                    message.setSenderName(senderName);

                    messages.add(message);
                } while (cursor.moveToNext());
            }
        } catch (SQLiteException e) {
            Log.e(TAG, "Error getting conversation messages: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null && db.isOpen()) {
                db.close();
            }
        }
        return messages;
    }

    @SuppressLint("Range")
    private String getUserName(String email) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        String name = "User";

        try {
            db = this.getReadableDatabase();
            cursor = db.query(TABLE_USERS, new String[]{COLUMN_NAME},
                    COLUMN_EMAIL + " = ?", new String[]{email},
                    null, null, null);

            if (cursor.moveToFirst()) {
                name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
            }
        } catch (SQLiteException e) {
            Log.e(TAG, "Error getting user name: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null && db.isOpen()) {
                db.close();
            }
        }
        return name;
    }

    public void markMessagesAsRead(String conversationId, String currentUserEmail) {
        SQLiteDatabase db = null;

        try {
            db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(COLUMN_MESSAGE_IS_READ, 1);

            db.update(TABLE_MESSAGES, values,
                    COLUMN_CONVERSATION_ID + " = ? AND " + COLUMN_RECEIVER_EMAIL + " = ?",
                    new String[]{conversationId, currentUserEmail});
        } catch (SQLiteException e) {
            Log.e(TAG, "Error marking messages as read: " + e.getMessage());
        } finally {
            if (db != null && db.isOpen()) {
                db.close();
            }
        }
    }
}

