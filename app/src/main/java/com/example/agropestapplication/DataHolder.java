package com.example.agropestapplication;

public class DataHolder
{
    String name,contact,emil,userImage,password;

    public DataHolder(String name, String emil, String password,String userImage,String contact ) {
        this.name = name;
        this.contact = contact;
        this.emil = emil;
        this.userImage = userImage;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getEmil() {
        return emil;
    }

    public void setEmil(String emil) {
        this.emil = emil;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

