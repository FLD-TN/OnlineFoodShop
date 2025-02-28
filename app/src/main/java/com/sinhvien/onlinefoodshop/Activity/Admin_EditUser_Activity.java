package com.sinhvien.onlinefoodshop.Activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.sinhvien.onlinefoodshop.ApiService;
import com.sinhvien.onlinefoodshop.Model.UserModel;
import com.sinhvien.onlinefoodshop.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Admin_EditUser_Activity extends AppCompatActivity {
    private static final String TAG = "Admin_EditUser_Activity";
    private EditText edtFullName, edtEmail, edtPhoneNumber, edtRole;
    private Button btnSave, btnCancel;
    private ApiService apiService;
    private UserModel currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_edit_user);

        edtFullName = findViewById(R.id.edtFullName);
        edtEmail = findViewById(R.id.edtEmail);
        edtPhoneNumber = findViewById(R.id.edtPhoneNumber);
        edtRole = findViewById(R.id.edtRole);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getBaseUrl())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(ApiService.class);

        Intent intent = getIntent();
        currentUser = (UserModel) intent.getSerializableExtra("userDetail");

        if (currentUser != null) {
            edtFullName.setText(currentUser.getFullName());
            edtEmail.setText(currentUser.getEmail());
            edtPhoneNumber.setText(currentUser.getPhoneNumber());
            edtRole.setText(currentUser.getRole());
        } else {
            Log.e(TAG, "Current user is null");
            Toast.makeText(this, "Không tải được thông tin người dùng", Toast.LENGTH_SHORT).show();
            finish();
        }

        btnSave.setOnClickListener(v -> saveUserChanges());
        btnCancel.setOnClickListener(v -> finish());
    }

    private void saveUserChanges() {
        if (currentUser == null) {
            Toast.makeText(this, "Thông tin người dùng không hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }

        String fullName = edtFullName.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String phoneNumber = edtPhoneNumber.getText().toString().trim();
        String role = edtRole.getText().toString().trim();

        if (fullName.isEmpty() || email.isEmpty() || phoneNumber.isEmpty() || role.isEmpty()) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        String originalEmail = currentUser.getEmail();
        currentUser.setFullName(fullName);
        currentUser.setEmail(email); // Cập nhật email mới
        currentUser.setPhoneNumber(phoneNumber);
        currentUser.setRole(role);

        apiService.updateUserByEmail(originalEmail, currentUser).enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "User updated successfully: " + email);
                    Toast.makeText(Admin_EditUser_Activity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("updatedUser", response.body());
                    setResult(RESULT_OK, resultIntent);
                    finish();
                } else {
                    Log.e(TAG, "Update failed. Code: " + response.code());
                    Toast.makeText(Admin_EditUser_Activity.this, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) { // Sửa từ Call<Void> thành Call<UserModel>
                Log.e(TAG, "Error updating user: " + t.getMessage());
                Toast.makeText(Admin_EditUser_Activity.this, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


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