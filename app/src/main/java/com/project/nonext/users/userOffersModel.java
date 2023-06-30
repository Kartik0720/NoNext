package com.project.nonext.users;

public class userOffersModel {
    String offerCode,date, url;

    public userOffersModel() {
    }

    public userOffersModel(String offerCode, String date, String url) {
        this.offerCode = offerCode;
        this.date = date;
        this.url = url;
    }

    public String getOfferCode() {
        return offerCode;
    }

    public void setOfferCode(String offerCode) {
        this.offerCode = offerCode;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}