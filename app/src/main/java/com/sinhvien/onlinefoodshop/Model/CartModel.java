package com.sinhvien.onlinefoodshop.Model;

import java.io.Serializable;
import java.text.DecimalFormat;

public class CartModel implements Serializable {
    private String productID;
    private String productName;
    private double productPrice;
    private String productImageUrl;
    private int quantity;

    public CartModel(String productID, String productName, double productPrice, String productImageUrl, int quantity) {
        this.productID = productID;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productImageUrl = productImageUrl;
        this.quantity = quantity;
    }

    // Getters và Setters
    public String getProductID() { return productID; }
    public String getProductName() { return productName; }
    public double getProductPrice() { return productPrice; }
    public String getProductImageUrl() { return productImageUrl; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    // Phương thức format tiền mới
    public String getFormattedPrice() {
        DecimalFormat formatter = new DecimalFormat("#,###");
        return formatter.format(productPrice) + "đ";
    }

    public String getFormattedTotalPrice() {
        DecimalFormat formatter = new DecimalFormat("#,###");
        return formatter.format(getTotalPrice()) + "đ";
    }

    public double getTotalPrice() {
        return productPrice * quantity;
    }
}