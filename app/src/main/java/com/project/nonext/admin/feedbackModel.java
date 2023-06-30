package com.project.nonext.admin;

public class feedbackModel {
    String username,mobile,email,feedback;

    public feedbackModel() {
    }

    public feedbackModel(String username, String mobile, String email, String feedback) {
        this.username = username;
        this.mobile = mobile;
        this.email = email;
        this.feedback = feedback;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }
}
