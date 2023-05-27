package com.moutamid.onlinestore.activities.buyer_side;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.fxn.stash.Stash;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.moutamid.onlinestore.R;
import com.moutamid.onlinestore.constants.Constants;
import com.moutamid.onlinestore.databinding.ActivityProductDetailBinding;
import com.moutamid.onlinestore.fragments.buyer_fragments.DescriptionFragment;
import com.moutamid.onlinestore.fragments.buyer_fragments.RatingFragment;
import com.moutamid.onlinestore.models.CartModel;
import com.moutamid.onlinestore.models.ProductModel;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.ArrayList;
import java.util.List;

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
       // Constants.showDialog();
        ProductModel model = (ProductModel) Stash.getObject(Constants.MODEL, ProductModel.class);
        showDate(model);
        starUi(model);
        setupViewPager();

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

        binding.ratingCount.setText("(" + model.getRatingCount() + ")");

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

    private void starUi(ProductModel model) {
        double average = 0;
        try{
            average = ((5 * model.getStar5()) + (4 * model.getStar4()) + (3 * model.getStar3()) + (2 * model.getStar2()) + model.getStar1())
                    / (model.getStar1() + model.getStar2() + model.getStar3() + model.getStar4() + model.getStar5());
        } catch (Exception e) {}


        if (average >= 0.0) {
            binding.star1.setImageDrawable(getResources().getDrawable(R.drawable.star_rate_grey));
            binding.star2.setImageDrawable(getResources().getDrawable(R.drawable.star_rate_grey));
            binding.star3.setImageDrawable(getResources().getDrawable(R.drawable.star_rate_grey));
            binding.star4.setImageDrawable(getResources().getDrawable(R.drawable.star_rate_grey));
            binding.star5.setImageDrawable(getResources().getDrawable(R.drawable.star_rate_grey));
        } else if (average >= 0.5 && average <= 1.5) {
            binding.star1.setImageDrawable(getResources().getDrawable(R.drawable.star_rate_yellow));
            binding.star2.setImageDrawable(getResources().getDrawable(R.drawable.star_rate_grey));
            binding.star3.setImageDrawable(getResources().getDrawable(R.drawable.star_rate_grey));
            binding.star4.setImageDrawable(getResources().getDrawable(R.drawable.star_rate_grey));
            binding.star5.setImageDrawable(getResources().getDrawable(R.drawable.star_rate_grey));
        } else if (average >= 1.5 && average <= 2.5) {
            binding.star1.setImageDrawable(getResources().getDrawable(R.drawable.star_rate_yellow));
            binding.star2.setImageDrawable(getResources().getDrawable(R.drawable.star_rate_yellow));
            binding.star3.setImageDrawable(getResources().getDrawable(R.drawable.star_rate_grey));
            binding.star4.setImageDrawable(getResources().getDrawable(R.drawable.star_rate_grey));
            binding.star5.setImageDrawable(getResources().getDrawable(R.drawable.star_rate_grey));
        } else if (average >= 2.5 && average <= 3.5) {
            binding.star1.setImageDrawable(getResources().getDrawable(R.drawable.star_rate_yellow));
            binding.star2.setImageDrawable(getResources().getDrawable(R.drawable.star_rate_yellow));
            binding.star3.setImageDrawable(getResources().getDrawable(R.drawable.star_rate_yellow));
            binding.star4.setImageDrawable(getResources().getDrawable(R.drawable.star_rate_grey));
            binding.star5.setImageDrawable(getResources().getDrawable(R.drawable.star_rate_grey));
        } else if (average >= 3.5 && average <= 4) {
            binding.star1.setImageDrawable(getResources().getDrawable(R.drawable.star_rate_yellow));
            binding.star2.setImageDrawable(getResources().getDrawable(R.drawable.star_rate_yellow));
            binding.star3.setImageDrawable(getResources().getDrawable(R.drawable.star_rate_yellow));
            binding.star4.setImageDrawable(getResources().getDrawable(R.drawable.star_rate_yellow));
            binding.star5.setImageDrawable(getResources().getDrawable(R.drawable.star_rate_grey));
        } else if (average >= 4) {
            binding.star1.setImageDrawable(getResources().getDrawable(R.drawable.star_rate_yellow));
            binding.star2.setImageDrawable(getResources().getDrawable(R.drawable.star_rate_yellow));
            binding.star3.setImageDrawable(getResources().getDrawable(R.drawable.star_rate_yellow));
            binding.star4.setImageDrawable(getResources().getDrawable(R.drawable.star_rate_yellow));
            binding.star5.setImageDrawable(getResources().getDrawable(R.drawable.star_rate_yellow));
        }
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

    private void setupViewPager() {
        ViewPager viewPager = findViewById(R.id.viewPager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(
                getSupportFragmentManager());

        adapter.addFragment(new DescriptionFragment(), "Description");
        adapter.addFragment(new RatingFragment(), "Rating");

        viewPager.setAdapter(adapter);

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    static class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int arg0) {
            return this.mFragmentList.get(arg0);
        }

        @Override
        public int getCount() {
            return this.mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            this.mFragmentList.add(fragment);
            this.mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return this.mFragmentTitleList.get(position);
        }
    }


}