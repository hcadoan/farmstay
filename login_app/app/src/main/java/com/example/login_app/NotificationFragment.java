package com.example.login_app;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import adapter.NotificationAdapter;
import model.Notification_model;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NotificationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NotificationFragment extends Fragment {

    private NotificationAdapter notificationAdapter;
    private List<Notification_model> listNotify;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public NotificationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NotificationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NotificationFragment newInstance(String param1, String param2) {
        NotificationFragment fragment = new NotificationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notification, container, false);

        ListView lvNotify = view.findViewById(R.id.lvNotify);
        TextView tvDelete = view.findViewById(R.id.tvDelete);

        // Khởi tạo đối tượng SharedPreferences
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("SaveInfo", Context.MODE_PRIVATE);

        // Đọc chuỗi JSON từ SharedPreferences
        String json = sharedPreferences.getString("notify", "");
        String json2 = sharedPreferences.getString("notifySoil", "");
        String json3 = sharedPreferences.getString("notifyWater", "");

        // Chuyển đổi chuỗi JSON thành danh sách
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Notification_model>>(){}.getType();
        ArrayList<Notification_model> listNotify = gson.fromJson(json, type);

        // Khởi tạo danh sách rỗng cho listNotify2,3
        ArrayList<Notification_model> listNotify2 = new ArrayList<>();
        ArrayList<Notification_model> listNotify3 = new ArrayList<>();

        // Chuyển đổi chuỗi JSON2 thành danh sách listNotify2
        if (json2 != null && !json2.equals("")) {
            listNotify2 = gson.fromJson(json2, type);
        }

        // Chuyển đổi chuỗi JSON3 thành danh sách listNotify3
        if (json3 != null && !json3.equals("")) {
            listNotify3 = gson.fromJson(json3, type);
        }

        // Thêm tất cả các phần tử của listNotify2,3 vào listNotify
        if (listNotify2 != null && !listNotify2.isEmpty()) {
            listNotify.addAll(listNotify2);
        }
        if (listNotify3 != null && !listNotify3.isEmpty()) {
            listNotify.addAll(listNotify3);
        }

        notificationAdapter = new NotificationAdapter(getActivity(), listNotify);
        lvNotify.setAdapter(notificationAdapter);
        notificationAdapter.notifyDataSetChanged();

        ArrayList<Notification_model> finalListNotify = listNotify;
        tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                if(!finalListNotify.isEmpty()) {
                    editor.remove("notify");
                    editor.commit();
                    finalListNotify.clear();
                    notificationAdapter.notifyDataSetChanged();
                }
            }
        });

        return view;
    }
}