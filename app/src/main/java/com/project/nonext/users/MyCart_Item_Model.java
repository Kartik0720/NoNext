package com.project.nonext.users;

public class MyCart_Item_Model {


//    ITEMS
     String prodId,title,price,qty,url,cartId,sellerId,WishlistId;

    public MyCart_Item_Model() {
    }


    public MyCart_Item_Model(String prodId, String title, String price, String qty, String url, String cartId, String sellerId, String WishlistId) {
        this.prodId = prodId;
        this.sellerId = sellerId;
        this.title = title;
        this.price = price;
        this.qty = qty;
        this.url = url;
        this.cartId = cartId;
        this.WishlistId = WishlistId;
    }

    public String getWishlistId() {
        return WishlistId;
    }

    public void setWishlistId(String wishlistId) {
        WishlistId = wishlistId;
    }

    public String getSellerId(){return sellerId;}

    public void setSellerId(String sellerId){
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCartId() {
        return cartId;
    }

    public void setCartId(String cartId) {
        this.cartId = cartId;
    }


}
