package com.project.nonext.users;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.project.nonext.R;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ProductDetailActivity extends AppCompatActivity {
    private TextView userProductDetailTitle,userProductDetailPrice,userProductDetailHighlight,userProductDetailDescription,userQty;
    DocumentReference df;
    private ImageView userProductDetailImg;
    String productId,wishlistId;
    String imageUri;
    String sellerId;
    String title;
    String price;
    int totalbill;
    int qty = 1,heart=0,cart=0;
    ImageView plus,minus;
    FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();
    String userId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
    FloatingActionButton addToWishlist;
    Button addToCart,buyNow;


    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_product_detail);



        productId = getIntent().getStringExtra("ProductId");
        sellerId = getIntent().getStringExtra("SellerId");

        userProductDetailTitle = findViewById(R.id.userProductDetailTitle);
        userProductDetailPrice = findViewById(R.id.userProductDetailPrice);
        userProductDetailImg = findViewById(R.id.userProductDetailImg);
        userProductDetailHighlight = findViewById(R.id.userProductDetailHighlightDesc);
        userProductDetailDescription = findViewById(R.id.userProductDetailDescriptionDesc);
        plus = findViewById(R.id.plusQty);
        minus = findViewById(R.id.minusQty);
        userQty = findViewById(R.id.ProdQty);
        addToWishlist = findViewById(R.id.AddToWishList);

        addToCart = findViewById(R.id.add_to_cart);
        buyNow = findViewById(R.id.buy_now);
        Log.v("productId",productId);

        df = fStore.collection("Products").document(productId);
        df.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                assert value != null;
                userProductDetailTitle.setText(value.getString("title"));
                userProductDetailPrice.setText(value.getString("prices"));
                userProductDetailHighlight.setText(value.getString("highlights"));
                userProductDetailDescription.setText(value.getString("description"));
                imageUri = value.getString("url");
                Picasso.get().load(imageUri).error(R.drawable.logo).into(userProductDetailImg);

