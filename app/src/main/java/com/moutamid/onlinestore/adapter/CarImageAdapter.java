package com.moutamid.onlinestore.adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.moutamid.onlinestore.R;

import java.util.ArrayList;

public class CarImageAdapter extends RecyclerView.Adapter<CarImageAdapter.CarImageViewHolder>  {
    Context context;
    ArrayList<Uri> carImagesList;
    LinearLayout addPhotoLayout, addPhotoLayoutRecycler;

    private final int limit = 6;

    public CarImageAdapter(Context context, ArrayList<Uri> carImagesList) {
        this.context = context;
        this.carImagesList = carImagesList;
    }

    public CarImageAdapter(Context context, ArrayList<Uri> carImagesList, LinearLayout addPhotoLayout, LinearLayout addPhotoLayoutRecycler) {
        this.context = context;
        this.carImagesList = carImagesList;
        this.addPhotoLayout = addPhotoLayout;
        this.addPhotoLayoutRecycler = addPhotoLayoutRecycler;
    }

    @NonNull
    @Override
    public CarImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_layout, parent, false);
        return new CarImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CarImageViewHolder holder, int position) {
        holder.carImageView.setImageURI(carImagesList.get(position));
        holder.itemView.setOnClickListener(v -> {

            final Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.bs_image_menu);

            Button removeImg = dialog.findViewById(R.id.btnRemoveImage);
            Button cancel = dialog.findViewById(R.id.btnCancel);

            cancel.setOnClickListener(can -> {
                dialog.cancel();
            });

            removeImg.setOnClickListener(remove -> {
                try{
                    if(carImagesList.size() == 1){
                        carImagesList.remove(position);
                        notifyItemRemoved(position);
                        notifyDataSetChanged();
                        dialog.cancel();
                        if (carImagesList.isEmpty()) {
                            addPhotoLayoutRecycler.setVisibility(View.GONE);
                            addPhotoLayout.setVisibility(View.VISIBLE);
                        }
                    }
                    carImagesList.remove(position);
                    notifyItemRemoved(position);
                    notifyDataSetChanged();
                    dialog.cancel();
                } catch (Exception e){

                }

            });

            dialog.show();
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().getAttributes().windowAnimations = R.style.BottomSheetAnim;
            dialog.getWindow().setGravity(Gravity.BOTTOM);
        });
    }

    @Override
    public int getItemCount() {
        if (carImagesList.size() > limit){
            return limit;
        }
        return carImagesList.size();
    }

    public class CarImageViewHolder extends RecyclerView.ViewHolder{
        ImageView carImageView;
        public CarImageViewHolder(@NonNull View itemView) {
            super(itemView);
            carImageView = itemView.findViewById(R.id.carImageView);
        }

    }
}
