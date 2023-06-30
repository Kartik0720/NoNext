package com.project.nonext.admin;

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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class sellerList extends AppCompatActivity {

    RecyclerView recview;
    ArrayList<sellerModel> sellerdatalist;
    FirebaseFirestore fStore;
    sellerViewAdapter sellerVieAdapter;
    private FirebaseAuth mAuth;
    Task<QuerySnapshot> df;
    ProgressBar progressBar;

    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_seller_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("All seller");
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        progressBar = findViewById(R.id.adminSellerProgressBar);
        recview=(RecyclerView)findViewById(R.id.adminSellerList);
        recview.setLayoutManager(new LinearLayoutManager(this));
        sellerdatalist=new ArrayList<>();
        sellerVieAdapter = new sellerViewAdapter(sellerdatalist);
        recview.setAdapter(sellerVieAdapter);

        mAuth=FirebaseAuth.getInstance();
        String user = mAuth.getUid();
//        FirebaseUser user = mAuth.getCurrentUser();
        fStore=FirebaseFirestore.getInstance();
//        df = fStore.collection("Operators").document("Users").collection(user).document("userregister").getParent().get()
        df =  fStore.collection("Users").whereEqualTo("usertype","Seller").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        progressBar.setVisibility(View.VISIBLE);
                        List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot d : list) {
                            sellerModel obj = d.toObject(sellerModel.class);
                            sellerdatalist.add(obj);
                        }
                        sellerVieAdapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.GONE);
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