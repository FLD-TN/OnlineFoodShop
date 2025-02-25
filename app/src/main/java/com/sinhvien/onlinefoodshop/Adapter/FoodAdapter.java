package com.sinhvien.onlinefoodshop.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.sinhvien.onlinefoodshop.Model.FoodModel;
import com.sinhvien.onlinefoodshop.R;

import java.util.List;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {
    private List<FoodModel> foodList;

    public FoodAdapter(List<FoodModel> foodList) {
        this.foodList = foodList;
    }

    @Override
    public FoodViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_food, parent, false);
        return new FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FoodViewHolder holder, int position) {
        FoodModel food = foodList.get(position);

        // Dùng Glide để tải và resize ảnh
        Glide.with(holder.itemView.getContext())
                .load(food.getImage()) // Tải từ resource ID
                .override(300, 400) // Resize ảnh về 300x300 pixel (có thể điều chỉnh)
                .centerCrop() // Cắt ảnh để vừa khung
                .into(holder.foodImage);

        holder.foodName.setText(food.getName());
        holder.foodPrice.setText(food.getPrice() + " VNĐ");
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }

    public static class FoodViewHolder extends RecyclerView.ViewHolder {
        ImageView foodImage;
        TextView foodName;
        TextView foodPrice;

        public FoodViewHolder(View itemView) {
            super(itemView);
            foodImage = itemView.findViewById(R.id.food_image);
            foodName = itemView.findViewById(R.id.food_name);
            foodPrice = itemView.findViewById(R.id.food_price);
        }
    }
}