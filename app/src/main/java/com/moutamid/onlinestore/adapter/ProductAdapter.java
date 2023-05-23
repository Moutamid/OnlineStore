package com.moutamid.onlinestore.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.fxn.stash.Stash;
import com.moutamid.onlinestore.R;
import com.moutamid.onlinestore.constants.Constants;
import com.moutamid.onlinestore.models.CategoryModel;
import com.moutamid.onlinestore.models.ProductModel;

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.CategoryVH> {

    Context context;
    ArrayList<ProductModel> list;

    public ProductAdapter(Context context, ArrayList<ProductModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public CategoryVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CategoryVH(LayoutInflater.from(context).inflate(R.layout.product_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryVH holder, int position) {
        ProductModel model = list.get(holder.getAbsoluteAdapterPosition());
        holder.name.setText(model.getName());
        Glide.with(context).load(model.getThumbnail()).into(holder.imageView);
        holder.cat.setText(model.getCategory());
        holder.qt.setText("Qt# " + model.getStock());
        holder.price.setText("$" + model.getPrice());

        ArrayList<ProductModel> bookmarkModels = Stash.getArrayList(Constants.favrt, ProductModel.class);

        for (ProductModel fvrtModel : bookmarkModels){
            if (fvrtModel.getID().equals(model.getID())){
                holder.fav.setImageResource(R.drawable.favorite);
                holder.isFavrt = true;
            }
        }

        holder.fav.setOnClickListener(v -> {
            ArrayList<ProductModel> favrtList = Stash.getArrayList(Constants.favrt, ProductModel.class);
            if (holder.isFavrt){
                for (int i = 0; i < favrtList.size(); i++) {
                    if (favrtList.get(i).getID().equals(model.getID())) {
                        Toast.makeText(context, "removed", Toast.LENGTH_SHORT).show();
                        favrtList.remove(i);
                    }
                }
                holder.fav.setImageResource(R.drawable.round_favorite_border_24);
                holder.isFavrt = false;
                Stash.put(Constants.favrt, favrtList);
            } else {
                Toast.makeText(context, "added", Toast.LENGTH_SHORT).show();
                favrtList.add(model);
                Stash.put(Constants.favrt, favrtList);
                holder.fav.setImageResource(R.drawable.favorite);
                holder.isFavrt = true;
            }
        });

        holder.itemView.setOnClickListener(v -> {});

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class CategoryVH extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView name, price, qt, cat;
        Button add;
        ImageButton fav, view;
        boolean isFavrt;
        public CategoryVH(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            imageView = itemView.findViewById(R.id.image);
            price = itemView.findViewById(R.id.price);
            cat = itemView.findViewById(R.id.cat);
            qt = itemView.findViewById(R.id.qt);
            fav = itemView.findViewById(R.id.fav);
            add = itemView.findViewById(R.id.add);
            view = itemView.findViewById(R.id.view);

            isFavrt = false;

        }
    }

}
