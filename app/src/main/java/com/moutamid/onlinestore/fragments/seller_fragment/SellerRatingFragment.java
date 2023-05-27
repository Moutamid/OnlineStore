package com.moutamid.onlinestore.fragments.seller_fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.fxn.stash.Stash;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.moutamid.onlinestore.R;
import com.moutamid.onlinestore.adapter.RatingAdapter;
import com.moutamid.onlinestore.constants.Constants;
import com.moutamid.onlinestore.databinding.FragmentSellerRatingBinding;
import com.moutamid.onlinestore.models.ProductModel;
import com.moutamid.onlinestore.models.RatingModel;

import java.util.ArrayList;

public class SellerRatingFragment extends Fragment {
    FragmentSellerRatingBinding binding;
    ArrayList<RatingModel> list;
    public SellerRatingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSellerRatingBinding.inflate(getLayoutInflater(), container, false);

        ProductModel model = (ProductModel) Stash.getObject(Constants.MODEL, ProductModel.class);

        binding.recyler.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyler.setHasFixedSize(false);

        list = new ArrayList<>();

        getRating(model);

        return binding.getRoot();
    }

    private void getRating(ProductModel model) {
        Constants.databaseReference().child(Constants.Rating).child(model.getID())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                RatingModel model1 = dataSnapshot.getValue(RatingModel.class);
                                list.add(model1);
                            }
                        }

                        if (list.size() > 0){
                            binding.noLayout.setVisibility(View.GONE);
                            binding.recyler.setVisibility(View.VISIBLE);
                        } else {
                            binding.noLayout.setVisibility(View.VISIBLE);
                            binding.recyler.setVisibility(View.GONE);
                        }

                        RatingAdapter adapter = new RatingAdapter(requireContext(), list);
                        binding.recyler.setAdapter(adapter);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(requireContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

}