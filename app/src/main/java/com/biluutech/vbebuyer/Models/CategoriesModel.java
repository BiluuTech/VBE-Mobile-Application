package com.biluutech.vbebuyer.Models;

public class CategoriesModel {
    private String cid, imageUrl, categoryName;

    public CategoriesModel() {
    }

    public CategoriesModel(String cid, String imageUrl, String categoryName) {
        this.cid = cid;
        this.imageUrl = imageUrl;
        this.categoryName = categoryName;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
