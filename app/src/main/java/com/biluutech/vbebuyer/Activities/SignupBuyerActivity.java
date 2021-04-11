package com.biluutech.vbebuyer.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
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
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import static com.google.android.material.snackbar.Snackbar.*;

public class SignupBuyerActivity extends AppCompatActivity {

    private LinearLayout signUpLayout;
    private EditText signUpNameInputET, signUpEmailInputET, signUpPasswordInputET, signUpAddressInputET;
    private FirebaseAuth auth;
    private String name, email, password, address;
    private ProgressDialog progressDialog;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_buyer);

        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        signUpLayout = findViewById(R.id.signUpLayout);
        progressDialog = new ProgressDialog(this);
        signUpNameInputET = findViewById(R.id.signup_name_input_ET);
        signUpEmailInputET = findViewById(R.id.signup_email_input_ET);
        signUpPasswordInputET = findViewById(R.id.signup_password_input_ET);
        signUpAddressInputET = findViewById(R.id.signup_address_input_ET);

        name = signUpNameInputET.getText().toString().trim();
        email = signUpEmailInputET.getText().toString().trim();
        password = signUpPasswordInputET.getText().toString().trim();
        address = signUpAddressInputET.getText().toString().trim();


    }

    public void loginTxtClick(View view) {
        startActivity(new Intent(SignupBuyerActivity.this, LoginBuyerActivity.class));
        finish();
    }

    public void signUpBtnClick(View view) {
        CheckValidations();
    }

    private void CheckValidations() {
        if (!TextUtils.isEmpty(signUpNameInputET.getText().toString())) {
            if (!TextUtils.isEmpty(signUpAddressInputET.getText().toString())) {
                if (!TextUtils.isEmpty(signUpEmailInputET.getText().toString())) {
                    if (!TextUtils.isEmpty(signUpPasswordInputET.getText().toString())) {
                        CreateAccount();
                    } else {
                        signUpPasswordInputET.setError("Please fill this field");
                    }
                } else {
                    signUpEmailInputET.setError("Please fill this field");
                }
            } else {
                signUpAddressInputET.setError("Please fill this field");
            }
        } else {
            signUpNameInputET.setError("Please fill this field");
        }
    }

    private void CreateAccount() {

        progressDialog.setTitle("Create Account");
        progressDialog.setMessage("Please wait, We are creating your account");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        auth.createUserWithEmailAndPassword(signUpEmailInputET.getText().toString(), signUpPasswordInputET.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    StoreDataIntoDatabase();
                    progressDialog.dismiss();
                    startActivity(new Intent(SignupBuyerActivity.this, LoginBuyerActivity.class));
                    finish();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Snackbar snackbar = Snackbar.make(signUpLayout, e.getMessage(), LENGTH_LONG);
                snackbar.show();
            }
        });
    }

    private void StoreDataIntoDatabase() {

        HashMap<String, Object> dataMap = new HashMap<>();
        dataMap.put("name", signUpNameInputET.getText().toString().trim());
        dataMap.put("address", signUpAddressInputET.getText().toString().trim());
        dataMap.put("email", signUpEmailInputET.getText().toString().trim());
        dataMap.put("password", signUpPasswordInputET.getText().toString().trim());
        dataMap.put("bid", FirebaseAuth.getInstance().getCurrentUser().getUid());
        dataMap.put("status", "true");
        databaseReference.child("Buyers").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).updateChildren(dataMap);

    }
}