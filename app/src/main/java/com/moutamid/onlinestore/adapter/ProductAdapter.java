package com.moutamid.onlinestore.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
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
import com.moutamid.onlinestore.activities.buyer_side.ProductDetailActivity;
import com.moutamid.onlinestore.constants.Constants;
import com.moutamid.onlinestore.models.CartModel;
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
        ArrayList<CartModel> carttt = Stash.getArrayList(Constants.CART, CartModel.class);

        holder.ratingCount.setText("(" + model.getRatingCount() + ")");
        double average = 0;
        try{
            average = ((5 * model.getStar5()) + (4 * model.getStar4()) + (3 * model.getStar3()) + (2 * model.getStar2()) + model.getStar1())
                    / (model.getStar1() + model.getStar2() + model.getStar3() + model.getStar4() + model.getStar5());
        } catch (Exception e) {}


        if (average >= 0.0) {
            holder.star1.setImageDrawable(context.getResources().getDrawable(R.drawable.star_rate_grey));
            holder.star2.setImageDrawable(context.getResources().getDrawable(R.drawable.star_rate_grey));
            holder.star3.setImageDrawable(context.getResources().getDrawable(R.drawable.star_rate_grey));
            holder.star4.setImageDrawable(context.getResources().getDrawable(R.drawable.star_rate_grey));
            holder.star5.setImageDrawable(context.getResources().getDrawable(R.drawable.star_rate_grey));
        } else if (average >= 0.5 && average <= 1.5) {
            holder.star1.setImageDrawable(context.getResources().getDrawable(R.drawable.star_rate_yellow));
            holder.star2.setImageDrawable(context.getResources().getDrawable(R.drawable.star_rate_grey));
            holder.star3.setImageDrawable(context.getResources().getDrawable(R.drawable.star_rate_grey));
            holder.star4.setImageDrawable(context.getResources().getDrawable(R.drawable.star_rate_grey));
            holder.star5.setImageDrawable(context.getResources().getDrawable(R.drawable.star_rate_grey));
        } else if (average >= 1.5 && average <= 2.5) {
            holder.star1.setImageDrawable(context.getResources().getDrawable(R.drawable.star_rate_yellow));
            holder.star2.setImageDrawable(context.getResources().getDrawable(R.drawable.star_rate_yellow));
            holder.star3.setImageDrawable(context.getResources().getDrawable(R.drawable.star_rate_grey));
            holder.star4.setImageDrawable(context.getResources().getDrawable(R.drawable.star_rate_grey));
            holder.star5.setImageDrawable(context.getResources().getDrawable(R.drawable.star_rate_grey));
        } else if (average >= 2.5 && average <= 3.5) {
            holder.star1.setImageDrawable(context.getResources().getDrawable(R.drawable.star_rate_yellow));
            holder.star2.setImageDrawable(context.getResources().getDrawable(R.drawable.star_rate_yellow));
            holder.star3.setImageDrawable(context.getResources().getDrawable(R.drawable.star_rate_yellow));
            holder.star4.setImageDrawable(context.getResources().getDrawable(R.drawable.star_rate_grey));
            holder.star5.setImageDrawable(context.getResources().getDrawable(R.drawable.star_rate_grey));
        } else if (average >= 3.5 && average <= 4) {
            holder.star1.setImageDrawable(context.getResources().getDrawable(R.drawable.star_rate_yellow));
            holder.star2.setImageDrawable(context.getResources().getDrawable(R.drawable.star_rate_yellow));
            holder.star3.setImageDrawable(context.getResources().getDrawable(R.drawable.star_rate_yellow));
            holder.star4.setImageDrawable(context.getResources().getDrawable(R.drawable.star_rate_yellow));
            holder.star5.setImageDrawable(context.getResources().getDrawable(R.drawable.star_rate_grey));
        } else if (average >= 4) {
            holder.star1.setImageDrawable(context.getResources().getDrawable(R.drawable.star_rate_yellow));
            holder.star2.setImageDrawable(context.getResources().getDrawable(R.drawable.star_rate_yellow));
            holder.star3.setImageDrawable(context.getResources().getDrawable(R.drawable.star_rate_yellow));
            holder.star4.setImageDrawable(context.getResources().getDrawable(R.drawable.star_rate_yellow));
            holder.star5.setImageDrawable(context.getResources().getDrawable(R.drawable.star_rate_yellow));
        }


        for (ProductModel fvrtModel : bookmarkModels){
            if (fvrtModel.getID().equals(model.getID())){
                holder.fav.setImageResource(R.drawable.favorite);
                holder.isFavrt = true;
            }
        }

        for (CartModel cartModel : carttt){
            if (cartModel.getID().equals(model.getID())){
                holder.isAdded = true;
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

        holder.itemView.setOnClickListener(v -> {
            Stash.put(Constants.MODEL, model);
            context.startActivity(new Intent(context, ProductDetailActivity.class));
        });

        holder.view.setOnClickListener(v -> {
            Stash.put(Constants.MODEL, model);
            context.startActivity(new Intent(context, ProductDetailActivity.class));
        });

        holder.add.setOnClickListener(v -> {
            Constants.initDialog(context);
            Constants.showDialog();
            new Handler().postDelayed(() -> {
                ArrayList<CartModel> cart = Stash.getArrayList(Constants.CART, CartModel.class);
                CartModel cartModel = new CartModel(model.getID(), model, 1);
                if (!holder.isAdded){
                    cart.add(cartModel);
                    Stash.put(Constants.CART, cart);
                    holder.isAdded = true;
                    Toast.makeText(context, "Product added into cart", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Already Added into your cart", Toast.LENGTH_SHORT).show();
                }
                Constants.dismissDialog();
            }, 1500);
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class CategoryVH extends RecyclerView.ViewHolder{
        ImageView imageView, star1, star2, star3, star4, star5;
        TextView name, price, qt, cat, ratingCount;
        Button add;
        ImageButton fav, view;
        boolean isFavrt, isAdded;
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
            ratingCount = itemView.findViewById(R.id.ratingCount);
            star1 = itemView.findViewById(R.id.star1);
            star2 = itemView.findViewById(R.id.star2);
            star3 = itemView.findViewById(R.id.star3);
            star4 = itemView.findViewById(R.id.star4);
            star5 = itemView.findViewById(R.id.star5);

            isFavrt = false;
            isAdded = false;

        }
    }

}
