package com.moutamid.onlinestore;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.moutamid.onlinestore.activities.buyer_side.BuyerLoginActivity;
import com.moutamid.onlinestore.constants.Constants;
import com.moutamid.onlinestore.activities.seller_side.LoginActivity;
import com.moutamid.onlinestore.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Constants.checkApp(this);

        binding.seller.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });

        binding.buyer.setOnClickListener(v -> {
            startActivity(new Intent(this, BuyerLoginActivity.class));
            finish();
        });

    }
}