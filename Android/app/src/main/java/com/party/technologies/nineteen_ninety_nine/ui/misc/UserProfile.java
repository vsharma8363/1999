package com.party.technologies.nineteen_ninety_nine.ui.misc;

public class UserProfile {

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getInstagramUserName() {
        return instagramUserName;
    }

    public void setInstagramUserName(String instagramUserName) {
        this.instagramUserName = instagramUserName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    // variables for our coursename,
    // description,tracks and duration,imageId.
    private String fullName;
    private String phoneNumber;
    private String bio;
    private String age;
    private String instagramUserName;
    private String image;

    // constructor.
    public UserProfile(String fullName, String phoneNumber, String bio, String age, String instagramUserName, String image) {
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.bio = bio;
        this.age = age;
        this.instagramUserName = instagramUserName;
        this.image = image;
    }
}
