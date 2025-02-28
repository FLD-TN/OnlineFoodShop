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
import androidx.fragment.app.Fragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sinhvien.onlinefoodshop.Activity.MainActivity;
import com.sinhvien.onlinefoodshop.R;

public class UserInfomationFragment extends Fragment {

    private ImageView avatarImageView;
    private TextView txtFullName, btnEditInfo, btnHelpCenter, btnTerm, btnSettings, btnAppInfo;
    private Button btnLogout;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_infomation, container, false);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        avatarImageView = view.findViewById(R.id.avatarImageView);
        txtFullName = view.findViewById(R.id.txtFullName);
        btnEditInfo = view.findViewById(R.id.btnEditInfo);
        btnLogout = view.findViewById(R.id.btnLogout);
        btnHelpCenter = view.findViewById(R.id.btnHelpCenter);
        btnTerm = view.findViewById(R.id.btnTerm);
        btnSettings = view.findViewById(R.id.btnSettings);
        btnAppInfo = view.findViewById(R.id.btnAppInfo);

        if (mAuth.getCurrentUser() != null) {
            String uid = mAuth.getCurrentUser().getUid();
            db.collection("users").document(uid).get()
                    .addOnSuccessListener(document -> {
                        if (document.exists()) {
                            String fullName = document.getString("fullname");
                            String role = document.getString("role");
                            String greeting = "Xin chào, " + fullName + " (" + role + ")!";
                            txtFullName.setText(greeting);
                        } else {
                            Toast.makeText(getContext(), "Không tìm thấy thông tin người dùng!", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(getContext(), "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        }

        btnEditInfo.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Chức năng chỉnh sửa thông tin chưa được phát triển.", Toast.LENGTH_SHORT).show();
        });

        btnLogout.setOnClickListener(v -> {
            mAuth.signOut();
            SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();

            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
            requireActivity().finish();
        });

        return view;
    }
}