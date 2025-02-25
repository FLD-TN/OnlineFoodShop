package com.sinhvien.onlinefoodshop.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;
import com.sinhvien.onlinefoodshop.Adapter.BannerAdapter;
import com.sinhvien.onlinefoodshop.Adapter.CategoryAdapter;
import com.sinhvien.onlinefoodshop.Adapter.FoodAdapter;
import com.sinhvien.onlinefoodshop.Model.FoodModel;
import com.sinhvien.onlinefoodshop.R;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HomeFragment extends Fragment {

    RecyclerView recyclerHome;
    ViewPager2 viewPager;
    RecyclerView recyclerFoodList;

    List<Integer> imgList = new ArrayList<>();
    List<String> titleList = new ArrayList<>();
    List<Integer> bannerList = new ArrayList<>();

    private Handler autoSlideHandler = new Handler(Looper.getMainLooper());
    private Runnable autoSlideRunnable;
    private static final long AUTO_SLIDE_DELAY = 3000;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerHome = view.findViewById(R.id.recycler_home);
        viewPager = view.findViewById(R.id.view_pager);
        recyclerFoodList = view.findViewById(R.id.recycler_food_list);

        initializeRecyclerView();
        setupViewPager();
        initializeFoodRecyclerView();

        return view;
    }

    private void initializeRecyclerView() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2, GridLayoutManager.HORIZONTAL, false);
        recyclerHome.setLayoutManager(gridLayoutManager);

        imgList.clear();
        titleList.clear();

        imgList.add(R.drawable.noodles_icon);
        titleList.add("Phở");
        imgList.add(R.drawable.banhmi_icon);
        titleList.add("Bánh Mì");
        imgList.add(R.drawable.drink_icon);
        titleList.add("Đồ Uống");
        imgList.add(R.drawable.milk_tea);
        titleList.add("Trà Sữa");
        imgList.add(R.drawable.rice_icon);
        titleList.add("Cơm");
        imgList.add(R.drawable.snack_icon);
        titleList.add("Ăn vặt");
        imgList.add(R.drawable.cake_icon);
        titleList.add("Tráng Miệng");
        imgList.add(R.drawable.fastfood_icon);
        titleList.add("Đồ Ăn Nhanh");
        imgList.add(R.drawable.hotpot);
        titleList.add("Lẩu");
        imgList.add(R.drawable.seafood_icon);
        titleList.add("Hải Sản");
        imgList.add(R.drawable.vegetables_icon);
        titleList.add("Đồ chay");

        recyclerHome.setAdapter(new CategoryAdapter(imgList, titleList));
    }

    private void initializeFoodRecyclerView() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2); // 2 cột cho Food Item
        recyclerFoodList.setLayoutManager(gridLayoutManager);

        recyclerFoodList.setHasFixedSize(true);
        recyclerFoodList.setNestedScrollingEnabled(false);

        List<FoodModel> foodList = new ArrayList<>();
        foodList.add(new FoodModel("Phở Bò", "100000", R.drawable.pho_picture));
        foodList.add(new FoodModel("Cơm Tấm", "80000", R.drawable.com_tam_picture));
        foodList.add(new FoodModel("Trà Sữa", "45000", R.drawable.milk_tea));
        foodList.add(new FoodModel("Bánh Mì", "30000", R.drawable.banhmi_icon));

        recyclerFoodList.setAdapter(new FoodAdapter(foodList));
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setupViewPager() {
        bannerList.clear();
        bannerList.addAll(Arrays.asList(
                R.drawable.indianfood_banner3,
                R.drawable.beefsteak_banner,
                R.drawable.chinesefood_banner
        ));

        BannerAdapter bannerAdapter = new BannerAdapter(bannerList);
        viewPager.setAdapter(bannerAdapter);

        int initialPosition = Integer.MAX_VALUE / 2 - (Integer.MAX_VALUE / 2 % bannerList.size());
        viewPager.setCurrentItem(initialPosition, false);

        viewPager.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    stopAutoSlide();
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    startAutoSlide();
                    break;
            }
            return false;
        });

        startAutoSlide();
    }

    private void startAutoSlide() {
        if (autoSlideRunnable != null) {
            autoSlideHandler.removeCallbacks(autoSlideRunnable);
        }

        autoSlideRunnable = new Runnable() {
            @Override
            public void run() {
                int currentItem = viewPager.getCurrentItem();
                int nextItem = currentItem + 1;
                viewPager.setCurrentItem(nextItem, true);
                autoSlideHandler.postDelayed(this, AUTO_SLIDE_DELAY);
            }
        };
        autoSlideHandler.postDelayed(autoSlideRunnable, AUTO_SLIDE_DELAY);
    }

    private void stopAutoSlide() {
        if (autoSlideRunnable != null) {
            autoSlideHandler.removeCallbacks(autoSlideRunnable);
        }
    }
}