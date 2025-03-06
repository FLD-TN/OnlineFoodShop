package com.sinhvien.onlinefoodshop.Activity.ForUser;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.badge.BadgeDrawable;
import com.sinhvien.onlinefoodshop.Activity.CartActivity;
import com.sinhvien.onlinefoodshop.CartManager;
import com.sinhvien.onlinefoodshop.R;
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
    private int mPreviousFragment = FRAGMENT_HOME; // Lưu Fragment trước khi vào Cart
    private BottomNavigationView bottomNavigationView;

    // Lưu instance của các Fragment
    private HomeFragment homeFragment = new HomeFragment();
    private FavouriteFragment favouriteFragment = new FavouriteFragment();
    private NotificationFragment notificationFragment = new NotificationFragment();
    private UserInfomationFragment userInfomationFragment = new UserInfomationFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        CartManager.getInstance().setOnCartChangeListener(this::updateCartBadge);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.bottom_nav_home && mCurrentFragment != FRAGMENT_HOME) {
                replaceFragment(homeFragment);
                mCurrentFragment = FRAGMENT_HOME;
            } else if (id == R.id.bottom_nav_cart) {
                mPreviousFragment = mCurrentFragment; // Lưu Fragment hiện tại trước khi vào Cart
                Intent intent = new Intent(MainPageActivity.this, CartActivity.class);
                startActivity(intent);
                mCurrentFragment = FRAGMENT_CART;
                return false; // Không highlight tab "cart"
            } else if (id == R.id.bottom_nav_notification && mCurrentFragment != FRAGMENT_NOTIFICATION) {
                replaceFragment(notificationFragment);
                mCurrentFragment = FRAGMENT_NOTIFICATION;
            } else if (id == R.id.bottom_nav_fav && mCurrentFragment != FRAGMENT_FAVORITES) {
                replaceFragment(favouriteFragment);
                mCurrentFragment = FRAGMENT_FAVORITES;
            } else if (id == R.id.bottom_nav_user && mCurrentFragment != FRAGMENT_USER_INFORMATION) {
                replaceFragment(userInfomationFragment);
                mCurrentFragment = FRAGMENT_USER_INFORMATION;
            }
            return true;
        });

        if (savedInstanceState == null) {
            replaceFragment(homeFragment);
            bottomNavigationView.setSelectedItemId(R.id.bottom_nav_home);
        }

        updateCartBadge();
    }

    private void updateCartBadge() {
        int cartItemCount = CartManager.getInstance().getCartItems().size();
        BadgeDrawable badge = bottomNavigationView.getOrCreateBadge(R.id.bottom_nav_cart);
        if (cartItemCount > 0) {
            badge.setNumber(cartItemCount);
            badge.setVisible(true);
        } else {
            badge.setVisible(false);
        }
    }

    private void replaceFragment(androidx.fragment.app.Fragment fragment) {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.content_frame);
        if (currentFragment == null || !currentFragment.equals(fragment)) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, fragment)
                    .commit();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateCartBadge(); // Cập nhật badge

        if (mCurrentFragment == FRAGMENT_CART) {
            // Quay lại Fragment trước đó thay vì mặc định về Home
            mCurrentFragment = mPreviousFragment;
            switch (mCurrentFragment) {
                case FRAGMENT_HOME:
                    replaceFragment(homeFragment);
                    bottomNavigationView.setSelectedItemId(R.id.bottom_nav_home);
                    break;
                case FRAGMENT_NOTIFICATION:
                    replaceFragment(notificationFragment);
                    bottomNavigationView.setSelectedItemId(R.id.bottom_nav_notification);
                    break;
                case FRAGMENT_FAVORITES:
                    replaceFragment(favouriteFragment);
                    bottomNavigationView.setSelectedItemId(R.id.bottom_nav_fav);
                    break;
                case FRAGMENT_USER_INFORMATION:
                    replaceFragment(userInfomationFragment);
                    bottomNavigationView.setSelectedItemId(R.id.bottom_nav_user);
                    break;
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("currentFragment", mCurrentFragment);
        outState.putInt("previousFragment", mPreviousFragment);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mCurrentFragment = savedInstanceState.getInt("currentFragment", FRAGMENT_HOME);
        mPreviousFragment = savedInstanceState.getInt("previousFragment", FRAGMENT_HOME);
    }
}