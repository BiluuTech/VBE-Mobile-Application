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
import android.widget.TextView;
import android.widget.Toast;

import com.biluutech.vbebuyer.Adapters.ProductsAdapter;
import com.biluutech.vbebuyer.Models.ProductsModel;
import com.biluutech.vbebuyer.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CategoryProductActivity extends AppCompatActivity {

    private RecyclerView rvCategoryProduct;
    private ProductsAdapter productsAdapter;
    private List<ProductsModel> productsModelList;
    private DatabaseReference productsRef;
    private String cid;

    private CardView cvEmptyCategory;
    private TextView tvProductHeading;

    private FloatingActionButton fabCategoryActivity;
    private SpeechRecognizer speechRecognizer;
    private Intent speechRecognizerIntent;
    private String keeper = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_product);

        cvEmptyCategory = findViewById(R.id.cvEmptyCategory);
        tvProductHeading = findViewById(R.id.tvProductHeading);

        cid = getIntent().getStringExtra("cid");

        fabCategoryActivity = findViewById(R.id.fabCategoryActivity);

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        rvCategoryProduct = findViewById(R.id.rvCategoryProduct);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvCategoryProduct.setLayoutManager(linearLayoutManager);
        rvCategoryProduct.setHasFixedSize(true);

        productsModelList = new ArrayList<>();

        productsRef = FirebaseDatabase.getInstance().getReference().child("AdminProducts");

        productsRef.orderByChild("cid").equalTo(cid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){

                    cvEmptyCategory.setVisibility(View.GONE);
                    tvProductHeading.setVisibility(View.VISIBLE);

                    for (DataSnapshot p: snapshot.getChildren()){
                        ProductsModel pm = p.getValue(ProductsModel.class);
                        productsModelList.add(pm);
                    }
                    productsAdapter = new ProductsAdapter(CategoryProductActivity.this,productsModelList);
                    rvCategoryProduct.setAdapter(productsAdapter);
                    productsAdapter.notifyDataSetChanged();
                }
                else{
                    cvEmptyCategory.setVisibility(View.VISIBLE);
                    tvProductHeading.setVisibility(View.GONE);
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

                    if (keeper.contains("back") || keeper.contains("return")) {
                        startActivity(new Intent(CategoryProductActivity.this, HomeActivity.class));
                        finish();
                    }
                    else {
                        Toast.makeText(CategoryProductActivity.this, "Sorry no result found!", Toast.LENGTH_SHORT).show();
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

        fabCategoryActivity.setOnTouchListener(new View.OnTouchListener() {
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

    public void ibBackArrowCategoryProduct(View view) {
        finish();
    }
}