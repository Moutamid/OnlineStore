package com.moutamid.onlinestore.adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.util.Log;
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

import com.bumptech.glide.Glide;
import com.fxn.stash.Stash;
import com.moutamid.onlinestore.R;
import com.moutamid.onlinestore.constants.Constants;

import java.util.ArrayList;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder>  {
    Context context;
    ArrayList<String> ImagesList;
    LinearLayout addPhotoLayout, addPhotoLayoutRecycler;

    private final int limit = 6;

    public ImageAdapter(Context context, ArrayList<String> ImagesList) {
        this.context = context;
        this.ImagesList = ImagesList;
    }

    public ImageAdapter(Context context, ArrayList<String> ImagesList, LinearLayout addPhotoLayout, LinearLayout addPhotoLayoutRecycler) {
        this.context = context;
        this.ImagesList = ImagesList;
        this.addPhotoLayout = addPhotoLayout;
        this.addPhotoLayoutRecycler = addPhotoLayoutRecycler;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_layout, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        Glide.with(context).load(ImagesList.get(holder.getAbsoluteAdapterPosition())).into(holder.imageView);
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
                ArrayList<String> images = Stash.getArrayList(Constants.IMAGE_LIST, String.class);
                try{
                    if(ImagesList.size() == 1){
                        ImagesList.remove(position);
                        notifyItemRemoved(position);
                        notifyDataSetChanged();
                        dialog.cancel();
                        if (ImagesList.isEmpty()) {
                            addPhotoLayoutRecycler.setVisibility(View.GONE);
                            addPhotoLayout.setVisibility(View.VISIBLE);
                        }
                    }
                    if (images.size() > 0){
                        images.remove(position);
                    }
                    Stash.put(Constants.IMAGE_LIST, images);
                    ImagesList.remove(position);
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
        if (ImagesList.size() > limit){
            return limit;
        }
        return ImagesList.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.ImageView);
        }

    }
}
