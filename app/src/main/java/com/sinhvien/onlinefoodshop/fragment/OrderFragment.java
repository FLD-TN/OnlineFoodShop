package com.sinhvien.onlinefoodshop.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.sinhvien.onlinefoodshop.Adapter.UserOrderAdapter; // Thay đổi adapter
import com.sinhvien.onlinefoodshop.Model.OrderModel;
import com.sinhvien.onlinefoodshop.R;
import com.sinhvien.onlinefoodshop.RetrofitClient;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderFragment extends Fragment {
    private static final String TAG = "OrderFragment";
    private RecyclerView recyclerView;
    private UserOrderAdapter orderAdapter; // Thay đổi adapter
    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressBar progressBar;
    private TextView tvNoOrders;
    private List<OrderModel> orderList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order, container, false);
        initViews(view);
        setupRecyclerView();
        setupSwipeRefresh();
        loadOrders();
        return view;
    }

    private void initViews(View view) {
        recyclerView = view.findViewById(R.id.rvOrders);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefresh);
        progressBar = view.findViewById(R.id.progressBar);
        tvNoOrders = view.findViewById(R.id.tvNoOrders);
    }

    private void setupRecyclerView() {
        orderList = new ArrayList<>();
        orderAdapter = new UserOrderAdapter(orderList); // Thay đổi adapter
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(orderAdapter);
    }

    private void setupSwipeRefresh() {
        swipeRefreshLayout.setOnRefreshListener(this::loadOrders);
        swipeRefreshLayout.setColorSchemeResources(R.color.Lavender, R.color.teal_200);
    }

    private void loadOrders() {
        SharedPreferences prefs = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String userEmail = prefs.getString("email", null);

        if (userEmail == null) {
            showError("Vui lòng đăng nhập lại!");
            return;
        }

        showLoading(true);
        Log.d(TAG, "Loading orders for email: " + userEmail);

        RetrofitClient.getApiService().getOrdersByUser(userEmail).enqueue(new Callback<List<OrderModel>>() {
            @Override
            public void onResponse(Call<List<OrderModel>> call, Response<List<OrderModel>> response) {
                showLoading(false);
                if (response.isSuccessful() && response.body() != null) {
                    orderList.clear();
                    orderList.addAll(response.body());
                    orderAdapter.notifyDataSetChanged();
                    Log.d(TAG, "Loaded " + orderList.size() + " orders");
                    updateEmptyView();
                } else {
                    String error = "Error: " + response.code();
                    Log.e(TAG, error);
                    showError("Không thể tải đơn hàng");
                }
            }

            @Override
            public void onFailure(Call<List<OrderModel>> call, Throwable t) {
                showLoading(false);
                Log.e(TAG, "Network error", t);
                showError("Lỗi kết nối: " + t.getMessage());
            }
        });
    }

    private void showLoading(boolean isLoading) {
        progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        swipeRefreshLayout.setRefreshing(false);
    }

    private void showError(String message) {
        if (getContext() != null) {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        }
    }

    private void updateEmptyView() {
        tvNoOrders.setVisibility(orderList.isEmpty() ? View.VISIBLE : View.GONE);
        recyclerView.setVisibility(orderList.isEmpty() ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadOrders();
    }
}