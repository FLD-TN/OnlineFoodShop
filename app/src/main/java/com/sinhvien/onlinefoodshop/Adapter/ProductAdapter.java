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
import com.sinhvien.onlinefoodshop.Activity.Admin_ProductDetails_Activity;
import com.sinhvien.onlinefoodshop.Model.ProductModel;
import com.sinhvien.onlinefoodshop.R;
import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private List<ProductModel> productList = new ArrayList<>();
    private final ActivityResultLauncher<Intent> detailsLauncher;

    public ProductAdapter(ActivityResultLauncher<Intent> detailsLauncher) {
        this.detailsLauncher = detailsLauncher;
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
        holder.tvProductPrice.setText(String.format("%,.0f VND", product.getProductPrice()));
        holder.btnViewDetail.setEnabled(true);

        holder.btnViewDetail.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), Admin_ProductDetails_Activity.class);
            intent.putExtra("productDetail", product);
            detailsLauncher.launch(intent);
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView tvProductName, tvProductPrice;
        Button btnViewDetail;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvProductPrice = itemView.findViewById(R.id.tvProductPrice);
            btnViewDetail = itemView.findViewById(R.id.btnViewDetail);
        }
    }
}