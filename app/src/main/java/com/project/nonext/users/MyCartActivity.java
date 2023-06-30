package com.project.nonext.users;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.project.nonext.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MyCartActivity extends AppCompatActivity {

    RecyclerView MyCartRec;
    ArrayList<MyCart_Item_Model> MyCartDataList;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String UserId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
    MyCart_Adapter CartAdapter;
    TextView totalAmount,cartTotalItems,totalPrice,totalItemsAmount,myCartNoProduct;
    Button PlaceOrder;
    String productId,imageUri,sellerId,title,price,qty;
    ConstraintLayout myCartProdDetail;
    LinearLayout linearLayout;

    int totalBill,totalItems;

//    String CartDetails [] [] = new String[10][10];
    String CartDetails [] = new String[10];

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_my_cart);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("My Cart");
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        MyCartRec = (RecyclerView) findViewById(R.id.MyCart_recview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        MyCartRec.setLayoutManager(linearLayoutManager);
        totalPrice = findViewById(R.id.MyCart_Total_Amount);
        totalAmount = findViewById(R.id.Cart_Total_Amount);
        cartTotalItems = findViewById(R.id.MyCart_Total_Items);
        totalItemsAmount = findViewById(R.id.MyCart_Total_Item_price);
        PlaceOrder = findViewById(R.id.MyCart_Continue_Btn);
        myCartNoProduct = findViewById(R.id.MyCart_No_Product);
        myCartProdDetail = findViewById(R.id.MyCart_Prod_Details);
        linearLayout = findViewById(R.id.linearLayout);

        LocalBroadcastManager.getInstance(this).registerReceiver(mTotalAmount,new IntentFilter("MyTotalAmount"));
        LocalBroadcastManager.getInstance(this).registerReceiver(mTotalItems,new IntentFilter("MyTotalItems"));

        MyCartDataList = new ArrayList<>();
        CartAdapter = new MyCart_Adapter(MyCartDataList);
        MyCartRec.setAdapter(CartAdapter);
        if(MyCartDataList != null) {

            db.collection("Cart")
                    .whereEqualTo("userId",UserId)
                    .whereEqualTo("save", "no")
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @SuppressLint("NotifyDataSetChanged")
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot d : list) {

                                MyCart_Item_Model obj = d.toObject(MyCart_Item_Model.class);
                                MyCartDataList.add(obj);

                            }
                            if (list.isEmpty()) {
                                empty();
                            }
                            CartAdapter.notifyDataSetChanged();

                        }
                    });
            PlaceOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Toast.makeText(MyCartActivity.this, "continue", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(getApplicationContext(), DeliveryAddress.class);
                    i.putExtra("sellerId",sellerId);
                    i.putExtra("prodId",productId);
                    i.putExtra("title",title);
                    i.putExtra("Price",price);
                    i.putExtra("ImgUrl",imageUri);
                    i.putExtra("qty",  String.valueOf(qty));
                    i.putExtra("totalamount",String.valueOf(totalBill));
                    i.putExtra("Type","Cart");
                    startActivity(i);
                }
            });
        }

    }

    public BroadcastReceiver mTotalAmount = new BroadcastReceiver() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onReceive(Context context, Intent intent) {
            totalBill = intent.getIntExtra("cartTotalAmount",0);
            totalAmount.setText(totalBill+"/-");
            totalPrice.setText(totalBill+"/-");
            totalItemsAmount.setText(totalBill+"/-");
            CartDetails[2] = String.valueOf(totalAmount);
            Log.e("bill", String.valueOf(totalBill));
        }
    };
    public BroadcastReceiver mTotalItems =new BroadcastReceiver() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onReceive(Context context, Intent intent) {
            totalItems = intent.getIntExtra("cartTotalItems",0);
            cartTotalItems.setText("Price ("+totalItems+" items)");
            Log.e("items", String.valueOf(totalItems));

            sellerId = intent.getStringExtra("sellerId");
            productId = intent.getStringExtra("prodId");
            title = intent.getStringExtra("title");
            price = intent.getStringExtra("price");
            imageUri = intent.getStringExtra("url");
            qty = intent.getStringExtra("qty");
            if(totalItems == 0){
                empty();
            }
        }
    };
    public void empty(){
        MyCartRec.setVisibility(View.GONE);
        myCartProdDetail.setVisibility(View.GONE);
        linearLayout.setVisibility(View.GONE);
        PlaceOrder.setVisibility(View.GONE);
        myCartNoProduct.setVisibility(View.VISIBLE);
        Toast.makeText(this, "Empty", Toast.LENGTH_SHORT).show();

    }

}