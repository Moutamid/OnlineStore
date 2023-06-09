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

        double average = 0;
        try{
            average = ((5 * model.getProductModel().getStar5()) + (4 * model.getProductModel().getStar4()) + (3 * model.getProductModel().getStar3()) + (2 * model.getProductModel().getStar2()) + model.getProductModel().getStar1())
                    / (model.getProductModel().getStar1() + model.getProductModel().getStar2() + model.getProductModel().getStar3() + model.getProductModel().getStar4() + model.getProductModel().getStar5());
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

        holder.delete.setOnClickListener(v -> {
            listner.onDeleteClick(list.get(holder.getAbsoluteAdapterPosition()), holder.getAbsoluteAdapterPosition());
        });

        holder.itemView.setOnClickListener(v -> {
            Stash.put(Constants.MODEL, model.getProductModel());
            context.startActivity(new Intent(context, ProductDetailActivity.class));
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class CartVH extends RecyclerView.ViewHolder {

        ImageView image, star1, star2, star3, star4, star5;
        TextView name, cat, price, ratingCount;
        ImageButton delete;

        public CartVH(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.image);
            name = itemView.findViewById(R.id.name);
            cat = itemView.findViewById(R.id.category);
            price = itemView.findViewById(R.id.price);
            delete = itemView.findViewById(R.id.delete);
            ratingCount = itemView.findViewById(R.id.ratingCount);
            star1 = itemView.findViewById(R.id.star1);
            star2 = itemView.findViewById(R.id.star2);
            star3 = itemView.findViewById(R.id.star3);
            star4 = itemView.findViewById(R.id.star4);
            star5 = itemView.findViewById(R.id.star5);

        }
    }

}
