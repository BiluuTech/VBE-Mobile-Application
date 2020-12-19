package com.biluutech.vbebuyer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class SignupBuyerActivity extends AppCompatActivity {

    private TextView loginTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_buyer);

        loginTV = (TextView) findViewById(R.id.login_TV);

        loginTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(SignupBuyerActivity.this,LoginBuyerActivity.class));
                finish();

            }
        });

    }
}