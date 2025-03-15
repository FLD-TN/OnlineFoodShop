package com.sinhvien.onlinefoodshop.Activity.ForAdmin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.sinhvien.onlinefoodshop.Activity.ForAdmin.Banner.Admin_AddBanner_Activity;
import com.sinhvien.onlinefoodshop.Activity.ForAdmin.Discount.Admin_AddDiscount_Activity;
import com.sinhvien.onlinefoodshop.Activity.ForAdmin.Notification.Admin_AddNotification_Activity;
import com.sinhvien.onlinefoodshop.Activity.ForAdmin.Product.Admin_ProductList_Activity;
import com.sinhvien.onlinefoodshop.Activity.ForAdmin.User.Admin_UserList_Activity;
import com.sinhvien.onlinefoodshop.Activity.ForAdmin.Category.Admin_CategoryList_Activity;
import com.sinhvien.onlinefoodshop.Activity.MainActivity;
import com.sinhvien.onlinefoodshop.R;
import com.sinhvien.onlinefoodshop.Activity.ForAdmin.Order.Admin_OrderManagement_Activity;

public class Admin_HomePage_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home_page);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button btnViewUsers = findViewById(R.id.btnViewUsers);
        Button btnViewProducts = findViewById(R.id.btnViewProducts);
        Button btnViewCategories = findViewById(R.id.btnViewCategories);
        Button btnLogout = findViewById(R.id.btnLogout);
        Button btnManageDiscounts = findViewById(R.id.btnManageDiscounts);
        Button btnManageNotifications = findViewById(R.id.btnManageNotifications);
        Button btnManageOrders = findViewById(R.id.btnManageOrders); // Thêm nút quản lý đơn hàng
        Button btnManageBanner = findViewById(R.id.btnManageBanner); // Thêm nút quản lý banner


        TextView tvUserCount = findViewById(R.id.tvUserCount);
        TextView tvProductCount = findViewById(R.id.tvProductCount);
        TextView tvCategoryCount = findViewById(R.id.tvCategoryCount);
        tvUserCount.setText("150");
        tvProductCount.setText("300");
        tvCategoryCount.setText("25");

        Toast.makeText(this, "Chào mừng Admin!", Toast.LENGTH_SHORT).show();

        btnViewUsers.setOnClickListener(v -> {
            Intent intent = new Intent(this, Admin_UserList_Activity.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });

        btnViewProducts.setOnClickListener(v -> {
            Intent intent = new Intent(this, Admin_ProductList_Activity.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });

        btnViewCategories.setOnClickListener(v -> {
            Intent intent = new Intent(this, Admin_CategoryList_Activity.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });

        btnManageDiscounts.setOnClickListener(v -> {
            Intent intent = new Intent(this, Admin_AddDiscount_Activity.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });

        btnManageNotifications.setOnClickListener(v -> {
            Intent intent = new Intent(this, Admin_AddNotification_Activity.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });

        btnManageOrders.setOnClickListener(v -> {
            Intent intent = new Intent(this, Admin_OrderManagement_Activity.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });

        btnManageBanner.setOnClickListener(v -> {
            Intent intent = new Intent(this, Admin_AddBanner_Activity.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });

        btnLogout.setOnClickListener(v -> {
            SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
            prefs.edit().clear().apply();
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        prefs.edit().putLong("LastBackgroundTime", System.currentTimeMillis()).apply();
    }
}