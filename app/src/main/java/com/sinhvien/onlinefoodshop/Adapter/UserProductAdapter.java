package com.sinhvien.onlinefoodshop.Adapter;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.SpannableString;
import android.text.style.StrikethroughSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.sinhvien.onlinefoodshop.Activity.ForUser.Product_Details_Activity;
import com.sinhvien.onlinefoodshop.CartManager;
import com.sinhvien.onlinefoodshop.Model.CartModel;
import com.sinhvien.onlinefoodshop.Model.ProductModel;
import com.sinhvien.onlinefoodshop.R;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;

public class UserProductAdapter extends RecyclerView.Adapter<UserProductAdapter.ProductViewHolder> {
    private List<ProductModel> productList = new ArrayList<>();

    public void setProductList(List<ProductModel> productList) {
        this.productList.clear();
        this.productList.addAll(productList != null ? productList : new ArrayList<>());
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_user, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        ProductModel product = productList.get(position);
        if (product == null) {
            holder.tvProductName.setText("Sản phẩm không hợp lệ");
            return;
        }

        holder.tvProductName.setText(product.getProductName() != null ? product.getProductName() : "Tên trống");
        double originalPrice = product.getProductPrice();
        double discountedPrice = originalPrice; // Giá mặc định, nếu không có giảm giá
        // Kiểm tra giảm giá theo %
        if (product.getDiscount() > 0) {
            discountedPrice = originalPrice * (1 - product.getDiscount() / 100.0);
            holder.tvDiscountRibbon.setText("-" + product.getDiscount() + "%");
            holder.tvDiscountRibbon.setVisibility(View.VISIBLE);
        }
        // Kiểm tra giảm giá theo số tiền
        else if (product.getDiscountAmount() > 0) {
            discountedPrice = originalPrice - product.getDiscountAmount();
            holder.tvDiscountRibbon.setText("-" + String.format("%,dđ", product.getDiscountAmount()));
            holder.tvDiscountRibbon.setVisibility(View.VISIBLE);
        }
        // Không có giảm giá
        else {
            holder.tvDiscountRibbon.setVisibility(View.GONE);
        }

        // Cập nhật hiển thị giá
        if (discountedPrice < originalPrice) {
            holder.tvOriginalPrice.setText(String.format("%,.0fđ", originalPrice));
            holder.tvOriginalPrice.setPaintFlags(holder.tvOriginalPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.tvOriginalPrice.setVisibility(View.VISIBLE);
        } else {
            holder.tvOriginalPrice.setVisibility(View.GONE);
        }

        holder.tvProductPrice.setText(String.format("%,.0fđ", discountedPrice));

        if (product.getProductImage() != null && !product.getProductImage().isEmpty()) {
            Picasso.get()
                    .load(product.getProductImage())
                    .placeholder(R.drawable.placeholder_image)
                    .error(R.drawable.placeholder_image)
                    .fit()
                    .centerCrop()
                    .into(holder.ivProductImage);
        } else {
            holder.ivProductImage.setImageResource(R.drawable.placeholder_image);
        }

        // Nhấn nút "Thêm vào giỏ"
        holder.btnAddToCart.setOnClickListener(v -> {
            double finalPrice = product.getDiscount() > 0
                    ? product.getProductPrice() * (1 - product.getDiscount() / 100.0)
                    : product.getProductPrice() - product.getDiscountAmount();

            CartManager.getInstance().addToCart(new CartModel(
                    product.getProductID(), product.getProductName(), finalPrice,
                    product.getProductImage(), 1
            ));

            Toast.makeText(holder.itemView.getContext(),
                    "Đã thêm " + product.getProductName() + " vào giỏ hàng",
                    Toast.LENGTH_SHORT).show();
        });

        // Nhấn vào item để mở chi tiết
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), Product_Details_Activity.class);
            intent.putExtra("productDetail", product);
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView tvProductName, tvProductPrice, tvDiscountRibbon;
        ImageView ivProductImage;
        Button btnAddToCart;
        TextView tvOriginalPrice;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvProductPrice = itemView.findViewById(R.id.tvProductPrice);
            tvDiscountRibbon = itemView.findViewById(R.id.tvDiscountRibbon);
            ivProductImage = itemView.findViewById(R.id.ivProductImage);
            btnAddToCart = itemView.findViewById(R.id.btnAddToCart);
            tvOriginalPrice = itemView.findViewById(R.id.tvOriginalPrice);
        }
    }
}