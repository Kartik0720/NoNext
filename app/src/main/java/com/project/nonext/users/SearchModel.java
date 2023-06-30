package com.project.nonext.users;

public class SearchModel {
    public String title,url,prodId,sellerId;

    public SearchModel() {
    }

    public SearchModel(String title, String url, String prodId, String sellerId) {
        this.title = title;
        this.url = url;
        this.prodId = prodId;
        this.sellerId = sellerId;
    }

    public String getProdId() {
        return prodId;
    }

    public void setProdId(String prodId) {
        this.prodId = prodId;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
