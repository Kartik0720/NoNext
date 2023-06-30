package com.project.nonext;

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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    private TextView textUserLogin;
    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    private DatabaseReference DatabaseReference;
    private DocumentReference documentReference;
    private TextInputEditText edtUserFirstName,edtUserLastName,edtUserMobile,edtUserEmail,edtUserPass,edtUserCPass;
    private Button btnUserSignUp;
    private ProgressBar loadingPB;
    private String UserId;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

//        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
        edtUserEmail = findViewById(R.id.edtUserEmail);
        edtUserMobile = findViewById(R.id.edtUserMobile);
        edtUserPass = findViewById(R.id.edtUserPass);
        edtUserFirstName = findViewById(R.id.edtUserFirstName);
        edtUserLastName = findViewById(R.id.edtUserLastName);
        edtUserCPass = findViewById(R.id.edtUserCPass);
        btnUserSignUp = findViewById(R.id.btnUserSignUp);
        mAuth = FirebaseAuth.getInstance();
        textUserLogin = findViewById(R.id.textUserLogin);
        firebaseDatabase = FirebaseDatabase.getInstance();
        loadingPB = findViewById(R.id.progressBar);
//        databaseReference = firebaseDatabase.getReference("Users");

        textUserLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = (new Intent(getApplicationContext(), LoginActivity.class));
                startActivity(i);
//                finish();
            }
        });
        btnUserSignUp.setOnClickListener(view ->{
            createUser();
        });
    }
    private void createUser(){
        String UserMobile = Objects.requireNonNull(edtUserMobile.getText()).toString();
        String UserEmail = Objects.requireNonNull(edtUserEmail.getText()).toString();
        String UserPass = Objects.requireNonNull(edtUserPass.getText()).toString();
        String UserFirstName = Objects.requireNonNull(edtUserFirstName.getText()).toString();
        String UserLastName = Objects.requireNonNull(edtUserLastName.getText()).toString();
        String UserCPass = Objects.requireNonNull(edtUserCPass.getText()).toString();
//        UserId =UserName;
        if (TextUtils.isEmpty(UserFirstName)) {
            edtUserFirstName.setError("Please Enter FirstName");
            edtUserFirstName.requestFocus();
        } else if(TextUtils.isEmpty(UserLastName)){
                edtUserLastName.setError("Please Enter LastName");
                edtUserLastName.requestFocus();
        }else if  (TextUtils.isEmpty(UserEmail)){
            edtUserEmail.setError("Email can not be empty");
            edtUserEmail.requestFocus();
        }else if(TextUtils.isEmpty(UserMobile)){
            edtUserMobile.setError("Please Enter Mobile Number");
            edtUserMobile.requestFocus();
        }else if(UserMobile.length()!=10){
            edtUserMobile.setError("Please Enter valid Mobile Number");
            edtUserMobile.requestFocus();
        }else if(TextUtils.isEmpty(UserPass)){
            edtUserPass.setError("Please Enter Password");
            edtUserPass.requestFocus();
        }else if(UserPass.length()<6){
            edtUserPass.setError("Password should be greater than 6 letters");
            edtUserPass.requestFocus();
        }else if(TextUtils.isEmpty(UserCPass)){
            edtUserCPass.setError("Please Enter Confirm Password");
            edtUserCPass.requestFocus();
        }else if(!UserPass.equals(UserCPass)){
            edtUserPass.setError("Password Does not match");
            edtUserPass.requestFocus();

        }else{
            mAuth.createUserWithEmailAndPassword(UserEmail,UserPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        loadingPB.setVisibility(View.VISIBLE);
                        btnUserSignUp.setVisibility(View.INVISIBLE);
                        FirebaseUser user = mAuth.getCurrentUser();
                        Objects.requireNonNull(mAuth.getCurrentUser()).sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    /*registerModal registerModel = new registerModal(UserName,UserEmail,UserPass,UserMobile,UserId);

                                    databaseReference.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            Map<String,String> userInfo = new HashMap<>();
                                            userInfo.put("UserName",UserName);
                                            userInfo.put("UserEmail",UserEmail);
                                            userInfo.put("UserMobile",UserMobile);
                                            userInfo.put("UserPass",UserPass);
                                            //Specify if the user is admin
                                            userInfo.put("isAdmin","0");
                                            databaseReference.child(user.getUid()).setValue(userInfo);
                                            Toast.makeText(RegisterActivity.this, "Registered successfully,Please Check your email for Verification", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                        }
                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });*/

                                    loadingPB.setVisibility(View.GONE);
                                    //Storing Data into Firestore
                                   // documentReference = fStore.collection("Operators").document("Users").collection(user.getUid()).document("userregister");
                                    documentReference = fStore.collection("Users").document(user.getUid());
                                    Map<String,String> userInfo = new HashMap<>();
                                        userInfo.put("userfirstname",UserFirstName);
                                        userInfo.put("userlastname",UserLastName);
                                        userInfo.put("useremail",UserEmail);
                                        userInfo.put("usermobile",UserMobile);
                                        userInfo.put("userpass",UserPass);
                                        //Specify if the user is admin
                                        userInfo.put("usertype","User");

                                        documentReference.set(userInfo);
                                        Toast.makeText(RegisterActivity.this, "Registered successfully,Please Check your email for Verification", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                }else{
                                    Toast.makeText(RegisterActivity.this, "Registration Error : " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }else{
                        Toast.makeText(RegisterActivity.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        Intent myIntent = new Intent(getApplicationContext(), LoginActivity.class);
//        startActivity(myIntent);
//        return true;
//    }
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