package com.project.nonext.users;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.nonext.R;

public class DeliveryAddress extends AppCompatActivity {

    TextView DeliveryName,DeliveryAdd,DeliveryMobileNo;
    CardView DeliveryAddressCardView;
    Button DeliveryContinue;
    String addressDb,addressNameDb,addressPincodeDb,addressMobileDb,addressCityDb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_delivery_address);

        DeliveryName = findViewById(R.id.DeliveryAddressName);
        DeliveryAdd = findViewById(R.id.DeliveryAddress);
        DeliveryMobileNo = findViewById(R.id.DeliveryAddressMobileNo);
        DeliveryAddressCardView = findViewById(R.id.DeliveryAddressCardView);
        DeliveryContinue = findViewById(R.id.DeliveryAddressContinue);

        DocumentReference AddressPath;
        FirebaseFirestore fStore = FirebaseFirestore.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        assert user != null;
        AddressPath = fStore.collection("Users").document(String.valueOf(user.getUid()));
        AddressPath.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    addressDb = documentSnapshot.getString("address");
                    addressNameDb = documentSnapshot.getString("addressname");
                    addressMobileDb = documentSnapshot.getString("addressmobile");
                    addressCityDb = documentSnapshot.getString("addresscity");
                    addressPincodeDb = documentSnapshot.getString("addresspincode");

                    DeliveryName.setText(addressNameDb);
                    DeliveryAdd.setText(addressDb + " " + addressCityDb + "\n" + addressPincodeDb);
                    DeliveryMobileNo.setText(addressMobileDb);

                }
            }
        });

        DeliveryContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent payMent = new Intent(getApplicationContext(),Payment.class);
                Intent prodDetails = new Intent(getIntent());
                payMent.putExtras(prodDetails);
                payMent.putExtra("deliveryname",addressNameDb);
                payMent.putExtra("deliveryaddress",addressDb);
                payMent.putExtra("deliverypincod",addressPincodeDb);
                payMent.putExtra("deliverymobile",addressMobileDb);
                payMent.putExtra("deliverycity",addressCityDb);
                startActivity(payMent);
                finish();
            }
        });


    }
}