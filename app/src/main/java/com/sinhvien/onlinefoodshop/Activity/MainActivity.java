package com.sinhvien.onlinefoodshop.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.messaging.FirebaseMessaging;
import com.sinhvien.onlinefoodshop.Activity.ForAdmin.Admin_HomePage_Activity;
import com.sinhvien.onlinefoodshop.Activity.ForUser.MainPageActivity;
import com.sinhvien.onlinefoodshop.Adapter.ViewPagerAdapter;
import com.sinhvien.onlinefoodshop.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    private static final long TIMEOUT_MS = 300000; // 5 phút = 300.000 ms

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        setContentView(binding.getRoot());
        subscribeToFCMTopic();

        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        long lastBackgroundTime = prefs.getLong("LastBackgroundTime", 0);
        long currentTime = System.currentTimeMillis();

        String email = prefs.getString("Email", null);
        if (email != null) {
            if (lastBackgroundTime > 0 && (currentTime - lastBackgroundTime) > TIMEOUT_MS) {
                prefs.edit().clear().apply();
            } else {
                String cachedRole = prefs.getString("Role", "user");
                Intent intent = "admin".equalsIgnoreCase(cachedRole) ?
                        new Intent(this, Admin_HomePage_Activity.class) :
                        new Intent(this, MainPageActivity.class);
                startActivity(intent);
                finish();
                return;
            }
        }

        prefs.edit().clear().apply();
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), getLifecycle());
        binding.viewPager.setAdapter(adapter);

        new TabLayoutMediator(binding.tabLayout, binding.viewPager,
                (tab, position) -> tab.setText(position == 0 ? "Đăng Nhập" : "Đăng Ký")).attach();
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        prefs.edit().putLong("LastBackgroundTime", System.currentTimeMillis()).apply();
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        long lastBackgroundTime = prefs.getLong("LastBackgroundTime", 0);
        long currentTime = System.currentTimeMillis();

        if (prefs.getString("Email", null) != null && lastBackgroundTime > 0 && (currentTime - lastBackgroundTime) > TIMEOUT_MS) {
            prefs.edit().clear().apply();
            recreate();
        }
    }


    public void subscribeToFCMTopic() {
        FirebaseMessaging.getInstance().subscribeToTopic("all_users")
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("FCM", "Đăng ký nhận thông báo thành công!");
                        Toast.makeText(getApplicationContext(), "Đã đăng ký nhận thông báo!", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.e("FCM", "Lỗi đăng ký nhận thông báo!");
                    }
                });
    }
}