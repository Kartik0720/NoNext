package com.project.nonext;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class ForgotPassword extends AppCompatActivity {

    private TextView txtgotologin;
    private Button forgetbtn;
    private EditText txtEmail;
    private String Email;
    private FirebaseAuth auth;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Forgot Password");
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        auth = FirebaseAuth.getInstance();
        txtEmail = findViewById(R.id.edtForgotEmail);
        forgetbtn = findViewById(R.id.ForgotBtn);
        txtgotologin = findViewById(R.id.txtGoToLogin);

        txtgotologin.setOnClickListener(v -> {
            Intent i = (new Intent(getApplicationContext(), LoginActivity.class));
            startActivity(i);
        });

        forgetbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Email = txtEmail.getText().toString();

                if (Email.isEmpty()) {
                    txtEmail.setError("Required");
                } else {
                    forgetpass();
                }
            }

            private void forgetpass() {auth.sendPasswordResetEmail(Email).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(getApplicationContext(),"Chack Your Email", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                        //finish();
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Error : "+ task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
            }
        });
    }
    @Override
    protected void onPause() {
        unregisterReceiver(networkChangeListener);
        super.onPause();
    }

    @Override
    protected void onResume() {

        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener, filter);
        super.onResume();
    }
}