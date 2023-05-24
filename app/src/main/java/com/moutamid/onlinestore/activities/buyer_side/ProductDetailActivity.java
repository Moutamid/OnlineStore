package com.moutamid.onlinestore.activities.buyer_side;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.fxn.stash.Stash;
import com.moutamid.onlinestore.constants.Constants;
import com.moutamid.onlinestore.databinding.ActivityProductDetailBinding;
import com.moutamid.onlinestore.models.ProductModel;

public class ProductDetailActivity extends AppCompatActivity {
    ActivityProductDetailBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Constants.initDialog(this);
        Constants.showDialog();
        ProductModel model = (ProductModel) Stash.getObject(Constants.MODEL, ProductModel.class);

        if (Stash.getBoolean(Constants.isSEARCH, false)){
            searchProduct();
        } else {
            showDate(model);
        }

    }

    private void searchProduct() {
        String catToSearch = Stash.getString(Constants.SEARCH, "");
    }

    private void showDate(ProductModel model) {

    }


}