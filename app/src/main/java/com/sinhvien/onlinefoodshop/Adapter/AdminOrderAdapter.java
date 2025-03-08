package com.sinhvien.onlinefoodshop.Adapter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sinhvien.onlinefoodshop.ApiService;
import com.sinhvien.onlinefoodshop.Model.OrderModel;
import com.sinhvien.onlinefoodshop.Model.ProductModel;
import com.sinhvien.onlinefoodshop.R;
import com.sinhvien.onlinefoodshop.RetrofitClient;

import java.text.DecimalFormat;
import java.util.Formatter;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminOrderAdapter extends RecyclerView.Adapter<AdminOrderAdapter.OrderViewHolder> {
    private List<OrderModel> orders;
    private ApiService apiService;
    private final String[] statusValues = {"PENDING", "APPROVED", "DELIVERED", "SUCCESS", "CANCELLED"};

    public AdminOrderAdapter(List<OrderModel> orders) {
        this.orders = orders;
        this.apiService = RetrofitClient.getApiService();
    }

    public void setOrders(List<OrderModel> orders) {
        this.orders = orders;
        notifyDataSetChanged();
    }

    @Override
    public OrderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_admin, parent, false);
        return new OrderViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(OrderViewHolder holder, int position) {
        DecimalFormat formatter = new DecimalFormat("#,###");
        OrderModel order = orders.get(position);
        Log.d("AdminOrderAdapter", "Order ID: " + order.getOrderID() + ", User Name: " + order.getUserName());
        holder.tvOrderId.setText("Mã đơn: " + order.getOrderID());
        holder.tvUserEmail.setText("Email: "+ order.getUserEmail());
        holder.tvUserName.setText("Tên khách hàng: "+order.getUserName());
        holder.tvStatus.setText("Trạng thái: " +getStatusText(order.getStatus()));
        holder.tvPaymentMethod.setText("Phương thức thanh toán: " + order.getPaymentMethod());
        holder.tvAddress.setText("Địa chỉ: " + order.getAddress());
        holder.tvProducts.setText(getProductText(order.getProducts()));
        holder.tvTotalPrice.setText("Tổng tiền: " + formatter.format(order.getTotalPrice())+ "đ");

        int spinnerPosition = getSpinnerPosition(order.getStatus());
        holder.spinnerStatus.setSelection(spinnerPosition);


        holder.spinnerStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int spinnerPosition, long id) {
                String newStatus = statusValues[spinnerPosition];
                if (!newStatus.equals(order.getStatus())) {
                    updateOrderStatus(order.getOrderID(), newStatus, holder);
                }
            }



            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Không làm gì
            }
        });
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

    private int getSpinnerPosition(String status) {
        for (int i = 0; i < statusValues.length; i++) {
            if (statusValues[i].equals(status)) {
                return i;
            }
        }
        return 0;
    }

    private void updateOrderStatus(String orderID, String status , OrderViewHolder holder) {
        new AlertDialog.Builder(holder.itemView.getContext())
                .setTitle("Xác nhận")
                .setMessage("Bạn có chắc muốn thay đổi trạng thái thành " + getStatusText(status) + "?")
                .setPositiveButton("Có", (dialog, which) -> {
                    ApiService.UpdateStatusRequest request = new ApiService.UpdateStatusRequest(status);
                    Call<OrderModel> call = apiService.updateOrderStatus(orderID,request);
                    call.enqueue(new Callback<OrderModel>() {
                        @Override
                        public void onResponse(Call<OrderModel> call, Response<OrderModel> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(holder.itemView.getContext(), "Cập nhật trạng thái thành công!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent("com.sinhvien.onlinefoodshop.ORDER_STATUS_UPDATED");
                                LocalBroadcastManager.getInstance(holder.itemView.getContext()).sendBroadcast(intent);
                            } else {
                                Toast.makeText(holder.itemView.getContext(), "Cập nhật thất bại: " + response.code(), Toast.LENGTH_SHORT).show();
                                holder.spinnerStatus.setSelection(getSpinnerPosition(orders.get(holder.getAdapterPosition()).getStatus()));
                            }
                        }

                        @Override
                        public void onFailure(Call<OrderModel> call, Throwable t) {
                            Toast.makeText(holder.itemView.getContext(), "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                            holder.spinnerStatus.setSelection(getSpinnerPosition(orders.get(holder.getAdapterPosition()).getStatus()));
                        }
                    });
                })
                .setNegativeButton("Không", (dialog, which) -> {
                    holder.spinnerStatus.setSelection(getSpinnerPosition(orders.get(holder.getAdapterPosition()).getStatus()));
                })
                .show();
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderId, tvTotalPrice, tvStatus, tvAddress,tvUserEmail,tvPaymentMethod,tvProducts,tvUserName;
        Spinner spinnerStatus;

        public OrderViewHolder(View itemView) {
            super(itemView);
            tvOrderId = itemView.findViewById(R.id.tvOrderId);
            tvTotalPrice = itemView.findViewById(R.id.tvTotalPrice);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            spinnerStatus = itemView.findViewById(R.id.spinnerStatus);
            tvUserEmail = itemView.findViewById(R.id.tvUserEmail);
            tvPaymentMethod = itemView.findViewById(R.id.tvPaymentMethod);
            tvProducts = itemView.findViewById(R.id.tvProducts);
            tvUserName = itemView.findViewById(R.id.tvUserName);

        }
    }
}