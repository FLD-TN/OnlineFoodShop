package com.sinhvien.onlinefoodshop.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.airbnb.lottie.LottieAnimationView;
import com.sinhvien.onlinefoodshop.Adapter.ProductAdapter;
import com.sinhvien.onlinefoodshop.ApiService;
import com.sinhvien.onlinefoodshop.Model.ProductModel;
import com.sinhvien.onlinefoodshop.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import java.util.List;

public class Admin_ProductList_Activity extends AppCompatActivity {
    private static final String TAG = "Admin_ProductList_Activity";
    private RecyclerView rvProductList;
    private ProductAdapter productAdapter;
    private EditText etSearchProduct;
    private ProgressBar progressBar;
    private Button btnAddProduct;
    private ApiService apiService;
    private final String BASE_URL = "https://foodshop-backend-jck5.onrender.com/";

    private final ActivityResultLauncher<Intent> productDetailsLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    String deletedId = result.getData().getStringExtra("deletedId");
                    boolean dataChanged = result.getData().getBooleanExtra("dataChanged", false);
                    if (deletedId != null || dataChanged) {
                        loadProductList(); // Reload danh sách sau khi xóa hoặc sửa
                    }
                }
            });

    private final ActivityResultLauncher<Intent> addProductLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    ProductModel newProduct = (ProductModel) result.getData().getSerializableExtra("newProduct");
                    if (newProduct != null) {
                        loadProductList(); // Reload danh sách sau khi thêm
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_product_list);

        rvProductList = findViewById(R.id.rvProductList);
        etSearchProduct = findViewById(R.id.etSearchProduct);
        progressBar = findViewById(R.id.progressBar);
        btnAddProduct = findViewById(R.id.btnAddProduct);

        productAdapter = new ProductAdapter(productDetailsLauncher);
        rvProductList.setLayoutManager(new LinearLayoutManager(this));
        rvProductList.setAdapter(productAdapter);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(ApiService.class);
        //load danh sách product
        loadProductList();

        btnAddProduct.setOnClickListener(v -> {
            Intent intent = new Intent(this, Admin_AddProduct_Activity.class);
            addProductLauncher.launch(intent);
        });

        etSearchProduct.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String query = s.toString().trim();
                if (query.isEmpty()) {
                    loadProductList();
                } else {
                    searchProducts(query);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void loadProductList() {
        progressBar.setVisibility(View.VISIBLE);
        Call<List<ProductModel>> call = apiService.getProducts();
        call.enqueue(new Callback<List<ProductModel>>() {
            @Override
            public void onResponse(Call<List<ProductModel>> call, Response<List<ProductModel>> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    List<ProductModel> products = response.body();
                    Log.d(TAG, "Loaded " + products.size() + " products from server");
                    productAdapter.setProductList(products);
                    if (products.isEmpty()) {
                        Toast.makeText(Admin_ProductList_Activity.this, "Danh sách sản phẩm rỗng", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e(TAG, "Failed to load products. Code: " + response.code());
                    Toast.makeText(Admin_ProductList_Activity.this, "Không thể lấy dữ liệu. Mã lỗi: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ProductModel>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Log.e(TAG, "Error loading products: " + t.getMessage());
                Toast.makeText(Admin_ProductList_Activity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void searchProducts(String query) {
        progressBar.setVisibility(View.VISIBLE);
        Call<List<ProductModel>> call = apiService.searchProductsByName(query);
        call.enqueue(new Callback<List<ProductModel>>() {
            @Override
            public void onResponse(Call<List<ProductModel>> call, Response<List<ProductModel>> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    List<ProductModel> products = response.body();
                    Log.d(TAG, "Search found " + products.size() + " products for query: " + query);
                    productAdapter.setProductList(products);
                    if (products.isEmpty()) {
                        Toast.makeText(Admin_ProductList_Activity.this, "Không tìm thấy sản phẩm nào", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e(TAG, "Search failed. Code: " + response.code());
                    Toast.makeText(Admin_ProductList_Activity.this, "Tìm kiếm thất bại. Mã lỗi: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ProductModel>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Log.e(TAG, "Error searching products: " + t.getMessage());
                Toast.makeText(Admin_ProductList_Activity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}