package com.sinhvien.onlinefoodshop.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.sinhvien.onlinefoodshop.Activity.ForUser.ProductListActivity;
import com.sinhvien.onlinefoodshop.R;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryView> {
    private List<String> iconList = new ArrayList<>();
    private List<String> titleList = new ArrayList<>();
    private Context context; // Thêm context để khởi tạo Intent

    public CategoryAdapter(List<String> iconList, List<String> titleList) {
        this.iconList = iconList;
        this.titleList = titleList;
    }

    public void setData(List<String> iconList, List<String> titleList) {
        this.iconList.clear();
        this.titleList.clear();
        this.iconList.addAll(iconList);
        this.titleList.addAll(titleList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CategoryView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext(); // Lấy context từ parent
        View view = LayoutInflater.from(context).inflate(R.layout.row_category, parent, false);
        return new CategoryView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryView holder, int position) {
        String iconUrl = iconList.get(position);
        if (iconUrl != null && !iconUrl.isEmpty()) {
            Picasso.get()
                    .load(iconUrl)
                    .placeholder(R.drawable.placeholder_image)
                    .error(R.drawable.placeholder_image)
                    .into(holder.image_category);
        } else {
            holder.image_category.setImageResource(R.drawable.placeholder_image);
        }
        holder.title_category.setText(titleList.get(position));

        // Thêm sự kiện click cho item
        holder.itemView.setOnClickListener(v -> {
            String selectedCategory = titleList.get(position);
            Intent intent = new Intent(context, ProductListActivity.class);
            intent.putExtra("selectedCategory", selectedCategory);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return titleList.size();
    }

    public static class CategoryView extends RecyclerView.ViewHolder {
        ImageView image_category;
        TextView title_category;

        public CategoryView(@NonNull View itemView) {
            super(itemView);
            image_category = itemView.findViewById(R.id.image_category);
            title_category = itemView.findViewById(R.id.title_category);
        }
    }
}