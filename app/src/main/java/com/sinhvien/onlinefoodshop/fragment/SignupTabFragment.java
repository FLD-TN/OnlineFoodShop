package com.sinhvien.onlinefoodshop.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.fragment.app.Fragment;

import com.sinhvien.onlinefoodshop.Activity.ForUser.MainPageActivity;
import com.sinhvien.onlinefoodshop.ApiService;
import com.sinhvien.onlinefoodshop.Model.UserModel;
import com.sinhvien.onlinefoodshop.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignupTabFragment extends Fragment {

    private EditText edtEmail, edtPassWord, edtConfirmPassword, edtFullName, edtPhoneNumber;
    private Button btnSignup;
    private ApiService apiService;

    private String getBaseUrl() {
        return "https://foodshop-backend-jck5.onrender.com/"; //url từ render cloud
    }

    // Hàm kiểm tra môi trường và trả về baseUrl
//    private String getBaseUrl() {
//        if (isEmulator()) {
//            return "http://10.0.2.2:3000/";
//        } else {
//            return "http://192.168.1.7:3000/";
//        }
//    }
//
//    // Kiểm tra xem có phải emulator không (phiên bản ngắn gọn)
//    private boolean isEmulator() {
//        return "generic".equalsIgnoreCase(Build.BRAND) || Build.PRODUCT.contains("sdk");
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.signup_tab_fragment, container, false);

        // Sử dụng baseUrl động
        String baseUrl = getBaseUrl();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(ApiService.class);

        edtEmail = view.findViewById(R.id.edtEmail);
        edtPassWord = view.findViewById(R.id.edtPassWord);
        edtConfirmPassword = view.findViewById(R.id.edtConfirmPassword);
        edtFullName = view.findViewById(R.id.edtFullName);
        edtPhoneNumber = view.findViewById(R.id.edtPhoneNumber);
        btnSignup = view.findViewById(R.id.btnSignup);

        btnSignup.setOnClickListener(v -> {
            String email = edtEmail.getText().toString().trim();
            String password = edtPassWord.getText().toString().trim();
            String confirmPassword = edtConfirmPassword.getText().toString().trim();
            String fullName = edtFullName.getText().toString().trim();
            String phoneNumber = edtPhoneNumber.getText().toString().trim();

            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword) ||
                    TextUtils.isEmpty(fullName) || TextUtils.isEmpty(phoneNumber)) {
                Toast.makeText(getActivity(), "Vui Lòng Điền Đủ Thông Tin", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!password.equals(confirmPassword)) {
                Toast.makeText(getActivity(), "Mật Khẩu Không Khớp", Toast.LENGTH_SHORT).show();
                return;
            }

            UserModel user = new UserModel(email, fullName, phoneNumber,password, "user");
            user.setPassword(password);

            apiService.addUser(user).enqueue(new Callback<UserModel>() {
                @Override
                public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                    Log.d("SignupResponse", "Response raw: " + response.raw().toString());
                    if (response.isSuccessful()) {
                        Toast.makeText(getActivity(), "Đăng Ký Thành Công", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getActivity(), MainPageActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                    } else {
                        Toast.makeText(getActivity(), "Email đã tồn tại!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<UserModel> call, Throwable t) {
                    Log.e("SignupError", "Connection error: " + t.getMessage(), t);
                    Toast.makeText(getActivity(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });

        return view;
    }
}