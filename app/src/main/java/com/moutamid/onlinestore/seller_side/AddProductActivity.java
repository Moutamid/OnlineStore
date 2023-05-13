package com.moutamid.onlinestore.seller_side;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.snackbar.Snackbar;
import com.moutamid.onlinestore.R;
import com.moutamid.onlinestore.adapter.CarImageAdapter;
import com.moutamid.onlinestore.constants.Constants;
import com.moutamid.onlinestore.databinding.ActivityAddProductBinding;
import com.moutamid.onlinestore.models.ProductModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class AddProductActivity extends AppCompatActivity {
    ActivityAddProductBinding binding;

    private static final int PICK_FROM_GALLERY = 1;
    private static final int PICK_FROM_CAMERA = 2;
    private int limit = 6;
    ArrayList<Uri> carImagesList;

    ArrayAdapter<CharSequence> citiesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.header.title.setText("Add Product for listing");

        Constants.initDialog(this);

        carImagesList = new ArrayList<>();

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
            if (carImagesList.size() >= 6){
                Toast.makeText(this, "You can only add 6 images", Toast.LENGTH_SHORT).show();
            } else {
                showDialog();
            }
        });

        binding.btnAddCarPhoto.setOnClickListener(v -> {
            if (carImagesList.size() >= 6){
                Toast.makeText(this, "You can only add 6 images", Toast.LENGTH_SHORT).show();
            } else {
                showDialog();
            }
        });

        binding.create.setOnClickListener(v -> {
            if (valid()) {
                Constants.showDialog();
                uploadData();
            }
        });

    }

    private void uploadData() {
        String ID = UUID.randomUUID().toString();
        String currentDate = new SimpleDateFormat("ddMMyyyyhhmmss", Locale.getDefault()).format(new Date());
        for (int count =0; count<carImagesList.size(); count++){
            int finalCount = count;
            Constants.storageReference(Constants.auth().getCurrentUser().getUid())
                        .child(currentDate+"_image"+count)
                        .putFile(carImagesList.get(count))
                        .addOnSuccessListener(taskSnapshot -> {
                            taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(uri -> {
                                Map<String, String> map = new HashMap<>();
                                map.put("image", uri.toString());
                                if (finalCount == 0){
                                    uploadProduct(ID, uri.toString());
                                }
                                Constants.databaseReference().child(Constants.ProductImages)
                                        .child(Constants.auth().getCurrentUser().getUid()).child(ID)
                                        .push().setValue(map).addOnSuccessListener(unused -> {
                                            if (finalCount == carImagesList.size()-1) {
                                                Constants.dismissDialog();
                                                Snackbar.make(this, binding.root, "Product Added", Snackbar.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnFailureListener(e -> {
                                            Constants.dismissDialog();
                                            Snackbar.make(this, binding.root, e.getMessage(), Snackbar.LENGTH_SHORT).show();
                                        });
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

    private void uploadProduct(String id, String link) {
        ProductModel productModel = new ProductModel(
                id, Constants.auth().getCurrentUser().getUid(),
                binding.productName.getEditText().getText().toString(),
                binding.productDesc.getEditText().getText().toString(),
                binding.category.getEditText().getText().toString(),
                link, Double.parseDouble(binding.price.getEditText().getText().toString()),
                Long.parseLong(binding.stock.getEditText().getText().toString()), new Date().getTime()
        );

        Constants.databaseReference().child(Constants.Product).child(Constants.auth().getCurrentUser().getUid())
                .child(id).setValue(productModel)
                .addOnSuccessListener(unused -> {})
                .addOnFailureListener(e -> Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show());

    }

    private boolean valid() {
        if (carImagesList.size() == 0){
            Toast.makeText(this, "Please Add at least 1 Image", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (binding.productName.getEditText().getText().toString().isEmpty()){
            binding.productName.getEditText().setError("Name is Required*");
            return false;
        }
        if (binding.productDesc.getEditText().getText().toString().isEmpty()){
            binding.productDesc.getEditText().setError("Description is Required*");
            return false;
        }
        if (binding.price.getEditText().getText().toString().isEmpty()){
            binding.price.getEditText().setError("Price is Required*");
            return false;
        }
        if (binding.stock.getEditText().getText().toString().isEmpty()){
            binding.stock.getEditText().setError("Stock Availability is Required*");
            return false;
        }
        if (binding.category.getEditText().getText().toString().isEmpty()){
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
                try{
                    if (resultCode == RESULT_OK && data != null && data.getClipData() != null) {
                        binding.addImage.setVisibility(View.GONE);
                        binding.AddPhotoLayoutRecycler.setVisibility(View.VISIBLE);
                        int currentImage = 0;

                        while (currentImage < data.getClipData().getItemCount()) {
                            if (currentImage < limit){
                                carImagesList.add(data.getClipData().getItemAt(currentImage).getUri());
                            }
                            currentImage++;
                        }

                        CarImageAdapter adapter = new CarImageAdapter(AddProductActivity.this, carImagesList, binding.addImage, binding.AddPhotoLayoutRecycler);
                        binding.RecyclerViewImageList.setAdapter(adapter);

                    } else {
                        Toast.makeText(this, "Please Select Multiple Images", Toast.LENGTH_SHORT).show();
                    }
                }  catch (Exception e){
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            } else {
                if (resultCode == RESULT_OK && data != null && data.getData() != null) {
                    binding.addImage.setVisibility(View.GONE);
                    binding.AddPhotoLayoutRecycler.setVisibility(View.VISIBLE);

                    //CarImagesModel model = new CarImagesModel(data.getData());
                    carImagesList.add(data.getData());

                    CarImageAdapter adapter = new CarImageAdapter(AddProductActivity.this, carImagesList, binding.addImage, binding.AddPhotoLayoutRecycler);
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