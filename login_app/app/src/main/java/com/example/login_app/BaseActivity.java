package com.example.login_app;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import model.LoginResult;
import model.RetrofitInterface;
import model.RetrofitServer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BaseActivity extends AppCompatActivity {

    RetrofitServer retrofitServer = new RetrofitServer();
    RetrofitInterface retrofitInterface = retrofitServer.Retrofit();
    Timer timer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String lang = getLanguage();
        setLocale(lang);

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                SharedPreferences sharedPreferences = getSharedPreferences("SaveInfo", MODE_PRIVATE);
                String token = sharedPreferences.getString("token", "");

                Call<LoginResult> call = retrofitInterface.Logout(token);
                call.enqueue(new Callback<LoginResult>() {
                    @Override
                    public void onResponse(Call<LoginResult> call, Response<LoginResult> response) {
                        Log.d("token", String.valueOf(response.code()));

                        if (response.code() == 401) {
                            showSessionExpiredDialog();
                            timer.cancel();
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginResult> call, Throwable t) {
                        Log.e("error", "err");
                    }
                });
            }
        }, 0, 5000);  // update every 1000 milliseconds (1 second)
    }

    // Hàm hiển thị dialog hết phiên đăng nhập
    private void showSessionExpiredDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(BaseActivity.this);
        builder.setMessage(R.string.loginAgain);
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.logIn, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Xử lý đăng nhập lại
                SharedPreferences sharedPreferences = getSharedPreferences("SaveInfo",MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove("notify");
                editor.remove("notifySoil");
                editor.remove("notifyWater");
                editor.remove("notifyWater2");
                editor.remove("token");
                editor.commit();

                Intent intent = new Intent(BaseActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

        SharedPreferences.Editor editor = getSharedPreferences("SaveInfo", MODE_PRIVATE).edit();
        editor.putString("language",lang);
        editor.apply();
    }

    public String getLanguage() {
        SharedPreferences preferences = getSharedPreferences("SaveInfo", Activity.MODE_PRIVATE);
        return preferences.getString("language", "");
    }
}
