package com.project.nonext.seller;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.nonext.LoginActivity;
import com.project.nonext.NetworkChangeListener;
import com.project.nonext.R;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class seller_Register extends AppCompatActivity {

    private TextInputEditText edtSellerFirstName,edtSellerLastName,edtSellerMobile,edtSellerEmail,edtSellerPass,edtSellerCPass;
    private TextView txtSellerLogin;
    private Button btnSellerSignUp;
    private FirebaseAuth mAuth;
    private FirebaseFirestore fStore;
    private DocumentReference documentReference;
    private ProgressBar loadingPB;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_register);

        edtSellerFirstName = findViewById(R.id.edtSellerFirstName);
        edtSellerLastName = findViewById(R.id.edtSellerLastName);
        edtSellerMobile = findViewById(R.id.edtSellerMobile);
        edtSellerEmail = findViewById(R.id.edtSellerEmail);
        edtSellerPass = findViewById(R.id.edtSellerPass);
        edtSellerCPass = findViewById(R.id.edtSellerCPass);
        txtSellerLogin = findViewById(R.id.txtSellerLogin);
        btnSellerSignUp = findViewById(R.id.btnSellerSignUp);
        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        loadingPB = findViewById(R.id.progressBar);


        txtSellerLogin.setOnClickListener(v ->{
            Intent i = (new Intent(getApplicationContext(),LoginActivity.class));
            startActivity(i);
        });

        btnSellerSignUp.setOnClickListener(v ->{
            CreateSeller();
        });

    }

    private void CreateSeller() {
        String SellerFirstName = Objects.requireNonNull(edtSellerFirstName.getText()).toString();
        String SellerLastName = Objects.requireNonNull(edtSellerLastName.getText()).toString();
        String SellerMobile = Objects.requireNonNull(edtSellerMobile.getText()).toString();
        String SellerEmail = Objects.requireNonNull(edtSellerEmail.getText()).toString();
        String SellerPass = Objects.requireNonNull(edtSellerPass.getText()).toString();
        String SellerCPass = Objects.requireNonNull(edtSellerCPass.getText()).toString();

        if(TextUtils.isEmpty(SellerFirstName)){
            edtSellerFirstName.setError("Please Enter FirstName");
            edtSellerFirstName.requestFocus();
        }else if(TextUtils.isEmpty(SellerLastName)){
            edtSellerLastName.setError("Please Enter LastName");
            edtSellerLastName.requestFocus();
        }else if  (TextUtils.isEmpty(SellerEmail)){
            edtSellerEmail.setError("Email can not be empty");
            edtSellerEmail.requestFocus();
        }else if(TextUtils.isEmpty(SellerMobile)){
            edtSellerMobile.setError("Please Enter Mobile Number");
            edtSellerMobile.requestFocus();
        }else if(SellerMobile.length()!=10){
            edtSellerMobile.setError("Please Enter valid Mobile Number");
            edtSellerMobile.requestFocus();
        }else if(TextUtils.isEmpty(SellerPass)){
            edtSellerPass.setError("Please Enter Password");
            edtSellerPass.requestFocus();
        }else if(TextUtils.isEmpty(SellerCPass)){
            edtSellerCPass.setError("Please Enter Confirm Password");
            edtSellerCPass.requestFocus();
        }else if(!SellerPass.equals(SellerCPass)){
            edtSellerPass.setError("Password Does not match");
            edtSellerPass.requestFocus();
        }else if(SellerPass.length()<6){
            edtSellerPass.setError("Password should be greater than 6 letters");
            edtSellerPass.requestFocus();
        }else{
            mAuth.createUserWithEmailAndPassword(SellerEmail,SellerPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        loadingPB.setVisibility(View.VISIBLE);
                        btnSellerSignUp.setVisibility(View.INVISIBLE);
                        FirebaseUser seller = mAuth.getCurrentUser();
                        Objects.requireNonNull(mAuth.getCurrentUser()).sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {

                                    loadingPB.setVisibility(View.GONE);
                                    //Storing Data into Firestore
                                   // documentReference = fStore.collection("Operators").document("Users").collection(Objects.requireNonNull(seller).getUid()).document("userregister");
                                    documentReference = fStore.collection("Users").document(seller.getUid());
                                    Map<String,String> userInfo = new HashMap<>();
                                    userInfo.put("sellerfirstname",SellerFirstName);
                                    userInfo.put("sellerlastname",SellerLastName);
                                    userInfo.put("selleremail",SellerEmail);
                                    userInfo.put("sellermobile",SellerMobile);
                                    userInfo.put("sellerpass",SellerPass);
                                    //Specify if the user is admin
                                    userInfo.put("usertype","Seller");

                                    documentReference.set(userInfo);
                                    Toast.makeText(getApplicationContext(), "Registered successfully,Please Check your email for Verification", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                                }else{
                                    Toast.makeText(getApplicationContext(), "Registration Error : " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }else{
                        Toast.makeText(getApplicationContext(), Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
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