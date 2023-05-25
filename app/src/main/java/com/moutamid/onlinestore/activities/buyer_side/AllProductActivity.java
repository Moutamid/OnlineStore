package com.moutamid.onlinestore.activities.buyer_side;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.fxn.stash.Stash;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.moutamid.onlinestore.MainActivity;
import com.moutamid.onlinestore.R;
import com.moutamid.onlinestore.adapter.ProductAdapter;
import com.moutamid.onlinestore.adapter.SearchProductAdapter;
import com.moutamid.onlinestore.constants.Constants;
import com.moutamid.onlinestore.databinding.ActivityAllProductBinding;
import com.moutamid.onlinestore.models.ProductModel;

import java.util.ArrayList;

public class AllProductActivity extends AppCompatActivity {
    ActivityAllProductBinding binding;
    ArrayList<ProductModel> productList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAllProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        productList = new ArrayList<>();

        binding.header.title.setText("Products");

        binding.header.back.setOnClickListener(v -> {
            Stash.put(Constants.isQuery, false);
            startActivity(new Intent(this, BuyerMainActivity.class));
            finish();
        });

        if (Stash.getBoolean(Constants.isQuery, false)) {
            Log.d("dddddd", "enter Search");
            showSearch();
        } else {
            if (Stash.getBoolean(Constants.isSEARCH, false)){
                showCat();
            } else {
                showAll();
            }
        }

    }

    private void showSearch() {
        String cat = Stash.getString(Constants.SEARCH).toLowerCase();
        binding.header.title.setText("Search for \"" + cat + "\"");
        Constants.databaseReference().child(Constants.Product)
                .get().addOnSuccessListener(dataSnapshot -> {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            for (DataSnapshot ss : snapshot.getChildren()) {
                                ProductModel productModel = ss.getValue(ProductModel.class);
                                if (productModel.getCategory().toLowerCase().contains(cat) || productModel.getName().toLowerCase().contains(cat)) {
                                    productList.add(productModel);
                                } else {
                                    Log.d("dddddd", "fdd");
                                }
                            }
                        }

                        if (productList.size() == 0) {
                            Snackbar.make(AllProductActivity.this, binding.root, "No Product Found", Snackbar.LENGTH_INDEFINITE).show();
                        } else {
                            binding.recyler.setVisibility(View.VISIBLE);
                            binding.loading.setVisibility(View.GONE);
                        }

                    } else {
                        Snackbar.make(AllProductActivity.this, binding.root, "No Product Found", Snackbar.LENGTH_INDEFINITE).show();
                    }

                    Constants.dismissDialog();
                    SearchProductAdapter adapter = new SearchProductAdapter(AllProductActivity.this, productList);
                    binding.recyler.setAdapter(adapter);
                }).addOnFailureListener(e -> {
                    Constants.dismissDialog();
                    Snackbar.make(AllProductActivity.this, binding.root, "Something went wrong", Snackbar.LENGTH_SHORT).show();
                });
    }

    private void showCat() {
        String cat = Stash.getString(Constants.SEARCH);
        binding.header.title.setText("Search for \"" + cat + "\"");
        Constants.databaseReference().child(Constants.Product)
                .get().addOnSuccessListener(dataSnapshot -> {
                    if (dataSnapshot.exists()){
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            for (DataSnapshot ss : snapshot.getChildren()) {
                                ProductModel productModel = ss.getValue(ProductModel.class);
                                if (cat.equalsIgnoreCase(productModel.getCategory())) {
                                    productList.add(productModel);
                                }
                            }
                        }

                        if (productList.size() == 0) {
                            Snackbar.make(AllProductActivity.this, binding.root, "No Product Found", Snackbar.LENGTH_INDEFINITE).show();
                        } else {
                            binding.recyler.setVisibility(View.VISIBLE);
                            binding.loading.setVisibility(View.GONE);
                        }

                    } else {
                        Snackbar.make(AllProductActivity.this, binding.root, "No Product Found", Snackbar.LENGTH_INDEFINITE).show();
                    }

                    Constants.dismissDialog();
                    SearchProductAdapter adapter = new SearchProductAdapter(AllProductActivity.this, productList);
                    binding.recyler.setAdapter(adapter);
                }).addOnFailureListener(e -> {
                    Constants.dismissDialog();
                    Snackbar.make(AllProductActivity.this, binding.root, "Something went wrong", Snackbar.LENGTH_SHORT).show();
                });
    }

    private void showAll() {
        Constants.databaseReference().child(Constants.Product)
                .get().addOnSuccessListener(dataSnapshot -> {
                    if (dataSnapshot.exists()){
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            for (DataSnapshot ss : snapshot.getChildren()) {
                                ProductModel productModel = ss.getValue(ProductModel.class);
                                productList.add(productModel);
                            }
                        }
                        binding.recyler.setVisibility(View.VISIBLE);
                        binding.loading.setVisibility(View.GONE);
                    } else {
                        Snackbar.make(AllProductActivity.this, binding.root, "No Product Found", Snackbar.LENGTH_INDEFINITE).show();
                    }
                    Constants.dismissDialog();
                    SearchProductAdapter adapter = new SearchProductAdapter(AllProductActivity.this, productList);
                    binding.recyler.setAdapter(adapter);
                }).addOnFailureListener(e -> {
                    Constants.dismissDialog();
                    Snackbar.make(AllProductActivity.this, binding.root, "Something went wrong", Snackbar.LENGTH_SHORT).show();
                });
    }

    @Override
    public void onBackPressed() {
        Stash.put(Constants.isQuery, false);
        startActivity(new Intent(this, BuyerMainActivity.class));
        finish();
    }
}