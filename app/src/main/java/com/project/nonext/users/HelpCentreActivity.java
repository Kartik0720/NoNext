package com.project.nonext.users;

import android.content.Intent;
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
import com.google.android.material.card.MaterialCardView;
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

public class HelpCentreActivity extends AppCompatActivity {
    RecyclerView recview;
    ArrayList<OrdersModel> Orderdatalist;
    FirebaseFirestore fStore;
    OrdersAdapter userOrdersAdapter;
    sellerViewAdapter sellerVieAdapter;
    private FirebaseAuth mAuth;
    Task<QuerySnapshot> df;
    ProgressBar myOrderProgressBar;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    MaterialCardView trackMyo;
    MaterialCardView ManageOrder;
    MaterialCardView returns;
    MaterialCardView otherIsu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_centre);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("24x7 Customer Support");
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        trackMyo=findViewById(R.id.trackMyOrder);
        ManageOrder=findViewById(R.id.isuOrders);
        returns=findViewById(R.id.returnsCardView);
        otherIsu=findViewById(R.id.otherCardView);
        recview=(RecyclerView)findViewById(R.id.HelpRecOrders);
        recview.setLayoutManager(new LinearLayoutManager(this));
        Orderdatalist=new ArrayList<>();
        userOrdersAdapter = new OrdersAdapter(Orderdatalist);
        recview.setAdapter(userOrdersAdapter);
      /*  myOrderProgressBar = findViewById(R.id.UserOrderProgressBar);*/

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
                         /*   myOrderProgressBar.setVisibility(View.GONE);*/
                        }
                        userOrdersAdapter.notifyDataSetChanged();
                     /*   myOrderProgressBar.setVisibility(View.GONE);*/
                    }
                });
        trackMyo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HelpCentreActivity.this,TrackMyOrder.class));
            }
        });
        ManageOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HelpCentreActivity.this,MyOrdersActivity.class));
            }
        });
        returns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HelpCentreActivity.this,ReturnOrders.class));
            }
        });
        otherIsu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HelpCentreActivity.this,FeedbackUser.class));
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