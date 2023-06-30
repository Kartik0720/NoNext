package com.project.nonext.users;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

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

public class MyOrdersActivity extends AppCompatActivity {
    RecyclerView recview;
    ArrayList<OrdersModel> Orderdatalist;
    FirebaseFirestore fStore;
    OrdersAdapter userOrdersAdapter;
    sellerViewAdapter sellerVieAdapter;
    private FirebaseAuth mAuth;
    Task<QuerySnapshot> df;
    ProgressBar myOrderProgressBar;
    TextView orderEmpty;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("My Orders");
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        recview=(RecyclerView)findViewById(R.id.RecOrders);
        recview.setLayoutManager(new LinearLayoutManager(this));
        Orderdatalist=new ArrayList<>();
        userOrdersAdapter = new OrdersAdapter(Orderdatalist);
        recview.setAdapter(userOrdersAdapter);
        myOrderProgressBar = findViewById(R.id.UserOrderProgressBar);
        orderEmpty = findViewById(R.id.MyOrder_No_Product);

        mAuth=FirebaseAuth.getInstance();
        String user = mAuth.getUid();
//        FirebaseUser user = mAuth.getCurrentUser();
        fStore=FirebaseFirestore.getInstance();
//        df = fStore.collection("Operators").document("Users").collection(user).document("userregister").getParent().get()
        df =  fStore.collection("Orders").whereEqualTo("userId",user).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot d : list) {
                           OrdersModel obj = d.toObject(OrdersModel.class);
                            Orderdatalist.add(obj);
                            myOrderProgressBar.setVisibility(View.GONE);
                        }
                        if(list.isEmpty()){
                            empty();
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
    public void empty(){
        orderEmpty.setVisibility(View.VISIBLE);
        myOrderProgressBar.setVisibility(View.GONE);
        recview.setVisibility(View.GONE);
    }
}