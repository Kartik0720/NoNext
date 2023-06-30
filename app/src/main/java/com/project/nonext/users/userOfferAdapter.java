package com.project.nonext.users;

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

public class userOfferAdapter extends RecyclerView.Adapter<userOfferAdapter.myViewholder> {
    ArrayList<userOffersModel> offerlist;

    public userOfferAdapter(ArrayList<userOffersModel> offerlist) {
        this.offerlist = offerlist;
    }

    @NonNull
    @Override
    public myViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.offers_rv,parent,false);
        return new myViewholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewholder holder, int position) {
        holder.offerCode.setText(offerlist.get(position).offerCode);
        holder.date.setText(offerlist.get(position).date);
        Picasso.get().load(offerlist.get(position).getUrl())
                .error(R.drawable.logo).into(holder.offerImg);

    }

    @Override
    public int getItemCount() {
        return offerlist.size();
    }

   static class myViewholder extends RecyclerView.ViewHolder
    {
        TextView offerCode,date;
        ImageView offerImg;

        public myViewholder(@NonNull View itemView) {
            super(itemView);
            offerCode=itemView.findViewById(R.id.offerTextViewOne);
            date=itemView.findViewById(R.id.useroffersdate);
            offerImg=itemView.findViewById(R.id.offerImageView);
        }
    }
}
