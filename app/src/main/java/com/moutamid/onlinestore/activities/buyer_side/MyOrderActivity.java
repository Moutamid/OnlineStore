package com.moutamid.onlinestore.activities.buyer_side;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;

import com.fxn.stash.Stash;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.moutamid.onlinestore.R;
import com.moutamid.onlinestore.adapter.OrderBuyerAdapter;
import com.moutamid.onlinestore.constants.Constants;
import com.moutamid.onlinestore.databinding.ActivityMyOrderBinding;
import com.moutamid.onlinestore.models.BuyModel;

import java.util.ArrayList;

public class MyOrderActivity extends AppCompatActivity {
    ActivityMyOrderBinding binding;
    ArrayList<BuyModel> buyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMyOrderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        buyList = new ArrayList<>();

        Constants.initDialog(this);
        Constants.showDialog();

        binding.header.title.setText("My Orders");

        binding.recyler.setLayoutManager(new LinearLayoutManager(this));
        binding.recyler.setHasFixedSize(false);

        binding.header.back.setOnClickListener(v -> {
            Stash.put(Constants.isQuery, false);
            startActivity(new Intent(this, BuyerMainActivity.class));
            finish();
        });

        Constants.databaseReference().child(Constants.buy).child("buyer").child(Constants.auth().getCurrentUser().getUid())
                .get().addOnSuccessListener(dataSnapshot -> {
                    if (dataSnapshot.exists()){
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                            BuyModel model = snapshot.getValue(BuyModel.class);
                            buyList.add(model);
                        }
                    } else {
                        Snackbar.make(MyOrderActivity.this, binding.root, "No Data Found", Snackbar.LENGTH_SHORT).show();
                    }
                    Constants.dismissDialog();
                    binding.count.setText("Total# " + buyList.size());
                    OrderBuyerAdapter adapter = new OrderBuyerAdapter(MyOrderActivity.this, buyList);
                    binding.recyler.setAdapter(adapter);
                }).addOnFailureListener(e -> {
                    Constants.dismissDialog();
                    Snackbar.make(MyOrderActivity.this, binding.root, e.getMessage(), Snackbar.LENGTH_SHORT).show();
                });

    }
}