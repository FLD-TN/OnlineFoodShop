package com.sinhvien.onlinefoodshop.Activity.ForAdmin.Product;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.sinhvien.onlinefoodshop.ApiService;
import com.sinhvien.onlinefoodshop.Model.ProductModel;
import com.sinhvien.onlinefoodshop.R;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Admin_ProductDetails_Activity extends AppCompatActivity {
    private static final String TAG = "Admin_ProductDetails_Activity";
    private TextView tvProductName, tvProductPrice, tvDescription, tvCategory;
    private ImageView ivProductImage;
    private Button btnBack, btnEditProduct, btnDelete;
    private ApiService apiService;
    private ProductModel currentProduct;
    private ProgressBar progressBar;

    private final ActivityResultLauncher<Intent> editProductLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    ProductModel updatedProduct = (ProductModel) result.getData().getSerializableExtra("updatedProduct");
                    if (updatedProduct != null) {
                        currentProduct = updatedProduct;
                        updateUI();
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("dataChanged", true);
                        setResult(RESULT_OK, resultIntent);
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_product_details);

        tvProductName = findViewById(R.id.tvProductName);
        tvProductPrice = findViewById(R.id.tvProductPrice);
        tvDescription = findViewById(R.id.tvDescription);
        tvCategory = findViewById(R.id.tvCategory);
        ivProductImage = findViewById(R.id.ivProductImage);
        btnBack = findViewById(R.id.btnBack);
        btnEditProduct = findViewById(R.id.btnEditProduct);
        btnDelete = findViewById(R.id.btnDelete);
        progressBar = findViewById(R.id.progressBar);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://foodshop-backend-jck5.onrender.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(ApiService.class);

        Intent intent = getIntent();
        currentProduct = (ProductModel) intent.getSerializableExtra("productDetail");

        if (currentProduct != null && currentProduct.getProductID() != null) {
            updateUI();
        } else {
            Log.e(TAG, "Current product is null or missing productID");
            Toast.makeText(this, "Không tải được thông tin sản phẩm", Toast.LENGTH_SHORT).show();
            finish();
        }

        btnBack.setOnClickListener(v -> {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("dataChanged", true);
            setResult(RESULT_OK, resultIntent);
            finish();
        });

        btnEditProduct.setOnClickListener(v -> {
            Intent editIntent = new Intent(this, Admin_EditProduct_Activity.class);
            editIntent.putExtra("productDetail", currentProduct);
            editProductLauncher.launch(editIntent);
        });

        btnDelete.setOnClickListener(v -> confirmDeleteProduct());
    }

    private void updateUI() {
        tvProductName.setText("Tên: " + (currentProduct.getProductName() != null ? currentProduct.getProductName() : ""));
        tvProductPrice.setText("Giá: " + String.format("%,.0fđ", currentProduct.getProductPrice()));
        tvDescription.setText("Mô tả: " + (currentProduct.getDescription() != null ? currentProduct.getDescription() : ""));
        tvCategory.setText("Danh mục: " + (currentProduct.getCategory() != null ? currentProduct.getCategory() : ""));

        String iconUrl = currentProduct.getProductImage();
        if (iconUrl != null && !iconUrl.isEmpty()) {
            progressBar.setVisibility(View.VISIBLE); // Hiển thị ProgressBar khi bắt đầu tải ảnh
            Picasso.get()
                    .load(iconUrl)
                    .placeholder(R.drawable.placeholder_image)
                    .error(R.drawable.placeholder_image)
                    .into(ivProductImage, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                            progressBar.setVisibility(View.GONE); // Ẩn ProgressBar khi tải thành công
                        }

                        @Override
                        public void onError(Exception e) {
                            progressBar.setVisibility(View.GONE); // Ẩn ProgressBar khi tải thất bại
                            Log.e(TAG, "Error loading image: " + e.getMessage());
                            Toast.makeText(Admin_ProductDetails_Activity.this, "Không tải được ảnh", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            ivProductImage.setImageResource(R.drawable.placeholder_image);
            progressBar.setVisibility(View.GONE); // Ẩn ProgressBar nếu không có URL
        }
    }

    private void confirmDeleteProduct() {
        if (currentProduct == null || currentProduct.getProductID() == null) {
            Toast.makeText(this, "Không thể xóa: Thông tin sản phẩm không hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }

        new AlertDialog.Builder(this)
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc muốn xóa sản phẩm " + currentProduct.getProductName() + "?")
                .setPositiveButton("Có", (dialog, which) -> deleteProduct())
                .setNegativeButton("Không", null)
                .show();
    }

    private void deleteProduct() {
        progressBar.setVisibility(View.VISIBLE);
        apiService.deleteProduct(currentProduct.getProductID()).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    Log.d(TAG, "Product deleted: " + currentProduct.getProductID());
                    Toast.makeText(Admin_ProductDetails_Activity.this, "Xóa sản phẩm thành công", Toast.LENGTH_SHORT).show();
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("deletedId", currentProduct.getProductID());
                    setResult(RESULT_OK, resultIntent);
                    finish();
                } else {
                    Log.e(TAG, "Delete failed. Code: " + response.code() + ", Message: " + response.message());
                    Toast.makeText(Admin_ProductDetails_Activity.this, "Xóa sản phẩm thất bại. Mã lỗi: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Log.e(TAG, "Error deleting product: " + t.getMessage());
                Toast.makeText(Admin_ProductDetails_Activity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}