package com.example.login_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
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

public class ChangePassActivity extends BaseActivity {

    EditText etCurrentPass, etNewpass, etComfirmPass;
    TextView tvError, tvError2;
    Button btnOk;
    ImageView imBack;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Thiết lập Activity fullscreen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_change_pass);

        etCurrentPass = findViewById(R.id.etCurrentPass);
        etNewpass = findViewById(R.id.etNewpass);
        etComfirmPass = findViewById(R.id.etComfirmNewpass);
        tvError = findViewById(R.id.tvErrorPass4);
        tvError2 = findViewById(R.id.tvErrorPass3);
        btnOk = findViewById(R.id.btnOk);
        imBack = findViewById(R.id.imBack2);

        tvError.setVisibility(View.GONE);
        tvError2.setVisibility(View.GONE);

        sharedPreferences = getSharedPreferences("SaveInfo",MODE_PRIVATE);

        imBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChangePassActivity.this, SettingActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
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
                    tvError.setVisibility(View.VISIBLE);
                    tvError.setText(R.string.passwordmustbe6characters);
                    return;
                } else {
                    tvError.setVisibility(View.GONE);
                }
            }
        });

        etComfirmPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String comfirmPass = etComfirmPass.getText().toString().trim();
                String newPass = etNewpass.getText().toString().trim();
                if (!comfirmPass.equals(newPass)) {
                    tvError2.setVisibility(View.VISIBLE);
                    tvError2.setText(R.string.passwordnotmatch);
                    return;
                } else {
                    tvError2.setVisibility(View.GONE);
                }
            }
        });

        Toast toast = new Toast(ChangePassActivity.this);
        LayoutInflater inflater = getLayoutInflater();

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pass = sharedPreferences.getString("password2","");
                String currentPass = etCurrentPass.getText().toString().trim();
                String newPass = etNewpass.getText().toString().trim();
                String comfirmPass = etComfirmPass.getText().toString().trim();

                if(TextUtils.isEmpty(currentPass) || TextUtils.isEmpty(newPass) || TextUtils.isEmpty(comfirmPass)){
                    View view1 = inflater.inflate(R.layout.layout_toast_error, (ViewGroup) findViewById(R.id.Layout_toast_2));
                    TextView tvMessege = view1.findViewById(R.id.tvMessege2);
                    tvMessege.setText("Enter every details");
                    toast.setView(view1);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.setDuration(Toast.LENGTH_LONG);
                    toast.show();
                }
                else if(tvError.getVisibility() == View.VISIBLE || tvError2.getVisibility() == View.VISIBLE ||
                        (tvError.getVisibility() == View.VISIBLE && tvError2.getVisibility() == View.VISIBLE)) {
                    View view1 = inflater.inflate(R.layout.layout_toast_error, (ViewGroup) findViewById(R.id.Layout_toast_2));
                    TextView tvMessege = view1.findViewById(R.id.tvMessege2);
                    tvMessege.setText("Check your new password");
                    toast.setView(view1);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.setDuration(Toast.LENGTH_LONG);
                    toast.show();
                } else if(!currentPass.equals(pass)){
                    View view1 = inflater.inflate(R.layout.layout_toast_error, (ViewGroup) findViewById(R.id.Layout_toast_2));
                    TextView tvMessege = view1.findViewById(R.id.tvMessege2);
                    tvMessege.setText("Current password is incorrect");
                    toast.setView(view1);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.setDuration(Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });
    }
}