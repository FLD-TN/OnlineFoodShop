package com.sinhvien.onlinefoodshop.Model;

public class FoodModel {
    private String foodName;
    private String foodPrice;
    private int foodImage;

    public FoodModel(String foodName, String foodPrice, int foodImage)
    {
        this.foodName = foodName;
        this.foodPrice = foodPrice;
        this.foodImage = foodImage;
}
public String getName()
{
    return foodName;
}
public String getPrice()
{
    return foodPrice;
}
public int getImage()
{
    return foodImage;
}
}
