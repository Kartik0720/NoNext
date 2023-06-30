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
import java.util.Objects;

public class ElectronicsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<ProductsModel> productsArrayList;
    MyAdapter myAdapter;
    FirebaseFirestore db;
    ImageView electronicsCart;
     private FirestoreRecyclerAdapter adapter;
//    ProgressDialog progressDialog;
    ProgressBar electronicsProgressBar;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @SuppressLint("CutPasteId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_electronics);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Electronics");
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

//        progressDialog = new ProgressDialog(this);
//        progressDialog.setCancelable(false);
//        progressDialog.setMessage("Fetching Data...");
//        progressDialog.show();
        electronicsProgressBar = findViewById(R.id.electronicsProgressBar);
        recyclerView= findViewById(R.id.RecElectro);
        electronicsCart = findViewById(R.id.electronicsCart);
        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = FirebaseFirestore.getInstance();
        productsArrayList = new ArrayList<ProductsModel>();
        myAdapter  = new MyAdapter(productsArrayList);
        recyclerView.setAdapter(myAdapter);

        final SwipeRefreshLayout pullToRefresh = findViewById(R.id.electronicsRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                productsArrayList.clear();
                ViewProductListener(); // your code
                pullToRefresh.setRefreshing(false);
            }
        });
        electronicsCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ElectronicsActivity.this,MyCartActivity.class));
            }
        });
        ViewProductListener();
//        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
//
//        //Quire
//        Query query = firebaseFirestore.collection("Products");
//
//        FirestoreRecyclerOptions<ProductsModal> option = new FirestoreRecyclerOptions.Builder<ProductsModal>()
//                .setQuery(query, ProductsModal.class)
//                .build();
//
//        adapter = new FirestoreRecyclerAdapter<ProductsModal, ProductsViewHolder>(option) {
//
//            @SuppressLint("SetTextI18n")
//            @Override
//            protected void onBindViewHolder(@NonNull ProductsViewHolder holder, int position, @NonNull ProductsModal model) {
//
//                holder.list_name.setText(model.getName());
//                holder.list_price.setText(model.getPrice() + "");
//            }
//
//            @NonNull
//            @Override
//            public ProductsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.products_recview, parent, false);
//                return new ProductsViewHolder(view);
//            }
//            @Override
//            public void onError(FirebaseFirestoreException e) {
//                Log.e("error", e.getMessage());
//            }
//        };
//        mFirestoreList.setHasFixedSize(true);
//        mFirestoreList.setLayoutManager(new LinearLayoutManager(this));
//        mFirestoreList.setAdapter(adapter);
//
//    }
//    private static class ProductsViewHolder extends RecyclerView.ViewHolder {
//
//        private final TextView list_name;
//        private final TextView list_price;
//
//        public ProductsViewHolder(@NonNull View itemView) {
//            super(itemView);
//
//            list_name = itemView.findViewById(R.id.TxtTitle);
//            list_price = itemView.findViewById(R.id.TxtPrice);
//
//        }
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        adapter.stopListening();
//    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        adapter.startListening();
    }
    private void ViewProductListener(){
        db.collection("Products")
                .whereEqualTo("categories","Electronics")
                .whereEqualTo("delete","no")
            .addSnapshotListener(new EventListener<QuerySnapshot>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error!=null){
//                            if(progressDialog.isShowing())
//                               progressDialog.dismiss();
                    electronicsProgressBar.setVisibility(View.GONE);
                    Log.e("Firestore Error",error.getMessage());
                    return;
                }
                assert value != null;
                for(DocumentChange dc : value.getDocumentChanges()) {
                    if (dc.getType() == DocumentChange.Type.ADDED) {
                        productsArrayList.add(dc.getDocument().toObject(ProductsModel.class));
                    }
                    myAdapter.notifyDataSetChanged();
//                            if(progressDialog.isShowing())
//                                progressDialog.dismiss();
                    electronicsProgressBar.setVisibility(View.GONE);
                }
            }
            });
//        checking of document Id
           /* String abc = db.collection("Products").document().getId();
            db.collection("Products").document(abc).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {

                    Log.v("abc","abc "+ abc);
                }
            });
            Log.v("abc","abc "+ abc);*/
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




