package com.example.login_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class Farm_1Activity extends AppCompatActivity {

    ImageView imback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farm1);

        imback = findViewById(R.id.imBack4);
        imback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Farm_1Activity.this, DashboardActivity.class);
                startActivity(intent);
            }
        });
    }
}