package com.solstice.atoi.rlbet;

import android.content.Context;
import android.database.ContentObservable;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;

import java.util.ArrayList;

/**
 * Created by Atoi on 23.09.2016.
 */
public class DBObserver extends ContentObserver{
    private DBAdapter db = null;
    private static ArrayList<Bets> bets;
    /**
     * Creates a content observer.
     *
     * @param handler The handler to run {@link #onChange} on, or null if none.
     */
    public DBObserver(Handler handler, Context c) {
        super(handler);
        db = new DBAdapter(c);
    }

    @Override
    public void onChange(boolean selfChange) {
        this.onChange(selfChange,null);
    }

    @Override
    public void onChange(boolean selfChange, Uri uri) {
        db.open();

    }

    public static ArrayList<Bets> getBets() {
        return bets;
    }
}
