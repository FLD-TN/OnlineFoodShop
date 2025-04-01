package com.sinhvien.onlinefoodshop.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.sinhvien.onlinefoodshop.Adapter.OrderDetailAdapter;
import com.sinhvien.onlinefoodshop.Model.OrderModel;
import com.sinhvien.onlinefoodshop.R;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class OrderDetailActivity extends AppCompatActivity {
    private TextView tvOrderId, tvUserEmail, tvTotalPrice, tvStatus, tvOrderDate, tvAddress, tvPhoneNumber, tvPaymentMethod;
    private RecyclerView recyclerOrderItems;
    private ImageView btnBack;
    private Button btnEditStatus, btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        // Ánh xạ thủ công
        tvOrderId = findViewById(R.id.tvOrderId);
        tvUserEmail = findViewById(R.id.tvUserEmail);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        tvStatus = findViewById(R.id.tvStatus);
        tvOrderDate = findViewById(R.id.tvOrderDate);
        tvAddress = findViewById(R.id.tvAddress);
        tvPhoneNumber = findViewById(R.id.tvPhoneNumber);
        tvPaymentMethod = findViewById(R.id.tvPaymentMethod);
        recyclerOrderItems = findViewById(R.id.recyclerOrderItems);
        btnBack = findViewById(R.id.btnBack);
        btnEditStatus = findViewById(R.id.btnEditStatus);
        btnCancel = findViewById(R.id.btnCancel);

        // Lấy dữ liệu từ Intent
        OrderModel order = (OrderModel) getIntent().getSerializableExtra("order");
        boolean isAdmin = getIntent().getBooleanExtra("isAdmin", false);

        if (order != null) {
            tvOrderId.setText("Mã đơn: " + order.getOrderID());
            tvUserEmail.setText("Email: " + order.getUserEmail());
            tvTotalPrice.setText("Tổng: " + String.format("%.2f", order.getTotalPrice()));
            tvStatus.setText("Trạng thái: " + order.getStatus());
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
            tvOrderDate.setText("Ngày đặt: " + sdf.format(order.getOrderDate()));
            tvAddress.setText("Địa chỉ: " + order.getAddress());
            tvPhoneNumber.setText("SĐT: " + order.getPhoneNumber());
            tvPaymentMethod.setText("Phương thức thanh toán: " + order.getPaymentMethod());

            // Thiết lập RecyclerView
            OrderDetailAdapter adapter = new OrderDetailAdapter(this, order.getProducts());
            recyclerOrderItems.setLayoutManager(new LinearLayoutManager(this));
            recyclerOrderItems.setAdapter(adapter);

            btnBack.setOnClickListener(v -> finish());

            // Ẩn nút chỉnh sửa nếu không phải admin
            if (!isAdmin) {
                btnEditStatus.setVisibility(View.GONE);
                btnCancel.setVisibility(View.GONE);
            } else {
                btnEditStatus.setOnClickListener(v -> {
                    // Thêm logic chỉnh trạng thái
                });
                btnCancel.setOnClickListener(v -> {
                    // Thêm logic hủy đơn
                });
            }
        }

        // Nút quay lại
        btnBack.setOnClickListener(v -> finish());
    }
}