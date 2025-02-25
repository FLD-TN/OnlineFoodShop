package com.sinhvien.onlinefoodshop.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.sinhvien.onlinefoodshop.Adapter.NotificationAdapter;
import com.sinhvien.onlinefoodshop.R;
import java.util.ArrayList;
import java.util.List;

public class NotificationFragment extends Fragment {

    public NotificationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);

        RecyclerView recyclerNotification = view.findViewById(R.id.recyclerNotification);
        recyclerNotification.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Tạo dữ liệu tạm thời
        List<String> notificationList = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            notificationList.add("Đây là nội dung thông báo mẫu số " + i);
        }

        NotificationAdapter adapter = new NotificationAdapter(notificationList);
        recyclerNotification.setAdapter(adapter);

        return view;
    }
}
