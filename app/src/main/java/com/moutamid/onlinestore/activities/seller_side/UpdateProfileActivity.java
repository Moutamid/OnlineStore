package com.moutamid.onlinestore.activities.seller_side;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
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
            showDialog();
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

    private void showDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bs_get_image);

        Button openCamera = dialog.findViewById(R.id.btnOpenCamera);
        Button openGallery = dialog.findViewById(R.id.btnOpenGallery);
        Button cancel = dialog.findViewById(R.id.btnCancel);

        cancel.setOnClickListener(v -> {
            dialog.cancel();
        });

        openGallery.setOnClickListener(v -> {
            getImageFromGallery();
            dialog.cancel();
        });

        openCamera.setOnClickListener(v -> {
            getImageFromCamera();
            dialog.cancel();
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.BottomSheetAnim;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    private void getImageFromCamera() {
        ImagePicker.with(this)
                .cameraOnly()
                .cropSquare()
                .compress(512)
                .maxResultSize(1080, 1080)
                .start(IMAGE_CODE);
        Constants.showDialog();
    }

    private void getImageFromGallery() {
        ImagePicker.with(this)
                .galleryOnly()
                .cropSquare()
                .compress(512)
                .maxResultSize(1080, 1080)
                .start(IMAGE_CODE);
        Constants.showDialog();
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
                Glide.with(UpdateProfileActivity.this).load(image).addListener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        Constants.dismissDialog();
                        Snackbar.make(UpdateProfileActivity.this, binding.root, "Something went wrong", Snackbar.LENGTH_SHORT).show();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        Constants.dismissDialog();
                        return false;
                    }
                }).placeholder(R.drawable.profile_icon).into(binding.profile);
            }
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, SellerDashboardActivity.class));
        finish();
    }

}