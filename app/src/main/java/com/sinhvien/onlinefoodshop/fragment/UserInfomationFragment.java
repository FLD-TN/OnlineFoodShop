package com.sinhvien.onlinefoodshop.fragment;

import android.annotation.SuppressLint;
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

import androidx.fragment.app.Fragment;

import com.sinhvien.onlinefoodshop.DatabaseHelper;
import com.sinhvien.onlinefoodshop.Activity.MainActivity;
import com.sinhvien.onlinefoodshop.R;
import com.sinhvien.onlinefoodshop.Model.UserModel;

public class UserInfomationFragment extends Fragment {

    private ImageView avatarImageView;
    private TextView txtFullName, btnEditInfo, btnHelpCenter, btnTerm, btnSettings, btnAppInfo;
    private Button  btnLogout;

    private DatabaseHelper databaseHelper;

    @SuppressLint("WrongViewCast")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_infomation, container, false);

        // Ánh xạ các view
        avatarImageView = view.findViewById(R.id.avatarImageView);
        txtFullName = view.findViewById(R.id.txtFullName);
        btnEditInfo = view.findViewById(R.id.btnEditInfo);
        btnLogout = view.findViewById(R.id.btnLogout);
        btnHelpCenter = view.findViewById(R.id.btnHelpCenter);
        btnTerm = view.findViewById(R.id.btnTerm);
        btnSettings = view.findViewById(R.id.btnSettings);
        btnAppInfo = view.findViewById(R.id.btnAppInfo);

        // Khởi tạo DatabaseHelper
        databaseHelper = new DatabaseHelper(getContext());

        // Lấy email từ SharedPreferences
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String email = sharedPreferences.getString("Email", "");

        // Lấy thông tin người dùng từ Database
        UserModel user = databaseHelper.getUserByEmail(email);
        if (user != null) {
            String greeting = "Xin chào, " + user.getFullName() + "!";
            txtFullName.setText(greeting);
        } else {
            Toast.makeText(getContext(), "Không tìm thấy thông tin người dùng!", Toast.LENGTH_SHORT).show();
        }


        // Xử lý sự kiện nút "Chỉnh sửa thông tin"
        btnEditInfo.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Chức năng chỉnh sửa thông tin chưa được phát triển.", Toast.LENGTH_SHORT).show();
        });


        // Xử lý sự kiện nút "Đăng xuất"
        btnLogout.setOnClickListener(v -> {
            // Xóa dữ liệu bằng SharedPreferences
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();

            // Chuyển sang đăng nhập
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);

            // Kết thúc Activity hiện tại (MainActivity) để tránh quay lại bằng nút Back
            requireActivity().finish();
        });

        return view;
    }

}
