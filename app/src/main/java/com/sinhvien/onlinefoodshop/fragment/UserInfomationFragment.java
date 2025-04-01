package com.sinhvien.onlinefoodshop.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import com.sinhvien.onlinefoodshop.Activity.ForUser.EditUserInfoActivity;
import com.sinhvien.onlinefoodshop.Activity.ForUser.TermActivity;
import com.sinhvien.onlinefoodshop.Activity.MainActivity;
import com.sinhvien.onlinefoodshop.R;

public class UserInfomationFragment extends Fragment {

    private ImageView avatarImageView;
    private TextView txtFullName, btnEditInfo, btnHelpCenter, btnTerm, btnSettings, btnAppInfo;
    private Button btnLogout;
    private SharedPreferences sharedPreferences;

    // Launcher để nhận kết quả từ EditUserInfoActivity
//    private final ActivityResultLauncher<Intent> editInfoLauncher = registerForActivityResult(
//            new ActivityResultContracts.StartActivityForResult(),
//            result -> {
//                if (result.getResultCode() == getActivity().RESULT_OK) {
//                    updateUserInfo(); // Cập nhật giao diện sau khi lưu
//                }
//            }
//    );

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_infomation, container, false);

        // Lấy SharedPreferences
        sharedPreferences = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);

        // Khởi tạo các view
        avatarImageView = view.findViewById(R.id.avatarImageView);
        txtFullName = view.findViewById(R.id.txtFullName);
        btnEditInfo = view.findViewById(R.id.btnEditInfo);
        btnLogout = view.findViewById(R.id.btnLogout);
        btnHelpCenter = view.findViewById(R.id.btnHelpCenter);
        btnTerm = view.findViewById(R.id.btnTerm);
        btnSettings = view.findViewById(R.id.btnSettings);
        btnAppInfo = view.findViewById(R.id.btnAppInfo);

        // Hiển thị thông tin ban đầu
        updateUserInfo();
        
        // Xử lý sự kiện nhấn btnEditInfo
        btnEditInfo.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), EditUserInfoActivity.class);
            startActivity(intent);
        });

        // Xử lý nút đăng xuất (nếu cần)
        btnLogout.setOnClickListener(v -> {
            Toast.makeText(getActivity(), "Đã đăng xuất!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getActivity(), MainActivity.class));
            getActivity().finish();
        });

        btnTerm.setOnClickListener(v -> {
           Intent intent1 = new Intent(getActivity(), TermActivity.class);
           startActivity(intent1);
        });

        return view;
    }

    private void updateUserInfo() {
        String fullName = sharedPreferences.getString("fullName", "Anonymous");
        txtFullName.setText(fullName);
    }
}