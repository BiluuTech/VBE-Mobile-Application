package com.designer.vbeseller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class SignupSellerActivity extends AppCompatActivity {

    private TextView loginTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_seller);

        loginTV = (TextView) findViewById(R.id.login_TV);

        loginTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(SignupSellerActivity.this,LoginSellerActivity.class));
                finish();

            }
        });

    }
}