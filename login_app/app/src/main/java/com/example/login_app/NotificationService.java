package com.example.login_app;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;

import androidx.annotation.Nullable;

import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import model.Notification_model;
import api.RetrofitInterface;
import api.RetrofitServer;

public class NotificationService extends Service {

    private Timer timer;

    SharedPreferences sharedPreferences;

    public static final String CHANNEL_ID = "farmstay";
    public static final String CHANNEL_ID_1 = "SoilMoisture";
    public static final String CHANNEL_ID_2 = "WaterLevel";
    public static final String CHANNEL_ID_3 = "WaterLevel2";
    private List<Notification_model> listNotify;

    @Override
    public void onCreate() {
        super.onCreate();
        // Khởi tạo timer để định kỳ gọi hàm setNotificationAlarm
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                setNotificationAlarm();
            }
        }, 0, 5000); // Gọi hàm setNotificationAlarm mỗi 5 giay
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Intent notificationServiceIntent = new Intent(this, NotificationService.class);
        startService(notificationServiceIntent);

        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // Hủy timer khi service bị stop
        timer.cancel();
    }

    private void setNotificationAlarm() {
        // Gọi API và đặt thông báo ở đây
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

//        //bao chay
//        Call<List<SensorResuilt>> callfire = retrofitInterface.getLatestfireData();
//        callfire.enqueue(new Callback<List<SensorResuilt>>() {
//            @Override
//            public void onResponse(Call<List<SensorResuilt>> call, Response<List<SensorResuilt>> response) {
//                List<SensorResuilt> sensorDataList = response.body();
//                if (sensorDataList != null && !sensorDataList.isEmpty()) {
//                    SensorResuilt latestSensorData = sensorDataList.get(sensorDataList.size() - 1);
//                    String fireSensor = latestSensorData.getValue();
//
//                    if (fireSensor.equals("1")) {
//                        NotificationManager notificationManager = getSystemService(NotificationManager.class);
//                        int notificationId = 1;
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "My Channel", NotificationManager.IMPORTANCE_HIGH);
//                            channel.setDescription("My channel description");
//                            notificationManager.createNotificationChannel(channel);
//                        }
//                        Intent notificationIntent = new Intent(getApplicationContext(), DashboardActivity.class);
//                        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//                        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
//                                .setSmallIcon(R.drawable.ic_fire_2)
//                                .setContentTitle(getApplicationContext().getString(R.string.warning))
//                                .setContentText(getApplicationContext().getString(R.string.fireAlarm))
//                                .setContentIntent(pendingIntent)
//                                .setAutoCancel(true)
//                                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM))
//                                .setPriority(NotificationCompat.PRIORITY_MAX)
//                                .setDefaults(Notification.DEFAULT_ALL);
//
//                        notificationManager.notify(notificationId, builder.build());
//
//                        //test
//                        listNotify = new ArrayList<>();
//                        listNotify.add(new Notification_model(2, NotificationService.this.getString(R.string.warning2), NotificationService.this.getString(R.string.fireAlarm),time,date));
//
//                        sharedPreferences = getSharedPreferences("SaveInfo",MODE_PRIVATE);
//
//                        // Chuyển đổi danh sách thành chuỗi JSON
//                        Gson gson = new Gson();
//                        String json = gson.toJson(listNotify);
//
//                        // Lưu chuỗi JSON vào SharedPreferences
//                        SharedPreferences.Editor editor = sharedPreferences.edit();
//                        editor.putString("notify", json);
//                        editor.commit();
//                    }
//
//                    sharedPreferences = getSharedPreferences("SaveInfo",MODE_PRIVATE);
//                    SharedPreferences.Editor editor = sharedPreferences.edit();
//                    editor.putString("fireSensor", fireSensor);
//                    editor.commit();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<SensorResuilt>> call, Throwable t) {
//                Toast.makeText(NotificationService.this, t.getMessage(),
//                        Toast.LENGTH_LONG).show();
//            }
//        });
//
//        //Độ ẩm đất------------------------------------------
//
//        Call<List<SensorResuilt>> callMois = retrofitInterface.getLatestMoisData();
//        callMois.enqueue(new Callback<List<SensorResuilt>>() {
//            @Override
//            public void onResponse(Call<List<SensorResuilt>> call, Response<List<SensorResuilt>> response) {
//                List<SensorResuilt> sensorDataList = response.body();
//                if (sensorDataList != null && !sensorDataList.isEmpty()) {
//                    SensorResuilt latestSensorData = sensorDataList.get(sensorDataList.size() - 1);
//                    String moisSensor = latestSensorData.getValue();
//
//                    Handler handler = new Handler();
//                    if (Double.parseDouble(latestSensorData.getValue()) < 50.0) {
//                        NotificationManager notificationManager = getSystemService(NotificationManager.class);
//                        int notificationId = 2;
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                            NotificationChannel channel = new NotificationChannel(CHANNEL_ID_1, "My Channel 1", NotificationManager.IMPORTANCE_HIGH);
//                            channel.setDescription("My channel description");
//                            notificationManager.createNotificationChannel(channel);
//                        }
//                        Intent notificationIntent = new Intent(getApplicationContext(), DashboardActivity.class);
//                        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//                        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID_1)
//                                .setSmallIcon(R.drawable.ic_notifications)
//                                .setContentTitle(getApplicationContext().getString(R.string.warning2))
//                                .setContentText(getApplicationContext().getString(R.string.lowSoil))
//                                .setContentIntent(pendingIntent)
//                                .setAutoCancel(true)
//                                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM))
//                                .setPriority(NotificationCompat.PRIORITY_HIGH)
//                                .setDefaults(Notification.DEFAULT_ALL);
//
//                        // Delay 3 seconds before showing the notification
//                        handler.postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                notificationManager.notify(notificationId, builder.build());
//                            }
//                        }, 3000);
//
//                        //test
//                        listNotify = new ArrayList<>();
//                        listNotify.add(new Notification_model(2, NotificationService.this.getString(R.string.warning2), NotificationService.this.getString(R.string.lowSoil),time,date));
//
//                        sharedPreferences = getSharedPreferences("SaveInfo",MODE_PRIVATE);
//
//                        // Chuyển đổi danh sách thành chuỗi JSON
//                        Gson gson = new Gson();
//                        String json = gson.toJson(listNotify);
//
//                        // Lưu chuỗi JSON vào SharedPreferences
//                        SharedPreferences.Editor editor = sharedPreferences.edit();
//                        editor.putString("notifySoil", json);
//                        editor.commit();
//                    }
//
//                    sharedPreferences = getSharedPreferences("SaveInfo",MODE_PRIVATE);
//                    SharedPreferences.Editor editor = sharedPreferences.edit();
//                    editor.putString("moisSensor", moisSensor);
//                    editor.commit();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<SensorResuilt>> call, Throwable t) {
//                Toast.makeText(NotificationService.this, t.getMessage(),
//                        Toast.LENGTH_LONG).show();
//            }
//        });
//
//        //Mực nước ---------------------------------
//        Call<List<SensorResuilt>> callWater = retrofitInterface.getLatestwaterlevelData();
//        callWater.enqueue(new Callback<List<SensorResuilt>>() {
//            @Override
//            public void onResponse(Call<List<SensorResuilt>> call, Response<List<SensorResuilt>> response) {
//                List<SensorResuilt> sensorDataList = response.body();
//                if (sensorDataList != null && !sensorDataList.isEmpty()) {
//                    SensorResuilt latestSensorData = sensorDataList.get(sensorDataList.size() - 1);
//                    String waterSensor = latestSensorData.getValue();
//
//                    Handler handler = new Handler();
//                    if (Double.parseDouble(latestSensorData.getValue()) < 500) {
//                        NotificationManager notificationManager = getSystemService(NotificationManager.class);
//                        int notificationId = 3;
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                            NotificationChannel channel = new NotificationChannel(CHANNEL_ID_2, "My Channel 2", NotificationManager.IMPORTANCE_HIGH);
//                            channel.setDescription("My channel description");
//                            notificationManager.createNotificationChannel(channel);
//                        }
//                        Intent notificationIntent = new Intent(getApplicationContext(), DashboardActivity.class);
//                        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//                        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID_2)
//                                .setSmallIcon(R.drawable.ic_notifications)
//                                .setContentTitle(getApplicationContext().getString(R.string.warning2))
//                                .setContentText(getApplicationContext().getString(R.string.lowWater))
//                                .setContentIntent(pendingIntent)
//                                .setAutoCancel(true)
//                                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM))
//                                .setPriority(NotificationCompat.PRIORITY_HIGH)
//                                .setDefaults(Notification.DEFAULT_ALL);
//
//                        // Delay 3 seconds before showing the notification
//                        handler.postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                notificationManager.notify(notificationId, builder.build());
//                            }
//                        }, 3000);
//
//                        //test
//                        listNotify = new ArrayList<>();
//                        listNotify.add(new Notification_model(2, NotificationService.this.getString(R.string.warning2), NotificationService.this.getString(R.string.lowWater),time,date));
//
//                        sharedPreferences = getSharedPreferences("SaveInfo",MODE_PRIVATE);
//
//                        // Chuyển đổi danh sách thành chuỗi JSON
//                        Gson gson = new Gson();
//                        String json = gson.toJson(listNotify);
//
//                        // Lưu chuỗi JSON vào SharedPreferences
//                        SharedPreferences.Editor editor = sharedPreferences.edit();
//                        editor.putString("notifyWater", json);
//                        editor.commit();
//                    } else if (Double.parseDouble(latestSensorData.getValue()) > 1000) {
//                        NotificationManager notificationManager = getSystemService(NotificationManager.class);
//                        int notificationId = 4;
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                            NotificationChannel channel = new NotificationChannel(CHANNEL_ID_3, "My Channel 3", NotificationManager.IMPORTANCE_HIGH);
//                            channel.setDescription("My channel description");
//                            notificationManager.createNotificationChannel(channel);
//                        }
//                        Intent notificationIntent = new Intent(getApplicationContext(), DashboardActivity.class);
//                        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//                        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID_3)
//                                .setSmallIcon(R.drawable.ic_notifications)
//                                .setContentTitle(getApplicationContext().getString(R.string.warning2))
//                                .setContentText(getApplicationContext().getString(R.string.highWater))
//                                .setContentIntent(pendingIntent)
//                                .setAutoCancel(true)
//                                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM))
//                                .setPriority(NotificationCompat.PRIORITY_HIGH)
//                                .setDefaults(Notification.DEFAULT_ALL);
//
//                        // Delay 3 seconds before showing the notification
//                        handler.postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                notificationManager.notify(notificationId, builder.build());
//                            }
//                        }, 3000);
//
//                        //test
//                        listNotify = new ArrayList<>();
//                        listNotify.add(new Notification_model(2, NotificationService.this.getString(R.string.warning2), NotificationService.this.getString(R.string.highWater),time,date));
//
//                        sharedPreferences = getSharedPreferences("SaveInfo",MODE_PRIVATE);
//
//                        // Chuyển đổi danh sách thành chuỗi JSON
//                        Gson gson = new Gson();
//                        String json = gson.toJson(listNotify);
//
//                        // Lưu chuỗi JSON vào SharedPreferences
//                        SharedPreferences.Editor editor = sharedPreferences.edit();
//                        editor.putString("notifyWater2", json);
//                        editor.commit();
//                    }
//                    sharedPreferences = getSharedPreferences("SaveInfo",MODE_PRIVATE);
//                    SharedPreferences.Editor editor = sharedPreferences.edit();
//                    editor.putString("waterSensor", waterSensor);
//                    editor.commit();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<SensorResuilt>> call, Throwable t) {
//                Toast.makeText(NotificationService.this, t.getMessage(),
//                        Toast.LENGTH_LONG).show();
//            }
//        });
    }
}
