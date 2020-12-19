package com.biluutech.vbebuyer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class LoginBuyerActivity extends AppCompatActivity {

    private TextView signupTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_buyer);

        signupTV = (TextView) findViewById(R.id.signup_TV);

        signupTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(LoginBuyerActivity.this,SignupBuyerActivity.class));
                finish();

            }
        });

    }
}