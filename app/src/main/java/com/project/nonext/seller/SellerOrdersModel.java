package com.project.nonext.seller;

public class SellerOrdersModel {
    String url,title,orderstatus,orderId;

    public SellerOrdersModel() {
    }

    public SellerOrdersModel(String url, String title, String orderstatus, String orderId) {
        this.url = url;
        this.title = title;
        this.orderstatus = orderstatus;
        this.orderId = orderId;
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

    public String getOrderstatus() {
        return orderstatus;
    }

    public void setOrderstatus(String orderstatus) {
        this.orderstatus = orderstatus;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
