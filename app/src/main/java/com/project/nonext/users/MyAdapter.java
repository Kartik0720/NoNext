package com.project.nonext.users;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.nonext.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    Context context;
    ArrayList<ProductsModel> productsArrayList;

    public MyAdapter( ArrayList<ProductsModel> productsArrayList) {
        this.productsArrayList = productsArrayList;
    }



    @NonNull
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_rv_item,parent,false);
        return new MyViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull MyAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        ProductsModel products = productsArrayList.get(position);
        holder.title.setText(products.title);
        holder.prices.setText(products.prices);
        holder.desc.setText(products.description);
        Picasso.get().load(productsArrayList.get(position).getUrl())
                .error(R.drawable.logo).into(holder.img);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.title.getContext(), ProductDetailActivity.class);
                intent.putExtra("ProductId",productsArrayList.get(position).getProdId());
                intent.putExtra("SellerId",productsArrayList.get(position).getSellerId());
//                intent.putExtra("utitle",productsArrayList.get(position).getTitle());
//                intent.putExtra("uprice",productsArrayList.get(position).getPrices());

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                holder.title.getContext().startActivities(new Intent[]{intent});
            }
        });
    }

    @Override
    public int getItemCount() {
        return productsArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView title,prices,desc;
        ImageView img;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.productTitle);
            prices = itemView.findViewById(R.id.productPrice);
            desc = itemView.findViewById(R.id.productDescription);
            img = itemView.findViewById(R.id.productImageView);
        }
    }
}

