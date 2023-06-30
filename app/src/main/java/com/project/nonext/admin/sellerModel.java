package com.project.nonext.admin;

public class sellerModel {
    String selleremail,sellerfirstname,sellerlastname,sellermobile,sellerpass;

    public sellerModel() {
    }

    public sellerModel(String selleremail, String sellerfirstname, String sellerlastname, String sellermobile, String sellerpass) {
        this.selleremail = selleremail;
        this.sellerfirstname = sellerfirstname;
        this.sellerlastname = sellerlastname;
        this.sellermobile = sellermobile;
        this.sellerpass = sellerpass;
    }

    public String getSelleremail() {
        return selleremail;
    }

    public void setSelleremail(String selleremail) {
        this.selleremail = selleremail;
    }

    public String getSellerfirstname() {
        return sellerfirstname;
    }

    public void setSellerfirstname(String sellerfirstname) {
        this.sellerfirstname = sellerfirstname;
    }

    public String getSellerlastname() {
        return sellerlastname;
    }

    public void setSellerlastname(String sellerlastname) {
        this.sellerlastname = sellerlastname;
    }

    public String getSellermobile() {
        return sellermobile;
    }

    public void setSellermobile(String sellermobile) {
        this.sellermobile = sellermobile;
    }

    public String getSellerpass() {
        return sellerpass;
    }

    public void setSellerpass(String sellerpass) {
        this.sellerpass = sellerpass;
    }
}
