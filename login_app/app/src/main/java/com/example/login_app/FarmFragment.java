package com.example.login_app;

import static android.content.Context.MODE_PRIVATE;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
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

import com.github.lzyzsd.circleprogress.CircleProgress;
import com.github.lzyzsd.circleprogress.DonutProgress;
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

    private TextView tvCreenSoil, tvCreenWater, tvValueSoil, tvValueWater;
    private Timer timer;
    private DonutProgress donutProgressTemp, donutProgressHumi, donutProgressSoil;
    private CircleProgress circleProgressWater;

    private List<Notification_model> listNotify;
    SharedPreferences sharedPreferences;


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

        donutProgressTemp = view.findViewById(R.id.circleProgressTemp);
        donutProgressHumi = view.findViewById(R.id.circleProgressHumi);
        tvCreenSoil = view.findViewById(R.id.tvCreenSoil);
        tvCreenWater = view.findViewById(R.id.tvCreenWater);
        tvValueSoil = view.findViewById(R.id.tvValueSoil);
        tvValueWater = view.findViewById(R.id.tvValueWater);
        circleProgressWater = view.findViewById(R.id.circleProgressWater);
        donutProgressSoil = view.findViewById(R.id.circleProgressSoil);

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                getLatestSensorData();
            }
        }, 0, 5000);  // update every 1000 milliseconds (1 second)

        return view;
    }

    private void getLatestSensorData() {
        RetrofitServer retrofitServer = new RetrofitServer();
        RetrofitInterface sensorDataService = retrofitServer.Retrofit();

        //Nhietdo------------------------------------------
        Call<List<SensorResuilt>> callTemp = sensorDataService.getLatestTempData();
        callTemp.enqueue(new Callback<List<SensorResuilt>>() {
            @Override
            public void onResponse(Call<List<SensorResuilt>> call, Response<List<SensorResuilt>> response) {
                List<SensorResuilt> sensorDataList = response.body();
                if (sensorDataList != null && !sensorDataList.isEmpty()) {
                    SensorResuilt latestSensorData = sensorDataList.get(sensorDataList.size() - 1);
                    donutProgressTemp.setProgress(Float.parseFloat(latestSensorData.getValue()));
                    donutProgressTemp.setSuffixText("°C");
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
                    donutProgressHumi.setProgress(Float.parseFloat(latestSensorData.getValue()));
                    donutProgressHumi.setSuffixText("%");
                }
            }

            @Override
            public void onFailure(Call<List<SensorResuilt>> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });

        //do am dat
        Call<List<SensorResuilt>> callMois = sensorDataService.getLatestMoisData();
        callMois.enqueue(new Callback<List<SensorResuilt>>() {
            @Override
            public void onResponse(Call<List<SensorResuilt>> call, Response<List<SensorResuilt>> response) {
                List<SensorResuilt> sensorDataList = response.body();
                if (sensorDataList != null && !sensorDataList.isEmpty()) {
                    SensorResuilt latestSensorData = sensorDataList.get(sensorDataList.size() - 1);
                    String moisSensor = latestSensorData.getValue();
                    tvValueSoil.setText(moisSensor);
                    donutProgressSoil.setProgress(Float.parseFloat(moisSensor));
                    donutProgressSoil.setSuffixText("%");

                    Handler handler = new Handler();
                    if (Double.parseDouble(latestSensorData.getValue()) < 50.0) {
                        tvCreenSoil.setText(R.string.lowSoil);
                        tvCreenSoil.setTextColor(Color.parseColor("#F44336"));
                        donutProgressSoil.setFinishedStrokeColor(Color.RED);
                    } else {
                        tvCreenSoil.setText(R.string.soilIsNormal);
                        tvCreenSoil.setTextColor(Color.WHITE);
                        donutProgressSoil.setFinishedStrokeColor(Color.BLUE);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<SensorResuilt>> call, Throwable t) {

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
                    String waterSensor = latestSensorData.getValue();
                    tvValueWater.setText(waterSensor);
                    circleProgressWater.setProgress(Integer.parseInt(waterSensor));
                    circleProgressWater.setSuffixText("mm");

                    Handler handler = new Handler();
                    if (Double.parseDouble(latestSensorData.getValue()) < 500) {
                        tvCreenWater.setText(R.string.lowWater);
                        tvCreenWater.setTextColor(Color.parseColor("#F44336"));
                        circleProgressWater.setFinishedColor(Color.parseColor("#F44336"));
                    } else if (Double.parseDouble(latestSensorData.getValue()) > 1000) {
                        tvCreenWater.setText(R.string.highWater);
                        tvCreenWater.setTextColor(Color.parseColor("#F44336"));
                        circleProgressWater.setFinishedColor(Color.parseColor("#F44336"));
                    } else {
                        tvCreenWater.setText(R.string.waterIsNormal);
                        tvCreenWater.setTextColor(Color.WHITE);
                        circleProgressWater.setFinishedColor(Color.BLUE);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<SensorResuilt>> call, Throwable t) {

            }
        });

    }
}