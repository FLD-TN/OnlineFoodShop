package com.sinhvien.onlinefoodshop.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;
import com.sinhvien.onlinefoodshop.Activity.OrderDetailActivity; // Thêm import này
import com.sinhvien.onlinefoodshop.ApiService;
import com.sinhvien.onlinefoodshop.Model.OrderModel;
import com.sinhvien.onlinefoodshop.R;
import com.sinhvien.onlinefoodshop.RetrofitClient;
import java.util.Arrays;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminOrderAdapter extends RecyclerView.Adapter<AdminOrderAdapter.OrderViewHolder> {
    private List<OrderModel> orders;
    private String filterMode = "ALL";
    private final String[] statusValues = {"PENDING", "APPROVED", "DELIVERED", "SUCCESS", "CANCELLED"};
    private ApiService apiService;
    private Context context;

    public AdminOrderAdapter(List<OrderModel> orders, Context context) {
        this.orders = orders;
        this.context = context;
        this.apiService = RetrofitClient.getApiService();
    }

    public void setOrders(List<OrderModel> orders) {
        this.orders = orders;
        notifyDataSetChanged();
    }

    public void setFilterMode(String mode) {
        this.filterMode = mode;
        notifyDataSetChanged();
    }

    @Override
    public OrderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_admin_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OrderViewHolder holder, int position) {
        OrderModel order = orders.get(position);
        holder.tvOrderId.setText("Mã đơn: " + order.getOrderID());
        holder.tvUserEmail.setText("Email: " + order.getUserEmail());
        holder.tvTotalPrice.setText("Tổng: " + String.format("%.2f", order.getTotalPrice()));
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

        // Áp dụng background xen kẽ
        if (position % 2 == 0) {
            holder.itemView.setBackgroundColor(0xFFE0E0E0); // Xám nhạt đậm hơn
        } else {
            holder.itemView.setBackgroundColor(0xFFFFFFFF); // Trắng
        }

        // Xử lý nút Chỉnh trạng thái
        holder.btnEditStatus.setOnClickListener(v -> showEditStatusDialog(order, holder.itemView.getContext()));

        // Xử lý nút Hủy
        holder.btnCancel.setOnClickListener(v -> showCancelConfirmationDialog(order, holder.itemView.getContext()));

        // Xử lý nút Xem chi tiết
        holder.btnViewDetails.setOnClickListener(v -> {
            Intent intent = new Intent(context, OrderDetailActivity.class);
            intent.putExtra("order", order);
            intent.putExtra("isAdmin", true); // Đánh dấu là admin
            context.startActivity(intent);
        });
    }

    private void updateOrderStatus(String orderId, String newStatus, Context context) {
        ApiService.UpdateStatusRequest request = new ApiService.UpdateStatusRequest(newStatus);
        apiService.updateOrderStatus(orderId, request).enqueue(new Callback<OrderModel>() {
            @Override
            public void onResponse(Call<OrderModel> call, Response<OrderModel> response) {
                if (response.isSuccessful()) {
                    // Gửi broadcast để thông báo cập nhật
                    Intent intent = new Intent("com.sinhvien.onlinefoodshop.ORDER_STATUS_UPDATED");
                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                }
            }

            @Override
            public void onFailure(Call<OrderModel> call, Throwable t) {}
        });
    }

    private void showEditStatusDialog(OrderModel order, Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Chọn trạng thái mới cho " + order.getOrderID());

        builder.setItems(statusValues, (dialog, which) -> {
            String newStatus = statusValues[which];
            if (!newStatus.equals(order.getStatus())) {
                updateOrderStatus(order.getOrderID(), newStatus, context);
            }
        });

        builder.setNegativeButton("Hủy", null);
        builder.show();
    }

    private void showCancelConfirmationDialog(OrderModel order, Context context) {
        new AlertDialog.Builder(context)
                .setTitle("Xác nhận hủy đơn hàng")
                .setMessage("Bạn có chắc chắn muốn hủy đơn hàng " + order.getOrderID() + " không?")
                .setPositiveButton("Hủy", (dialog, which) -> {
                    updateOrderStatus(order.getOrderID(), "CANCELLED", context);
                })
                .setNegativeButton("Không", null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderId, tvUserEmail, tvTotalPrice, tvStatus;
        Button btnEditStatus, btnCancel, btnViewDetails; // Thêm btnViewDetails

        OrderViewHolder(View itemView) {
            super(itemView);
            tvOrderId = itemView.findViewById(R.id.tvOrderId);
            tvUserEmail = itemView.findViewById(R.id.tvUserEmail);
            tvTotalPrice = itemView.findViewById(R.id.tvTotalPrice);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            btnEditStatus = itemView.findViewById(R.id.btnEditStatus);
            btnCancel = itemView.findViewById(R.id.btnCancel);
            btnViewDetails = itemView.findViewById(R.id.btnViewDetails); // Ánh xạ nút mới
        }
    }
}