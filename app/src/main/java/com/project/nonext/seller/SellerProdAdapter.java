package com.project.nonext.seller;

import android.annotation.SuppressLint;
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

public class SellerProdAdapter extends RecyclerView.Adapter<SellerProdAdapter.SellerProdViewHolder> {

    ArrayList<sellerprodmodel> ProdDataList;

    public SellerProdAdapter(ArrayList<sellerprodmodel> prodDataList) {
        ProdDataList = prodDataList;
    }

    @NonNull
    @Override
    public SellerProdViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_seller_prod_layot,parent,false);
        return new SellerProdViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SellerProdViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.SellerProdTitle.setText(ProdDataList.get(position).getTitle());
        holder.SellerProdPrice.setText(ProdDataList.get(position).getPrices());
        Picasso.get().load(ProdDataList.get(position).getUrl())
                .error(R.drawable.logo).into(holder.SellerProdImg
        );

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.SellerProdTitle.getContext(),SellerProdDetails.class);
                intent.putExtra("uProdId",ProdDataList.get(position).getProdId());
//                intent.putExtra("utitle",ProdDataList.get(position).getTitle());
//                intent.putExtra("uprice",ProdDataList.get(position).getPrices());

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                holder.SellerProdTitle.getContext().startActivities(new Intent[]{intent});
            }
        });
    }

    @Override
    public int getItemCount() {
        return ProdDataList.size();
    }

    static class SellerProdViewHolder extends RecyclerView.ViewHolder{

        TextView SellerProdTitle,SellerProdPrice;
        ImageView SellerProdImg;

        public SellerProdViewHolder(@NonNull View itemView) {
            super(itemView);

            SellerProdTitle = itemView.findViewById(R.id.SellerProdLayutTitle);
            SellerProdPrice = itemView.findViewById(R.id.SellerProdLayutPrice);
            SellerProdImg = itemView.findViewById(R.id.SellerProdLayutImg);
        }
    }

}
