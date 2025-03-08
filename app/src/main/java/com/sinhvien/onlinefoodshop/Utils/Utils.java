// File: Utils.java
package com.sinhvien.onlinefoodshop.Utils;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.client.http.FileContent;
import com.google.api.services.drive.model.Permission;
import com.sinhvien.onlinefoodshop.Model.CategoryModel;
import com.sinhvien.onlinefoodshop.ApiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Utils {
    private static final String TAG = "Utils";

    public static void loadCategories(Context context, ApiService apiService, Spinner spinnerCategory) {
        apiService.getCategories().enqueue(new Callback<List<CategoryModel>>() {
            @Override
            public void onResponse(Call<List<CategoryModel>> call, Response<List<CategoryModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<CategoryModel> categoryList = response.body();
                    List<String> categoryNames = new ArrayList<>();
                    for (CategoryModel category : categoryList) {
                        categoryNames.add(category.getCategoryName());
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(context,
                            android.R.layout.simple_spinner_item, categoryNames);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerCategory.setAdapter(adapter);
                } else {
                    Log.e(TAG, "Failed to load categories. Code: " + response.code());
                    Toast.makeText(context, "Không thể tải danh mục", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<CategoryModel>> call, Throwable t) {
                Log.e(TAG, "Error loading categories: " + t.getMessage());
                Toast.makeText(context, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static java.io.File uriToFile(Context context, Uri uri) throws Exception {
        java.io.File file = new java.io.File(context.getCacheDir(), "temp_image_" + System.currentTimeMillis() + ".png");
        try (InputStream inputStream = context.getContentResolver().openInputStream(uri);
             FileOutputStream outputStream = new FileOutputStream(file)) {
            byte[] buffer = new byte[1024];
            int len;
            while ((len = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);
            }
        }
        return file;
    }

    public static void setFilePublic(Drive driveService, String fileId) {
        try {
            Permission permission = new Permission();
            permission.setType("anyone");
            permission.setRole("reader");

            driveService.permissions().create(fileId, permission)
                    .setFields("id")
                    .execute();

            Log.d(TAG, "File permission set to public: " + fileId);
        } catch (Exception e) {
            Log.e(TAG, "Error setting file permission: " + e.getMessage(), e);
        }
    }


    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void setupSpinner(Context context, Spinner spinner, String[] items) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    public static Retrofit getRetrofitInstance(String baseUrl) {
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

}