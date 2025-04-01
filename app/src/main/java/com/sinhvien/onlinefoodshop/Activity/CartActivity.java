package com.sinhvien.onlinefoodshop.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sinhvien.onlinefoodshop.Activity.ForUser.CheckoutActivity;
import com.sinhvien.onlinefoodshop.Adapter.CartAdapter;
import com.sinhvien.onlinefoodshop.CartManager;
import com.sinhvien.onlinefoodshop.Model.OrderModel;
import com.sinhvien.onlinefoodshop.Model.ProductModel;
import com.sinhvien.onlinefoodshop.R;
import com.sinhvien.onlinefoodshop.RetrofitClient;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartActivity extends AppCompatActivity implements CartAdapter.OnCartItemChangeListener {
    private RecyclerView recyclerCart;
    private TextView tvTotalPrice;
    private Button btnCheckout;
    private ImageView btnBack, btnClearCart;
    private CartAdapter cartAdapter;
    private CartManager cartManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recyclerCart = findViewById(R.id.recyclerCart);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        btnCheckout = findViewById(R.id.btnCheckout);
        btnBack = findViewById(R.id.btnBack);
        btnClearCart = findViewById(R.id.btnClearCart);

        cartManager = CartManager.getInstance();
        cartAdapter = new CartAdapter(this, cartManager.getCartItems(), this);

        recyclerCart.setLayoutManager(new LinearLayoutManager(this));
        recyclerCart.setAdapter(cartAdapter);

        updateTotalPrice();

        btnBack.setOnClickListener(v -> finish());

        btnClearCart.setOnClickListener(v -> {
            cartManager.clearCart();
            cartAdapter.notifyDataSetChanged();
            updateTotalPrice();
        });

        btnCheckout.setOnClickListener(v -> {
            if (cartManager.getCartItems().isEmpty()) {
                Toast.makeText(this, "Giỏ hàng trống!", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(CartActivity.this, CheckoutActivity.class);
                startActivity(intent);
            }
        });
    }

    private void createOrder() {
        String userEmail = getSharedPreferences("UserPrefs", MODE_PRIVATE).getString("email", "");
        String userName = getSharedPreferences("UserPrefs", MODE_PRIVATE).getString("fullName", "Unknown");

        List<ProductModel> orderProducts = new ArrayList<>();
        for (com.sinhvien.onlinefoodshop.Model.CartModel cartItem : cartManager.getCartItems()) {
            ProductModel product = new ProductModel(
                    cartItem.getProductName(),
                    cartItem.getProductPrice(),
                    "", // Description
                    "", // Category
                    cartItem.getProductImageUrl()
            );
            product.setProductID(cartItem.getProductID());
            product.setQuantity(cartItem.getQuantity());
            orderProducts.add(product);
        }

        // Lấy giờ hiện tại của Việt Nam
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
        Date vietnamDate = calendar.getTime();

        OrderModel order = new OrderModel(
                userEmail,
                orderProducts,
                cartManager.getTotalPrice(),
                "CASH",
                "Default Address",
                "0123456789",
                userName,
                vietnamDate
        );

        RetrofitClient.getApiService().createOrder(order).enqueue(new Callback<OrderModel>() {
            @Override
            public void onResponse(Call<OrderModel> call, Response<OrderModel> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(CartActivity.this, "Đặt hàng thành công!", Toast.LENGTH_SHORT).show();
                    cartManager.clearCart();
                    cartAdapter.notifyDataSetChanged();
                    updateTotalPrice();
                    Intent intent = new Intent(CartActivity.this, MainActivity.class);
                    intent.putExtra("navigateTo", "OrderFragment");
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(CartActivity.this, "Đặt hàng thất bại!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<OrderModel> call, Throwable t) {
                Toast.makeText(CartActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        updateTotalPrice();
        finish();
    }

    @Override
    public void onQuantityChanged() {
        updateTotalPrice();
    }

    @Override
    public void onItemRemoved(int position) {
        cartManager.removeItem(position);
        cartAdapter.notifyDataSetChanged();
        updateTotalPrice();
    }

    private void updateTotalPrice() {
        double total = cartManager.getTotalPrice();
        DecimalFormat formatter = new DecimalFormat("#,###");
        tvTotalPrice.setText(formatter.format(total) + "đ");
    }
}