package com.sinhvien.onlinefoodshop.Adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.sinhvien.onlinefoodshop.Activity.ForAdmin.Product.Admin_ProductDetails_Activity;
import com.sinhvien.onlinefoodshop.Model.ProductModel;
import com.sinhvien.onlinefoodshop.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private List<ProductModel> productList = new ArrayList<>();
    private final ActivityResultLauncher<Intent> detailsLauncher;
    private final OnDiscountActionListener discountActionListener;

    public interface OnDiscountActionListener {
        void onEditDiscount(ProductModel product);
        void onRemoveDiscount(ProductModel product);
    }

    // Constructor với cả hai tham số (dành cho quản lý khuyến mãi)
    public ProductAdapter(ActivityResultLauncher<Intent> detailsLauncher, OnDiscountActionListener listener) {
        this.detailsLauncher = detailsLauncher;
        this.discountActionListener = listener;
    }

    // Constructor mới chỉ với detailsLauncher (dành cho danh sách không quản lý khuyến mãi)
    public ProductAdapter(ActivityResultLauncher<Intent> detailsLauncher) {
        this.detailsLauncher = detailsLauncher;
        this.discountActionListener = null; // Không cần listener cho khuyến mãi
    }

    public void setProductList(List<ProductModel> productList) {
        this.productList.clear();
        this.productList.addAll(productList != null ? productList : new ArrayList<>());
        notifyDataSetChanged();
    }

    public List<ProductModel> getProductList() {
        return new ArrayList<>(productList);
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_admin, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        ProductModel product = productList.get(position);
        if (product == null) {
            holder.tvProductName.setText("Sản phẩm không hợp lệ");
            holder.btnViewDetail.setEnabled(false);
            return;
        }

        holder.tvProductName.setText(product.getProductName() != null ? product.getProductName() : "Tên trống");
        String iconUrl = product.getProductImage();
        if (iconUrl != null && !iconUrl.isEmpty()) {
            holder.progressBar.setVisibility(View.VISIBLE);
            Picasso.get()
                    .load(iconUrl)
                    .placeholder(R.drawable.placeholder_image)
                    .error(R.drawable.placeholder_image)
                    .into(holder.ivProductImage, new Callback() {
                        @Override
                        public void onSuccess() {
                            holder.progressBar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError(Exception e) {
                            holder.progressBar.setVisibility(View.GONE);
                        }
                    });
        } else {
            holder.ivProductImage.setImageResource(R.drawable.placeholder_image);
            holder.progressBar.setVisibility(View.GONE);
        }
        holder.tvProductPrice.setText(String.format("%,.0f đ", product.getProductPrice()));

        // Hiển thị trạng thái khuyến mãi
        if (product.getDiscount() > 0) {
            holder.tvDiscount.setText("Giảm" + product.getDiscount() + "%");
            holder.btnRemoveDiscount.setVisibility(View.VISIBLE);
        } else if (product.getDiscountAmount() > 0) {
            holder.tvDiscount.setText("Giảm" + String.format("%,d đ", product.getDiscountAmount()));
            holder.btnRemoveDiscount.setVisibility(View.VISIBLE);
        } else {
            holder.tvDiscount.setText("Không có khuyến mãi");
            holder.btnRemoveDiscount.setVisibility(View.GONE);
        }

        holder.btnViewDetail.setEnabled(true);
        if (detailsLauncher != null) {
            holder.btnViewDetail.setOnClickListener(v -> {
                Intent intent = new Intent(v.getContext(), Admin_ProductDetails_Activity.class);
                intent.putExtra("productDetail", product);
                detailsLauncher.launch(intent);
            });
        } else {
            holder.btnViewDetail.setVisibility(View.GONE);
        }

        // Chỉ hiển thị các nút khuyến mãi nếu listener không null
        if (discountActionListener != null) {
            holder.btnEditDiscount.setVisibility(View.VISIBLE);
            holder.btnRemoveDiscount.setVisibility(product.getDiscount() > 0 || product.getDiscountAmount() > 0 ? View.VISIBLE : View.GONE);
            holder.btnEditDiscount.setOnClickListener(v -> discountActionListener.onEditDiscount(product));
            holder.btnRemoveDiscount.setOnClickListener(v -> discountActionListener.onRemoveDiscount(product));
        } else {
            holder.btnEditDiscount.setVisibility(View.GONE);
            holder.btnRemoveDiscount.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView tvProductName, tvProductPrice, tvDiscount;
        Button btnViewDetail, btnEditDiscount, btnRemoveDiscount;
        ImageView ivProductImage;
        ProgressBar progressBar;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvProductPrice = itemView.findViewById(R.id.tvProductPrice);
                tvDiscount = itemView.findViewById(R.id.tvDiscount);
            btnViewDetail = itemView.findViewById(R.id.btnViewDetail);
            btnEditDiscount = itemView.findViewById(R.id.btnEditDiscount);
            btnRemoveDiscount = itemView.findViewById(R.id.btnRemoveDiscount);
            ivProductImage = itemView.findViewById(R.id.ivProductImage);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }
}