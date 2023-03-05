package com.example.login_app;

import android.app.Activity;
import android.app.Application;

import androidx.fragment.app.Fragment;

import java.util.LinkedList;
import java.util.List;

public class MyApplication extends Application {

    private List<Activity> mActivityList = new LinkedList<>();
    private List<Fragment> mFragmentList = new LinkedList<>();

    public void addActivity(Activity activity) {
        mActivityList.add(activity);
    }

    public void removeActivity(Activity activity) {
        mActivityList.remove(activity);
    }

    public void addFragment(Fragment fragment) {
        mFragmentList.add(fragment);
    }

    public void removeFragment(Fragment fragment) {
        mFragmentList.remove(fragment);
    }

    public void recreateAll() {
        for (Activity activity : mActivityList) {
            activity.recreate();
        }
        for (Fragment fragment : mFragmentList) {
            fragment.getActivity().recreate();
        }
    }

}
