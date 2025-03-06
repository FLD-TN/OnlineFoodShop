package com.sinhvien.onlinefoodshop.Activity.ForAdmin.Discount;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
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

        etSearchProduct = findViewById(R.id.etSearchProduct);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        recyclerProducts = findViewById(R.id.recyclerProducts);
        btnAddDiscount = findViewById(R.id.btnAddDiscount);
        btnApplyCategoryDiscount = findViewById(R.id.btnApplyCategoryDiscount);
        recyclerProducts.setLayoutManager(new LinearLayoutManager(this));

        // Khởi tạo ApiService
        apiService = RetrofitClient.getApiService();

        // Khởi tạo adapter
        adapter = new ProductAdapter(null, this);
        recyclerProducts.setAdapter(adapter);

        // Tải danh mục và sản phẩm từ backend
        loadCategories();
        loadProducts();

        // Tìm kiếm sản phẩm
        etSearchProduct.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String searchQuery = s.toString().trim();
                if (searchQuery.isEmpty()) {
                    filterProductsByCategory(((CategoryModel) spinnerCategory.getSelectedItem()).getCategoryId());
                } else {
                    searchProductsByName(searchQuery);
                }
            }
        });

        // Nút thêm khuyến mãi cho sản phẩm
        btnAddDiscount.setOnClickListener(v -> showAddDiscountDialog(false));

        // Nút áp dụng khuyến mãi cho danh mục
        btnApplyCategoryDiscount.setOnClickListener(v -> showAddDiscountDialog(true));
    }

    private void loadCategories() {
        apiService.getCategories().enqueue(new Callback<List<CategoryModel>>() {
            @Override
            public void onResponse(Call<List<CategoryModel>> call, Response<List<CategoryModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    categoryList.clear();
                    CategoryModel allCategory = new CategoryModel();
                    allCategory.setCategoryId("all");
                    allCategory.setCategoryName("Tất cả");
                    categoryList.add(allCategory); // Thêm danh mục "Tất cả"
                    categoryList.addAll(response.body());
                    ArrayAdapter<CategoryModel> spinnerAdapter = new ArrayAdapter<>(
                            Admin_AddDiscount_Activity.this,
                            android.R.layout.simple_spinner_item,
                            categoryList
                    );
                    spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerCategory.setAdapter(spinnerAdapter);
                    spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            CategoryModel selectedCategory = (CategoryModel) parent.getItemAtPosition(position);
                            filterProductsByCategory(selectedCategory.getCategoryId());
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {}
                    });
                } else {
                    Toast.makeText(Admin_AddDiscount_Activity.this, "Không tải được danh mục", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<CategoryModel>> call, Throwable t) {
                Toast.makeText(Admin_AddDiscount_Activity.this, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
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
                    filteredProductList.clear();
                    filteredProductList.addAll(productList);
                    adapter.setProductList(filteredProductList);
                } else {
                    Toast.makeText(Admin_AddDiscount_Activity.this, "Không tải được sản phẩm", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ProductModel>> call, Throwable t) {
                Toast.makeText(Admin_AddDiscount_Activity.this, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void searchProductsByName(String name) {
        apiService.searchProductsByName(name).enqueue(new Callback<List<ProductModel>>() {
            @Override
            public void onResponse(Call<List<ProductModel>> call, Response<List<ProductModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    filteredProductList.clear();
                    filteredProductList.addAll(response.body());
                    adapter.setProductList(filteredProductList);
                } else {
                    Toast.makeText(Admin_AddDiscount_Activity.this, "Không tìm thấy sản phẩm", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ProductModel>> call, Throwable t) {
                Toast.makeText(Admin_AddDiscount_Activity.this, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void filterProductsByCategory(String categoryId) {
        filteredProductList.clear();
        if (categoryId.equals("all")) {
            filteredProductList.addAll(productList);
        } else {
            for (ProductModel product : productList) {
                if (product.getCategory().equals(categoryId)) {
                    filteredProductList.add(product);
                }
            }
        }
        String searchQuery = etSearchProduct.getText().toString().trim();
        if (searchQuery.isEmpty()) {
            adapter.setProductList(filteredProductList);
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

        if (forCategory) {
            tvProductLabel.setVisibility(View.GONE);
            spinnerProduct.setVisibility(View.GONE);
        } else {
            tvProductLabel.setVisibility(View.VISIBLE);
            spinnerProduct.setVisibility(View.VISIBLE);
            List<String> productNames = new ArrayList<>();
            for (ProductModel product : productList) {
                productNames.add(product.getProductName());
            }
            ArrayAdapter<String> productAdapter = new ArrayAdapter<>(
                    this,
                    android.R.layout.simple_spinner_item,
                    productNames
            );
            productAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerProduct.setAdapter(productAdapter);
        }

        List<String> types = new ArrayList<>();
        types.add("Theo %");
        types.add("Theo số tiền");
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                types
        );
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDiscountType.setAdapter(typeAdapter);

        new AlertDialog.Builder(this)
                .setTitle(forCategory ? "Áp dụng khuyến mãi cho danh mục" : "Thêm khuyến mãi")
                .setView(dialogView)
                .setPositiveButton("Lưu", (dialog, which) -> {
                    String valueStr = etDiscountValue.getText().toString().trim();
                    if (valueStr.isEmpty()) {
                        etDiscountValue.setError("Vui lòng nhập giá trị giảm giá");
                        return;
                    }
                    int value;
                    try {
                        value = Integer.parseInt(valueStr);
                        if (value <= 0) {
                            etDiscountValue.setError("Giá trị phải lớn hơn 0");
                            return;
                        }
                    } catch (NumberFormatException e) {
                        etDiscountValue.setError("Giá trị không hợp lệ");
                        return;
                    }

                    String type = spinnerDiscountType.getSelectedItem().toString();

                    if (forCategory) {
                        CategoryModel selectedCategory = (CategoryModel) spinnerCategory.getSelectedItem();
                        if (!selectedCategory.getCategoryId().equals("all")) {
                            for (ProductModel product : productList) {
                                if (product.getCategory().equals(selectedCategory.getCategoryId())) {
                                    if (type.equals("Theo %")) {
                                        product.setDiscount(value);
                                        product.setDiscountAmount(0);
                                    } else {
                                        product.setDiscountAmount(value);
                                        product.setDiscount(0);
                                    }
                                    updateProductOnServer(product);
                                }
                            }
                        }
                    } else {
                        int selectedProductIndex = spinnerProduct.getSelectedItemPosition();
                        ProductModel selectedProduct = productList.get(selectedProductIndex);
                        if (type.equals("Theo %")) {
                            selectedProduct.setDiscount(value);
                            selectedProduct.setDiscountAmount(0);
                        } else {
                            selectedProduct.setDiscountAmount(value);
                            selectedProduct.setDiscount(0);
                        }
                        updateProductOnServer(selectedProduct);
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void updateProductOnServer(ProductModel product) {
        apiService.updateProduct(product.getProductID(), product).enqueue(new Callback<ProductModel>() {
            @Override
            public void onResponse(Call<ProductModel> call, Response<ProductModel> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(Admin_AddDiscount_Activity.this, "Cập nhật khuyến mãi thành công", Toast.LENGTH_SHORT).show();
                    // Cập nhật lại danh sách sản phẩm
                    loadProducts();
                } else {
                    Toast.makeText(Admin_AddDiscount_Activity.this, "Cập nhật khuyến mãi thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ProductModel> call, Throwable t) {
                Toast.makeText(Admin_AddDiscount_Activity.this, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onEditDiscount(ProductModel product) {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_discount, null);
        EditText etDiscountValue = dialogView.findViewById(R.id.etDiscountValue);
        Spinner spinnerDiscountType = dialogView.findViewById(R.id.spinnerDiscountType);
        Spinner spinnerProduct = dialogView.findViewById(R.id.spinnerProduct);
        TextView tvProductLabel = dialogView.findViewById(R.id.tvProductLabel);
        tvProductLabel.setVisibility(View.GONE);
        spinnerProduct.setVisibility(View.GONE);

        if (product.getDiscount() > 0) {
            etDiscountValue.setText(String.valueOf(product.getDiscount()));
            spinnerDiscountType.setSelection(0);
        } else if (product.getDiscountAmount() > 0) {
            etDiscountValue.setText(String.valueOf(product.getDiscountAmount()));
            spinnerDiscountType.setSelection(1);
        }

        List<String> types = new ArrayList<>();
        types.add("Theo %");
        types.add("Theo số tiền");
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                types
        );
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDiscountType.setAdapter(typeAdapter);

        new AlertDialog.Builder(this)
                .setTitle("Chỉnh sửa khuyến mãi")
                .setView(dialogView)
                .setPositiveButton("Lưu", (dialog, which) -> {
                    String valueStr = etDiscountValue.getText().toString().trim();
                    if (valueStr.isEmpty()) {
                        etDiscountValue.setError("Vui lòng nhập giá trị giảm giá");
                        return;
                    }
                    int value;
                    try {
                        value = Integer.parseInt(valueStr);
                        if (value <= 0) {
                            etDiscountValue.setError("Giá trị phải lớn hơn 0");
                            return;
                        }
                    } catch (NumberFormatException e) {
                        etDiscountValue.setError("Giá trị không hợp lệ");
                        return;
                    }

                    String type = spinnerDiscountType.getSelectedItem().toString();
                    if (type.equals("Theo %")) {
                        product.setDiscount(value);
                        product.setDiscountAmount(0);
                    } else {
                        product.setDiscountAmount(value);
                        product.setDiscount(0);
                    }
                    updateProductOnServer(product);
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
}