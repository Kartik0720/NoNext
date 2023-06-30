package com.project.nonext.seller;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.nonext.NetworkChangeListener;
import com.project.nonext.R;
import com.project.nonext.users.AddAddressActivity;

public class SellerAddressActivity extends AppCompatActivity {
    CardView sellerAddAddressCardView,sellerAddress1CardView;
    TextView fullName,address,mobileNo;
    DocumentReference df;
    FirebaseFirestore fStore;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_address);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Addresses");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        sellerAddAddressCardView = findViewById(R.id.sellerAddAddressCardView);
        sellerAddress1CardView = findViewById(R.id.sellerAddress1CardView);
        fullName = findViewById(R.id.sellerAddressFullName);
        address = findViewById(R.id.sellerFullAddress);
        mobileNo = findViewById(R.id.sellerAddressMobileNo);
        fStore = FirebaseFirestore.getInstance();
        sellerAddAddressCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SellerAddressActivity.this, AddAddressActivity.class));
            }
        });
        //df = fStore.collection("Operators").document("Users").collection(user.getUid()).document("userregister");
        df = fStore.collection("Users").document(user.getUid());
        df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    sellerAddAddressCardView.setVisibility(View.GONE);
                    sellerAddress1CardView.setVisibility((View.VISIBLE));
                    String addressDb = documentSnapshot.getString("address");
                    String addressNameDb = documentSnapshot.getString("addressname");
                    String addressMobileDb = documentSnapshot.getString("addressmobile");
                    String addressCityDb = documentSnapshot.getString("addresscity");
                    String addressPincodeDb = documentSnapshot.getString("addresspincode");
                    if (addressDb == null) {
                        showAddAddress();
                    } else {
                        address.setText(addressDb + " " + addressCityDb + "\n" + addressPincodeDb);
                        fullName.setText(addressNameDb);
                        mobileNo.setText(addressMobileDb);
                    }
                }
            }
        });

    }
    public void showAddAddress(){
        sellerAddAddressCardView.setVisibility(View.VISIBLE);
        sellerAddress1CardView.setVisibility(View.GONE);
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