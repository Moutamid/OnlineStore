package com.moutamid.onlinestore;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.moutamid.onlinestore.constants.Constants;
import com.moutamid.onlinestore.seller_side.LoginActivity;

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
        Toast.makeText(this, "Login", Toast.LENGTH_SHORT).show();
    }
}