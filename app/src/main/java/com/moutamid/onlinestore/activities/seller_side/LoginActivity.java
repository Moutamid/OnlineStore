package com.moutamid.onlinestore.activities.seller_side;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;
import com.moutamid.onlinestore.ForgotPasswordActivity;
import com.moutamid.onlinestore.MainActivity;
import com.moutamid.onlinestore.constants.Constants;
import com.moutamid.onlinestore.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {
    ActivityLoginBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Constants.initDialog(this);

        binding.header.title.setText("Login to your account");

        binding.header.back.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });

        binding.forgot.setOnClickListener(v -> {
            startActivity(new Intent(this, ForgotPasswordActivity.class));
            finish();
        });

        binding.create.setOnClickListener(v -> {
            startActivity(new Intent(this, SignupActivity.class));
            finish();
        });

        binding.login.setOnClickListener(v -> {
            if (valid()) {
                Constants.showDialog();
                Constants.auth().signInWithEmailAndPassword(
                        binding.email.getEditText().getText().toString().trim(),
                        binding.password.getEditText().getText().toString()
                ).addOnSuccessListener(authResult -> {
                    Constants.dismissDialog();
                    startActivity(new Intent(LoginActivity.this, SellerDashboardActivity.class));
                    finish();
                }).addOnFailureListener(e -> {
                    Constants.dismissDialog();
                    Snackbar.make(this, binding.root, e.getMessage(), Snackbar.LENGTH_SHORT).show();
                });
            }
        });

    }

    private boolean valid() {
        if (binding.email.getEditText().getText().toString().isEmpty()){
            binding.email.getEditText().setError("Email is Required*");
            return false;
        }
        if (binding.password.getEditText().getText().toString().isEmpty()){
            binding.password.getEditText().setError("Password is Required*");
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}