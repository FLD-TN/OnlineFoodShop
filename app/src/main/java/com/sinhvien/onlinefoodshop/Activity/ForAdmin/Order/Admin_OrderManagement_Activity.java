package com.sinhvien.onlinefoodshop.Activity.ForAdmin.Order;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.sinhvien.onlinefoodshop.R;
import com.sinhvien.onlinefoodshop.fragment.AdminOrdersFragment;

public class Admin_OrderManagement_Activity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_order_management);

        // Load AdminOrdersFragment
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new AdminOrdersFragment())
                    .commit();
        }
    }
}