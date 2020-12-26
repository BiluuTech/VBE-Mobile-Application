package com.alimedia.vbeadmin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashAdminActivity extends AppCompatActivity {

    private TextView welcomeTV, storeTV;
    private ImageView logoIV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_admin);

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.bounce);
        Animation animation1 = AnimationUtils.loadAnimation(this, R.anim.text);

        logoIV = findViewById(R.id.logo_IV);
        storeTV = findViewById(R.id.store_TV);
        welcomeTV = findViewById(R.id.welcome_TV);

        logoIV.startAnimation(animation);
        storeTV.startAnimation(animation1);
        welcomeTV.startAnimation(animation1);

        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashAdminActivity.this, LoginAdminActivity.class);
                startActivity(intent);
                finish();

            }
        }, 5000);



    }
}