package com.moutamid.onlinestore.activities.seller_side;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.moutamid.onlinestore.R;
import com.moutamid.onlinestore.activities.buyer_side.MyOrderActivity;
import com.moutamid.onlinestore.adapter.OrderBuyerAdapter;
import com.moutamid.onlinestore.constants.Constants;
import com.moutamid.onlinestore.databinding.ActivitySellerDashboardBinding;
import com.moutamid.onlinestore.models.BuyModel;
import com.moutamid.onlinestore.models.UserModel;

import java.util.ArrayList;

public class SellerDashboardActivity extends AppCompatActivity {
    ActivitySellerDashboardBinding binding;
    ArrayList<BuyModel> buyList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySellerDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Constants.checkApp(this);
        Constants.initDialog(this);
        Constants.showDialog();

        buyList = new ArrayList<>();

        binding.recyler.setLayoutManager(new LinearLayoutManager(this));
        binding.recyler.setHasFixedSize(false);

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
                            binding.listedCount.setText(""+0);
                        }
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
        Constants.databaseReference().child(Constants.buy).child("seller").child(Constants.auth().getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                                BuyModel model = snapshot.getValue(BuyModel.class);
                                buyList.add(model);
                            }

                            binding.ordersCount.setText(""+buyList.size());

                        }  else {
                            binding.ordersCount.setText(""+0);
                        }
                        Constants.dismissDialog();
                        OrderBuyerAdapter adapter = new OrderBuyerAdapter(SellerDashboardActivity.this, buyList);
                        binding.recyler.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError e) {
                        Constants.dismissDialog();
                        Snackbar.make(SellerDashboardActivity.this, binding.root, e.getMessage(), Snackbar.LENGTH_SHORT).show();
                    }
                });
    }

}