package com.moutamid.onlinestore.seller_side;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.moutamid.onlinestore.R;
import com.moutamid.onlinestore.databinding.ActivityAddProductBinding;

public class AddProductActivity extends AppCompatActivity {
    ActivityAddProductBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



    }
}