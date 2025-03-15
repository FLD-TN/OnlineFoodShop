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
import com.sinhvien.onlinefoodshop.R;

import java.text.DecimalFormat;

public class CartActivity extends AppCompatActivity implements CartAdapter.OnCartItemChangeListener {
    private RecyclerView recyclerCart;
    private TextView tvTotalPrice;
    private Button btnCheckout;
    private ImageView btnBack,btnClearCart;
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
                Intent intent = new Intent(this, CheckoutActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onQuantityChanged() {
        updateTotalPrice();
    }

    @Override
    public void onItemRemoved(int position) {
        cartManager.removeItem(position);
        // Thay vì dùng notifyItemRemoved(position), hãy dùng notifyDataSetChanged()
        cartAdapter.notifyDataSetChanged();
        updateTotalPrice();
    }

    private void updateTotalPrice() {
        double total = cartManager.getTotalPrice();
        DecimalFormat formatter = new DecimalFormat("#,###");
        tvTotalPrice.setText(formatter.format(total) + "đ");
    }
}