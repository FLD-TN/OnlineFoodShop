package com.sinhvien.onlinefoodshop.Activity.ForUser;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.firestore.auth.User;
import com.sinhvien.onlinefoodshop.Activity.ForAdmin.User.Admin_EditUser_Activity;
import com.sinhvien.onlinefoodshop.ApiService;
import com.sinhvien.onlinefoodshop.Model.UserModel;
import com.sinhvien.onlinefoodshop.R;
import com.sinhvien.onlinefoodshop.RetrofitClient;
import com.sinhvien.onlinefoodshop.fragment.UserInfomationFragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditUserInfoActivity extends AppCompatActivity {

    private EditText edtFullName, edtEmail, edtPhoneNumber,edtPassWord;
    private Button btnSave, btnCancel;
    private SharedPreferences userPrefs;
    private ApiService apiService;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_edit_user_info);

        edtFullName = findViewById(R.id.edtFullName);
        edtEmail = findViewById(R.id.edtEmail);
        edtPhoneNumber = findViewById(R.id.edtPhoneNumber);
        edtPassWord = findViewById(R.id.edtPassWord);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);

        userPrefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        apiService = RetrofitClient.getApiService();
        loadUserInfo();

        btnSave.setOnClickListener(v -> saveUserInfo());
        btnCancel.setOnClickListener(v -> finish());
    }

    private void loadUserInfo() {
        String fullName = userPrefs.getString("fullName", "Anonymous");
        String email = userPrefs.getString("email", "");
        String phoneNumber = userPrefs.getString("phoneNumber", "");
        String passWord = userPrefs.getString("password","");



        edtFullName.setText(fullName);
        edtEmail.setText(email);
        edtPhoneNumber.setText(phoneNumber);
        edtPassWord.setText(passWord);
        edtEmail.setEnabled(false);
    }

    private void saveUserInfo()
    {
        String fullName = edtFullName.getText().toString().trim();
        String phoneNumber = edtPhoneNumber.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String passWord = edtPassWord.getText().toString().trim();

        //Kiểm tra dữ liệu
        if(fullName.isEmpty() || phoneNumber.isEmpty())
        {
           Toast.makeText(this,"Vui lòng điền đủ thông tin", Toast.LENGTH_SHORT).show();
           return;
        }

        //Cập nhật ShareReferences
        SharedPreferences.Editor editor = userPrefs.edit();
        editor.putString("fullName", fullName);
        editor.putString("email", email);
        editor.putString("phoneNumber", phoneNumber);
        editor.putString("password",passWord);
        editor.apply();

        // Tạo đối tượng UserModel để gửi lên API
        UserModel updatedUser = new UserModel();
        updatedUser.setEmail(email);
        updatedUser.setFullName(fullName);
        updatedUser.setPhoneNumber(phoneNumber);
        updatedUser.setRole(userPrefs.getString("role", "user")); // Giữ nguyên role từ trước
        updatedUser.setPassword(passWord);

        // Gửi request cập nhật lên MongoDB qua API
        apiService.updateUserByEmail(email, updatedUser).enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(EditUserInfoActivity.this,"Cập nhật thành công", Toast.LENGTH_SHORT).show();
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("updatedUser", response.body());
                    setResult(RESULT_OK, resultIntent);
                    finish();
                } else {
                    Toast.makeText(EditUserInfoActivity.this, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) { // Sửa từ Call<Void> thành Call<UserModel>
                Log.e(TAG, "Error updating user: " + t.getMessage());
                Toast.makeText(EditUserInfoActivity.this, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

}