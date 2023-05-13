package com.moutamid.onlinestore.seller_side;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
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

        binding.inventory.setOnClickListener(v -> {
            startActivity(new Intent(this, InventoryActivity.class));
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

        productCount();

    }

    private void productCount() {
        Constants.databaseReference().child(Constants.Product).child(Constants.auth().getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            binding.listedCount.setText(""+snapshot.getChildrenCount());
                        } else {
                            binding.listedCount.setText(0);
                        }
                        Constants.dismissDialog();
                        getSoldCount();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Constants.dismissDialog();
                        Toast.makeText(SellerDashboardActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void getSoldCount() {

    }

}