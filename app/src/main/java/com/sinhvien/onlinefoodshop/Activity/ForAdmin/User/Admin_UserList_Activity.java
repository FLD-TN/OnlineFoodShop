package com.sinhvien.onlinefoodshop.Activity.ForAdmin.User;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
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

public class Admin_UserList_Activity extends AppCompatActivity {
    private static final String TAG = "Admin_UserList_Activity";
    private RecyclerView rvUserList;
    private UserAdapter userAdapter;
    private EditText etSearchUser;
    private LottieAnimationView loadingAnimation;
    private View loadingOverlay; // Thêm nền trắng
    private ProgressBar progressBar;
    private Button btnAddUser;
    private ApiService apiService;
    private final String BASE_URL = getBaseUrl();

    private final ActivityResultLauncher<Intent> userDetailsLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    String deletedEmail = result.getData().getStringExtra("deletedEmail");
                    boolean dataChanged = result.getData().getBooleanExtra("dataChanged", false);
                    if (deletedEmail != null || dataChanged) {
                        loadUserList(); // Reload danh sách từ server sau khi xóa hoặc sửa
                    }
                }
            });

    private final ActivityResultLauncher<Intent> addUserLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    UserModel newUser = (UserModel) result.getData().getSerializableExtra("newUser");
                    if (newUser != null) {
                        loadUserList(); // Reload danh sách từ server sau khi thêm
                    }
                }
            });

    private String getBaseUrl() {
        return "https://foodshop-backend-jck5.onrender.com/";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_user_list);

        rvUserList = findViewById(R.id.rvUserListFragment);
        etSearchUser = findViewById(R.id.etSearchUser);
        progressBar = findViewById(R.id.progressBar);
        btnAddUser = findViewById(R.id.btnAddUser);
        loadingOverlay = findViewById(R.id.loadingOverlay); // Khởi tạo nền trắng
        loadingAnimation = findViewById(R.id.loadingAnimation); // Khởi tạo animation

        userAdapter = new UserAdapter(userDetailsLauncher);
        rvUserList.setLayoutManager(new LinearLayoutManager(this));
        rvUserList.setAdapter(userAdapter);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(ApiService.class);

        // Ẩn animation và overlay sau 2 giây
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            loadingOverlay.setVisibility(View.GONE);
            loadingAnimation.setVisibility(View.GONE);
            loadingAnimation.cancelAnimation();
            loadUserList(); // Load danh sách sau khi animation kết thúc
        }, 2000); // 2000ms = 2 giây

        btnAddUser.setOnClickListener(v -> {
            Intent intent = new Intent(this, Admin_AddUser_Activity.class);
            addUserLauncher.launch(intent);
        });

        etSearchUser.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String query = s.toString().trim();
                if (query.isEmpty()) {
                    loadUserList();
                } else {
                    searchUsers(query);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

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
                    if (users.isEmpty()) {
                        Toast.makeText(Admin_UserList_Activity.this, "Danh sách user rỗng", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e(TAG, "Failed to load users. Code: " + response.code());
                    Toast.makeText(Admin_UserList_Activity.this, "Không thể lấy dữ liệu. Mã lỗi: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<UserModel>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Log.e(TAG, "Error loading users: " + t.getMessage());
                Toast.makeText(Admin_UserList_Activity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void searchUsers(String query) {
        progressBar.setVisibility(View.VISIBLE);
        Call<List<UserModel>> call = apiService.searchUsersByEmail(query);
        call.enqueue(new Callback<List<UserModel>>() {
            @Override
            public void onResponse(Call<List<UserModel>> call, Response<List<UserModel>> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    List<UserModel> users = response.body();
                    Log.d(TAG, "Search found " + users.size() + " users for query: " + query);
                    userAdapter.setUserList(users);
                } else {
                    Log.e(TAG, "Search failed. Code: " + response.code());
                    Toast.makeText(Admin_UserList_Activity.this, "Tìm kiếm thất bại. Mã lỗi: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<UserModel>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Log.e(TAG, "Error searching users: " + t.getMessage());
                Toast.makeText(Admin_UserList_Activity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}