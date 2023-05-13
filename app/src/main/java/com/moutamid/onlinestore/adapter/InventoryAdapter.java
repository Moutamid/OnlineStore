package com.moutamid.onlinestore.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.moutamid.onlinestore.R;
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
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class InventoryVH extends RecyclerView.ViewHolder{
        ImageView image;
        public InventoryVH(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
        }
    }

}
