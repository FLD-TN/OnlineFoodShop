package com.sinhvien.onlinefoodshop.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sinhvien.onlinefoodshop.R;

import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryView> {

    List<Integer> imgList = new ArrayList<>();
    List<String> titleList = new ArrayList<>();
    List<Integer> priceList = new ArrayList<>();

    public CategoryAdapter(List<Integer> imgList, List<String> titleList) {
        this.imgList = imgList;
        this.titleList = titleList;
    }

    @NonNull
    @Override
    public CategoryView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_category, parent, false);
        return new CategoryView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryView holder, int position) {
        holder.image_category.setImageResource(imgList.get(position));
        holder.title_category.setText(titleList.get(position));
    }

    @Override
    public int getItemCount() {
        return imgList.size();
    }

    public static class CategoryView extends RecyclerView.ViewHolder {

         ImageView image_category;
         TextView title_category;

        public CategoryView(@NonNull View itemView) {
            super(itemView);

            image_category = (ImageView) itemView.findViewById(R.id.image_category);
            title_category = (TextView) itemView.findViewById(R.id.title_category);

        }
    }
}
