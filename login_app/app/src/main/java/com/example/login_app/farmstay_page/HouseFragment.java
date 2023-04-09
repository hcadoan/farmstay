package com.example.login_app.farmstay_page;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.widget.SwitchCompat;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.login_app.R;

import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import model.Notification_model;
import api.RelayApi;
import api.RetrofitInterface;
import api.RetrofitServer;
import api.RetrofitServer2;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HouseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HouseFragment extends Fragment {

    private Timer timer;
    private ImageView mImageView, imLamp1, imLamp2, imLamp3, imFan;
    private ConstraintLayout mLayout;
    private TextView warningTextView, tvTemp, tvHumi, tvMessage;
    private SwitchCompat switch1;
    private SwitchCompat switch2;
    private SwitchCompat switch3;
    private SwitchCompat switchFan;
    private Button switch4, switch5;
    private ScrollView scrollViewHouse;

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

    public HouseFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HouseFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HouseFragment newInstance(String param1, String param2) {
        HouseFragment fragment = new HouseFragment();
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
        View view = inflater.inflate(R.layout.fragment_house, container, false);

        warningTextView = view.findViewById(R.id.warningTextView);
        mImageView = view.findViewById(R.id.ImFire);
        mLayout = view.findViewById(R.id.layout);
        imLamp1 = view.findViewById(R.id.ImLamp1);
        imLamp2 = view.findViewById(R.id.ImLamp2);
        imLamp3 = view.findViewById(R.id.ImLamp3);
        imFan = view.findViewById(R.id.imFan);
        tvTemp = view.findViewById(R.id.tvTemp);
        tvHumi = view.findViewById(R.id.tvHumi);
        switch1 = view.findViewById(R.id.switchBtn1);
        switch2 = view.findViewById(R.id.switchBtn2);
        switch3 = view.findViewById(R.id.switchBtn3);
        switch4 = view.findViewById(R.id.btnLampOFF);
        switch5 = view.findViewById(R.id.btnLampON);
        switchFan = view.findViewById(R.id.switchFan);

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                getLatestSensorData();
            }
        }, 0, 5000);  // update every 1000 milliseconds (1 second)

