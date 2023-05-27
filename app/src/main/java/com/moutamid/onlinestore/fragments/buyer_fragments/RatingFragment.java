package com.moutamid.onlinestore.fragments.buyer_fragments;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.fxn.stash.Stash;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.moutamid.onlinestore.R;
import com.moutamid.onlinestore.adapter.RatingAdapter;
import com.moutamid.onlinestore.constants.Constants;
import com.moutamid.onlinestore.databinding.FragmentRatingBinding;
import com.moutamid.onlinestore.models.ProductModel;
import com.moutamid.onlinestore.models.RatingModel;
import com.moutamid.onlinestore.models.UserModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class RatingFragment extends Fragment {
    FragmentRatingBinding binding;
    ArrayList<RatingModel> list;

    UserModel userModel;

    public RatingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentRatingBinding.inflate(getLayoutInflater(), container, false);

        ProductModel model = (ProductModel) Stash.getObject(Constants.MODEL, ProductModel.class);

        Constants.initDialog(requireContext());

        binding.recycler.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recycler.setHasFixedSize(false);

        Constants.databaseReference().child(Constants.User).child(Constants.auth().getCurrentUser().getUid())
                .get().addOnSuccessListener(dataSnapshot -> {
                    if (dataSnapshot.exists()){
                        userModel = dataSnapshot.getValue(UserModel.class);
                    }
                }).addOnFailureListener(e -> {
                    e.printStackTrace();
                    Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                });

        list = new ArrayList<>();

        getRating(model);

        binding.add.setOnClickListener(v -> {
            addReview(model, userModel);
        });

        return binding.getRoot();
    }

    private void addReview(ProductModel model, UserModel userModel) {
        final Dialog dialog = new Dialog(requireContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.add_review);
        dialog.setCancelable(true);

        AtomicInteger starCount = new AtomicInteger();
        starCount.set(0);

        EditText message = dialog.findViewById(R.id.message);
        Button cancel = dialog.findViewById(R.id.cancelBtn);
        Button add = dialog.findViewById(R.id.addBtn);

        ImageView star1, star2, star3, star4, star5;

        star1 = dialog.findViewById(R.id.star1);
        star2 = dialog.findViewById(R.id.star2);
        star3 = dialog.findViewById(R.id.star3);
        star4 = dialog.findViewById(R.id.star4);
        star5 = dialog.findViewById(R.id.star5);

        cancel.setOnClickListener(v -> {
            dialog.cancel();
        });
        
        add.setOnClickListener(v -> {
            Map<String, Object> rating = new HashMap<>();
            if (starCount.get() == 0 || message.getText().toString().isEmpty()) {
                Toast.makeText(requireContext(), "Please Add A Review First", Toast.LENGTH_SHORT).show();
            } else {
                Constants.showDialog();

                RatingModel ratingModel = new RatingModel(
                        userModel.getImage(), userModel.getName(), message.getText().toString(),
                        starCount.get(), new Date().getTime()
                );

                if (starCount.get() == 5) {
                    rating.put("star5", (model.getStar5() + 1));
                } else if (starCount.get() == 4) {
                    rating.put("star4", (model.getStar4() + 1));
                } else if (starCount.get() == 3) {
                    rating.put("star3", (model.getStar3() + 1));
                } else if (starCount.get() == 2) {
                    rating.put("star2", (model.getStar2() + 1));
                } else if (starCount.get() == 1) {
                    rating.put("star1", (model.getStar1() + 1));
                }
                rating.put("ratingCount", (model.getRatingCount() + 1));

                Constants.databaseReference().child(Constants.Product).child(model.getUserID())
                        .child(model.getID()).updateChildren(rating)
                        .addOnSuccessListener(unused -> {
                                Constants.databaseReference().child(Constants.Rating).child(model.getID()).push()
                                        .setValue(ratingModel).addOnSuccessListener(unused1 -> {
                                            Constants.dismissDialog();
                                            Toast.makeText(requireContext(), "Thanks for your rating", Toast.LENGTH_SHORT).show();
                                        }).addOnFailureListener(e -> {
                                            Constants.dismissDialog();
                                            Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                        });
                        }).addOnFailureListener(e -> {
                            Constants.dismissDialog();
                            Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
            }
        });

        star1.setOnClickListener(v -> {
            star1.setImageDrawable(getResources().getDrawable(R.drawable.star_rate_yellow));
            star2.setImageDrawable(getResources().getDrawable(R.drawable.star_rate_grey));
            star3.setImageDrawable(getResources().getDrawable(R.drawable.star_rate_grey));
            star4.setImageDrawable(getResources().getDrawable(R.drawable.star_rate_grey));
            star5.setImageDrawable(getResources().getDrawable(R.drawable.star_rate_grey));
            starCount.set(1);
        });
        star2.setOnClickListener(v -> {
            star1.setImageDrawable(getResources().getDrawable(R.drawable.star_rate_yellow));
            star2.setImageDrawable(getResources().getDrawable(R.drawable.star_rate_yellow));
            star3.setImageDrawable(getResources().getDrawable(R.drawable.star_rate_grey));
            star4.setImageDrawable(getResources().getDrawable(R.drawable.star_rate_grey));
            star5.setImageDrawable(getResources().getDrawable(R.drawable.star_rate_grey));
            starCount.set(2);
        });
        star3.setOnClickListener(v -> {
            star1.setImageDrawable(getResources().getDrawable(R.drawable.star_rate_yellow));
            star2.setImageDrawable(getResources().getDrawable(R.drawable.star_rate_yellow));
            star3.setImageDrawable(getResources().getDrawable(R.drawable.star_rate_yellow));
            star4.setImageDrawable(getResources().getDrawable(R.drawable.star_rate_grey));
            star5.setImageDrawable(getResources().getDrawable(R.drawable.star_rate_grey));
            starCount.set(3);
        });
        star4.setOnClickListener(v -> {
            star1.setImageDrawable(getResources().getDrawable(R.drawable.star_rate_yellow));
            star2.setImageDrawable(getResources().getDrawable(R.drawable.star_rate_yellow));
            star3.setImageDrawable(getResources().getDrawable(R.drawable.star_rate_yellow));
            star4.setImageDrawable(getResources().getDrawable(R.drawable.star_rate_yellow));
            star5.setImageDrawable(getResources().getDrawable(R.drawable.star_rate_grey));
            starCount.set(4);
        });
        star5.setOnClickListener(v -> {
            star1.setImageDrawable(getResources().getDrawable(R.drawable.star_rate_yellow));
            star2.setImageDrawable(getResources().getDrawable(R.drawable.star_rate_yellow));
            star3.setImageDrawable(getResources().getDrawable(R.drawable.star_rate_yellow));
            star4.setImageDrawable(getResources().getDrawable(R.drawable.star_rate_yellow));
            star5.setImageDrawable(getResources().getDrawable(R.drawable.star_rate_yellow));
            starCount.set(5);
        });


        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.CENTER);

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
                            binding.recycler.setVisibility(View.VISIBLE);
                        } else {
                            binding.noLayout.setVisibility(View.VISIBLE);
                            binding.recycler.setVisibility(View.GONE);
                        }

                        RatingAdapter adapter = new RatingAdapter(requireContext(), list);
                        binding.recycler.setAdapter(adapter);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(requireContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}