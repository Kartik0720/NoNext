package com.project.nonext;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.nonext.admin.admin_Activity;
import com.project.nonext.seller.seller_Activity;

import java.util.Objects;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {

    VideoView videoView;

    private static final String TAG = "SplashActivity";
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    private DocumentReference df;
//    boolean check;
//    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

//        NetworkUtils networkUtils = new NetworkUtils();
//        check = networkUtils.checkConnection();
//        if(check) {

            videoView = (VideoView) findViewById(R.id.VideoViewSplash);
            videoView.setVisibility(View.VISIBLE);
            Uri video = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.video);
            videoView.setVideoURI(video);


//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
//
            Thread td = new Thread() {
                public void run() {
                    try {
                        sleep(4000);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    } finally {
//                  Intent intent = new Intent(SplashActivity.this,MainActivity.class);
//                  startActivity(intent);
//                  finish();
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                           // df = fStore.collection("Operators").document("Users").collection(String.valueOf(user.getUid())).document("userregister");
                            df =  fStore.collection("Users").document(String.valueOf(user.getUid()));
                            Log.e(TAG, "users" + user.getUid());
                            df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    if (Objects.requireNonNull(documentSnapshot.getString("usertype")).equals("Admin")) {
                                        Toast.makeText(getApplicationContext(), "Admin Login Successfully", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(getApplicationContext(), admin_Activity.class));
                                    } else if (Objects.requireNonNull(documentSnapshot.getString("usertype")).equals("Seller")) {
                                        Toast.makeText(getApplicationContext(), "Seller Login Successfully", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(getApplicationContext(), seller_Activity.class));
                                        //                        setContentView(R.layout.activity_main);
                                    } else {
                                        Toast.makeText(getApplicationContext(), "User Login Successfully", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                    }
                                    finish();
                                }
                            });
//                            CollectionReference cf = fStore.collection("Users").document(user.getUid()).collection("userregister")
                        } else {
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            finish();
                        }
                    }
                }
            };
            td.start();
            videoView.start();
//        }
//        try {
//            VideoView videoHolder = new VideoView(this);
//            setContentView(videoHolder);
//            Uri video = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.nonext_splash_raw);
//            videoHolder.setVideoURI(video);
//
//            videoHolder.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//                public void onCompletion(MediaPlayer mp) {
//                    jump();
//                }
//            });
//            videoHolder.start();
//        } catch (Exception ex) {
//            jump();
//        }
//    }
//
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        jump();
//        return true;
//    }
//
//    private void jump() {
//        if (isFinishing())
//            return;
//        startActivity(new Intent(this, MainActivity.class));
//        finish();
    }

//    @Override
//    protected void onPause() {
//        unregisterReceiver(networkChangeListener);
//        super.onPause();
//    }
//
//    @Override
//    protected void onResume() {
//
//        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
//        registerReceiver(networkChangeListener, filter);
//        super.onResume();
//    }
}