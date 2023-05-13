package com.moutamid.onlinestore.seller_side;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.moutamid.onlinestore.R;
import com.moutamid.onlinestore.databinding.ActivityInventoryBinding;

public class InventoryActivity extends AppCompatActivity {
    ActivityInventoryBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInventoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



    }
}