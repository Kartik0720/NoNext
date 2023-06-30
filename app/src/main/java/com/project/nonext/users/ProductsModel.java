package com.project.nonext.users;

public class ProductsModel {

    public String title,prices,description,url,prodId,sellerId;


    public ProductsModel() {
    }

    public ProductsModel(String title, String prices, String description, String url, String prodId, String sellerId) {
        this.title = title;
        this.prices = prices;
        this.description = description;
        this.url = url;
        this.prodId = prodId;
        this.sellerId = sellerId;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getProdId() {
        return prodId;
    }

    public void setProdId(String prodId) {
        this.prodId = prodId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrices() {
        return prices;
    }

    public void setPrices(String prices) {
        this.prices = prices;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
