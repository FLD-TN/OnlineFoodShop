package com.sinhvien.onlinefoodshop.fragment;

import android.content.Intent;
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
import com.sinhvien.onlinefoodshop.Activity.MainActivity;
import com.sinhvien.onlinefoodshop.R;

public class SignupTabFragment extends Fragment {

    private EditText edtEmail, edtPassWord, edtConfirmPassword, edtFullName, edtPhoneNumber;
    private Button btnSignup;
    private DatabaseHelper databaseHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.signup_tab_fragment, container, false);

        edtEmail = view.findViewById(R.id.edtEmail);
        edtPassWord = view.findViewById(R.id.edtPassWord);
        edtConfirmPassword = view.findViewById(R.id.edtConfirmPassword);
        edtFullName = view.findViewById(R.id.edtFullName);
        edtPhoneNumber = view.findViewById(R.id.edtPhoneNumber);
        btnSignup = view.findViewById(R.id.btnSignup);
        databaseHelper = new DatabaseHelper(getActivity());

        btnSignup.setOnClickListener(v -> {
            String email = edtEmail.getText().toString();
            String password = edtPassWord.getText().toString();
            String confirmPassword = edtConfirmPassword.getText().toString();
            String fullName = edtFullName.getText().toString();
            String phoneNumber = edtPhoneNumber.getText().toString();

            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword) ||
                    TextUtils.isEmpty(fullName) || TextUtils.isEmpty(phoneNumber)) {
                Toast.makeText(getActivity(), "Vui Lòng Điền Đủ Thông Tin", Toast.LENGTH_SHORT).show();
            } else if (!password.equals(confirmPassword)) {
                Toast.makeText(getActivity(), "Mật Khẩu Không Khớp", Toast.LENGTH_SHORT).show();
            } else if (databaseHelper.checkEmail(email)) {
                // Kiểm tra xem email đã tồn tại chưa
                Toast.makeText(getActivity(), "Email Đã Tồn Tại, Vui Lòng Sử Dụng Email Khác", Toast.LENGTH_SHORT).show();
            } else {
                // Gọi insertData với 4 tham số String nếu email chưa tồn tại
                boolean result = databaseHelper.insertData(email, password, fullName, phoneNumber);
                if (result) {
                    Toast.makeText(getActivity(), "Đăng Ký Thành Công", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                } else {
                    Toast.makeText(getActivity(), "Đăng Ký Thất Bại", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }
}