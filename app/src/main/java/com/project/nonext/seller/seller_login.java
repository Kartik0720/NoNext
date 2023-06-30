package com.project.nonext.seller;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.nonext.ForgotPassword;
import com.project.nonext.LoginActivity;
import com.project.nonext.NetworkChangeListener;
import com.project.nonext.R;
import com.project.nonext.admin.admin_Activity;

import java.util.Objects;

public class seller_login extends AppCompatActivity {

    TextInputEditText EdtSellerEmail,EdtSellerPass;
    Button sellerLoginBtn;
    private FirebaseAuth mAuth;
    private FirebaseFirestore fStore;
    private ProgressBar sellerLoadingPB;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_login);

        TextView txtForgotPass,txtSellerSignUp,txtGoToUserLogin;

//        Toolbar toolbar =(Toolbar)findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        EdtSellerEmail = findViewById(R.id.edtSellerEmail);
        EdtSellerPass = findViewById(R.id.edtSellerPass);
        txtForgotPass = findViewById(R.id.txtSellerForgotPassword);
        txtSellerSignUp = findViewById(R.id.txtSellerSignUp);
        txtGoToUserLogin = findViewById(R.id.txtgotouserlogin);
        sellerLoginBtn = findViewById(R.id.sellerLoginBtn);
        sellerLoadingPB = findViewById(R.id.sellerLoginProgressBar);
        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        txtForgotPass.setOnClickListener(v ->{
            Intent i = (new Intent(getApplicationContext(), ForgotPassword.class));
            startActivity(i);
        });
        txtSellerSignUp.setOnClickListener(v ->{
            Intent i = (new Intent(getApplicationContext(),seller_Register.class));
            startActivity(i);
        });
        txtGoToUserLogin.setOnClickListener(v -> {
            Intent i = (new Intent(getApplicationContext(), LoginActivity.class));
            startActivity(i);
        });

        sellerLoginBtn.setOnClickListener(v -> loginSeller());

    }

    private void loginSeller() {
        String SellerEmail = Objects.requireNonNull(EdtSellerEmail.getText()).toString();
        String SellerPass = Objects.requireNonNull(EdtSellerPass.getText()).toString();

        if (TextUtils.isEmpty(SellerEmail)){
            EdtSellerEmail.setError("Email can not be empty");
            EdtSellerEmail.requestFocus();
        }else if(TextUtils.isEmpty(SellerPass)){
            EdtSellerPass.setError("Password can not be empty");
            EdtSellerPass.requestFocus();
        }else{
            mAuth.signInWithEmailAndPassword(SellerEmail,SellerPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if(Objects.requireNonNull(mAuth.getCurrentUser()).isEmailVerified()) {
                            sellerLoadingPB.setVisibility(View.VISIBLE);
//                            Toast.makeText(getApplicationContext(), "Logged in Successfully", Toast.LENGTH_SHORT).show();
                            assert user != null;
                            checkUserAccessLevel(user.getUid());
//                            startActivity(new Intent(getApplicationContext(), seller_Activity.class));
                        }else{
                            Toast.makeText(seller_login.this, "Please Verify Email", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        sellerLoadingPB.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "There is a error" + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();

                    }
                }
            });
        }
    }
    //pressing back button
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent myIntent = new Intent(getApplicationContext(), seller_Activity.class);
        startActivity(myIntent);

        if (item.isChecked())
            item.setChecked(false);
        else item.setChecked(true);
        return true;
    }

//    Checking User is admin or not
    private void checkUserAccessLevel(String uid){
       // DocumentReference df = fStore.collection("Operators").document("Users").collection(uid).document("userregister");
        DocumentReference df = fStore.collection("Users").document(uid);
        df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(Objects.requireNonNull(documentSnapshot.getString("usertype")).equals("Seller")){
                    Toast.makeText(getApplicationContext(), "seller Login Successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), seller_Activity.class));
                }else if(Objects.requireNonNull(documentSnapshot.getString("usertype")).equals("Admin")){
                    Toast.makeText(getApplicationContext(), "Admin Login Successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), admin_Activity.class));
                }
                else{
                    Toast.makeText(getApplicationContext(), "Login Failed", Toast.LENGTH_SHORT).show();
//                    startActivity(new Intent(getApplicationContext(),seller_Activity.class));
                }
//                loadingPB.setVisibility(View.GONE);
                finish();

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(seller_login.this,LoginActivity.class));
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