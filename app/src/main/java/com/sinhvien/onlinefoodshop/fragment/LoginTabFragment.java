package com.sinhvien.onlinefoodshop.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import com.sinhvien.onlinefoodshop.DatabaseHelper;
import com.sinhvien.onlinefoodshop.Activity.MainPageActivity;
import com.sinhvien.onlinefoodshop.R;
import com.sinhvien.onlinefoodshop.Model.UserModel;

public class LoginTabFragment extends Fragment {

    private EditText edtEmail, edtPassWord;
    private Button btnLogin;
    private DatabaseHelper databaseHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_tab_fragment, container, false);

        edtEmail = view.findViewById(R.id.edtEmail);
        edtPassWord = view.findViewById(R.id.edtPassWord);
        btnLogin = view.findViewById(R.id.btnLogin);
        databaseHelper = new DatabaseHelper(getActivity());

        btnLogin.setOnClickListener(v -> {
            String email = edtEmail.getText().toString();
            String password = edtPassWord.getText().toString();

            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                Toast.makeText(getActivity(), "Vui Lòng Điền Đủ Thông Tin", Toast.LENGTH_SHORT).show();
            } else {
                boolean checkCredentials = databaseHelper.checkEmailPassword(email, password);
                if (checkCredentials) {
                    UserModel user = databaseHelper.getUserByEmail(email);
                    if (user != null) {
                        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("FullName", user.getFullName());
                        editor.putString("Email", user.getEmail());
                        editor.apply();
                    }
                    Toast.makeText(getActivity(), "Đăng Nhập Thành Công", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(), MainPageActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                } else {
                    Toast.makeText(getActivity(), "Đăng Nhập Thất Bại", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }
}