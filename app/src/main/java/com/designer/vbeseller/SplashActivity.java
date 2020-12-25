package com.designer.vbeseller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashActivity extends AppCompatActivity {

    private ImageView logoIV;
    private TextView storeTV,welcomeTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

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
                Intent intent = new Intent(SplashActivity.this, LoginSellerActivity.class);
                startActivity(intent);
                finish();

            }
        }, 5000);


    }
}