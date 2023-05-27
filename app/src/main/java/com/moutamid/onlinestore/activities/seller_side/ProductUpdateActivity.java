package com.moutamid.onlinestore.activities.seller_side;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import com.fxn.stash.Stash;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.moutamid.onlinestore.R;
import com.moutamid.onlinestore.adapter.ImageAdapter;
import com.moutamid.onlinestore.constants.Constants;
import com.moutamid.onlinestore.databinding.ActivityProductUpdateBinding;
import com.moutamid.onlinestore.models.ProductModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ProductUpdateActivity extends AppCompatActivity {
    ActivityProductUpdateBinding binding;

    private static final int PICK_FROM_GALLERY = 1;
    private static final int PICK_FROM_CAMERA = 2;
    private int limit = 6;
    ArrayList<String> images;
    ArrayList<String> imagesToUpload;
    ArrayList<Uri> imagesURi;
    ArrayAdapter<CharSequence> citiesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductUpdateBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.header.title.setText("Update Product for listing");


        images = new ArrayList<>();
        imagesURi = new ArrayList<>();
        imagesToUpload = new ArrayList<>();

        Constants.initDialog(this);
        Constants.showDialog();
        ProductModel model = (ProductModel) Stash.getObject(Constants.MODEL, ProductModel.class);
        showDate(model);

        binding.RecyclerViewImageList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        binding.RecyclerViewImageList.setHasFixedSize(true);

        citiesAdapter = ArrayAdapter.createFromResource(this, R.array.categories, R.layout.dropdown_layout);
        citiesAdapter.setDropDownViewResource(R.layout.simple_dropdown_item);
        binding.categoryAuto.setAdapter(citiesAdapter);

        binding.header.back.setOnClickListener(v -> {
            startActivity(new Intent(this, SellerDashboardActivity.class));
            finish();
        });

        binding.addImage.setOnClickListener(v -> {
            if (images.size() >= 6) {
                Toast.makeText(this, "You can only add 6 images", Toast.LENGTH_SHORT).show();
            } else {
                showDialog();
            }
        });

        binding.btnAddCarPhoto.setOnClickListener(v -> {
            if (images.size() >= 6) {
                Toast.makeText(this, "You can only add 6 images", Toast.LENGTH_SHORT).show();
            } else {
                showDialog();
            }
        });

        binding.update.setOnClickListener(v -> {
            if (valid()) {
                Constants.showDialog();
                if (imagesURi.size() > 0) {
                    uploadData(model);
                } else {
                    upload(model);
                }

            }
        });

    }

    private void upload(ProductModel model) {
        uploadProduct(model.getID(), model.getThumbnail(), model);
    }

    private void uploadData(ProductModel model) {
        String ID = model.getID();
        String currentDate = new SimpleDateFormat("ddMMyyyyhhmmss", Locale.getDefault()).format(new Date());
        Constants.databaseReference().child(Constants.ProductImages)
                .child(Constants.auth().getCurrentUser().getUid()).child(ID).removeValue();

        for (int count = 0; count < imagesURi.size(); count++) {
            int finalCount = count;
            Constants.storageReference(Constants.auth().getCurrentUser().getUid())
                    .child(currentDate + "_image" + count)
                    .putFile(imagesURi.get(count))
                    .addOnSuccessListener(taskSnapshot -> {
                        taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(uri -> {
                            ArrayList<String> images = Stash.getArrayList(Constants.IMAGE_LIST, String.class);
                            if (finalCount == 0) {
                                if (images.size() > 1) {
                                    uploadProduct(ID, model.getThumbnail(), model);
                                } else {
                                    uploadProduct(ID, uri.toString(), model);
                                }
                            }
                            imagesToUpload.add(uri.toString());
                            if (finalCount == imagesURi.size() - 1) {
                                uploadImage(ID);
                            }
                        }).addOnFailureListener(e -> {
                            Constants.dismissDialog();
                            Snackbar.make(this, binding.root, e.getMessage(), Snackbar.LENGTH_SHORT).show();
                        });
                    }).addOnFailureListener(e -> {
                        Constants.dismissDialog();
                        Snackbar.make(this, binding.root, e.getMessage(), Snackbar.LENGTH_SHORT).show();
                    });
        }

    }

    private void uploadImage(String ID) {
        for (int i = 0; i < imagesToUpload.size(); i++) {
            int finalCount = i;
            Map<String, String> map = new HashMap<>();
            map.put("image", imagesToUpload.get(i));
            Constants.databaseReference().child(Constants.ProductImages)
                    .child(Constants.auth().getCurrentUser().getUid()).child(ID)
                    .push().setValue(map).addOnSuccessListener(unused -> {
                        if (finalCount == imagesToUpload.size() - 1) {
                            Constants.dismissDialog();
                            Snackbar.make(this, binding.root, "Product Updated", Snackbar.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> {
                        Constants.dismissDialog();
                        Snackbar.make(this, binding.root, e.getMessage(), Snackbar.LENGTH_SHORT).show();
                    });
        }
    }

    private void uploadProduct(String id, String link, ProductModel model) {
        ProductModel productModel = new ProductModel(
                id, Constants.auth().getCurrentUser().getUid(),
                binding.productName.getEditText().getText().toString(),
                binding.productDesc.getEditText().getText().toString(),
                binding.category.getEditText().getText().toString(),
                link, Double.parseDouble(binding.price.getEditText().getText().toString()),
                Long.parseLong(binding.stock.getEditText().getText().toString()), new Date().getTime(),
                model.getStar1(), model.getStar2(), model.getStar3(), model.getStar4(), model.getStar5(), model.getRatingCount()
        );

        Constants.databaseReference().child(Constants.Product).child(Constants.auth().getCurrentUser().getUid())
                .child(id).setValue(productModel)
                .addOnSuccessListener(unused -> {
                    Constants.dismissDialog();
                    Snackbar.make(this, binding.root, "Product Updated", Snackbar.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void showDate(ProductModel model) {
        Constants.databaseReference().child(Constants.ProductImages).child(model.getUserID())
                .child(model.getID()).get().addOnSuccessListener(dataSnapshot -> {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            images.add(snapshot.child("image").getValue(String.class));
                            imagesToUpload.add(snapshot.child("image").getValue(String.class));
                        }
                        Stash.put(Constants.IMAGE_LIST, imagesToUpload);
                        binding.addImage.setVisibility(View.GONE);
                        binding.AddPhotoLayoutRecycler.setVisibility(View.VISIBLE);

                        ImageAdapter adapter = new ImageAdapter(ProductUpdateActivity.this, images, binding.addImage, binding.AddPhotoLayoutRecycler);
                        binding.RecyclerViewImageList.setAdapter(adapter);

                        binding.productName.getEditText().setText(model.getName());
                        binding.price.getEditText().setText("" + model.getPrice());
                        binding.categoryAuto.setText(model.getCategory());
                        binding.stock.getEditText().setText("" + model.getStock());
                        binding.productDesc.getEditText().setText(model.getDescription());

                    }
                    Constants.dismissDialog();
                }).addOnFailureListener(e -> {
                    Constants.dismissDialog();
                    Snackbar.make(ProductUpdateActivity.this, binding.root, e.getMessage(), Snackbar.LENGTH_SHORT).show();
                });
    }

    private boolean valid() {
        if (imagesToUpload.size() == 0) {
            Toast.makeText(this, "Please Add at least 1 Image", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (binding.productName.getEditText().getText().toString().isEmpty()) {
            binding.productName.getEditText().setError("Name is Required*");
            return false;
        }
        if (binding.productDesc.getEditText().getText().toString().isEmpty()) {
            binding.productDesc.getEditText().setError("Description is Required*");
            return false;
        }
        if (binding.price.getEditText().getText().toString().isEmpty()) {
            binding.price.getEditText().setError("Price is Required*");
            return false;
        }
        if (binding.stock.getEditText().getText().toString().isEmpty()) {
            binding.stock.getEditText().setError("Stock Availability is Required*");
            return false;
        }
        if (binding.category.getEditText().getText().toString().isEmpty()) {
            binding.category.getEditText().setError("Category is Required*");
            return false;
        }
        return true;
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
                .compress(512)
                .maxResultSize(1080, 1080)
                .start(PICK_FROM_CAMERA);
    }

    private void getImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(Intent.createChooser(intent, ""), PICK_FROM_GALLERY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == PICK_FROM_GALLERY) {
                try {
                    if (resultCode == RESULT_OK && data != null && data.getClipData() != null) {
                        binding.addImage.setVisibility(View.GONE);
                        binding.AddPhotoLayoutRecycler.setVisibility(View.VISIBLE);
                        int currentImage = 0;

                        while (currentImage < data.getClipData().getItemCount()) {
                            if (currentImage < limit) {
                                images.add(data.getClipData().getItemAt(currentImage).getUri().toString());
                                imagesURi.add(data.getClipData().getItemAt(currentImage).getUri());
                                Log.d("SizeTest", "" + imagesURi.size());
                            }
                            currentImage++;
                        }

                        ImageAdapter adapter = new ImageAdapter(ProductUpdateActivity.this, images, binding.addImage, binding.AddPhotoLayoutRecycler);
                        binding.RecyclerViewImageList.setAdapter(adapter);

                    } else {
                        Toast.makeText(this, "Please Select Multiple Images", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            } else {
                if (resultCode == RESULT_OK && data != null && data.getData() != null) {
                    binding.addImage.setVisibility(View.GONE);
                    binding.AddPhotoLayoutRecycler.setVisibility(View.VISIBLE);

                    images.add(data.getData().toString());
                    imagesURi.add(data.getData());

                    ImageAdapter adapter = new ImageAdapter(ProductUpdateActivity.this, images, binding.addImage, binding.AddPhotoLayoutRecycler);
                    binding.RecyclerViewImageList.setAdapter(adapter);
                }
            }
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, SellerDashboardActivity.class));
        finish();
    }

}