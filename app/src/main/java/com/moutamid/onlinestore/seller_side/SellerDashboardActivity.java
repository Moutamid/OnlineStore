package com.moutamid.onlinestore.seller_side;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.moutamid.onlinestore.R;
import com.moutamid.onlinestore.databinding.ActivitySellerDashboardBinding;

public class SellerDashboardActivity extends AppCompatActivity {
    ActivitySellerDashboardBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySellerDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}