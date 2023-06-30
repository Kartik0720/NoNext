package com.project.nonext.users;

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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.project.nonext.NetworkChangeListener;
import com.project.nonext.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class OffersActivity extends AppCompatActivity  {

    RecyclerView recyclerView;
    ArrayList<userOffersModel> offerlist;
    FirebaseFirestore db;
    userOfferAdapter adapter;
    ProgressBar offerProgressBar;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offers);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("All Offers");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        recyclerView = findViewById(R.id.userOffersList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        offerlist = new ArrayList<>();
        adapter=new userOfferAdapter(offerlist);
        recyclerView.setAdapter(adapter);
        offerProgressBar = findViewById(R.id.UserOffersProgressBar);

        db=FirebaseFirestore.getInstance();
        db.collection("Offers").get()
                .addOnSuccessListener((OnSuccessListener<QuerySnapshot>) queryDocumentSnapshots -> {
                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();

                    for (DocumentSnapshot d : list) {

                        userOffersModel obj = d.toObject(userOffersModel.class);
                        offerlist.add(obj);
                        offerProgressBar.setVisibility(View.GONE);

                    }
                    adapter.notifyDataSetChanged();
                });
       /* db.collection("Offers").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> list=queryDocumentSnapshots.getDocuments();
                        for(DocumentSnapshot d:list){
                            userOffersModel obj=d.toObject(userOffersModel.class);

                        }
                        adapter.notifyDataSetChanged();
                    }
                });*/
       /* FirebaseUser user = mAuth.getCurrentUser();
        offerTitle = findViewById(R.id.offerTextView);
        offerdate = findViewById(R.id.useroffersdate);
        userOfferDetailImg = findViewById(R.id.offerImageView);*/


       /* df = fStore.collection("Offers").document();
        df.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                offerTitle.setText(value.getString("offerCode"));
                offerdate.setText(value.getString("date"));
                String uri = value.getString("url");
                Picasso.get().load(uri).error(R.drawable.logo).into(userOfferDetailImg);
            }
        });*/

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
