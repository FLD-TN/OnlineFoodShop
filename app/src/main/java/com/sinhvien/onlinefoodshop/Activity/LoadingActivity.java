package com.sinhvien.onlinefoodshop.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.appcompat.app.AppCompatActivity;

import com.sinhvien.onlinefoodshop.R;

public class LoadingActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        // Lấy Intent đích từ Activity trước
        Intent targetIntent = getIntent().getParcelableExtra("targetIntent");

        // Chạy animation trong 2 giây rồi chuyển sang Activity đích
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            if (targetIntent != null) {
                startActivity(targetIntent);
            }
            finish(); // Đóng LoadingActivity
        }, 2000); // 2000ms = 2 giây
    }
}