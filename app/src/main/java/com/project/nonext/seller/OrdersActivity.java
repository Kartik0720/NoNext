package com.project.nonext.seller;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.project.nonext.NetworkChangeListener;
import com.project.nonext.R;
import com.project.nonext.admin.sellerViewAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class OrdersActivity extends AppCompatActivity {
    RecyclerView recview;
    ArrayList<SellerOrdersModel> Orderdatalist;
    FirebaseFirestore fStore;
    SellerOrdersAdapter userOrdersAdapter;
    sellerViewAdapter sellerVieAdapter;
    private FirebaseAuth mAuth;
    Task<QuerySnapshot> df;
    ProgressBar myOrderProgressBar;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_orders);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("My Orders");
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        recview=(RecyclerView)findViewById(R.id.RecSellerOrders);
        recview.setLayoutManager(new LinearLayoutManager(this));
        Orderdatalist=new ArrayList<>();
        userOrdersAdapter = new SellerOrdersAdapter(Orderdatalist);
        recview.setAdapter(userOrdersAdapter);
        myOrderProgressBar = findViewById(R.id.sellerOrderProgressBar);

        mAuth=FirebaseAuth.getInstance();
        String user = mAuth.getUid();
//        FirebaseUser user = mAuth.getCurrentUser();
        fStore=FirebaseFirestore.getInstance();
//        df = fStore.collection("Operators").document("Users").collection(user).document("userregister").getParent().get()
        df =  fStore.collection("Orders").whereEqualTo("sellerId",user).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot d : list) {
                            SellerOrdersModel obj = d.toObject(SellerOrdersModel.class);
                            Orderdatalist.add(obj);
                            myOrderProgressBar.setVisibility(View.GONE);
                        }
                        userOrdersAdapter.notifyDataSetChanged();
                        myOrderProgressBar.setVisibility(View.GONE);
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