package com.sinhvien.onlinefoodshop.Activity.ForAdmin.User;

import android.content.Intent;
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

public class Admin_AddUser_Activity extends AppCompatActivity {
    private static final String TAG = "Admin_AddUser_Activity";
    private EditText edtFullName, edtEmail, edtPhoneNumber, edtRole, edtPassword;
    private Button btnSave, btnCancel;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_user);

        edtFullName = findViewById(R.id.edtFullName);
        edtEmail = findViewById(R.id.edtEmail);
        edtPhoneNumber = findViewById(R.id.edtPhoneNumber);
        edtRole = findViewById(R.id.edtRole);
        edtPassword = findViewById(R.id.edtPassword);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getBaseUrl())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(ApiService.class);

        btnSave.setOnClickListener(v -> addUser());
        btnCancel.setOnClickListener(v -> finish());
    }

    private void addUser() {
        String fullName = edtFullName.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String phoneNumber = edtPhoneNumber.getText().toString().trim();
        String role = edtRole.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        if (fullName.isEmpty() || email.isEmpty() || phoneNumber.isEmpty() || role.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        UserModel newUser = new UserModel(email, fullName, phoneNumber, role,password);
        newUser.setPassword(password);

        apiService.addUser(newUser).enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "User added successfully: " + email);
                    Toast.makeText(Admin_AddUser_Activity.this, "Thêm người dùng thành công", Toast.LENGTH_SHORT).show();
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("newUser", response.body());
                    setResult(RESULT_OK, resultIntent);
                    finish();
                } else {
                    Log.e(TAG, "Add user failed. Code: " + response.code());
                    Toast.makeText(Admin_AddUser_Activity.this, "Thêm người dùng thất bại. Mã lỗi: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                Log.e(TAG, "Error adding user: " + t.getMessage());
                Toast.makeText(Admin_AddUser_Activity.this, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
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