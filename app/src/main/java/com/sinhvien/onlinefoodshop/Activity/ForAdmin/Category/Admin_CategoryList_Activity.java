package com.sinhvien.onlinefoodshop.Activity.ForAdmin.Category;

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
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.airbnb.lottie.LottieAnimationView;
import com.sinhvien.onlinefoodshop.Adapter.AdminCategoryAdapter;
import com.sinhvien.onlinefoodshop.ApiService;
import com.sinhvien.onlinefoodshop.Model.CategoryModel;
import com.sinhvien.onlinefoodshop.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import java.util.List;

public class Admin_CategoryList_Activity extends AppCompatActivity {
    private static final String TAG = "Admin_CategoryList_Activity";
    private RecyclerView rvCategoryList;
    private AdminCategoryAdapter categoryAdapter;
    private EditText etSearchCategory;
    private LottieAnimationView loadingAnimation;
    private View loadingOverlay;
    private Button btnAddCategory;
    private ApiService apiService;
    private final String BASE_URL = "https://foodshop-backend-jck5.onrender.com/";

    private final ActivityResultLauncher<Intent> categoryDetailsLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    String deletedId = result.getData().getStringExtra("deletedId");
                    boolean dataChanged = result.getData().getBooleanExtra("dataChanged", false);
                    if (deletedId != null || dataChanged) {
                        loadCategoryList();
                    }
                }
            });

    private final ActivityResultLauncher<Intent> addCategoryLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    CategoryModel newCategory = (CategoryModel) result.getData().getSerializableExtra("newCategory");
                    if (newCategory != null) {
                        loadCategoryList();
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category_list);

        rvCategoryList = findViewById(R.id.rvCategoryList);
        etSearchCategory = findViewById(R.id.etSearchCategory);
        btnAddCategory = findViewById(R.id.btnAddCategory);
        loadingOverlay = findViewById(R.id.loadingOverlay);
        loadingAnimation = findViewById(R.id.loadingAnimation);

        categoryAdapter = new AdminCategoryAdapter(categoryDetailsLauncher);
        rvCategoryList.setLayoutManager(new LinearLayoutManager(this));
        rvCategoryList.setAdapter(categoryAdapter);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(ApiService.class);

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            loadingOverlay.setVisibility(View.GONE);
            loadingAnimation.setVisibility(View.GONE);
            loadingAnimation.cancelAnimation();
            loadCategoryList();
        }, 2000);

        btnAddCategory.setOnClickListener(v -> {
            Intent intent = new Intent(this, Admin_AddCategory_Activity.class);
            addCategoryLauncher.launch(intent);
        });

        etSearchCategory.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String query = s.toString().trim();
                if (query.isEmpty()) {
                    loadCategoryList();
                } else {
                    searchCategories(query);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void loadCategoryList() {
        Call<List<CategoryModel>> call = apiService.getCategories();
        call.enqueue(new Callback<List<CategoryModel>>() {
            @Override
            public void onResponse(Call<List<CategoryModel>> call, Response<List<CategoryModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<CategoryModel> categories = response.body();
                    Log.d(TAG, "Loaded " + categories.size() + " categories from server");
                    categoryAdapter.setCategoryList(categories);
                    if (categories.isEmpty()) {
                        Toast.makeText(Admin_CategoryList_Activity.this, "Danh sách loại sản phẩm rỗng", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e(TAG, "Failed to load categories. Code: " + response.code());
                    Toast.makeText(Admin_CategoryList_Activity.this, "Không thể lấy dữ liệu. Mã lỗi: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<CategoryModel>> call, Throwable t) {
                Log.e(TAG, "Error loading categories: " + t.getMessage());
                Toast.makeText(Admin_CategoryList_Activity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void searchCategories(String query) {
        Call<List<CategoryModel>> call = apiService.searchCategoriesByName(query);
        call.enqueue(new Callback<List<CategoryModel>>() {
            @Override
            public void onResponse(Call<List<CategoryModel>> call, Response<List<CategoryModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<CategoryModel> categories = response.body();
                    Log.d(TAG, "Search found " + categories.size() + " categories for query: " + query);
                    categoryAdapter.setCategoryList(categories);
                } else {
                    Log.e(TAG, "Search failed. Code: " + response.code());
                    Toast.makeText(Admin_CategoryList_Activity.this, "Tìm kiếm thất bại. Mã lỗi: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<CategoryModel>> call, Throwable t) {
                Log.e(TAG, "Error searching categories: " + t.getMessage());
                Toast.makeText(Admin_CategoryList_Activity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}