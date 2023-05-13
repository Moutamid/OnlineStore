package com.moutamid.onlinestore.seller_side;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.snackbar.Snackbar;
import com.moutamid.onlinestore.R;
import com.moutamid.onlinestore.constants.Constants;
import com.moutamid.onlinestore.databinding.ActivityUpdateProfileBinding;
import com.moutamid.onlinestore.models.UserModel;

public class UpdateProfileActivity extends AppCompatActivity {
    private static final int IMAGE_CODE = 1;
    ActivityUpdateProfileBinding binding;
    Uri image;
    String password, email;
    String imageLink;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUpdateProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.header.title.setText("Profile");

        Constants.initDialog(this);
        Constants.showDialog();

        binding.header.back.setOnClickListener(v -> {
            startActivity(new Intent(this, SellerDashboardActivity.class));
            finish();
        });

        binding.image.setOnClickListener(v -> {
            ImagePicker.with(this).cropSquare().compress(1024)
                    .maxResultSize(1080, 1080)
                    .start(IMAGE_CODE);
        });


        Constants.databaseReference().child(Constants.User).child(Constants.auth().getCurrentUser().getUid())
                .get().addOnSuccessListener(dataSnapshot -> {
                    if (dataSnapshot.exists()) {
                        UserModel userModel = dataSnapshot.getValue(UserModel.class);
                        binding.email.setText(userModel.getEmail());
                        binding.address.setText(userModel.getAddress());
                        binding.phone.setText("+"+userModel.getPhoneNumber());
                        binding.username.setText(userModel.getName());
                        imageLink = userModel.getImage();
                        password = userModel.getPassword();
                        email = userModel.getEmail();
                        Glide.with(UpdateProfileActivity.this).load(userModel.getImage()).placeholder(R.drawable.profile_icon).into(binding.profile);
                    }
                    Constants.dismissDialog();
                }).addOnFailureListener(e -> {
                    Constants.dismissDialog();
                    Snackbar.make(this, binding.root, e.getMessage(), Snackbar.LENGTH_SHORT).show();
                });

        binding.update.setOnClickListener(v -> {
            Constants.showDialog();
            if (image != null){
                uploadImage(image);
            } else {
                updateProfile(imageLink);
            }
        });

    }

    private void updateProfile(String imageLink) {
        UserModel userModel = new UserModel(
          Constants.auth().getCurrentUser().getUid(),
                binding.username.getText().toString(), email, password,
                binding.phone.getText().toString(),
                binding.address.getText().toString(), imageLink, true
        );
        Constants.databaseReference().child(Constants.User).child(Constants.auth().getCurrentUser().getUid()).setValue(userModel)
                .addOnSuccessListener(unused -> {
                    Constants.dismissDialog();
                    Snackbar.make(this, binding.root, "Profile Updated", Snackbar.LENGTH_INDEFINITE).show();
                }).addOnFailureListener(e -> {
                    Constants.dismissDialog();
                    Snackbar.make(this, binding.root, "Something went wrong", Snackbar.LENGTH_SHORT).show();
                });
    }

    private void uploadImage(Uri image) {
        Constants.storageReference(Constants.auth().getCurrentUser().getUid()).child("logo").putFile(image)
                .addOnSuccessListener(taskSnapshot -> {
                    taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(uri -> {
                        updateProfile(uri.toString());
                    }).addOnFailureListener(e -> {
                        Constants.dismissDialog();
                        Snackbar.make(this, binding.root, "Something went wrong", Snackbar.LENGTH_SHORT).show();
                    });
                }).addOnFailureListener(e -> {
                    Constants.dismissDialog();
                    Snackbar.make(this, binding.root, "Something went wrong", Snackbar.LENGTH_SHORT).show();
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_CODE) {
            if (resultCode == RESULT_OK && data != null) {
                image = data.getData();
                Glide.with(UpdateProfileActivity.this).load(image).placeholder(R.drawable.profile_icon).into(binding.profile);
            }
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, SellerDashboardActivity.class));
        finish();
    }

}