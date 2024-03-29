package com.biluutech.vbebuyer.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.biluutech.vbebuyer.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginBuyerActivity extends AppCompatActivity {

    private EditText loginEmailInputET, loginPasswordInputET;
    private String email, password;
    private FirebaseAuth auth;
    private LinearLayout buyerLoginLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_buyer);
        auth = FirebaseAuth.getInstance();
        buyerLoginLayout = findViewById(R.id.buyerLoginLayout);
        loginEmailInputET = findViewById(R.id.login_email_input_ET);
        loginPasswordInputET = findViewById(R.id.login_password_input_ET);

        email = loginEmailInputET.getText().toString();
        password = loginPasswordInputET.getText().toString();

    }

    public void signUpTxtClick(View view) {
        startActivity(new Intent(LoginBuyerActivity.this, SignupBuyerActivity.class));
        finish();
    }

    public void loginBtnClick(View view) {
        CheckValidations();
    }

    private void CheckValidations() {
        if (!TextUtils.isEmpty(loginEmailInputET.getText().toString())) {
            if (!TextUtils.isEmpty(loginPasswordInputET.getText().toString().trim()) && loginPasswordInputET.getText().toString().trim().length() >= 6) {
                LoginAccount();
            } else {
                loginPasswordInputET.setError("Please fill this field at least 6 character");
            }
        } else {
            loginEmailInputET.setError("Please fill this field");
        }
    }

    private void LoginAccount() {
        auth.signInWithEmailAndPassword(loginEmailInputET.getText().toString(), loginPasswordInputET.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    startActivity(new Intent(LoginBuyerActivity.this,HomeActivity.class));
                    finish();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Snackbar snackbar = Snackbar.make(buyerLoginLayout, e.getMessage(), BaseTransientBottomBar.LENGTH_LONG);
                snackbar.show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (FirebaseAuth.getInstance().getCurrentUser() != null){
            startActivity(new Intent(LoginBuyerActivity.this,HomeActivity.class));
            finish();
        }
    }
}