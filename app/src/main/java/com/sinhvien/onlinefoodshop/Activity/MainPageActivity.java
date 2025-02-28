package com.sinhvien.onlinefoodshop.Activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.sinhvien.onlinefoodshop.R;
import com.sinhvien.onlinefoodshop.fragment.AboutFragment;
import com.sinhvien.onlinefoodshop.fragment.CartFragment;
import com.sinhvien.onlinefoodshop.fragment.FavouriteFragment;
import com.sinhvien.onlinefoodshop.fragment.HomeFragment;
import com.sinhvien.onlinefoodshop.fragment.NotificationFragment;
import com.sinhvien.onlinefoodshop.fragment.UserInfomationFragment;

public class MainPageActivity extends AppCompatActivity {

    private static final int FRAGMENT_HOME = 0;
    private static final int FRAGMENT_ABOUT = 1;
    private static final int FRAGMENT_CART = 2;
    private static final int FRAGMENT_USER_INFORMATION = 3;
    private static final int FRAGMENT_NOTIFICATION = 4;
    private static final int FRAGMENT_FAVORITES = 5;

    private int mCurrentFragment = FRAGMENT_HOME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.bottom_nav_home && mCurrentFragment != FRAGMENT_HOME) {
                replaceFragment(new HomeFragment());
                mCurrentFragment = FRAGMENT_HOME;
            } else if (id == R.id.bottom_nav_cart && mCurrentFragment != FRAGMENT_CART) {
                replaceFragment(new CartFragment());
                mCurrentFragment = FRAGMENT_CART;
            } else if (id == R.id.bottom_nav_notification && mCurrentFragment != FRAGMENT_NOTIFICATION) {
                replaceFragment(new NotificationFragment());
                mCurrentFragment = FRAGMENT_NOTIFICATION;
            } else if (id == R.id.bottom_nav_fav && mCurrentFragment != FRAGMENT_FAVORITES) {
                replaceFragment(new FavouriteFragment());
                mCurrentFragment = FRAGMENT_FAVORITES;
            } else if (id == R.id.bottom_nav_user && mCurrentFragment != FRAGMENT_USER_INFORMATION) {
                replaceFragment(new UserInfomationFragment());
                mCurrentFragment = FRAGMENT_USER_INFORMATION;
            }
            return true;
        });

        if (savedInstanceState == null) {
            replaceFragment(new HomeFragment());
            bottomNavigationView.setSelectedItemId(R.id.bottom_nav_home);
        }
    }

    private void replaceFragment(androidx.fragment.app.Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, fragment)
                .commit();
    }

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        prefs.edit().putLong("LastBackgroundTime", System.currentTimeMillis()).apply();
    }
}