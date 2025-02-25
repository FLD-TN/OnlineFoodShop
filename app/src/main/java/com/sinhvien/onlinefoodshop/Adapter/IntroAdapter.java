package com.sinhvien.onlinefoodshop.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.airbnb.lottie.LottieAnimationView;
import com.sinhvien.onlinefoodshop.R;

import java.util.List;

public class IntroAdapter extends RecyclerView.Adapter<IntroAdapter.IntroViewHolder> {
    private List<Integer> animationList; // Đổi từ String sang Integer

    public IntroAdapter(List<Integer> animationList) {
        this.animationList = animationList;
    }

    public static class IntroViewHolder extends RecyclerView.ViewHolder {
        public LottieAnimationView lottieView;

        public IntroViewHolder(View itemView) {
            super(itemView);
            lottieView = itemView.findViewById(R.id.lottieAnimationView);
        }
    }

    @Override
    public IntroViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_intro, parent, false);
        return new IntroViewHolder(view);
    }

    @Override
    public void onBindViewHolder(IntroViewHolder holder, int position) {
        holder.lottieView.setAnimation(animationList.get(position)); // Load từ raw
        holder.lottieView.playAnimation();
    }

    @Override
    public int getItemCount() {
        return animationList.size();
    }
}