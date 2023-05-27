package com.moutamid.onlinestore.activities.seller_side;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

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
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.moutamid.onlinestore.R;
import com.moutamid.onlinestore.activities.buyer_side.BuyerMainActivity;
import com.moutamid.onlinestore.activities.buyer_side.ProductDetailActivity;
import com.moutamid.onlinestore.constants.Constants;
import com.moutamid.onlinestore.databinding.ActivityProductDetailSellerBinding;
import com.moutamid.onlinestore.fragments.buyer_fragments.DescriptionFragment;
import com.moutamid.onlinestore.fragments.buyer_fragments.RatingFragment;
import com.moutamid.onlinestore.fragments.seller_fragment.SellerRatingFragment;
import com.moutamid.onlinestore.models.ProductModel;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.ArrayList;
import java.util.List;

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
        starUi(model);
        setupViewPager();

        binding.update.setOnClickListener(v -> {
            startActivity(new Intent(this, ProductUpdateActivity.class));
            finish();
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

    private void setupViewPager() {
        ViewPager viewPager = findViewById(R.id.viewPager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(
                getSupportFragmentManager());

        adapter.addFragment(new DescriptionFragment(), "Description");
        adapter.addFragment(new SellerRatingFragment(), "Rating");

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