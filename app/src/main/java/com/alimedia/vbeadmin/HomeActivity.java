package com.alimedia.vbeadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeActivity extends AppCompatActivity {

    private DrawerLayout mDrawerlayout;
    private ActionBarDrawerToggle mtoggle;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private TextView headerNameTV;
    private TextView headerEmailTV;
    private String headerEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mDrawerlayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mtoggle = new ActionBarDrawerToggle(this, mDrawerlayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerlayout.addDrawerListener(mtoggle);
        mtoggle.syncState();

        FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
        tx.replace(R.id.container, new HomeFragment());
        tx.commit();

    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        navigationView = (NavigationView) findViewById(R.id.nav_view);

        headerNameTV = (TextView) findViewById(R.id.admin_name_TV);
        headerEmailTV = (TextView) findViewById(R.id.admin_email_TV);

        DatabaseReference headerRef = FirebaseDatabase.getInstance().getReference().child("Admin");

        headerRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {

                    headerEmail = snapshot.child("email").getValue().toString();
                    headerEmailTV.setText(headerEmail);
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

                    case R.id.addCategory:
                        fragment = new AddCategoryFragment();
                        loadFragment(fragment);
                        break;

                    case R.id.logout:
                        startActivity(new Intent(HomeActivity.this, LoginAdminActivity.class));
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Fragment fragment = null;
        fragment = new HomeFragment();
        loadFragment(fragment);
    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment).commit();
        mDrawerlayout.closeDrawer(GravityCompat.START);
        fragmentTransaction.addToBackStack(null);
    }
}