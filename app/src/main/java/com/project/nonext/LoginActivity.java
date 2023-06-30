package com.project.nonext;

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
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.nonext.admin.admin_Activity;
import com.project.nonext.seller.seller_login;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private TextView textUserSignUp,forgetPassword,txtgotosellerlogin;
    private FirebaseAuth mAuth;
    private DocumentReference reference;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseFirestore fStore =  FirebaseFirestore.getInstance();
    private ProgressBar loadingPB;
    TextInputEditText edtUserEmail,edtUserPass,edtUserName;
    Button loginBtn;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

//        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mAuth = FirebaseAuth.getInstance();
        edtUserEmail = findViewById(R.id.edtUserEmail);
        edtUserPass = findViewById(R.id.edtUserPass);
        loginBtn = findViewById(R.id.userLoginBtn);
        textUserSignUp = findViewById(R.id.textUserSignUp);
        txtgotosellerlogin = findViewById(R.id.txtgotosellerlogin);
        forgetPassword = findViewById(R.id.txtUserForgotPassword);
//        edtUserName = findViewById(R.id.edtUserName);
        firebaseDatabase = FirebaseDatabase.getInstance();
//        reference = firebaseDatabase.getReference("Users");
        loadingPB = findViewById(R.id.userLoginProgressBar);

        txtgotosellerlogin.setOnClickListener(v -> {
            Intent i = (new Intent(getApplicationContext(), seller_login.class));
            startActivity(i);
        });
        textUserSignUp.setOnClickListener(v -> {
            Intent j = (new Intent(getApplicationContext(),RegisterActivity.class));
            startActivity(j);
        });
        loginBtn.setOnClickListener(View -> {
//            loadingPB.setVisibility(View.VISIBLE);
            loginUser();
        });
        forgetPassword.setOnClickListener(View -> {
            startActivity(new Intent(getApplicationContext(),ForgotPassword.class));
        });

    }
    private void loginUser(){

        String UserEmail = Objects.requireNonNull(edtUserEmail.getText()).toString();
        String UserPass = Objects.requireNonNull(edtUserPass.getText()).toString();
//        String UserName = Objects.requireNonNull(edtUserName.getText()).toString();

        if (TextUtils.isEmpty(UserEmail)){
            edtUserEmail.setError("Email can not be empty");
            edtUserEmail.requestFocus();
        }else if(TextUtils.isEmpty(UserPass)){
            edtUserPass.setError("Password can not be empty");
            edtUserPass.requestFocus();
        }else{
//            loadingPB.setVisibility(View.VISIBLE);
           /* mAuth.signInWithEmailAndPassword(UserEmail,UserPass).addOnCompleteListener(new OnCompleteListener<AuthResult>()
                {@Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        FirebaseUser user = mAuth.getCurrentUser();
                        String registerUserId = user.getUid();
                        if(Objects.requireNonNull(mAuth.getCurrentUser()).isEmailVerified()){
//                            reference = firebaseDatabase.getReference().child("Users").child(registerUserId);
//                            reference.addValueEventListener(new ValueEventListener() {
//                                @Override
//                                public void onDataChange(@NonNull DataSnapshot snapshot) {
////                                    System.out.println("admin");
//                                    String userType = snapshot.child("isAdmin").getValue().toString();
//                                        if(userType.equals("1")){
//                                            Toast.makeText(getApplicationContext(), "Admin Login successfully", Toast.LENGTH_SHORT).show();
//                                            startActivity(new Intent(getApplicationContext(),admin_Activity.class));
//                                        }else{
////                                            Log.v(TAG,"admin");
//                                            Toast.makeText(LoginActivity.this,"User Login successfully",Toast.LENGTH_SHORT).show();
//                                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
//                                        }
//                                        finish();
//
//                                }
//
//                                @Override
//                                public void onCancelled(@NonNull DatabaseError error) {
//
//                                }
//                            });
//                            reference = db.collection("users").document(registerUserId);
//                            reference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//                                @Override
//                                public void onSuccess(DocumentSnapshot documentSnapshot) {
//                                    if(documentSnapshot.getString("isAdmin").equals("0")){
//                                        Toast.makeText(getApplicationContext(), "User Login Successfully", Toast.LENGTH_SHORT).show();
//                                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
//                                    }
//                                    else{
//                                        Toast.makeText(getApplicationContext(), "Row not found", Toast.LENGTH_SHORT).show();
//                                    }
//                                }
//                            });
//                            Toast.makeText(LoginActivity.this,"User Login successfully",Toast.LENGTH_SHORT).show();

//                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        }else{
                            Toast.makeText(LoginActivity.this, "Please Verify your Email Address", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(LoginActivity.this, Objects.requireNonNull(task.getException()).getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
            });*/

            mAuth.signInWithEmailAndPassword(UserEmail,UserPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if(Objects.requireNonNull(mAuth.getCurrentUser()).isEmailVerified()){
                            loadingPB.setVisibility(View.VISIBLE);
                            Toast.makeText(getApplicationContext(), "Logged in Successfully", Toast.LENGTH_SHORT).show();
                            checkUserAccessLevel(user.getUid());
                        }else{
                            Toast.makeText(LoginActivity.this, "Please Verify Email", Toast.LENGTH_SHORT).show();
                        }

                    }else {
                        loadingPB.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "There is a error" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                    }
                }
            });
//                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
//                @Override
//                public void onSuccess(AuthResult authResult) {
////                    loadingPB.setVisibility(View.VISIBLE);
//                    Toast.makeText(getApplicationContext(), "Logged in Successfully", Toast.LENGTH_SHORT).show();
//                    checkUserAccessLevel(authResult.getUser().getUid());
//
//                }
//            }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
//                    Toast.makeText(getApplicationContext(), "There is a error" + e.getMessage(), Toast.LENGTH_SHORT).show();
////                    loadingPB.setVisibility(View.GONE);
//                }
//            });
        }
    }
    //pressing back button
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent myIntent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(myIntent);
        if (item.isChecked())
            item.setChecked(false);
        else item.setChecked(true);
        return true;
    }

    //Checking User is admin or not
    private void checkUserAccessLevel(String uid){
        //DocumentReference df = fStore.collection("Operators").document("Users").collection(uid).document("userregister");
        DocumentReference df = fStore.collection("Users").document(uid);
        df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(Objects.requireNonNull(documentSnapshot.getString("usertype")).equals("Admin")){
                    Toast.makeText(getApplicationContext(), "Admin Login Successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(),
                            admin_Activity.class));
                }else {
                    Toast.makeText(getApplicationContext(), "User Login Successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }
//                loadingPB.setVisibility(View.GONE);
                finish();

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