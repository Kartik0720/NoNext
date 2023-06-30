package com.project.nonext.seller;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.project.nonext.NetworkChangeListener;
import com.project.nonext.R;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class SellerProdDetails extends AppCompatActivity {

    private TextView SellerProductDetailTitle,SellerProductDetailPrice,SellerProductDetailHighlight,SellerProductDetailDescription;
    private Button editbtn,deletebtn;
    private FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    DocumentReference df;
    public String productId;
    private ImageView sellerProductDetailImg;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_prod_details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Product");
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        FirebaseUser user = mAuth.getCurrentUser();
        productId = getIntent().getStringExtra("uProdId");
        SellerProductDetailTitle = findViewById(R.id.SellerProductDetailTitle);
        SellerProductDetailPrice = findViewById(R.id.SellerProductDetailPrice);
        sellerProductDetailImg = findViewById(R.id.sellerProductDetailImg);
        SellerProductDetailHighlight = findViewById(R.id.SellerProductDetailHighlightDesc);
        SellerProductDetailDescription = findViewById(R.id.SellerProductDetailDescriptionDesc);
        editbtn = findViewById(R.id.edit_btn);
        deletebtn = findViewById(R.id.delete_btn);

        df = fStore.collection("Products").document(productId);
        df.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                SellerProductDetailTitle.setText(Objects.requireNonNull(value).getString("title"));
                SellerProductDetailPrice.setText(value.getString("prices"));
                SellerProductDetailHighlight.setText(value.getString("highlights"));
                SellerProductDetailDescription.setText(value.getString("description"));
                String uri = value.getString("url");
                Picasso.get().load(uri).error(R.drawable.logo).into(sellerProductDetailImg);
            }
        });
        editbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent prodEdit = new Intent(getApplicationContext(),ProductEditActivity.class);
                prodEdit.putExtra("uProdId",productId);
                startActivity(prodEdit);
            }
        });

        deletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DocumentReference deleteProductRef = fStore.collection("Products").document(productId);

                deleteProductRef.update("delete","yes").addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(SellerProdDetails.this, "Success", Toast.LENGTH_SHORT).show();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SellerProdDetails.this, "Failure", Toast.LENGTH_SHORT).show();

                    }
                });
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