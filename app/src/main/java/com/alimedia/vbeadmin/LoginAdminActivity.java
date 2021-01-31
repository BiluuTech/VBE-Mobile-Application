package com.alimedia.vbeadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginAdminActivity extends AppCompatActivity {

    private EditText loginEmailInputET,loginPasswordInputET;
    private String email, password;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_admin);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Admin");
        loginEmailInputET = findViewById(R.id.login_email_input_ET);
        loginPasswordInputET = findViewById(R.id.login_password_input_ET);



    }

    public void adminloginBtnClick(View view) {
        FetchData();
    }

    private void FetchData() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    email = snapshot.child("email").getValue().toString();
                    password = snapshot.child("password").getValue().toString();
                    CheckAuthentication();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void CheckAuthentication() {

        if (loginEmailInputET.getText().toString().equalsIgnoreCase(email)){
            if (loginPasswordInputET.getText().toString().equalsIgnoreCase(password)){
                Toast.makeText(this, "Admin Logged in", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(this, "Invalid Password", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(this, "Invalid Email", Toast.LENGTH_SHORT).show();
        }

    }


}