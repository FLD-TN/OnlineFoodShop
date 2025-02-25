package com.sinhvien.onlinefoodshop.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.sinhvien.onlinefoodshop.R;
import java.util.List;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder> {

    private final List<String> favoriteList;

    public FavoriteAdapter(List<String> favoriteList) {
        this.favoriteList = favoriteList;
    }

    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_favorite, parent, false);
        return new FavoriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteViewHolder holder, int position) {
        String favoriteItem = favoriteList.get(position);

        // Hiển thị dữ liệu mẫu
        holder.tvFavoriteName.setText("Sản phẩm #" + (position + 1));
        holder.tvFavoritePrice.setText("Giá: " + (100000 + position * 10000) + "đ");
        holder.ivFavoriteImage.setImageResource(R.drawable.com_tam_picture);

        // Xử lý sự kiện xóa khỏi yêu thích
        holder.btnRemoveFavorite.setOnClickListener(v -> {
            favoriteList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, favoriteList.size());
            Toast.makeText(v.getContext(), "Đã xóa khỏi yêu thích!", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return favoriteList.size();
    }

    public static class FavoriteViewHolder extends RecyclerView.ViewHolder {
        ImageView ivFavoriteImage;
        TextView tvFavoriteName, tvFavoritePrice;
        Button btnRemoveFavorite;

        public FavoriteViewHolder(@NonNull View itemView) {
            super(itemView);
            ivFavoriteImage = itemView.findViewById(R.id.ivFavoriteImage);
            tvFavoriteName = itemView.findViewById(R.id.tvFavoriteName);
            tvFavoritePrice = itemView.findViewById(R.id.tvFavoritePrice);
            btnRemoveFavorite = itemView.findViewById(R.id.btnRemoveFavorite);
        }
    }
}
