package com.sinhvien.onlinefoodshop.Activity;

import android.content.DialogInterface;
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
import com.sinhvien.onlinefoodshop.Model.CategoryModel;
import com.sinhvien.onlinefoodshop.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Admin_CategoryDetails_Activity extends AppCompatActivity {
    private static final String TAG = "Admin_CategoryDetails_Activity";
    private TextView tvCategoryName;
        private ImageView ivCategoryIcon;
    private Button btnBack, btnEditCategory, btnDelete;
    private ProgressBar progressBar;
    private ApiService apiService;
    private CategoryModel currentCategory;

    private final ActivityResultLauncher<Intent> editCategoryLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    CategoryModel updatedCategory = (CategoryModel) result.getData().getSerializableExtra("updatedCategory");
                    if (updatedCategory != null) {
                        currentCategory = updatedCategory;
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
        setContentView(R.layout.activity_admin_category_details);

        tvCategoryName = findViewById(R.id.tvCategoryName);
        ivCategoryIcon = findViewById(R.id.ivCategoryIcon);
        btnBack = findViewById(R.id.btnBack);
        btnEditCategory = findViewById(R.id.btnEditCategory);
        btnDelete = findViewById(R.id.btnDelete);
        progressBar = findViewById(R.id.progressBar);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://foodshop-backend-jck5.onrender.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(ApiService.class);

        Intent intent = getIntent();
        currentCategory = (CategoryModel) intent.getSerializableExtra("categoryDetail");

        if (currentCategory != null && currentCategory.getCategoryId() != null) {
            updateUI();
        } else {
            Log.e(TAG, "Current category is null or missing categoryId");
            Toast.makeText(this, "Không tải được thông tin loại sản phẩm", Toast.LENGTH_SHORT).show();
            finish();
        }

        btnBack.setOnClickListener(v -> {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("dataChanged", true);
            setResult(RESULT_OK, resultIntent);
            finish();
        });

        btnEditCategory.setOnClickListener(v -> {
            Intent editIntent = new Intent(this, Admin_EditCategory_Activity.class);
            editIntent.putExtra("categoryDetail", currentCategory);
            editCategoryLauncher.launch(editIntent);
        });

        btnDelete.setOnClickListener(v -> confirmDeleteCategory());
    }

    private void updateUI() {
        tvCategoryName.setText("Tên loại sản phẩm: " + (currentCategory.getCategoryName() != null ? currentCategory.getCategoryName() : "Không có tên"));

        String iconUrl = currentCategory.getCategoryIcon();
        if (iconUrl != null && !iconUrl.isEmpty()) {
            progressBar.setVisibility(View.VISIBLE); // Hiển thị ProgressBar khi bắt đầu tải ảnh
            Picasso.get()
                    .load(iconUrl)
                    .placeholder(R.drawable.placeholder_image)
                    .error(R.drawable.placeholder_image)
                    .into(ivCategoryIcon, new Callback() {
                        @Override
                        public void onSuccess() {
                            progressBar.setVisibility(View.GONE); // Ẩn ProgressBar khi tải thành công
                        }

                        @Override
                        public void onError(Exception e) {
                            progressBar.setVisibility(View.GONE); // Ẩn ProgressBar khi tải thất bại
                            Log.e(TAG, "Error loading image: " + e.getMessage());
                            Toast.makeText(Admin_CategoryDetails_Activity.this, "Không tải được ảnh", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            ivCategoryIcon.setImageResource(R.drawable.placeholder_image);
            progressBar.setVisibility(View.GONE); // Ẩn ProgressBar nếu không có URL
        }
    }

    private void confirmDeleteCategory() {
        if (currentCategory == null || currentCategory.getCategoryId() == null) {
            Toast.makeText(this, "Không thể xóa: Thông tin loại sản phẩm không hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }

        new AlertDialog.Builder(this)
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc muốn xóa loại sản phẩm " + currentCategory.getCategoryName() + "?")
                .setPositiveButton("Có", (dialog, which) -> deleteCategory())
                .setNegativeButton("Không", null)
                .show();
    }

    private void deleteCategory() {
        progressBar.setVisibility(View.VISIBLE); // Hiển thị ProgressBar khi bắt đầu xóa
        apiService.deleteCategory(currentCategory.getCategoryId()).enqueue(new retrofit2.Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    Log.d(TAG, "Category deleted: " + currentCategory.getCategoryId());
                    Toast.makeText(Admin_CategoryDetails_Activity.this, "Xóa loại sản phẩm thành công", Toast.LENGTH_SHORT).show();
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("deletedId", currentCategory.getCategoryId());
                    setResult(RESULT_OK, resultIntent);
                    finish();
                } else {
                    Log.e(TAG, "Delete failed. Code: " + response.code());
                    Toast.makeText(Admin_CategoryDetails_Activity.this, "Xóa loại sản phẩm thất bại. Mã lỗi: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Log.e(TAG, "Error deleting category: " + t.getMessage());
                Toast.makeText(Admin_CategoryDetails_Activity.this, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}