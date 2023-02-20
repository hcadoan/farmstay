package com.example.login_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ContactActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        TextView textViewphone = findViewById(R.id.textviewPhone);
        textViewphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phoneNumber = textViewphone.getText().toString();
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumber, null));
                startActivity(intent);
            }
        });
        TextView textViewemail = findViewById(R.id.textViewemail);
        textViewemail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = textViewemail.getText().toString();
                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", email, null));
                startActivity(Intent.createChooser(intent, "Send email..."));
            }
        });
        TextView textViewquery = findViewById(R.id.textViewquery);
        textViewquery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = textViewquery.getText().toString();
                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + phoneNumber));
                startActivity(intent);
            }
        });
        ImageView imageViewfb = findViewById(R.id.imageViewfb);
        imageViewfb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String facebookUrl = "https://www.facebook.com/namanh2209/";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(facebookUrl));
                startActivity(intent);
            }
        });
        ImageView imageViewins = findViewById(R.id.imageViewins);
        imageViewins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String facebookUrl = "https://www.instagram.com/n_anh229/";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(facebookUrl));
                startActivity(intent);
            }
        });
        ImageView imageViewyt = findViewById(R.id.imageViewyt);
        imageViewyt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String facebookUrl = "https://www.youtube.com/watch?v=PR_yVho1Txc&list=RDGMEM6CZm14o9sc-Q22TIneLI8gVMHehotFZ8BGo&index=9";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(facebookUrl));
                startActivity(intent);
            }
        });
        ImageView imageViewweb = findViewById(R.id.imageViewweb);
        imageViewweb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String facebookUrl = "https://www.flaticon.com/";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(facebookUrl));
                startActivity(intent);
            }
        });

        ImageView imback = findViewById(R.id.imBack3);
        imback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ContactActivity.this, DashboardActivity.class);
                startActivity(intent);
            }
        });
    }
}