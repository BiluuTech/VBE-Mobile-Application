package com.designer.vbeseller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import static com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_LONG;

public class SignupSellerActivity extends AppCompatActivity {

    private EditText signUpEmailInputET, signUpNameInputET, signUpPasswordInputET;
    private String name, email, password;
    private FirebaseAuth auth;
    private LinearLayout signUpLayout;
    private DatabaseReference databaseReference;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_seller);

        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Sellers");
        progressDialog = new ProgressDialog(this);
        signUpLayout = findViewById(R.id.signUpLayout);
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

        progressDialog.setTitle("Create Account");
        progressDialog.setMessage("Please wait, We are creating your account");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        auth.createUserWithEmailAndPassword(signUpEmailInputET.getText().toString(),signUpPasswordInputET.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    StoreSellerIntoDatabase();
                    progressDialog.dismiss();
                    startActivity(new Intent(SignupSellerActivity.this,LoginSellerActivity.class));
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
    private void StoreSellerIntoDatabase() {

        HashMap<String, Object> dataMap = new HashMap<>();
        dataMap.put("name", signUpNameInputET.getText().toString());
        dataMap.put("email", signUpEmailInputET.getText().toString());
        dataMap.put("password", signUpPasswordInputET.getText().toString());
        databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).updateChildren(dataMap);

    }
}