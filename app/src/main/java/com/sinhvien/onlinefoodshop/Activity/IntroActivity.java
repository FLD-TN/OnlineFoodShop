package com.sinhvien.onlinefoodshop.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.sinhvien.onlinefoodshop.Adapter.IntroAdapter;
import com.sinhvien.onlinefoodshop.R;

import java.util.Arrays;
import java.util.List;

public class IntroActivity extends AppCompatActivity {
    private ViewPager2 viewPager;
    private Handler handler = new Handler(Looper.getMainLooper());
    private static final int DISPLAY_TIME = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        viewPager = findViewById(R.id.viewPager);
        // Danh sách ID tài nguyên từ raw
        List<Integer> animations = Arrays.asList(R.raw.loading); // Không cần đuôi .json
        IntroAdapter adapter = new IntroAdapter(animations);
        viewPager.setAdapter(adapter);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(IntroActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, DISPLAY_TIME);
    }
}