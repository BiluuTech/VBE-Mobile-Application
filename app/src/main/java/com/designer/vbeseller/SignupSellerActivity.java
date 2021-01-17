package com.designer.vbeseller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import static com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_LONG;

public class SignupSellerActivity extends AppCompatActivity {

    private EditText signUpEmailInputET, signUpNameInputET, signUpPasswordInputET;
    private String name, email, password;
    private FirebaseAuth auth;
    private LinearLayout signUpLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_seller);

        auth = FirebaseAuth.getInstance();
        signUpLayout = findViewById(R.id.signUpLayout);
        //loginTV = (TextView) findViewById(R.id.login_TV);
        signUpNameInputET = findViewById(R.id.signup_name_input_ET);
        signUpEmailInputET = findViewById(R.id.signup_email_input_ET);
        signUpPasswordInputET = findViewById(R.id.signup_password_input_ET);

        name = signUpNameInputET.getText().toString();
        email = signUpEmailInputET.getText().toString();
        password = signUpPasswordInputET.getText().toString();
    }

    public void loginTxtClick(View view) {
        startActivity(new Intent(SignupSellerActivity.this, LoginSellerActivity.class));
        finish();
    }

    public void signUpBtnClick(View view) {

        CheckValidations();
    }

    private void CheckValidations() {
        if (!TextUtils.isEmpty(signUpNameInputET.getText().toString())) {
            if (!TextUtils.isEmpty(signUpEmailInputET.getText().toString())) {
                if (!TextUtils.isEmpty(signUpPasswordInputET.getText().toString()) && signUpPasswordInputET.getText().toString().length() >= 6) {
                    CreateAccount();
                } else {
                    signUpPasswordInputET.setError("Please fill this field at least 6 character");
                }
            } else {
                signUpEmailInputET.setError("Please fill this field");
            }
        } else {
            signUpNameInputET.setError("Please fill this field");
        }
    }

    private void CreateAccount() {
        auth.createUserWithEmailAndPassword(signUpEmailInputET.getText().toString(), signUpPasswordInputET.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            startActivity(new Intent(SignupSellerActivity.this, LoginSellerActivity.class));
                            finish();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Snackbar snackbar = Snackbar.make(signUpLayout, e.getMessage(), LENGTH_LONG);
                snackbar.show();
            }
        });
    }
}