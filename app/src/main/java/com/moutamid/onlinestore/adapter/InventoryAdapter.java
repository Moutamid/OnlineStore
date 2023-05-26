package com.moutamid.onlinestore.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.fxn.stash.Stash;
import com.moutamid.onlinestore.R;
import com.moutamid.onlinestore.activities.buyer_side.ProductDetailActivity;
import com.moutamid.onlinestore.activities.seller_side.ProductDetailSellerActivity;
import com.moutamid.onlinestore.constants.Constants;
import com.moutamid.onlinestore.models.ProductModel;

import java.util.ArrayList;

public class InventoryAdapter extends RecyclerView.Adapter<InventoryAdapter.InventoryVH> {

    Context context;
    ArrayList<ProductModel> list;

    public InventoryAdapter(Context context, ArrayList<ProductModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public InventoryVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new InventoryVH(LayoutInflater.from(context).inflate(R.layout.inventory_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull InventoryVH holder, int position) {
        ProductModel model = list.get(holder.getAbsoluteAdapterPosition());
        Glide.with(context).load(model.getThumbnail()).into(holder.image);

        holder.name.setText(model.getName());
        holder.price.setText("$"+model.getPrice());
        holder.stock.setText(""+model.getStock());
        holder.catg.setText(model.getCategory());
        holder.date.setText(Constants.getFormatedDate(model.getTimeStamp()));

        holder.itemView.setOnClickListener(v -> {
            Stash.put(Constants.MODEL, model);
            context.startActivity(new Intent(context, ProductDetailSellerActivity.class));
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class InventoryVH extends RecyclerView.ViewHolder{
        ImageView image;
        TextView name, price, stock, date, catg;
        public InventoryVH(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            name = itemView.findViewById(R.id.name);
            price = itemView.findViewById(R.id.price);
            date = itemView.findViewById(R.id.date);
            stock = itemView.findViewById(R.id.stock);
            catg = itemView.findViewById(R.id.category);
        }
    }

}
