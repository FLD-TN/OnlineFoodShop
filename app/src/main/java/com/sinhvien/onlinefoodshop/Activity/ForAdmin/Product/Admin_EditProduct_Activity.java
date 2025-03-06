package com.sinhvien.onlinefoodshop.Activity.ForAdmin.Product;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.sinhvien.onlinefoodshop.ApiService;
import com.sinhvien.onlinefoodshop.Model.ProductModel;
import com.sinhvien.onlinefoodshop.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Admin_EditProduct_Activity extends AppCompatActivity {
    private static final String TAG = "Admin_EditProduct_Activity";
    private EditText edtProductName, edtProductPrice, edtDescription, edtCategory, edtProductImage;
    private Button btnSave, btnCancel;
    private ApiService apiService;
    private ProductModel currentProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_edit_product);

        edtProductName = findViewById(R.id.edtProductName);
        edtProductPrice = findViewById(R.id.edtProductPrice);
        edtDescription = findViewById(R.id.edtDescription);
        edtCategory = findViewById(R.id.edtCategory);
        edtProductImage = findViewById(R.id.edtProductImage);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://foodshop-backend-jck5.onrender.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(ApiService.class);

        Intent intent = getIntent();
        currentProduct = (ProductModel) intent.getSerializableExtra("productDetail");

        if (currentProduct != null && currentProduct.getProductID() != null) {
            edtProductName.setText(currentProduct.getProductName());
            edtProductPrice.setText(String.valueOf(currentProduct.getProductPrice()));
            edtDescription.setText(currentProduct.getDescription());
            edtCategory.setText(currentProduct.getCategory());
            edtProductImage.setText(currentProduct.getProductImage());
        } else {
            Log.e(TAG, "Current product is null or missing productID");
            Toast.makeText(this, "Không tải được thông tin sản phẩm", Toast.LENGTH_SHORT).show();
            finish();
        }

        btnSave.setOnClickListener(v -> saveProductChanges());
        btnCancel.setOnClickListener(v -> finish());
    }

    private void saveProductChanges() {
        if (currentProduct == null || currentProduct.getProductID() == null) {
            Toast.makeText(this, "Thông tin sản phẩm không hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }

        String productName = edtProductName.getText().toString().trim();
        String priceStr = edtProductPrice.getText().toString().trim();
        String description = edtDescription.getText().toString().trim();
        String category = edtCategory.getText().toString().trim();
        String productImage = edtProductImage.getText().toString().trim();

        if (productName.isEmpty() || priceStr.isEmpty()) {
            Toast.makeText(this, "Vui lòng điền tên và giá sản phẩm", Toast.LENGTH_SHORT).show();
            return;
        }

        double productPrice;
        try {
            productPrice = Double.parseDouble(priceStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Giá sản phẩm phải là số", Toast.LENGTH_SHORT).show();
            return;
        }

        currentProduct.setProductName(productName);
        currentProduct.setProductPrice(productPrice);
        currentProduct.setDescription(description);
        currentProduct.setCategory(category);
        currentProduct.setProductImage(productImage.isEmpty() ? null : productImage);

        apiService.updateProduct(currentProduct.getProductID(), currentProduct).enqueue(new Callback<ProductModel>() {
            @Override
            public void onResponse(Call<ProductModel> call, Response<ProductModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "Product updated successfully: " + productName);
                    Toast.makeText(Admin_EditProduct_Activity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("updatedProduct", response.body());
                    setResult(RESULT_OK, resultIntent);
                    finish();
                } else {
                    Log.e(TAG, "Update failed. Code: " + response.code() + ", Message: " + response.message());
                    Toast.makeText(Admin_EditProduct_Activity.this, "Cập nhật thất bại. Mã lỗi: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ProductModel> call, Throwable t) {
                Log.e(TAG, "Error updating product: " + t.getMessage());
                Toast.makeText(Admin_EditProduct_Activity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}