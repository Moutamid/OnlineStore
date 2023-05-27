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

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class InventoryVH extends RecyclerView.ViewHolder{
        ImageView image, star1, star2, star3, star4, star5;
        TextView name, price, stock, date, catg, ratingCount;
        public InventoryVH(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            name = itemView.findViewById(R.id.name);
            price = itemView.findViewById(R.id.price);
            date = itemView.findViewById(R.id.date);
            stock = itemView.findViewById(R.id.stock);
            catg = itemView.findViewById(R.id.category);
            ratingCount = itemView.findViewById(R.id.ratingCount);
            star1 = itemView.findViewById(R.id.star1);
            star2 = itemView.findViewById(R.id.star2);
            star3 = itemView.findViewById(R.id.star3);
            star4 = itemView.findViewById(R.id.star4);
            star5 = itemView.findViewById(R.id.star5);
        }
    }

}
