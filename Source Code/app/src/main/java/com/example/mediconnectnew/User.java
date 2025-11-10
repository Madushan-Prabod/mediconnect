package com.example.mediconnectnew;

public class User {
    private int id;
    private String name;
    private String email;
    private String phone;
    private String password;
    private boolean isDoctor;
    private String specialization;
    private boolean isOnline;

    public User() {}

    public User(String name, String email, String phone, String password, boolean isDoctor, String specialization) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.isDoctor = isDoctor;
        this.specialization = specialization;
        this.isOnline = false;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public boolean isDoctor() { return isDoctor; }
    public void setDoctor(boolean doctor) { isDoctor = doctor; }

    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }

    public boolean isOnline() { return isOnline; }
    public void setOnline(boolean online) { isOnline = online; }
}
