package com.sinhvien.onlinefoodshop.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
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

import com.google.gson.Gson;
import com.sinhvien.onlinefoodshop.Activity.Admin_HomePage_Activity;
import com.sinhvien.onlinefoodshop.Activity.MainPageActivity;
import com.sinhvien.onlinefoodshop.ApiService;
import com.sinhvien.onlinefoodshop.ApiService.LoginRequest;
import com.sinhvien.onlinefoodshop.Model.UserModel;
import com.sinhvien.onlinefoodshop.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginTabFragment extends Fragment {

    private EditText edtEmail, edtPassWord;
    private Button btnLogin;
    private ApiService apiService;

    private String getBaseUrl() {
        return "https://foodshop-backend-jck5.onrender.com/"; //url từ render cloud
    }


    // Hàm kiểm tra môi trường và trả về baseUrl
//    private String getBaseUrl() {
//        // Kiểm tra nếu chạy trên emulator
//        if (isEmulator()) {
//            return "http://10.0.2.2:3000/";
//        } else {
//            return "http://192.168.1.7:3000/"; // Cập nhật IP của máy tính chạy server
//        }
//    }
//
//    // Kiểm tra xem có phải emulator không
//    private boolean isEmulator() {
//        return Build.FINGERPRINT.contains("generic")
//                || Build.HARDWARE.contains("goldfish")
//                || Build.HARDWARE.contains("ranchu")
//                || Build.PRODUCT.contains("sdk");
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_tab_fragment, container, false);

        // Sử dụng baseUrl động
        String baseUrl = getBaseUrl();
        Log.d("LoginConfig", "Using baseUrl: " + baseUrl);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(ApiService.class);

        edtEmail = view.findViewById(R.id.edtEmail);
        edtPassWord = view.findViewById(R.id.edtPassWord);
        btnLogin = view.findViewById(R.id.btnLogin);


        btnLogin.setOnClickListener(v -> {
            String email = edtEmail.getText().toString().trim();
            String password = edtPassWord.getText().toString().trim();

            Log.d("LoginRequest", "Email from EditText: " + email);
            Log.d("LoginRequest", "Password from EditText: " + password);

            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                Toast.makeText(getActivity(), "Vui Lòng Điền Đủ Thông Tin", Toast.LENGTH_SHORT).show();
                return;
            }

            LoginRequest loginRequest = new LoginRequest(email, password);

            Log.d("LoginRequest", "Request body: " + new Gson().toJson(loginRequest));

            apiService.login(loginRequest).enqueue(new Callback<UserModel>() {
                @Override
                public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                    Log.d("LoginResponse", "Response raw: " + response.raw().toString());
                    if (response.isSuccessful()) {
                        UserModel user = response.body();
                        Log.d("LoginResponse", "Full user object: " +
                                "Email: " + user.getEmail() +
                                ", Name: " + user.getFullName() +
                                ", Phone: " + user.getPhoneNumber() +
                                ", Role: " + user.getRole());

                        SharedPreferences prefs = getActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("Email", user.getEmail());
                        editor.putString("FullName", user.getFullName());
                        editor.putString("PhoneNumber", user.getPhoneNumber());
                        editor.putString("Role", user.getRole());
                        editor.putLong("LastBackgroundTime", 0);
                        editor.apply();

                        Log.d("SharedPrefs", "Dữ liệu đã lưu: " +
                                "Email: " + prefs.getString("Email", "not_found") +
                                ", Name: " + prefs.getString("FullName", "not_found") +
                                ", Phone: " + prefs.getString("PhoneNumber", "not_found") +
                                ", Role: " + prefs.getString("Role", "not_found"));

                        String role = user.getRole() != null ? user.getRole() : "user";
                        Intent intent;
                        if ("admin".equalsIgnoreCase(role)) {
                            Toast.makeText(getActivity(), "Chào mừng Admin!", Toast.LENGTH_SHORT).show();
                            intent = new Intent(getActivity(), Admin_HomePage_Activity.class);
                        } else {
                            Toast.makeText(getActivity(), "Đăng Nhập Thành Công", Toast.LENGTH_SHORT).show();
                            intent = new Intent(getActivity(), MainPageActivity.class);
                        }
                        startActivity(intent);
                        getActivity().finish();
                    } else {
                        Toast.makeText(getActivity(), "Email hoặc mật khẩu không đúng!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<UserModel> call, Throwable t) {
                    Log.e("LoginError", "Connection error: " + t.getMessage(), t);
                    Toast.makeText(getActivity(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });

        return view;
    }
}