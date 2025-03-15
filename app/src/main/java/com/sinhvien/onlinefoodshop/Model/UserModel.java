package com.sinhvien.onlinefoodshop.Model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class UserModel implements Serializable {
    @SerializedName("email")
    private String email;

    @SerializedName("fullName")
    private String fullName;

    @SerializedName("phoneNumber")
    private String phoneNumber;

    @SerializedName("role")
    private String role;

    @SerializedName("password")
    private String password;

    public UserModel(String email, String fullName, String phoneNumber, String role,String password) {
        this.email = email;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.role = role;
        this.password = password;
    }

    public UserModel() {}

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}