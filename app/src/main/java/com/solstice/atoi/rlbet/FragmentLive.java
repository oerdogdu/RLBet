package com.solstice.atoi.rlbet;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Atoi on 15.09.2016.
 */
public class FragmentLive extends Fragment{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_live, container, false);

        return v;
    }

    public static FragmentSchedule newInstance() {

        FragmentSchedule f = new FragmentSchedule();
        return f;
    }
}