//        //bật/tắt đèn với socketIO
//        sharedPreferences = getActivity().getSharedPreferences("SaveInfo", MODE_PRIVATE);
//        String socketUrl = sharedPreferences.getString("socketUrl","");
//
//        Socket socket = null;
//        try {
//            socket = IO.socket(socketUrl);
//        } catch (URISyntaxException e) {
//            e.printStackTrace();
//        }
//
//        socket.connect();
//
//        // Cập nhật trạng thái đèn trong SharedPreferences
//        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//
//        // Lắng nghe sự kiện từ máy chủ Socket.IO để cập nhật trạng thái đèn
//        socket.on("light_1_status", new Emitter.Listener() {
//            @Override
//            public void call(Object... args) {
//                boolean isLightOn = (boolean) args[0];
//
//                editor.putBoolean("isLight_1_On", isLightOn);
//                editor.apply();
//
//                // Cập nhật giao diện của ứng dụng
//                if (isLightOn) {
//                    // Hiển thị đèn bật trên giao diện của ứng dụng
//                    switch1.setChecked(true);
//                } else {
//                    // Hiển thị đèn tắt trên giao diện của ứng dụng
//                    switch1.setChecked(false);
//                }
//            }
//        });
//
//        socket.on("light_2_status", new Emitter.Listener() {
//            @Override
//            public void call(Object... args) {
//                boolean isLightOn2 = (boolean) args[0];
//
//                editor.putBoolean("isLight_2_On", isLightOn2);
//                editor.apply();
//
//                // Cập nhật giao diện của ứng dụng
//                if (isLightOn2) {
//                    // Hiển thị đèn bật trên giao diện của ứng dụng
//                    switch2.setChecked(true);
//                } else {
//                    // Hiển thị đèn tắt trên giao diện của ứng dụng
//                    switch2.setChecked(false);
//                }
//            }
//        });
//
//        socket.on("light_3_status", new Emitter.Listener() {
//            @Override
//            public void call(Object... args) {
//                boolean isLightOn3 = (boolean) args[0];
//
//                editor.putBoolean("isLight_3_On", isLightOn3);
//                editor.apply();
//
//                // Cập nhật giao diện của ứng dụng
//                if (isLightOn3) {
//                    // Hiển thị đèn bật trên giao diện của ứng dụng
//                    switch3.setChecked(true);
//                } else {
//                    // Hiển thị đèn tắt trên giao diện của ứng dụng
//                    switch3.setChecked(false);
//                }
//            }
//        });
//
//        Socket finalSocket = socket;
//        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
//                if(isChecked){
//                    finalSocket.emit("turn_light_1_on");
//                } else {
//                    finalSocket.emit("turn_light_1_off");
//                }
//            }
//        });
//
//        switch2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
//                if(isChecked){
//                    finalSocket.emit("turn_light_2_on");
//                } else {
//                    finalSocket.emit("turn_light_2_off");
//                }
//            }
//        });
//
//        switch3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
//                if(isChecked){
//                    finalSocket.emit("turn_light_3_on");
//                } else {
//                    finalSocket.emit("turn_light_3_off");
//                }
//            }
//        });

        //bat tat den voi retrofit
        RetrofitServer2 retrofitServer2 = new RetrofitServer2();
        RelayApi relayApi = retrofitServer2.Retrofit();

        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                int state = isChecked ? 1 : 0;
                Call<ResponseBody> call = relayApi.setRelayState(1, state);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try {
                            String message = response.body().string();
                            // Xử lý kết quả trả về từ server
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        // Xử lý lỗi
                    }
                });
                if(isChecked){
                    imLamp1.setImageResource(R.drawable.ic_light_on);
                } else {
                    imLamp1.setImageResource(R.drawable.ic_light_off);
                }

            }
        });

        switch2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                int state = isChecked ? 1 : 0;
                Call<ResponseBody> call = relayApi.setRelayState(2, state);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try {
                            String message = response.body().string();
                            // Xử lý kết quả trả về từ server
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        // Xử lý lỗi
                    }
                });
                if(isChecked){
                    imLamp2.setImageResource(R.drawable.ic_light_on);
                } else {
                    imLamp2.setImageResource(R.drawable.ic_light_off);
                }
            }
        });

        switch3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                int state = isChecked ? 1 : 0;
                Call<ResponseBody> call = relayApi.setRelayState(3, state);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try {
                            String message = response.body().string();
                            // Xử lý kết quả trả về từ server
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        // Xử lý lỗi
                    }
                });
                if(isChecked){
                    imLamp3.setImageResource(R.drawable.ic_light_on);
                } else {
                    imLamp3.setImageResource(R.drawable.ic_light_off);
                }
            }
        });

        switchFan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    imFan.setImageResource(R.drawable.fan_on);
                } else {
                    imFan.setImageResource(R.drawable.fan_off);
                }
            }
        });

        switch4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Gửi yêu cầu tắt đến tất cả các kênh relay
                for (int channel = 1; channel <= 3; channel++) {
                    Call<ResponseBody> call = relayApi.setRelayState(channel, 0);
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            try {
                                String message = response.body().string();
                                // Xử lý kết quả trả về từ server
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            // Xử lý lỗi
                        }
                    });
                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        switch1.setChecked(false);
                        switch2.setChecked(false);
                        switch3.setChecked(false);
                    }
                }, 1000);
            }
        });

        switch5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Gửi yêu cầu tắt đến tất cả các kênh relay
                for (int channel = 1; channel <= 3; channel++) {
                    Call<ResponseBody> call = relayApi.setRelayState(channel, 1);
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            try {
                                String message = response.body().string();
                                // Xử lý kết quả trả về từ server
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            // Xử lý lỗi
                        }
                    });
                }
                // Tự động tắt switch
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        switch1.setChecked(true);
                        switch2.setChecked(true);
                        switch3.setChecked(true);
                    }
                }, 1000);
            }
        });

        return view;
    }

    private void getLatestSensorData() {
        RetrofitServer retrofitServer = new RetrofitServer();
        RetrofitInterface sensorDataService = retrofitServer.Retrofit();

//        //bao chay
//        Call<List<SensorResuilt>> callfire = sensorDataService.getLatestfireData();
//        callfire.enqueue(new Callback<List<SensorResuilt>>() {
//            @Override
//            public void onResponse(Call<List<SensorResuilt>> call, Response<List<SensorResuilt>> response) {
//                List<SensorResuilt> sensorDataList = response.body();
//                if (sensorDataList != null && !sensorDataList.isEmpty()) {
//                    SensorResuilt latestSensorData = sensorDataList.get(sensorDataList.size() - 1);
//                    String fireSensor = latestSensorData.getValue();
//
//                    if (fireSensor.equals("1")) {
//                        mLayout.setBackgroundResource(R.drawable.bg_icon_red);
//                    } else {
//                        mLayout.setBackgroundResource(R.drawable.bg_icon_2);
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<SensorResuilt>> call, Throwable t) {
//
//            }
//        });
//
//        //Nhietdo------------------------------------------
//        Call<List<SensorResuilt>> callTemp = sensorDataService.getLatestTempData();
//        callTemp.enqueue(new Callback<List<SensorResuilt>>() {
//            @Override
//            public void onResponse(Call<List<SensorResuilt>> call, Response<List<SensorResuilt>> response) {
//                List<SensorResuilt> sensorDataList = response.body();
//                if (sensorDataList != null && !sensorDataList.isEmpty()) {
//                    SensorResuilt latestSensorData = sensorDataList.get(sensorDataList.size() - 1);
//                    double value = Double.parseDouble(latestSensorData.getValue().toString());
//                    int latestValue = (int) Math.round(value);
//                    tvTemp.setText(String.valueOf(latestValue));
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<SensorResuilt>> call, Throwable t) {
//                Toast.makeText(getActivity(), t.getMessage(),
//                        Toast.LENGTH_LONG).show();
//            }
//        });
//        //Độ ẩm------------------------------------------
//        Call<List<SensorResuilt>> callHumi = sensorDataService.getLatestHumiData();
//        callHumi.enqueue(new Callback<List<SensorResuilt>>() {
//            @Override
//            public void onResponse(Call<List<SensorResuilt>> call, Response<List<SensorResuilt>> response) {
//                List<SensorResuilt> sensorDataList = response.body();
//                if (sensorDataList != null && !sensorDataList.isEmpty()) {
//                    SensorResuilt latestSensorData = sensorDataList.get(sensorDataList.size() - 1);
//                    double value = Double.parseDouble(latestSensorData.getValue().toString());
//                    int latestValue = (int) Math.round(value);
//                    tvHumi.setText(String.valueOf(latestValue));
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<SensorResuilt>> call, Throwable t) {
//                Toast.makeText(getActivity(), t.getMessage(),
//                        Toast.LENGTH_LONG).show();
//            }
//        });
    }
}