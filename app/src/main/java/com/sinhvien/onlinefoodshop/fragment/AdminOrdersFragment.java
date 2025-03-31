package com.sinhvien.onlinefoodshop.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.sinhvien.onlinefoodshop.Adapter.AdminOrderAdapter;
import com.sinhvien.onlinefoodshop.ApiService;
import com.sinhvien.onlinefoodshop.Model.OrderModel;
import com.sinhvien.onlinefoodshop.R;
import com.sinhvien.onlinefoodshop.RetrofitClient;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminOrdersFragment extends Fragment {
    private RecyclerView recyclerOrders;
    private Spinner spinnerFilter;
    private AdminOrderAdapter orderAdapter;
    private ApiService apiService;
    private final String[] statusValues = {"ALL", "PENDING", "APPROVED", "DELIVERED", "SUCCESS", "CANCELLED"};
    private BroadcastReceiver orderStatusUpdatedReceiver;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        orderStatusUpdatedReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int selectedPosition = spinnerFilter.getSelectedItemPosition();
                String selectedStatus = statusValues[selectedPosition];
                if ("ALL".equals(selectedStatus)) {
                    loadAllOrders();
                } else {
                    loadOrdersByStatus(selectedStatus);
                }
            }
        };
        LocalBroadcastManager.getInstance(requireActivity()).registerReceiver(
                orderStatusUpdatedReceiver,
                new IntentFilter("com.sinhvien.onlinefoodshop.ORDER_STATUS_UPDATED")
        );
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(requireActivity()).unregisterReceiver(orderStatusUpdatedReceiver);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_orders, container, false);

        recyclerOrders = view.findViewById(R.id.recyclerOrders);
        spinnerFilter = view.findViewById(R.id.spinnerFilter);
        apiService = RetrofitClient.getApiService();

        orderAdapter = new AdminOrderAdapter(new ArrayList<>(), requireContext()); // Truyền context
        recyclerOrders.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerOrders.setAdapter(orderAdapter);

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item, statusValues);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFilter.setAdapter(spinnerAdapter);

        spinnerFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedStatus = statusValues[position];
                orderAdapter.setFilterMode("ALL".equals(selectedStatus) ? "ALL" : "SPECIFIC");
                if ("ALL".equals(selectedStatus)) {
                    loadAllOrders();
                } else {
                    loadOrdersByStatus(selectedStatus);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                loadAllOrders();
            }
        });

        loadAllOrders();
        return view;
    }

    private void loadAllOrders() {
        apiService.getAllOrders().enqueue(new Callback<List<OrderModel>>() {
            @Override
            public void onResponse(Call<List<OrderModel>> call, Response<List<OrderModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    orderAdapter.setOrders(response.body());
                    orderAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<OrderModel>> call, Throwable t) {}
        });
    }

    private void loadOrdersByStatus(String status) {
        apiService.getOrdersByStatus(status).enqueue(new Callback<List<OrderModel>>() {
            @Override
            public void onResponse(Call<List<OrderModel>> call, Response<List<OrderModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    orderAdapter.setOrders(response.body());
                    orderAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<OrderModel>> call, Throwable t) {}
        });
    }
}