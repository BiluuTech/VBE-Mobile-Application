package com.biluutech.vbebuyer.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.biluutech.vbebuyer.Adapters.CartAdapter;
import com.biluutech.vbebuyer.Adapters.ProductsAdapter;
import com.biluutech.vbebuyer.Models.ProductsModel;
import com.biluutech.vbebuyer.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CartActivity extends AppCompatActivity {

    private RecyclerView rvCartProduct;
    private CartAdapter cartAdapter;
    private List<ProductsModel> productsModelList;
    private DatabaseReference cartProductsRef;

    private RelativeLayout rlBottom;
    private CardView cvEmpty;

    private FloatingActionButton fabCart;
    private SpeechRecognizer speechRecognizer;
    private Intent speechRecognizerIntent;
    private String keeper = "";

    private ArrayList<String> productNamesList, productIdList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        productNamesList = new ArrayList<>();
        productIdList = new ArrayList<>();

        rlBottom = findViewById(R.id.rlBottom);
        cvEmpty = findViewById(R.id.cvEmpty);

        fabCart = findViewById(R.id.fabCart);

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        rvCartProduct = findViewById(R.id.rvCartProduct);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvCartProduct.setLayoutManager(linearLayoutManager);
        rvCartProduct.setHasFixedSize(true);

        productsModelList = new ArrayList<>();
        cartProductsRef = FirebaseDatabase.getInstance().getReference().child("Cart").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        cartProductsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                productsModelList.clear();
                if (snapshot.exists()){

                    rlBottom.setVisibility(View.VISIBLE);
                    cvEmpty.setVisibility(View.GONE);

                    for (DataSnapshot p: snapshot.getChildren()){
                        ProductsModel pm = p.getValue(ProductsModel.class);
                        productsModelList.add(pm);
                    }
                    cartAdapter = new CartAdapter(CartActivity.this,productsModelList);
                    rvCartProduct.setAdapter(cartAdapter);
                    cartAdapter.notifyDataSetChanged();
                }
                else{
                    rlBottom.setVisibility(View.GONE);
                    cvEmpty.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) {

            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float rmsdB) {

            }

            @Override
            public void onBufferReceived(byte[] buffer) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int error) {

            }

            @Override
            public void onResults(Bundle bundle) {

                ArrayList<String> matchesFound = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

                if (matchesFound != null) {

                    keeper = matchesFound.get(0);

                    if (keeper.contains("remove") || keeper.contains("delete") || keeper.contains("cross")){
                        cartProductsRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                productsModelList.clear();
                                if (snapshot.exists()){

                                    int i = 0;
                                    for (DataSnapshot ds : snapshot.getChildren()) {
                                        String productName = ds.child("productName").getValue(String.class).toLowerCase();
                                        String pid = ds.child("pid").getValue(String.class);
                                        productNamesList.add(i, productName);
                                        productIdList.add(i, pid);
                                        i++;
                                    }
                                    for (i = 0; i <= productNamesList.size() - 1; i++) {
                                        if (keeper.contains(productNamesList.get(i).toLowerCase())) {
                                            cartProductsRef.child(productIdList.get(i)).removeValue();
                                            break;
                                        }
                                    }

                                }
                                else{
                                    rlBottom.setVisibility(View.GONE);
                                    cvEmpty.setVisibility(View.VISIBLE);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                    else if (keeper.contains("back") || keeper.contains("return")) {
                        startActivity(new Intent(CartActivity.this, HomeActivity.class));
                        finish();
                    }else if (keeper.contains("checkout") || keeper.contains("check")){
                        startActivity(new Intent(CartActivity.this,CheckoutActivity.class));
                        finish();
                    }
                    else {
                        Toast.makeText(CartActivity.this, "Sorry no result found!", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onPartialResults(Bundle partialResults) {

            }

            @Override
            public void onEvent(int eventType, Bundle params) {

            }
        });

        fabCart.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent motionEvent) {

                switch (motionEvent.getAction()) {

                    case MotionEvent.ACTION_DOWN:
                        speechRecognizer.startListening(speechRecognizerIntent);
                        keeper = "";
                        break;
                    case MotionEvent.ACTION_UP:
                        speechRecognizer.stopListening();
                        break;

                }
                return false;
            }
        });

    }

    public void ibBackArrowCart(View view) {
        startActivity(new Intent(CartActivity.this,HomeActivity.class));
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(CartActivity.this,HomeActivity.class));
        finish();
    }

    public void btnCheckoutClick(View view) {
        startActivity(new Intent(CartActivity.this,CheckoutActivity.class));
        finish();
    }
}