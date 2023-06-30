package com.project.nonext.seller;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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
import com.project.nonext.NetworkChangeListener;
import com.project.nonext.R;

import java.util.HashMap;
import java.util.Map;

public class SellerAddAddressActivity extends AppCompatActivity {
    EditText sellerAddAddressPincode,sellerAddAddress,sellerAddAddressFullName,sellerAddAddressCity,sellerAddAddressMobileNo;
    DocumentReference df;
    FirebaseFirestore fStore;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();;
    FirebaseUser user = mAuth.getCurrentUser();
    Button addBtn;
    ProgressBar loadingPB;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_add_address);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Add Address");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //findViewById
        sellerAddAddress = findViewById(R.id.sellerEdtAddAddress);
        sellerAddAddressFullName = findViewById(R.id.sellerEdtAddAddressFullName);
        sellerAddAddressMobileNo = findViewById(R.id.sellerEdtAddAddressMobileNo);
        sellerAddAddressCity = findViewById(R.id.sellerEdtAddAddressCity);
        sellerAddAddressPincode = findViewById(R.id.sellerEdtAddAddressPincode);
        loadingPB = findViewById(R.id.addAddressLoadingPB);
        addBtn = findViewById(R.id.addAddressBtn);
        fStore = FirebaseFirestore.getInstance();


        //df = fStore.collection("Operators").document("Users").collection(user.getUid()).document("userregister");
       df = fStore.collection("Users").document(user.getUid());
        df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    String addressDb = documentSnapshot.getString("address");
                    String addressNameDb = documentSnapshot.getString("addressname");
                    String addressMobileDb = documentSnapshot.getString("addressmobile");
                    String addressCityDb = documentSnapshot.getString("addresscity");
                    String addressPincodeDb = documentSnapshot.getString("addresspincode");
                    sellerAddAddress.setText(addressDb);
                    sellerAddAddressFullName.setText(addressNameDb);
                    sellerAddAddressMobileNo.setText(addressMobileDb);
                    sellerAddAddressCity.setText(addressCityDb);
                    sellerAddAddressPincode.setText(addressPincodeDb);
                    sellerAddAddressFullName.requestFocus();
                }
            }
        });
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingPB.setVisibility(View.VISIBLE);
                String address = sellerAddAddress.getText().toString();
                String addressName = sellerAddAddressFullName.getText().toString();
                String addressMobile = sellerAddAddressMobileNo.getText().toString();
                String addressCity = sellerAddAddressCity.getText().toString();
                String addressPincode = sellerAddAddressPincode.getText().toString();
                Log.v("abcd",addressName);
                Map<String,String> mapAddress = new HashMap<>();
                mapAddress.put("addressname",addressName);
                mapAddress.put("address",address);
                mapAddress.put("addressmobile",addressMobile);
                mapAddress.put("addresscity",addressCity);
                mapAddress.put("addresspincode",addressPincode);

                df.set(mapAddress, SetOptions.merge())
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            loadingPB.setVisibility(View.GONE);
                            Toast.makeText(SellerAddAddressActivity.this, "Data Added Successfully", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        loadingPB.setVisibility(View.GONE);
                        Toast.makeText(SellerAddAddressActivity.this, "Data Not Added", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
            startActivity(new Intent(SellerAddAddressActivity.this,SellerAccountActivity.class));
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