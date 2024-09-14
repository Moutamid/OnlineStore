package com.moutamid.onlinestore.activities.buyer_side;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.widget.Toast;

import com.fxn.stash.Stash;
import com.google.android.material.shape.ShapeAppearanceModel;
import com.moutamid.onlinestore.R;
import com.moutamid.onlinestore.constants.Constants;
import com.moutamid.onlinestore.databinding.ActivityBuyerMainBinding;
import com.moutamid.onlinestore.fragments.buyer_fragments.CartFragment;
import com.moutamid.onlinestore.fragments.buyer_fragments.FavrouiteFragment;
import com.moutamid.onlinestore.fragments.buyer_fragments.HomeFragment;
import com.moutamid.onlinestore.fragments.buyer_fragments.PersonFragment;

public class BuyerMainActivity extends AppCompatActivity {
    ActivityBuyerMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBuyerMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //Constants.checkApp(this);

        binding.bottomNav.setItemActiveIndicatorColor(ColorStateList.valueOf(getResources().getColor(R.color.white)));
        binding.bottomNav.setItemActiveIndicatorShapeAppearance(new ShapeAppearanceModel().withCornerSize(50).toBuilder().build());
        binding.bottomNav.setItemActiveIndicatorHeight(100);
        binding.bottomNav.setItemActiveIndicatorWidth(100);

        binding.searchBtn.setOnClickListener(v -> {
            if (binding.search.getText().toString().isEmpty()){
                Toast.makeText(this, "Search query is empty", Toast.LENGTH_SHORT).show();
            } else {
                String ss = binding.search.getText().toString();
                Stash.put(Constants.SEARCH, ss);
                Stash.put(Constants.isQuery, true);
                startActivity(new Intent(this, AllProductActivity.class));
                finish();
            }
        });

        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new HomeFragment()).commit();
        binding.bottomNav.setSelectedItemId(R.id.nav_home);

        binding.bottomNav.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new HomeFragment()).commit();
            } else if (itemId == R.id.nav_cart) {
                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new CartFragment()).commit();
            } else if (itemId == R.id.nav_favr) {
                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new FavrouiteFragment()).commit();
            } else if (itemId == R.id.nav_user) {
                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new PersonFragment()).commit();
            }
            return true;
        });

    }
}