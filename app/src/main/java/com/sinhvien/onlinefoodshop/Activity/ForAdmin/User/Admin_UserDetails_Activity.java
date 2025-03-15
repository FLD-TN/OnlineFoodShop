package com.sinhvien.onlinefoodshop.Activity.ForAdmin.User;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.sinhvien.onlinefoodshop.Adapter.UserAdapter;
import com.sinhvien.onlinefoodshop.ApiService;
import com.sinhvien.onlinefoodshop.Model.UserModel;
import com.sinhvien.onlinefoodshop.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Admin_UserDetails_Activity extends AppCompatActivity {
    private static final String TAG = "Admin_UserDetails_Activity";
    private TextView tvFullName, tvEmail, tvPhoneNumber, tvRole;
    private Button btnBack, btnEditUser, btnDelete;
    private ApiService apiService;
    private UserModel currentUser;
    private ProgressBar progressBar;
    private UserAdapter userAdapter;

    private final ActivityResultLauncher<Intent> editUserLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    UserModel updatedUser = (UserModel) result.getData().getSerializableExtra("updatedUser");
                    if (updatedUser != null) {
                        currentUser = updatedUser;
                        updateUI();
                    }
                }
            });

    private void loadUserList() {
        progressBar.setVisibility(View.VISIBLE);
        Call<List<UserModel>> call = apiService.getUsers();
        call.enqueue(new Callback<List<UserModel>>() {
            @Override
            public void onResponse(Call<List<UserModel>> call, Response<List<UserModel>> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    List<UserModel> users = response.body();
                    Log.d(TAG, "Loaded " + users.size() + " users from server");
                    userAdapter.setUserList(users);
                }
            }
            @Override
            public void onFailure(Call<List<UserModel>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Log.e(TAG, "Error loading users: " + t.getMessage());
            }
        });
    }

    private final ActivityResultLauncher<Intent> updateUserLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null){
                    UserModel updatedUser = (UserModel) result.getData().getSerializableExtra("updatedUser");
                    if (updatedUser != null){
                        loadUserList(); //reload khi update
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_user_details);

        tvFullName = findViewById(R.id.tvFullName);
        tvEmail = findViewById(R.id.tvEmail);
        tvPhoneNumber = findViewById(R.id.tvPhoneNumber);
        tvRole = findViewById(R.id.tvRole);
        btnBack = findViewById(R.id.btnBack);
        btnEditUser = findViewById(R.id.btnEditUser);
        btnDelete = findViewById(R.id.btnDelete);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getBaseUrl())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(ApiService.class);

        Intent intent = getIntent();
        currentUser = (UserModel) intent.getSerializableExtra("userDetail");

        if (currentUser != null) {
            updateUI();
        } else {
            Log.e(TAG, "Current user is null");
            Toast.makeText(this, "Không tải được thông tin người dùng", Toast.LENGTH_SHORT).show();
            finish();
        }

        btnBack.setOnClickListener(v -> {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("dataChanged", true); // Báo hiệu dữ liệu đã thay đổi
            setResult(RESULT_OK, resultIntent); // Trả kết quả về Activity trước
            finish(); // Quay lại ngay lập tức
        });


        btnEditUser.setOnClickListener(v -> {
            Intent editIntent = new Intent(this, Admin_EditUser_Activity.class);
            editIntent.putExtra("userDetail", currentUser);
            editUserLauncher.launch(editIntent);
        });
        btnDelete.setOnClickListener(v -> confirmDeleteUser());
    }


    private void updateUI() {
        tvFullName.setText("Họ tên: " + (currentUser.getFullName() != null ? currentUser.getFullName() : ""));
        tvEmail.setText("Email: " + (currentUser.getEmail() != null ? currentUser.getEmail() : ""));
        tvPhoneNumber.setText("Số điện thoại: " + (currentUser.getPhoneNumber() != null ? currentUser.getPhoneNumber() : ""));
        tvRole.setText("Vai trò: " + (currentUser.getRole() != null ? currentUser.getRole() : ""));
    }
    private void confirmDeleteUser() {

        if (currentUser == null || currentUser.getEmail() == null) {
            Toast.makeText(this, "Không thể xóa: Thông tin người dùng không hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }

        new AlertDialog.Builder(this)
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc muốn xóa người dùng " + currentUser.getEmail() + "?")
                .setPositiveButton("Có", (dialog, which) -> deleteUser())
                .setNegativeButton("Không", null)
                .show();
    }

    private void deleteUser() {
        apiService.deleteUserByEmail(currentUser.getEmail()).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "User deleted: " + currentUser.getEmail());
                    Toast.makeText(Admin_UserDetails_Activity.this, "Xóa người dùng thành công", Toast.LENGTH_SHORT).show();
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("deletedEmail", currentUser.getEmail());
                    setResult(RESULT_OK, resultIntent);
                    finish();
                } else {
                    Log.e(TAG, "Delete failed. Code: " + response.code());
                    Toast.makeText(Admin_UserDetails_Activity.this, "Xóa người dùng thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e(TAG, "Error deleting user: " + t.getMessage());
                Toast.makeText(Admin_UserDetails_Activity.this, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

// Dùng render cloud để chạy port
    private String getBaseUrl() {
        return "https://foodshop-backend-jck5.onrender.com/"; //url từ render cloud
    }

//    private String getBaseUrl() {
//        if (isEmulator()) {
//            return "http://10.0.2.2:3000/";
//        } else {
//            return "http://192.168.1.7:3000/";
//        }
//    }
//
//    private boolean isEmulator() {
//        return "generic".equalsIgnoreCase(Build.BRAND) || Build.PRODUCT.contains("sdk");
//    }
}