package com.moutamid.onlinestore.fragments.buyer_fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fxn.stash.Stash;
import com.moutamid.onlinestore.R;
import com.moutamid.onlinestore.adapter.CartAdapter;
import com.moutamid.onlinestore.constants.Constants;
import com.moutamid.onlinestore.databinding.FragmentCartBinding;
import com.moutamid.onlinestore.lister.CartListner;
import com.moutamid.onlinestore.models.CartModel;

import java.util.ArrayList;

public class CartFragment extends Fragment {
    FragmentCartBinding binding;
    double price = 0;
    CartAdapter adapter;

    public CartFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCartBinding.inflate(getLayoutInflater(), container, false);

        Constants.initDialog(requireContext());

        ArrayList<CartModel> cart = Stash.getArrayList(Constants.CART, CartModel.class);
        binding.recyler.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyler.setHasFixedSize(false);
        adapter = new CartAdapter(requireContext(), cart, listner);
        binding.recyler.setAdapter(adapter);

        binding.count.setText("Total# " + cart.size());

        for (CartModel cartModel : cart) {
            price += cartModel.getProductModel().getPrice();
        }
        binding.price.setText("$" + String.format("%.2f", price));

        return binding.getRoot();
    }

    CartListner listner = new CartListner() {
        @Override
        public void onDeleteClick(CartModel cartModel, int pos) {
            ArrayList<CartModel> cart = Stash.getArrayList(Constants.CART, CartModel.class);
            Log.d("PPP", cart.size()+"");
            cart.remove(cartModel);
            binding.count.setText("Total# " + cart.size());
            String p = binding.price.getText().toString().substring(1);
            Log.d("PPP", cart.size()+"");
            double pp = Double.parseDouble(p);
            pp -= cartModel.getProductModel().getPrice();
            binding.price.setText("$" + String.format("%.2f", pp));
            Stash.put(Constants.CART, cart);
            adapter = new CartAdapter(requireContext(), cart, listner);
            binding.recyler.setAdapter(adapter);
        }
    };

}