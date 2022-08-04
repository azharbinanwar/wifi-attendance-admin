package dev.azsoft.wifiattendancesystemadmin.models;

import com.google.gson.GsonBuilder;

public class Attendance {
    String id;
    String uid;
    long workingMinutes;
    String networkName;
    String userName;
    String profilePhoto;

    public Attendance(String id, String uid, long workingMinutes,String networkName, String userName, String profilePhoto) {
        this.id = id;
        this.uid = uid;
        this.workingMinutes = workingMinutes;
        this.networkName = networkName;
        this.userName = userName;
        this.profilePhoto = profilePhoto;
    }

    public String getNetworkName() {
        return networkName;
    }

    public Attendance setNetworkName(String networkName) {
        this.networkName = networkName;
        return this;
    }

    public String getId() {
        return id;
    }

    public Attendance setId(String id) {
        this.id = id;
        return this;
    }

    public String getUid() {
        return uid;
    }

    public Attendance setUid(String uid) {
        this.uid = uid;
        return this;
    }

    public long getWorkingMinutes() {
        return workingMinutes;
    }

    public Attendance setWorkingMinutes(long workingMinutes) {
        this.workingMinutes = workingMinutes;
        return this;
    }

    public String getUserName() {
        return userName;
    }

    public Attendance setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public Attendance setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
        return this;
    }
}
