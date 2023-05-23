package com.moutamid.onlinestore.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.moutamid.onlinestore.R;
import com.moutamid.onlinestore.models.CategoryModel;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryVH> {

    Context context;
    ArrayList<CategoryModel> list;

    public CategoryAdapter(Context context, ArrayList<CategoryModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public CategoryVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CategoryVH(LayoutInflater.from(context).inflate(R.layout.category_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryVH holder, int position) {
        CategoryModel model = list.get(holder.getAbsoluteAdapterPosition());
        holder.name.setText(model.getName());
        Glide.with(context).load(model.getLink()).into(holder.imageView);

        holder.itemView.setOnClickListener(v -> {});

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class CategoryVH extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView name;
        public CategoryVH(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            imageView = itemView.findViewById(R.id.image);
        }
    }

}
