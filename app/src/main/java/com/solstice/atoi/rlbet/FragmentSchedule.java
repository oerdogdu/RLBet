package com.solstice.atoi.rlbet;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Atoi on 15.09.2016.
 */
public class FragmentSchedule extends Fragment{

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private static RecyclerAdapter mAdapter;
    private static DBAdapter db;
    private static ArrayList<Bets> betList = new ArrayList<>();



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_schedule, container, false);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);
        mLinearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        db = new DBAdapter(getContext());
        for(Bets b : db.getSchedule()) {
            betList.add(b);
        }
        mAdapter = new RecyclerAdapter(betList, getActivity());
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        return v;
    }

    public static void refreshList() {
        for(Bets b : db.getSchedule()) {
            betList.add(b);
        }
        Log.d("lala", betList.size()+"");
        mAdapter.refresh(betList);
    }

    public FragmentSchedule() {

    }

    public static FragmentSchedule newInstance(String text) {

        FragmentSchedule f = new FragmentSchedule();
        return f;
    }
}
