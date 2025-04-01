package com.sinhvien.onlinefoodshop.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.sinhvien.onlinefoodshop.Model.ProductModel; // Sử dụng ProductModel
import com.sinhvien.onlinefoodshop.R;
import java.util.List;

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.OrderItemViewHolder> {
    private Context context;
    private List<ProductModel> products; // Thay đổi thành ProductModel

    public OrderDetailAdapter(Context context, List<ProductModel> products) {
        this.context = context;
        this.products = products;
    }

    @Override
    public OrderItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_detail, parent, false);
        return new OrderItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OrderItemViewHolder holder, int position) {
        ProductModel product = products.get(position);
        holder.tvProductName.setText(product.getProductName());
        holder.tvProductPrice.setText(String.format("%.2f", product.getProductPrice()));
        holder.tvQuantity.setText("x" + product.getQuantity());
        Glide.with(context).load(product.getProductImage()).into(holder.ivProductImage);
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    static class OrderItemViewHolder extends RecyclerView.ViewHolder {
        TextView tvProductName, tvProductPrice, tvQuantity;
        ImageView ivProductImage;

        OrderItemViewHolder(View itemView) {
            super(itemView);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvProductPrice = itemView.findViewById(R.id.tvProductPrice);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            ivProductImage = itemView.findViewById(R.id.ivProductImage);
        }
    }
}