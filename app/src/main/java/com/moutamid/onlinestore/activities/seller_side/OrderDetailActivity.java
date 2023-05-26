package com.moutamid.onlinestore.activities.seller_side;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.moutamid.onlinestore.R;
import com.moutamid.onlinestore.databinding.ActivityOrderDetailBinding;

public class OrderDetailActivity extends AppCompatActivity {
    ActivityOrderDetailBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrderDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



    }
}