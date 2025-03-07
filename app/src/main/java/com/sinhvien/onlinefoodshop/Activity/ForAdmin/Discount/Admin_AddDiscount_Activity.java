package com.sinhvien.onlinefoodshop.Activity.ForAdmin.Discount;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.sinhvien.onlinefoodshop.ApiService;
import com.sinhvien.onlinefoodshop.Adapter.ProductAdapter;
import com.sinhvien.onlinefoodshop.Model.CategoryModel;
import com.sinhvien.onlinefoodshop.Model.ProductModel;
import com.sinhvien.onlinefoodshop.R;
import com.sinhvien.onlinefoodshop.RetrofitClient;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Admin_AddDiscount_Activity extends AppCompatActivity implements ProductAdapter.OnDiscountActionListener {
    private static final String TAG = "Admin_AddDiscount_Activity";
    private EditText etSearchProduct;
    private Spinner spinnerCategory;
    private RecyclerView recyclerProducts;
    private Button btnAddDiscount, btnApplyCategoryDiscount;
    private ProductAdapter adapter;
    private List<ProductModel> productList = new ArrayList<>();
    private List<ProductModel> filteredProductList = new ArrayList<>();
    private List<CategoryModel> categoryList = new ArrayList<>();
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_discount);

        // Ánh xạ giao diện
        etSearchProduct = findViewById(R.id.etSearchProduct);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        recyclerProducts = findViewById(R.id.recyclerProducts);
        btnAddDiscount = findViewById(R.id.btnAddDiscount);
        btnApplyCategoryDiscount = findViewById(R.id.btnApplyCategoryDiscount);

        // Cấu hình RecyclerView
        recyclerProducts.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ProductAdapter(null, this);
        recyclerProducts.setAdapter(adapter);

        // Khởi tạo ApiService
        apiService = RetrofitClient.getApiService();

        // Tải dữ liệu
        loadCategories();
        loadProducts();

        // Sự kiện tìm kiếm
        etSearchProduct.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                filterProducts(s.toString().trim());
            }
        });

        // Sự kiện nút
        btnAddDiscount.setOnClickListener(v -> showAddDiscountDialog(false));
        btnApplyCategoryDiscount.setOnClickListener(v -> showAddDiscountDialog(true));
    }

    private void loadCategories() {
        apiService.getCategories().enqueue(new Callback<List<CategoryModel>>() {
            @Override
            public void onResponse(Call<List<CategoryModel>> call, Response<List<CategoryModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    categoryList.clear();

                    // Thêm danh mục "Tất cả"
                    CategoryModel allCategory = new CategoryModel();
                    allCategory.setCategoryId("all");
                    allCategory.setCategoryName("Tất cả");
                    categoryList.add(allCategory);

                    // Thêm các danh mục từ server
                    categoryList.addAll(response.body());
                    for (CategoryModel category : response.body()) {
                        if (category.getCategoryName().isEmpty()) {
                            category.setCategoryName("Danh mục " + category.getCategoryId().substring(0, 8) + "...");
                        }
                    }

                    // Tùy chỉnh ArrayAdapter để hiển thị chỉ categoryName
                    ArrayAdapter<CategoryModel> spinnerAdapter = new ArrayAdapter<CategoryModel>(
                            Admin_AddDiscount_Activity.this, android.R.layout.simple_spinner_item, categoryList) {
                        @Override
                        public View getView(int position, View convertView, ViewGroup parent) {
                            View view = super.getView(position, convertView, parent);
                            ((TextView) view.findViewById(android.R.id.text1)).setText(categoryList.get(position).getCategoryName());
                            return view;
                        }

                        @Override
                        public View getDropDownView(int position, View convertView, ViewGroup parent) {
                            View view = super.getDropDownView(position, convertView, parent);
                            ((TextView) view.findViewById(android.R.id.text1)).setText(categoryList.get(position).getCategoryName());
                            return view;
                        }
                    };
                    spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerCategory.setAdapter(spinnerAdapter);

                    // Sự kiện chọn danh mục
                    spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            filterProductsByCategory(categoryList.get(position).getCategoryName());
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {}
                    });

                    // Khởi tạo lần đầu
                    filterProductsByCategory("Tất cả");
                } else {
                    showToast("Không tải được danh mục");
                }
            }

            @Override
            public void onFailure(Call<List<CategoryModel>> call, Throwable t) {
                showToast("Lỗi tải danh mục: " + t.getMessage());
            }
        });
    }

    private void loadProducts() {
        apiService.getProducts().enqueue(new Callback<List<ProductModel>>() {
            @Override
            public void onResponse(Call<List<ProductModel>> call, Response<List<ProductModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    productList.clear();
                    productList.addAll(response.body());

                    CategoryModel selectedCategory = (CategoryModel) spinnerCategory.getSelectedItem();
                    filterProductsByCategory(selectedCategory != null ? selectedCategory.getCategoryName() : "Tất cả");
                } else {
                    showToast("Không tải được sản phẩm");
                }
            }

            @Override
            public void onFailure(Call<List<ProductModel>> call, Throwable t) {
                showToast("Lỗi tải sản phẩm: " + t.getMessage());
            }
        });
    }

    private void searchProductsByName(String name) {
        if (productList.isEmpty()) {
            showToast("Danh sách sản phẩm trống");
            filteredProductList.clear();
            adapter.setProductList(filteredProductList);
            return;
        }

        apiService.searchProductsByName(name).enqueue(new Callback<List<ProductModel>>() {
            @Override
            public void onResponse(Call<List<ProductModel>> call, Response<List<ProductModel>> response) {
                filteredProductList.clear();
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    filteredProductList.addAll(response.body());
                } else {
                    searchLocally(name);
                }
                adapter.setProductList(filteredProductList);
            }

            @Override
            public void onFailure(Call<List<ProductModel>> call, Throwable t) {
                showToast("Lỗi tìm kiếm qua API: " + t.getMessage());
                filteredProductList.clear();
                searchLocally(name);
                adapter.setProductList(filteredProductList);
            }
        });
    }

    private void searchLocally(String name) {
        String searchLower = name.toLowerCase();
        for (ProductModel product : productList) {
            if (product.getProductName() != null && product.getProductName().toLowerCase().contains(searchLower)) {
                filteredProductList.add(product);
            }
        }
        if (filteredProductList.isEmpty()) {
            showToast("Không tìm thấy sản phẩm với từ khóa: " + name);
        } else {
            showToast("Tìm kiếm cục bộ: Đã tìm thấy " + filteredProductList.size() + " sản phẩm");
        }
    }

    private void filterProductsByCategory(String categoryName) {
        filteredProductList.clear();
        if (categoryName.equals("Tất cả")) {
            filteredProductList.addAll(productList);
        } else {
            for (ProductModel product : productList) {
                if (product.getCategory() != null && product.getCategory().trim().toLowerCase().equals(categoryName.trim().toLowerCase())) {
                    filteredProductList.add(product);
                }
            }
            if (filteredProductList.isEmpty()) {
                showToast("Không tìm thấy sản phẩm cho danh mục: " + categoryName);
            }
        }
        adapter.setProductList(filteredProductList);
    }

    private void filterProducts(String searchQuery) {
        if (searchQuery.isEmpty()) {
            CategoryModel selectedCategory = (CategoryModel) spinnerCategory.getSelectedItem();
            filterProductsByCategory(selectedCategory != null ? selectedCategory.getCategoryName() : "Tất cả");
        } else {
            searchProductsByName(searchQuery);
        }
    }

    private void showAddDiscountDialog(boolean forCategory) {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_discount, null);
        EditText etDiscountValue = dialogView.findViewById(R.id.etDiscountValue);
        Spinner spinnerDiscountType = dialogView.findViewById(R.id.spinnerDiscountType);
        Spinner spinnerProduct = dialogView.findViewById(R.id.spinnerProduct);
        TextView tvProductLabel = dialogView.findViewById(R.id.tvProductLabel);

        setupDiscountTypeSpinner(spinnerDiscountType);
        if (forCategory) {
            tvProductLabel.setVisibility(View.GONE);
            spinnerProduct.setVisibility(View.GONE);
        } else {
            setupProductSpinner(spinnerProduct);
        }

        new AlertDialog.Builder(this)
                .setTitle(forCategory ? "Áp dụng khuyến mãi cho danh mục" : "Thêm khuyến mãi")
                .setView(dialogView)
                .setPositiveButton("Lưu", (dialog, which) -> {
                    int discountValue = validateDiscountValue(etDiscountValue);
                    if (discountValue == -1) return;

                    String discountType = spinnerDiscountType.getSelectedItem().toString();
                    if (forCategory) {
                        applyDiscountToCategory(discountType, discountValue);
                    } else {
                        applyDiscountToProduct(spinnerProduct, discountType, discountValue);
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void setupDiscountTypeSpinner(Spinner spinnerDiscountType) {
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,
                new String[]{"Theo %", "Theo số tiền"});
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDiscountType.setAdapter(typeAdapter);
    }

    private void setupProductSpinner(Spinner spinnerProduct) {
        List<String> productNames = new ArrayList<>();
        for (ProductModel product : productList) {
            productNames.add(product.getProductName());
        }
        ArrayAdapter<String> productAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, productNames);
        productAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerProduct.setAdapter(productAdapter);
    }

    private int validateDiscountValue(EditText etDiscountValue) {
        String valueStr = etDiscountValue.getText().toString().trim();
        if (valueStr.isEmpty()) {
            etDiscountValue.setError("Vui lòng nhập giá trị giảm giá");
            return -1;
        }
        try {
            int value = Integer.parseInt(valueStr);
            if (value <= 0) {
                etDiscountValue.setError("Giá trị phải lớn hơn 0");
                return -1;
            }
            return value;
        } catch (NumberFormatException e) {
            etDiscountValue.setError("Giá trị không hợp lệ");
            return -1;
        }
    }

    private void applyDiscountToCategory(String discountType, int discountValue) {
        CategoryModel selectedCategory = (CategoryModel) spinnerCategory.getSelectedItem();
        if (selectedCategory == null || selectedCategory.getCategoryId().equals("all")) return;

        for (ProductModel product : productList) {
            if (product.getCategory() != null && product.getCategory().trim().toLowerCase().equals(selectedCategory.getCategoryName().trim().toLowerCase())) {
                updateProductDiscount(product, discountType, discountValue);
            }
        }
    }

    private void applyDiscountToProduct(Spinner spinnerProduct, String discountType, int discountValue) {
        ProductModel selectedProduct = productList.get(spinnerProduct.getSelectedItemPosition());
        updateProductDiscount(selectedProduct, discountType, discountValue);
    }

    private void updateProductDiscount(ProductModel product, String discountType, int discountValue) {
        if (discountType.equals("Theo %")) {
            product.setDiscount(discountValue);
            product.setDiscountAmount(0);
        } else {
            product.setDiscountAmount(discountValue);
            product.setDiscount(0);
        }
        updateProductOnServer(product);
    }

    private void updateProductOnServer(ProductModel product) {
        apiService.updateProduct(product.getProductID(), product).enqueue(new Callback<ProductModel>() {
            @Override
            public void onResponse(Call<ProductModel> call, Response<ProductModel> response) {
                showToast(response.isSuccessful() ? "Cập nhật khuyến mãi thành công" : "Cập nhật khuyến mãi thất bại");
                if (response.isSuccessful()) loadProducts();
            }

            @Override
            public void onFailure(Call<ProductModel> call, Throwable t) {
                showToast("Lỗi cập nhật: " + t.getMessage());
            }
        });
    }

    @Override
    public void onEditDiscount(ProductModel product) {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_discount, null);
        EditText etDiscountValue = dialogView.findViewById(R.id.etDiscountValue);
        Spinner spinnerDiscountType = dialogView.findViewById(R.id.spinnerDiscountType);
        dialogView.findViewById(R.id.tvProductLabel).setVisibility(View.GONE);
        dialogView.findViewById(R.id.spinnerProduct).setVisibility(View.GONE);

        setupDiscountTypeSpinner(spinnerDiscountType);
        if (product.getDiscount() > 0) {
            etDiscountValue.setText(String.valueOf(product.getDiscount()));
            spinnerDiscountType.setSelection(0);
        } else if (product.getDiscountAmount() > 0) {
            etDiscountValue.setText(String.valueOf(product.getDiscountAmount()));
            spinnerDiscountType.setSelection(1);
        }

        new AlertDialog.Builder(this)
                .setTitle("Chỉnh sửa khuyến mãi")
                .setView(dialogView)
                .setPositiveButton("Lưu", (dialog, which) -> {
                    int discountValue = validateDiscountValue(etDiscountValue);
                    if (discountValue != -1) {
                        updateProductDiscount(product, spinnerDiscountType.getSelectedItem().toString(), discountValue);
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    @Override
    public void onRemoveDiscount(ProductModel product) {
        product.setDiscount(0);
        product.setDiscountAmount(0);
        updateProductOnServer(product);
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}