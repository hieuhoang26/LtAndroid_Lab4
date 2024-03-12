package com.example.lab2;

public class Contact {
    private int id;
    private String name;
    private String phoneNumber;

    private String image;
    private boolean status;

    public Contact(int id, String name, String phoneNumber, String image, boolean status) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.image = image;
        this.status = status;
    }
    public Contact(int id, String name, String phoneNumber, String image) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
