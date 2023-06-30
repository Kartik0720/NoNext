package com.project.nonext.users;

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

public class AddAddressActivity extends AppCompatActivity {
    EditText AddAddressPincode,AddAddress,AddAddressFullName,AddAddressCity,AddAddressMobileNo;
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
        setContentView(R.layout.activity_users_add_address);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Add Address");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //findViewById
        AddAddress = findViewById(R.id.edtAddAddress);
        AddAddressFullName = findViewById(R.id.edtAddAddressFullName);
        AddAddressMobileNo = findViewById(R.id.edtAddAddressMobileNo);
        AddAddressCity = findViewById(R.id.edtAddAddressCity);
        AddAddressPincode = findViewById(R.id.edtAddAddressPincode);
        loadingPB = findViewById(R.id.addAddressLoadingPB);
        addBtn = findViewById(R.id.addAddressBtn);
        fStore = FirebaseFirestore.getInstance();


       // df = fStore.collection("Operators").document("Users").collection(user.getUid()).document("userregister");
        df =  fStore.collection("Users").document(String.valueOf(user.getUid()));
        df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    String addressDb = documentSnapshot.getString("address");
                    String addressNameDb = documentSnapshot.getString("addressname");
                    String addressMobileDb = documentSnapshot.getString("addressmobile");
                    String addressCityDb = documentSnapshot.getString("addresscity");
                    String addressPincodeDb = documentSnapshot.getString("addresspincode");
                    AddAddress.setText(addressDb);
                    AddAddressFullName.setText(addressNameDb);
                    AddAddressMobileNo.setText(addressMobileDb);
                    AddAddressCity.setText(addressCityDb);
                    AddAddressPincode.setText(addressPincodeDb);
                    AddAddressFullName.requestFocus();
                }
            }
        });
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingPB.setVisibility(View.VISIBLE);
                String address = AddAddress.getText().toString();
                String addressName = AddAddressFullName.getText().toString();
                String addressMobile = AddAddressMobileNo.getText().toString();
                String addressCity = AddAddressCity.getText().toString();
                String addressPincode = AddAddressPincode.getText().toString();
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
                              Toast.makeText(AddAddressActivity.this, "Data Added Successfully", Toast.LENGTH_SHORT).show();
                         }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            loadingPB.setVisibility(View.GONE);
                            Toast.makeText(AddAddressActivity.this, "Data Not Added", Toast.LENGTH_SHORT).show();
                        }
                });
                
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
            startActivity(new Intent(AddAddressActivity.this,AddressActivity.class));
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