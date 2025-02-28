package com.sinhvien.onlinefoodshop.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;
import com.sinhvien.onlinefoodshop.Activity.ProductListActivity;
import com.sinhvien.onlinefoodshop.Adapter.BannerAdapter;
import com.sinhvien.onlinefoodshop.Adapter.CategoryAdapter;
import com.sinhvien.onlinefoodshop.Adapter.UserProductAdapter;
import com.sinhvien.onlinefoodshop.ApiService;
import com.sinhvien.onlinefoodshop.Model.CategoryModel;
import com.sinhvien.onlinefoodshop.Model.ProductModel;
import com.sinhvien.onlinefoodshop.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class HomeFragment extends Fragment {
    private static final String TAG = "HomeFragment";
    RecyclerView recyclerHome;
    ViewPager2 viewPager;
    RecyclerView recyclerFoodList;
    TextView tvViewAll;

    List<Integer> bannerList = new ArrayList<>();
    private UserProductAdapter productAdapter;
    private CategoryAdapter categoryAdapter;
    private ApiService apiService;

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
        tvViewAll = view.findViewById(R.id.tv_view_all);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://foodshop-backend-jck5.onrender.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(ApiService.class);

        productAdapter = new UserProductAdapter();
        recyclerFoodList.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recyclerFoodList.setHasFixedSize(true);
        recyclerFoodList.setNestedScrollingEnabled(false);
        recyclerFoodList.setAdapter(productAdapter);

        categoryAdapter = new CategoryAdapter(new ArrayList<>(), new ArrayList<>());
        recyclerHome.setLayoutManager(new GridLayoutManager(getActivity(), 2, GridLayoutManager.HORIZONTAL, false));
        recyclerHome.setAdapter(categoryAdapter);

        setupViewPager();
        loadCategories();
        loadRandomProducts();

        tvViewAll.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ProductListActivity.class);
            startActivity(intent);
        });

        return view;
    }

    private void loadCategories() {
        Call<List<CategoryModel>> call = apiService.getCategories();
        call.enqueue(new Callback<List<CategoryModel>>() {
            @Override
            public void onResponse(Call<List<CategoryModel>> call, Response<List<CategoryModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<CategoryModel> categories = response.body();
                    Log.d(TAG, "Loaded " + categories.size() + " categories");
                    List<String> titleList = new ArrayList<>();
                    List<String> iconList = new ArrayList<>();
                    for (CategoryModel category : categories) {
                        titleList.add(category.getCategoryName());
                        iconList.add(category.getCategoryIcon() != null ? category.getCategoryIcon() : "");
                    }
                    categoryAdapter.setData(iconList, titleList);
                    recyclerHome.setAdapter(categoryAdapter);
                } else {
                    Log.e(TAG, "Failed to load categories. Code: " + response.code());
                    Toast.makeText(getActivity(), "Không thể tải danh sách loại sản phẩm", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<CategoryModel>> call, Throwable t) {
                Log.e(TAG, "Error loading categories: " + t.getMessage());
                Toast.makeText(getActivity(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadRandomProducts() {
        Call<List<ProductModel>> call = apiService.getProducts();
        call.enqueue(new Callback<List<ProductModel>>() {
            @Override
            public void onResponse(Call<List<ProductModel>> call, Response<List<ProductModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<ProductModel> fullList = response.body();
                    Log.d(TAG, "Loaded " + fullList.size() + " products");
                    List<ProductModel> randomList = getRandomProducts(fullList, 6);
                    productAdapter.setProductList(randomList);
                } else {
                    Log.e(TAG, "Failed to load products. Code: " + response.code());
                    Toast.makeText(getActivity(), "Không thể tải sản phẩm", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ProductModel>> call, Throwable t) {
                Log.e(TAG, "Error loading products: " + t.getMessage());
                Toast.makeText(getActivity(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private List<ProductModel> getRandomProducts(List<ProductModel> fullList, int count) {
        List<ProductModel> shuffledList = new ArrayList<>(fullList);
        Collections.shuffle(shuffledList);
        return shuffledList.size() > count ? shuffledList.subList(0, count) : shuffledList;
    }

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