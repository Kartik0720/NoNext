package com.project.nonext.seller;

public class sellerprodmodel {

    String title,prices,url,prodId;

    public sellerprodmodel() {
    }

    public sellerprodmodel(String title, String prices,String url,String prodId) {
        this.title = title;
        this.prices = prices;
        this.url = url;
        this.prodId = prodId;
    }
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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

    public String getProdId() {

        return prodId;
    }

    public void setProdId(String prodId) {
        this.prodId = prodId;
    }
}
