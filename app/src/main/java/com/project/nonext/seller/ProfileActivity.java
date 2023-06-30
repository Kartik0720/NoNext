package com.project.nonext.seller;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.nonext.NetworkChangeListener;
import com.project.nonext.R;

public class ProfileActivity extends AppCompatActivity {
    private DocumentReference documentReference;
    private FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    private TextView sellerProfileFirstName,sellerProfileLastName;
    private FirebaseAuth mAuth;
    private MaterialCardView sellerProfileLogoutCardView,sellerMyAccount,sellerSettings,sellerOrders,sellerAddress;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_profile);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mAuth = FirebaseAuth.getInstance();
        sellerProfileLogoutCardView= findViewById(R.id.sellerProfileLogoutCardView);
        sellerMyAccount = findViewById(R.id.sellerMyAccountCardView);
        sellerAddress = findViewById(R.id.sellerAddressCardView);
        sellerSettings = findViewById(R.id.sellerSettingsCardView);
        sellerOrders = findViewById(R.id.sellerOrdersCardView);
        sellerProfileFirstName = findViewById(R.id.sellerProfileFirstName);
        sellerProfileLastName = findViewById(R.id.sellerProfileLastName);

        showAllDataUser();
        sellerAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileActivity.this, SellerAddressActivity.class));
            }
        });
        sellerMyAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, SellerAccountActivity.class));
            }
        });
        sellerOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileActivity.this, OrdersActivity.class));
            }
        });
        sellerProfileLogoutCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent i = (new Intent(ProfileActivity.this,
                        seller_login.class));
                startActivity(i);
            }
        });
    }
    private void showAllDataUser(){
        //Displaying Data from Firestore
        FirebaseUser user = mAuth.getCurrentUser();
       /* documentReference = fStore.collection("Operators")
                .document("Users")
                .collection(user.getUid())
                .document("userregister");*/
        documentReference =  fStore.collection("Users").document(String.valueOf(user.getUid()));
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()) {
                    String user_userFirstName = documentSnapshot.getString("sellerfirstname");
                    String user_userLastName = documentSnapshot.getString("sellerlastname");
//                        String user_userMobile = documentSnapshot.getString("UserMobile");
//                        Log.v(TAG, user_userEmail);
                    sellerProfileFirstName.setText(user_userFirstName);
                    sellerProfileLastName.setText(user_userLastName);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ProfileActivity.this, "Failed to fetch data", Toast.LENGTH_SHORT).show();
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