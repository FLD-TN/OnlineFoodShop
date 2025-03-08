package com.sinhvien.onlinefoodshop.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.sinhvien.onlinefoodshop.Activity.ForAdmin.Admin_HomePage_Activity;
import com.sinhvien.onlinefoodshop.Activity.ForUser.MainPageActivity;
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
    private static final String TAG = "LoginTabFragment";
    private EditText edtEmail, edtPassWord;
    private Button btnLogin;
    private ApiService apiService;

    private String getBaseUrl() {
        return "https://foodshop-backend-jck5.onrender.com/";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_tab_fragment, container, false);

        String baseUrl = getBaseUrl();
        Log.d(TAG, "Using baseUrl: " + baseUrl);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(ApiService.class);

        initViews(view);
        setupLoginButton();

        return view;
    }

    private void initViews(View view) {
        edtEmail = view.findViewById(R.id.edtEmail);
        edtPassWord = view.findViewById(R.id.edtPassWord);
        btnLogin = view.findViewById(R.id.btnLogin);
    }

    private void setupLoginButton() {
        btnLogin.setOnClickListener(v -> {
            String email = edtEmail.getText().toString().trim();
            String password = edtPassWord.getText().toString().trim();

            Log.d(TAG, "Login attempt with email: " + email);

            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                Toast.makeText(getActivity(), "Vui lòng điền đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            LoginRequest loginRequest = new LoginRequest(email, password);
            performLogin(loginRequest);
        });
    }

    private void performLogin(LoginRequest loginRequest) {
        apiService.login(loginRequest).enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    handleLoginSuccess(response.body());
                } else {
                    Toast.makeText(getActivity(), "Email hoặc mật khẩu không đúng!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                Log.e(TAG, "Login failed", t);
                Toast.makeText(getActivity(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleLoginSuccess(UserModel user) {
        if (getActivity() == null) return;

        // Save user data with consistent keys
        SharedPreferences prefs = getActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putString("email", user.getEmail()) // Changed to lowercase
                .putString("fullName", user.getFullName())
                .putString("phoneNumber", user.getPhoneNumber())
                .putString("role", user.getRole())
                .putLong("lastBackgroundTime", 0)
                .apply();

        // Verify saved data
        String savedEmail = prefs.getString("email", null);
        Log.d(TAG, "Saved user email: " + savedEmail);

        // Navigate based on role
        String role = user.getRole() != null ? user.getRole().toLowerCase() : "user";
        Intent intent;

        if (role.equals("admin")) {
            Toast.makeText(getActivity(), "Chào mừng Admin!", Toast.LENGTH_SHORT).show();
            intent = new Intent(getActivity(), Admin_HomePage_Activity.class);
        } else {
            Toast.makeText(getActivity(), "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
            intent = new Intent(getActivity(), MainPageActivity.class);
        }

        startActivity(intent);
        getActivity().finish();
    }
}