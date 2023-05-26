package com.moutamid.onlinestore.activities.seller_side;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.moutamid.onlinestore.R;
import com.moutamid.onlinestore.databinding.ActivityProductDetailBinding;

public class ProductUpdateActivity extends AppCompatActivity {
    ActivityProductDetailBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}