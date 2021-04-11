package com.biluutech.vbebuyer.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.biluutech.vbebuyer.Activities.CartActivity;
import com.biluutech.vbebuyer.Activities.CategoryProductActivity;
import com.biluutech.vbebuyer.Activities.ProductDescriptionActivity;
import com.biluutech.vbebuyer.Adapters.CategoriesAdapter;
import com.biluutech.vbebuyer.Adapters.ProductsAdapter;
import com.biluutech.vbebuyer.Adapters.SliderAdapterExample;
import com.biluutech.vbebuyer.Models.CategoriesModel;
import com.biluutech.vbebuyer.Models.ProductsModel;
import com.biluutech.vbebuyer.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class HomeFragment extends Fragment {

    private SliderView sliderView;
    private SliderAdapterExample adapter;
    private List<Integer> mSliderItems;
    //Products RecyclerView
    private RecyclerView rvProducts;
    private ProductsAdapter productsAdapter;
    private List<ProductsModel> productsModelList;
    private DatabaseReference productsRef;
    //Category RecyclerView
    private RecyclerView rvCategories;
    private CategoriesAdapter categoriesAdapter;
    private List<CategoriesModel> categoriesModelList;
    private DatabaseReference categoriesRef;

    private FloatingActionButton fabHomeFragment;
    private SpeechRecognizer speechRecognizer;
    private Intent speechRecognizerIntent;
    private String keeper = "";

    private ArrayList<String> categoryNamesList, categoryIdList, productNamesList, productIdList;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        categoryNamesList = new ArrayList<>();
        categoryIdList = new ArrayList<>();
        productNamesList = new ArrayList<>();
        productIdList = new ArrayList<>();

        sliderView = view.findViewById(R.id.imageSlider);
        mSliderItems = new ArrayList<>();
        mSliderItems.add(R.drawable.a);
        mSliderItems.add(R.drawable.c);
        mSliderItems.add(R.drawable.b);
        mSliderItems.add(R.drawable.d);
        mSliderItems.add(R.drawable.e);
        mSliderItems.add(R.drawable.f);
        mSliderItems.add(R.drawable.g);
        mSliderItems.add(R.drawable.h);
        mSliderItems.add(R.drawable.i);
        adapter = new SliderAdapterExample(getActivity(), mSliderItems);
        sliderView.setAutoCycle(true);
        sliderView.setSliderAdapter(adapter);
        sliderView.setSliderTransformAnimation(SliderAnimations.SPINNERTRANSFORMATION);

        fabHomeFragment = view.findViewById(R.id.fabHomeFragment);

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(getContext());
        speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        rvProducts = view.findViewById(R.id.rv_products);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvProducts.setLayoutManager(linearLayoutManager);
        rvProducts.setHasFixedSize(true);

        rvCategories = view.findViewById(R.id.rv_categories);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getContext());
        linearLayoutManager2.setOrientation(RecyclerView.HORIZONTAL);
        rvCategories.setLayoutManager(linearLayoutManager2);
        rvCategories.setHasFixedSize(true);

        productsModelList = new ArrayList<>();
        categoriesModelList = new ArrayList<>();

        productsRef = FirebaseDatabase.getInstance().getReference().child("AdminProducts");
        categoriesRef = FirebaseDatabase.getInstance().getReference().child("Categories");

        productsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot p : snapshot.getChildren()) {
                        ProductsModel pm = p.getValue(ProductsModel.class);
                        productsModelList.add(pm);
                    }
                    productsAdapter = new ProductsAdapter(getContext(), productsModelList);
                    rvProducts.setAdapter(productsAdapter);
                    productsAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        categoriesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot c : snapshot.getChildren()) {
                        CategoriesModel cm = c.getValue(CategoriesModel.class);
                        categoriesModelList.add(cm);
                    }
                    categoriesAdapter = new CategoriesAdapter(getContext(), categoriesModelList);
                    rvCategories.setAdapter(categoriesAdapter);
                    categoriesAdapter.notifyDataSetChanged();
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

                    keeper = matchesFound.get(0).toLowerCase();

//                    Toast.makeText(MediaPlayerActivity.this, "Result =" + keeper, Toast.LENGTH_SHORT).show();

                    if (keeper.contains("category")) {

                        categoriesRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    int i = 0;
                                    for (DataSnapshot ds : snapshot.getChildren()) {
                                        String categoryName = ds.child("categoryName").getValue(String.class).toLowerCase();
                                        String cid = ds.child("cid").getValue(String.class).toLowerCase();
                                        categoryNamesList.add(i, categoryName);
                                        categoryIdList.add(i, cid);
                                        i++;
                                    }
                                    for (i = 0; i <= categoryNamesList.size() - 1; i++) {
                                        if (keeper.contains(categoryNamesList.get(i).toLowerCase().toString())) {
//                                            Toast.makeText(getContext(), ""+categoryIdList.get(i), Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(getContext(), CategoryProductActivity.class);
                                            intent.putExtra("cid", categoryIdList.get(i));
                                            startActivity(intent);
                                            break;
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    } else if (keeper.contains("product")) {
                        productsRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    int i = 0;
                                    for (DataSnapshot ds : snapshot.getChildren()) {
                                        String productName = ds.child("productName").getValue(String.class).toLowerCase();
                                        String pid = ds.child("pid").getValue(String.class);
                                        productNamesList.add(i, productName);
                                        productIdList.add(i, pid);
                                        i++;
                                    }
                                    for (i = 0; i <= productNamesList.size() - 1; i++) {
                                        if (keeper.contains(productNamesList.get(i).toLowerCase().toString())) {
//                                            Toast.makeText(getContext(), ""+categoryIdList.get(i), Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(getContext(), ProductDescriptionActivity.class);
                                            intent.putExtra("pid", productIdList.get(i));
                                            startActivity(intent);
                                            break;
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    } else if (keeper.contains("cart") || keeper.contains("card")) {
                        startActivity(new Intent(getContext(), CartActivity.class));
                        getActivity().finish();
                    } else {
                        Toast.makeText(getActivity(), "Sorry no result found!", Toast.LENGTH_SHORT).show();
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

        fabHomeFragment.setOnTouchListener(new View.OnTouchListener() {
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

        return view;
    }
}