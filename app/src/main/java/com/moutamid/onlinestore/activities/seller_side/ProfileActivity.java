package com.moutamid.onlinestore.activities.seller_side;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.moutamid.onlinestore.R;
import com.moutamid.onlinestore.SplashScreenActivity;
import com.moutamid.onlinestore.constants.Constants;
import com.moutamid.onlinestore.databinding.ActivityProfileBinding;
import com.moutamid.onlinestore.models.UserModel;

public class ProfileActivity extends AppCompatActivity {
    ActivityProfileBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.header.title.setText("Profile");

        Constants.initDialog(this);
        Constants.showDialog();

        binding.header.back.setOnClickListener(v -> {
            startActivity(new Intent(this, SellerDashboardActivity.class));
            finish();
        });

        Constants.databaseReference().child(Constants.User).child(Constants.auth().getCurrentUser().getUid())
                .get().addOnSuccessListener(dataSnapshot -> {
                    if (dataSnapshot.exists()) {
                        UserModel userModel = dataSnapshot.getValue(UserModel.class);
                        binding.email.setText(userModel.getEmail());
                        binding.address.setText(userModel.getAddress());
                        binding.phone.setText("+"+userModel.getPhoneNumber());
                        binding.username.setText(userModel.getName());
                        Glide.with(ProfileActivity.this).load(userModel.getImage()).placeholder(R.drawable.profile_icon).into(binding.profile);
                    }
                    Constants.dismissDialog();
                }).addOnFailureListener(e -> {
                    Constants.dismissDialog();
                    Snackbar.make(this, binding.root, e.getMessage(), Snackbar.LENGTH_SHORT).show();
                });

        binding.update.setOnClickListener(v -> {
            startActivity(new Intent(this, UpdateProfileActivity.class));
            finish();
        });

        binding.logout.setOnClickListener(v -> {
            Constants.auth().signOut();
            startActivity(new Intent(this, SplashScreenActivity.class));
            finish();
        });

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, SellerDashboardActivity.class));
        finish();
    }
}