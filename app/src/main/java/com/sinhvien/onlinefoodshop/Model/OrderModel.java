package com.sinhvien.onlinefoodshop.Model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

public class OrderModel {
    @SerializedName("_id")
    private String id;
    @SerializedName("orderID")
    private String orderID;
    @SerializedName("userEmail")
    private String userEmail;
    @SerializedName("userName")
    private String userName;
    @SerializedName("products")
    private List<ProductModel> products;
    @SerializedName("totalPrice")
    private double totalPrice;
    @SerializedName("paymentMethod")
    private String paymentMethod;
    @SerializedName("status")
    private String status;
    @SerializedName("address")
    private String address;
    @SerializedName("phoneNumber")
    private String phoneNumber;
    @SerializedName("orderDate")
    private Date orderDate;

    public OrderModel(String userEmail, List<ProductModel> products,
                      double totalPrice, String paymentMethod,
                      String address, String phoneNumber, String userName, Date orderDate) {
        this.userEmail = userEmail;
        this.products = products;
        this.totalPrice = totalPrice;
        this.paymentMethod = paymentMethod;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.status = "PENDING";
        this.userName = userName;
        this.orderDate = orderDate;
    }

    // Getters và Setters
    public String getOrderID() { return orderID; }
    public void setOrderID(String orderID) { this.orderID = orderID; }

    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }

    public List<ProductModel> getProducts() { return products; }
    public void setProducts(List<ProductModel> products) { this.products = products; }

    public double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }



}