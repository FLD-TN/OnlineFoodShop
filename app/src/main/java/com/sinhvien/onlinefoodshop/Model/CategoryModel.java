package com.sinhvien.onlinefoodshop.Model;

import java.io.Serializable;

public class CategoryModel implements Serializable {
    private String categoryId;
    private String categoryName;
    private String categoryIcon;

    public CategoryModel() {}

    public CategoryModel(String categoryName, String categoryIcon) {
        this.categoryName = categoryName;
        this.categoryIcon = categoryIcon;
    }

    public String getCategoryId() { return categoryId; }
    public void setCategoryId(String categoryId) { this.categoryId = categoryId; }
    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }
    public String getCategoryIcon() { return categoryIcon; }
    public void setCategoryIcon(String categoryIcon) { this.categoryIcon = categoryIcon; }

    @Override
    public String toString() {
        return "CategoryModel{" +
                "categoryId='" + categoryId + '\'' +
                ", categoryName='" + categoryName + '\'' +
                ", categoryIcon='" + categoryIcon + '\'' +
                '}';
    }
}