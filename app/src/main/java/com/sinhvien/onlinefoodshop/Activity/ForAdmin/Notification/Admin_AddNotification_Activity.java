// Admin_AddNotification_Activity.java
package com.sinhvien.onlinefoodshop.Activity.ForAdmin.Notification;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.sinhvien.onlinefoodshop.ApiService;
import com.sinhvien.onlinefoodshop.Model.NotificationModel;
import com.sinhvien.onlinefoodshop.R;
import com.sinhvien.onlinefoodshop.RetrofitClient;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Admin_AddNotification_Activity extends AppCompatActivity {
    private EditText etTitle, etContent;
    private Button btnPublish;
    private ProgressBar progressBar;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_notification);

        initializeViews();
        setupToolbar();
        setupApiService();
    }

    private void initializeViews() {
        etTitle = findViewById(R.id.etNotificationTitle);
        etContent = findViewById(R.id.etNotificationContent);
        btnPublish = findViewById(R.id.btnPublishNotification);
        progressBar = findViewById(R.id.progressBar);

        btnPublish.setOnClickListener(v -> validateAndPublish());
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Tạo thông báo mới");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setupApiService() {
        apiService = RetrofitClient.getApiService();
    }

    private void validateAndPublish() {
        String title = etTitle.getText().toString().trim();
        String content = etContent.getText().toString().trim();

        if (title.isEmpty()) {
            etTitle.setError("Vui lòng nhập tiêu đề");
            return;
        }

        if (content.isEmpty()) {
            etContent.setError("Vui lòng nhập nội dung");
            return;
        }

        publishNotification(title, content);
    }

    private void publishNotification(String title, String content) {
        try {
            progressBar.setVisibility(View.VISIBLE);
            btnPublish.setEnabled(false);

            NotificationModel notification = new NotificationModel();
            notification.setTitle(title);
            notification.setContent(content);
            notification.setTimestamp(getCurrentTimestamp());

            apiService.createNotification(notification).enqueue(new Callback<NotificationModel>() {
                @Override
                public void onResponse(Call<NotificationModel> call, Response<NotificationModel> response) {
                    runOnUiThread(() -> {
                        progressBar.setVisibility(View.GONE);
                        btnPublish.setEnabled(true);

                        if (response.isSuccessful() && response.body() != null) {
                            Toast.makeText(Admin_AddNotification_Activity.this,
                                    "Thông báo đã được phát hành", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            String error = "Lỗi: " + response.code();
                            Toast.makeText(Admin_AddNotification_Activity.this,
                                    error, Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onFailure(Call<NotificationModel> call, Throwable t) {
                    runOnUiThread(() -> {
                        progressBar.setVisibility(View.GONE);
                        btnPublish.setEnabled(true);
                        Toast.makeText(Admin_AddNotification_Activity.this,
                                "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    });
                }
            });
        } catch (Exception e) {
            runOnUiThread(() -> {
                progressBar.setVisibility(View.GONE);
                btnPublish.setEnabled(true);
                Toast.makeText(this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            });
        }
    }

    private String getCurrentTimestamp() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                .format(new Date());
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}