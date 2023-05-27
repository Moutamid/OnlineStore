package com.moutamid.onlinestore.models;

import java.util.ArrayList;

public class ProductModel {
    String ID, userID, name, description, category;
    String thumbnail;
    double price;
    long stock, timeStamp;
    ArrayList<String> imagesList;
    long star1,star2,star3,star4,star5, ratingCount;

    public ProductModel() {
    }

    public ProductModel(String ID, String userID, String name, String description, String category, String thumbnail, double price, long stock, long timeStamp, long star1, long star2, long star3, long star4, long star5, long ratingCount) {
        this.ID = ID;
        this.userID = userID;
        this.name = name;
        this.description = description;
        this.category = category;
        this.thumbnail = thumbnail;
        this.price = price;
        this.stock = stock;
        this.timeStamp = timeStamp;
        this.star1 = star1;
        this.star2 = star2;
        this.star3 = star3;
        this.star4 = star4;
        this.star5 = star5;
        this.ratingCount = ratingCount;
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

    public long getStar1() {
        return star1;
    }

    public void setStar1(long star1) {
        this.star1 = star1;
    }

    public long getStar2() {
        return star2;
    }

    public void setStar2(long star2) {
        this.star2 = star2;
    }

    public long getStar3() {
        return star3;
    }

    public void setStar3(long star3) {
        this.star3 = star3;
    }

    public long getStar4() {
        return star4;
    }

    public void setStar4(long star4) {
        this.star4 = star4;
    }

    public long getStar5() {
        return star5;
    }

    public void setStar5(long star5) {
        this.star5 = star5;
    }

    public long getRatingCount() {
        return ratingCount;
    }

    public void setRatingCount(long ratingCount) {
        this.ratingCount = ratingCount;
    }
}
