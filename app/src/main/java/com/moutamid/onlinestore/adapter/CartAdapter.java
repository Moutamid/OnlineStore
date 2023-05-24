package com.moutamid.onlinestore.adapter;

import android.content.Context;
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
import com.moutamid.onlinestore.constants.Constants;
import com.moutamid.onlinestore.lister.CartListner;
import com.moutamid.onlinestore.models.CartModel;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartVH> {

    Context context;
    ArrayList<CartModel> list;
    CartListner listner;

    public CartAdapter(Context context, ArrayList<CartModel> list, CartListner listner) {
        this.context = context;
        this.list = list;
        this.listner = listner;
    }

    @NonNull
    @Override
    public CartVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CartVH(LayoutInflater.from(context).inflate(R.layout.cart_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CartVH holder, int position) {
        CartModel model = list.get(holder.getAbsoluteAdapterPosition());

        holder.name.setText(model.getProductModel().getName());
        holder.cat.setText(model.getProductModel().getCategory());
        holder.price.setText("$" + model.getProductModel().getPrice() + " x " + model.getCount());
        Glide.with(context).load(model.getProductModel().getThumbnail()).into(holder.image);

        holder.delete.setOnClickListener(v -> {
            listner.onDeleteClick(list.get(holder.getAbsoluteAdapterPosition()), holder.getAbsoluteAdapterPosition());
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class CartVH extends RecyclerView.ViewHolder {

        ImageView image;
        TextView name, cat, price;
        ImageButton delete;

        public CartVH(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.image);
            name = itemView.findViewById(R.id.name);
            cat = itemView.findViewById(R.id.category);
            price = itemView.findViewById(R.id.price);
            delete = itemView.findViewById(R.id.delete);

        }
    }

}
