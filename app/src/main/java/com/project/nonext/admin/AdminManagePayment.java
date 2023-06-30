package com.project.nonext.admin;

import android.os.Bundle;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;

import com.project.nonext.R;

public class AdminManagePayment extends AppCompatActivity {

    WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_manage_payment);
        webView = findViewById(R.id.webView);
        webView.loadUrl("https://razorpay.com/");
    }
}