package com.biluutech.vbebuyer.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.biluutech.vbebuyer.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class ProductDescriptionActivity extends AppCompatActivity {

    private ImageView ivProductDescImage;
    private TextView tvProductDescName, tvProductDescPrice, tvProductDescText;

    private DatabaseReference productDescRef, cartRef;

    private String productName, productPrice, productDesc, pid, cid, sid, imageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_description);

        init();

        productDescRef = FirebaseDatabase.getInstance().getReference().child("AdminProducts").child("pid");


    }

    private void init() {

        ivProductDescImage = findViewById(R.id.ivProductDescImage);
        tvProductDescName = findViewById(R.id.tvProductDescName);
        tvProductDescPrice = findViewById(R.id.tvProductDescPrice);
        tvProductDescText = findViewById(R.id.tvProductDescText);

        pid = getIntent().getStringExtra("pid");

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("AdminProducts").child(pid);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                productName = snapshot.child("productName").getValue().toString();
                productPrice = snapshot.child("productPrice").getValue().toString();
                productDesc = snapshot.child("productDescription").getValue().toString();
                cid = snapshot.child("cid").getValue().toString();
                sid = snapshot.child("sid").getValue().toString();
                imageUrl = snapshot.child("imageUrl").getValue().toString();



                tvProductDescName.setText(productName);
                tvProductDescPrice.setText("Rs "+productPrice+ " /-");
                tvProductDescText.setText(productDesc);
                Glide.with(getBaseContext())
                        .load(imageUrl)
                        .transition(DrawableTransitionOptions.withCrossFade(500))
                        .into(ivProductDescImage);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    public void ibBackArrowProductDescription(View view) {
        finish();
    }

    public void btnAddToCartClick(View view) {

        cartRef = FirebaseDatabase.getInstance().getReference().child("Cart").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(pid);

        HashMap<String, Object> cartMap = new HashMap<>();
        cartMap.put("uid", FirebaseAuth.getInstance().getCurrentUser().getUid());
        cartMap.put("productName", productName);
        cartMap.put("productPrice", productPrice);
        cartMap.put("pid", pid);
        cartMap.put("imageUrl", imageUrl);

        cartRef.updateChildren(cartMap);

        startActivity(new Intent(ProductDescriptionActivity.this, HomeActivity.class));

    }
}