package com.sinhvien.onlinefoodshop.Activity.ForUser;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.sinhvien.onlinefoodshop.Adapter.UserProductAdapter;
import com.sinhvien.onlinefoodshop.ApiService;
import com.sinhvien.onlinefoodshop.Model.CategoryModel;
import com.sinhvien.onlinefoodshop.Model.ProductModel;
import com.sinhvien.onlinefoodshop.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import java.util.ArrayList;
import java.util.List;

public class ProductListActivity extends AppCompatActivity {
    private static final String TAG = "ProductListActivity";
    private RecyclerView rvProductList;
    private EditText etSearchProduct;
    private Spinner spinnerCategory;
    private ProgressBar progressBar;
    private UserProductAdapter productAdapter;
    private ApiService apiService;
    ImageView btnBack;
    private List<ProductModel> fullProductList = new ArrayList<>();
    private final String BASE_URL = "https://foodshop-backend-jck5.onrender.com/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        rvProductList = findViewById(R.id.rvProductList);
        etSearchProduct = findViewById(R.id.etSearchProduct);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        progressBar = findViewById(R.id.progressBar);
        btnBack = findViewById(R.id.btnBack);

        productAdapter = new UserProductAdapter();
        rvProductList.setLayoutManager(new GridLayoutManager(this, 2));
        rvProductList.setAdapter(productAdapter);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(ApiService.class);

        loadProductList();

        etSearchProduct.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String query = s.toString().trim();
                if (query.isEmpty()) {
                    filterByCategory(spinnerCategory.getSelectedItem().toString());
                } else {
                    searchProducts(query);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        loadCategories();

        btnBack.setOnClickListener(v -> finish());
    }

    private void loadProductList() {
        progressBar.setVisibility(View.VISIBLE);
        Call<List<ProductModel>> call = apiService.getProducts();
        call.enqueue(new Callback<List<ProductModel>>() {
            @Override
            public void onResponse(Call<List<ProductModel>> call, Response<List<ProductModel>> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    fullProductList = response.body();
                    Log.d(TAG, "Loaded " + fullProductList.size() + " products");
                    productAdapter.setProductList(fullProductList);
                    if (fullProductList.isEmpty()) {
                        Toast.makeText(ProductListActivity.this, "Danh sách sản phẩm rỗng", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e(TAG, "Failed to load products. Code: " + response.code());
                    Toast.makeText(ProductListActivity.this, "Không thể tải dữ liệu", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ProductModel>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Log.e(TAG, "Error loading products: " + t.getMessage());
                Toast.makeText(ProductListActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
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
                    productAdapter.setProductList(products);
                } else {
                    Toast.makeText(ProductListActivity.this, "Tìm kiếm thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ProductModel>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(ProductListActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadCategories() {
        Call<List<CategoryModel>> call = apiService.getCategories();
        call.enqueue(new Callback<List<CategoryModel>>() {
            @Override
            public void onResponse(Call<List<CategoryModel>> call, Response<List<CategoryModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<CategoryModel> categoryModels = response.body();
                    List<String> categories = new ArrayList<>();
                    categories.add("Tất cả"); // Thêm "Tất cả" vào đầu danh sách
                    for (CategoryModel categoryModel : categoryModels) {
                        categories.add(categoryModel.getCategoryName());
                    }
                    setupCategorySpinner(categories);
                } else {
                    Toast.makeText(ProductListActivity.this, "Không thể tải danh mục", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<CategoryModel>> call, Throwable t) {
                Toast.makeText(ProductListActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void setupCategorySpinner(List<String> categories) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);
        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedCategory = parent.getItemAtPosition(position).toString();
                filterByCategory(selectedCategory);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }
    private void filterByCategory(String category) {
        if (category.equals("Tất cả")) {
            productAdapter.setProductList(fullProductList);
        } else {
            List<ProductModel> filteredList = new ArrayList<>();
            for (ProductModel product : fullProductList) {
                if (product.getCategory() != null && product.getCategory().equalsIgnoreCase(category)) {
                    filteredList.add(product);
                }
            }
            productAdapter.setProductList(filteredList);
        }
    }
}