//                Check Item is available on database
                if(heart == 0){
                    Task<Object> a = fStore.collection("Wishlist")
                            .whereEqualTo("prodId",productId)
                            .get()
                            .continueWith(new Continuation<QuerySnapshot, Object>() {
                                @Override
                                public Object then(@NonNull Task<QuerySnapshot> task) throws Exception {
                                    if(task.isSuccessful()){
                                        if(task.getResult().isEmpty()){
                                            heart = 0;
                                        }else{
                                            Log.d("abc","pink");
                                            addToWishlist.setImageResource(R.drawable.heart_red);
                                            heart = 1;
                                        }

                                    }
                                    return null;
                                }
                            });
                    if(cart==0){
                         fStore.collection("Cart")
                        .whereEqualTo("prodId",productId)
                        .get()
                        .continueWith(new Continuation<QuerySnapshot, Object>() {
                            @Override
                            public Object then(@NonNull Task<QuerySnapshot> task) throws Exception {
                                if(task.isSuccessful()){
                                    if(task.getResult().isEmpty()){
                                        cart = 0;
                                    }else{
                                        addToCart.setText("Go To Cart");
                                        cart = 1;
                                    }

                                }
                                return null;
                            }
                        });
                    }
//                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                                @Override
//                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                                    addToWishlist.setImageResource(R.drawable.heart_pink);
//                                    heart = 1;
//                                }
//                            });

                }

                title = value.getString("title");
                price = value.getString("prices");

            }
        });

        plus.setOnClickListener(view ->{
            if(qty<=5) {
                qty = qty + 1;
                userQty.setText(String.valueOf(qty));
            }else{
                userQty.setText(String.valueOf(qty));
            }
        });

        minus.setOnClickListener(view -> {
            if(qty>1){
                qty = qty - 1;
                userQty.setText(String.valueOf(qty));
            }else{
                qty = 1;
                userQty.setText(String.valueOf(qty));
            }
        });

        addToCart.setOnClickListener(view -> {
            if(user != null) {
                if(cart==0) {
                    addToCartUser();
                }else {
                    startActivity(new Intent(getApplicationContext(),MyCartActivity.class));
                }
            }else{
                Toast.makeText(getApplicationContext(), "Please Login", Toast.LENGTH_SHORT).show();

            }
        });

        addToWishlist.setOnClickListener(view -> {
            if(heart == 0) {
                AddToWishlist();
                addToWishlist.setImageResource(R.drawable.heart_red);
                heart = 1;
            }else{
                deleteToWishlist();
                addToWishlist.setImageResource(R.drawable.heart);
            }

        });

        buyNow.setOnClickListener(view -> {
            if(user != null) {
//                addToCartUser();
                Intent PDetails = new Intent(getApplicationContext(), DeliveryAddress.class);
                PDetails.putExtra("sellerId",sellerId);
                PDetails.putExtra("prodId",productId);
                PDetails.putExtra("title",title);
                PDetails.putExtra("Price",price);
                PDetails.putExtra("ImgUrl",imageUri);
                PDetails.putExtra("qty",  String.valueOf(qty));
                totalbill = Integer.parseInt(price)  * qty;
                PDetails.putExtra("totalamount",String.valueOf(totalbill));
                PDetails.putExtra("Type","Prod");
                startActivity(PDetails);


            }else{
                Toast.makeText(getApplicationContext(), "Please Login", Toast.LENGTH_SHORT).show();

            }

        });
    }

    private void deleteToWishlist() {
          fStore.collection("Wishlist")
                .whereEqualTo("prodId",productId)
                .get().continueWith(new Continuation<QuerySnapshot, Object>() {

                    @Override
                    public Object then(@NonNull Task<QuerySnapshot> task) throws Exception {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot document : task.getResult()){
//                                Log.e("test123",document.getId()+"=>"+document.getData());
                                wishlistId = document.getId();
//                                Log.e("test12",wishlistId);
                                DocumentReference deleteWishlist =  fStore.collection("Wishlist")
                                        .document(wishlistId);

                                deleteWishlist.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(getApplicationContext(), "successfully Removed", Toast.LENGTH_SHORT).show();
                                        heart = 0;
                                    }
                                });
                            }
                        }
                        return null;
                    }
                });


    }

    private void AddToWishlist() {
//        set Path
        @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        @SuppressLint("SimpleDateFormat") DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        Date date = Calendar.getInstance().getTime();
        Date time = Calendar.getInstance().getTime();

        CollectionReference addWishlist = fStore.collection("Wishlist");

        String prodId = productId;
        String title = userProductDetailTitle.getText().toString();
        String price = userProductDetailPrice.getText().toString();

        Map<String,String> WishlistProd = new HashMap<>();

        WishlistProd.put("prodId",prodId);
        WishlistProd.put("title",title);
        WishlistProd.put("price",price);
        WishlistProd.put("qty",String.valueOf(qty));
        WishlistProd.put("url",imageUri.toString());
        WishlistProd.put("delete","no");
        WishlistProd.put("userId", String.valueOf(userId));
        WishlistProd.put("date",dateFormat.format(date));
        WishlistProd.put("time", timeFormat.format(time));
        WishlistProd.put("sellerId",sellerId);

        addWishlist.add(WishlistProd).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                documentReference.update("WishlistId",documentReference.getId());
                Toast.makeText(getApplicationContext(), "successfully added", Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void addToCartUser() {
//        set Path
        @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        @SuppressLint("SimpleDateFormat") DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        Date date = Calendar.getInstance().getTime();
        Date time = Calendar.getInstance().getTime();

        CollectionReference addCart = fStore.collection("Cart");

        String prodId = productId;
        String title = userProductDetailTitle.getText().toString();
        String price = userProductDetailPrice.getText().toString();

        Map<String,String> CartProd = new HashMap<>();

        CartProd.put("prodId",prodId);
        CartProd.put("title",title);
        CartProd.put("price",price);
        CartProd.put("qty",String.valueOf(qty));
        CartProd.put("url",imageUri.toString());
        CartProd.put("delete","no");
        CartProd.put("userId", String.valueOf(userId));
        CartProd.put("date",dateFormat.format(date));
        CartProd.put("time", timeFormat.format(time));
        CartProd.put("sellerId",sellerId);
        CartProd.put("save","no");

        addCart.add(CartProd).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                documentReference.update("cartId",documentReference.getId());
                Toast.makeText(getApplicationContext(), "successfully added", Toast.LENGTH_SHORT).show();
                addToCart.setText("Go To Cart");
                cart=1;
            }
        });


    }
}