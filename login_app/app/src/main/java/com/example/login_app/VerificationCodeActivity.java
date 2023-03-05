package com.example.login_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chaos.view.PinView;

import java.util.HashMap;

import model.LoginResult;
import model.RetrofitInterface;
import model.RetrofitServer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerificationCodeActivity extends BaseActivity {

    PinView pinViewCode;
    Button btnVerification, btnOk;
    ImageView imBack, imback2;
    TextView tvEmail, tvErrorpass2, tvErrorpass3;
    ConstraintLayout layout1, layout2;
    EditText etNewpass, etComfirmNewpass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Thiết lập Activity fullscreen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_verification_code);

        pinViewCode = findViewById(R.id.pinviewCode);
        btnVerification = findViewById(R.id.btnVerification);
        imBack = findViewById(R.id.imBack);
        tvEmail = findViewById(R.id.tvEmail);

        imback2 = findViewById(R.id.imBack2);
        etNewpass = findViewById(R.id.etNewpass);
        etComfirmNewpass = findViewById(R.id.etComfirmNewpass);
        tvErrorpass2 = findViewById(R.id.tvErrorPass2);
        tvErrorpass3 = findViewById(R.id.tvErrorPass3);
        btnOk = findViewById(R.id.btnOk);

        layout1 = findViewById(R.id.constraintLayout1);
        layout2 = findViewById(R.id.constraintLayout2);

        String email = getIntent().getStringExtra("emailVerify");
        tvEmail.setText(email);

        layout2.setVisibility(View.GONE);
        tvErrorpass2.setVisibility(View.GONE);
        tvErrorpass3.setVisibility(View.GONE);
        imBack.setVisibility(View.VISIBLE);
        imback2.setVisibility(View.GONE);

        imBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VerificationCodeActivity.this, EmailActivity.class);
                startActivity(intent);
            }
        });

        etNewpass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String newPass = etNewpass.getText().toString().trim();
                if (newPass.length() < 6) {
                    tvErrorpass2.setVisibility(View.VISIBLE);
                    tvErrorpass2.setText("Password must be 6 characters");
                    return;
                } else {
                    tvErrorpass2.setVisibility(View.GONE);
                }
            }
        });

        etComfirmNewpass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String comfirmPass = etComfirmNewpass.getText().toString().trim();
                String newPass = etNewpass.getText().toString().trim();
                if (!comfirmPass.equals(newPass)) {
                    tvErrorpass3.setVisibility(View.VISIBLE);
                    tvErrorpass3.setText("Password not match");
                    return;
                } else {
                    tvErrorpass3.setVisibility(View.GONE);
                }
            }
        });

        Toast toast = new Toast(VerificationCodeActivity.this);
        LayoutInflater inflater = getLayoutInflater();

        RetrofitServer retrofitServer = new RetrofitServer();
        RetrofitInterface retrofitInterface = retrofitServer.Retrofit();

        btnVerification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String verifyCode = pinViewCode.getText().toString();
                String email = tvEmail.getText().toString();

                if(TextUtils.isEmpty(verifyCode)){
                    View view1 = inflater.inflate(R.layout.layout_toast_error, (ViewGroup) findViewById(R.id.Layout_toast_2));
                    TextView tvMessege = view1.findViewById(R.id.tvMessege2);
                    tvMessege.setText("Enter every details");
                    toast.setView(view1);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.setDuration(Toast.LENGTH_LONG);
                    toast.show();
                }
                else {
                    HashMap<String, String> map = new HashMap<>();

                    map.put("email", email);
                    map.put("resetPasswordCode", verifyCode);

                    Call<Void> callReset = retrofitInterface.verifyresetpassword(map);
                    callReset.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.code() == 400) {
                                View view1 = inflater.inflate(R.layout.layout_toast_success, (ViewGroup) findViewById(R.id.Layout_toast));
                                TextView tvMessege = view1.findViewById(R.id.tvMessege1);
                                tvMessege.setText("Successful verification");
                                toast.setView(view1);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.setDuration(Toast.LENGTH_LONG);
                                toast.show();

                                layout1.setVisibility(View.GONE);
                                layout2.setVisibility(View.VISIBLE);
                                imBack.setVisibility(View.GONE);
                                imback2.setVisibility(View.VISIBLE);

                            } else if (response.code() == 404){
                                View view1 = inflater.inflate(R.layout.layout_toast_error, (ViewGroup) findViewById(R.id.Layout_toast_2));
                                TextView tvMessege = view1.findViewById(R.id.tvMessege2);
                                tvMessege.setText("Verification code is incorrect");
                                toast.setView(view1);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.setDuration(Toast.LENGTH_LONG);
                                toast.show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Toast.makeText(VerificationCodeActivity.this, t.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = tvEmail.getText().toString();
                String newPassword = etComfirmNewpass.getText().toString();

                if(tvErrorpass2.getVisibility() == View.VISIBLE || tvErrorpass3.getVisibility() == View.VISIBLE ||
                        (tvErrorpass2.getVisibility() == View.VISIBLE && tvErrorpass3.getVisibility() == View.VISIBLE)) {
                    View view1 = inflater.inflate(R.layout.layout_toast_error, (ViewGroup) findViewById(R.id.Layout_toast_2));
                    TextView tvMessege = view1.findViewById(R.id.tvMessege2);
                    tvMessege.setText("Check your new password");
                    toast.setView(view1);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.setDuration(Toast.LENGTH_LONG);
                    toast.show();
                } else {
                    HashMap<String, String> map = new HashMap<>();

                    map.put("email", email);
                    map.put("newPassword", newPassword);

                    Call<Void> callReset = retrofitInterface.resetpassword(map);
                    callReset.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.code() == 500){
                                View view1 = inflater.inflate(R.layout.layout_toast_error, (ViewGroup) findViewById(R.id.Layout_toast_2));
                                TextView tvMessege = view1.findViewById(R.id.tvMessege2);
                                tvMessege.setText("Error resetting password");
                                toast.setView(view1);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.setDuration(Toast.LENGTH_LONG);
                                toast.show();
                            } else if (response.code() == 200){
                                View view1 = inflater.inflate(R.layout.layout_toast_success, (ViewGroup) findViewById(R.id.Layout_toast));
                                TextView tvMessege = view1.findViewById(R.id.tvMessege1);
                                tvMessege.setText("Password reset successfully");
                                toast.setView(view1);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.setDuration(Toast.LENGTH_LONG);
                                toast.show();

                                Intent intent = new Intent(VerificationCodeActivity.this, LoginActivity.class);
                                startActivity(intent);
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Toast.makeText(VerificationCodeActivity.this, t.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });

    }
}