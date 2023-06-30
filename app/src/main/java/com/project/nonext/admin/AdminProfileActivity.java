package com.project.nonext.admin;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.nonext.LoginActivity;
import com.project.nonext.NetworkChangeListener;
import com.project.nonext.R;
import com.project.nonext.users.AddressActivity;
import com.project.nonext.users.MyAccount;

public class AdminProfileActivity extends AppCompatActivity {

    private DocumentReference documentReference;
    private FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    private TextView adminProfileFirstName,adminProfileLastName;
    private FirebaseAuth mAuth;
    private MaterialCardView adminProfileLogoutCardView,adminMyAccount,adminAddress,adminSettings,adminOrders;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_profile);
        mAuth = FirebaseAuth.getInstance();
        adminProfileLogoutCardView= findViewById(R.id.adminProfileLogoutCardView);
        adminMyAccount = findViewById(R.id.adminMyAccountCardView);
        adminAddress = findViewById(R.id.adminAddressCardView);
        adminSettings = findViewById(R.id.adminSettingsCardView);
        adminOrders = findViewById(R.id.adminOrdersCardView);
        adminProfileFirstName = findViewById(R.id.adminProfileFirstName);
        adminProfileLastName = findViewById(R.id.adminProfileLastName);

        showAllDataUser();
        adminMyAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminProfileActivity.this, MyAccount.class));
            }
        });
        adminAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminProfileActivity.this, AddressActivity.class));
            }
        });
        adminProfileLogoutCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent i = (new Intent(AdminProfileActivity.this,
                        LoginActivity.class));
                startActivity(i);
            }
        });
    }
    private void showAllDataUser(){
        //Displaying Data from Firestore
        FirebaseUser user = mAuth.getCurrentUser();
        /*documentReference = fStore.collection("Operators")
                .document("Users")
                .collection(user.getUid())
                .document("userregister");*/
        documentReference = fStore.collection("Users").document(user.getUid());
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()) {
                    String user_userFirstName = documentSnapshot.getString("userfirstname");
                    String user_userLastName = documentSnapshot.getString("userlastname");
//                        String user_userMobile = documentSnapshot.getString("UserMobile");
//                        Log.v(TAG, user_userEmail);
                    adminProfileFirstName.setText(user_userFirstName);
                    adminProfileLastName.setText(user_userLastName);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AdminProfileActivity.this, "Failed to fetch data", Toast.LENGTH_SHORT).show();
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