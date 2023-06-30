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

public class AdminAdapter extends RecyclerView.Adapter<AdminAdapter.AdminViewHolder> {

    Context context;
    ArrayList<userModel> list;

    public AdminAdapter( ArrayList<userModel> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public AdminViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_rv_user,parent,false);
        return new AdminViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminAdapter.AdminViewHolder holder, int position) {
        holder.firstName.setText(list.get(position).userfirstname);
        holder.lastName.setText(list.get(position).userlastname);
        holder.email.setText(list.get(position).useremail);
        holder.mobile.setText(list.get(position).usermobile);
        holder.password.setText(list.get(position).userpass);
//        userModel userModel = list.get(position);
//        holder.firstName.setText(userModel.userfirstname);
//        holder.lastName.setText(userModel.userlastname);
//        holder.email.setText(userModel.useremail);
//        holder.mobile.setText(userModel.usermobile);
//        holder.password.setText(userModel.userpass);
//        holder.usertype.setText(userModel.usertype);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class AdminViewHolder extends RecyclerView.ViewHolder{

        TextView firstName,lastName,email,mobile,password,usertype;

        public AdminViewHolder(@NonNull View itemView) {
            super(itemView);

            firstName = itemView.findViewById(R.id.fbFirstName);
            lastName = itemView.findViewById(R.id.fbLastName);
            email = itemView.findViewById(R.id.fbEmail);
            mobile = itemView.findViewById(R.id.fbMobile);
            password = itemView.findViewById(R.id.fbPassword);
//            usertype = itemView.findViewById(R.id.fbUserType);
        }
    }
}
