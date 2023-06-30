package com.project.nonext.seller;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.project.nonext.NetworkChangeListener;
import com.project.nonext.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ProductActivity extends AppCompatActivity {

    RecyclerView SellerProdRec;
    ArrayList<sellerprodmodel> ProdDataList;
    FirebaseFirestore db;
    SellerProdAdapter ProdAdapter;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    String UserId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();


    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_product);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("My Products");
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        FloatingActionButton fab = findViewById(R.id.floating_btn);

        SellerProdRec = findViewById(R.id.sellerprodrec);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        SellerProdRec.setLayoutManager(linearLayoutManager);
//        SellerProdRec.setLayoutManager(new GridLayoutManager(this,2));
        ProdDataList = new ArrayList<>();

        ProdAdapter = new SellerProdAdapter(ProdDataList);
        SellerProdRec.setAdapter(ProdAdapter);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),seller_add_product.class));
                Toast.makeText(getApplicationContext(),"you clicked on add product",Toast.LENGTH_SHORT).show();
            }
        });

        db = FirebaseFirestore.getInstance();

        db.collection("Products").whereEqualTo("sellerId",UserId).whereEqualTo("delete","no").get()
                .addOnSuccessListener((OnSuccessListener<QuerySnapshot>) queryDocumentSnapshots -> {
                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();

                    for (DocumentSnapshot d : list) {

                        sellerprodmodel obj = d.toObject(sellerprodmodel.class);
                        ProdDataList.add(obj);

                    }
                    ProdAdapter.notifyDataSetChanged();
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