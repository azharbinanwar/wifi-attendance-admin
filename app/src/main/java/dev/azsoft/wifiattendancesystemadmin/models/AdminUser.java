package dev.azsoft.wifiattendancesystemadmin.models;

import com.google.gson.GsonBuilder;

import java.util.Objects;

public class AdminUser {
    String uid;
    String name;
    String email;
    String designation;
    String profileImage;
    long createdAt;

    public AdminUser() {
    }

    public AdminUser(String uid, String name, String email, String designation, String profileImage, long createdAt) {
        this.uid = uid;
        this.email = email;
        this.designation = designation;
        this.profileImage = profileImage;
        this.createdAt = createdAt;
    }

    public String getUid() {
        return uid;
    }

    public AdminUser setUid(String uid) {
        this.uid = uid;
        return this;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return new GsonBuilder().create().toJson(this, AdminUser.class);
    }

    static public AdminUser fromString(String value) {
        return new GsonBuilder().create().fromJson(value, AdminUser.class);
    }

}
