package com.biluutech.vbebuyer;

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


public class UpdateProfileFragment extends Fragment {

    private static final int GalleryPick = 1;

    private ImageButton addImageBtn;

    private EditText etBuyerName;
    private EditText etBuyerNumber;

    private CircleImageView civProfileImage;

    private Uri imageUri;
    private String myUrl = "";
    private StorageReference buyersImageRef;

    private ProgressDialog loadingBar;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    private Button btnUpdate;
    private String saveCurrentDate, saveCurrentTime, subcategoryRandomKey, downloadImageUrl;


    public UpdateProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_update_profile, container, false);

        addImageBtn = view.findViewById(R.id.add_image_Btn);
        etBuyerName = view.findViewById(R.id.et_buyer_name);
        etBuyerNumber = view.findViewById(R.id.et_buyer_number);
        civProfileImage = view.findViewById(R.id.civ_profile_image);
        btnUpdate = view.findViewById(R.id.btn_update);
        buyersImageRef = FirebaseStorage.getInstance().getReference().child("Profile Pictures");

        loadingBar = new ProgressDialog(getContext());

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        addImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenGallery();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ValidateProfileData();
            }
        });

        return view;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GalleryPick && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            civProfileImage.setImageURI(imageUri);
        }
    }

    private void OpenGallery() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GalleryPick);
    }

    private void ValidateProfileData() {

        if (imageUri == null) {
            Toast.makeText(getContext(), "Profile image is mandatory...", Toast.LENGTH_SHORT).show();
        } else if (!TextUtils.isEmpty(etBuyerName.getText().toString())) {
            if (!TextUtils.isEmpty(etBuyerNumber.getText().toString())) {

                StoreProfileInformation();

            } else {
                etBuyerNumber.setError("Enter PhoneNumber");
            }
        } else {
            etBuyerName.setError("Enter UserName");
        }
    }

    private void StoreProfileInformation() {
        loadingBar.setTitle("Update Profile");
        loadingBar.setMessage("Dear customer, please wait while we are uploading your profile.");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        subcategoryRandomKey = saveCurrentDate + saveCurrentTime;

        final StorageReference filePath = buyersImageRef.child(imageUri.getLastPathSegment() + subcategoryRandomKey + ".jpg");

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

                            SaveProfileInfoToDatabase();
                        }
                    }
                });
            }
        });
    }

    private void SaveProfileInfoToDatabase() {
        HashMap<String, Object> datamap = new HashMap<>();
        datamap.put("name", etBuyerName.getText().toString());
        datamap.put("number", etBuyerNumber.getText().toString());
        datamap.put("imageUrl", downloadImageUrl);

        databaseReference.child("Buyers").child(firebaseAuth.getCurrentUser().getUid()).updateChildren(datamap)
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
    private void loadFragment(Fragment fragment) {


        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment).commit();
        fragmentTransaction.addToBackStack(null);


    }
}
