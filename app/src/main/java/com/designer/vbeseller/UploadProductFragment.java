package com.designer.vbeseller;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

public class UploadProductFragment extends Fragment {

    private static final int GalleryPick = 1001;

    private ImageButton addProductImageBtn;

    private EditText etProductName;
    private EditText etProductPrice;
    private EditText etProductDescription;

    private ImageView productImage;

    private Uri imageUri;
    private String myUrl = "";
    private StorageReference productsImageRef;

    private ProgressDialog loadingBar;

    private FirebaseAuth auth;
    private DatabaseReference databaseReference;

    private Button btnUpload;
    private String saveCurrentDate, saveCurrentTime, RandomKey, downloadImageUrl;

    private Calendar calendar;
    private SimpleDateFormat currentDate;
    private SimpleDateFormat currentTime;


    public UploadProductFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_upload_product, container, false);

        addProductImageBtn = view.findViewById(R.id.add_product_Image_Btn);
        etProductPrice = view.findViewById(R.id.et_product_price);
        etProductDescription = view.findViewById(R.id.et_product_description);
        etProductName = view.findViewById(R.id.et_product_name);
        productImage = view.findViewById(R.id.product_image);
        btnUpload = view.findViewById(R.id.btn_upload_product);
        productsImageRef = FirebaseStorage.getInstance().getReference().child("Product Pictures");

        loadingBar = new ProgressDialog(getContext());

        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        calendar = Calendar.getInstance();

        currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        RandomKey = saveCurrentDate + saveCurrentTime;

        addProductImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenGallery();
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ValidateProfileData();
            }
        });

        return view;
    }

    private void OpenGallery() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GalleryPick);
    }

    private void ValidateProfileData() {

        if (imageUri == null) {
            Toast.makeText(getContext(), "Product image is mandatory...", Toast.LENGTH_SHORT).show();
        } else if (!TextUtils.isEmpty(etProductName.getText().toString())) {
            if (!TextUtils.isEmpty(etProductPrice.getText().toString())) {

                if (!TextUtils.isEmpty(etProductDescription.getText().toString())) {

                    StoreProductInformation();

                } else {
                    etProductDescription.setError("Enter Product Description");
                }
            } else {
                etProductPrice.setError("Enter Product Price");
            }
        } else {
            etProductName.setError("Enter UserName");
        }
    }

    private void StoreProductInformation() {
        loadingBar.setTitle("Add New Product");
        loadingBar.setMessage("Dear Seller, please wait while we are uploading your profile.");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();


        final StorageReference filePath = productsImageRef.child(imageUri.getLastPathSegment() + RandomKey + ".jpg");

        final UploadTask uploadTask = filePath.putFile(imageUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String message = e.toString();
                Toast.makeText(getContext(), "Error: " + message, Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(getContext(), "Product Image uploaded Successfully...", Toast.LENGTH_SHORT).show();

                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }

                        downloadImageUrl = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            downloadImageUrl = task.getResult().toString();

                            Toast.makeText(getContext(), "got the Product image Url Successfully...", Toast.LENGTH_SHORT).show();

                            SaveProductInfoToDatabase();
                        }
                    }
                });
            }
        });
    }

    private void SaveProductInfoToDatabase() {
        HashMap<String, Object> dataMap = new HashMap<>();
        dataMap.put("product name", etProductName.getText().toString());
        dataMap.put("product price", etProductPrice.getText().toString());
        dataMap.put("product description", etProductDescription.getText().toString());
        dataMap.put("imageUrl", downloadImageUrl);

        databaseReference.child("Sellers").child(auth.getCurrentUser().getUid()).child("Products").child(RandomKey).updateChildren(dataMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {
                            Fragment fragment = null;

                            fragment = new HomeFragment();
                            loadFragment(fragment);
                        }
                    }
                });
        Toast.makeText(getActivity(), "Product Uploaded", Toast.LENGTH_LONG).show();
        loadingBar.dismiss();


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GalleryPick && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            productImage.setImageURI(imageUri);
        }
    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment).commit();
        fragmentTransaction.addToBackStack(null);
    }
}