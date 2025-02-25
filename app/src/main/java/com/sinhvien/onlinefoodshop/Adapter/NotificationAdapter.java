package com.sinhvien.onlinefoodshop.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.sinhvien.onlinefoodshop.R;
import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    private final List<String> notificationList;

    public NotificationAdapter(List<String> notificationList) {
        this.notificationList = notificationList;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification, parent, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        String notification = notificationList.get(position);

        // Tạm thời sử dụng dữ liệu giả
        holder.tvNotificationTitle.setText("Thông báo #" + (position + 1));
        holder.tvNotificationContent.setText("Nội dung thông báo: " + notification);
        holder.tvNotificationTime.setText((position + 1) * 5 + " phút trước");
        holder.ivNotificationIcon.setImageResource(R.drawable.noodles_icon);
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    public static class NotificationViewHolder extends RecyclerView.ViewHolder {
        ImageView ivNotificationIcon;
        TextView tvNotificationTitle, tvNotificationContent, tvNotificationTime;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            ivNotificationIcon = itemView.findViewById(R.id.ivNotificationIcon);
            tvNotificationTitle = itemView.findViewById(R.id.tvNotificationTitle);
            tvNotificationContent = itemView.findViewById(R.id.tvNotificationContent);
            tvNotificationTime = itemView.findViewById(R.id.tvNotificationTime);
        }
    }
}
