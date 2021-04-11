package com.biluutech.vbebuyer.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.biluutech.vbebuyer.Fragments.AboutUsFragment;
import com.biluutech.vbebuyer.Fragments.HelpFragment;
import com.biluutech.vbebuyer.Fragments.HomeFragment;
import com.biluutech.vbebuyer.R;
import com.biluutech.vbebuyer.Fragments.UpdateProfileFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class HomeActivity extends AppCompatActivity {


    private DrawerLayout mDrawerlayout;
    private ActionBarDrawerToggle mtoggle;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private TextView headerNameTV;
    private TextView headerEmailTV;
    private ImageView headerImageIV;
    private FirebaseAuth firebaseAuth;
    private String headerName, headerNumber, headerImage;
    private TextView tvCartCounter;
    private String counter;
    private static final int RECORD_PERMISSION = 100;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        firebaseAuth = FirebaseAuth.getInstance();

        headerNameTV = (TextView) findViewById(R.id.buyer_name_TV);
        headerEmailTV = (TextView) findViewById(R.id.buyer_email_TV);
        headerImageIV = (ImageView) findViewById(R.id.header_image);

        DatabaseReference headerRef = FirebaseDatabase.getInstance().getReference().child("Buyers").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        headerRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {

                    headerName = snapshot.child("name").getValue().toString();
                    headerNumber = snapshot.child("email").getValue().toString();

                    if (snapshot.hasChild("imageUrl")) {

                        headerImage = snapshot.child("imageUrl").getValue().toString();
                        Picasso.get().load(headerImage).into(headerImageIV);

                    }
                    headerNameTV.setText(headerName);
                    headerEmailTV.setText(headerNumber);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int id = item.getItemId();
                Fragment fragment = null;

                switch (id) {

                    case R.id.home:
                        fragment = new HomeFragment();
                        loadFragment(fragment);
                        break;

                    case R.id.updateProfile:
                        fragment = new UpdateProfileFragment();
                        loadFragment(fragment);
                        break;

                    case R.id.cart:
                        startActivity(new Intent(HomeActivity.this,CartActivity.class));
                        finish();
                        break;

                    case R.id.aboutUs:
                        fragment = new AboutUsFragment();
                        loadFragment(fragment);
                        break;

                    case R.id.help:
                        fragment = new HelpFragment();
                        loadFragment(fragment);
                        break;

                    case R.id.logout:
                        firebaseAuth.signOut();
                        startActivity(new Intent(HomeActivity.this, LoginBuyerActivity.class));
                        finish();
                        break;
                }
                return true;
            }
        });

        if (mtoggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment).commit();
        mDrawerlayout.closeDrawer(GravityCompat.START);
        fragmentTransaction.addToBackStack(null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        tvCartCounter = findViewById(R.id.tv_cart_counter);

        mDrawerlayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mtoggle = new ActionBarDrawerToggle(this, mDrawerlayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerlayout.addDrawerListener(mtoggle);
        mtoggle.syncState();

        FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
        tx.replace(R.id.container, new HomeFragment());
        tx.commit();
        isStoragePermissionGranted();

        DatabaseReference cartCountRef = FirebaseDatabase.getInstance().getReference().child("Cart").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        cartCountRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    counter = String.valueOf(snapshot.getChildrenCount());
                    tvCartCounter.setText(counter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public boolean isStoragePermissionGranted() {
        int ACCESS_EXTERNAL_STORAGE = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
        if ((ACCESS_EXTERNAL_STORAGE != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, RECORD_PERMISSION);
            return false;
        }
        return true;
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RECORD_PERMISSION && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Fragment fragment = null;
        fragment = new HomeFragment();
        loadFragment(fragment);
    }

    public void btnCartClick(View view) {
        startActivity(new Intent(HomeActivity.this,CartActivity.class));
        finish();
    }
}