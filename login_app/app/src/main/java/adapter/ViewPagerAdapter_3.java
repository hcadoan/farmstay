package adapter;

import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.login_app.FarmFragment;
import com.example.login_app.HouseFragment;

public class ViewPagerAdapter_3 extends FragmentStatePagerAdapter {


    public ViewPagerAdapter_3(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new HouseFragment();
            case 1:
                return new FarmFragment();
            default:
                return new HouseFragment();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title = "";
        switch(position){
            case 0:
                title = "Home";
                break;
            case 1:
                title = "Farm";
                break;
        }
        return title;
    }
}
