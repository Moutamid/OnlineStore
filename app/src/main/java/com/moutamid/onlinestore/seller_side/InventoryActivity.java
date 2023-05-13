package com.moutamid.onlinestore.seller_side;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.moutamid.onlinestore.R;
import com.moutamid.onlinestore.adapter.InventoryAdapter;
import com.moutamid.onlinestore.constants.Constants;
import com.moutamid.onlinestore.databinding.ActivityInventoryBinding;
import com.moutamid.onlinestore.models.ProductModel;

import java.util.ArrayList;

public class InventoryActivity extends AppCompatActivity {
    ActivityInventoryBinding binding;
    ArrayList<ProductModel> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInventoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.header.title.setText("Add Product for listing");

        Constants.initDialog(this);
        Constants.showDialog();

        productList = new ArrayList<>();

        binding.header.back.setOnClickListener(v -> {
            startActivity(new Intent(this, SellerDashboardActivity.class));
            finish();
        });

        binding.recyler.setLayoutManager(new LinearLayoutManager(this));
        binding.recyler.setHasFixedSize(false);

        Constants.databaseReference().child(Constants.Product).child(Constants.auth().getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            productList.clear();
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                ProductModel productModel = dataSnapshot.getValue(ProductModel.class);
                                productList.add(productModel);
                            }
                        }
                        Constants.dismissDialog();
                        InventoryAdapter adapter = new InventoryAdapter(InventoryActivity.this, productList);
                        binding.recyler.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Constants.dismissDialog();
                        Snackbar.make(InventoryActivity.this, binding.root, "Something went wrong", Snackbar.LENGTH_SHORT).show();
                    }
                });


    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, SellerDashboardActivity.class));
        finish();
    }


}