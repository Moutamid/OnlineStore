package com.moutamid.onlinestore.activities.seller_side;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;
import com.moutamid.onlinestore.constants.Constants;
import com.moutamid.onlinestore.databinding.ActivitySignupBinding;
import com.moutamid.onlinestore.models.UserModel;

public class SignupActivity extends AppCompatActivity {
    ActivitySignupBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Constants.initDialog(this);

        binding.login.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });

        binding.header.back.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });

        binding.create.setOnClickListener(v -> {
            if (valid()) {
                Constants.showDialog();
                Constants.auth().createUserWithEmailAndPassword(
                        binding.email.getEditText().getText().toString().trim(),
                        binding.password.getEditText().getText().toString()
                ).addOnSuccessListener(authResult -> {
                    String ID = Constants.auth().getCurrentUser().getUid();
                    UserModel userModel = new UserModel(
                            ID, binding.username.getEditText().getText().toString(),
                            binding.email.getEditText().getText().toString(),
                            binding.password.getEditText().getText().toString(),
                            (binding.ccp.getSelectedCountryCode() + binding.phone.getEditText().getText().toString()),
                            binding.address.getEditText().getText().toString(), "", true
                    );

                    Constants.databaseReference().child(Constants.User).child(ID).setValue(userModel)
                            .addOnSuccessListener(unused -> {
                                Constants.dismissDialog();
                                Snackbar.make(this, binding.root, "User created Successfully", Snackbar.LENGTH_INDEFINITE).setAction(
                                        "Login Now", v1 -> {
                                            startActivity(new Intent(this, LoginActivity.class));
                                            finish();
                                        }
                                ).show();
                            }).addOnFailureListener(e -> {
                                Constants.dismissDialog();
                                Snackbar.make(this, binding.root, "Something went wrong", Snackbar.LENGTH_SHORT).show();
                            });

                }).addOnFailureListener(e -> {
                    Constants.dismissDialog();
                    Snackbar.make(this, binding.root, "Something went wrong", Snackbar.LENGTH_SHORT).show();
                });
            }
        });

    }

    private boolean valid() {
        if (binding.username.getEditText().getText().toString().isEmpty()){
            binding.username.getEditText().setError("Name is Required*");
            return false;
        }
        if (binding.phone.getEditText().getText().toString().isEmpty()){
            binding.phone.getEditText().setError("Phone Number is Required*");
            return false;
        }
        if (binding.email.getEditText().getText().toString().isEmpty()){
            binding.email.getEditText().setError("Email is Required*");
            return false;
        }
        if (binding.address.getEditText().getText().toString().isEmpty()){
            binding.address.getEditText().setError("Address is Required*");
            return false;
        }
        if (binding.password.getEditText().getText().toString().isEmpty()){
            binding.password.getEditText().setError("Password is Required*");
            return false;
        }
        if (binding.rePassword.getEditText().getText().toString().isEmpty()){
            binding.rePassword.getEditText().setError("Password is Required*");
            return false;
        }
        if (!binding.rePassword.getEditText().getText().toString().equals(binding.password.getEditText().getText().toString())){
            binding.rePassword.getEditText().setError("Password is not match");
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}