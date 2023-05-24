package com.moutamid.onlinestore.models;

import com.moutamid.onlinestore.models.ProductModel;

public class CartModel {
    String ID;
    ProductModel productModel;
    int count;

    public CartModel(String ID, ProductModel productModel, int count) {
        this.ID = ID;
        this.productModel = productModel;
        this.count = count;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public ProductModel getProductModel() {
        return productModel;
    }

    public void setProductModel(ProductModel productModel) {
        this.productModel = productModel;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
