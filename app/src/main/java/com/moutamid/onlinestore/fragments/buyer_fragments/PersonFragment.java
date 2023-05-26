package com.moutamid.onlinestore.fragments.buyer_fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.moutamid.onlinestore.R;
import com.moutamid.onlinestore.SplashScreenActivity;
import com.moutamid.onlinestore.activities.buyer_side.AccountSettingActivity;
import com.moutamid.onlinestore.activities.buyer_side.MyOrderActivity;
import com.moutamid.onlinestore.activities.seller_side.ProfileActivity;
import com.moutamid.onlinestore.adapter.OrderBuyerAdapter;
import com.moutamid.onlinestore.constants.Constants;
import com.moutamid.onlinestore.databinding.FragmentPersonBinding;
import com.moutamid.onlinestore.models.UserModel;

public class PersonFragment extends Fragment {
    FragmentPersonBinding binding;
    public PersonFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPersonBinding.inflate(getLayoutInflater(), container, false);

        Constants.initDialog(requireContext());
        Constants.showDialog();

        Constants.databaseReference().child(Constants.User).child(Constants.auth().getCurrentUser().getUid())
                .get().addOnSuccessListener(dataSnapshot -> {
                    if (dataSnapshot.exists()) {
                        UserModel userModel = dataSnapshot.getValue(UserModel.class);
                        binding.email.setText(userModel.getEmail());
                        binding.name.setText(userModel.getName());
                        Glide.with(requireContext()).load(userModel.getImage()).placeholder(R.drawable.profile_icon).into(binding.profile);
                    }
                    Constants.dismissDialog();
                }).addOnFailureListener(e -> {
                    Constants.dismissDialog();
                    Snackbar.make(requireContext(), binding.root, e.getMessage(), Snackbar.LENGTH_SHORT).show();
                });

        binding.account.setOnClickListener(v -> {
            startActivity(new Intent(requireContext(), AccountSettingActivity.class));
            requireActivity().finish();
        });
        binding.orders.setOnClickListener(v -> {
            startActivity(new Intent(requireContext(), MyOrderActivity.class));
            requireActivity().finish();
        });

        binding.logout.setOnClickListener(v -> {
            new AlertDialog.Builder(requireContext()).setMessage("Are you sure you want to logout?")
                    .setTitle("Log out")
                    .setPositiveButton("Yes", ((dialog, which) -> {
                        dialog.dismiss();
                        Constants.showDialog();
                        new Handler().postDelayed(() -> {
                            Constants.dismissDialog();
                            Constants.auth().signOut();
                            startActivity(new Intent(requireContext(), SplashScreenActivity.class));
                            requireActivity().finish();
                        }, 1500);
                    })).setNegativeButton("No", (dialog, which) -> dialog.dismiss()).show();
        });

        return binding.getRoot();
    }
}