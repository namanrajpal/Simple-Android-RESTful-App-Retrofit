package com.zappos.namanrajpal.ilovezappos.Model;

import android.databinding.BaseObservable;

import java.io.Serializable;

/**
 * Created by Naman Rajpal on 2/2/2017.
 */

public class Product implements Serializable {

    String productName;
    String productUrl;
    String price;
    String brandName;
    String thumbnailImageUrl;


    @Override
    public String toString()
    {
        return productName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductUrl() {
        return productUrl;
    }

    public void setProductUrl(String productUrl) {
        this.productUrl = productUrl;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getThumbnailImageUrl() {
        return thumbnailImageUrl;
    }

    public void setThumbnailImageUrl(String thumbnailImageUrl) {
        this.thumbnailImageUrl = thumbnailImageUrl;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }
}
