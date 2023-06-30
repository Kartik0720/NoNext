package com.project.nonext.users;

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

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.OrderViewHolder> {
    ArrayList<OrdersModel> userOrdersList;

    public OrdersAdapter(ArrayList<OrdersModel> userOrdersList) {
        this.userOrdersList = userOrdersList;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_rv_item,parent,false);
        return new OrderViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.Status.setText(userOrdersList.get(position).orderstatus);
        holder.Title.setText(userOrdersList.get(position).title);
        Picasso.get().load(userOrdersList.get(position).getUrl())
                .error(R.drawable.logo).into(holder.productImg);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(holder.Title.getContext(),OrderDetailsActivity.class);
                intent.putExtra("orderId",userOrdersList.get(position).getOrderId());
                holder.Title.getContext().startActivities(new Intent[]{intent});
            }
        });
    }

    @Override
    public int getItemCount() {
        {
            return userOrdersList.size();
        }
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView Title,date,Status;
        ImageView productImg;
        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            Title=itemView.findViewById(R.id.OrdersProductTitle);
            Status = itemView.findViewById(R.id.OrdersProductStatus);
            productImg=itemView.findViewById(R.id.OrdersProductImageView);
        }
    }
}
