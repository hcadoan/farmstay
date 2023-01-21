package com.example.login_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import adapter.ViewPagerAdapter;
import me.relex.circleindicator.CircleIndicator;

public class OnboardingActivity extends AppCompatActivity {

    private TextView tvSkip;
    private Button btnNext;
    private ViewPager viewPager;
    private CircleIndicator circleIndicator;
    private LinearLayout layoutBottom;
    private RelativeLayout layoutNextSkip;

    private ViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        anhXa();

        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(viewPagerAdapter);

        circleIndicator.setViewPager(viewPager);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position == 2){
                    layoutBottom.setVisibility(View.GONE);
                } else {
                    layoutBottom.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void anhXa(){
        tvSkip = findViewById(R.id.tvSkip);
        btnNext = findViewById(R.id.btn_next);
        viewPager = findViewById(R.id.viewPager);
        circleIndicator = findViewById(R.id.circle_Indicator);
        layoutBottom = findViewById(R.id.layout_bottom);
        layoutNextSkip = findViewById(R.id.layout_nextSkip);

        tvSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(2);
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(viewPager.getCurrentItem() < 2){
                    viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                }
            }
        });
    }
}