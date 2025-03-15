package com.sinhvien.onlinefoodshop.Adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.sinhvien.onlinefoodshop.Model.OrderModel;
import com.sinhvien.onlinefoodshop.Model.ProductModel;
import com.sinhvien.onlinefoodshop.Model.UserModel;
import com.sinhvien.onlinefoodshop.R;

import java.text.DecimalFormat;
import java.util.List;

public class UserOrderAdapter extends RecyclerView.Adapter<UserOrderAdapter.OrderViewHolder> {
    private List<OrderModel> orders;


    public UserOrderAdapter(List<OrderModel> orders) {
        this.orders = orders;
    }

    public void setOrders(List<OrderModel> orders) {
        this.orders = orders;
        notifyDataSetChanged();
    }

    @Override
    public OrderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_user, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OrderViewHolder holder, int position) {
        DecimalFormat formatter = new DecimalFormat("#,###");
        OrderModel order = orders.get(position);
        holder.tvUserName.setText(getUserOrderName(order.getUserName()));
        holder.tvOrderId.setText("Mã đơn: " + order.getOrderID());
        holder.tvTotalPrice.setText("Tổng tiền: " + formatter.format(order.getTotalPrice()) + "đ");
        holder.tvStatus.setText("Trạng thái: " + getStatusText(order.getStatus()));
        holder.tvAddress.setText("Địa chỉ: " + order.getAddress());
        holder.tvProducts.setText(getProductText(order.getProducts()));
        // Set background color based on status
        switch (order.getStatus()) {
            case "PENDING":
                holder.itemView.setBackgroundColor(Color.GRAY);
                break;
            case "APPROVED":
                holder.itemView.setBackgroundColor(Color.GREEN);
                break;
            case "CANCELLED":
                holder.itemView.setBackgroundColor(Color.parseColor("#FF6666"));
            default:
                holder.itemView.setBackgroundColor(Color.WHITE);
                break;
        }
    }


    @Override
    public int getItemCount() {
        return orders != null ? orders.size() : 0;
    }

    private String getStatusText(String status) {
        switch (status) {
            case "PENDING": return "Chờ xác nhận";
            case "APPROVED": return "Xác nhận";
            case "DELIVERED": return "Đang giao";
            case "SUCCESS": return "Đã giao";
            case "CANCELLED": return "Đã hủy";
            default: return status;
        }
    }

    private String getProductText(List<ProductModel> products) {
        StringBuilder productText = new StringBuilder("Sản phẩm: ");
        for (ProductModel product : products) {
            productText.append(product.getProductName())
                    .append(" (x")
                    .append(product.getQuantity())
                    .append("), ");
        }
        // xoá dấu phẩy và khoảng trống ở cuối
        productText.setLength(productText.length() - 2);
        return productText.toString();
    }

    private String getUserOrderName(String userName)
    {
        StringBuilder userNameText = new StringBuilder("Người đặt: ");
        userNameText.append(userName);
        return userNameText.toString();
        //xoá phẩy và space
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderId, tvTotalPrice, tvStatus, tvAddress,tvProducts,tvUserName;

        public OrderViewHolder(View itemView) {
            super(itemView);
            tvOrderId = itemView.findViewById(R.id.tvOrderId);
            tvTotalPrice = itemView.findViewById(R.id.tvTotalPrice);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            tvProducts = itemView.findViewById(R.id.tvProducts);
            tvUserName = itemView.findViewById(R.id.tvUserName);
        }
    }
}