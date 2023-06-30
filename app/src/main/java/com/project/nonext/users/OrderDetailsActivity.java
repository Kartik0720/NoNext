package com.project.nonext.users;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.project.nonext.R;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class OrderDetailsActivity extends AppCompatActivity {
    TextView myOrdersProductDetailTitle,myOrdersProductDetailPrice;
    ImageView myOrdersProductDetailImageView;
    DocumentReference df;
    FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();
    String orderId,imageUri,status;
    View myOrdersViewOrderPlaced,myOrdersViewOrderConfirmed,myOrdersViewOrderPacked,myOrdersViewOrderShipped,myOrdersViewOrderDelivered;
    View myOrdersHorizontalDelivered,myOrdersHorizontalShipped,myOrdersHorizontalPacked,myOrdersHorizontalConfirmed;
    TextView myOrdersTextShipAddress,myOrdersTextShipPincode,myOrdersTextBillPincode,myOrdersTextBillAddress,myOrdersTextPayment;
    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_order_details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Order Details");
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        myOrdersProductDetailTitle = findViewById(R.id.myOrdersProductDetailTitle);
        myOrdersProductDetailPrice = findViewById(R.id.myOrdersProductDetailPrice);
        myOrdersProductDetailImageView = findViewById(R.id.myOrdersProductDetailImageView);
        myOrdersViewOrderPlaced = findViewById(R.id.myOrdersViewOrderPlaced);
        myOrdersViewOrderConfirmed = findViewById(R.id.myOrdersViewOrderConfirmed);
        myOrdersViewOrderPacked = findViewById(R.id.myOrdersViewOrderPacked);
        myOrdersViewOrderShipped = findViewById(R.id.myOrdersViewOrderShipped);
        myOrdersViewOrderDelivered = findViewById(R.id.myOrdersViewOrderDelivered);

        myOrdersHorizontalDelivered = findViewById(R.id.myOrdersHorizontalDelivered);
        myOrdersHorizontalShipped = findViewById(R.id.myOrdersHorizontalShipped);
        myOrdersHorizontalPacked = findViewById(R.id.myOrdersHorizontalPacked);
        myOrdersHorizontalConfirmed = findViewById(R.id.myOrdersHorizontalConfirmed);
        myOrdersTextShipAddress = findViewById(R.id.myOrdersTextShipAddress);
        myOrdersTextShipPincode = findViewById(R.id.myOrdersTextShipPincode);
        myOrdersTextBillPincode = findViewById(R.id.myOrdersTextBillPincode);
        myOrdersTextBillAddress = findViewById(R.id.myOrdersTextBillAddress);
        myOrdersTextPayment = findViewById(R.id.myOrdersTextPayment);

        orderId = getIntent().getStringExtra("orderId");
        df = fStore.collection("Orders").document(orderId);
        df.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                myOrdersProductDetailTitle.setText(value.getString("title"));
                myOrdersProductDetailPrice.setText(value.getString("price"));
                myOrdersTextShipAddress.setText(value.getString("address"));
                myOrdersTextBillAddress.setText(value.getString("address"));
                myOrdersTextShipPincode.setText(value.getString("pincode"));
                myOrdersTextBillPincode.setText(value.getString("pincode"));
                imageUri = value.getString("url");
                Picasso.get().load(imageUri).error(R.drawable.logo).into(myOrdersProductDetailImageView);
                status = value.getString("orderstatus");
                Log.d("abc",status);
                if(status.equals("Pending")){
                    myOrdersViewOrderPlaced.setBackgroundResource(R.drawable.order_status_current);
                    myOrdersViewOrderConfirmed.setBackgroundResource(R.drawable.order_status_remaining);
                    myOrdersViewOrderPacked.setBackgroundResource(R.drawable.order_status_remaining);
                    myOrdersViewOrderShipped.setBackgroundResource(R.drawable.order_status_remaining);
                    myOrdersViewOrderDelivered.setBackgroundResource(R.drawable.order_status_remaining);
                    myOrdersHorizontalDelivered.setBackgroundResource(R.drawable.order_status_remaining);
                    myOrdersHorizontalShipped.setBackgroundResource(R.drawable.order_status_remaining);
                    myOrdersHorizontalPacked.setBackgroundResource(R.drawable.order_status_remaining);
                    myOrdersHorizontalConfirmed.setBackgroundResource(R.drawable.order_status_remaining);
                }else if(status.equals("confirmed")){
                    myOrdersViewOrderPlaced.setBackgroundResource(R.drawable.order_status_current);
                    myOrdersViewOrderConfirmed.setBackgroundResource(R.drawable.order_status_current);
                    myOrdersViewOrderPacked.setBackgroundResource(R.drawable.order_status_remaining);
                    myOrdersViewOrderShipped.setBackgroundResource(R.drawable.order_status_remaining);
                    myOrdersViewOrderDelivered.setBackgroundResource(R.drawable.order_status_remaining);
                    myOrdersHorizontalDelivered.setBackgroundResource(R.drawable.order_status_remaining);
                    myOrdersHorizontalShipped.setBackgroundResource(R.drawable.order_status_remaining);
                    myOrdersHorizontalPacked.setBackgroundResource(R.drawable.order_status_remaining);
                    myOrdersHorizontalConfirmed.setBackgroundResource(R.drawable.order_status_current);
                }else if(status.equals("packed")){
                    myOrdersViewOrderPlaced.setBackgroundResource(R.drawable.order_status_current);
                    myOrdersViewOrderConfirmed.setBackgroundResource(R.drawable.order_status_current);
                    myOrdersViewOrderPacked.setBackgroundResource(R.drawable.order_status_current);
                    myOrdersViewOrderShipped.setBackgroundResource(R.drawable.order_status_remaining);
                    myOrdersViewOrderDelivered.setBackgroundResource(R.drawable.order_status_remaining);
                    myOrdersHorizontalDelivered.setBackgroundResource(R.drawable.order_status_remaining);
                    myOrdersHorizontalShipped.setBackgroundResource(R.drawable.order_status_remaining);
                    myOrdersHorizontalPacked.setBackgroundResource(R.drawable.order_status_current);
                    myOrdersHorizontalConfirmed.setBackgroundResource(R.drawable.order_status_current);
                }else if(status.equals("shipped")){
                    myOrdersViewOrderPlaced.setBackgroundResource(R.drawable.order_status_current);
                    myOrdersViewOrderConfirmed.setBackgroundResource(R.drawable.order_status_current);
                    myOrdersViewOrderPacked.setBackgroundResource(R.drawable.order_status_current);
                    myOrdersViewOrderShipped.setBackgroundResource(R.drawable.order_status_current);
                    myOrdersViewOrderDelivered.setBackgroundResource(R.drawable.order_status_remaining);
                    myOrdersHorizontalDelivered.setBackgroundResource(R.drawable.order_status_remaining);
                    myOrdersHorizontalShipped.setBackgroundResource(R.drawable.order_status_current);
                    myOrdersHorizontalPacked.setBackgroundResource(R.drawable.order_status_current);
                    myOrdersHorizontalConfirmed.setBackgroundResource(R.drawable.order_status_current);
                }else{
                    myOrdersViewOrderPlaced.setBackgroundResource(R.drawable.order_status_current);
                    myOrdersViewOrderConfirmed.setBackgroundResource(R.drawable.order_status_current);
                    myOrdersViewOrderPacked.setBackgroundResource(R.drawable.order_status_current);
                    myOrdersViewOrderShipped.setBackgroundResource(R.drawable.order_status_current);
                    myOrdersViewOrderDelivered.setBackgroundResource(R.drawable.order_status_current);
                    myOrdersHorizontalDelivered.setBackgroundResource(R.drawable.order_status_current);
                    myOrdersHorizontalShipped.setBackgroundResource(R.drawable.order_status_current);
                    myOrdersHorizontalPacked.setBackgroundResource(R.drawable.order_status_current);
                    myOrdersHorizontalConfirmed.setBackgroundResource(R.drawable.order_status_current);
                }
            }
        });
//        myOrdersViewOrderPlaced.setBackgroundResource(R.drawable.order_status_remaining);
    }
}