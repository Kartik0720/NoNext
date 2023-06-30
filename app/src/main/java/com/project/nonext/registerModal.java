package com.project.nonext;

public class registerModal {
    public registerModal() {
    }
    private String UserName,UserEmail,UserPass,UserMobile,UserId;



    public registerModal(String userName, String userEmail, String userPass, String userMobile, String userId) {
        UserName = userName;
        UserEmail = userEmail;
        UserPass = userPass;
        UserMobile = userMobile;
        UserId = userId;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getUserEmail() {
        return UserEmail;
    }

    public void setUserEmail(String userEmail) {
        UserEmail = userEmail;
    }

    public String getUserPass() {
        return UserPass;
    }

    public void setUserPass(String userPass) {
        UserPass = userPass;
    }

    public String getUserMobile() {
        return UserMobile;
    }

    public void setUserMobile(String userMobile) {
        UserMobile = userMobile;
    }
}
