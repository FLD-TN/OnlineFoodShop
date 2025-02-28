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
import com.sinhvien.onlinefoodshop.Activity.Admin_CategoryDetails_Activity;
import com.sinhvien.onlinefoodshop.Model.CategoryModel;
import com.sinhvien.onlinefoodshop.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;

public class AdminCategoryAdapter extends RecyclerView.Adapter<AdminCategoryAdapter.CategoryViewHolder> {
    private List<CategoryModel> categoryList = new ArrayList<>();
    private final ActivityResultLauncher<Intent> detailsLauncher;

    public AdminCategoryAdapter(ActivityResultLauncher<Intent> detailsLauncher) {
        this.detailsLauncher = detailsLauncher;
    }

    public void setCategoryList(List<CategoryModel> categoryList) {
        this.categoryList.clear();
        this.categoryList.addAll(categoryList != null ? categoryList : new ArrayList<>());
        notifyDataSetChanged();
    }

    public List<CategoryModel> getCategoryList() {
        return new ArrayList<>(categoryList);
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category_admin, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        CategoryModel category = categoryList.get(position);
        if (category == null) {
            holder.tvCategoryName.setText("Loại sản phẩm không hợp lệ");
            holder.btnViewDetail.setEnabled(false);
            return;
        }

        holder.tvCategoryName.setText(category.getCategoryName() != null ? category.getCategoryName() : "Tên trống");
        String iconUrl = category.getCategoryIcon();
        if (iconUrl != null && !iconUrl.isEmpty()) {
            holder.progressBar.setVisibility(View.VISIBLE); // Hiển thị ProgressBar khi tải ảnh
            Picasso.get()
                    .load(iconUrl)
                    .placeholder(R.drawable.placeholder_image)
                    .error(R.drawable.placeholder_image)
                    .into(holder.ivCategoryIcon, new Callback() {
                        @Override
                        public void onSuccess() {
                            holder.progressBar.setVisibility(View.GONE); // Ẩn khi tải thành công
                        }

                        @Override
                        public void onError(Exception e) {
                            holder.progressBar.setVisibility(View.GONE); // Ẩn khi lỗi
                        }
                    });
        } else {
            holder.ivCategoryIcon.setImageResource(R.drawable.placeholder_image);
            holder.progressBar.setVisibility(View.GONE); // Ẩn nếu không có URL
        }
        holder.btnViewDetail.setEnabled(true);

        holder.btnViewDetail.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), Admin_CategoryDetails_Activity.class);
            intent.putExtra("categoryDetail", category);
            detailsLauncher.launch(intent);
        });
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        ImageView ivCategoryIcon;
        TextView tvCategoryName;
        Button btnViewDetail;
        ProgressBar progressBar;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            ivCategoryIcon = itemView.findViewById(R.id.ivCategoryIcon);
            tvCategoryName = itemView.findViewById(R.id.tvCategoryName);
            btnViewDetail = itemView.findViewById(R.id.btnViewDetail);
            progressBar = itemView.findViewById(R.id.progressBar); // Ánh xạ ProgressBar
        }
    }
}