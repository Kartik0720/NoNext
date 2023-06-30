package com.project.nonext.users;

import android.annotation.SuppressLint;
import android.content.Intent;
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
import com.project.nonext.R;

import java.util.ArrayList;

public class AppliancesActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<ProductsModel> productsArrayList;
    MyAdapter myAdapter;
    FirebaseFirestore db;
    private FirestoreRecyclerAdapter adapter;
    ProgressBar AppliancesProgressBar;
    ImageView appliancesCart;
    @SuppressLint("CutPasteId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_appliances);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Home Appliances");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        appliancesCart = findViewById(R.id.appliancesCart);
        AppliancesProgressBar = findViewById(R.id.AppliancesProgressBar);
        recyclerView= findViewById(R.id.RecAppliances);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        db = FirebaseFirestore.getInstance();
        productsArrayList = new ArrayList<ProductsModel>();
        myAdapter  = new MyAdapter(productsArrayList);
        recyclerView.setAdapter(myAdapter);
        final SwipeRefreshLayout pullToRefresh = findViewById(R.id.appliancesRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                productsArrayList.clear();
                ViewProductListener(); // your code
                pullToRefresh.setRefreshing(false);
            }
        });
        appliancesCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AppliancesActivity.this,MyCartActivity.class));
            }
        });
        ViewProductListener();
    }


    private void ViewProductListener(){
        db.collection("Products").whereEqualTo("categories","Home Appliances")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(error!=null){
                            AppliancesProgressBar.setVisibility(View.GONE);
                            Log.e("Firestore Error",error.getMessage());
                            return;
                        }
                        for(DocumentChange dc : value.getDocumentChanges()) {
                            if (dc.getType() == DocumentChange.Type.ADDED) {
                                productsArrayList.add(dc.getDocument().toObject(ProductsModel.class));
                            }
                            myAdapter.notifyDataSetChanged();
                            AppliancesProgressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }
}