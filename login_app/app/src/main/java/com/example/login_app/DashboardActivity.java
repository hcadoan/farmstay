package com.example.login_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.example.login_app.databinding.ActivityDashboardBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
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

public class DashboardActivity extends BaseActivity {

    BottomNavigationView bottomNavigationView;

    ActivityDashboardBinding binding;
    SharedPreferences sharedPreferences;

    public static final String CHANNEL_ID = "farmstay";
    public static final String CHANNEL_ID_1 = "SoilMoisture";
    public static final String CHANNEL_ID_2 = "WaterLevel";
    public static final String CHANNEL_ID_3 = "WaterLevel2";
    private List<Notification_model> listNotify;
    Timer timer;

    private Animation rotateOpen, rotateClose, fromBottom, toBottom;
    private FloatingActionButton floatingBtn, setting_btn, contact_btn, chat_btn;
    private Boolean clicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Thiết lập Activity fullscreen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        binding = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new HomePageFragment());

        bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setBackground(null);

        floatingBtn = findViewById(R.id.floatingBtn);
        setting_btn = findViewById(R.id.setting_btn);
        contact_btn = findViewById(R.id.contact_btn);
        chat_btn = findViewById(R.id.chat_btn);

        binding.bottomNavigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        replaceFragment(new HomePageFragment());
                        break;
                    case R.id.farmstay:
                        replaceFragment(new FarmstayFragment());
                        break;
                    case R.id.notification:
                        replaceFragment(new NotificationFragment());
                        break;
                    case R.id.account:
                        replaceFragment(new AccountFragment());
                        break;
                }
                return true;
            }
        });

        ((MyApplication) getApplicationContext()).addActivity(this);

        //test
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

        listNotify = new ArrayList<>();
        listNotify.add(new Notification_model(2,DashboardActivity.this.getString(R.string.warning2),DashboardActivity.this.getString(R.string.fireAlarm),time,date));

        sharedPreferences = getSharedPreferences("SaveInfo",MODE_PRIVATE);

        // Chuyển đổi danh sách thành chuỗi JSON
        Gson gson = new Gson();
        String json = gson.toJson(listNotify);

        // Lưu chuỗi JSON vào SharedPreferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("notifytest", json);
        editor.commit();

        rotateOpen = AnimationUtils.loadAnimation(this,R.anim.rotate_open_anim);
        rotateClose = AnimationUtils.loadAnimation(this,R.anim.rotate_close_anim);
        fromBottom = AnimationUtils.loadAnimation(this,R.anim.from_bottom_anim);
        toBottom = AnimationUtils.loadAnimation(this,R.anim.to_bottom_anim);

        floatingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onFloatingButton();
            }
        });

        setting_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashboardActivity.this, SettingActivity.class);
                startActivity(intent);
            }
        });

        contact_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashboardActivity.this, ContactActivity.class);
                startActivity(intent);
            }
        });

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                getLatestSensorData();
            }
        }, 0, 5000);  // update every 1000 milliseconds (1 second)


    }

    private void onFloatingButton(){
        setVisibility(clicked);
        setAnimation(clicked);
        clicked = !clicked;
    }

    private void setVisibility(Boolean clicked) {
        if(!clicked){
            setting_btn.setVisibility(View.VISIBLE);
            contact_btn.setVisibility(View.VISIBLE);
            chat_btn.setVisibility(View.VISIBLE);
        } else {
            setting_btn.setVisibility(View.INVISIBLE);
            contact_btn.setVisibility(View.INVISIBLE);
            chat_btn.setVisibility(View.INVISIBLE);
        }
    }

    private void setAnimation(Boolean clicked) {
        if(!clicked){
            setting_btn.startAnimation(fromBottom);
            contact_btn.startAnimation(fromBottom);
            chat_btn.startAnimation(fromBottom);
            floatingBtn.startAnimation(rotateOpen);
        } else {
            setting_btn.startAnimation(toBottom);
            contact_btn.startAnimation(toBottom);
            chat_btn.startAnimation(toBottom);
            floatingBtn.startAnimation(rotateClose);
        }
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout,fragment);
        fragmentTransaction.commit();
    }

    private void getLatestSensorData() {
        RetrofitServer retrofitServer = new RetrofitServer();
        RetrofitInterface retrofitInterface = retrofitServer.Retrofit();

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

        //bao chay
        Call<List<SensorResuilt>> callfire = retrofitInterface.getLatestfireData();
        callfire.enqueue(new Callback<List<SensorResuilt>>() {
            @Override
            public void onResponse(Call<List<SensorResuilt>> call, Response<List<SensorResuilt>> response) {
                List<SensorResuilt> sensorDataList = response.body();
                if (sensorDataList != null && !sensorDataList.isEmpty()) {
                    SensorResuilt latestSensorData = sensorDataList.get(sensorDataList.size() - 1);
                    String fireSensor = latestSensorData.getValue();

                    if (fireSensor.equals("1")) {
                        NotificationManager notificationManager = getSystemService(NotificationManager.class);
                        int notificationId = 1;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "My Channel", NotificationManager.IMPORTANCE_HIGH);
                            channel.setDescription("My channel description");
                            notificationManager.createNotificationChannel(channel);
                        }
                        Intent notificationIntent = new Intent(getApplicationContext(), DashboardActivity.class);
                        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                                .setSmallIcon(R.drawable.ic_fire_2)
                                .setContentTitle(getApplicationContext().getString(R.string.warning))
                                .setContentText(getApplicationContext().getString(R.string.fireAlarm))
                                .setContentIntent(pendingIntent)
                                .setAutoCancel(true)
                                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM))
                                .setPriority(NotificationCompat.PRIORITY_MAX)
                                .setDefaults(Notification.DEFAULT_ALL);

                        notificationManager.notify(notificationId, builder.build());

                        //test
                        listNotify = new ArrayList<>();
                        listNotify.add(new Notification_model(2,DashboardActivity.this.getString(R.string.warning2),DashboardActivity.this.getString(R.string.fireAlarm),time,date));

                        sharedPreferences = getSharedPreferences("SaveInfo",MODE_PRIVATE);

                        // Chuyển đổi danh sách thành chuỗi JSON
                        Gson gson = new Gson();
                        String json = gson.toJson(listNotify);

                        // Lưu chuỗi JSON vào SharedPreferences
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("notify", json);
                        editor.commit();
                    }

                    sharedPreferences = getSharedPreferences("SaveInfo",MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("fireSensor", fireSensor);
                    editor.commit();
                }
            }

            @Override
            public void onFailure(Call<List<SensorResuilt>> call, Throwable t) {
                Toast.makeText(DashboardActivity.this, t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });

        //Độ ẩm đất------------------------------------------

        Call<List<SensorResuilt>> callMois = retrofitInterface.getLatestMoisData();
        callMois.enqueue(new Callback<List<SensorResuilt>>() {
            @Override
            public void onResponse(Call<List<SensorResuilt>> call, Response<List<SensorResuilt>> response) {
                List<SensorResuilt> sensorDataList = response.body();
                if (sensorDataList != null && !sensorDataList.isEmpty()) {
                    SensorResuilt latestSensorData = sensorDataList.get(sensorDataList.size() - 1);
                    String moisSensor = latestSensorData.getValue();

                    Handler handler = new Handler();
                    if (Double.parseDouble(latestSensorData.getValue()) < 50.0) {
                        NotificationManager notificationManager = getSystemService(NotificationManager.class);
                        int notificationId = 2;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            NotificationChannel channel = new NotificationChannel(CHANNEL_ID_1, "My Channel 1", NotificationManager.IMPORTANCE_HIGH);
                            channel.setDescription("My channel description");
                            notificationManager.createNotificationChannel(channel);
                        }
                        Intent notificationIntent = new Intent(getApplicationContext(), DashboardActivity.class);
                        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID_1)
                                .setSmallIcon(R.drawable.ic_notifications)
                                .setContentTitle(getApplicationContext().getString(R.string.warning2))
                                .setContentText(getApplicationContext().getString(R.string.lowSoil))
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
                        listNotify.add(new Notification_model(2,DashboardActivity.this.getString(R.string.warning2),DashboardActivity.this.getString(R.string.lowSoil),time,date));

                        sharedPreferences = getSharedPreferences("SaveInfo",MODE_PRIVATE);

                        // Chuyển đổi danh sách thành chuỗi JSON
                        Gson gson = new Gson();
                        String json = gson.toJson(listNotify);

                        // Lưu chuỗi JSON vào SharedPreferences
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("notifySoil", json);
                        editor.commit();
                    }

                    sharedPreferences = getSharedPreferences("SaveInfo",MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("moisSensor", moisSensor);
                    editor.commit();
                }
            }

            @Override
            public void onFailure(Call<List<SensorResuilt>> call, Throwable t) {
                Toast.makeText(DashboardActivity.this, t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });

        //Mực nước ---------------------------------
        Call<List<SensorResuilt>> callWater = retrofitInterface.getLatestwaterlevelData();
        callWater.enqueue(new Callback<List<SensorResuilt>>() {
            @Override
            public void onResponse(Call<List<SensorResuilt>> call, Response<List<SensorResuilt>> response) {
                List<SensorResuilt> sensorDataList = response.body();
                if (sensorDataList != null && !sensorDataList.isEmpty()) {
                    SensorResuilt latestSensorData = sensorDataList.get(sensorDataList.size() - 1);
                    String waterSensor = latestSensorData.getValue();

                    Handler handler = new Handler();
                    if (Double.parseDouble(latestSensorData.getValue()) < 500) {
                        NotificationManager notificationManager = getSystemService(NotificationManager.class);
                        int notificationId = 3;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            NotificationChannel channel = new NotificationChannel(CHANNEL_ID_2, "My Channel 2", NotificationManager.IMPORTANCE_HIGH);
                            channel.setDescription("My channel description");
                            notificationManager.createNotificationChannel(channel);
                        }
                        Intent notificationIntent = new Intent(getApplicationContext(), DashboardActivity.class);
                        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID_2)
                                .setSmallIcon(R.drawable.ic_notifications)
                                .setContentTitle(getApplicationContext().getString(R.string.warning2))
                                .setContentText(getApplicationContext().getString(R.string.lowWater))
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
                        listNotify.add(new Notification_model(2,DashboardActivity.this.getString(R.string.warning2),DashboardActivity.this.getString(R.string.lowWater),time,date));

                        sharedPreferences = getSharedPreferences("SaveInfo",MODE_PRIVATE);

                        // Chuyển đổi danh sách thành chuỗi JSON
                        Gson gson = new Gson();
                        String json = gson.toJson(listNotify);

                        // Lưu chuỗi JSON vào SharedPreferences
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("notifyWater", json);
                        editor.commit();
                    } else if (Double.parseDouble(latestSensorData.getValue()) > 1000) {
                        NotificationManager notificationManager = getSystemService(NotificationManager.class);
                        int notificationId = 4;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            NotificationChannel channel = new NotificationChannel(CHANNEL_ID_3, "My Channel 3", NotificationManager.IMPORTANCE_HIGH);
                            channel.setDescription("My channel description");
                            notificationManager.createNotificationChannel(channel);
                        }
                        Intent notificationIntent = new Intent(getApplicationContext(), DashboardActivity.class);
                        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID_3)
                                .setSmallIcon(R.drawable.ic_notifications)
                                .setContentTitle(getApplicationContext().getString(R.string.warning2))
                                .setContentText(getApplicationContext().getString(R.string.highWater))
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
                        listNotify.add(new Notification_model(2,DashboardActivity.this.getString(R.string.warning2),DashboardActivity.this.getString(R.string.highWater),time,date));

                        sharedPreferences = getSharedPreferences("SaveInfo",MODE_PRIVATE);

                        // Chuyển đổi danh sách thành chuỗi JSON
                        Gson gson = new Gson();
                        String json = gson.toJson(listNotify);

                        // Lưu chuỗi JSON vào SharedPreferences
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("notifyWater2", json);
                        editor.commit();
                    }
                    sharedPreferences = getSharedPreferences("SaveInfo",MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("waterSensor", waterSensor);
                    editor.commit();
                }
            }

            @Override
            public void onFailure(Call<List<SensorResuilt>> call, Throwable t) {
                Toast.makeText(DashboardActivity.this, t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, DashboardActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ((MyApplication) getApplicationContext()).removeActivity(this);
    }
}