package com.project.nonext.users;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.project.nonext.NetworkChangeListener;
import com.project.nonext.R;

import java.util.ArrayList;

public class MobileActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<ProductsModel> productsArrayList;
    MyAdapter myAdapter;
    FirebaseFirestore db;
    private FirestoreRecyclerAdapter adapter;
    //    ProgressDialog progressDialog;
    ProgressBar mobileProgressBar;
    ImageView mobileCart;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @SuppressLint("CutPasteId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_mobile);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Mobiles");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mobileCart = findViewById(R.id.mobileCart);
        mobileProgressBar = findViewById(R.id.mobileProgressBar);
        recyclerView= findViewById(R.id.RecMobile);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        db = FirebaseFirestore.getInstance();
        productsArrayList = new ArrayList<ProductsModel>();
        myAdapter  = new MyAdapter(productsArrayList);
        recyclerView.setAdapter(myAdapter);
        final SwipeRefreshLayout pullToRefresh = findViewById(R.id.mobileRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                productsArrayList.clear();
                ViewProductListener(); // your code
                pullToRefresh.setRefreshing(false);
            }
        });
        mobileCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MobileActivity.this,MyCartActivity.class));
            }
        });
        ViewProductListener();
    }


    private void ViewProductListener(){
        db.collection("Products")
                .whereEqualTo("categories","Mobiles")
                .whereEqualTo("delete","no")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(error!=null){
                            mobileProgressBar.setVisibility(View.GONE);
                            Log.e("Firestore Error",error.getMessage());
                            return;
                        }
                        for(DocumentChange dc : value.getDocumentChanges()) {
                            if (dc.getType() == DocumentChange.Type.ADDED) {
                                productsArrayList.add(dc.getDocument().toObject(ProductsModel.class));
                            }
                            myAdapter.notifyDataSetChanged();
                            mobileProgressBar.setVisibility(View.GONE);
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