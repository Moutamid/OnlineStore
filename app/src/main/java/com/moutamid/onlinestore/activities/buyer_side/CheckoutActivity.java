package com.moutamid.onlinestore.activities.buyer_side;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.fxn.stash.Stash;
import com.google.android.material.snackbar.Snackbar;
import com.moutamid.onlinestore.R;
import com.moutamid.onlinestore.constants.Constants;
import com.moutamid.onlinestore.databinding.ActivityCheckoutBinding;
import com.moutamid.onlinestore.models.BuyModel;
import com.moutamid.onlinestore.models.CartModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class CheckoutActivity extends AppCompatActivity {
    ActivityCheckoutBinding binding;
    ArrayList<BuyModel> buyList;
    double subTotal = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCheckoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Constants.initDialog(this);

        buyList = new ArrayList<>();

        ArrayList<CartModel> cart = Stash.getArrayList(Constants.CART, CartModel.class);

        String price = Stash.getString(Constants.checkoutPrice);
        binding.total.setText("$" + price);
        subTotal = Double.parseDouble(price)+5;
        binding.totalPayment.setText("$" + subTotal);
        binding.price.setText("$" + subTotal);
        binding.checc.setText("Checkout (" + cart.size() + ")");

        binding.buy.setOnClickListener(v -> {
            if (valid()){
                Constants.showDialog();
                for (int i = 0; i < cart.size(); i++) {
                    String ID = UUID.randomUUID().toString();
                    BuyModel buyModel = new BuyModel (
                            ID,
                            binding.name.getEditText().getText().toString(),
                            binding.numb.getEditText().getText().toString(),
                            binding.address.getEditText().getText().toString(),
                            cart.get(i).getProductModel(), cart.get(i).getCount(),
                            (binding.cod.isChecked() ? "COD" : "IAP"),
                            subTotal, new Date().getTime());
                    buyList.add(buyModel);

                    int finalI = i;
                    Constants.databaseReference().child(Constants.buy).child("seller").child(buyModel.getProduct().getUserID()).child(ID).setValue(buyModel)
                            .addOnSuccessListener(unused -> {
                                Constants.databaseReference().child(Constants.buy).child("buyer").child(Constants.auth().getCurrentUser().getUid()).child(ID).setValue(buyModel)
                                        .addOnSuccessListener(unused2 -> {
                                            if (finalI == cart.size()-1){
                                                Constants.dismissDialog();
                                                Toast.makeText(this, "Thank you for buying", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(CheckoutActivity.this, BuyerMainActivity.class));
                                                finish();
                                            }
                                        }).addOnFailureListener(e -> {
                                            Constants.dismissDialog();
                                            Snackbar.make(CheckoutActivity.this, binding.root, e.getMessage(), Snackbar.LENGTH_SHORT).show();
                                        });
                            }).addOnFailureListener(e -> {
                                Constants.dismissDialog();
                                Snackbar.make(CheckoutActivity.this, binding.root, e.getMessage(), Snackbar.LENGTH_SHORT).show();
                            });
                }
            }
        });

    }

    private boolean valid() {

        if (binding.name.getEditText().getText().toString().isEmpty()){
            Toast.makeText(this, "Please add your Name", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (binding.numb.getEditText().getText().toString().isEmpty()){
            Toast.makeText(this, "Please add your Mobile Number", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (binding.address.getEditText().getText().toString().isEmpty()){
            Toast.makeText(this, "Please add your Address", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, BuyerMainActivity.class));
        finish();
    }
}