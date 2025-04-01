package com.sinhvien.onlinefoodshop.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.sinhvien.onlinefoodshop.Activity.OrderDetailActivity;
import com.sinhvien.onlinefoodshop.Model.OrderModel;
import com.sinhvien.onlinefoodshop.R;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class UserOrderAdapter extends RecyclerView.Adapter<UserOrderAdapter.OrderViewHolder> {
    private List<OrderModel> orders;
    private Context context;

    public UserOrderAdapter(List<OrderModel> orders) {
        this.orders = orders;
    }

    @Override
    public OrderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_user_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OrderViewHolder holder, int position) {
        OrderModel order = orders.get(position);
        holder.tvOrderId.setText("Mã đơn: " + order.getOrderID());
        holder.tvTotalPrice.setText("Tổng: " + String.format("%.2f", order.getTotalPrice()));
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        holder.tvOrderDate.setText("Ngày đặt: " + sdf.format(order.getOrderDate()));
        holder.tvStatus.setText(order.getStatus());

        // Áp dụng màu cho trạng thái
        switch (order.getStatus()) {
            case "PENDING":
                holder.tvStatus.setBackgroundColor(0xFFFFE082); // Vàng nhạt
                break;
            case "APPROVED":
                holder.tvStatus.setBackgroundColor(0xFF81D4FA); // Xanh dương nhạt
                break;
            case "DELIVERED":
                holder.tvStatus.setBackgroundColor(0xFF80CBC4); // Xanh ngọc nhạt
                break;
            case "SUCCESS":
                holder.tvStatus.setBackgroundColor(0xFFA5D6A7); // Xanh lá nhạt
                break;
            case "CANCELLED":
                holder.tvStatus.setBackgroundColor(0xFFEF9A9A); // Đỏ nhạt
                break;
        }

        // Click nút xem chi tiết
        holder.btnViewDetails.setOnClickListener(v -> {
            Intent intent = new Intent(context, OrderDetailActivity.class);
            intent.putExtra("order", order);
            intent.putExtra("isAdmin", false); // Đánh dấu không phải admin
            context.startActivity(intent);
        });

        // Click toàn bộ item
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, OrderDetailActivity.class);
            intent.putExtra("order", order);
            intent.putExtra("isAdmin", false);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderId, tvTotalPrice, tvStatus, tvOrderDate;
        Button btnViewDetails;

        OrderViewHolder(View itemView) {
            super(itemView);
            tvOrderId = itemView.findViewById(R.id.tvOrderId);
            tvTotalPrice = itemView.findViewById(R.id.tvTotalPrice);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvOrderDate = itemView.findViewById(R.id.tvOrderDate);
            btnViewDetails = itemView.findViewById(R.id.btnViewDetails);
        }
    }
}