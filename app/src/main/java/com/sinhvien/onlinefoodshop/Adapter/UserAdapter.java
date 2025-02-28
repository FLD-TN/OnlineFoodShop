package com.sinhvien.onlinefoodshop.Adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.sinhvien.onlinefoodshop.Activity.Admin_UserDetails_Activity;
import com.sinhvien.onlinefoodshop.Model.UserModel;
import com.sinhvien.onlinefoodshop.R;
import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private List<UserModel> userList = new ArrayList<>();
    private final ActivityResultLauncher<Intent> detailsLauncher;

    public UserAdapter(ActivityResultLauncher<Intent> detailsLauncher) {
        this.detailsLauncher = detailsLauncher;
    }

    public void setUserList(List<UserModel> userList) {
        this.userList.clear();
        this.userList.addAll(userList != null ? userList : new ArrayList<>());
        notifyDataSetChanged();
    }

    public List<UserModel> getUserList() {
        return new ArrayList<>(userList); // Trả về bản sao để tránh thay đổi trực tiếp
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        UserModel user = userList.get(position);
        if (user == null) {
            holder.tvEmail.setText("User không hợp lệ");
            holder.btnViewDetail.setEnabled(false);
            return;
        }

        holder.tvEmail.setText(user.getEmail() != null ? user.getEmail() : "Email trống");
        holder.btnViewDetail.setEnabled(true);

        holder.btnViewDetail.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), Admin_UserDetails_Activity.class);
            intent.putExtra("userDetail", user);
            detailsLauncher.launch(intent);
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView tvEmail;
        Button btnViewDetail;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            tvEmail = itemView.findViewById(R.id.tvEmail);
            btnViewDetail = itemView.findViewById(R.id.btnViewDetail);
        }
    }
}