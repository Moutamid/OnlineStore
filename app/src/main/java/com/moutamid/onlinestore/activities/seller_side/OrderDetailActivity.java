package com.moutamid.onlinestore.activities.seller_side;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.fxn.stash.Stash;
import com.google.android.material.snackbar.Snackbar;
import com.moutamid.onlinestore.MainActivity;
import com.moutamid.onlinestore.R;
import com.moutamid.onlinestore.activities.buyer_side.BuyerMainActivity;
import com.moutamid.onlinestore.activities.buyer_side.CheckoutActivity;
import com.moutamid.onlinestore.constants.Constants;
import com.moutamid.onlinestore.databinding.ActivityOrderDetailBinding;
import com.moutamid.onlinestore.models.BuyModel;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class OrderDetailActivity extends AppCompatActivity {
    ActivityOrderDetailBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrderDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BuyModel model = (BuyModel) Stash.getObject(Constants.order, BuyModel.class);

        binding.header.title.setText("Order Detail");

        binding.header.back.setOnClickListener(v -> {
            startActivity(new Intent(this, SellerDashboardActivity.class));
            finish();
        });

        Glide.with(this).load(model.getProduct().getThumbnail()).into(binding.image);

        binding.count.setText(""+model.getProductCount());
        binding.address.setText(model.getAddress());
        binding.name.setText(model.getProduct().getName());
        binding.buyerName.setText(model.getName());
        binding.number.setText(model.getNumb());
        if (model.getType().equals("COD")) {
            binding.type.setText("Cash on delivery (COD)");
        } else {
            binding.type.setText("In-App Purchase (IAP)");
        }

        binding.total.setText("$" + model.getProduct().getPrice());
        double price = (model.getProduct().getPrice() * model.getProductCount()) + 5;
        binding.totalPayment.setText("$" + price);

        binding.date.setText(Constants.getFormatedDate(model.getTimeStamp()));
        binding.category.setText(model.getProduct().getCategory());
        binding.price.setText("$" + model.getProduct().getPrice() + " x " + model.getProductCount());

        binding.complete.setOnClickListener(v -> {
            Map<String, Object> map = new HashMap<>();
            map.put("status", "COM");
            Constants.databaseReference().child(Constants.buy).child("seller").child(model.getProduct().getUserID()).child(model.getID()).updateChildren(map)
                    .addOnSuccessListener(unused -> {
                        Constants.databaseReference().child(Constants.buy).child("buyer").child(model.getBuyerID()).child(model.getID()).updateChildren(map)
                                .addOnSuccessListener(unused2 -> {
                                    Constants.dismissDialog();
                                    Toast.makeText(this, "Order Completed", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(OrderDetailActivity.this, SellerDashboardActivity.class));
                                    finish();
                                }).addOnFailureListener(e -> {
                                    Constants.dismissDialog();
                                    Snackbar.make(OrderDetailActivity.this, binding.root, e.getMessage(), Snackbar.LENGTH_SHORT).show();
                                });
                    }).addOnFailureListener(e -> {
                        Constants.dismissDialog();
                        Snackbar.make(OrderDetailActivity.this, binding.root, e.getMessage(), Snackbar.LENGTH_SHORT).show();
                    });
        });

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, SellerDashboardActivity.class));
        finish();
    }
}