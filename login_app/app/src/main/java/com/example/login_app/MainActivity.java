package com.example.login_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import model.LoginResult;
import model.RetrofitInterface;
import model.RetrofitServer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    EditText nameText, emailText, passwordText, password_2Text;
    Button signupBtn, loginBtn;
    TextView tvErrorEmail, tvErrorPass, tvErrorComfirmPass;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nameText = findViewById(R.id.username);
        emailText = findViewById(R.id.email);
        passwordText = findViewById(R.id.password);
        password_2Text = findViewById(R.id.password_2);
        signupBtn = findViewById(R.id.signupBtn);
        loginBtn = findViewById(R.id.loginBtn);
        tvErrorEmail = findViewById(R.id.tvErrorEmail);
        tvErrorPass = findViewById(R.id.tvErrorPass);
        tvErrorComfirmPass = findViewById(R.id.tvErrorPassComfirm);

        RetrofitServer retrofitServer = new RetrofitServer();
        RetrofitInterface retrofitInterface = retrofitServer.Retrofit();

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(loginIntent);
            }
        });

        Toast toast = new Toast(MainActivity.this);
        LayoutInflater inflater = getLayoutInflater();

        tvErrorEmail.setVisibility(View.GONE);
        tvErrorPass.setVisibility(View.GONE);
        tvErrorComfirmPass.setVisibility(View.GONE);

        sharedPreferences = getSharedPreferences("SaveInfo",MODE_PRIVATE);

        emailText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String email = emailText.getText().toString().trim();
                if (!isValidEmail(email)) {
                    tvErrorEmail.setVisibility(View.VISIBLE);
                    tvErrorEmail.setText("Invalid email");
                    return;
                } else {
                    tvErrorEmail.setVisibility(View.GONE);
                }
            }
        });

        password_2Text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String password2 = password_2Text.getText().toString().trim();
                if (password2.length() < 6) {
                    tvErrorPass.setVisibility(View.VISIBLE);
                    tvErrorPass.setText("Password must be 6 characters");
                    return;
                } else {
                    tvErrorPass.setVisibility(View.GONE);
                }
            }
        });

        passwordText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String password = passwordText.getText().toString().trim();
                String password2 = password_2Text.getText().toString().trim();
                if (!password.equals(password2)) {
                    tvErrorComfirmPass.setVisibility(View.VISIBLE);
                    tvErrorComfirmPass.setText("Password not match");
                    return;
                } else {
                    tvErrorComfirmPass.setVisibility(View.GONE);
                }
            }
        });

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = nameText.getText().toString();
                String email = emailText.getText().toString();
                String password = passwordText.getText().toString();
                String password_2 = password_2Text.getText().toString();
                if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(name) || TextUtils.isEmpty(password_2)){
                    View view1 = inflater.inflate(R.layout.layout_toast_error, (ViewGroup) findViewById(R.id.Layout_toast_2));
                    TextView tvMessege = view1.findViewById(R.id.tvMessege2);
                    tvMessege.setText("Enter every details");
                    toast.setView(view1);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.setDuration(Toast.LENGTH_LONG);
                    toast.show();
                }
                else if(tvErrorEmail.getVisibility() == View.VISIBLE || tvErrorPass.getVisibility() == View.VISIBLE || tvErrorComfirmPass.getVisibility() == View.VISIBLE
                        || (tvErrorEmail.getVisibility() == View.VISIBLE && tvErrorPass.getVisibility() == View.VISIBLE)
                        || (tvErrorPass.getVisibility() == View.VISIBLE && tvErrorComfirmPass.getVisibility() == View.VISIBLE)
                        || (tvErrorEmail.getVisibility() == View.VISIBLE && tvErrorComfirmPass.getVisibility() == View.VISIBLE)
                        || (tvErrorEmail.getVisibility() == View.VISIBLE && tvErrorPass.getVisibility() == View.VISIBLE && tvErrorComfirmPass.getVisibility() == View.VISIBLE)){
                    View view1 = inflater.inflate(R.layout.layout_toast_error, (ViewGroup) findViewById(R.id.Layout_toast_2));
                    TextView tvMessege = view1.findViewById(R.id.tvMessege2);
                    tvMessege.setText("check registration information");
                    toast.setView(view1);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.setDuration(Toast.LENGTH_LONG);
                    toast.show();
                }

                else{
                    HashMap<String, String> map = new HashMap<>();

                    map.put("name", name);
                    map.put("email", email);
                    map.put("password", password);

                    Call<Void> callSignup = retrofitInterface.executeSignup(map);

                    callSignup.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.code() == 200) {

                                View view = inflater.inflate(R.layout.layout_toast_success, (ViewGroup) findViewById(R.id.Layout_toast));
                                toast.setView(view);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.setDuration(Toast.LENGTH_LONG);
                                toast.show();

                                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                startActivity(intent);

                            } else if (response.code() == 409) {
                                View view = inflater.inflate(R.layout.layout_toast_error, (ViewGroup) findViewById(R.id.Layout_toast_2));
                                TextView tvMessege = view.findViewById(R.id.tvMessege2);
                                tvMessege.setText("Username already exists");
                                toast.setView(view);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.setDuration(Toast.LENGTH_LONG);
                                toast.show();
                            } else if (response.code() == 400) {
                                View view = inflater.inflate(R.layout.layout_toast_error, (ViewGroup) findViewById(R.id.Layout_toast_2));
                                TextView tvMessege = view.findViewById(R.id.tvMessege2);
                                tvMessege.setText("Email already exists");
                                toast.setView(view);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.setDuration(Toast.LENGTH_LONG);
                                toast.show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Toast.makeText(MainActivity.this, t.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });


                }
            }
        });
    }

    private boolean isValidEmail(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}