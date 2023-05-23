package com.moutamid.onlinestore.activities.buyer_side;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.ColorStateList;
import android.os.Bundle;

import com.google.android.material.shape.ShapeAppearanceModel;
import com.moutamid.onlinestore.R;
import com.moutamid.onlinestore.databinding.ActivityBuyerMainBinding;

public class BuyerMainActivity extends AppCompatActivity {
    ActivityBuyerMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBuyerMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.bottomNav.setItemActiveIndicatorColor(ColorStateList.valueOf(getResources().getColor(R.color.white)));
        binding.bottomNav.setItemActiveIndicatorShapeAppearance(new ShapeAppearanceModel().withCornerSize(50).toBuilder().build());
        binding.bottomNav.setItemActiveIndicatorHeight(100);
        binding.bottomNav.setItemActiveIndicatorWidth(100);
    }
}