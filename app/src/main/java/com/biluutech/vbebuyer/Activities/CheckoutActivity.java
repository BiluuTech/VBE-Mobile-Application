package com.biluutech.vbebuyer.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.biluutech.vbebuyer.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class CheckoutActivity extends AppCompatActivity {

    private TextView tvBuyerName, tvBuyerAddress, tvTotalItems, tvSubTotal, tvTotal;
    private Button btnPLaceOrder;

    private DatabaseReference buyerDataRef, itemsRef, orderRef;

    private long total = 0;
    private String totalItems, address;

    private FloatingActionButton fabCheckout;
    private SpeechRecognizer speechRecognizer;
    private Intent speechRecognizerIntent;
    private String keeper = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        tvBuyerName = findViewById(R.id.tvBuyerName);
        tvBuyerAddress = findViewById(R.id.tvBuyerAddress);
        tvTotalItems = findViewById(R.id.tvTotalItems);
        tvSubTotal = findViewById(R.id.tvSubTotal);
        tvTotal = findViewById(R.id.tvTotal);
        btnPLaceOrder = findViewById(R.id.btn_place_order);

        fabCheckout = findViewById(R.id.fabCheckout);

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());


        buyerDataRef = FirebaseDatabase.getInstance().getReference().child("Buyers").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        itemsRef = FirebaseDatabase.getInstance().getReference().child("Cart").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        orderRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(String.valueOf(System.currentTimeMillis()));

        buyerDataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String name = snapshot.child("name").getValue().toString();
                    address = snapshot.child("address").getValue().toString();

                    tvBuyerName.setText(name);
                    tvBuyerAddress.setText(address);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        itemsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                totalItems = String.valueOf(snapshot.getChildrenCount());
                tvTotalItems.setText(totalItems);

                for (DataSnapshot ds : snapshot.getChildren()) {
                    String productPrice = ds.child("productPrice").getValue(String.class);
                    total += Long.parseLong(productPrice);
                }
                tvSubTotal.setText("Rs " + (int) total + " /-");
                tvTotal.setText("Rs " + (int) (total + 500) + " /-");
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
                        startActivity(new Intent(CheckoutActivity.this, CartActivity.class));
                        finish();
                    }else if (keeper.contains("place") || keeper.contains("confirm")){
                        startActivity(new Intent(CheckoutActivity.this,ThanksActivity.class));
                        finish();
                    }
                    else {
                        Toast.makeText(CheckoutActivity.this, "Sorry no result found!", Toast.LENGTH_SHORT).show();
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

        fabCheckout.setOnTouchListener(new View.OnTouchListener() {
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

    public void ibBackArrowCheckout(View view) {
        startActivity(new Intent(CheckoutActivity.this, CartActivity.class));
        finish();
    }

    public void btnPlaceOrderClick(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Order Placing");
        builder.setMessage("Are you sure you want to place order?");
        builder.setIcon(getResources().getDrawable(R.drawable.logo));
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            builder.setBackground(getResources().getDrawable(R.drawable.alert_bg,null));
//        }
        builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                orderRef.child("totalItems").setValue(totalItems);
                orderRef.child("bid").setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());
                orderRef.child("totalAmount").setValue(String.valueOf(total + 500));
                orderRef.child("address").setValue(address);

                itemsRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for (DataSnapshot sp : snapshot.getChildren()) {

                            String pid = sp.child("pid").getValue().toString();

                            HashMap<String, Object> orderMap = new HashMap<>();
                            orderMap.put("productId", pid);
                            orderRef.child(pid).updateChildren(orderMap);

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                itemsRef.removeValue();
                startActivity(new Intent(CheckoutActivity.this,ThanksActivity.class));
                finish();

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setCancelable(false);
        AlertDialog dialog = builder.show();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.Red));
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorBlue));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(CheckoutActivity.this, CartActivity.class));
        finish();
    }
}