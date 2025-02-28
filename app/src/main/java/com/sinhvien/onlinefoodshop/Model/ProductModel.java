package com.sinhvien.onlinefoodshop.Model;

import java.io.Serializable;

public class ProductModel implements Serializable {
    private String productID;
    private String productName;
    private double productPrice;
    private String description;
    private String category;
    private String productImage;

    public ProductModel(String productName, double productPrice, String description, String category,String productImage) {
        this.productName = productName;
        this.productPrice = productPrice;
        this.description = description;
        this.category = category;
        this.productImage = productImage;
    }

    public ProductModel() {}

    public String getProductID() { return productID; }
    public void setProductID(String productID) { this.productID = productID; }
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public double getProductPrice() { return productPrice; }
    public void setProductPrice(double productPrice) { this.productPrice = productPrice; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getProductImage() { return productImage; }
    public void setProductImage(String productImage) { this.productImage = productImage; }

    @Override
    public String toString() {
        return "ProductModel{" +
                "productID='" + productID + '\'' +
                ", productName='" + productName + '\'' +
                ", productPrice=" + productPrice +
                ", description='" + description + '\'' +
                ", category='" + category + '\'' +
                ", productImage='" + productImage + '\'' +
                '}';
    }
}