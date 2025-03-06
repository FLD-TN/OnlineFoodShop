package com.sinhvien.onlinefoodshop.Activity.ForUser;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.button.MaterialButton;
import com.sinhvien.onlinefoodshop.Adapter.UserProductAdapter;
import com.sinhvien.onlinefoodshop.CartManager;
import com.sinhvien.onlinefoodshop.Model.CartModel;
import com.sinhvien.onlinefoodshop.Model.ProductModel;
import com.sinhvien.onlinefoodshop.R;
import com.sinhvien.onlinefoodshop.ApiService;
import com.squareup.picasso.Picasso;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import java.util.ArrayList;
import java.util.List;

public class Product_Details_Activity extends AppCompatActivity {
    private static final String TAG = "ProductDetailsActivity";
    private ImageView ivProductImage;
    private TextView tvProductName, tvProductPrice, tvProductDescription;
    private MaterialButton btnAddToCart;
    private RecyclerView recyclerRelatedProducts;
    private UserProductAdapter relatedProductsAdapter;
    private ProductModel product;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        // Ánh xạ giao diện
        ivProductImage = findViewById(R.id.ivProductImage);
        tvProductName = findViewById(R.id.tvProductName);
        tvProductPrice = findViewById(R.id.tvProductPrice);
        tvProductDescription = findViewById(R.id.tvProductDescription);
        btnAddToCart = findViewById(R.id.btnAddToCart);
        recyclerRelatedProducts = findViewById(R.id.recyclerRelatedProducts);

        // Khởi tạo Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://foodshop-backend-jck5.onrender.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(ApiService.class);

        // Lấy dữ liệu sản phẩm từ Intent
        product = (ProductModel) getIntent().getSerializableExtra("productDetail");
        if (product == null) {
            Toast.makeText(this, "Không thể tải chi tiết sản phẩm", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Hiển thị thông tin sản phẩm
        tvProductName.setText(product.getProductName() != null ? product.getProductName() : "Tên trống");
        tvProductPrice.setText(String.format("%,.0fđ", product.getProductPrice()));
        tvProductDescription.setText(product.getDescription() != null ? product.getDescription() : "Không có mô tả");
        if (product.getProductImage() != null && !product.getProductImage().isEmpty()) {
            Picasso.get()
                    .load(product.getProductImage())
                    .placeholder(R.drawable.placeholder_image)
                    .error(R.drawable.placeholder_image)
                    .into(ivProductImage);
        } else {
            ivProductImage.setImageResource(R.drawable.placeholder_image);
        }

        // Thiết lập nút "Thêm vào giỏ hàng"
            btnAddToCart.setOnClickListener(v -> {
            CartModel cartItem = new CartModel(
                    product.getProductID(),
                    product.getProductName(),
                    product.getProductPrice(),
                    product.getProductImage(),
                    1
            );
            CartManager.getInstance().addToCart(cartItem);
            Toast.makeText(this, "Đã thêm " + product.getProductName() + " vào giỏ hàng", Toast.LENGTH_SHORT).show();
        });

        // Thiết lập RecyclerView cho sản phẩm cùng loại
        relatedProductsAdapter = new UserProductAdapter();
        recyclerRelatedProducts.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerRelatedProducts.setAdapter(relatedProductsAdapter);

        // Tải sản phẩm cùng loại
        loadRelatedProducts(product.getCategory());
    }

    private void loadRelatedProducts(String category) {
        Call<List<ProductModel>> call = apiService.getProducts();
        call.enqueue(new Callback<List<ProductModel>>() {
            @Override
            public void onResponse(Call<List<ProductModel>> call, Response<List<ProductModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<ProductModel> allProducts = response.body();
                    List<ProductModel> relatedProducts = new ArrayList<>();
                    for (ProductModel p : allProducts) {
                        if (p.getCategory() != null && p.getCategory().equals(category) &&
                                !p.getProductID().equals(product.getProductID())) {
                            relatedProducts.add(p);
                        }
                    }
                    relatedProductsAdapter.setProductList(relatedProducts);
                    Log.d(TAG, "Loaded " + relatedProducts.size() + " related products for category: " + category);
                } else {
                    Toast.makeText(Product_Details_Activity.this, "Không thể tải sản phẩm cùng loại", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Failed to load related products. Code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<ProductModel>> call, Throwable t) {
                Toast.makeText(Product_Details_Activity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Error loading related products: " + t.getMessage());
            }
        });
    }
}