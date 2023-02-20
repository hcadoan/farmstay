package com.example.login_app;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.HashMap;
import java.util.List;

import model.LoginResult;
import model.RetrofitInterface;
import model.RetrofitServer;
import model.SensorResuilt;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    EditText emailText, passwordText;
    Button loginBtn, signupBtn;
    TextView forgotPassword;
    CheckBox cbSave;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailText = findViewById(R.id.email);
        passwordText = findViewById(R.id.password);
        loginBtn = findViewById(R.id.loginBtn);
        signupBtn = findViewById(R.id.signupBtn);
        forgotPassword = findViewById(R.id.forgotPassword);
        cbSave = findViewById(R.id.cbSave);

        RetrofitServer retrofitServer = new RetrofitServer();
        RetrofitInterface retrofitInterface = retrofitServer.Retrofit();

        Toast toast = new Toast(LoginActivity.this);
        LayoutInflater inflater = getLayoutInflater();

        //luu thong tin
        sharedPreferences = getSharedPreferences("SaveInfo",MODE_PRIVATE);

        //lay thong tin
        emailText.setText(sharedPreferences.getString("email",""));
        passwordText.setText(sharedPreferences.getString("password",""));
        cbSave.setChecked(sharedPreferences.getBoolean("check", false));

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, EmailActivity.class);
                startActivity(intent);
            }
        });

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signupIntent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(signupIntent);
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailText.getText().toString();
                String password = passwordText.getText().toString();

                if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){
                    View view1 = inflater.inflate(R.layout.layout_toast_error, (ViewGroup) findViewById(R.id.Layout_toast_2));
                    TextView tvMessege = view1.findViewById(R.id.tvMessege2);
                    tvMessege.setText("Enter every details");
                    toast.setView(view1);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.setDuration(Toast.LENGTH_LONG);
                    toast.show();
                }
                else {
                    if(cbSave.isChecked()){
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("check", true);
                        editor.putString("email", email);
                        editor.putString("password", password);
                        editor.commit();
                    } else {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.remove("check");
                        editor.remove("email");
                        editor.remove("password");
                        editor.commit();
                    }

                    HashMap<String, String> map = new HashMap<>();

                    map.put("email", email);
                    map.put("password", password);

                    Call<LoginResult> call = retrofitInterface.executeLogin(map);

                    call.enqueue(new Callback<LoginResult>() {
                        @Override
                        public void onResponse(Call<LoginResult> call, Response<LoginResult> response) {
                            if (response.code() == 200) {

                                LoginResult result = response.body();
                                String name2 = result.getName().toString();
                                String email2 = result.getEmail().toString();

                                View view = inflater.inflate(R.layout.layout_toast_success, (ViewGroup) findViewById(R.id.Layout_toast));
                                TextView tvMessege = view.findViewById(R.id.tvMessege1);
                                tvMessege.setText("Log In Success");
                                toast.setView(view);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.setDuration(Toast.LENGTH_LONG);
                                toast.show();

                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("name2", name2);
                                editor.putString("email2", email2);
                                editor.putString("password2", password);
                                editor.commit();
                                Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                                startActivity(intent);

                            } else if (response.code() == 404) {
                                View view = inflater.inflate(R.layout.layout_toast_error, (ViewGroup) findViewById(R.id.Layout_toast_2));
                                TextView tvMessege = view.findViewById(R.id.tvMessege2);
                                tvMessege.setText("Email is not registered");
                                toast.setView(view);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.setDuration(Toast.LENGTH_LONG);
                                toast.show();
                            } else if (response.code() == 401) {
                                View view = inflater.inflate(R.layout.layout_toast_error, (ViewGroup) findViewById(R.id.Layout_toast_2));
                                TextView tvMessege = view.findViewById(R.id.tvMessege2);
                                tvMessege.setText("Wrong password");
                                toast.setView(view);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.setDuration(Toast.LENGTH_LONG);
                                toast.show();
                            }
                        }

                        @Override
                        public void onFailure(Call<LoginResult> call, Throwable t) {
                            Toast.makeText(LoginActivity.this, t.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
    }
}