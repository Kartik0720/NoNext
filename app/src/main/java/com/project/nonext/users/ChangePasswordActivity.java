package com.project.nonext.users;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.project.nonext.R;

import java.util.HashMap;
import java.util.Map;

public class ChangePasswordActivity extends AppCompatActivity {

    EditText userPassword,userNewPassword,userNewConfirmPassword;
    DocumentReference df;
    FirebaseFirestore fStore;
    FirebaseAuth mAuth;
    Button saveBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_change_password);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Account");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //FindViewById
        userPassword = findViewById(R.id.userPassword);
        userNewPassword = findViewById(R.id.userNewPassword);
        userNewConfirmPassword = findViewById(R.id.userNewConfirmPassword);
        saveBtn = findViewById(R.id.passwordSaveBtn);
        fStore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        //Changing Password
        String oldPassword = userPassword.getText().toString();
//        Log.v("abcd", String.valueOf(user));
        Log.v("abcd",oldPassword);
      //  df = fStore.collection("Operators").document("Users").collection(user.getUid()).document("userregister");
        df =  fStore.collection("Users").document(String.valueOf(user.getUid()));
        df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()) {
                    String oldPasswordDb = documentSnapshot.getString("userpass");
                    saveBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            assert oldPasswordDb != null;

                            if(oldPasswordDb.equals(oldPassword)){
                                changePassword(oldPasswordDb);
                            }else{
                                userPassword.setError("Password Not Match");
                                userPassword.requestFocus();
                            }
                        }
                    });
                }
            }
        });
    }
    public void changePassword(String oldPasswordDb){
        FirebaseUser user = mAuth.getCurrentUser();
        String newPassword = userNewPassword.getText().toString();
        String newConfirmPassword = userNewConfirmPassword.getText().toString();
        if(oldPasswordDb.equals(newPassword)){
            userNewPassword.setError("Please Enter Different password");
            userNewPassword.requestFocus();
        }
        if(newPassword.equals(newConfirmPassword)){
            //df = fStore.collection("Operators").document("Users").collection(user.getUid()).document("userregister");
            df =  fStore.collection("Users").document(String.valueOf(user.getUid()));
            Map<String,String> password = new HashMap<>();
            password.put("userpass",newPassword);
            df.set(password, SetOptions.merge())
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            startActivity(new Intent(ChangePasswordActivity.this,MyAccount.class));
                            Toast.makeText(ChangePasswordActivity.this, "Password Changed", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ChangePasswordActivity.this, "Password Could Not Change", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}