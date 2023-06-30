package com.project.nonext.users;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.project.nonext.R;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Payment extends AppCompatActivity  implements PaymentResultListener {
    private static final String TAG = "Payment";
    RadioButton radioButtonPayOnline,radioButtonCOD;
    TextView paymentAmount;
    Button paymentBtn;
    String totalprice,totalItem;
    String ProdId,SellerId,ProdTitle,ProdPrice,ProdImgUrl,prodQty,Cart;
    String addressName, deliverAddress,pincod,mobile,addressCity;
    int amount;
    String UserId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

    FirebaseFirestore fStore = FirebaseFirestore.getInstance();

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_payment);

        paymentAmount = findViewById(R.id.Payment_Total_Amount);
        paymentBtn = findViewById(R.id.PaymentBtn);
        radioButtonCOD = findViewById(R.id.radioButtonCOD);
        radioButtonPayOnline = findViewById(R.id.radioButtonPayOnline);


//        Deliver Details
        addressName = getIntent().getStringExtra("deliveryname");
        deliverAddress = getIntent().getStringExtra("deliveryaddress");
        pincod = getIntent().getStringExtra("deliverypincod");
        mobile = getIntent().getStringExtra("deliverymobile");
        addressCity = getIntent().getStringExtra("deliverycity");


//        Product Details
        SellerId = getIntent().getStringExtra("sellerId");
        ProdId = getIntent().getStringExtra("prodId");
        ProdTitle = getIntent().getStringExtra("title");
        ProdPrice = getIntent().getStringExtra("Price");
        ProdImgUrl = getIntent().getStringExtra("ImgUrl");
        prodQty = getIntent().getStringExtra("qty");
        totalprice = getIntent().getStringExtra("totalamount");
        Cart = getIntent().getStringExtra("Type");
        paymentAmount.setText(totalprice+"/-");

        amount = Integer.parseInt(totalprice);
        paymentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(Cart.equals("Cart")){
                    fStore.collection("Cart")
                    .get()
                    .continueWith(new Continuation<QuerySnapshot, Object>() {

                        @Override
                        public Object then(@NonNull Task<QuerySnapshot> task) throws Exception {
                            if(task.isSuccessful()){
                                for(QueryDocumentSnapshot document : task.getResult()){
//                                    DocumentReference clearCart =  fStore.collection("Cart")
//                                            .document(document.getId());

                                    Map<String, Object> data = document.getData();
                                    SellerId = String.valueOf(data.get("sellerId"));
                                    ProdId = String.valueOf(data.get("prodId"));
                                    ProdTitle = String.valueOf(data.get("title"));
                                    ProdPrice = String.valueOf(data.get("price"));
                                    ProdImgUrl = String.valueOf(data.get("url"));
                                    prodQty = String.valueOf(data.get("qty"));
                                    Log.d(TAG,Cart);

//                                    placeOrder();
                                }
                                paymentMethod();
                            }
                            return null;
                        }
                    });

                }
                else if(Cart.equals("Prod")){
                    Log.d(TAG,ProdTitle);
                    paymentMethod();
//                    Intent prodpage = new Intent(getApplicationContext(),ProductDetailActivity.class);
//                    prodpage.putExtra("ProductId",ProdId);
//                    startActivity(prodpage);
//                    placeOrder();
                    finish();
                }

            }
        });
    }

    private void placeOrder() {
        Log.d("abcd","Inside Order");
        @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        @SuppressLint("SimpleDateFormat") DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        Date date = Calendar.getInstance().getTime();
        Date time = Calendar.getInstance().getTime();

        Map<String,String> OrderInfo = new HashMap<>();

        OrderInfo.put("prodId",ProdId);
        OrderInfo.put("sellerId",SellerId);
        OrderInfo.put("userId",UserId);

        OrderInfo.put("title",ProdTitle);
        OrderInfo.put("price",ProdPrice);
        OrderInfo.put("url",ProdImgUrl);
        OrderInfo.put("qty",prodQty);

        OrderInfo.put("deliveryprice","free");
        OrderInfo.put("discountprice","0");
        OrderInfo.put("orderstatus","Pending");
        OrderInfo.put("orderdate", dateFormat.format(date));
        OrderInfo.put("time", timeFormat.format(time));
        OrderInfo.put("paymentmethod","COD");

        OrderInfo.put("fullname",addressName);
        OrderInfo.put("address", deliverAddress);
        OrderInfo.put("pincode",pincod);
        OrderInfo.put("mobile",mobile);
        OrderInfo.put("deliverycity",addressCity);
        OrderInfo.put("paymentamount",totalprice);

        CollectionReference documentReference = fStore.collection("Orders");

        documentReference.add(OrderInfo).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {

                documentReference.update("orderId",documentReference.getId());
                Toast.makeText(getApplicationContext(), "Order Place successfully", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(Payment.this,MyOrdersActivity.class);
                startActivity(i);
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Payment.this, "Order Failed", Toast.LENGTH_SHORT).show();
            }
        });


    }

    //Payment Method
    public void paymentMethod(){
        Log.d(TAG, String.valueOf(radioButtonCOD.isChecked()));
        if(radioButtonPayOnline.isChecked()){
            Checkout checkout = new Checkout();

            final Activity activity = Payment.this;

            try {
                JSONObject options = new JSONObject();
                //Set Company Name
                options.put("name", "NO NEXT App");
                //Ref no
                options.put("description", "Reference No. #123456");
                //Image to be display
                options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
                //options.put("order_id", "order_9A33XWu170gUtm");
                // Currency type
                options.put("currency", "INR");
                //double total = Double.parseDouble(mAmountText.getText().toString());
                //multiply with 100 to get exact amount in rupee
//            ProdPrice = 100 * ProdPrice;
                //amount
                options.put("amount", amount*100);
//                JSONObject preFill = new JSONObject();
//                //email
//                preFill.put("email", "knavadia20@gmail.com");
//                //contact
//                preFill.put("contact", "7622907087");
//
//                options.put("prefill", preFill);

                checkout.open(activity, options);
            } catch (Exception e) {
                Log.e("TAG", "Error in starting Razorpay Checkout", e);
            }
        }
        else if(radioButtonCOD.isChecked()){
            if(Cart.equals("Cart")){
                fStore.collection("Cart")
                .get()
                .continueWith(new Continuation<QuerySnapshot, Object>() {

                    @Override
                    public Object then(@NonNull Task<QuerySnapshot> task) throws Exception {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("abcd","Inside COD");
                                DocumentReference clearCart = fStore.collection("Cart")
                                        .document(document.getId());
                                placeOrder();
                                clearCart.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(getApplicationContext(), "successfully Removed", Toast.LENGTH_SHORT).show();
//                                Intent CartPage = new Intent(getApplicationContext(),MyCartActivity.class);
//                                startActivity(CartPage);
//                                finish();
                                    }
                                });
                            }
                        }
                        return null;
                    }
                });
            }
            else{
                placeOrder();
            }

        }
    }

    @Override
    public void onPaymentSuccess(String s) {
        Log.d(TAG,"Payment Success");
        Toast.makeText(this, "Payment Successfully", Toast.LENGTH_SHORT).show();
        if(Cart.equals("Cart")) {

            fStore.collection("Cart")
                    .get()
                    .continueWith(new Continuation<QuerySnapshot, Object>() {

                        @Override
                        public Object then(@NonNull Task<QuerySnapshot> task) throws Exception {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    DocumentReference clearCart = fStore.collection("Cart")
                                            .document(document.getId());
                                    placeOrder();
                                    clearCart.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(getApplicationContext(), "successfully Removed", Toast.LENGTH_SHORT).show();
//                                Intent CartPage = new Intent(getApplicationContext(),MyCartActivity.class);
//                                startActivity(CartPage);
//                                finish();
                                        }
                                    });
                                }
                            }
                            return null;
                        }
                    });
        }else{
            Log.d(TAG,"placeOrder else");
            placeOrder();
        }
    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(this, "Payment Failed", Toast.LENGTH_SHORT).show();
    }
}