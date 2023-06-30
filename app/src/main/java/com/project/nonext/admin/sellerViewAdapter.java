package com.project.nonext.admin;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.nonext.R;

import java.util.ArrayList;

import io.grpc.Context;

public class sellerViewAdapter extends RecyclerView.Adapter<sellerViewAdapter.AdminViewHolder> {

    Context context;
    ArrayList<sellerModel> sellerViewList;

    public sellerViewAdapter(ArrayList<sellerModel> sellerViewList) {
        this.context = context;
        this.sellerViewList = sellerViewList;
    }

   /* public sellerViewAdapter(ArrayList<sellerModel> sellerdatalist) {

    }
*/
    @NonNull
    @Override
    public AdminViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_rv_user,parent,false);
        return new sellerViewAdapter.AdminViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminViewHolder holder, int position) {
        holder.firstName.setText(sellerViewList.get(position).sellerfirstname);
        holder.lastName.setText(sellerViewList.get(position).sellerlastname);
        holder.email.setText(sellerViewList.get(position).selleremail);
        holder.mobile.setText(sellerViewList.get(position).sellermobile);
        holder.password.setText(sellerViewList.get(position).sellerpass);
    }

    @Override
    public int getItemCount() {
        return sellerViewList.size();
    }

    public class AdminViewHolder extends RecyclerView.ViewHolder {
        TextView firstName,lastName,email,mobile,password;

        public AdminViewHolder(@NonNull View itemView) {
            super(itemView);

            firstName = itemView.findViewById(R.id.fbFirstName);
            lastName = itemView.findViewById(R.id.fbLastName);
            email = itemView.findViewById(R.id.fbEmail);
            mobile = itemView.findViewById(R.id.fbMobile);
            password = itemView.findViewById(R.id.fbPassword);
        }
    }
}
