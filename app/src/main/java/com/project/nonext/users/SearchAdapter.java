package com.project.nonext.users;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.project.nonext.R;
import com.squareup.picasso.Picasso;

public class SearchAdapter extends FirestoreRecyclerAdapter<SearchModel,SearchAdapter.SearchViewHolder> {


    public interface OnItemClickListener {
        void onItemClick(int position);
    }

//    private final SimpleDateFormat format = new SimpleDateFormat("MM-dd-yy", Locale.US);
    private final OnItemClickListener listener;

    SearchAdapter(FirestoreRecyclerOptions<SearchModel> options, OnItemClickListener listener) {
        super(options);
        this.listener = listener;
    }

    SearchAdapter(FirestoreRecyclerOptions<SearchModel> options) {
        super(options);
        this.listener = null;
    }

    class SearchViewHolder extends RecyclerView.ViewHolder {
        final CardView view;
        final TextView productName;
        ImageView img;

        SearchViewHolder(CardView v) {
            super(v);
            view = v;
            productName = v.findViewById(R.id.search_product);
            img = v.findViewById(R.id.searchProductImageView);
        }
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final SearchViewHolder holder, @NonNull int position, @NonNull final SearchModel search) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.productName.setText(search.getTitle());
        Picasso.get().load(search.getUrl())
                .error(R.drawable.logo).into(holder.img);
//        if (listener != null) {
        holder.view.setClickable(true);
            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    listener.onItemClick(holder.getAbsoluteAdapterPosition());
                    Intent intent = new Intent(holder.productName.getContext(), ProductDetailActivity.class);
                    intent.putExtra("ProductId",search.getProdId());
                    intent.putExtra("SellerId",search.getSellerId());
//                intent.putExtra("utitle",productsArrayList.get(position).getTitle());
//                intent.putExtra("uprice",productsArrayList.get(position).getPrices());

                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    holder.productName.getContext().startActivities(new Intent[]{intent});
                }
            });
//        }
    }

    // Create new views (invoked by the layout manager)
    @Override
    public SearchViewHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        // create a new view
        CardView v = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_rv_item, parent, false);

        return new SearchViewHolder(v);
    }
}

