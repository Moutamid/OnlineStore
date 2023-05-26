package com.moutamid.onlinestore.activities.buyer_side;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.fxn.stash.Stash;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.moutamid.onlinestore.R;
import com.moutamid.onlinestore.constants.Constants;
import com.moutamid.onlinestore.databinding.ActivityProductDetailBinding;
import com.moutamid.onlinestore.models.CartModel;
import com.moutamid.onlinestore.models.ProductModel;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.ArrayList;

public class ProductDetailActivity extends AppCompatActivity {
    ActivityProductDetailBinding binding;
    ArrayList<String> images;
    double price = 0;
    int count = 1;
    boolean added = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        images = new ArrayList<>();

        binding.count.setText("" + count);

        Constants.initDialog(this);
        Constants.showDialog();
        ProductModel model = (ProductModel) Stash.getObject(Constants.MODEL, ProductModel.class);
        showDate(model);

        binding.back.setOnClickListener(v -> {
            startActivity(new Intent(this, BuyerMainActivity.class));
            finish();
        });

        binding.add.setOnClickListener(v -> {
            count++;
            binding.count.setText("" + count);
        });

        binding.remove.setOnClickListener(v -> {
            if (count > 1) {
                count--;
                binding.count.setText("" + count);
            }
        });

        binding.checkout.setOnClickListener(v -> {
            CartModel model1 = new CartModel(model.getID(), model, count);
            ArrayList<CartModel> cart = Stash.getArrayList(Constants.CART, CartModel.class);
            Constants.showDialog();
            new Handler().postDelayed(() -> {
                for (int i = 0; i < cart.size(); i++) {
                    if (cart.get(i).getID().equals(model1.getID())) {
                        int jj = cart.get(i).getCount();
                        cart.get(i).setCount(jj + count);
                        Stash.put(Constants.CART, cart);
                        Snackbar.make(ProductDetailActivity.this, binding.root, "Product added into cart", Snackbar.LENGTH_SHORT).show();
                        added = true;
                        break;
                    }
                }
                if (!added){
                    cart.add(model1);
                    Stash.put(Constants.CART, cart);
                    added = true;
                    Snackbar.make(ProductDetailActivity.this, binding.root, "Product added into cart", Snackbar.LENGTH_SHORT).show();
                }

                Constants.dismissDialog();
            }, 1500);
        });

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, BuyerMainActivity.class));
        finish();
    }

    private void showDate(ProductModel model) {
        Constants.databaseReference().child(Constants.ProductImages).child(model.getUserID())
                .child(model.getID()).get().addOnSuccessListener(dataSnapshot -> {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            images.add(snapshot.child("image").getValue(String.class));
                        }
                        SliderAdapter adapter = new SliderAdapter(ProductDetailActivity.this, images);
                        binding.imageSlider.setSliderAdapter(adapter);
                        price = model.getPrice();
                        binding.price.setText("$" + model.getPrice());
                        binding.name.setText(model.getName());
                        binding.cat.setText(model.getCategory());
                        binding.stock.setText("" + model.getStock());
                        binding.desc.setText(model.getDescription());

                        ArrayList<ProductModel> bookmarkModels = Stash.getArrayList(Constants.favrt, ProductModel.class);

                        for (ProductModel fvrtModel : bookmarkModels) {
                            if (fvrtModel.getID().equals(model.getID())) {
                                binding.fav.setImageResource(R.drawable.favorite);
                            }
                        }

                    }
                    Constants.dismissDialog();
                }).addOnFailureListener(e -> {
                    Constants.dismissDialog();
                    Snackbar.make(ProductDetailActivity.this, binding.root, e.getMessage(), Snackbar.LENGTH_SHORT).show();
                });
    }

    public class SliderAdapter extends SliderViewAdapter<SliderAdapter.SliderAdapterVH> {
        private Context context;
        private ArrayList<String> mSliderItems;

        public SliderAdapter(Context context, ArrayList<String> mSliderItems) {
            this.context = context;
            this.mSliderItems = mSliderItems;
        }

        @Override
        public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
            View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.slider_layout, parent, false);
            return new SliderAdapterVH(inflate);
        }

        @Override
        public void onBindViewHolder(SliderAdapterVH viewHolder, final int position) {

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