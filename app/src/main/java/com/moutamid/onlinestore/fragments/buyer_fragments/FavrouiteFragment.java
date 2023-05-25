package com.moutamid.onlinestore.fragments.buyer_fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fxn.stash.Stash;
import com.moutamid.onlinestore.R;
import com.moutamid.onlinestore.adapter.ProductAdapter;
import com.moutamid.onlinestore.adapter.ProductAllAdapter;
import com.moutamid.onlinestore.constants.Constants;
import com.moutamid.onlinestore.databinding.FragmentFavrouiteBinding;
import com.moutamid.onlinestore.models.ProductModel;

import java.util.ArrayList;

public class FavrouiteFragment extends Fragment {
    FragmentFavrouiteBinding binding;
    public FavrouiteFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentFavrouiteBinding.inflate(getLayoutInflater(), container, false);

        binding.recyler.setLayoutManager(new GridLayoutManager(requireContext(), 1));
        binding.recyler.setHasFixedSize(false);

        ArrayList<ProductModel> productList = Stash.getArrayList(Constants.favrt, ProductModel.class);
        ProductAllAdapter adapter = new ProductAllAdapter(requireContext(), productList);
        binding.recyler.setAdapter(adapter);

        return binding.getRoot();
    }
}