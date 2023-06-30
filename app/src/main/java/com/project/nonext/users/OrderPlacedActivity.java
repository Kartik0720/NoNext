package com.project.nonext.users;

import android.content.Intent;
import android.os.Bundle;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.project.nonext.R;

import java.util.Timer;
import java.util.TimerTask;

public class OrderPlacedActivity extends AppCompatActivity {
    VideoView vv;
    Timer timer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_order_placed);
        vv = findViewById(R.id.videoView);
        vv.setVideoPath("android.resource://" + getPackageName() + "/" + R.raw.orderplaced);
        vv.start();

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent(OrderPlacedActivity.this,MyOrdersActivity.class);
                startActivity(intent);
                finish();
            }
        },5000);

    }
}