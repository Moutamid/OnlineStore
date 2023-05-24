package com.moutamid.onlinestore.models;

import java.util.ArrayList;

public class ProductModel {
    String ID, userID, name, description, category;
    String thumbnail;
    double price;
    long stock, timeStamp;
    ArrayList<String> imagesList;
    public ProductModel() {
    }

    public ProductModel(String ID, String userID, String name, String description, String category, String thumbnail, double price, long stock, long timeStamp) {
        this.ID = ID;
        this.userID = userID;
        this.name = name;
        this.description = description;
        this.category = category;
        this.thumbnail = thumbnail;
        this.price = price;
        this.stock = stock;
        this.timeStamp = timeStamp;
    }

    public ProductModel(String ID, String userID, String name, String description, String category, String thumbnail, double price, long stock, long timeStamp, ArrayList<String> imagesList) {
        this.ID = ID;
        this.userID = userID;
        this.name = name;
        this.description = description;
        this.category = category;
        this.thumbnail = thumbnail;
        this.price = price;
        this.stock = stock;
        this.timeStamp = timeStamp;
        this.imagesList = imagesList;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public long getStock() {
        return stock;
    }

    public void setStock(long stock) {
        this.stock = stock;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public ArrayList<String> getImagesList() {
        return imagesList;
    }

    public void setImagesList(ArrayList<String> imagesList) {
        this.imagesList = imagesList;
    }
}
