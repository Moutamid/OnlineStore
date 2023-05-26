package com.moutamid.onlinestore.models;

public class BuyModel {
    String ID, buyerID, name, numb, address;
    ProductModel product;
    int productCount;
    String type, status;
    double finalPrice;
    long timeStamp;

    public BuyModel() {
    }

    public BuyModel(String ID, String buyerID, String name, String numb, String address, ProductModel product, int productCount, String type, String status, double finalPrice, long timeStamp) {
        this.ID = ID;
        this.buyerID = buyerID;
        this.name = name;
        this.numb = numb;
        this.address = address;
        this.product = product;
        this.productCount = productCount;
        this.type = type;
        this.status = status;
        this.finalPrice = finalPrice;
        this.timeStamp = timeStamp;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumb() {
        return numb;
    }

    public void setNumb(String numb) {
        this.numb = numb;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public ProductModel getProduct() {
        return product;
    }

    public void setProduct(ProductModel product) {
        this.product = product;
    }

    public int getProductCount() {
        return productCount;
    }

    public void setProductCount(int productCount) {
        this.productCount = productCount;
    }

    public double getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(double finalPrice) {
        this.finalPrice = finalPrice;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBuyerID() {
        return buyerID;
    }

    public void setBuyerID(String buyerID) {
        this.buyerID = buyerID;
    }
}
