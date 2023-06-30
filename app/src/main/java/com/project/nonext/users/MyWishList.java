package com.project.nonext.users;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.project.nonext.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MyWishList extends AppCompatActivity {

    RecyclerView MyWishListRec;
    ArrayList<MyCart_Item_Model> MyWishListDataList;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String UserId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
    MyWishList_Adapter WishListAdapter;
    String productId,imageUri,sellerId,title,price,qty;



    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_my_wish_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("My Wishlist");
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        MyWishListRec = (RecyclerView) findViewById(R.id.MyWishList_recview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        MyWishListRec.setLayoutManager(linearLayoutManager);


        MyWishListDataList = new ArrayList<>();
        WishListAdapter = new MyWishList_Adapter(MyWishListDataList);
        MyWishListRec.setAdapter(WishListAdapter);
        if (MyWishListDataList != null) {

            db.collection("Wishlist")
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @SuppressLint("NotifyDataSetChanged")
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot d : list) {

                                MyCart_Item_Model obj = d.toObject(MyCart_Item_Model.class);
                                MyWishListDataList.add(obj);

                            }
                            WishListAdapter.notifyDataSetChanged();

                        }
                    });
        }
    }
}