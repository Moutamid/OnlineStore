package com.moutamid.onlinestore.fragments.buyer_fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fxn.stash.Stash;
import com.moutamid.onlinestore.R;
import com.moutamid.onlinestore.constants.Constants;
import com.moutamid.onlinestore.databinding.FragmentDescriptionBinding;
import com.moutamid.onlinestore.models.ProductModel;

public class DescriptionFragment extends Fragment {
    FragmentDescriptionBinding binding;
    public DescriptionFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDescriptionBinding.inflate(getLayoutInflater(), container, false);

        ProductModel model = (ProductModel) Stash.getObject(Constants.MODEL, ProductModel.class);

        binding.desc.setText(model.getDescription());

        return binding.getRoot();
    }
}