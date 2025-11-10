# MediConnect



## 📝 Description

MediConnect is a native Android application designed to facilitate seamless communication between patients and medical professionals. This platform streamlines healthcare interactions, providing a convenient and efficient way for patients to connect with their doctors and other healthcare providers. Key features include robust testing capabilities to ensure optimal performance and reliability, making MediConnect a dependable solution for modern healthcare communication.

## ✨ Features

- 🧪 Testing


## 🛠️ Tech Stack

- 🤖 Android (Native)


## 📁 Project Structure

```
.
├── App.apk
├── Project Report.pdf
└── Source Code
    ├── app
    │   ├── build.gradle.kts
    │   ├── google-services.json
    │   ├── proguard-rules.pro
    │   └── src
    │       ├── androidTest
    │       │   └── java
    │       │       └── com
    │       │           └── example
    │       │               └── mediconnectnew
    │       │                   └── ExampleInstrumentedTest.java
    │       ├── main
    │       │   ├── AndroidManifest.xml
    │       │   ├── assets
    │       │   │   └── hospitals.json
    │       │   ├── java
    │       │   │   └── com
    │       │   │       └── example
    │       │   │           └── mediconnectnew
    │       │   │               ├── AIChatActivity.java
    │       │   │               ├── AIService.java
    │       │   │               ├── ChatAdapter.java
    │       │   │               ├── ChatMessage.java
    │       │   │               ├── ConsultDoctorActivity.java
    │       │   │               ├── ConversationActivity.java
    │       │   │               ├── ConversationAdapter.java
    │       │   │               ├── DashboardActivity.java
    │       │   │               ├── DatabaseHelper.java
    │       │   │               ├── DoctorAdapter.java
    │       │   │               ├── HealthRecord.java
    │       │   │               ├── HealthRecordAdapter.java
    │       │   │               ├── HealthRecordsActivity.java
    │       │   │               ├── Hospital.java
    │       │   │               ├── HospitalActivity.java
    │       │   │               ├── HospitalAdapter.java
    │       │   │               ├── LoginActivity.java
    │       │   │               ├── Message.java
    │       │   │               ├── MessageConversation.java
    │       │   │               ├── MessageConversationAdapter.java
    │       │   │               ├── MessageHistoryActivity.java
    │       │   │               ├── NotificationAdapter.java
    │       │   │               ├── NotificationItem.java
    │       │   │               ├── NotificationsActivity.java
    │       │   │               ├── ProfileActivity.java
    │       │   │               ├── RegisterActivity.java
    │       │   │               ├── Reminder.java
    │       │   │               ├── ReminderActivity.java
    │       │   │               ├── ReminderAdapter.java
    │       │   │               ├── ReminderReceiver.java
    │       │   │               ├── SplashActivity.java
    │       │   │               ├── SymptomCheckerActivity.java
    │       │   │               ├── User.java
    │       │   │               └── WelcomeActivity.java
    │       │   └── res
    │       │       ├── drawable
    │       │       │   ├── about.png
    │       │       │   ├── ai_message_background.xml
    │       │       │   ├── app_logo.png
    │       │       │   ├── card_background.xml
    │       │       │   ├── circle_background.xml
    │       │       │   ├── consult_doctor.png
    │       │       │   ├── distance_background.xml
    │       │       │   ├── gov_hospitals.png
    │       │       │   ├── gradient_background.xml
    │       │       │   ├── health_report.png
    │       │       │   ├── ic_add.xml
    │       │       │   ├── ic_ai_agent.png
    │       │       │   ├── ic_alarm.xml
    │       │       │   ├── ic_arrow_back.xml
    │       │       │   ├── ic_arrow_forward.xml
    │       │       │   ├── ic_attach_file.xml
    │       │       │   ├── ic_clear_all.xml
    │       │       │   ├── ic_close.xml
    │       │       │   ├── ic_edit.xml
    │       │       │   ├── ic_email.xml
    │       │       │   ├── ic_file_document.xml
    │       │       │   ├── ic_file_image.xml
    │       │       │   ├── ic_file_pdf.xml
    │       │       │   ├── ic_folder.xml
    │       │       │   ├── ic_hospital.xml
    │       │       │   ├── ic_launcher_background.xml
    │       │       │   ├── ic_launcher_foreground.xml
    │       │       │   ├── ic_logout.xml
    │       │       │   ├── ic_medical_cross.xml
    │       │       │   ├── ic_message.xml
    │       │       │   ├── ic_message_empty.xml
    │       │       │   ├── ic_more_vert.xml
    │       │       │   ├── ic_notifications.xml
    │       │       │   ├── ic_notifications_off.xml
    │       │       │   ├── ic_person.png
    │       │       │   ├── ic_person_placeholder.xml
    │       │       │   ├── ic_phone.xml
    │       │       │   ├── ic_reminders.png
    │       │       │   ├── ic_robot.xml
    │       │       │   ├── ic_search.xml
    │       │       │   ├── ic_send.xml
    │       │       │   ├── info.png
    │       │       │   ├── message_input_background.xml
    │       │       │   ├── received_message_background.xml
    │       │       │   ├── send_button_background.xml
    │       │       │   ├── sent_message_background.xml
    │       │       │   ├── splash_background.xml
    │       │       │   ├── status_background.xml
    │       │       │   ├── symptom_checker.png
    │       │       │   ├── tag_background.xml
    │       │       │   ├── unread_count_background.xml
    │       │       │   ├── unread_indicator.xml
    │       │       │   └── user_message_background.xml
    │       │       ├── layout
    │       │       │   ├── activity_ai_chat.xml
    │       │       │   ├── activity_chat.xml
    │       │       │   ├── activity_consult_doctor.xml
    │       │       │   ├── activity_conversation.xml
    │       │       │   ├── activity_dashboard.xml
    │       │       │   ├── activity_health_records.xml
    │       │       │   ├── activity_hospital.xml
    │       │       │   ├── activity_login.xml
    │       │       │   ├── activity_message_history.xml
    │       │       │   ├── activity_notifications.xml
    │       │       │   ├── activity_profile.xml
    │       │       │   ├── activity_register.xml
    │       │       │   ├── activity_reminder.xml
    │       │       │   ├── activity_splash.xml
    │       │       │   ├── activity_symptom_checker.xml
    │       │       │   ├── activity_welcome.xml
    │       │       │   ├── item_chat_message.xml
    │       │       │   ├── item_doctor.xml
    │       │       │   ├── item_health_record.xml
    │       │       │   ├── item_hospital.xml
    │       │       │   ├── item_message_conversation.xml
    │       │       │   ├── item_message_received.xml
    │       │       │   ├── item_message_sent.xml
    │       │       │   ├── item_notification.xml
    │       │       │   └── item_reminder.xml
    │       │       ├── menu
    │       │       │   └── record_options_menu.xml
    │       │       ├── raw
    │       │       │   ├── background.gif
    │       │       │   └── notification_sound.mp3
    │       │       ├── values
    │       │       │   ├── colors.xml
    │       │       │   ├── strings.xml
    │       │       │   ├── styles.xml
    │       │       │   └── themes.xml
    │       │       ├── values-night
    │       │       │   └── themes.xml
    │       │       └── xml
    │       │           ├── backup_rules.xml
    │       │           ├── data_extraction_rules.xml
    │       │           └── file_paths.xml
    │       └── test
    │           └── java
    │               └── com
    │                   └── example
    │                       └── mediconnectnew
    │                           └── ExampleUnitTest.java
    ├── build.gradle.kts
    ├── gradle
    │   ├── libs.versions.toml
    │   └── wrapper
    │       ├── gradle-wrapper.jar
    │       └── gradle-wrapper.properties
    ├── gradle.properties
    ├── gradlew
    ├── gradlew.bat
    └── settings.gradle.kts
```

## 🛠️ Development Setup

### Native Android Setup
1. Open project in Android Studio
2. Sync Gradle and build project
3. Run on emulator or connected device


Please ensure your code follows the project's style guidelines and includes tests where applicable.

