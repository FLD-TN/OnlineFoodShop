package com.sinhvien.onlinefoodshop.Activity.ForAdmin.Banner;

import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.sinhvien.onlinefoodshop.R;

public class Admin_AddBanner_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_add_banner);

        Button btnAddBanner = findViewById(R.id.btnAddBanner);

        btnAddBanner.setOnClickListener(v -> {


        });


    }
}