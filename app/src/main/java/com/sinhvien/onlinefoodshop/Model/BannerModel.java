package com.sinhvien.onlinefoodshop.Model;

public class BannerModel {

    private String BannerID;
    private String imageURL;

    public BannerModel(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getBannerID()
    {
        return BannerID;
    }

    public void setBannerID(String BannerID)
    {
        this.BannerID = BannerID;
    }


}
