package com.party.technologies.nineteen_ninety_nine.data.user;

import java.util.ArrayList;

public class User {

    private String phoneNumber;
    private String fullName;
    private String email;
    private String bio;

    public String getInstagramUserName() {
        return instagramUserName;
    }

    public void setInstagramUserName(String instagramUserName) {
        this.instagramUserName = instagramUserName;
    }

    public ArrayList<String> getInstagramPhotoURLs() {
        return instagramPhotoURLs;
    }

    public void setInstagramPhotoURLs(ArrayList<String> instagramPhotoURLs) {
        this.instagramPhotoURLs = instagramPhotoURLs;
    }

    private String instagramUserName;
    private ArrayList<String> instagramPhotoURLs;

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    private String UID;
    private long accountCreatedTime;
    private long lastLoginTime;

    public User() {
        this.accountCreatedTime = System.currentTimeMillis();
    }

    public User(String UID, String phoneNumber) {
        this.UID = UID;
        this.phoneNumber = phoneNumber;
        instagramPhotoURLs = new ArrayList<String>();
        this.accountCreatedTime = System.currentTimeMillis();
    }

    public long getAccountCreatedTime() {
        return accountCreatedTime;
    }

    public long getLastLoginTime() {
        return lastLoginTime;
    }

    public void resetLoginTime() {
        lastLoginTime = System.currentTimeMillis();
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

}
