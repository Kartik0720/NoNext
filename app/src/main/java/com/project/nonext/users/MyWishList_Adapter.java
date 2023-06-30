package com.project.nonext.users;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.nonext.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MyWishList_Adapter extends RecyclerView.Adapter<MyWishList_Adapter.MyWishListViewHolder>{

    ArrayList<MyCart_Item_Model> MyWishListDataList;
    String WishListProdId;
    FirebaseFirestore fStore = FirebaseFirestore.getInstance();

    public MyWishList_Adapter(ArrayList<MyCart_Item_Model> myWishListDataList) {
        MyWishListDataList = myWishListDataList;
    }

    @NonNull
    @Override
    public MyWishListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_users_wishlist_item_layout,parent,false);
        return new MyWishListViewHolder(v);    }

    @Override
    public void onBindViewHolder(@NonNull MyWishListViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.WishListTitle.setText(MyWishListDataList.get(position).getTitle());
        holder.WishListPrice.setText(MyWishListDataList.get(position).getPrice()+"/-");
        WishListProdId = MyWishListDataList.get(position).getWishlistId();
        Picasso.get().load(MyWishListDataList.get(position).getUrl())
                .error(R.drawable.logo).into(holder.WishListProdImg);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(holder.WishListTitle.getContext(), ProductDetailActivity.class);
                intent.putExtra("ProductId",MyWishListDataList.get(position).getProdId());
                intent.putExtra("SellerId", MyWishListDataList.get(position).getSellerId());

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                holder.WishListTitle.getContext().startActivities(new Intent[]{intent});
            }
        });
        holder.WishListItemRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DocumentReference RemoveItem = fStore.collection("Wishlist")
                        .document(MyWishListDataList.get(position).getWishlistId());
                RemoveItem.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        MyWishListDataList.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position,getItemCount());
//                        CartAdapter.notifyItemRemoved(MyCartDataList.get(position));
                        Toast.makeText(view.getContext(), "successfully Removed", Toast.LENGTH_SHORT).show();
//                        items = 0;
                    }
                });
            }
        });

    }

    @Override
    public int getItemCount() {
        return MyWishListDataList.size();
    }

    class MyWishListViewHolder extends RecyclerView.ViewHolder{

        TextView WishListTitle,WishListPrice,WishListItemRemove;
        ImageView WishListProdImg;

        public MyWishListViewHolder(@NonNull View itemView) {
            super(itemView);
            WishListTitle = itemView.findViewById(R.id.MyWishList_Prod_Title);
            WishListPrice = itemView.findViewById(R.id.MyWishList_Prod_Price);
            WishListProdImg = itemView.findViewById(R.id.MyWishList_Prod_Img);
            WishListItemRemove = itemView.findViewById(R.id.MyWishList_Remove_Btn);
        }
    }
}
