package com.sinhvien.onlinefoodshop.Activity.ForUser;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.firebase.firestore.auth.User;
import com.google.gson.Gson;
import com.sinhvien.onlinefoodshop.CartManager;
import com.sinhvien.onlinefoodshop.Model.CartModel;
import com.sinhvien.onlinefoodshop.Model.OrderModel;
import com.sinhvien.onlinefoodshop.Model.ProductModel;
import com.sinhvien.onlinefoodshop.Model.UserModel;
import com.sinhvien.onlinefoodshop.R;
import com.sinhvien.onlinefoodshop.RetrofitClient;
import com.sinhvien.onlinefoodshop.fragment.OrderFragment;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckoutActivity extends AppCompatActivity {

    private EditText etAddress, etPhoneNumber;
    private RadioGroup rgPaymentMethod;
    private Button btnConfirmCheckout;
    private CartManager cartManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        etAddress = findViewById(R.id.etAddress);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        rgPaymentMethod = findViewById(R.id.rgPaymentMethod);
        btnConfirmCheckout = findViewById(R.id.btnConfirmCheckout);

        cartManager = CartManager.getInstance();

        btnConfirmCheckout.setOnClickListener(v -> {
            String address = etAddress.getText().toString().trim();
            String phoneNumber = etPhoneNumber.getText().toString().trim();
            int selectedId = rgPaymentMethod.getCheckedRadioButtonId();

            if (address.isEmpty() || phoneNumber.isEmpty()) {
                Toast.makeText(this, "Vui lòng điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (selectedId == -1) {
                Toast.makeText(this, "Vui lòng chọn phương thức thanh toán!", Toast.LENGTH_SHORT).show();
                return;
            }

            RadioButton selectedRadioButton = findViewById(selectedId);
            String paymentMethod = selectedRadioButton.getText().toString();

            if (paymentMethod.equals("Thanh toán khi nhận hàng (COD)")) {
                createOrder("COD", address, phoneNumber);
            } else if (paymentMethod.equals("Thanh toán online")) {
                Toast.makeText(this, "Chức năng thanh toán online chưa được hỗ trợ!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createOrder(String paymentMethod, String address, String phoneNumber) {
        List<CartModel> cartItems = cartManager.getCartItems();
        if (cartItems.isEmpty()) {
            Toast.makeText(this, "Giỏ hàng trống!", Toast.LENGTH_SHORT).show();
            return;
        }

        double totalPrice = cartManager.getTotalPrice();
        List<ProductModel> orderProducts = new ArrayList<>();
        List<UserModel> orderUserName = new ArrayList<>();

        for (CartModel cartItem : cartItems) {
            ProductModel product = new ProductModel();
            product.setProductID(cartItem.getProductID());
            product.setProductName(cartItem.getProductName());
            product.setProductPrice(cartItem.getProductPrice());
            product.setProductImage(cartItem.getProductImageUrl());
            product.setQuantity(cartItem.getQuantity());
            orderProducts.add(product);
        }

        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        Log.d("Checkout", "Stored email in prefs: " + prefs.getString("email", null));
        String userEmail = prefs.getString("email", null);
        String userName = prefs.getString("fullName", null);

        OrderModel order = new OrderModel(userEmail, orderProducts, totalPrice,
                paymentMethod, address, phoneNumber, userName);

        // Log request details
        Log.d("Checkout", "Creating order with details:");
        Log.d("Checkout", "User Email: " + userEmail);
        Log.d("Checkout", "User Name: " + userName);
        Log.d("Checkout", "Total Price: " + totalPrice);
        Log.d("Checkout", "Products Count: " + orderProducts.size());
        Log.d("Checkout", "Order JSON: " + new Gson().toJson(order));

        RetrofitClient.getApiService().createOrder(order).enqueue(new Callback<OrderModel>() {
            @Override
            public void onResponse(Call<OrderModel> call, Response<OrderModel> response) {
                if (response.isSuccessful()) {
                    OrderModel createdOrder = response.body();
                    Log.d("Checkout", "Order created successfully: " +
                            new Gson().toJson(createdOrder));
                    Toast.makeText(CheckoutActivity.this,
                            "Đặt hàng thành công!", Toast.LENGTH_LONG).show();
                    cartManager.clearCart();
                    finish();
                } else {
                    handleErrorResponse(response);
                }
            }

            @Override
            public void onFailure(Call<OrderModel> call, Throwable t) {
                Log.e("Checkout", "Network error: " + t.getMessage(), t);
                Toast.makeText(CheckoutActivity.this,
                        "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleErrorResponse(Response<OrderModel> response) {
        if (response.errorBody() != null) {
            try {
                String errorBody = response.errorBody().string();
                Log.e("Checkout", "Error response: " + errorBody);
                Toast.makeText(CheckoutActivity.this,
                        "Lỗi: " + errorBody, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Log.e("Checkout", "Error reading error body", e);
                Toast.makeText(CheckoutActivity.this,
                        "Lỗi không xác định", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(CheckoutActivity.this,
                    "Lỗi: " + response.code(), Toast.LENGTH_SHORT).show();
        }
    }
}