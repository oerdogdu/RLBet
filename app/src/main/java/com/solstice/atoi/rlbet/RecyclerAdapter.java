package com.solstice.atoi.rlbet;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Atoi on 24.09.2016.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.CrimeHolder> implements View.OnClickListener{

    private static ArrayList<Bets> betList;
    private Context c;
    private Handler handler;


    @Override
    public void onClick(View v) {
        Log.d("recyc", "clicked");
    }

    public static class CrimeHolder extends RecyclerView.ViewHolder {
        public TextView txtTeam1, txtTeam2, txtX1, txtX2;
        public CrimeHolder(View itemView) {
            super(itemView);
            txtTeam1 = (TextView) itemView.findViewById(R.id.item_team1);
            txtTeam2 = (TextView) itemView.findViewById(R.id.item_team2);
            txtX1 = (TextView) itemView.findViewById(R.id.item_x1);
            txtX2 = (TextView) itemView.findViewById(R.id.item_x2);
        }
    }

    public RecyclerAdapter(ArrayList<Bets> betList, Context c) {
        this.c = c;
        this.betList = betList;
    }

    @Override
    public CrimeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_item_row, parent, false);
        return new CrimeHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(CrimeHolder holder, int position) {
        for(Bets b : betList) {
            holder.txtTeam1.setText(b.getTeam1().toString());
            holder.txtTeam2.setText((b.getTeam2()).toString());
            holder.txtX1.setText(String.valueOf(b.getX1()).toString());
            holder.txtX2.setText(String.valueOf(b.getX2()).toString());
        }
    }

    public void refresh(ArrayList<Bets> bets) {
        betList = new ArrayList<>(bets);
        handler = new Handler(Looper.getMainLooper());
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                handler.post(new Runnable() { // This thread runs in the UI
                    @Override
                    public void run() {
                        notifyDataSetChanged();
                    }
                });
            }
        };
        new Thread(runnable).start();
    }

    @Override
    public int getItemCount() {
        if(betList!=null) {
            return betList.size();
        }
        else {
            return 0;
        }
    }

}
