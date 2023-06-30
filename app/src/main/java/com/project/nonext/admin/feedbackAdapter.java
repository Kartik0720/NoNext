package com.project.nonext.admin;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.nonext.R;

import java.util.ArrayList;

import io.grpc.Context;

public class feedbackAdapter extends RecyclerView.Adapter<feedbackAdapter.AdminViewHolder> {

    Context context;
    ArrayList<feedbackModel> feedbackArrayList;

    public feedbackAdapter(ArrayList<feedbackModel> feedbackArrayList) {
        this.context = context;
        this.feedbackArrayList = feedbackArrayList;
    }

    @NonNull
    @Override
    public AdminViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_feedback_rv_item,parent,false);
        return new AdminViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.username.setText(feedbackArrayList.get(position).username);
        holder.feedback.setText(feedbackArrayList.get(position).feedback);
        holder.email.setText(feedbackArrayList.get(position).email);
        holder.mobile.setText(feedbackArrayList.get(position).mobile);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.username.getContext(),feedbackDetail.class);
                intent.putExtra("username",feedbackArrayList.get(position).getUsername());
                intent.putExtra("feedback",feedbackArrayList.get(position).getFeedback());
                intent.putExtra("email",feedbackArrayList.get(position).getEmail());
                intent.putExtra("Mobile",feedbackArrayList.get(position).getMobile());

                /*intent.putExtra("ProductId",productsArrayList.get(position).getProdId());*/
                //          intent.putExtra("utitle",productsArrayList.get(position).getTitle());
//                intent.putExtra("uprice",productsArrayList.get(position).getPrices());

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                holder.username.getContext().startActivities(new Intent[]{intent});
            }
        });
    }

    @Override
    public int getItemCount() {
        return feedbackArrayList.size();
    }
    public static class AdminViewHolder extends RecyclerView.ViewHolder{
            TextView username,mobile,email,feedback;
        public AdminViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.fbAdminUserName);
            feedback = itemView.findViewById(R.id.fbAdminFb);
            email = itemView.findViewById(R.id.fbAdminEmail);
            mobile = itemView.findViewById(R.id.fbadminMobile);
        }
    }
}
