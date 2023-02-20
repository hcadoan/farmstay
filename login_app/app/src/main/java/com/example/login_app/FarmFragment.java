package com.example.login_app;

import static android.content.Context.MODE_PRIVATE;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import model.Notification_model;
import model.RetrofitInterface;
import model.RetrofitServer;
import model.SensorResuilt;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FarmFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FarmFragment extends Fragment {

    private TextView tempTextView;
    private TextView humiTextView;
    private TextView moisTextView;
    private TextView waterTextView;
    private Timer timer;

    private List<Notification_model> listNotify;
    SharedPreferences sharedPreferences;

    public static final String CHANNEL_ID = "farmstay";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FarmFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FarmFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FarmFragment newInstance(String param1, String param2) {
        FarmFragment fragment = new FarmFragment();
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
        View view = inflater.inflate(R.layout.fragment_farm, container, false);

        tempTextView = view.findViewById(R.id.textViewTemp);
        humiTextView = view.findViewById(R.id.textViewHumi);
        moisTextView = view.findViewById(R.id.tvMoisture);
        waterTextView = view.findViewById(R.id.tvWaterlevel);

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                getLatestSensorData();
            }
        }, 0, 1000);  // update every 1000 milliseconds (1 second)

        return view;
    }

    private void getLatestSensorData() {
        RetrofitServer retrofitServer = new RetrofitServer();
        RetrofitInterface sensorDataService = retrofitServer.Retrofit();

        // Lấy thời gian hiện tại
        Calendar calendar = Calendar.getInstance();

        // Đặt giờ, phút cho thời gian hiện tại
        calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY));
        calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE));

        // Lấy giờ, phút của thời gian hiện tại
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        // Đặt ngày, tháng và năm cho ngày hiện tại
        calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
        calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH));

        // Lấy ngày, tháng và năm của ngày hiện tại
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        String time = String.format("%02d:%02d", hour, minute); // định dạng giờ:phút
        String date = String.format("%02d/%02d/%d", dayOfMonth, month + 1, year); // định dạng ngày/tháng/năm

        //Nhietdo------------------------------------------
        Call<List<SensorResuilt>> callTemp = sensorDataService.getLatestTempData();
        callTemp.enqueue(new Callback<List<SensorResuilt>>() {
            @Override
            public void onResponse(Call<List<SensorResuilt>> call, Response<List<SensorResuilt>> response) {
                List<SensorResuilt> sensorDataList = response.body();
                if (sensorDataList != null && !sensorDataList.isEmpty()) {
                    SensorResuilt latestSensorData = sensorDataList.get(sensorDataList.size() - 1);
                    tempTextView.setText(String.valueOf(latestSensorData.getValue()));
                }
            }

            @Override
            public void onFailure(Call<List<SensorResuilt>> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
        //Độ ẩm------------------------------------------
        Call<List<SensorResuilt>> callHumi = sensorDataService.getLatestHumiData();
        callHumi.enqueue(new Callback<List<SensorResuilt>>() {
            @Override
            public void onResponse(Call<List<SensorResuilt>> call, Response<List<SensorResuilt>> response) {
                List<SensorResuilt> sensorDataList = response.body();
                if (sensorDataList != null && !sensorDataList.isEmpty()) {
                    SensorResuilt latestSensorData = sensorDataList.get(sensorDataList.size() - 1);
                    humiTextView.setText(String.valueOf(latestSensorData.getValue()));
                }
            }

            @Override
            public void onFailure(Call<List<SensorResuilt>> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });

        //Độ ẩm đất------------------------------------------

        Call<List<SensorResuilt>> callMois = sensorDataService.getLatestMoisData();
        callMois.enqueue(new Callback<List<SensorResuilt>>() {
            @Override
            public void onResponse(Call<List<SensorResuilt>> call, Response<List<SensorResuilt>> response) {
                List<SensorResuilt> sensorDataList = response.body();
                if (sensorDataList != null && !sensorDataList.isEmpty()) {
                    SensorResuilt latestSensorData = sensorDataList.get(sensorDataList.size() - 1);
                    moisTextView.setText(String.valueOf(latestSensorData.getValue()));
                    Handler handler = new Handler();
                    if (Double.parseDouble(latestSensorData.getValue()) < 30.0) {
                        NotificationManager notificationManager = getContext().getSystemService(NotificationManager.class);
                        int notificationId = 1;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "My Channel", NotificationManager.IMPORTANCE_HIGH);
                            channel.setDescription("My channel description");
                            notificationManager.createNotificationChannel(channel);
                        }
                        Intent notificationIntent = new Intent(getActivity(), MainActivity.class);
                        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        PendingIntent pendingIntent = PendingIntent.getActivity(getActivity(), 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                        NotificationCompat.Builder builder = new NotificationCompat.Builder(getActivity(), CHANNEL_ID)
                                .setSmallIcon(R.drawable.ic_notifications)
                                .setContentTitle("Warning Farm")
                                .setContentText("Low Soil Moisture")
                                .setContentIntent(pendingIntent)
                                .setAutoCancel(true)
                                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM))
                                .setPriority(NotificationCompat.PRIORITY_HIGH)
                                .setDefaults(Notification.DEFAULT_ALL);

                        // Delay 3 seconds before showing the notification
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                notificationManager.notify(notificationId, builder.build());
                            }
                        }, 3000);

                        //test
                        listNotify = new ArrayList<>();
                        listNotify.add(new Notification_model(2,"Warning","Low Soil Moisture",time,date));

                        sharedPreferences = getActivity().getSharedPreferences("SaveInfo",MODE_PRIVATE);

                        // Chuyển đổi danh sách thành chuỗi JSON
                        Gson gson = new Gson();
                        String json = gson.toJson(listNotify);

                        // Lưu chuỗi JSON vào SharedPreferences
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("notifySoil", json);
                        editor.commit();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<SensorResuilt>> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });


        //Mực nước ---------------------------------
        Call<List<SensorResuilt>> callWater = sensorDataService.getLatestwaterlevelData();
        callWater.enqueue(new Callback<List<SensorResuilt>>() {
            @Override
            public void onResponse(Call<List<SensorResuilt>> call, Response<List<SensorResuilt>> response) {
                List<SensorResuilt> sensorDataList = response.body();
                if (sensorDataList != null && !sensorDataList.isEmpty()) {
                    SensorResuilt latestSensorData = sensorDataList.get(sensorDataList.size() - 1);
                    waterTextView.setText(String.valueOf(latestSensorData.getValue()));
                    Handler handler = new Handler();
                    if (Double.parseDouble(latestSensorData.getValue()) < 400) {
                        NotificationManager notificationManager = getContext().getSystemService(NotificationManager.class);
                        int notificationId = 1;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "My Channel", NotificationManager.IMPORTANCE_HIGH);
                            channel.setDescription("My channel description");
                            notificationManager.createNotificationChannel(channel);
                        }
                        Intent notificationIntent = new Intent(getActivity(), MainActivity.class);
                        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        PendingIntent pendingIntent = PendingIntent.getActivity(getActivity(), 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                        NotificationCompat.Builder builder = new NotificationCompat.Builder(getActivity(), CHANNEL_ID)
                                .setSmallIcon(R.drawable.ic_notifications)
                                .setContentTitle("Warning Farm")
                                .setContentText("Low Water Level")
                                .setContentIntent(pendingIntent)
                                .setAutoCancel(true)
                                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM))
                                .setPriority(NotificationCompat.PRIORITY_HIGH)
                                .setDefaults(Notification.DEFAULT_ALL);

                        // Delay 3 seconds before showing the notification
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                notificationManager.notify(notificationId, builder.build());
                            }
                        }, 3000);

                        //test
                        listNotify = new ArrayList<>();
                        listNotify.add(new Notification_model(2,"Warning","Low Water Level",time,date));

                        sharedPreferences = getActivity().getSharedPreferences("SaveInfo",MODE_PRIVATE);

                        // Chuyển đổi danh sách thành chuỗi JSON
                        Gson gson = new Gson();
                        String json = gson.toJson(listNotify);

                        // Lưu chuỗi JSON vào SharedPreferences
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("notifyWater", json);
                        editor.commit();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<SensorResuilt>> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }
}