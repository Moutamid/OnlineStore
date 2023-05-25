package com.moutamid.onlinestore;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.moutamid.onlinestore.activities.buyer_side.BuyerMainActivity;
import com.moutamid.onlinestore.constants.Constants;
import com.moutamid.onlinestore.models.UserModel;
import com.moutamid.onlinestore.activities.seller_side.SellerDashboardActivity;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        if (Constants.auth().getCurrentUser() != null) {
             checkUser();
        } else {
            new Handler().postDelayed(() -> {
                startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
                finish();
            }, 2000);
        }


    }

    private void checkUser() {
        Constants.databaseReference().child(Constants.User).child(Constants.auth().getCurrentUser().getUid())
                .get().addOnSuccessListener(dataSnapshot -> {
                    if (dataSnapshot.exists()) {
                        UserModel userModel = dataSnapshot.getValue(UserModel.class);
                        if (userModel.isSeller()) {
                            startActivity(new Intent(SplashScreenActivity.this, SellerDashboardActivity.class));
                            finish();
                        } else {
                            startActivity(new Intent(SplashScreenActivity.this, BuyerMainActivity.class));
                            finish();
                        }
                    } else {
                        Toast.makeText(this, "Data Not Exist", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(e -> {
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}