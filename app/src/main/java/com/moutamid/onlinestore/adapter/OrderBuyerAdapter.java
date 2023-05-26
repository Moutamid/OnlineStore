package com.moutamid.onlinestore.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.fxn.stash.Stash;
import com.moutamid.onlinestore.R;
import com.moutamid.onlinestore.activities.buyer_side.ProductDetailActivity;
import com.moutamid.onlinestore.constants.Constants;
import com.moutamid.onlinestore.lister.CartListner;
import com.moutamid.onlinestore.models.BuyModel;
import com.moutamid.onlinestore.models.CartModel;

import java.util.ArrayList;

public class OrderBuyerAdapter extends RecyclerView.Adapter<OrderBuyerAdapter.CartVH> {

    Context context;
    ArrayList<BuyModel> list;


    public OrderBuyerAdapter(Context context, ArrayList<BuyModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public CartVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CartVH(LayoutInflater.from(context).inflate(R.layout.order_buyer_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CartVH holder, int position) {
        BuyModel model = list.get(holder.getAbsoluteAdapterPosition());

        holder.name.setText(model.getProduct().getName());
        holder.cat.setText(model.getProduct().getCategory());
        holder.date.setText(Constants.getFormatedDate(model.getTimeStamp()));
        holder.price.setText("$" + model.getProduct().getPrice() + " x " + model.getProductCount());
        Glide.with(context).load(model.getProduct().getThumbnail()).into(holder.image);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class CartVH extends RecyclerView.ViewHolder {

        ImageView image;
        TextView name, cat, price, date;

        public CartVH(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.image);
            name = itemView.findViewById(R.id.name);
            cat = itemView.findViewById(R.id.category);
            price = itemView.findViewById(R.id.price);
            date = itemView.findViewById(R.id.date);

        }
    }

}
