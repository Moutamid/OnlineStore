package com.moutamid.onlinestore;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.moutamid.onlinestore.constants.Constants;
import com.moutamid.onlinestore.databinding.ActivityForgotPasswordBinding;

public class ForgotPasswordActivity extends AppCompatActivity {
    ActivityForgotPasswordBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityForgotPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Constants.initDialog(this);

        binding.back.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });

        binding.recover.setOnClickListener(v -> {
            if (binding.email.getEditText().getText().toString().isEmpty()){
                binding.email.getEditText().setError("Email is Required*");
            } else {
                Constants.showDialog();
                Constants.auth().sendPasswordResetEmail(binding.email.getEditText().getText().toString())
                        .addOnSuccessListener(unused -> {
                            Constants.dismissDialog();
                            Snackbar.make(this, binding.root, "Check Your Email", Snackbar.LENGTH_INDEFINITE)
                                    .setAction("Open Mail", c -> {
                                        try {
                                            Intent intent = new Intent(Intent.ACTION_MAIN);
                                            intent.addCategory(Intent.CATEGORY_APP_EMAIL);
                                            this.startActivity(intent);
                                        } catch (android.content.ActivityNotFoundException e) {
                                            Toast.makeText(ForgotPasswordActivity.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
                                        }
                                    }).show();
                        }).addOnFailureListener(e -> {
                            Constants.dismissDialog();
                            Snackbar.make(this, binding.root, "Something went wrong", Snackbar.LENGTH_SHORT).show();
                        });
            }
        });

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

}