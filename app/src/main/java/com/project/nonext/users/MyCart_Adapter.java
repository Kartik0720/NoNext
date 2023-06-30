package com.project.nonext.users;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.nonext.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MyCart_Adapter extends RecyclerView.Adapter<MyCart_Adapter.MyCartViewHolder> {

    ArrayList<MyCart_Item_Model> MyCartDataList;
    FirebaseFirestore fStore = FirebaseFirestore.getInstance();
//    FirebaseUser UserId = FirebaseAuth.getInstance().getCurrentUser();

    String CartProdId;
    int totalAmount = 0,items=0;
    Context context;

    public MyCart_Adapter(ArrayList<MyCart_Item_Model> myCartDataList) {
        MyCartDataList = myCartDataList;
    }

    public MyCart_Adapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public MyCartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_users_cart_item_layout,parent,false);
        return new MyCartViewHolder(v);
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyCartViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.MyCartTitle.setText(MyCartDataList.get(position).getTitle());
        holder.MyCartPrice.setText(MyCartDataList.get(position).getPrice()+"/-");
        holder.MyCartQty.setText(MyCartDataList.get(position).getQty());
        CartProdId = MyCartDataList.get(position).getCartId();
        Picasso.get().load(MyCartDataList.get(position).getUrl())
                .error(R.drawable.logo).into(holder.MyCartProdImg);


//                      Delete Product from Cart
        holder.ItemRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DocumentReference RemoveItem = fStore.collection("Cart")
                    .document(MyCartDataList.get(position).getCartId());
                RemoveItem.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        MyCartDataList.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position,getItemCount());
//                        CartAdapter.notifyItemRemoved(MyCartDataList.get(position));
                        Toast.makeText(view.getContext(), "successfully Removed", Toast.LENGTH_SHORT).show();
//                        items = 0;
                        items = getItemCount();
                        Intent totalItems = new Intent("MyTotalItems");
                        totalItems.putExtra("cartTotalItems",items);
                        LocalBroadcastManager.getInstance(context).sendBroadcast(totalItems);
//                        totalAmount = totalAmount - Integer.parseInt(MyCartDataList.get(position).getPrice());
//                        Intent intent = new Intent("MyTotalAmount");
//                        intent.putExtra("cartTotalAmount",totalAmount);
//                        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                    }
                });
            }

        });

//        Item Save For Later
        holder.ItemSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DocumentReference RemoveItem = fStore.collection("Cart")
                        .document(MyCartDataList.get(position).getCartId());
                RemoveItem.update("save","yes").addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        MyCartDataList.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position,getItemCount());
//                        CartAdapter.notifyItemRemoved(MyCartDataList.get(position));
                        Toast.makeText(view.getContext(), "successfully Moved", Toast.LENGTH_SHORT).show();
                        items = getItemCount();
                        Intent totalItems = new Intent("MyTotalItems");
                        totalItems.putExtra("cartTotalItems",items);
                        LocalBroadcastManager.getInstance(context).sendBroadcast(totalItems);
//                        totalAmount = totalAmount - Integer.parseInt(MyCartDataList.get(position).getPrice());
//                        Intent intent = new Intent("MyTotalAmount");
//                        intent.putExtra("cartTotalAmount",totalAmount);
//                        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                    }
                });
            }

        });



//        Minus Qty
        holder.QtyMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyCart_Item_Model qty1;
                int qty = Integer.parseInt(MyCartDataList.get(position).getQty());

                if(qty>1) {
                    qty = qty - 1;
                    Log.e("qtym", String.valueOf(qty));
//                    holder.MyCartQty.setText(String.valueOf(qty));
                    DocumentReference qtyMinus = fStore.collection("Cart")
                            .document(MyCartDataList.get(position).getCartId());
                    qtyMinus.update("qty",String.valueOf(qty));
                    notifyItemChanged(position);

                }
            }
        });

//        plush Qry
        holder.QtyPlush.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int qty = Integer.parseInt(MyCartDataList.get(position).getQty());

                if(qty<5) {
                    qty = qty + 1;
                    Log.e("qtyp", String.valueOf(qty));
                    DocumentReference qtyPlush = fStore.collection("Cart")
                            .document(MyCartDataList.get(position).getCartId());
                    qtyPlush.update("qty",String.valueOf(qty));
//                    holder.MyCartQty.setText(String.valueOf(qty));
                    notifyItemChanged(position);
                }
            }
        });

//        Count Items
        items  = getItemCount();
        Intent totalItems = new Intent("MyTotalItems");
        totalItems.putExtra("cartTotalItems",items);
        totalItems.putExtra("sellerId",MyCartDataList.get(position).getSellerId());
        totalItems.putExtra( "prodId",MyCartDataList.get(position).getProdId());
        totalItems.putExtra( "title",MyCartDataList.get(position).getTitle());
        totalItems.putExtra( "price",MyCartDataList.get(position).getPrice());
        totalItems.putExtra( "qty",MyCartDataList.get(position).getQty());
        totalItems.putExtra( "url",MyCartDataList.get(position).getUrl());
        LocalBroadcastManager.getInstance(context).sendBroadcast(totalItems);
        Log.e("CartItems", String.valueOf(items));

//        Count Total OF Amount
        totalAmount = totalAmount + Integer.parseInt(MyCartDataList.get(position).getPrice());
        Intent intent = new Intent("MyTotalAmount");
        intent.putExtra("cartTotalAmount",totalAmount);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
        Log.e("CartTotal", String.valueOf(totalAmount));


    }


    @Override
    public int getItemCount() {
        return MyCartDataList.size();
    }

    static class MyCartViewHolder extends RecyclerView.ViewHolder
    {
        TextView MyCartTitle,MyCartPrice,MyCartQty,ItemRemove,ItemSave;
        ImageView MyCartProdImg,QtyPlush,QtyMinus;

        public MyCartViewHolder(@NonNull View itemView) {
            super(itemView);

            MyCartProdImg =itemView.findViewById(R.id.MyCart_Prod_Img);
            MyCartTitle =itemView.findViewById(R.id.MyCart_Prod_Title);
            MyCartPrice =itemView.findViewById(R.id.MyCart_Prod_Price);
            MyCartQty =itemView.findViewById(R.id.MyCart_Qty);
            ItemRemove = itemView.findViewById(R.id.MyCart_Remove_Btn);
            QtyMinus = itemView.findViewById(R.id.MyCart_minusQty);
            QtyPlush = itemView.findViewById(R.id.MyCart_plusQty);
            ItemSave = itemView.findViewById(R.id.MyCart_save);
        }
    }

}
