package com.project.nonext.users;

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

public class AddressActivity extends AppCompatActivity {

    CardView addAddressCardView,address1CardView,ordersCardView;
    TextView fullName,address,mobileNo;
    DocumentReference df;
    FirebaseFirestore fStore;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_address);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Addresses");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        addAddressCardView = findViewById(R.id.addAddressCardView);
        address1CardView = findViewById(R.id.address1CardView);
        /*ordersCardView = findViewById(R.id.address1CardView);*/
        fullName = findViewById(R.id.addressFullName);
        address = findViewById(R.id.fullAddress);
        mobileNo = findViewById(R.id.addressMobileNo);
        fStore = FirebaseFirestore.getInstance();
        addAddressCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AddressActivity.this,AddAddressActivity.class));
//                addAddressCardView.setVisibility(View.GONE);
//                address1CardView.setVisibility(View.VISIBLE);
            }
        });
        address1CardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AddressActivity.this,AddAddressActivity.class));
            }
        });
        //df = fStore.collection("Operators").document("Users").collection(user.getUid()).document("userregister");
        df =  fStore.collection("Users").document(String.valueOf(user.getUid()));
        df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    addAddressCardView.setVisibility(View.GONE);
                    address1CardView.setVisibility((View.VISIBLE));
                    String addressDb = documentSnapshot.getString("address");
                    String addressNameDb = documentSnapshot.getString("addressname");
                    String addressMobileDb = documentSnapshot.getString("addressmobile");
                    String addressCityDb = documentSnapshot.getString("addresscity");
                    String addressPincodeDb = documentSnapshot.getString("addresspincode");
                    address.setText(addressDb + " " + addressCityDb + "\n" + addressPincodeDb);
                    fullName.setText(addressNameDb);
                    mobileNo.setText(addressMobileDb);
                }
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