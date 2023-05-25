package com.moutamid.onlinestore.fragments.buyer_fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fxn.stash.Stash;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.moutamid.onlinestore.activities.buyer_side.AllProductActivity;
import com.moutamid.onlinestore.adapter.CategoryAdapter;
import com.moutamid.onlinestore.adapter.ProductAdapter;
import com.moutamid.onlinestore.constants.Constants;
import com.moutamid.onlinestore.databinding.FragmentHomeBinding;
import com.moutamid.onlinestore.models.CategoryModel;
import com.moutamid.onlinestore.models.ProductModel;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class HomeFragment extends Fragment {
    FragmentHomeBinding binding;
    ArrayList<CategoryModel> categoryList;
    ArrayList<ProductModel> productList;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(getLayoutInflater(), container, false);

        Constants.initDialog(requireContext());

        categoryList = new ArrayList<>();
        productList = new ArrayList<>();

        binding.recyler.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.HORIZONTAL));
        binding.recyler.setHasFixedSize(false);
        binding.recylerProduct.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.HORIZONTAL));
        binding.recylerProduct.setHasFixedSize(false);

        binding.viewAll.setOnClickListener(v -> {
            Stash.put(Constants.isSEARCH, false);
            startActivity(new Intent(requireContext(), AllProductActivity.class));
        });

        addCategory();
        getProducts();

        return binding.getRoot();
    }

    private void getProducts() {
        Constants.showDialog();
        AtomicInteger i = new AtomicInteger();
        AtomicInteger j = new AtomicInteger();
        Constants.databaseReference().child(Constants.Product)
                .get().addOnSuccessListener(dataSnapshot -> {
                    if (dataSnapshot.exists()){
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            for (DataSnapshot ss : snapshot.getChildren()) {
                                if (j.get() < 3){
                                    ProductModel productModel = ss.getValue(ProductModel.class);
                                    productList.add(productModel);
                                }
                                j.getAndIncrement();
                            }
                            if (j.get() >= 3){
                                j.getAndSet(0);
                            }
                        }
                    }
                    Constants.dismissDialog();
                    ProductAdapter adapter = new ProductAdapter(requireContext(), productList);
                    binding.recylerProduct.setAdapter(adapter);
                }).addOnFailureListener(e -> {
                    Constants.dismissDialog();
                    Snackbar.make(requireContext(), binding.root, "Something went wrong", Snackbar.LENGTH_SHORT).show();
                });

    }

    private void addCategory() {
        categoryList.add(new CategoryModel("Electronics", "https://down-id.img.susercontent.com/file/dcd61dcb7c1448a132f49f938b0cb553_tn"));
        categoryList.add(new CategoryModel("Computer & Accessories", "https://down-id.img.susercontent.com/file/id-50009109-0bd6a9ebd0f2ae9b7e8b9ce7d89897d6_tn"));
        categoryList.add(new CategoryModel("Mom & Baby", "https://down-id.img.susercontent.com/file/4d1673a14c26c8361a76258d78446324_tn"));
        categoryList.add(new CategoryModel("Men Clothes", "https://down-id.img.susercontent.com/file/04dba508f1ad19629518defb94999ef9_tn"));
        categoryList.add(new CategoryModel("Men Bags", "https://down-id.img.susercontent.com/file/47ed832eed0feb62fd28f08c9229440e_tn"));
        categoryList.add(new CategoryModel("Fashion Accessories", "https://down-id.img.susercontent.com/file/1f18bdfe73df39c66e7326b0a3e08e87_tn"));
        categoryList.add(new CategoryModel("Health", "https://down-id.img.susercontent.com/file/eb7d583e4b72085e71cd21a70ce47d7a_tn"));
        categoryList.add(new CategoryModel("Hobby & Collection", "https://down-id.img.susercontent.com/file/42394b78fac1169d67c6291973a3b132_tn"));
        categoryList.add(new CategoryModel("Photography", "https://down-id.img.susercontent.com/file/9abe95c0c755968c5114f084ee11b8cb_tn"));
        categoryList.add(new CategoryModel("Mobile & Accessories", "https://down-id.img.susercontent.com/file/5230277eefafad8611aaf703d3e99568_tn"));
        categoryList.add(new CategoryModel("Automotive", "https://down-id.img.susercontent.com/file/27838b968afb76ca59dd8e8f57ece91f_tn"));
        categoryList.add(new CategoryModel("Women Bags", "https://down-id.img.susercontent.com/file/id-50009109-da8cea4e4705abb4dd935b244668e9dd_tn"));
        categoryList.add(new CategoryModel("Watches", "https://down-id.img.susercontent.com/file/2bdf8cf99543342d4ebd8e1bdb576f80_tn"));
        categoryList.add(new CategoryModel("Women Shoes", "https://down-id.img.susercontent.com/file/id-50009109-a947822064b7a8077b15596c85bd9303_tn"));
        categoryList.add(new CategoryModel("Baby & Kids Fashion", "https://down-id.img.susercontent.com/file/9251edd6d6dd98855ff5a99497835d9c_tn"));
        categoryList.add(new CategoryModel("Women Clothes", "https://down-id.img.susercontent.com/file/6d63cca7351ba54a2e21c6be1721fa3a_tn"));
        categoryList.add(new CategoryModel("Home & Living", "https://down-id.img.susercontent.com/file/c1494110e0383780cdea73ed890e0299_tn"));
        categoryList.add(new CategoryModel("Beauty & Care", "https://down-id.img.susercontent.com/file/2715b985ae706a4c39a486f83da93c4b_tn"));
        categoryList.add(new CategoryModel("Sports & Outdoor", "https://down-id.img.susercontent.com/file/b2c24b49fd96704ed80b4f45080bfcac_tn"));
        categoryList.add(new CategoryModel("Men Shoes", "https://down-id.img.susercontent.com/file/3c8ff51aab1692a80c5883972a679168_tn"));

        CategoryAdapter adapter = new CategoryAdapter(requireContext(), categoryList);
        binding.recyler.setAdapter(adapter);

    }
}