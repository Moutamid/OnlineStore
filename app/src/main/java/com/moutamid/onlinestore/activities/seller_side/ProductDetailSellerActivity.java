package com.moutamid.onlinestore.activities.seller_side;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.fxn.stash.Stash;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.moutamid.onlinestore.R;
import com.moutamid.onlinestore.activities.buyer_side.BuyerMainActivity;
import com.moutamid.onlinestore.activities.buyer_side.ProductDetailActivity;
import com.moutamid.onlinestore.constants.Constants;
import com.moutamid.onlinestore.databinding.ActivityProductDetailSellerBinding;
import com.moutamid.onlinestore.models.ProductModel;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.ArrayList;

public class ProductDetailSellerActivity extends AppCompatActivity {
    ActivityProductDetailSellerBinding binding;
    ArrayList<String> images;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductDetailSellerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        images = new ArrayList<>();

        binding.back.setOnClickListener(v -> {
            startActivity(new Intent(this, SellerDashboardActivity.class));
            finish();
        });

        Constants.initDialog(this);
        Constants.showDialog();
        ProductModel model = (ProductModel) Stash.getObject(Constants.MODEL, ProductModel.class);
        showDate(model);

        binding.update.setOnClickListener(v -> {
            startActivity(new Intent(this, ProductUpdateActivity.class));
            finish();
        });

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, SellerDashboardActivity.class));
        finish();
    }

    private void showDate(ProductModel model) {
        Constants.databaseReference().child(Constants.ProductImages).child(model.getUserID())
                .child(model.getID()).get().addOnSuccessListener(dataSnapshot -> {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            images.add(snapshot.child("image").getValue(String.class));
                        }
                        SliderAdapter adapter = new SliderAdapter(images);
                        binding.imageSlider.setSliderAdapter(adapter);
                        binding.name.setText(model.getName());
                        binding.price.setText("$"+model.getPrice());
                        binding.cat.setText(model.getCategory());
                        binding.stock.setText("" + model.getStock());
                        binding.desc.setText(model.getDescription());

                    }
                    Constants.dismissDialog();
                }).addOnFailureListener(e -> {
                    Constants.dismissDialog();
                    Snackbar.make(ProductDetailSellerActivity.this, binding.root, e.getMessage(), Snackbar.LENGTH_SHORT).show();
                });
    }

    public class SliderAdapter extends SliderViewAdapter<SliderAdapter.SliderAdapterVH> {
        private ArrayList<String> mSliderItems;

        public SliderAdapter(ArrayList<String> mSliderItems) {
            this.mSliderItems = mSliderItems;
        }

        @Override
        public SliderAdapter.SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
            View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.slider_layout, parent, false);
            return new SliderAdapter.SliderAdapterVH(inflate);
        }

        @Override
        public void onBindViewHolder(SliderAdapter.SliderAdapterVH viewHolder, final int position) {

            String sliderItem = mSliderItems.get(position);

            Glide.with(viewHolder.itemView)
                    .load(sliderItem)
                    .fitCenter()
                    .into(viewHolder.imageViewBackground);

        }

        @Override
        public int getCount() {
            return mSliderItems.size();
        }

        class SliderAdapterVH extends SliderViewAdapter.ViewHolder {
            ImageView imageViewBackground;

            public SliderAdapterVH(View itemView) {
                super(itemView);
                imageViewBackground = itemView.findViewById(R.id.iv_auto_image_slider);
            }
        }

    }

}