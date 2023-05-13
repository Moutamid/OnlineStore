package com.moutamid.onlinestore.seller_side;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.moutamid.onlinestore.R;
import com.moutamid.onlinestore.constants.Constants;
import com.moutamid.onlinestore.databinding.ActivitySellerDashboardBinding;
import com.moutamid.onlinestore.models.UserModel;

public class SellerDashboardActivity extends AppCompatActivity {
    ActivitySellerDashboardBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySellerDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Constants.checkApp(this);
        Constants.initDialog(this);
        Constants.showDialog();

        binding.profile.setOnClickListener(v -> {
            startActivity(new Intent(this, ProfileActivity.class));
            finish();
        });

        binding.newProduct.setOnClickListener(v -> {
            startActivity(new Intent(this, AddProductActivity.class));
            finish();
        });

        Constants.databaseReference().child(Constants.User).child(Constants.auth().getCurrentUser().getUid())
                .get().addOnSuccessListener(dataSnapshot -> {
                    if (dataSnapshot.exists()){
                        UserModel model = dataSnapshot.getValue(UserModel.class);
                        binding.username.setText(model.getName());
                        Glide.with(SellerDashboardActivity.this).load(model.getImage()).placeholder(R.drawable.profile_icon).into(binding.profile);
                    }
                    Constants.dismissDialog();
                }).addOnFailureListener(e -> {});

        getUserData();

    }

    private void getUserData() {

    }
}