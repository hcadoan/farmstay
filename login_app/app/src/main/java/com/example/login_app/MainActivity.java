package com.example.login_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    EditText nameText, emailText, passwordText;
    Button signupBtn, loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        nameText = findViewById(R.id.username);
        emailText = findViewById(R.id.email);
        passwordText = findViewById(R.id.password);
        signupBtn = findViewById(R.id.signupBtn);
        loginBtn = findViewById(R.id.loginBtn);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(loginIntent);
            }
        });

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = nameText.getText().toString();
                String email = emailText.getText().toString();
                String password = emailText.getText().toString();
                if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(name)){
                    Toast.makeText(MainActivity.this, "Enter every details", Toast.LENGTH_SHORT).show();
                }
                else if(password.length() < 6){
                    Toast.makeText(MainActivity.this, "Password must be 6 characters", Toast.LENGTH_SHORT).show();
                }
                else{
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(MainActivity.this, "Account Created Successfully", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(MainActivity.this, DashboardActivity.class));
                                    }
                                    else {
                                        Toast.makeText(MainActivity.this, "Couldn't Create an account", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });
    }
}