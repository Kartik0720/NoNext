package com.project.nonext.admin;

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

public class AdminProductAdapter extends RecyclerView.Adapter<AdminProductAdapter.AdminViewHolder> {
    Context context;
    ArrayList<adminProductModel> AdminPList;

    public AdminProductAdapter(ArrayList<adminProductModel> adminPList) {
        this.context = context;
        AdminPList = adminPList;
    }


    @NonNull
    @Override
    public AdminViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_rv_item,parent,false);
        return new AdminViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminViewHolder holder, @SuppressLint("RecyclerView") int position) {
        adminProductModel model =AdminPList.get(position);
        holder.title.setText(model.title);
        holder.prices.setText(model.prices);
        holder.desc.setText(model.description);
        Picasso.get().load(AdminPList.get(position).getUrl())
                .error(R.drawable.logo).into(holder.img);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.title.getContext(), AdminProductDetailActivity.class);
                intent.putExtra("uProdId",AdminPList.get(position).getProdId());
//                intent.putExtra("utitle",ProdDataList.get(position).getTitle());
//                intent.putExtra("uprice",ProdDataList.get(position).getPrices());

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                holder.title.getContext().startActivities(new Intent[]{intent});
            }
        });
    }

    @Override
    public int getItemCount() {
        return  AdminPList.size();
    }


    public static class AdminViewHolder extends RecyclerView.ViewHolder {

        TextView title,prices,desc;
        ImageView img;
        public AdminViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.productTitle);
            prices = itemView.findViewById(R.id.productPrice);
            desc = itemView.findViewById(R.id.productDescription);
            img = itemView.findViewById(R.id.productImageView);
        }
    }
}
