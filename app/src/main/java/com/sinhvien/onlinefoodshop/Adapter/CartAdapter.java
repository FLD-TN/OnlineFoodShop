package com.sinhvien.onlinefoodshop.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.sinhvien.onlinefoodshop.Model.CartModel;
import com.sinhvien.onlinefoodshop.R;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private List<CartModel> cartItems;
    private Context context;
    private OnCartItemChangeListener listener;

    public interface OnCartItemChangeListener {
        void onQuantityChanged();
        void onItemRemoved(int position);
    }

    public CartAdapter(Context context, List<CartModel> cartItems, OnCartItemChangeListener listener) {
        this.context = context;
        this.cartItems = cartItems;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartModel item = cartItems.get(position);
        holder.tvProductName.setText(item.getProductName());
        holder.tvProductPrice.setText(item.getFormattedTotalPrice());
        holder.tvQuantity.setText(String.valueOf(item.getQuantity()));
        Glide.with(context).load(item.getProductImageUrl()).into(holder.ivProductImage);

        holder.btnIncrease.setOnClickListener(v -> {
            item.setQuantity(item.getQuantity() + 1);
            holder.tvQuantity.setText(String.valueOf(item.getQuantity()));
            listener.onQuantityChanged();
        });

        holder.btnDecrease.setOnClickListener(v -> {
            if (item.getQuantity() > 1) {
                item.setQuantity(item.getQuantity() - 1);
                holder.tvQuantity.setText(String.valueOf(item.getQuantity()));
                listener.onQuantityChanged();
            }
        });

        holder.btnRemove.setOnClickListener(v -> listener.onItemRemoved(position));
    }

    public void updateCartItems(List<CartModel> newCartItems) {
        this.cartItems.clear();
        this.cartItems.addAll(newCartItems);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    static class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView ivProductImage,btnRemove;
        TextView tvProductName, tvProductPrice, tvQuantity;
        Button btnIncrease, btnDecrease;



        CartViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProductImage = itemView.findViewById(R.id.ivProductImage);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvProductPrice = itemView.findViewById(R.id.tvProductPrice);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            btnIncrease = itemView.findViewById(R.id.btnIncrease);
            btnDecrease = itemView.findViewById(R.id.btnDecrease);
            btnRemove = itemView.findViewById(R.id.btnRemove);
        }
    }
}