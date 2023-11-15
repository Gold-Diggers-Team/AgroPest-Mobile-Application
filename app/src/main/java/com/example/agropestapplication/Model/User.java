package com.example.agropestapplication.Model;

public class User {

    private String username;
    private String email;
    private String phoneNumber;
    private String imageUrl;
    private String password;

    // Add a default constructor
    public User() {
    }

    public User(String username, String email, String phoneNumber, String imageUrl,String password) {
        this.username = username;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.imageUrl = imageUrl;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getImageUrl() {
        return imageUrl;
    }

}
