package com.solstice.atoi.rlbet;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.Locale;

/**
 * Created by Atoi on 15.09.2016.
 */
public class ViewPagerAdapter extends FragmentPagerAdapter{
    private Context _context;

    public ViewPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        _context = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new FragmentSchedule();
            case 1:
                return new FragmentLive();
        }
        return null;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Locale l = Locale.getDefault();
        switch (position) {
            case 0:
                return _context.getResources().getString(R.string.schedule).toUpperCase(l);
            case 1:
                return _context.getResources().getString(R.string.live).toUpperCase(l);
        }
        return null;
    }

}
