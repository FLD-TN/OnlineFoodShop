package com.sinhvien.onlinefoodshop.Model;

public class UserModel {
    private String email;
    private String fullName;
    private String phoneNumber;
    private String password;

    // Constructor với 4 tham số
    public UserModel(String email, String fullName, String phoneNumber, String password) {
        this.email = email;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.password = password;
    }

    // Getter và Setter
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}